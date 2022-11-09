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

import static org.opennms.velocloud.client.v1.FunctionRefsHolder.ENTERPRISE_GET_ENTERPRISE_EDGES;
import static org.opennms.velocloud.client.v1.FunctionRefsHolder.ENTERPRISE_GET_ENTERPRISE_USERS;
import static org.opennms.velocloud.client.v1.FunctionRefsHolder.EVENT_GET_ENTERPRISE_EVENTS;
import static org.opennms.velocloud.client.v1.FunctionRefsHolder.MONITORING_GET_ENTERPRISE_EDGE_NVS_TUNNEL_STATUS;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.CustomerEvent;
import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.client.api.model.Link;
import org.opennms.velocloud.client.api.model.Tunnel;
import org.opennms.velocloud.client.api.model.User;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdges;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseUsers;
import org.opennms.velocloud.client.v1.model.EventGetEnterpriseEvents;
import org.opennms.velocloud.client.v1.model.EventGetEnterpriseEventsResult;
import org.opennms.velocloud.client.v1.model.Interval;
import org.opennms.velocloud.client.v1.model.MonitoringGetEnterpriseEdgeNvsTunnelStatusBody;

public class VelocloudApiCustomerClientV1 implements VelocloudApiCustomerClient {

    final ApiExecutor executor;
    private final int enterpriseId;
    private final VelocloudApiClientCredentials credentials;

    public VelocloudApiCustomerClientV1(final ApiExecutor executor,
                                        final int enterpriseId,
                                        final VelocloudApiClientCredentials credentials) {
        this.executor = executor;
        this.enterpriseId = enterpriseId;
        this.credentials = credentials;
    }

    @Override
    public List<Edge> getEdges() throws VelocloudApiException {
        final var edges = executor.get(
                "edges",
                ENTERPRISE_GET_ENTERPRISE_EDGES,
                credentials,
                new EnterpriseGetEnterpriseEdges()
                        .enterpriseId(this.enterpriseId)
                        .addWithItem(EnterpriseGetEnterpriseEdges.WithEnum.SITE)
                        .addWithItem(EnterpriseGetEnterpriseEdges.WithEnum.CONFIGURATION)
                        .addWithItem(EnterpriseGetEnterpriseEdges.WithEnum.LINKS)
        );

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
                                  .withEdgeState(e.getEdgeState().getValue())
                                  .withServiceState(e.getServiceState().getValue())
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
                                                            .withLinkState(l.getState().getValue())
                                                            .withServiceState(l.getServiceState().getValue())
                                                            .build())
                                              .collect(Collectors.toList()))
                                  .build())
                    .collect(Collectors.toList());
    }

    @Override
    public List<User> getUsers() throws VelocloudApiException {
        final var users = executor.get(
                "users",
                ENTERPRISE_GET_ENTERPRISE_USERS,
                credentials,
                new EnterpriseGetEnterpriseUsers().enterpriseId(this.enterpriseId)
        );

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
        final EventGetEnterpriseEventsResult events = executor.get(
                "events",
                EVENT_GET_ENTERPRISE_EVENTS,
                credentials,
                new EventGetEnterpriseEvents().enterpriseId(this.enterpriseId).interval(new Interval()
                        .start(OffsetDateTime.ofInstant(start, ZoneId.systemDefault()))
                        .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault())))
        );

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
    public List<Tunnel> getNvsTunnels() throws VelocloudApiException {
        final var tunnels = executor.get(
                "nvs tunnels",
                MONITORING_GET_ENTERPRISE_EDGE_NVS_TUNNEL_STATUS,
                credentials,
                new MonitoringGetEnterpriseEdgeNvsTunnelStatusBody().enterpriseId(this.enterpriseId)
        );

        return tunnels.stream()
                      .map(tunnel -> Tunnel.builder()
                                           .withId(tunnel.getId())
                                           .withTag(tunnel.getTag())
                                           .withDataKey(tunnel.getDataKey())
                                           .withState(tunnel.getState().getValue())
                                           .withLink(tunnel.getData().getLink())
                                           .withName(tunnel.getData().getName())
                                           .build())
                      .collect(Collectors.toList());
    }
}
