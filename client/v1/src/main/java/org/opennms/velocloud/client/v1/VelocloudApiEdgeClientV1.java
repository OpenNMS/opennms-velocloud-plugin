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
import org.opennms.velocloud.client.api.model.MetricsApp;
import org.opennms.velocloud.client.api.model.MetricsEdgeSystem;
import org.opennms.velocloud.client.api.model.MetricsEdgeQoe;
import org.opennms.velocloud.client.api.model.MetricsEdgeLink;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.model.BasicMetric;
import org.opennms.velocloud.client.v1.model.BasicMetricSummary;
import org.opennms.velocloud.client.v1.model.BasicMetrics;
import org.opennms.velocloud.client.v1.model.EdgeLinkMetric;
import org.opennms.velocloud.client.v1.model.EdgeMetric;
import org.opennms.velocloud.client.v1.model.EdgeMetrics;
import org.opennms.velocloud.client.v1.model.EdgeStatusMetricsSummary;
import org.opennms.velocloud.client.v1.model.Interval;
import org.opennms.velocloud.client.v1.model.LinkQualityEventGetLinkQualityEvents;
import org.opennms.velocloud.client.v1.model.LinkQualityEventGetLinkQualityEventsResult;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeAppMetrics;
import org.opennms.velocloud.client.v1.model.MetricsGetEdgeAppMetricsResultItem;
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

    private static final List<EdgeLinkMetric> EDGE_LINK_METRICS = Arrays.asList(
            EdgeLinkMetric.BPSOFBESTPATHRX,
                                                    EdgeLinkMetric.BPSOFBESTPATHTX
                                                    );

    private static final List<BasicMetric> EDGE_TRAFFIC_METRIC_LIST = Arrays.asList(
            BasicMetric.BYTESRX,
            BasicMetric.BYTESTX,
            BasicMetric.TOTALBYTES,
            BasicMetric.PACKETSRX,
            BasicMetric.PACKETSTX,
            BasicMetric.TOTALPACKETS
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
    public Map<Integer, MetricsEdgeLink> getEdgeLinkMetrics(final Instant start, final Instant end) throws VelocloudApiException {

        final List<MetricsGetEdgeLinkMetricsResultItem> metrics = this.api.call("edge link metrics", GET_EDGE_LINK_METRICS,
                new MetricsGetEdgeLinkMetrics().enterpriseId(enterpriseId).edgeId(edgeId)
                        .interval(new Interval().start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                                .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault())))
                        .metrics(EDGE_LINK_METRICS));
        return metrics.stream().collect(Collectors.toMap(MetricsGetEdgeLinkMetricsResultItem::getLinkId,VelocloudApiEdgeClientV1::map));
    }

    @Override
    public List<MetricsApp> getEdgeAppMetrics(final Instant start, final Instant end) throws VelocloudApiException {


        final List<MetricsGetEdgeAppMetricsResultItem> metrics = this.api.call("edge applications metrics", GET_EDGE_APP_METRICS,
                new MetricsGetEdgeAppMetrics().edgeId(edgeId).enterpriseId(enterpriseId).metrics(EDGE_TRAFFIC_METRICS)
                        .interval(new Interval().start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                                .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault()))));
        //TODO map result
        return null;
    }

    @Override
    public List<MetricsEdgeQoe> getEdgeQoeMetrics(final Instant start, final Instant end) throws VelocloudApiException {
        this.api.call("edge QoE score metrics", GET_LINK_QUALITY_EVENTS,
                new LinkQualityEventGetLinkQualityEvents().edgeId(edgeId).enterpriseId(enterpriseId).links()
                        .interval(new Interval().start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                                .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault()))));
        //TODO map result
        return null;
    }

    @Override
    public List<MetricsEdgeQoe> getEdgeQoeComponentsMetrics(final Instant start, final Instant end) throws VelocloudApiException {
        this.api.call("edge QoE component metrics", ,
                new
                        .interval(new Interval().start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                                .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault()))));
        //TODO map result
        return null;
    }

    @Override
    public MetricsEdgeSystem getEdgeSystemMetrics(final Instant start, final Instant end) throws VelocloudApiException {
        final EdgeStatusMetricsSummary metrics = this.api.call("edge system metrics", GET_EDGE_SYSTEM_METRICS,
                new MetricsGetEdgeStatusMetrics().enterpriseId(enterpriseId).edgeId(edgeId).metrics(EDGE_SYSTEM_METRICS)
                        .interval(new Interval().start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                                .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault()))));
        return map(metrics);
    }

    private static MetricsEdgeLink map(MetricsGetEdgeLinkMetricsResultItem item) {
        return MetricsEdgeLink.builder()
                .withLinkId(item.getLinkId())
                .withBpsOfBestPathRx(item.getBpsOfBestPathRx())
                .withBpsOfBestPathTx(item.getBpsOfBestPathTx())
                .build();
    }

    private static MetricsEdgeSystem map(EdgeStatusMetricsSummary item) {
        return MetricsEdgeSystem.builder()
                .withCpuPct(map(item.getCpuPct()))
                .withCpuCoreTemp(map(item.getCpuCoreTemp()))
                .withMemoryPct(map(item.getMemoryPct()))
                .withFlowCount(map(item.getFlowCount()))
                .withHandoffQueueDrops(map(item.getHandoffQueueDrops()))
                .withTunnelCount(map(item.getTunnelCount()))
                .withTunnelCountV6(map(item.getTunnelCountV6()))
                .build();
    }

    private static Aggregate map(BasicMetricSummary metric) {
        return Aggregate.builder()
                .withMin(metric.getMin())
                .withMax(metric.getMax())
                .withAverage(metric.getAverage())
                .build();
    }
}
