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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.client.api.internal.Utils;
import org.opennms.velocloud.client.api.model.Customer;
import org.opennms.velocloud.client.api.model.Gateway;
import org.opennms.velocloud.client.api.model.PartnerEvent;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyEnterprises;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyEnterprisesResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyGateways;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyGatewaysResultItem;
import org.opennms.velocloud.client.v1.model.EventGetProxyEvents;
import org.opennms.velocloud.client.v1.model.EventGetProxyEventsResult;
import org.opennms.velocloud.client.v1.model.Interval;

public class VelocloudApiPartnerClientV1 implements VelocloudApiPartnerClient {

    public final static ApiCall<EnterpriseProxyGetEnterpriseProxyGateways, List<EnterpriseProxyGetEnterpriseProxyGatewaysResultItem>>
            ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_GATEWAYS = AllApi::enterpriseProxyGetEnterpriseProxyGateways;
    public final static ApiCall<EnterpriseProxyGetEnterpriseProxyEnterprises, List<EnterpriseProxyGetEnterpriseProxyEnterprisesResultItem>>
            ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_ENTERPRISES = AllApi::enterpriseProxyGetEnterpriseProxyEnterprises;
    public final static ApiCall<EventGetProxyEvents, EventGetProxyEventsResult>
            EVENT_GET_PROXY_EVENTS = AllApi::eventGetProxyEvents;

    private final ApiExecutor executor;
    private final int enterpriseProxyId;
    private final VelocloudApiClientCredentials credentials;
    private final AllApi allApi;

    final VelocloudCall<EnterpriseProxyGetEnterpriseProxyGateways,
            List<EnterpriseProxyGetEnterpriseProxyGatewaysResultItem>> gatewaysCall;
    private final VelocloudCall<EnterpriseProxyGetEnterpriseProxyEnterprises,
            List<EnterpriseProxyGetEnterpriseProxyEnterprisesResultItem>> customersCall;
    private final VelocloudCall<EventGetProxyEvents, EventGetProxyEventsResult> eventsCall;

    public VelocloudApiPartnerClientV1(ApiExecutor executor,
                                       final int enterpriseProxyId,
                                       VelocloudApiClientCredentials credentials, AllApi allApi) {
        this.executor = executor;
        this.enterpriseProxyId = enterpriseProxyId;
        this.credentials = credentials;
        this.allApi = allApi;

        this.gatewaysCall = executor.createApiCall("gateways", ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_GATEWAYS, credentials, allApi);
        this.customersCall = executor.createApiCall("customers", ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_ENTERPRISES, credentials, allApi);
        this.eventsCall = executor.createApiCall("events", EVENT_GET_PROXY_EVENTS, credentials, allApi);
    }

    @Override
    public VelocloudApiCustomerClient getCustomerClient(final Integer enterpriseId) {
        return new VelocloudApiCustomerClientV1(executor, enterpriseId, credentials, allApi);
    }

    @Override
    public List<Gateway> getGateways() throws VelocloudApiException {
        final var enterpriseGateways = gatewaysCall.call(
                new EnterpriseProxyGetEnterpriseProxyGateways().enterpriseProxyId(this.enterpriseProxyId)
                        .addWithItem(EnterpriseProxyGetEnterpriseProxyGateways.WithEnum.SITE)
        );

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
        final var enterprises = customersCall.call(
                new EnterpriseProxyGetEnterpriseProxyEnterprises().enterpriseProxyId(this.enterpriseProxyId)
                        .addWithItem(EnterpriseProxyGetEnterpriseProxyEnterprises.WithEnum.EDGES)
        );

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
        final EventGetProxyEventsResult events = eventsCall.call(
                new EventGetProxyEvents().interval(new Interval()
                        .start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                        .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault()))
                )
        );

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
}
