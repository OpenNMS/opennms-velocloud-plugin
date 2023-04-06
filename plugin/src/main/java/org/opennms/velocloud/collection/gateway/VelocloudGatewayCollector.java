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

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.collectors.CollectionRequest;
import org.opennms.integration.api.v1.collectors.CollectionSet;
import org.opennms.integration.api.v1.collectors.resource.NodeResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSet;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSetResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableNodeResource;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.client.api.model.Gateway;
import org.opennms.velocloud.client.api.model.MetricsGateway;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.collection.AbstractVelocloudServiceCollector;
import org.opennms.velocloud.connections.ConnectionManager;
import org.opennms.velocloud.pollers.gateway.AbstractGatewayStatusPoller;
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
        final var gatewayId = (String) requireNonNull(attributes.get(AbstractGatewayStatusPoller.ATTR_GATEWAY_ID),
                "Missing attribute: " + AbstractGatewayStatusPoller.ATTR_GATEWAY_ID);

        final ImmutableNodeResource nodeResource = ImmutableNodeResource.newBuilder().setNodeId(request.getNodeId()).build();
        final MetricsGateway gatewayMetrics;

        int milliseconds = Integer.parseInt((String) requireNonNull(attributes.get(SERVICE_INTERVAL), "Missing attribute: " + SERVICE_INTERVAL));
        try {
            VelocloudApiPartnerClient client = getPartnerClient(attributes);

            final Optional<Gateway> gateway = client.getGateways().stream()
                    .filter(gw -> Objects.equals(gw.gatewayId, gatewayId)).findAny();
            if (gateway.isEmpty()) {
                return createFailedCollectionSet(nodeResource, Instant.now().toEpochMilli());
            }
            gatewayMetrics = client.getGatewayMetrics(gateway.get().id, milliseconds);
        } catch (VelocloudApiException ex) {
            return  CompletableFuture.failedFuture(ex);
        }

        final ImmutableCollectionSetResource.Builder<NodeResource> gatewayAttrBuilder =
                ImmutableCollectionSetResource.newBuilder(NodeResource.class).setResource(nodeResource);

        addAggregate(gatewayAttrBuilder, "velocloud-gateway-cpu-pct", "GwCpuPct", gatewayMetrics.getCpuPercentage());
        addAggregate(gatewayAttrBuilder, "velocloud-gateway-handoff-queue-drop", "GwHandoffQueDrps", gatewayMetrics.getHandoffQueueDrops());
        addAggregate(gatewayAttrBuilder, "velocloud-gateway-memory-pct", "GwMemoryPct", gatewayMetrics.getMemoryUsage());
        addAggregate(gatewayAttrBuilder, "velocloud-gateway-flow-count", "GwFlowCnt", gatewayMetrics.getFlowCount());
        addAggregate(gatewayAttrBuilder, "velocloud-gateway-tunnel", "GwTunnelCnt", gatewayMetrics.getTunnelCount());
        addAggregate(gatewayAttrBuilder, "velocloud-gateway-tunnel", "GwTunnelCntV6", gatewayMetrics.getTunnelCountV6());

        return CompletableFuture.completedFuture(ImmutableCollectionSet.newBuilder()
                .addCollectionSetResource(gatewayAttrBuilder.build()).setStatus(CollectionSet.Status.SUCCEEDED)
                .setTimestamp(gatewayMetrics.getTimestamp()).build());
    }
}
