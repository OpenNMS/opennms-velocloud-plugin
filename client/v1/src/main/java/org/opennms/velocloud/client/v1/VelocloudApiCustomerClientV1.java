/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
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

package org.opennms.velocloud.client.v1;

import static org.opennms.velocloud.client.v1.VelocloudApiClientProviderV1.getInterval;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Aggregate;
import org.opennms.velocloud.client.api.model.CloudService;
import org.opennms.velocloud.client.api.model.CustomerEvent;
import org.opennms.velocloud.client.api.model.Datacenter;
import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.client.api.model.Link;
import org.opennms.velocloud.client.api.model.MetricsEdge;
import org.opennms.velocloud.client.api.model.MetricsLink;
import org.opennms.velocloud.client.api.model.Path;
import org.opennms.velocloud.client.api.model.Score;
import org.opennms.velocloud.client.api.model.Traffic;
import org.opennms.velocloud.client.api.model.TrafficApplication;
import org.opennms.velocloud.client.api.model.TrafficDestination;
import org.opennms.velocloud.client.api.model.TrafficSource;
import org.opennms.velocloud.client.api.model.Tunnel;
import org.opennms.velocloud.client.api.model.User;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.model.Application;
import org.opennms.velocloud.client.v1.model.BasicMetric;
import org.opennms.velocloud.client.v1.model.BasicMetricSummary;
import org.opennms.velocloud.client.v1.model.BasicMetrics;
import org.opennms.velocloud.client.v1.model.ConfigurationGetRoutableApplications;
import org.opennms.velocloud.client.v1.model.ConfigurationGetRoutableApplicationsResult;
import org.opennms.velocloud.client.v1.model.EdgeGetEdgeGatewayAssignments;
import org.opennms.velocloud.client.v1.model.EdgeGetEdgeGatewayAssignmentsResult;
import org.opennms.velocloud.client.v1.model.EdgeGetEdgeSdwanPeers;
import org.opennms.velocloud.client.v1.model.EdgeGetEdgeSdwanPeersResultItem;
import org.opennms.velocloud.client.v1.model.EdgeLinkMetric;
import org.opennms.velocloud.client.v1.model.EdgeMetric;
import org.opennms.velocloud.client.v1.model.EdgeMetrics;
import org.opennms.velocloud.client.v1.model.EdgeRecord;
import org.opennms.velocloud.client.v1.model.EdgeStatusMetricsSummary;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseDataCenters;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseDataCentersResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdgeList;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdgeListResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdges;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdgesResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseServices;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseServicesResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseUsers;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseUsersResultItem;
import org.opennms.velocloud.client.v1.model.EventGetEnterpriseEvents;
import org.opennms.velocloud.client.v1.model.EventGetEnterpriseEventsResult;
import org.opennms.velocloud.client.v1.model.Interval;
import org.opennms.velocloud.client.v1.model.LinkQualityEventGetLinkQualityEvents;
import org.opennms.velocloud.client.v1.model.LinkQualityEventGetLinkQualityEventsResult;
import org.opennms.velocloud.client.v1.model.LinkQualityObject;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeAppMetrics;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeAppMetricsResultItem;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeDestMetrics;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeDestMetricsResultItem;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeDeviceMetrics;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeDeviceMetricsResultItem;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeLinkMetrics;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeLinkMetricsResultItem;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeStatusMetrics;
import org.opennms.velocloud.client.v1.model.MonitoringGetEnterpriseEdgeNvsTunnelStatusBody;
import org.opennms.velocloud.client.v1.model.MonitoringGetEnterpriseEdgeNvsTunnelStatusResultItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

public class VelocloudApiCustomerClientV1 implements VelocloudApiCustomerClient {

    private static final Logger LOG = LoggerFactory.getLogger(VelocloudApiCustomerClientV1.class);
    public final static ApiCache.Endpoint<EnterpriseGetEnterpriseEdges, List<EnterpriseGetEnterpriseEdgesResultItem>>
            ENTERPRISE_GET_ENTERPRISE_EDGES = AllApi::enterpriseGetEnterpriseEdges;

