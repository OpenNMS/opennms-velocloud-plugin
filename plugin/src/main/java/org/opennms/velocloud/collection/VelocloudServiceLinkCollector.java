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

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.collectors.CollectionRequest;
import org.opennms.integration.api.v1.collectors.CollectionSet;
import org.opennms.integration.api.v1.collectors.immutables.ImmutableNumericAttribute;
import org.opennms.integration.api.v1.collectors.resource.CollectionSetResource;
import org.opennms.integration.api.v1.collectors.resource.GenericTypeResource;
import org.opennms.integration.api.v1.collectors.resource.IpInterfaceResource;
import org.opennms.integration.api.v1.collectors.resource.NumericAttribute;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSet;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSetResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableGenericTypeResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableIpInterfaceResource;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableNodeResource;
import org.opennms.integration.api.v1.pollers.Status;
import org.opennms.integration.api.v1.pollers.immutables.ImmutablePollerResult;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudServiceCollector;
import org.opennms.velocloud.client.api.model.MetricsTraffic;

public class VelocloudServiceLinkCollector implements VelocloudServiceCollector {

    private final VelocloudApiCustomerClient client;

    public VelocloudServiceLinkCollector(VelocloudApiCustomerClient client) {
        this.client = Objects.requireNonNull(client);
    }

    @Override
    public void initialize() {
    }

    @Override
    public CompletableFuture<CollectionSet> collect(CollectionRequest request, Map<String, Object> attributes) {
TODO
        //now()
        //now()-5min
        
        final var edgeId = Objects.requireNonNull(attributes.get(ATTR_EDGE_ID),  // liefert eien String zurück
                "Missing attribute: " + ATTR_EDGE_ID);
        final var linkId = Objects.requireNonNull(attributes.get(ATTR_LINK_ID),
                "Missing attribute: " + ATTR_LINK_ID);

        final var link = this.client.getEdgeAppTraffic()
                getEdges().stream()
                .filter(e -> Objects.equals(e.logicalId, edgeId))
                .flatMap(e -> e.links.stream())
                .filter(l -> Objects.equals(l.logicalId, linkId))
                .findAny();

        if (link.isEmpty()) {
            return CompletableFuture.completedFuture(ImmutablePollerResult.newBuilder()
                    .setStatus(Status.Down)
                    .setReason("No link with id " + edgeId + "/" + linkId)
                    .build());
        }

        return CompletableFuture.completedFuture(this.poll(link.get()));        
        
        
        
        final ImmutableNodeResource nodeResource = ImmutableNodeResource.newBuilder().setNodeId(request.getNodeId()).build();
        final ImmutableIpInterfaceResource interfaceResource =
                ImmutableIpInterfaceResource.newBuilder().setNodeResource(nodeResource).setInstance(request.getAddress().toString()/*todo check format*/).build();
        final CollectionSetResource<IpInterfaceResource> trBytes = ImmutableCollectionSetResource.newBuilder(IpInterfaceResource.class).setResource(interfaceResource).addNumericAttribute(
                ImmutableNumericAttribute.newBuilder().setGroup("velocloud-trafic-someting...").setName("trBytes")
                        .setValue(123.0/*todo*/).setType(NumericAttribute.Type.GAUGE)
                        .build()).build();//todo what unit of measure?    todo pro attribute todo pro sekunde

        final ImmutableCollectionSet.Builder builder = ImmutableCollectionSet.newBuilder().addCollectionSetResource(trBytes);






        MetricsTraffic mt;
        mt.getPerApplication().forEach( (name, appTraffic)-> {
                    final CollectionSetResource<GenericTypeResource> appResource = ImmutableCollectionSetResource.newBuilder(GenericTypeResource.class).setResource(
                                    ImmutableGenericTypeResource.newBuilder().setNodeResource(nodeResource)
                                            .setType("VelocloudAppTraffic")
                                            .setInstance(name)
                                            .build()
                            )
                            .addNumericAttribute(ImmutableNumericAttribute.newBuilder().setGroup("VelCloAppGroup")
                                    .setName("bytesRx")
                                    .setValue((double) appTraffic.getBytesTx()).build())
                            .addNumericAttribute(ImmutableNumericAttribute.newBuilder().setGroup("VelCloAppGroup")
                                    .setName("bytesTx")
                                    .setValue((double) appTraffic.getBytesTx()).build())
                            .addNumericAttribute(ImmutableNumericAttribute.newBuilder().setGroup("VelCloAppGroup")
                                    .setName("packetsRx")
                                    .setValue((double) appTraffic.getBytesTx()).build())
                            .addNumericAttribute(ImmutableNumericAttribute.newBuilder().setGroup("VelCloAppGroup")
                                    .setName("packetsTx")
                                    .setValue((double) appTraffic.getBytesTx()).build())

                            .build();

builder.addCollectionSetResource(appResource);
                }


        );

        builder.build();
        return new CompletableFuture<>();
    }
}
