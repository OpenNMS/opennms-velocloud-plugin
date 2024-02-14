/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022-2024 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2024 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.velocloud.events;

import java.net.InetAddress;
import java.sql.Date;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.client.api.model.CustomerEvent;
import org.opennms.velocloud.client.api.model.PartnerEvent;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.connections.ConnectionManager;
import org.opennms.velocloud.requisition.AbstractRequisitionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.ImmutableMap;

public class VelocloudEventIngestor implements Runnable, HealthCheck {
    private static final Logger LOG = LoggerFactory.getLogger(VelocloudEventIngestor.class);

    public static final String LOGICAL_ID = "logicalId";

    public static final String ENTERPRISE_EVENTS_UEI = "uei.opennms.org/vendor/vmware/velocloud/enterpriseEvents";

    public static final String PROXY_EVENTS_UEI = "uei.opennms.org/vendor/vmware/velocloud/proxyEvents";

    private final static Map<String, Severity> SEVERITY_MAP = ImmutableMap.<String, Severity>builder()
            .put("EMERGENCY", Severity.CRITICAL)
            .put("ALERT", Severity.CRITICAL)
            .put("CRITICAL", Severity.MAJOR)
            .put("ERROR", Severity.MINOR)
            .put("WARNING", Severity.WARNING)
            .put("NOTICE", Severity.NORMAL)
            .put("INFO", Severity.NORMAL)
            .put("DEBUG", Severity.NORMAL)
            .build();

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private final ClientManager clientManager;

    private final ConnectionManager connectionManager;

    private final NodeDao nodeDao;

    private final EventForwarder eventForwarder;

    private Instant lastPoll = null;

    private ScheduledFuture<?> scheduledFuture;

    private long delay = 0L;

    private class RequisitionIdentifier {
        private String foreignSource;
        private String enterpriseId;
        private String alias;

        public RequisitionIdentifier(final Node n) {
            final Map<String, String> map = n.getMetaData().stream()
                    .filter(metaData -> Objects.equals(metaData.getContext(), AbstractRequisitionProvider.VELOCLOUD_METADATA_CONTEXT))
                    .collect(Collectors.toMap(metaData -> metaData.getKey(), metaData -> metaData.getValue()));
            foreignSource = Objects.requireNonNull(n.getForeignSource());
            enterpriseId = map.get("enterpriseId");
            alias = Objects.requireNonNull(map.get("alias"));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RequisitionIdentifier requisitionIdentifier = (RequisitionIdentifier) o;
            return Objects.equals(foreignSource, requisitionIdentifier.foreignSource) && Objects.equals(enterpriseId, requisitionIdentifier.enterpriseId) && Objects.equals(alias, requisitionIdentifier.alias);
        }

        @Override
        public int hashCode() {
            return Objects.hash(foreignSource, enterpriseId, alias);
        }
    }

