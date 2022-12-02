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

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.velocloud.client.api.VelocloudApiEdgeClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Aggregate;
import org.opennms.velocloud.client.api.model.ApplicationTraffic;
import org.opennms.velocloud.client.api.model.DestinationTraffic;
import org.opennms.velocloud.client.api.model.MetricsEdge;
import org.opennms.velocloud.client.api.model.MetricsEdgeLink;
import org.opennms.velocloud.client.api.model.MetricsEdgeQoe;
import org.opennms.velocloud.client.api.model.MetricsEdgeSystem;
import org.opennms.velocloud.client.api.model.SourceTraffic;
import org.opennms.velocloud.client.api.model.Traffic;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.model.Application;
import org.opennms.velocloud.client.v1.model.BasicMetric;
import org.opennms.velocloud.client.v1.model.BasicMetricSummary;
import org.opennms.velocloud.client.v1.model.BasicMetrics;
import org.opennms.velocloud.client.v1.model.ConfigurationGetRoutableApplications;
import org.opennms.velocloud.client.v1.model.ConfigurationGetRoutableApplicationsResult;
import org.opennms.velocloud.client.v1.model.EdgeLinkMetric;
import org.opennms.velocloud.client.v1.model.EdgeMetric;
import org.opennms.velocloud.client.v1.model.EdgeMetrics;
import org.opennms.velocloud.client.v1.model.EdgeStatusMetricsSummary;
import org.opennms.velocloud.client.v1.model.FlowMetricSummary;
import org.opennms.velocloud.client.v1.model.Interval;
import org.opennms.velocloud.client.v1.model.LinkQualityEventGetLinkQualityEvents;
import org.opennms.velocloud.client.v1.model.LinkQualityEventGetLinkQualityEventsResult;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeAppMetrics;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeAppMetricsResultItem;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeDestMetrics;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeDestMetricsResultItem;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeDeviceMetrics;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeDeviceMetricsResultItem;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeLinkMetrics;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeLinkMetricsResultItem;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeStatusMetrics;

public class VelocloudApiEdgeClientV1 implements VelocloudApiEdgeClient {

    public final static ApiCache.Endpoint<MetricsGetEdgeLinkMetrics, List<MetricsGetEdgeLinkMetricsResultItem>>
            GET_EDGE_LINK_METRICS = AllApi::metricsGetEdgeLinkMetrics;
    public final static ApiCache.Endpoint<MetricsGetEdgeAppMetrics, List<MetricsGetEdgeAppMetricsResultItem>>
            GET_EDGE_APP_METRICS = AllApi::metricsGetEdgeAppMetrics;
    public final static ApiCache.Endpoint<LinkQualityEventGetLinkQualityEvents, LinkQualityEventGetLinkQualityEventsResult>
            GET_LINK_QUALITY_EVENTS = AllApi::linkQualityEventGetLinkQualityEvents;
    public final static ApiCache.Endpoint<MetricsGetEdgeStatusMetrics, EdgeStatusMetricsSummary>
            GET_EDGE_SYSTEM_METRICS = AllApi::metricsGetEdgeStatusMetrics;
    public final static ApiCache.Endpoint<ConfigurationGetRoutableApplications, ConfigurationGetRoutableApplicationsResult>
            GET_ROUTABLE_APPLICATIONS = AllApi::configurationGetRoutableApplications;
    public final static ApiCache.Endpoint<MetricsGetEdgeDeviceMetrics, List<MetricsGetEdgeDeviceMetricsResultItem>>
            GET_EDGE_DEVICE_METRICS = AllApi::metricsGetEdgeDeviceMetrics;
    public final static ApiCache.Endpoint<MetricsGetEdgeDestMetrics, List<MetricsGetEdgeDestMetricsResultItem>>
            GET_EDGE_DEST_METRICS = AllApi::metricsGetEdgeDestMetrics;