    public final static ApiCache.Endpoint<EnterpriseGetEnterpriseEdgeList, List<EnterpriseGetEnterpriseEdgeListResultItem>>
            ENTERPRISE_GET_ENTERPRISE_EDGE_LIST = AllApi::enterpriseGetEnterpriseEdgeList;

    public final static ApiCache.Endpoint<EnterpriseGetEnterpriseUsers, List<EnterpriseGetEnterpriseUsersResultItem>>
            ENTERPRISE_GET_ENTERPRISE_USERS = AllApi::enterpriseGetEnterpriseUsers;
    public final static ApiCache.Endpoint<EventGetEnterpriseEvents, EventGetEnterpriseEventsResult>
            EVENT_GET_ENTERPRISE_EVENTS  = AllApi::eventGetEnterpriseEvents;
    public final static ApiCache.Endpoint<MonitoringGetEnterpriseEdgeNvsTunnelStatusBody, List<MonitoringGetEnterpriseEdgeNvsTunnelStatusResultItem>>
            MONITORING_GET_ENTERPRISE_EDGE_NVS_TUNNEL_STATUS = AllApi::monitoringGetEnterpriseEdgeNvsTunnelStatus;
    public final static ApiCache.Endpoint<MetricsGetEdgeLinkMetrics, List<MetricsGetEdgeLinkMetricsResultItem>>
            GET_EDGE_LINK_METRICS = AllApi::metricsGetEdgeLinkMetrics;
    public final static ApiCache.Endpoint<MetricsGetEdgeAppMetrics, List<MetricsGetEdgeAppMetricsResultItem>>
            GET_EDGE_APP_METRICS = AllApi::metricsGetEdgeAppMetrics;
    public final static ApiCache.Endpoint<LinkQualityEventGetLinkQualityEvents, LinkQualityEventGetLinkQualityEventsResult>
            GET_LINK_QUALITY_EVENTS = AllApi::linkQualityEventGetLinkQualityEvents;
    public final static ApiCache.Endpoint<MetricsGetEdgeStatusMetrics, EdgeStatusMetricsSummary>
            GET_EDGE_SYSTEM_METRICS = AllApi::metricsGetEdgeStatusMetrics;
    public final static ApiCache.Endpoint<MetricsGetEdgeDeviceMetrics, List<MetricsGetEdgeDeviceMetricsResultItem>>
            GET_EDGE_DEVICE_METRICS = AllApi::metricsGetEdgeDeviceMetrics;
    public final static ApiCache.Endpoint<MetricsGetEdgeDestMetrics, List<MetricsGetEdgeDestMetricsResultItem>>
            GET_EDGE_DEST_METRICS = AllApi::metricsGetEdgeDestMetrics;

    private static final List<EdgeLinkMetric> EDGE_LINK_METRICS = Arrays.asList(
            //bandwidth
            EdgeLinkMetric.BPSOFBESTPATHRX,
            EdgeLinkMetric.BPSOFBESTPATHTX,
            //traffic
            EdgeLinkMetric.P1BYTESRX,
            EdgeLinkMetric.P1BYTESTX,
            EdgeLinkMetric.P1PACKETSRX,
            EdgeLinkMetric.P1PACKETSTX,
            EdgeLinkMetric.P2BYTESRX,
            EdgeLinkMetric.P2BYTESTX,
            EdgeLinkMetric.P2PACKETSRX,
            EdgeLinkMetric.P2PACKETSTX,
            EdgeLinkMetric.P3BYTESRX,
            EdgeLinkMetric.P3BYTESTX,
            EdgeLinkMetric.P3PACKETSRX,
            EdgeLinkMetric.P3PACKETSTX,
            //quality
            EdgeLinkMetric.BESTLATENCYMSRX,
            EdgeLinkMetric.BESTLATENCYMSTX,
            EdgeLinkMetric.BESTJITTERMSRX,
            EdgeLinkMetric.BESTJITTERMSTX,
            EdgeLinkMetric.BESTLOSSPCTRX,
            EdgeLinkMetric.BESTLOSSPCTTX,
            EdgeLinkMetric.SCORERX,
            EdgeLinkMetric.SCORETX
    );

