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

package org.opennms.velocloud.collection.gateway;

import static org.opennms.velocloud.collection.AbstractVelocloudCollectorFactory.PREFIX_VELOCLOUD;
import static org.opennms.velocloud.pollers.gateway.AbstractGatewayStatusPoller.ATTR_GATEWAY_ID;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.collectors.CollectionRequest;
import org.opennms.integration.api.v1.collectors.CollectionSet;
import org.opennms.integration.api.v1.collectors.resource.NodeResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSet;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSetResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableNodeResource;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.client.api.model.MetricsGateway;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.collection.AbstractVelocloudServiceCollector;
import org.opennms.velocloud.connections.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VelocloudGatewayCollector extends AbstractVelocloudServiceCollector {

    private static final Logger LOG = LoggerFactory.getLogger(VelocloudGatewayCollector.class);

    public VelocloudGatewayCollector(ClientManager clientManager, ConnectionManager connectionManager) {
        super(clientManager, connectionManager);
    }

    @Override
    public void initialize() {
    }

    @Override
    public CompletableFuture<CollectionSet> collect(CollectionRequest request, Map<String, Object> attributes) {
        final MetricsGateway gatewayMetrics;
        final VelocloudApiPartnerClient client;
        try {
            //ensure we have a gateway
            Objects.requireNonNull(attributes.get(ATTR_GATEWAY_ID));

            Integer gatewayId = Integer.parseInt(attributes.get("gatewayId").toString());
            client = getPartnerClient(attributes);
            gatewayMetrics = client.getGatewayMetrics(gatewayId);
        } catch (RuntimeException | VelocloudApiException ex) {
            LOG.warn("Error collecting Velocloud data for gateway {} with id {}, Message: {}",
                    attributes.get(PREFIX_VELOCLOUD + ATTR_GATEWAY_ID), attributes.get(PREFIX_VELOCLOUD + "id"), ex.getMessage());
            return  CompletableFuture.failedFuture(ex);
        }

        final ImmutableNodeResource nodeResource = ImmutableNodeResource.newBuilder().setNodeId(request.getNodeId()).build();

        final ImmutableCollectionSetResource.Builder<NodeResource> gatewayAttrBuilder =
                ImmutableCollectionSetResource.newBuilder(NodeResource.class).setResource(nodeResource);

        addAggregate(gatewayAttrBuilder, "velocloud-gateway-cpu-pct", "CpuPercentage", gatewayMetrics.getCpuPercentage());
        addAggregate(gatewayAttrBuilder, "velocloud-gateway-handoff-queue-drop", "HandoffQueueDrops", gatewayMetrics.getHandoffQueueDrops());
        addAggregate(gatewayAttrBuilder, "velocloud-gateway-memory-pct", "MemoryUsage", gatewayMetrics.getMemoryUsage());
        addAggregate(gatewayAttrBuilder, "velocloud-gateway-flow-count", "FlowCount", gatewayMetrics.getFlowCount());
        addAggregate(gatewayAttrBuilder, "velocloud-gateway-tunnel-count", "TunnelCount", gatewayMetrics.getTunnelCount());
        addAggregate(gatewayAttrBuilder, "velocloud-gateway-tunnel-count-v6", "TunnelCountV6", gatewayMetrics.getTunnelCountV6());

        return CompletableFuture.completedFuture(ImmutableCollectionSet.newBuilder()
                .addCollectionSetResource(gatewayAttrBuilder.build()).setStatus(CollectionSet.Status.SUCCEEDED).build());
    }
}
