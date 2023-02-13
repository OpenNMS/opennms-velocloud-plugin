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
import static org.opennms.velocloud.pollers.link.AbstractLinkStatusPoller.ATTR_LINK_ID;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.collectors.CollectionRequest;
import org.opennms.integration.api.v1.collectors.CollectionSet;
import org.opennms.integration.api.v1.collectors.resource.IpInterfaceResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSet;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSetResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableIpInterfaceResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableNodeResource;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.MetricsLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VelocloudLinkCollector extends AbstractVelocloudServiceCollector {

    private static final Logger LOG = LoggerFactory.getLogger(VelocloudLinkCollector.class);

    private final VelocloudApiCustomerClient client;

    public VelocloudLinkCollector(VelocloudApiCustomerClient client) {
        this.client = Objects.requireNonNull(client);
    }

    @Override
    public void initialize() {
    }

    @Override
    public CompletableFuture<CollectionSet> collect(CollectionRequest request, Map<String, Object> attributes) {

        Integer edgeId = null;
        String linkId = null;
        final MetricsLink linkMetrics;
        final long timestamp = Instant.now().toEpochMilli();

        try {
            edgeId = Integer.parseInt(Objects.requireNonNull(attributes.get(ATTR_EDGE_ID),
                    "Missing attribute: " + ATTR_EDGE_ID).toString());
            linkId = Objects.requireNonNull(attributes.get(ATTR_LINK_ID),
                    "Missing attribute: " + ATTR_LINK_ID).toString();
            linkMetrics = this.client.getLinkMetrics(edgeId, linkId);
        } catch (RuntimeException | VelocloudApiException ex) {
            LOG.warn("Error collecting Velocloud link data for link {}/{}, Error: {}", edgeId, linkId, ex.toString());
            return  CompletableFuture.failedFuture(ex);
        }

        if (linkMetrics == null) {
            return CompletableFuture.completedFuture(ImmutableCollectionSet.newBuilder()
                    .setStatus(CollectionSet.Status.FAILED).setTimestamp(timestamp).build());
        }

        final ImmutableNodeResource nodeResource = ImmutableNodeResource.newBuilder().setNodeId(request.getNodeId()).build();
        final ImmutableIpInterfaceResource interfaceResource =
                ImmutableIpInterfaceResource.newBuilder().setNodeResource(nodeResource).setInstance(request.getAddress().toString()/*todo check format*/).build();

        final ImmutableCollectionSetResource.Builder<IpInterfaceResource> linkAttrBuilder =
                ImmutableCollectionSetResource.newBuilder(IpInterfaceResource.class).setResource(interfaceResource);

        final int milliseconds = this.client.getIntervalMillis();

        addNumAttr(linkAttrBuilder, "velocloud-link-bandwidth", "bandwidthRx", linkMetrics.getBandwidthRx());
        addNumAttr(linkAttrBuilder, "velocloud-link-bandwidth", "bandwidthTx", linkMetrics.getBandwidthTx());
        addNumAttr(linkAttrBuilder, "velocloud-link-latency", "bestLatencyMsRx", linkMetrics.getBestLatencyMsRx());
        addNumAttr(linkAttrBuilder, "velocloud-link-latency", "bestLatencyMsTx", linkMetrics.getBestLatencyMsTx());
        addNumAttr(linkAttrBuilder, "velocloud-link-jitter", "bestJitterMsRx", linkMetrics.getBestJitterMsRx());
        addNumAttr(linkAttrBuilder, "velocloud-link-jitter", "bestJitterMsTx", linkMetrics.getBestJitterMsTx());
        addNumAttr(linkAttrBuilder, "velocloud-link-loss-pct", "bestLossPctRx", linkMetrics.getBestLossPctRx());
        addNumAttr(linkAttrBuilder, "velocloud-link-loss.pct", "bestLossPctRx", linkMetrics.getBestLossPctTx());
        addNumAttr(linkAttrBuilder, "velocloud-link-score", "scoreRx", linkMetrics.getScoreRx());
        addNumAttr(linkAttrBuilder, "velocloud-link-score", "scoreTx", linkMetrics.getScoreTx());

        addTraffic(linkAttrBuilder, "velocloud-link-traffic-p1", "p1", linkMetrics.getTrafficPriority1(), milliseconds);
        addTraffic(linkAttrBuilder, "velocloud-link-traffic-p2", "p2", linkMetrics.getTrafficPriority2(), milliseconds);
        addTraffic(linkAttrBuilder, "velocloud-link-traffic-p3", "p3", linkMetrics.getTrafficPriority3(), milliseconds);
        addTraffic(linkAttrBuilder, "velocloud-link-traffic-control", "control", linkMetrics.getTrafficControl(), milliseconds);

        return CompletableFuture.completedFuture(ImmutableCollectionSet.newBuilder()
                        .setStatus(CollectionSet.Status.SUCCEEDED)
                        .addCollectionSetResource(linkAttrBuilder.build())
                        .build()
        );
    }
}
