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

package org.opennms.velocloud.collection.edge;

import static java.util.Objects.requireNonNull;
import static org.opennms.velocloud.pollers.edge.AbstractEdgeStatusPoller.ATTR_EDGE_ID;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.collectors.CollectionRequest;
import org.opennms.integration.api.v1.collectors.CollectionSet;
import org.opennms.integration.api.v1.collectors.resource.GenericTypeResource;
import org.opennms.integration.api.v1.collectors.resource.NodeResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSet;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSetResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableGenericTypeResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableNodeResource;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.client.api.model.MetricsEdge;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.collection.AbstractVelocloudServiceCollector;
import org.opennms.velocloud.connections.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VelocloudEdgeCollector extends AbstractVelocloudServiceCollector {
    private static final Logger LOG = LoggerFactory.getLogger(VelocloudEdgeCollector.class);

    public VelocloudEdgeCollector(ClientManager clientManager, ConnectionManager connectionManager) {
        super(clientManager, connectionManager);
    }

    @Override
    public void initialize() {
    }

    @Override
    public CompletableFuture<CollectionSet> collect(CollectionRequest request, Map<String, Object> attributes) {

        final var edgeId = Objects.requireNonNull(attributes.get(ATTR_EDGE_ID),
                "Missing attribute: " + ATTR_EDGE_ID);

        final VelocloudApiCustomerClient client;
        final Optional<Edge> edge;

        try {
            client = getCustomerClient(attributes);
            edge = client.getEdges().stream()
                    .filter(e -> Objects.equals(e.logicalId, edgeId))
                    .findAny();
        } catch (VelocloudApiException ex) {
            return  CompletableFuture.failedFuture(ex);
        }

        final ImmutableNodeResource nodeResource = ImmutableNodeResource.newBuilder().setNodeId(request.getNodeId()).build();

        if (edge.isEmpty()) {
            return createFailedCollectionSet(nodeResource, Instant.now().toEpochMilli());
        }

        int milliseconds = Integer.parseInt((String) requireNonNull(attributes.get(SERVICE_INTERVAL), "Missing attribute: " + SERVICE_INTERVAL));

        final MetricsEdge edgeMetrics;
        try {
            edgeMetrics = client.getEdgeMetrics(edge.get().edgeId, milliseconds);
        } catch (VelocloudApiException ex) {
            return  CompletableFuture.failedFuture(ex);
        }

        final ImmutableCollectionSetResource.Builder<NodeResource> edgeAttrBuilder =
                ImmutableCollectionSetResource.newBuilder(NodeResource.class).setResource(nodeResource);

        addAggregate(edgeAttrBuilder, "velocloud-edge-cpu", "EdgeCpuPct", edgeMetrics.getCpuPct());
        addAggregate(edgeAttrBuilder, "velocloud-edge-cpu", "EdgeCpuCoreTemp", edgeMetrics.getCpuCoreTemp());
        addAggregate(edgeAttrBuilder, "velocloud-edge-memory-pct", "EdgeMemoryPct", edgeMetrics.getMemoryPct());
        addAggregate(edgeAttrBuilder, "velocloud-edge-flow-count", "EdgeFlowCnt", edgeMetrics.getFlowCount());
        addAggregate(edgeAttrBuilder, "velocloud-edge-queue-drops", "EdgeQueueDrops", edgeMetrics.getHandoffQueueDrops());
        addAggregate(edgeAttrBuilder, "velocloud-edge-tunnel", "EdgeTunnelCnt", edgeMetrics.getTunnelCount());
        addAggregate(edgeAttrBuilder, "velocloud-edge-tunnel", "EdgeTunnelCntV6", edgeMetrics.getTunnelCountV6());

        final ImmutableCollectionSet.Builder resultBuilder = ImmutableCollectionSet.newBuilder();
        resultBuilder.addCollectionSetResource(edgeAttrBuilder.build());

        edgeMetrics.getTrafficApplications().forEach( appTraffic -> {
            final ImmutableCollectionSetResource.Builder<GenericTypeResource> appResourceBuilder =
                    ImmutableCollectionSetResource.newBuilder(GenericTypeResource.class).setResource(
                            ImmutableGenericTypeResource.newBuilder().setNodeResource(nodeResource)
                                    .setType("VelocloudAppTraffic")
                                    .setInstance(appTraffic.getName())
                                    .build())
                            .addStringAttribute(createStringAttribute("velocloud-app", "application", appTraffic.getName()))
                            .addStringAttribute(createStringAttribute("velocloud-app", "edgeName", edge.get().name));

            addTraffic(appResourceBuilder, "velocloud-app-traffic", "", appTraffic.getTraffic(), milliseconds);

            resultBuilder.addCollectionSetResource(appResourceBuilder.build());
        });

        return CompletableFuture.completedFuture(resultBuilder.setStatus(CollectionSet.Status.SUCCEEDED)
                .setTimestamp(edgeMetrics.getTimestamp()).build());
    }
}