    public VelocloudEventIngestor(final ClientManager clientManager,
                                  final ConnectionManager connectionManager,
                                  final NodeDao nodeDao,
                                  final EventForwarder eventForwarder,
                                  final long delay) {
        this.clientManager = Objects.requireNonNull(clientManager);
        this.connectionManager = Objects.requireNonNull(connectionManager);
        this.nodeDao = Objects.requireNonNull(nodeDao);
        this.eventForwarder = Objects.requireNonNull(eventForwarder);
        this.delay = delay;

        LOG.debug("Velocloud Event Ingestor is initializing (delay = {}ms).", delay);
        scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(this, this.delay, this.delay, TimeUnit.MILLISECONDS);
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

        final Set<RequisitionIdentifier> requisitionIdentifiers = nodeDao.getNodes().stream()
                .filter(node -> node.getMetaData().stream()
                    .anyMatch(metaData -> Objects.equals(AbstractRequisitionProvider.VELOCLOUD_METADATA_CONTEXT, metaData.getContext()) && Objects.equals("alias", metaData.getKey())))
                .map(node -> new RequisitionIdentifier(node))
                .collect(Collectors.toSet());

        for(final RequisitionIdentifier requisitionIdentifier : requisitionIdentifiers) {
            final Integer enterpriseId = (requisitionIdentifier.enterpriseId == null || requisitionIdentifier.enterpriseId.isBlank()) ? null : Integer.parseInt(requisitionIdentifier.enterpriseId);

            try {
                final Optional<VelocloudApiCustomerClient> velocloudApiCustomerClient = connectionManager.getCustomerClient(requisitionIdentifier.alias, enterpriseId);

                if (velocloudApiCustomerClient.isPresent()) {
                    try {
                        processCustomerEvents(lastPoll, now, requisitionIdentifier.foreignSource, velocloudApiCustomerClient.get());
                    } catch (VelocloudApiException e) {
                        LOG.error("Cannot process customer events for alias='{}', enterpriseId='{}'", requisitionIdentifier.alias, requisitionIdentifier.enterpriseId);
                    }
                    continue;
                }
            } catch (VelocloudApiException e) {
                LOG.debug("Cannot create customer client for alias='{}', enterpriseId='{}'", requisitionIdentifier.alias, requisitionIdentifier.enterpriseId);
            }

            try {
                final Optional<VelocloudApiPartnerClient> velocloudApiPartnerClient = connectionManager.getPartnerClient(requisitionIdentifier.alias);

                if (velocloudApiPartnerClient.isPresent()) {
                    try {
                        processPartnerEvents(lastPoll, now, requisitionIdentifier.foreignSource, velocloudApiPartnerClient.get());
                    } catch (VelocloudApiException e) {
                        LOG.error("Cannot process partner events for alias='{}', enterpriseId='{}'", requisitionIdentifier.alias, requisitionIdentifier.enterpriseId);
                    }
                }
            } catch (VelocloudApiException e) {
                LOG.error("Cannot create customer or partner client for alias='{}', enterpriseId='{}'", requisitionIdentifier.alias, requisitionIdentifier.enterpriseId);
            }
        }

        // interval start and end is inclusive
        lastPoll = now.plus(1, ChronoUnit.MILLIS);
    }

    private void processEvent(final String foreignSource, final String foreignId, final PartnerEvent partnerEvent) {
        final Node node = nodeDao.getNodeByCriteria(foreignSource + ":" + foreignId);

        if (node == null) {
            LOG.warn("Ignoring proxy event #{} since node {} cannot be found.", partnerEvent.getId(), foreignSource + ":" + foreignId);
            return;
        }

        final Optional<InetAddress> inetAddress = node.getIpInterfaces().stream().map(iface -> iface.getIpAddress()).findFirst();
        final ImmutableInMemoryEvent.Builder builder = ImmutableInMemoryEvent.newBuilder()
                .setUei(PROXY_EVENTS_UEI)
                .setSource(VelocloudEventIngestor.class.getCanonicalName())
                .setNodeId(node.getId())
                .setSeverity(SEVERITY_MAP.get(partnerEvent.getSeverity()))
                .setInterface(inetAddress.orElse(null));

        builder.addParameter(ImmutableEventParameter.newInstance("gatewayName", partnerEvent.getGatewayName()));
        builder.addParameter(ImmutableEventParameter.newInstance("event", partnerEvent.getEvent()));
        builder.addParameter(ImmutableEventParameter.newInstance("message", partnerEvent.getMessage()));
        builder.addParameter(ImmutableEventParameter.newInstance("severity", partnerEvent.getSeverity()));
        builder.addParameter(ImmutableEventParameter.newInstance("category", partnerEvent.getCategory()));
        builder.addParameter(ImmutableEventParameter.newInstance("proxyUsername", partnerEvent.getProxyUsername()));
        builder.addParameter(ImmutableEventParameter.newInstance("detail", partnerEvent.getDetail()));
        builder.addParameter(ImmutableEventParameter.newInstance("enterpriseName", partnerEvent.getEnterpriseName()));
        builder.addParameter(ImmutableEventParameter.newInstance("networkName", partnerEvent.getNetworkName()));
        builder.addParameter(ImmutableEventParameter.newInstance("id", String.valueOf(partnerEvent.getId())));

        if (partnerEvent.getEventTime() != null) {
            builder.setTime(Date.from(partnerEvent.getEventTime().toInstant()));
            builder.addParameter(ImmutableEventParameter.newInstance("eventTime", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(partnerEvent.getEventTime())));
        } else {
            builder.addParameter(ImmutableEventParameter.newInstance("eventTime", null));
        }

        eventForwarder.sendSync(builder.build());
    }

