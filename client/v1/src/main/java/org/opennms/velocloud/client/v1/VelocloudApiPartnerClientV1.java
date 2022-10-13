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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.client.api.internal.Utils;
import org.opennms.velocloud.client.api.model.Customer;
import org.opennms.velocloud.client.api.model.Gateway;
import org.opennms.velocloud.client.api.model.User;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyEnterprises;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyGateways;
import org.opennms.velocloud.client.v1.VelocloudApiClientProviderV1.ApiCall;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyUsers;

public class VelocloudApiPartnerClientV1 implements VelocloudApiPartnerClient {

    private final AllApi api;
    private final int enterpriseProxyId;

    public VelocloudApiPartnerClientV1(final AllApi api,
                                       final int enterpriseProxyId) {
        this.api = Objects.requireNonNull(api);
        this.enterpriseProxyId = enterpriseProxyId;
    }

    @Override
    public VelocloudApiCustomerClient getCustomerClient(final Integer enterpriseId) {
        return new VelocloudApiCustomerClientV1(this.api, enterpriseId);
    }

    @Override
    public List<Gateway> getGateways() throws VelocloudApiException {
        final var enterpriseGateways = ApiCall.call(this.api, "gateways",
                                                    AllApi::enterpriseProxyGetEnterpriseProxyGateways,
                                                    new EnterpriseProxyGetEnterpriseProxyGateways()
                                                            .enterpriseProxyId(this.enterpriseProxyId)
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
                                                  .build()
                                     )
                                 .collect(Collectors.toList());
    }

    @Override
    public List<Customer> getCustomers() throws VelocloudApiException {
        final var enterprises = ApiCall.call(this.api, "customers",
                                             AllApi::enterpriseProxyGetEnterpriseProxyEnterprises,
                                             new EnterpriseProxyGetEnterpriseProxyEnterprises()
                                                     .enterpriseProxyId(this.enterpriseProxyId)
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
    public List<User> getUsers() throws VelocloudApiException {
        final var users = ApiCall.call(this.api, "users",
                AllApi::enterpriseProxyGetEnterpriseProxyUsers,
                new EnterpriseProxyGetEnterpriseProxyUsers());

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
}
