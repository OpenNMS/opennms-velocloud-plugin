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

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.client.api.model.Link;
import org.opennms.velocloud.client.api.model.User;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdges;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseUsers;

import org.opennms.velocloud.client.v1.VelocloudApiClientProviderV1.ApiCall;

public class VelocloudApiCustomerClientV1 implements VelocloudApiCustomerClient {

    private final AllApi api;
    private final int enterpriseId;

    public VelocloudApiCustomerClientV1(final AllApi api,
                                        final int enterpriseId,
                                        final Duration cacheDuration) {
        this.api = Objects.requireNonNull(api);
        this.enterpriseId = enterpriseId;
    }

    @Override
    public List<Edge> getEdges() throws VelocloudApiException {
        final var edges = ApiCall.call(this.api, "edges",
                                       AllApi::enterpriseGetEnterpriseEdges,
                                       new EnterpriseGetEnterpriseEdges()
                                               .enterpriseId(this.enterpriseId)
                                               .addWithItem(EnterpriseGetEnterpriseEdges.WithEnum.SITE)
                                               .addWithItem(EnterpriseGetEnterpriseEdges.WithEnum.CONFIGURATION)
                                               .addWithItem(EnterpriseGetEnterpriseEdges.WithEnum.LINKS));

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
                                  .withModelNumber(e.getModelNumber())
                                  .withName(e.getName())
                                  .withOperatorAlertsEnabled(e.getOperatorAlertsEnabled().getValue() != 0)
                                  .withSelfMacAddress(e.getSelfMacAddress())
                                  .withSiteId(e.getSiteId())
                                  .withSoftwareVersion(e.getSoftwareVersion())
                                  .withLinks(e.getLinks().stream()
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
                                                            .build())
                                              .collect(Collectors.toList()))
                                  .build())
                    .collect(Collectors.toList());
    }

    @Override
    public List<User> getUsers() throws VelocloudApiException {
        final var users = ApiCall.call(this.api, "users",
                                       AllApi::enterpriseGetEnterpriseUsers,
                                       new EnterpriseGetEnterpriseUsers()
                                               .enterpriseId(this.enterpriseId));

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
