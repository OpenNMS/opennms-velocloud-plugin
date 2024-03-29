/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2023 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2023 The OpenNMS Group, Inc.
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

package org.opennms.velocloud.collection.link;

import static java.util.Objects.requireNonNull;
import static org.opennms.velocloud.pollers.link.AbstractLinkStatusPoller.ATTR_EDGE_ID;
import static org.opennms.velocloud.pollers.link.AbstractLinkStatusPoller.ATTR_LINK_ID;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.collectors.CollectionRequest;
import org.opennms.integration.api.v1.collectors.CollectionSet;
import org.opennms.integration.api.v1.collectors.resource.IpInterfaceResource;
import org.opennms.integration.api.v1.collectors.resource.NodeResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSet;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSetResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableIpInterfaceResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableNodeResource;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Link;
import org.opennms.velocloud.client.api.model.MetricsLink;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.collection.AbstractVelocloudServiceCollector;
import org.opennms.velocloud.connections.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.InetAddresses;

public class VelocloudLinkCollector extends AbstractVelocloudServiceCollector {

    private static final Logger LOG = LoggerFactory.getLogger(VelocloudLinkCollector.class);

    public VelocloudLinkCollector(ClientManager clientManager, ConnectionManager connectionManager) {
        super(clientManager, connectionManager);
    }

    @Override
    public void initialize() {
    }

    @Override
    public CompletableFuture<CollectionSet> collect(CollectionRequest request, Map<String, Object> attributes) {
        final var edgeId = Objects.requireNonNull(attributes.get(ATTR_EDGE_ID),
                "Missing attribute: " + ATTR_EDGE_ID);
        final var linkId = Objects.requireNonNull(attributes.get(ATTR_LINK_ID),
                "Missing attribute: " + ATTR_LINK_ID);

        final VelocloudApiCustomerClient client;
        final Optional<Link> link;
        try {
            client = getCustomerClient(attributes);
            link = client.getEdges().stream()
                    .filter(e -> Objects.equals(e.logicalId, edgeId))
                    .flatMap(e -> e.links.stream())
                    .filter(l -> Objects.equals(l.logicalId, linkId))
                    .findAny();
        } catch (VelocloudApiException ex) {
            return  CompletableFuture.failedFuture(ex);
        }

        final ImmutableNodeResource nodeResource = ImmutableNodeResource.newBuilder().setNodeId(request.getNodeId()).build();
        final ImmutableIpInterfaceResource interfaceResource = ImmutableIpInterfaceResource.newBuilder()
                .setNodeResource(nodeResource).setInstance(InetAddresses.toAddrString(request.getAddress()))
                .build();

        if (link.isEmpty()) {
            return createFailedCollectionSet(nodeResource, Instant.now().toEpochMilli());
        }

        int milliseconds = Integer.parseInt((String) requireNonNull(attributes.get(SERVICE_INTERVAL), "Missing attribute: " + SERVICE_INTERVAL));

        final MetricsLink linkMetrics;
        try {
            linkMetrics = client.getLinkMetrics(link.get().edgeId, link.get().internalId, milliseconds);
        } catch (VelocloudApiException ex) {
            return  CompletableFuture.failedFuture(ex);
        }

        final ImmutableCollectionSetResource.Builder<IpInterfaceResource> linkAttrBuilder =
                ImmutableCollectionSetResource.newBuilder(IpInterfaceResource.class).setResource(interfaceResource);

        addNumAttr(linkAttrBuilder, "velocloud-link-bandwidth", "BandwidthRx", linkMetrics.getBandwidthRx());
        addNumAttr(linkAttrBuilder, "velocloud-link-bandwidth", "BandwidthTx", linkMetrics.getBandwidthTx());
        addNumAttr(linkAttrBuilder, "velocloud-link-latency", "BestLatencyMsRx", linkMetrics.getBestLatencyMsRx());
        addNumAttr(linkAttrBuilder, "velocloud-link-latency", "BestLatencyMsTx", linkMetrics.getBestLatencyMsTx());
        addNumAttr(linkAttrBuilder, "velocloud-link-jitter", "BestJitterMsRx", linkMetrics.getBestJitterMsRx());
        addNumAttr(linkAttrBuilder, "velocloud-link-jitter", "BestJitterMsTx", linkMetrics.getBestJitterMsTx());
        addNumAttr(linkAttrBuilder, "velocloud-link-loss-pct", "BestLossPctRx", linkMetrics.getBestLossPctRx());
        addNumAttr(linkAttrBuilder, "velocloud-link-loss.pct", "BestLossPctRx", linkMetrics.getBestLossPctTx());

        addNumAttr(linkAttrBuilder, "velocloud-link-score", "ScoreRx", linkMetrics.getScoreRx());
        addNumAttr(linkAttrBuilder, "velocloud-link-score", "ScoreTx", linkMetrics.getScoreTx());

        addTraffic(linkAttrBuilder, "velocloud-link-traffic", "P1", linkMetrics.getTrafficPriority1(), milliseconds);
        addTraffic(linkAttrBuilder, "velocloud-link-traffic", "P2", linkMetrics.getTrafficPriority2(), milliseconds);
        addTraffic(linkAttrBuilder, "velocloud-link-traffic", "P3", linkMetrics.getTrafficPriority3(), milliseconds);
        addTraffic(linkAttrBuilder, "velocloud-link-traffic", "Control", linkMetrics.getTrafficControl(), milliseconds);

        return CompletableFuture.completedFuture(ImmutableCollectionSet.newBuilder()
                .setStatus(CollectionSet.Status.SUCCEEDED)
                .setTimestamp(linkMetrics.getTimestamp())
                .addCollectionSetResource(linkAttrBuilder.build())
                .build()
        );
    }
}
