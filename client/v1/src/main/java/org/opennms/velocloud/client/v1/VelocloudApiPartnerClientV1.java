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

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.client.api.internal.Utils;
import org.opennms.velocloud.client.api.model.Aggregate;
import org.opennms.velocloud.client.api.model.Customer;
import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.client.api.model.Gateway;
import org.opennms.velocloud.client.api.model.MetricsGateway;
import org.opennms.velocloud.client.api.model.PartnerEvent;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.model.BasicMetricSummary;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyEnterprises;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyEnterprisesResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyGateways;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyGatewaysResultItem;
import org.opennms.velocloud.client.v1.model.EventGetProxyEvents;
import org.opennms.velocloud.client.v1.model.EventGetProxyEventsResult;
import org.opennms.velocloud.client.v1.model.GatewayGetGatewayEdgeAssignments;
import org.opennms.velocloud.client.v1.model.GatewayGetGatewayEdgeAssignmentsResultItem;
import org.opennms.velocloud.client.v1.model.GatewayMetric;
import org.opennms.velocloud.client.v1.model.GatewayMetrics;
import org.opennms.velocloud.client.v1.model.GatewayStatusMetricsSummary;
import org.opennms.velocloud.client.v1.model.Interval;
import org.opennms.velocloud.client.v1.model.MetricsGetGatewayStatusMetrics;

public class VelocloudApiPartnerClientV1 implements VelocloudApiPartnerClient {

    public final static ApiCache.Endpoint<MetricsGetGatewayStatusMetrics, GatewayStatusMetricsSummary>
            GET_GATEWAY_STATUS_METRIC = AllApi::metricsGetGatewayStatusMetrics;
    public final static ApiCache.Endpoint<EnterpriseProxyGetEnterpriseProxyGateways, List<EnterpriseProxyGetEnterpriseProxyGatewaysResultItem>>
            ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_GATEWAYS = AllApi::enterpriseProxyGetEnterpriseProxyGateways;
    public final static ApiCache.Endpoint<EnterpriseProxyGetEnterpriseProxyEnterprises, List<EnterpriseProxyGetEnterpriseProxyEnterprisesResultItem>>
            ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_ENTERPRISES = AllApi::enterpriseProxyGetEnterpriseProxyEnterprises;
    public final static ApiCache.Endpoint<EventGetProxyEvents, EventGetProxyEventsResult>
            EVENT_GET_PROXY_EVENTS = AllApi::eventGetProxyEvents;
    public final static ApiCache.Endpoint<GatewayGetGatewayEdgeAssignments, List<GatewayGetGatewayEdgeAssignmentsResultItem>>
            GATEWAY_GET_GATEWAY_EDGE_ASSIGNMENTS = AllApi::gatewayGetGatewayEdgeAssignments;

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
    private final int enterpriseProxyId;

    private final int delayInMilliseconds;

    public VelocloudApiPartnerClientV1(final ApiCache.Api api,
                                       final int enterpriseProxyId, int delayInMilliseconds) {
        this.api = Objects.requireNonNull(api);
        this.enterpriseProxyId = enterpriseProxyId;
        this.delayInMilliseconds = delayInMilliseconds;
    }

    @Override
    public VelocloudApiCustomerClient getCustomerClient(final Integer enterpriseId) {
        return new VelocloudApiCustomerClientV1(this.api, enterpriseId, delayInMilliseconds);
    }

    @Override
    public List<Gateway> getGateways() throws VelocloudApiException {
        final var enterpriseGateways = this.api.call("gateways", ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_GATEWAYS,
                new EnterpriseProxyGetEnterpriseProxyGateways().enterpriseProxyId(this.enterpriseProxyId)
                        .addWithItem(EnterpriseProxyGetEnterpriseProxyGateways.WithEnum.SITE));

        return enterpriseGateways.stream()
                                 .map(g -> Gateway.builder()
                                                  .withDeviceId(g.getDeviceId())
                                                  .withDescription(g.getDescription())
                                                  .withDnsName(g.getDnsName())
                                                  .withGatewayId(g.getLogicalId())
                                                  .withId(g.getId())
                                                  .withIpAddress(Utils.getValidInetAddress(g.getIpAddress()))
                                                  .withIsLoadBalanceed(g.isIsLoadBalanced())
                                                  .withRoles(g.getRoles() == null
                                                             ? Collections.emptyList()
                                                             : g.getRoles().stream().map(r -> r.getGatewayRole().getValue()).collect(Collectors.toList()))
                                                  .withName(g.getName())
                                                  .withSiteId(g.getSiteId())
                                                  .withSiteName(g.getSite().getName())
                                                  .withAddress(g.getSite().getStreetAddress())
                                                  .withAddress2((g.getSite().getStreetAddress2()))
                                                  .withZip(g.getSite().getPostalCode())
                                                  .withCity(g.getSite().getCity())
                                                  .withState(g.getSite().getState())
                                                  .withCountry(g.getSite().getCountry())
                                                  .withLatitude(g.getSite().getLat())
                                                  .withLongitude(g.getSite().getLon())
                                                  .withGatewayState(g.getGatewayState().getValue())
                                                  .withServiceState(g.getServiceState().getValue())
                                                  .withBastionState(g.getBastionState().getValue())
                                                  .withBuildNumber(g.getBuildNumber())
                                                  .withSoftwareVersion(g.getSoftwareVersion())
                                                  .withNetworkId(g.getNetworkId())
                                                  .withPrivateIpAddress(Utils.getValidInetAddress(g.getPrivateIpAddress()))
                                                  .withConnectedEdges(g.getConnectedEdges() != null
                                                                      ? g.getConnectedEdges()
                                                                      : 0)
                                                  .build()
                                     )
                                 .collect(Collectors.toList());
    }

