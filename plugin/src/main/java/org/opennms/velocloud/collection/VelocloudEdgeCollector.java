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

package org.opennms.velocloud.collection;

import static org.opennms.velocloud.pollers.link.AbstractLinkStatusPoller.ATTR_EDGE_ID;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
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
import org.opennms.velocloud.client.api.model.MetricsEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VelocloudEdgeCollector extends AbstractVelocloudServiceCollector {
    private static final Logger LOG = LoggerFactory.getLogger(VelocloudEdgeCollector.class);

    private final VelocloudApiCustomerClient client;

    public VelocloudEdgeCollector(VelocloudApiCustomerClient client) {
        this.client = Objects.requireNonNull(client);
    }

    @Override
    public void initialize() {
    }

    @Override
    public CompletableFuture<CollectionSet> collect(CollectionRequest request, Map<String, Object> attributes) {

        Integer edgeId = null;
        final MetricsEdge edgeMetrics;
        final long timestamp = Instant.now().toEpochMilli();

        try {
            edgeId = Integer.parseInt(Objects.requireNonNull(attributes.get(ATTR_EDGE_ID),
                    "Missing attribute: " + ATTR_EDGE_ID).toString());
            edgeMetrics = this.client.getEdgeMetrics(edgeId);
        } catch (RuntimeException | VelocloudApiException ex) {
            LOG.warn("Error collecting Velocloud data for edge {}, Error: {}", edgeId, ex.toString());
            return  CompletableFuture.failedFuture(ex);
        }

        if (edgeMetrics == null) {
            return CompletableFuture.completedFuture(ImmutableCollectionSet.newBuilder()
                    .setStatus(CollectionSet.Status.FAILED).setTimestamp(timestamp).build());
        }

        final ImmutableNodeResource nodeResource = ImmutableNodeResource.newBuilder().setNodeId(request.getNodeId()).build();

        final ImmutableCollectionSetResource.Builder<NodeResource> edgeAttrBuilder =
                ImmutableCollectionSetResource.newBuilder(NodeResource.class).setResource(nodeResource);

        final int milliseconds = this.client.getIntervalMillis();

        addAggregate(edgeAttrBuilder, "velocloud-edge-cpu-pct", "CpuPct", edgeMetrics.getCpuPct());
        addAggregate(edgeAttrBuilder, "velocloud-edge-cpu-core-temp", "CpuCoreTemp", edgeMetrics.getCpuCoreTemp());
        addAggregate(edgeAttrBuilder, "velocloud-edge-memory-pct", "MemoryPct", edgeMetrics.getMemoryPct());
        addAggregate(edgeAttrBuilder, "velocloud-edge-flow-count", "FlowCount", edgeMetrics.getFlowCount());
        addAggregate(edgeAttrBuilder, "velocloud-edge-queue-drops", "QueueDrops", edgeMetrics.getHandoffQueueDrops());
        addAggregate(edgeAttrBuilder, "velocloud-edge-tunnel-count", "TunnelCount", edgeMetrics.getTunnelCount());
        addAggregate(edgeAttrBuilder, "velocloud-edge-tunnel-countV6", "TunnelCountV6", edgeMetrics.getTunnelCountV6());

        final ImmutableCollectionSet.Builder resultBuilder = ImmutableCollectionSet.newBuilder();
        resultBuilder.addCollectionSetResource(edgeAttrBuilder.build());

        edgeMetrics.getTrafficApplications().forEach( appTraffic -> {
            final ImmutableCollectionSetResource.Builder<GenericTypeResource> appResourceBuilder =
                    ImmutableCollectionSetResource.newBuilder(GenericTypeResource.class).setResource(
                            ImmutableGenericTypeResource.newBuilder().setNodeResource(nodeResource)
                                    .setType("VelocloudAppTraffic")
                                    .setInstance(appTraffic.getName())
                                    .build())
                    .addStringAttribute(createStringAttribute("velocloud-edge-app-traffic", "DisplayName", appTraffic.getDisplayName()))
                    .addStringAttribute(createStringAttribute("velocloud-edge-app-traffic", "ApplicationClass", appTraffic.getApplicationClass()))
                    .addStringAttribute(createStringAttribute("velocloud-edge-app-traffic", "Description", appTraffic.getDescription()));

            addTraffic(appResourceBuilder, "velocloud-app-traffic", appTraffic.getName(), appTraffic.getTraffic(), milliseconds);

            resultBuilder.addCollectionSetResource(appResourceBuilder.build());
        });

        return CompletableFuture.completedFuture(resultBuilder.setStatus(CollectionSet.Status.SUCCEEDED).build());
    }
}
