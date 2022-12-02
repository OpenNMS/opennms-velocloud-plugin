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
import java.util.Objects;

import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiGatewayClient;
import org.opennms.velocloud.client.api.model.Aggregate;
import org.opennms.velocloud.client.api.model.MetricsGateway;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.model.BasicMetricSummary;
import org.opennms.velocloud.client.v1.model.GatewayMetric;
import org.opennms.velocloud.client.v1.model.GatewayMetrics;
import org.opennms.velocloud.client.v1.model.GatewayStatusMetricsSummary;
import org.opennms.velocloud.client.v1.model.Interval;
import org.opennms.velocloud.client.v1.model.MetricsGetGatewayStatusMetrics;

public class VelocloudApiGatewayClientV1 implements VelocloudApiGatewayClient {

    public final static ApiCache.Endpoint<MetricsGetGatewayStatusMetrics, GatewayStatusMetricsSummary>
            GET_GATEWAY_STATUS_METRIC = AllApi::metricsGetGatewayStatusMetrics;

    public final static GatewayMetrics METRICS = new GatewayMetrics() {{
        add(GatewayMetric.TUNNELCOUNT);
        add(GatewayMetric.MEMORYPCT);
        add(GatewayMetric.FLOWCOUNT);
        add(GatewayMetric.CPUPCT);
        add(GatewayMetric.HANDOFFQUEUEDROPS);
        add(GatewayMetric.CONNECTEDEDGES);
        add(GatewayMetric.TUNNELCOUNTV6);
    }};

    private final ApiCache.Api api;
    private final int gatewayId;

    public VelocloudApiGatewayClientV1(final ApiCache.Api api, final int gatewayId) {
        this.api = Objects.requireNonNull(api);
        this.gatewayId = gatewayId;
    }

    @Override
    public MetricsGateway getMetrics(final Instant start, final Instant end) throws VelocloudApiException {

        final GatewayStatusMetricsSummary metrics = this.api.call("edges", GET_GATEWAY_STATUS_METRIC,
                new MetricsGetGatewayStatusMetrics()
                        .gatewayId(gatewayId)
                        .interval(new Interval().start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                                .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault())))
                        .metrics(METRICS));

        return map(metrics);
    }

    private MetricsGateway map(GatewayStatusMetricsSummary metrics) {
        return MetricsGateway.builder()
                .withCpuPercentage(map(metrics.getCpuPct()))
                .withMemoryUsage(map(metrics.getMemoryPct()))
                .withFlowCounts(map(metrics.getFlowCount()))
                .withCpuPercentage(map(metrics.getCpuPct()))
                .withHandoffQueueDrops(map(metrics.getHandoffQueueDrops()))
                .withTunnelCount(map(metrics.getTunnelCount()))
                .withTunnelCountV6(map(metrics.getTunnelCountV6())).build();
    }

    private Aggregate map(BasicMetricSummary metric) {
        return Aggregate.builder()
                .withMin(metric.getMin())
                .withMax(metric.getMax())
                .withAverage(metric.getAverage())
                .build();
    }
}