    @Override
    public List<Customer> getCustomers() throws VelocloudApiException {
        final var enterprises = this.api.call("customers", ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_ENTERPRISES,
                new EnterpriseProxyGetEnterpriseProxyEnterprises().enterpriseProxyId(this.enterpriseProxyId)
                        .addWithItem(EnterpriseProxyGetEnterpriseProxyEnterprises.WithEnum.EDGES));

        return enterprises.stream()
                          .map(e -> Customer.builder()
                                            .withEnterpriseId(e.getLogicalId())
                                            .withId(e.getId())
                                            .withAccountNumber(e.getAccountNumber())
                                            .withAlertsEnabled(e.getAlertsEnabled().getValue() == 1)
                                            .withBastionState(e.getBastionState().getValue())
                                            .withDescription(e.getDescription())
                                            .withDomain(e.getDomain())
                                            .withGatewayPoolId(e.getGatewayPoolId())
                                            .withLocale(e.getLocale())
                                            .withLongitude(e.getLon())
                                            .withLatitude(e.getLat())
                                            .withName(e.getName())
                                            .withNetworkId(e.getNetworkId())
                                            .withTimezone(e.getTimezone())
                                            .withAddress(e.getStreetAddress())
                                            .withOperatorAlertsEnabled(e.getOperatorAlertsEnabled().getValue() == 1)
                                            .withCity(e.getCity())
                                            .withCountry(e.getCountry())
                                            .withState(e.getState())
                                            .withZip(e.getPostalCode())
                                            .build())
                          .collect(Collectors.toList());
    }

    @Override
    public List<PartnerEvent> getEvents(final Instant start, final Instant end) throws VelocloudApiException {
        final EventGetProxyEventsResult events = this.api.call("events", EVENT_GET_PROXY_EVENTS,
                new EventGetProxyEvents().interval(new Interval()
                        .start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                        .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault()))));

        return events.getData().stream().map(
                        e -> PartnerEvent.builder()
                                .withDetail(e.getDetail())
                                .withCategory(e.getCategory().getValue())
                                .withEvent(e.getEvent())
                                .withId(e.getId())
                                .withEventTime(e.getEventTime())
                                .withEnterpriseName(e.getEnterpriseName())
                                .withProxyUsername(e.getProxyUsername())
                                .withGatewayName(e.getGatewayName())
                                .withNetworkName(e.getNetworkName())
                                .withMessage(e.getMessage())
                                .withSeverity(e.getSeverity().getValue())
                                .build())
                .collect(Collectors.toList());
    }

    public List<Edge> getEdgeAssignments(final int gatewayId) throws VelocloudApiException {
        final List<GatewayGetGatewayEdgeAssignmentsResultItem> assignedEdges = this.api.call(
                "gatewayEdgeAssignments",
                GATEWAY_GET_GATEWAY_EDGE_ASSIGNMENTS,
                new GatewayGetGatewayEdgeAssignments()
                        .gatewayId(gatewayId)
        );

        return assignedEdges.stream()
                .map(e -> Edge.builder()
                        .withLogicalId(e.getLogicalId())
                        .withEdgeId(e.getId())
                        .withName(e.getName())
                        .withEdgeState(e.getEdgeState().getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public MetricsGateway getGatewayMetrics(int gatewayId, int intervalMillis) throws VelocloudApiException {

        final GatewayStatusMetricsSummary metrics = this.api.call("Gateway metrics ", GET_GATEWAY_STATUS_METRIC,
                new MetricsGetGatewayStatusMetrics()
                        .gatewayId(gatewayId)
                        .interval(getInterval(intervalMillis, delayInMilliseconds))
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
        if (metric == null) {
            return null;
        }
        return Aggregate.builder()
                .withMin(metric.getMin())
                .withMax(metric.getMax())
                .withAverage(metric.getAverage())
                .build();
    }
}