    private static final List<EdgeLinkMetric> EDGE_LINK_METRICS = Arrays.asList(
            EdgeLinkMetric.BPSOFBESTPATHRX,
            EdgeLinkMetric.BPSOFBESTPATHTX,
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
            EdgeLinkMetric.P3PACKETSTX
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

    private final ApiCache.Api api;
    private final int enterpriseId;
    private final int edgeId;

    public VelocloudApiEdgeClientV1(final ApiCache.Api api,
                                    final int enterpriseId,
                                    final int edgeId) {
        this.api = Objects.requireNonNull(api);
        this.enterpriseId = enterpriseId;
        this.edgeId = edgeId;
    }

    @Override
    public MetricsEdge getEdgeMetrics(final Instant start, final Instant end) throws VelocloudApiException {

        final List<MetricsGetEdgeLinkMetricsResultItem> metrics = this.api.call("edge link metrics", GET_EDGE_LINK_METRICS,
                new MetricsGetEdgeLinkMetrics().enterpriseId(enterpriseId).edgeId(edgeId)
                        .interval(new Interval().start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                                .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault())))
                        .metrics(EDGE_LINK_METRICS));

        final Traffic.Collector collectorPriority1 = Traffic.collector();
        final Traffic.Collector collectorPriority2 = Traffic.collector();
        final Traffic.Collector collectorPriority3 = Traffic.collector();
        final Traffic.Collector collectorControl = Traffic.collector();

        //collecting Edge traffic per business priority by summing corresponding traffic of all Edge Links
        metrics.forEach( item -> {
            collectorPriority1.add(item.getP1BytesRx(), item.getP1BytesTx(), item.getP1PacketsRx(), item.getP1PacketsTx());
            collectorPriority2.add(item.getP2BytesRx(), item.getP1BytesTx(), item.getP1PacketsRx(), item.getP1PacketsTx());
            collectorPriority3.add(item.getP3BytesRx(), item.getP1BytesTx(), item.getP1PacketsRx(), item.getP1PacketsTx());
            collectorControl.add(item.getControlBytesRx(), item.getControlBytesTx(), item.getControlPacketsRx(), item.getControlPacketsTx());
        });

