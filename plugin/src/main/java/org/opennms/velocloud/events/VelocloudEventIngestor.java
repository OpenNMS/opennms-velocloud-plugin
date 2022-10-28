package org.opennms.velocloud.events;

import java.net.InetAddress;
import java.sql.Date;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.events.EventForwarder;
import org.opennms.integration.api.v1.health.Context;
import org.opennms.integration.api.v1.health.HealthCheck;
import org.opennms.integration.api.v1.health.Response;
import org.opennms.integration.api.v1.health.Status;
import org.opennms.integration.api.v1.health.immutables.ImmutableResponse;
import org.opennms.integration.api.v1.model.MetaData;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.v1.model.immutables.ImmutableEventParameter;
import org.opennms.integration.api.v1.model.immutables.ImmutableInMemoryEvent;
import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.client.api.model.EnterpriseEvent;
import org.opennms.velocloud.client.api.model.ProxyEvent;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.connections.Connection;
import org.opennms.velocloud.connections.ConnectionManager;
import org.opennms.velocloud.requisition.AbstractRequisitionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class VelocloudEventIngestor implements Runnable, HealthCheck {
    private static final Logger LOG = LoggerFactory.getLogger(VelocloudEventIngestor.class);
    public static final String LOGICAL_ID = "logicalId";
    public static final String ENTERPRISE_EVENTS_UEI = "uei.opennms.org/vendor/vmware/velocloud/enterpriseEvents";
    public static final String PROXY_EVENTS_UEI = "uei.opennms.org/vendor/vmware/velocloud/proxyEvents";
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledFuture;
    private long delay = 0L;
    private final ClientManager clientManager;
    private final ConnectionManager connectionManager;
    private final NodeDao nodeDao;
    private final EventForwarder eventForwarder;
    private final static Map<String, Severity> SEVERITY_MAP = new TreeMap<>();

    private Instant lastPoll = null;

    static {
        SEVERITY_MAP.put("EMERGENCY", Severity.CRITICAL);
        SEVERITY_MAP.put("ALERT", Severity.CRITICAL);
        SEVERITY_MAP.put("CRITICAL", Severity.MAJOR);
        SEVERITY_MAP.put("ERROR", Severity.MINOR);
        SEVERITY_MAP.put("WARNING", Severity.WARNING);
        SEVERITY_MAP.put("NOTICE", Severity.NORMAL);
        SEVERITY_MAP.put("INFO", Severity.NORMAL);
        SEVERITY_MAP.put("DEBUG", Severity.NORMAL);
    }

    public VelocloudEventIngestor(final ClientManager clientManager,
                                  final ConnectionManager connectionManager,
                                  final NodeDao nodeDao,
                                  final EventForwarder eventForwarder) {
        this.clientManager = clientManager;
        this.connectionManager = connectionManager;
        this.nodeDao = nodeDao;
        this.eventForwarder = eventForwarder;
    }

    public void init() {
        LOG.debug("Velocloud Event Ingestor is initializing (delay = {}ms).", delay);
        scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(this, delay, delay, TimeUnit.MILLISECONDS);
    }

    public void destroy() {
        LOG.debug("Velocloud Event Ingestor is shutting down.");
        scheduledFuture.cancel(false);
        scheduledFuture = null;
    }

    @Override
    public void run() {
        LOG.debug("Velocloud Event Ingestor is polling for events.");

        final Instant now = Instant.now();

        if (lastPoll == null) {
            lastPoll = now.minus(delay, ChronoUnit.MILLIS);
        }

        for (final String alias : connectionManager.getAliases()) {
            final Optional<Connection> connection = connectionManager.getConnection(alias);

            if (connection.isPresent()) {
                VelocloudApiPartnerClient velocloudApiPartnerClient = null;
                try {
                    velocloudApiPartnerClient = clientManager.getPartnerClient(VelocloudApiClientCredentials.builder()
                            .withOrchestratorUrl(connection.get().getOrchestratorUrl())
                            .withApiKey(connection.get().getApiKey())
                            .build());
                } catch (Exception e) {
                    LOG.debug("Cannot create partner connection for the given credentials.");
                }

                if (velocloudApiPartnerClient != null) {
                    try {
                        final String foreignSource = String.format("%s-%s", AbstractRequisitionProvider.VELOCLOUD_PARNTER_IDENTIFIER, connection.get().getAlias());
                        processPartnerEvents(lastPoll, now, foreignSource, velocloudApiPartnerClient);
                    } catch (Exception e) {
                        LOG.error("Error processing partner events.", e);
                    }
                    continue;
                }

                VelocloudApiCustomerClient velocloudApiCustomerClient = null;
                try {
                    velocloudApiCustomerClient = clientManager.getCustomerClient(VelocloudApiClientCredentials.builder()
                            .withOrchestratorUrl(connection.get().getOrchestratorUrl())
                            .withApiKey(connection.get().getApiKey())
                            .build());
                } catch (Exception e) {
                    LOG.debug("Cannot create customer connection for the given credentials.");
                }

                if (velocloudApiCustomerClient != null) {
                    try {
                        final String foreignSource = String.format("%s-%s", AbstractRequisitionProvider.VELOCLOUD_CUSTOMER_IDENTIFIER, connection.get().getAlias());
                        processCustomerEvents(lastPoll, now, foreignSource, velocloudApiCustomerClient);
                    } catch (Exception e) {
                        LOG.error("Error processing customer events.", e);
                    }
                }
            }
        }

        lastPoll = now.plus(1, ChronoUnit.MILLIS);
    }

    private void processEvent(final String foreignSource, final String foreignId, final ProxyEvent proxyEvent) {
        final Node node = nodeDao.getNodeByCriteria(foreignSource + ":" + foreignId);

        if (node == null) {
            LOG.warn("Ignoring proxy event #{} since node {} cannot be found.", proxyEvent.getId(), foreignSource + ":" + foreignId);
            return;
        }

        final Optional<InetAddress> inetAddress = node.getIpInterfaces().stream().map(iface -> iface.getIpAddress()).findFirst();
        final ImmutableInMemoryEvent.Builder builder = ImmutableInMemoryEvent.newBuilder()
                .setUei(PROXY_EVENTS_UEI)
                .setSource(VelocloudEventIngestor.class.getCanonicalName())
                .setNodeId(node.getId())
                .setSeverity(SEVERITY_MAP.get(proxyEvent.getSeverity()))
                .setInterface(inetAddress.orElse(null));

        builder.addParameter(ImmutableEventParameter.newInstance("gatewayName", proxyEvent.getGatewayName()));
        builder.addParameter(ImmutableEventParameter.newInstance("event", proxyEvent.getEvent()));
        builder.addParameter(ImmutableEventParameter.newInstance("message", proxyEvent.getMessage()));
        builder.addParameter(ImmutableEventParameter.newInstance("severity", proxyEvent.getSeverity()));
        builder.addParameter(ImmutableEventParameter.newInstance("category", proxyEvent.getCategory()));
        builder.addParameter(ImmutableEventParameter.newInstance("proxyUsername", proxyEvent.getProxyUsername()));
        builder.addParameter(ImmutableEventParameter.newInstance("detail", proxyEvent.getDetail()));
        builder.addParameter(ImmutableEventParameter.newInstance("enterpriseName", proxyEvent.getEnterpriseName()));
        builder.addParameter(ImmutableEventParameter.newInstance("networkName", proxyEvent.getNetworkName()));
        builder.addParameter(ImmutableEventParameter.newInstance("id", String.valueOf(proxyEvent.getId())));

        if (proxyEvent.getEventTime() != null) {
            builder.setTime(Date.from(proxyEvent.getEventTime().toInstant()));
            builder.addParameter(ImmutableEventParameter.newInstance("eventTime", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(proxyEvent.getEventTime())));
        } else {
            builder.addParameter(ImmutableEventParameter.newInstance("eventTime", null));
        }

        eventForwarder.sendSync(builder.build());
    }

    private void processEvent(final String foreignSource, final String foreignId, final EnterpriseEvent enterpriseEvent) {
        final Node node = nodeDao.getNodeByCriteria(foreignSource + ":" + foreignId);

        if (node == null) {
            LOG.warn("Ignoring enterprise event #{} since node {} cannot be found.", enterpriseEvent.getId(), foreignSource + ":" + foreignId);
            return;
        }

        final String logicalId = parseJson(enterpriseEvent.getDetail(), LOGICAL_ID);

        final Optional<InetAddress> inetAddress;

        if (!Strings.isNullOrEmpty(logicalId)) {
            inetAddress = node.getIpInterfaces().stream()
                    .filter(iface -> logicalId.equals(getMetadataForKey(LOGICAL_ID, iface.getMetaData())))
                    .map(iface -> iface.getIpAddress()).findFirst();
        } else {
            inetAddress = Optional.empty();
        }

        final ImmutableInMemoryEvent.Builder builder = ImmutableInMemoryEvent.newBuilder()
                .setUei(ENTERPRISE_EVENTS_UEI)
                .setSource(VelocloudEventIngestor.class.getCanonicalName())
                .setNodeId(node.getId())
                .setSeverity(SEVERITY_MAP.get(enterpriseEvent.getSeverity()))
                .setInterface(inetAddress.orElse(null));

        builder.addParameter(ImmutableEventParameter.newInstance("edgeName", enterpriseEvent.getEdgeName()));
        builder.addParameter(ImmutableEventParameter.newInstance("event", enterpriseEvent.getEvent()));
        builder.addParameter(ImmutableEventParameter.newInstance("message", enterpriseEvent.getMessage()));
        builder.addParameter(ImmutableEventParameter.newInstance("severity", enterpriseEvent.getSeverity()));
        builder.addParameter(ImmutableEventParameter.newInstance("category", enterpriseEvent.getCategory()));
        builder.addParameter(ImmutableEventParameter.newInstance("enterpriseUsername", enterpriseEvent.getEnterpriseUsername()));
        builder.addParameter(ImmutableEventParameter.newInstance("detail", enterpriseEvent.getDetail()));
        builder.addParameter(ImmutableEventParameter.newInstance("id", String.valueOf(enterpriseEvent.getId())));

        if (enterpriseEvent.getEventTime() != null) {
            builder.setTime(Date.from(enterpriseEvent.getEventTime().toInstant()));
            builder.addParameter(ImmutableEventParameter.newInstance("eventTime", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(enterpriseEvent.getEventTime())));
        } else {
            builder.addParameter(ImmutableEventParameter.newInstance("eventTime", null));
        }

        eventForwarder.sendSync(builder.build());
    }

    private void processCustomerEvents(final Instant start, final Instant end, final String foreignSource, final VelocloudApiCustomerClient velocloudApiCustomerClient) throws VelocloudApiException {
        final List<EnterpriseEvent> enterpriseEventList = velocloudApiCustomerClient.getEnterpriseEvents(start, end);
        LOG.debug(enterpriseEventList.size() + " enterprise events found.");

        for (final EnterpriseEvent enterpriseEvent : enterpriseEventList) {
            if (!Strings.isNullOrEmpty(enterpriseEvent.getEdgeName())) {
                processEvent(foreignSource, enterpriseEvent.getEdgeName(), enterpriseEvent);
            } else {
                LOG.debug("ignoring enterprise event #" + enterpriseEvent.getId() + " due to missing edgeName.");
            }
        }
    }

    private void processPartnerEvents(final Instant start, final Instant end, final String foreignSource, final VelocloudApiPartnerClient velocloudApiPartnerClient) throws VelocloudApiException {
        final List<ProxyEvent> enterpriseEventList = velocloudApiPartnerClient.getProxyEvents(start, end);
        LOG.debug(enterpriseEventList.size() + " proxy events found.");
        for (final ProxyEvent proxyEvent : enterpriseEventList) {
            if (!Strings.isNullOrEmpty(proxyEvent.getGatewayName())) {
                processEvent(foreignSource, proxyEvent.getGatewayName(), proxyEvent);
            } else {
                LOG.debug("ignoring proxy event #" + proxyEvent.getId() + " due to missing gatewayName.");
            }
        }
    }

    private String getMetadataForKey(final String key, final List<MetaData> metaDataList) {
        for (final MetaData metaData : metaDataList) {
            if (AbstractRequisitionProvider.VELOCLOUD_METADATA_CONTEXT.equals(metaData.getContext()) && key.equals(metaData.getKey())) {
                return metaData.getValue();
            }
        }
        return null;
    }

    private String parseJson(String detail, String key) {
        if (detail != null) {
            try {
                final JSONObject jsonObject = new JSONObject(detail);
                if (jsonObject.has(key)) {
                    return jsonObject.getString(key);
                }
            } catch (JSONException e1) {
                try {
                    final JSONArray array = new JSONArray(detail);
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject jsonObject = new JSONObject(array.getJSONObject(i));
                        if (jsonObject.has(key)) {
                            return jsonObject.getString("key");
                        }
                    }
                } catch (JSONException e2) {
                    LOG.debug("Cannot parse enterprise event JSON detail property: '{}'", detail);
                }
            }
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "Velocloud Event Ingestor";
    }

    @Override
    public Response perform(Context context) throws Exception {
        return ImmutableResponse.newBuilder()
                .setStatus(scheduledFuture.isDone() ? Status.Failure : Status.Success)
                .setMessage(scheduledFuture.isDone() ? "Not running" : "Running")
                .build();
    }

    public void setDelay(final long delay) {
        if (scheduledFuture != null) {
            destroy();
        }

        this.delay = delay;

        if (delay == 0L) {
            return;
        }

        init();
    }
}