    private static final List<BasicMetric> EDGE_TRAFFIC_METRIC_LIST = Arrays.asList(
            BasicMetric.BYTESRX,
            BasicMetric.BYTESTX,
            BasicMetric.PACKETSRX,
            BasicMetric.PACKETSTX
    );
    private static final BasicMetrics EDGE_TRAFFIC_METRICS = new BasicMetrics(){{addAll(EDGE_TRAFFIC_METRIC_LIST);}};

    private static final List<EdgeMetric> EDGE_SYSTEM_METRIC_LIST = Arrays.asList(
            EdgeMetric.CPUPCT,
            EdgeMetric.CPUCORETEMP,
            EdgeMetric.MEMORYPCT,
            EdgeMetric.FLOWCOUNT,
            EdgeMetric.HANDOFFQUEUEDROPS,
            EdgeMetric.TUNNELCOUNT,
            EdgeMetric.TUNNELCOUNTV6
    );
    private static final EdgeMetrics EDGE_SYSTEM_METRICS = new EdgeMetrics(){{addAll(EDGE_SYSTEM_METRIC_LIST);}};

    public final static ApiCache.Endpoint<EnterpriseGetEnterpriseServices, List<EnterpriseGetEnterpriseServicesResultItem>>
            ENTERPRISE_GET_ENTERPRISE_SERVICES = AllApi::enterpriseGetEnterpriseServices;

    public final static ApiCache.Endpoint<EnterpriseGetEnterpriseDataCenters, List<EnterpriseGetEnterpriseDataCentersResultItem>>
            MONITORING_GET_ENTERPRISE_DATA_CENTERS = AllApi::enterpriseGetEnterpriseDataCenters;

    public final static ApiCache.Endpoint<EdgeGetEdgeSdwanPeers, List<EdgeGetEdgeSdwanPeersResultItem>>
            EDGE_GET_EDGE_SDWAN_PEERS = AllApi::edgeGetEdgeSdwanPeers;
    public final static ApiCache.Endpoint<EdgeGetEdgeGatewayAssignments, EdgeGetEdgeGatewayAssignmentsResult>
            EDGE_GET_GATEWAY_ASSIGNMENTS = AllApi::edgeGetEdgeGatewayAssignments;

    private final ApiCache.Api api;

    private final int enterpriseId;

    private final int delayInMilliseconds;

    public VelocloudApiCustomerClientV1(final ApiCache.Api api,
                                        final int enterpriseId,
                                        int delayInMilliseconds) {
        this.api = Objects.requireNonNull(api);
        this.enterpriseId = enterpriseId;
        this.delayInMilliseconds = delayInMilliseconds;
    }

    public List<Path> getPaths(final int edgeId) throws VelocloudApiException {
        final var paths = this.api.call("paths", EDGE_GET_EDGE_SDWAN_PEERS,
                new EdgeGetEdgeSdwanPeers()
                        .edgeId(edgeId)
                        .enterpriseId(this.enterpriseId)
                        .limit(Integer.MAX_VALUE));

        return paths.stream()
                .map(p -> Path.builder()
                        .withPeerType(p.getPeerType().toString())
                        .withPeerName(p.getPeerName())
                        .withEdgeLogicalId(p.getEdgeLogicalId())
                        .withDeviceLogicalId(p.getDeviceLogicalId())
                        .withDescription(p.getDescription())
                        .withPathCountStable(p.getPathStatusCount().getStable())
                        .withPathCountUnstable(p.getPathStatusCount().getUnstable())
                        .withPathCountStandBy(p.getPathStatusCount().getStandby())
                        .withPathCountDead(p.getPathStatusCount().getDead())
                        .withPathCountUnknown(p.getPathStatusCount().getUnknown())
                        .withPathCountTotal(p.getPathStatusCount().getTotal())
                        .build())
                .collect(Collectors.toList());
    }