        return MetricsEdge.builder()
                .withMetricsEdgeLinks( //collecting edge link data
                        metrics.stream().collect(Collectors.toMap(
                                MetricsGetEdgeLinkMetricsResultItem::getLinkId,
                                item -> MetricsEdgeLink.builder()
                                        .withLinkId(item.getLinkId())
                                        .withBpsOfBestPathRx(item.getBpsOfBestPathRx())
                                        .withBpsOfBestPathTx(item.getBpsOfBestPathTx())
                                        .build())))
                .withTrafficPriority1(collectorControl.toTraffic())
                .withTrafficPriority2(collectorControl.toTraffic())
                .withTrafficPriority3(collectorControl.toTraffic())
                .withTrafficControl(collectorControl.toTraffic())
                .build();
    }

    @Override
    public List<ApplicationTraffic> getEdgeAppTraffic(final Instant start, final Instant end) throws VelocloudApiException {

        final List<MetricsGetEdgeAppMetricsResultItem> metrics = this.api.call("edge applications traffic", GET_EDGE_APP_METRICS,
                new MetricsGetEdgeAppMetrics().edgeId(edgeId).enterpriseId(enterpriseId).metrics(EDGE_TRAFFIC_METRICS)
                        .interval(new Interval().start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                                .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault()))));

        final ConfigurationGetRoutableApplicationsResult routableApplications = getRoutableApplications();
        final Map<Integer, String> classes = routableApplications.getApplicationClasses().entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        final Map<Integer, Application> applications = routableApplications.getApplications().stream()
                .collect(Collectors.toMap(Application::getId, application -> application));

        return metrics.stream().map(metric -> {
            Application application = applications.get(metric.getApplication());
            return ApplicationTraffic.builder()
                    .withDescription(application.getDescription())
                    .withName(application.getName())
                    .withDisplayName(application.getDisplayName())
                    .withApplicationClass(classes.get(application.getPropertyClass()))
                    .withTraffic(map(metric))
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<SourceTraffic> getEdgeSourceTraffic(Instant start, Instant end) throws VelocloudApiException {
        final List<MetricsGetEdgeDeviceMetricsResultItem> edgeTrafficItems = this.api.call("edge source metrics", GET_EDGE_DEVICE_METRICS,
                new MetricsGetEdgeDeviceMetrics().edgeId(edgeId).enterpriseId(enterpriseId).metrics(EDGE_TRAFFIC_METRICS)
                        .interval(new Interval().start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                                .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault()))));

        return edgeTrafficItems.stream().map(edgeTraffic ->
                SourceTraffic.builder()
                        .withName(edgeTraffic.getName())
                        .withSourceMac(edgeTraffic.getSourceMac())
                        .withIpAddress(edgeTraffic.getInfo().getIpAddress())
                        .withHostName(edgeTraffic.getInfo().getHostName())
                        .withTraffic(map(edgeTraffic))
                        .build()

                /*
                TODO
                What of this information are relevant for OpenNMS?

                added: "macAddress": "xx:xx:xx:xx:xx:xx",
                added: "hostName": "SOME-NAME",
                added: "ipAddress": "111.111.111.111",

                additionally available:

                "id": 1123456,
                "created": "2022-04-21T18:34:37.000Z",
                "enterpriseId": 461,
                "edgeId": 2595,
                "segmentId": 0,
                "os": 1,
                "osName": "WINDOWS",
                "osVersion": "10",
                "deviceType": "Desktop/Laptop",
                "deviceModel": null,
                "lastContact": "2022-04-21T18:34:38.000Z",
                "modified": "2022-06-15T20:56:31.000Z",
                "logicalId": "01234567-89ab-cdef-0123-456789abcdef"

                Maybe combine SourceTraffic and DestinationTraffic to one class?

                */
        ).collect(Collectors.toList());

    }

    @Override
    public List<DestinationTraffic> getEdgeDestinationTrafficMetrics(Instant start, Instant end) throws VelocloudApiException {
        final List<MetricsGetEdgeDestMetricsResultItem> edgeTraffics = this.api.call("edge dest metrics", GET_EDGE_DEST_METRICS,
                new MetricsGetEdgeDestMetrics().edgeId(edgeId).enterpriseId(enterpriseId).metrics(EDGE_TRAFFIC_METRICS)
                        .interval(new Interval().start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                                .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault()))));

        return edgeTraffics.stream().map(edgeTraffic ->
                DestinationTraffic.builder()
                        .withName(edgeTraffic.getName())
                        .withName(edgeTraffic.getName())
                        .withDestinationDomain(edgeTraffic.getDestDomain())
                        .withTraffic(map(edgeTraffic))
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<MetricsEdgeQoe> getEdgeQoeMetrics(final Instant start, final Instant end) throws VelocloudApiException {
        //TODO
        return null;
    }

    @Override
    public MetricsEdgeSystem getEdgeSystemMetrics(final Instant start, final Instant end) throws VelocloudApiException {
        final EdgeStatusMetricsSummary metricsSummary = this.api.call("edge system metrics", GET_EDGE_SYSTEM_METRICS,
                new MetricsGetEdgeStatusMetrics().enterpriseId(enterpriseId).edgeId(edgeId).metrics(EDGE_SYSTEM_METRICS)
                        .interval(new Interval().start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                                .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault()))));
        return MetricsEdgeSystem.builder()
                .withCpuPct(map(metricsSummary.getCpuPct()))
                .withCpuCoreTemp(map(metricsSummary.getCpuCoreTemp()))
                .withMemoryPct(map(metricsSummary.getMemoryPct()))
                .withFlowCount(map(metricsSummary.getFlowCount()))
                .withHandoffQueueDrops(map(metricsSummary.getHandoffQueueDrops()))
                .withTunnelCount(map(metricsSummary.getTunnelCount()))
                .withTunnelCountV6(map(metricsSummary.getTunnelCountV6()))
                .build();
    }

    private ConfigurationGetRoutableApplicationsResult getRoutableApplications() throws VelocloudApiException {
        return this.api.call("routable applications", GET_ROUTABLE_APPLICATIONS,
                new ConfigurationGetRoutableApplications().edgeId(edgeId).enterpriseId(enterpriseId));
    }

    private static Aggregate map(BasicMetricSummary metric) {
        return Aggregate.builder()
                .withMin(metric.getMin())
                .withMax(metric.getMax())
                .withAverage(metric.getAverage())
                .build();
    }

    private static Traffic map(FlowMetricSummary flowMetricSummary) {
        return Traffic.builder()
                .withBytesRx(flowMetricSummary.getBytesRx())
                .withBytesTx(flowMetricSummary.getBytesTx())
                .withPacketsRx(flowMetricSummary.getPacketsRx())
                .withPacketsTx(flowMetricSummary.getPacketsTx())
                .build();
    }
}
