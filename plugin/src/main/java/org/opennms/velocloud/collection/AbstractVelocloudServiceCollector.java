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

package org.opennms.velocloud.collection;

import static org.opennms.velocloud.pollers.AbstractStatusPoller.ATTR_API_KEY;
import static org.opennms.velocloud.pollers.AbstractStatusPoller.ATTR_ORCHESTRATOR_URL;

import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.collectors.immutables.ImmutableNumericAttribute;
import org.opennms.integration.api.v1.collectors.immutables.ImmutableStringAttribute;
import org.opennms.integration.api.v1.collectors.resource.NumericAttribute;
import org.opennms.integration.api.v1.collectors.resource.Resource;
import org.opennms.integration.api.v1.collectors.resource.StringAttribute;
import org.opennms.integration.api.v1.collectors.resource.immutables.ImmutableCollectionSetResource;
import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.client.api.VelocloudServiceCollector;
import org.opennms.velocloud.client.api.model.Aggregate;
import org.opennms.velocloud.client.api.model.Traffic;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.connections.ConnectionManager;

public abstract class AbstractVelocloudServiceCollector implements VelocloudServiceCollector {

    protected final ClientManager clientManager;
    protected final ConnectionManager connectionManager;

    public AbstractVelocloudServiceCollector(ClientManager clientManager, ConnectionManager connectionManager) {
        this.clientManager = clientManager;
        this.connectionManager = connectionManager;
    }

    public static void addNumAttr(ImmutableCollectionSetResource.Builder<? extends Resource> builder, String groupId,
                                  String name, Number value) {
        if(value != null) {
            builder.addNumericAttribute(createNumAttr(groupId, name, value.doubleValue()));
        }

    }

    public static void addNumAttr(ImmutableCollectionSetResource.Builder<? extends Resource> builder, String groupId,
                            String name, Number value, long milliseconds) {
        if(value != null) {
            builder.addNumericAttribute(createNumAttr(groupId, name, value.doubleValue() * 1000 / milliseconds));
        }
    }

    public static StringAttribute createStringAttribute(String groupId, String name, String value) {
        return ImmutableStringAttribute.newBuilder().setGroup(groupId).setName(name).setValue(value).build();
    }

    public static NumericAttribute createNumAttr(String groupId, String name, double value) {
        return ImmutableNumericAttribute.newBuilder().setGroup(groupId).setName(name).setValue(value)
                .setType(NumericAttribute.Type.GAUGE).build();
    }

    public static void addAggregate(ImmutableCollectionSetResource.Builder<? extends Resource> builder, String groupId,
                              String prefix, Aggregate aggregate) {
        if(aggregate != null) {
            addNumAttr(builder, groupId, prefix + "Min", aggregate.getMin());
            addNumAttr(builder, groupId, prefix + "Max", aggregate.getMax());
            addNumAttr(builder, groupId, prefix + "Average", aggregate.getAverage());
        }
    }

    public static void addTraffic(ImmutableCollectionSetResource.Builder<? extends Resource> builder, String groupId,
                            String prefix, Traffic traffic, long milliseconds) {
        if(traffic != null) {
            addNumAttr(builder, groupId, prefix + "BytesRx", traffic.getBytesRx(), milliseconds);
            addNumAttr(builder, groupId, prefix + "BytesTx", traffic.getBytesTx(), milliseconds);
            addNumAttr(builder, groupId, prefix + "PacketsRx", traffic.getPacketsRx(), milliseconds);
            addNumAttr(builder, groupId, prefix + "PacketsTx", traffic.getPacketsTx(), milliseconds);
        }
    }

    protected VelocloudApiPartnerClient getPartnerClient(Map<String, Object> attributes) {
        try {
            return clientManager.getPartnerClient(new VelocloudApiClientCredentials(
                    Objects.requireNonNull(attributes.get(ATTR_ORCHESTRATOR_URL),"orchestrator url required").toString(),
                    Objects.requireNonNull(attributes.get(ATTR_API_KEY),"api key required").toString()));
        } catch (VelocloudApiException e) {
            throw new RuntimeException(e);
        }
    }

    protected VelocloudApiCustomerClient getCustomerClient(Map<String, Object> attributes) {
        try {
            return clientManager.getCustomerClient(new VelocloudApiClientCredentials(
                    Objects.requireNonNull(attributes.get(ATTR_ORCHESTRATOR_URL),"orchestrator url required").toString(),
                    Objects.requireNonNull(attributes.get(ATTR_API_KEY),"api key required").toString()));
        } catch (VelocloudApiException e) {
            throw new RuntimeException(e);
        }
    }

}
