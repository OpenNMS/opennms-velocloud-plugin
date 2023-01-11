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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.CustomerEvent;
import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.client.api.model.Link;
import org.opennms.velocloud.client.api.model.Path;
import org.opennms.velocloud.client.api.model.Tunnel;
import org.opennms.velocloud.client.api.model.User;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.model.EdgeGetEdgeSdwanPeers;
import org.opennms.velocloud.client.v1.model.EdgeGetEdgeSdwanPeersResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdges;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdgesResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseUsers;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseUsersResultItem;
import org.opennms.velocloud.client.v1.model.EventGetEnterpriseEvents;
import org.opennms.velocloud.client.v1.model.EventGetEnterpriseEventsResult;
import org.opennms.velocloud.client.v1.model.Interval;
import org.opennms.velocloud.client.v1.model.MonitoringGetEnterpriseEdgeNvsTunnelStatusBody;
import org.opennms.velocloud.client.v1.model.MonitoringGetEnterpriseEdgeNvsTunnelStatusResultItem;

public class VelocloudApiCustomerClientV1 implements VelocloudApiCustomerClient {

    public final static ApiCache.Endpoint<EnterpriseGetEnterpriseEdges, List<EnterpriseGetEnterpriseEdgesResultItem>>
            ENTERPRISE_GET_ENTERPRISE_EDGES = AllApi::enterpriseGetEnterpriseEdges;
    public final static ApiCache.Endpoint<EnterpriseGetEnterpriseUsers, List<EnterpriseGetEnterpriseUsersResultItem>>
            ENTERPRISE_GET_ENTERPRISE_USERS = AllApi::enterpriseGetEnterpriseUsers;
    public final static ApiCache.Endpoint<EventGetEnterpriseEvents, EventGetEnterpriseEventsResult>
            EVENT_GET_ENTERPRISE_EVENTS  = AllApi::eventGetEnterpriseEvents;
    public final static ApiCache.Endpoint<MonitoringGetEnterpriseEdgeNvsTunnelStatusBody, List<MonitoringGetEnterpriseEdgeNvsTunnelStatusResultItem>>
            MONITORING_GET_ENTERPRISE_EDGE_NVS_TUNNEL_STATUS = AllApi::monitoringGetEnterpriseEdgeNvsTunnelStatus;

    public final static ApiCache.Endpoint<EdgeGetEdgeSdwanPeers, List<EdgeGetEdgeSdwanPeersResultItem>>
            EDGE_GET_EDGE_SDWAN_PEERS = AllApi::edgeGetEdgeSdwanPeers;

    private final ApiCache.Api api;

    private final int enterpriseId;

    public VelocloudApiCustomerClientV1(final ApiCache.Api api,
                                        final int enterpriseId) {
        this.api = Objects.requireNonNull(api);
        this.enterpriseId = enterpriseId;
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
    public List<Tunnel> getNvsTunnels() throws VelocloudApiException {
        final var tunnels = this.api.call("nvs tunnels", MONITORING_GET_ENTERPRISE_EDGE_NVS_TUNNEL_STATUS,
                new MonitoringGetEnterpriseEdgeNvsTunnelStatusBody().enterpriseId(this.enterpriseId));

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
