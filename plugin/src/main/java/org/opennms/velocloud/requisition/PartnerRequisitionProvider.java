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

package org.opennms.velocloud.requisition;

import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.config.requisition.Requisition;
import org.opennms.integration.api.v1.config.requisition.SnmpPrimaryType;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisition;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionInterface;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionMetaData;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionMonitoredService;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionNode;
import org.opennms.velocloud.connections.Connection;
import org.opennms.velocloud.connections.ConnectionManager;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.clients.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class PartnerRequisitionProvider extends AbstractRequisitionProvider<PartnerRequisitionProvider.Request> {

    private static final Logger LOG = LoggerFactory.getLogger(PartnerRequisitionProvider.class);

    public final static String TYPE = "velocloud-partner";

    public PartnerRequisitionProvider(final ClientManager clientManager,
                                      final ConnectionManager connectionManager) {
        super(clientManager, connectionManager);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    protected Request createRequest(final Connection connection, final Map<String, String> parameters) {
        return new Request(connection);
    }

    @Override
    protected Requisition handleRequest(final RequestContext context) throws VelocloudApiException {
        final var client = context.getPartnerClient();

        final var requisition = ImmutableRequisition.newBuilder()
                                                    .setForeignSource(context.getForeignSource());

        try {
            for (final var gateway : client.getGateways()) {
                final var node = ImmutableRequisitionNode.newBuilder()
                                                         .setForeignId(gateway.gatewayName)
                                                         .setNodeLabel(gateway.gatewayName);
                node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                             .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                             .setKey("alias")
                                                             .setValue(context.getAlias())
                                                             .build());
                node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                             .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                             .setKey("roles")
                                                             .setValue(String.join("\n", gateway.roles))
                                                             .build());
                node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                             .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                             .setKey("deviceId")
                                                             .setValue(gateway.deviceId)
                                                             .build());
                node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                             .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                             .setKey("gatewayId")
                                                             .setValue(gateway.gatewayId)
                                                             .build());
                node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                             .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                             .setKey("id")
                                                             .setValue(String.valueOf(gateway.id))
                                                             .build());
                node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                             .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                             .setKey("gatewayName")
                                                             .setValue(gateway.gatewayName)
                                                             .build());
                node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                             .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                             .setKey("description")
                                                             .setValue(gateway.description)
                                                             .build());
                if (!Strings.isNullOrEmpty(gateway.dnsName)) {
                    node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                                 .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                                 .setKey("dnsName")
                                                                 .setValue(gateway.dnsName)
                                                                 .build());
                }
                node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                             .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                             .setKey("networkId")
                                                             .setValue(String.valueOf(gateway.networkId))
                                                             .build());
                node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                             .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                             .setKey("softwareVersion")
                                                             .setValue(gateway.softwareVersion)
                                                             .build());
                node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                             .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                             .setKey("buildNumber")
                                                             .setValue(gateway.buildNumber)
                                                             .build());

                final var iface = ImmutableRequisitionInterface.newBuilder()
                                                               .setIpAddress(gateway.ipAddress)
                                                               .setSnmpPrimary(SnmpPrimaryType.PRIMARY);
                if (gateway.privateIpAddress != null) {
                    iface.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                                  .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                                  .setKey("privateIpAddress")
                                                                  .setValue(Objects.toString(gateway.privateIpAddress))
                                                                  .build());
                }

                final var service = ImmutableRequisitionMonitoredService.newBuilder()
                                                                        .setName("VelocloudGateway");

                node.addInterface(iface.build());

                requisition.addNode(node.build());
            }
        } catch (VelocloudApiException e) {
            LOG.error("Building requisition failed", e);
        }
        return requisition.build();
    }

    public static class Request extends AbstractRequisitionProvider.Request {
        public Request() {
        }

        public Request(final Connection connection) {
            super(VELOCLOUD_PARNTER_IDENTIFIER, connection);
        }
    }
}