    public List<CloudService> getCloudServices(final String logicalEdgeId) throws VelocloudApiException {
        final List<EnterpriseGetEnterpriseEdgeListResultItem> edges = this.api.call("edgeList", ENTERPRISE_GET_ENTERPRISE_EDGE_LIST,
                new EnterpriseGetEnterpriseEdgeList()
                        .enterpriseId(this.enterpriseId)
                        .addWithItem(EnterpriseGetEnterpriseEdgeList.WithEnum.CLOUDSERVICES)
                        .addWithItem(EnterpriseGetEnterpriseEdgeList.WithEnum.NVSFROMEDGE));

        return edges.stream()
                .filter(e->Objects.equals(e.getLogicalId(), logicalEdgeId))
                .flatMap(e->e.getCloudServices().stream())
                .map(c -> CloudService.builder()
                        .withLink(c.getLink())
                        .withRole(Objects.equals(c.getNvsIp(), c.getProvider().getData().getPrimaryServer()) ? "primary" : "secondary")
                        .withName(c.getProvider().getName())
                        .withTimestamp(c.getTimestamp())
                        .withState(c.getState())
                        .withPathId(c.getPathId())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public List<Edge> getEdges() throws VelocloudApiException {
        final var edges = this.api.call("edges", ENTERPRISE_GET_ENTERPRISE_EDGES,
                new EnterpriseGetEnterpriseEdges()
                        .enterpriseId(this.enterpriseId)
                        .addWithItem(EnterpriseGetEnterpriseEdges.WithEnum.SITE)
                        .addWithItem(EnterpriseGetEnterpriseEdges.WithEnum.CONFIGURATION)
                        .addWithItem(EnterpriseGetEnterpriseEdges.WithEnum.RECENTLINKS));

        return edges.stream()
                    .map(e -> Edge.builder()
                                  .withSite(e.getSite().getName())
                                  .withOperator(e.getConfiguration().getOperator().getName())
                                  .withHub(e.isIsHub())
                                  .withAlertsEnabled(e.getAlertsEnabled().getValue() != 0)
                                  .withBuildNumber(e.getBuildNumber())
                                  .withCustomInfo(e.getCustomInfo())
                                  .withDescription(e.getDescription())
                                  .withDeviceFamily(e.getDeviceFamily())
                                  .withDeviceId(e.getDeviceId())
                                  .withDnsName(e.getDnsName())
                                  .withLteRegion(e.getLteRegion())
                                  .withLogicalId(e.getLogicalId())
                                  .withEdgeId(e.getId())
                                  .withModelNumber(e.getModelNumber())
                                  .withName(e.getName())
                                  .withOperatorAlertsEnabled(e.getOperatorAlertsEnabled().getValue() != 0)
                                  .withSelfMacAddress(e.getSelfMacAddress())
                                  .withSiteId(e.getSiteId())
                                  .withSoftwareVersion(e.getSoftwareVersion())
                                  .withEdgeState(e.getEdgeState().getValue())
                                  .withServiceState(e.getServiceState().getValue())
                                  .withSiteId(e.getSiteId())
                                  .withSiteName(e.getSite().getName())
                                  .withAddress(e.getSite().getStreetAddress())
                                  .withAddress2((e.getSite().getStreetAddress2()))
                                  .withZip(e.getSite().getPostalCode())
                                  .withCity(e.getSite().getCity())
                                  .withState(e.getSite().getState())
                                  .withCountry(e.getSite().getCountry())
                                  .withLatitude(e.getSite().getLat())
                                  .withLongitude(e.getSite().getLon())
                                  .withLinks(e.getRecentLinks().stream()
                                              .map(l -> Link.builder()
                                                            .withId(l.getId())
                                                            .withCreated(l.getCreated())
                                                            .withEdgeId(l.getEdgeId())
                                                            .withLogicalId(l.getLogicalId())
                                                            .withInternalId(l.getInternalId())
                                                            .withInterface(l.getInterface())
                                                            .withMacAddress(l.getMacAddress())
                                                            .withIpAddress(l.getIpAddress())
                                                            .withIpv6Address(l.getIpV6Address())
                                                            .withNetmask(l.getNetmask())
                                                            .withNetworkSide(l.getNetworkSide().toString())
                                                            .withNetworkType(l.getNetworkType().toString())
                                                            .withDisplayName(l.getDisplayName())
                                                            .withIsp(l.getIsp())
                                                            .withOrg(l.getOrg())
                                                            .withLat(l.getLat())
                                                            .withLon(l.getLon())
                                                            .withLinkMode(l.getLinkMode().toString())
                                                            .withAlertsEnabled(l.getAlertsEnabled().getValue() == 1)
                                                            .withOperatorAlertsEnabled(l.getOperatorAlertsEnabled().getValue() == 1)
                                                            .withLinkState(l.getState().getValue())
                                                            .withServiceState(l.getServiceState().getValue())
                                                            .build())
                                              .collect(Collectors.toList()))
                                  .build())
                    .collect(Collectors.toList());
    }

    @Override
    public List<User> getUsers() throws VelocloudApiException {
        final var users = this.api.call("users", ENTERPRISE_GET_ENTERPRISE_USERS,
                new EnterpriseGetEnterpriseUsers().enterpriseId(this.enterpriseId));

        return users.stream()
                    .map(user -> User.builder()
                                     .setId(user.getId())
                                     .setUserType(user.getUserType())
                                     .setDomain(user.getDomain())
                                     .setUsername(user.getUsername())
                                     .setFirstName(user.getFirstName())
                                     .setLastName(user.getLastName())
                                     .setEmail(user.getEmail())
                                     .setRoleId(user.getRoleId())
                                     .setRoleName(user.getRoleName())
                                     .setAccessLevel(user.getAccessLevel().getValue())
                                     .setActive(user.getIsActive().getValue() == 1)
                                     .setLocked(user.getIsLocked().getValue() == 1)
                                     .setNative(user.getIsNative().getValue() == 1)
                                     .setSshUsername(user.getSshUsername())
                                     .build())
                    .collect(Collectors.toList());
    }

    @Override
    public List<CustomerEvent> getEvents(final Instant start, final Instant end) throws VelocloudApiException {
        final EventGetEnterpriseEventsResult events = this.api.call("events", EVENT_GET_ENTERPRISE_EVENTS,
                new EventGetEnterpriseEvents().enterpriseId(this.enterpriseId).interval(new Interval()
                        .start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                        .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault()))));

        return events.getData().stream().map(
                        e -> CustomerEvent.builder()
                                .withDetail(e.getDetail())
                                .withCategory(e.getCategory().getValue())
                                .withEvent(e.getEvent())
                                .withId(e.getId())
                                .withEventTime(e.getEventTime())
                                .withEdgeName(e.getEdgeName())
                                .withEnterpriseUsername(e.getEnterpriseUsername())
                                .withMessage(e.getMessage())
                                .withSeverity(e.getSeverity().getValue())
                                .build())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Tunnel> getTunnelState(final String dataKey) throws VelocloudApiException {
        final Set<MonitoringGetEnterpriseEdgeNvsTunnelStatusResultItem> tunnelStatus = new HashSet<>();

        tunnelStatus.addAll(this.api.call("nvs tunnels (NVS_FROM_EDGE_TUNNEL)", MONITORING_GET_ENTERPRISE_EDGE_NVS_TUNNEL_STATUS,
                new MonitoringGetEnterpriseEdgeNvsTunnelStatusBody().enterpriseId(this.enterpriseId).tag("NVS_FROM_EDGE_TUNNEL")));

        tunnelStatus.addAll(this.api.call("nvs tunnels (EDGE_NVS_TUNNEL)", MONITORING_GET_ENTERPRISE_EDGE_NVS_TUNNEL_STATUS,
                new MonitoringGetEnterpriseEdgeNvsTunnelStatusBody().enterpriseId(this.enterpriseId).tag("EDGE_NVS_TUNNEL")));

        return tunnelStatus.stream()
                .filter(t->Objects.equals(t.getDataKey(), dataKey))
                .sorted(Comparator.comparing(EdgeRecord::getTimestamp).reversed())
                .map(tunnel -> Tunnel.builder()
                        .withId(tunnel.getId())
                        .withDataKey(tunnel.getDataKey())
                        .withState(tunnel.getState().getValue())
                        .withLink(tunnel.getMonitoringGetEnterpriseEdgeNvsTunnelStatusResultItemData().getLink())
                        .withName(tunnel.getMonitoringGetEnterpriseEdgeNvsTunnelStatusResultItemData().getName())
                        .withDestination(tunnel.getMonitoringGetEnterpriseEdgeNvsTunnelStatusResultItemData().getDestination() != null ? tunnel.getMonitoringGetEnterpriseEdgeNvsTunnelStatusResultItemData().getDestination().toLowerCase() : null)
                        .build())
                .findFirst();
    }

    String getState(final String status, final String key) {
        if (Strings.isNullOrEmpty(status)) {
            return null;
        }
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final JsonNode node = mapper.readTree(status);
            if (node == null || node.get(key) == null) {
                return null;
            }
            return node.get(key).asText();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public List<Datacenter> getDatacenters() throws VelocloudApiException {
        final List<EnterpriseGetEnterpriseDataCentersResultItem> datacenters = this.api.call("datacenters", MONITORING_GET_ENTERPRISE_DATA_CENTERS,
                new EnterpriseGetEnterpriseDataCenters().enterpriseId(this.enterpriseId));

        return datacenters.stream()
                .map(e->Datacenter.builder()
                        .withLogicalId(e.getLogicalId())
                        .withName(e.getName())
                        .withPrimaryState(getState(e.getStatus(), "primary"))
                        .withSecondaryState(getState(e.getStatus(), "secondary"))
                        .build())
                .collect(Collectors.toList());
    }

    public Optional<Integer> getSuperGateway(final int edgeId) throws VelocloudApiException {
        final EdgeGetEdgeGatewayAssignmentsResult assignedGateways = this.api.call(
                "edgeGatewayAssignments",
                EDGE_GET_GATEWAY_ASSIGNMENTS,
                new EdgeGetEdgeGatewayAssignments()
                        .enterpriseId(enterpriseId)
                        .id(edgeId)
        );

        return Optional.of(assignedGateways.getSuperGateway().getId());
    }


    @Override
    public MetricsEdge getEdgeMetrics(int edgeId, int intervalMillis) throws VelocloudApiException {

        final Interval interval = getInterval(intervalMillis, delayInMilliseconds);

        final MetricsEdge.Builder builder = MetricsEdge.builder();
        final EdgeStatusMetricsSummary systemMetrics =
                this.api.call("edge system metrics", GET_EDGE_SYSTEM_METRICS,
                        new MetricsGetEdgeStatusMetrics().enterpriseId(enterpriseId).edgeId(edgeId)
                                .metrics(EDGE_SYSTEM_METRICS).interval(interval));

        builder.withCpuPct(map(systemMetrics.getCpuPct()))
                .withCpuCoreTemp(map(systemMetrics.getCpuCoreTemp()))
                .withMemoryPct(map(systemMetrics.getMemoryPct()))
                .withFlowCount(map(systemMetrics.getFlowCount()))
                .withHandoffQueueDrops(map(systemMetrics.getHandoffQueueDrops()))
                .withTunnelCount(map(systemMetrics.getTunnelCount()))
                .withTunnelCountV6(map(systemMetrics.getTunnelCountV6()))
                .withTimestamp(interval.getEnd().toEpochSecond());

        try {
            final List<MetricsGetEdgeAppMetricsResultItem> trafficApplications = this.api.call(
                    "edge traffic by applications ", GET_EDGE_APP_METRICS,
                    new MetricsGetEdgeAppMetrics().edgeId(edgeId).enterpriseId(enterpriseId)
                                    .metrics(EDGE_TRAFFIC_METRICS).interval(interval).resolveApplicationNames(true));

            builder.withTrafficApplications(map(trafficApplications));
        } catch (VelocloudApiException ex) {
            LOG.debug("Cannot get routable applications for edge with edgeId={}, enterpriseId={}. Error message: {}", edgeId, enterpriseId, ex.getMessage());
        }

        try {
            final List<MetricsGetEdgeDeviceMetricsResultItem> trafficSources =
                    this.api.call("edge traffic by source", GET_EDGE_DEVICE_METRICS,
                            new MetricsGetEdgeDeviceMetrics().edgeId(edgeId).enterpriseId(enterpriseId)
                                    .metrics(EDGE_TRAFFIC_METRICS).interval(interval));
            builder.withTrafficSources(map(trafficSources, VelocloudApiCustomerClientV1::map));
        } catch (VelocloudApiException ex) {
            LOG.debug("Cannot get device metrics for edge with edgeId={}, enterpriseId={}. Error message: {}", edgeId, enterpriseId, ex.getMessage());
        }

        try {
            final List<MetricsGetEdgeDestMetricsResultItem> trafficDestination =
                    this.api.call("edge traffic by destination", GET_EDGE_DEST_METRICS,
                            new MetricsGetEdgeDestMetrics().edgeId(edgeId).enterpriseId(enterpriseId)
                                    .metrics(EDGE_TRAFFIC_METRICS).interval(interval));
            builder.withTrafficDestinations(map(trafficDestination, VelocloudApiCustomerClientV1::map));
        } catch (VelocloudApiException ex) {
            LOG.debug("Cannot get destination metrics for edge with edgeId={}, enterpriseId={}. Error message: {}", edgeId, enterpriseId, ex.getMessage());
        }

        try {
            final LinkQualityEventGetLinkQualityEventsResult qoe =
                    this.api.call("edge dest metrics", GET_LINK_QUALITY_EVENTS,
                            new LinkQualityEventGetLinkQualityEvents().edgeId(edgeId).enterpriseId(enterpriseId)
                                    .interval(interval).maxSamples(1));
            builder.withScoreAfterOptimization(getScore(qoe));
        } catch (VelocloudApiException ex) {
            LOG.debug("Cannot get quality events for edge with edgeId={}, enterpriseId={}. Error message: {}", edgeId, enterpriseId, ex.getMessage());
        }

        return builder.build();
    }

    @Override
    public MetricsLink getLinkMetrics(int edgeId, String internalLinkId, int intervalMillis) throws VelocloudApiException {

        Objects.requireNonNull(internalLinkId);
        final Interval interval = getInterval(intervalMillis, delayInMilliseconds);
        final List<MetricsGetEdgeLinkMetricsResultItem> metrics = this.api.call(
                "edge link metrics",GET_EDGE_LINK_METRICS,
                new MetricsGetEdgeLinkMetrics().enterpriseId(enterpriseId).edgeId(edgeId)
                        .interval(interval)
                        .metrics(EDGE_LINK_METRICS));
        final LinkQualityEventGetLinkQualityEventsResult qoe =
                this.api.call("edge dest metrics", GET_LINK_QUALITY_EVENTS,
                        new LinkQualityEventGetLinkQualityEvents().edgeId(edgeId).enterpriseId(enterpriseId)
                                .interval(interval).maxSamples(1));

        return metrics.stream().filter(item -> internalLinkId.equals(item.getLink().getInternalId())).findFirst().map(item ->
                MetricsLink.builder()
                    .withBandwidthRx(item.getBpsOfBestPathRx())
                    .withBandwidthTx(item.getBpsOfBestPathRx())
                    .withTrafficPriority1(Traffic.builder()
                            .withBytesRx(item.getP1BytesRx())
                            .withBytesTx(item.getP1BytesTx())
                            .withPacketsRx(item.getP1PacketsRx())
                            .withPacketsTx(item.getP1PacketsTx())
                            .build())
                    .withTrafficPriority2(Traffic.builder()
                            .withBytesRx(item.getP2BytesRx())
                            .withBytesTx(item.getP2BytesTx())
                            .withPacketsRx(item.getP2PacketsRx())
                            .withPacketsTx(item.getP2PacketsTx())
                            .build())
                    .withTrafficPriority3(Traffic.builder()
                            .withBytesRx(item.getP3BytesRx())
                            .withBytesTx(item.getP3BytesTx())
                            .withPacketsRx(item.getP3PacketsRx())
                            .withPacketsTx(item.getP3PacketsTx())
                            .build())
                    .withTrafficControl(Traffic.builder()
                            .withBytesRx(item.getControlBytesRx())
                            .withBytesTx(item.getControlBytesTx())
                            .withPacketsRx(item.getControlPacketsRx())
                            .withPacketsTx(item.getControlPacketsTx())
                            .build())
                    .withBestLatencyMsRx(item.getBestLatencyMsRx())
                    .withBestLatencyMsTx(item.getBestLatencyMsTx())
                    .withBestJitterMsRx(item.getBestJitterMsRx())
                    .withBestJitterMsTx(item.getBestJitterMsTx())
                    .withBestLossPctRx(item.getBestLossPctRx())
                    .withBestLossPctTx(item.getBestLossPctTx())
                    .withScoreRx(item.getScoreRx())
                    .withScoreTx(item.getScoreTx())
                    .withTimestamp(interval.getEnd().toEpochSecond())
                ).orElseThrow(() -> new VelocloudApiException("Error getting link metrics"))
                //add score
                .withScoreBeforeOptimization(Optional.ofNullable(qoe.get(internalLinkId))
                        .map(linkQualityObject -> mapScore(linkQualityObject.getScore())).orElse(null))
                .build();
    }

    private static Aggregate map(BasicMetricSummary metric) {
        if (metric == null) {
            return null;
        }
        return Aggregate.builder()
                .withMin(metric.getMin())
                .withMax(metric.getMax())
                .withAverage(metric.getAverage())
                .build();
    }

    private static TrafficSource map(final MetricsGetEdgeDeviceMetricsResultItem metricsDevice) {
        return TrafficSource.builder()
                .withName(metricsDevice.getName())
                .withSourceMac(metricsDevice.getSourceMac())
                .withIpAddress(metricsDevice.getInfo().getIpAddress())
                .withHostName(metricsDevice.getInfo().getHostName())
                .withOsName(metricsDevice.getInfo().getOsName())
                .withOsVersion(metricsDevice.getInfo().getOsVersion())
                .withDeviceType(metricsDevice.getInfo().getDeviceType())
                .withDeviceModel(metricsDevice.getInfo().getDeviceModel())
                .withTraffic(Traffic.builder()
                        .withBytesRx(metricsDevice.getBytesRx())
                        .withBytesTx(metricsDevice.getBytesTx())
                        .withPacketsRx(metricsDevice.getPacketsRx())
                        .withPacketsTx(metricsDevice.getPacketsTx())
                        .build())
                .build();
    }

    private static TrafficDestination map(final MetricsGetEdgeDestMetricsResultItem metricsDest) {
        return TrafficDestination.builder()
                .withName(metricsDest.getName())
                .withDestinationDomain(metricsDest.getDestDomain())
                .withTraffic(Traffic.builder()
                        .withBytesRx(metricsDest.getBytesRx())
                        .withBytesTx(metricsDest.getBytesTx())
                        .withPacketsRx(metricsDest.getPacketsRx())
                        .withPacketsTx(metricsDest.getPacketsTx())
                        .build())
                .build();
    }

    private static <T,R> List<R> map(final List<T> list, final Function<T, R> mapFunction) {
        return list.stream().map(mapFunction).collect(Collectors.toList());
    }

    private List<TrafficApplication> map(final List<MetricsGetEdgeAppMetricsResultItem> trafficApplications) {
        return map(trafficApplications, (item) -> {
            return TrafficApplication.builder()
                    .withName(item.getName())
                    .withTraffic(Traffic.builder()
                            .withBytesRx(item.getBytesRx())
                            .withBytesTx(item.getBytesTx())
                            .withPacketsRx(item.getPacketsRx())
                            .withPacketsTx(item.getPacketsTx())
                            .build())
                    .build();

        });
    }

    private Score mapScore(Map<String, BigDecimal> score) {
        if (score == null) {
            return null;
        }
        return Score.builder()
                .withAudio(score.get("0"))
                .withVideo(score.get("1"))
                .withTransactional(score.get("2"))
                .build();
    }

    private Score getScore(LinkQualityEventGetLinkQualityEventsResult qoe) {
        if (qoe == null) {
            return null;
        }
        final LinkQualityObject overallLinkQuality = qoe.get("overallLinkQuality");
        if (overallLinkQuality == null) {
            return null;
        }
        final Map<String, BigDecimal> score = overallLinkQuality.getScore();
        if (score == null) {
            return null;
        }
        return Score.builder()
                .withAudio(score.get("0"))
                .withVideo(score.get("1"))
                .withTransactional(score.get("2"))
                .build();
    }
}