    private void processEvent(final String foreignSource, final String foreignId, final CustomerEvent customerEvent) {
        final Node node = nodeDao.getNodeByCriteria(foreignSource + ":" + foreignId);

        if (node == null) {
            LOG.warn("Ignoring enterprise event #{} since node {} cannot be found.", customerEvent.getId(), foreignSource + ":" + foreignId);
            return;
        }

        final String logicalId = parseYaml(customerEvent.getDetail(), LOGICAL_ID);

        final Optional<InetAddress> inetAddress;

        if (!Objects.toString(logicalId, "").isBlank()) {
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
                .setSeverity(SEVERITY_MAP.get(customerEvent.getSeverity()))
                .setInterface(inetAddress.orElse(null));

        builder.addParameter(ImmutableEventParameter.newInstance("edgeName", customerEvent.getEdgeName()));
        builder.addParameter(ImmutableEventParameter.newInstance("event", customerEvent.getEvent()));
        builder.addParameter(ImmutableEventParameter.newInstance("message", customerEvent.getMessage()));
        builder.addParameter(ImmutableEventParameter.newInstance("severity", customerEvent.getSeverity()));
        builder.addParameter(ImmutableEventParameter.newInstance("category", customerEvent.getCategory()));
        builder.addParameter(ImmutableEventParameter.newInstance("enterpriseUsername", customerEvent.getEnterpriseUsername()));
        builder.addParameter(ImmutableEventParameter.newInstance("detail", customerEvent.getDetail()));
        builder.addParameter(ImmutableEventParameter.newInstance("id", String.valueOf(customerEvent.getId())));

        if (customerEvent.getEventTime() != null) {
            builder.setTime(Date.from(customerEvent.getEventTime().toInstant()));
            builder.addParameter(ImmutableEventParameter.newInstance("eventTime", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(customerEvent.getEventTime())));
        } else {
            builder.addParameter(ImmutableEventParameter.newInstance("eventTime", null));
        }

        eventForwarder.sendSync(builder.build());
    }

    private void processCustomerEvents(final Instant start, final Instant end, final String foreignSource, final VelocloudApiCustomerClient velocloudApiCustomerClient) throws VelocloudApiException {
        final List<CustomerEvent> customerEventList = velocloudApiCustomerClient.getEvents(start, end);
        LOG.debug("{} enterprise events found.", customerEventList.size());

        int processed = 0;
        int ignored = 0;
        for (final CustomerEvent customerEvent : customerEventList) {
            if (!Objects.toString(customerEvent.getEdgeName(), "").isBlank()) {
                processEvent(foreignSource, customerEvent.getEdgeName(), customerEvent);
                processed++;
            } else {
                ignored++;
            }
        }
        LOG.debug("{} events processed, {} events ignored.", processed, ignored);
    }

    private void processPartnerEvents(final Instant start, final Instant end, final String foreignSource, final VelocloudApiPartnerClient velocloudApiPartnerClient) throws VelocloudApiException {
        final List<PartnerEvent> enterpriseEventList = velocloudApiPartnerClient.getEvents(start, end);
        LOG.debug("{} proxy events found.", enterpriseEventList.size());

        int processed = 0;
        int ignored = 0;
        for (final PartnerEvent partnerEvent : enterpriseEventList) {
            if (!Objects.toString(partnerEvent.getGatewayName(), "").isBlank()) {
                processEvent(foreignSource, partnerEvent.getGatewayName(), partnerEvent);
                processed++;
            } else {
                ignored++;
            }
        }
        LOG.debug("{} events processed, {} events ignored.", processed, ignored);
    }

    private String getMetadataForKey(final String key, final List<MetaData> metaDataList) {
        for (final MetaData metaData : metaDataList) {
            if (AbstractRequisitionProvider.VELOCLOUD_METADATA_CONTEXT.equals(metaData.getContext()) && key.equals(metaData.getKey())) {
                return metaData.getValue();
            }
        }
        return null;
    }

    private String parseYaml(final String detail, final String key) {
        final Yaml yaml = new Yaml();

        if (!Objects.toString(detail, "").isBlank()) {
            final Object object1 = yaml.load(detail);
            return searchKey(object1, key);
        }

        return null;
    }

    private String searchKey(final Object o, final String key) {
        if (o instanceof Map) {
            final Map<String, String> map = (Map) o;
            return map.get(key);
        } else {
            if (o instanceof List) {
                final List list = (List) o;
                for (final Object element : list) {
                    final String value = searchKey(element, key);
                    if (value != null) {
                        return value;
                    }
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
}
