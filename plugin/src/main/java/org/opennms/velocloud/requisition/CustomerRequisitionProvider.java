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
import org.opennms.integration.api.v1.config.requisition.Requisition;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisition;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionInterface;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionMetaData;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionMonitoredService;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionNode;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.internal.Utils;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.connections.Connection;
import org.opennms.velocloud.connections.ConnectionManager;

import com.google.common.base.Strings;

public class CustomerRequisitionProvider extends AbstractRequisitionProvider<CustomerRequisitionProvider.Request> {

    public final static String TYPE = "velocloud-customer";
    
    public static final String PARAMETER_ENTERPRISE_ID = "enterpriseId";

    public CustomerRequisitionProvider(final ClientManager clientManager,
                                       final ConnectionManager connectionManager) {
        super(clientManager, connectionManager);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    protected Request createRequest(final Connection connection, final Map<String, String> parameters) {
        final var request = new Request(connection);

        if (parameters.containsKey(PARAMETER_ENTERPRISE_ID)) {
            request.setEnterpriseId(Integer.parseInt(parameters.get(PARAMETER_ENTERPRISE_ID)));
        }

        return request;
    }

    @Override
    protected Requisition handleRequest(final RequestContext context) throws VelocloudApiException {
        final var client = (context.getRequest().enterpriseId != null)
                           ? context.getPartnerClient().getCustomerClient(context.getRequest().enterpriseId)
                           : context.getCustomerClient();

        final var requisition = ImmutableRequisition.newBuilder()
                                                    .setForeignSource(context.getForeignSource());

        for (var edge : client.getEdges()) {
            final var node = ImmutableRequisitionNode.newBuilder()
                                                     .setForeignId(edge.name)
                                                     .setNodeLabel(edge.name);
            node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                         .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                         .setKey("alias")
                                                         .setValue(context.getAlias())
                                                         .build());
            node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                         .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                         .setKey("operator")
                                                         .setValue(edge.operator)
                                                         .build());
            node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                         .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                         .setKey("logicalEdgeId")
                                                         .setValue(edge.logicalId)
                                                         .build());
            node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                         .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                         .setKey("edgeId")
                                                         .setValue(String.valueOf(edge.edgeId))
                                                         .build());
            node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                         .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                         .setKey("hub")
                                                         .setValue(String.valueOf(edge.hub))
                                                         .build());
            node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                         .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                         .setKey("alertsEnabled")
                                                         .setValue(String.valueOf(edge.alertsEnabled))
                                                         .build());
            node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                         .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                         .setKey("customInfo")
                                                         .setValue(edge.customInfo)
                                                         .build());
            node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                         .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                         .setKey("description")
                                                         .setValue(edge.description)
                                                         .build());
            node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                         .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                         .setKey("deviceFamily")
                                                         .setValue(edge.deviceFamily)
                                                         .build());
            node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                         .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                         .setKey("deviceId")
                                                         .setValue(edge.deviceId)
                                                         .build());
            if (edge.dnsName != null) {
                node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                             .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                             .setKey("dnsName")
                                                             .setValue(edge.dnsName)
                                                             .build());
            }
            if (edge.lteRegion != null) {
                node.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                             .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                             .setKey("lteRegion")
                                                             .setValue(edge.lteRegion)
                                                             .build());
            }

            {
                final var iface = ImmutableRequisitionInterface.newBuilder()
                                                               .setIpAddress(Utils.getValidInetAddress("0.0.0.0"))
                                                               .setDescription("Edge Meta");
                iface.addMonitoredService("VelocloudEdgeConnection");
                iface.addMonitoredService("VelocloudEdgeService");

                for(final var path : client.getPaths(edge.edgeId)) {
                    final var service = ImmutableRequisitionMonitoredService
                            .newBuilder()
                                    .setName(String.format("VelocloudEdgePath-%s", path.peerName))
                                    .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                            .setContext(VELOCLOUD_METADATA_CONTEXT)
                                            .setKey("deviceLogicalId")
                                            .setValue(path.deviceLogicalId)
                                            .build())
                                    .build();

                    iface.addMonitoredService(service);
                }

                node.addInterface(iface.build());
            }

            for (final var link : edge.links) {
                final var iface = ImmutableRequisitionInterface.newBuilder()
                                                               .setIpAddress(Utils.getValidInetAddress(link.ipAddress))
                                                               .setDescription(link.displayName);
                iface.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                              .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                              .setKey("linkId")
                                                              .setValue(link.logicalId)
                                                              .build());
                iface.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                              .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                              .setKey("interface")
                                                              .setValue(link._interface)
                                                              .build());
                if (link.macAddress != null) {
                    iface.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                                  .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                                  .setKey("macAddress")
                                                                  .setValue(link.macAddress)
                                                                  .build());
                }
                if (link.netmask != null) {
                    iface.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                                  .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                                  .setKey("netmask")
                                                                  .setValue(link.netmask)
                                                                  .build());
                }
                if (link.networkSide != null) {
                    iface.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                                  .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                                  .setKey("networkSide")
                                                                  .setValue(link.networkSide)
                                                                  .build());
                }
                if (link.networkType != null) {
                    iface.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                                  .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                                  .setKey("networkType")
                                                                  .setValue(link.networkType)
                                                                  .build());
                }
                if (link.isp != null) {
                    iface.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                                  .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                                  .setKey("isp")
                                                                  .setValue(link.isp)
                                                                  .build());
                }
                if (link.org != null) {
                    iface.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                                  .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                                  .setKey("org")
                                                                  .setValue(link.org)
                                                                  .build());
                }
                iface.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                              .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                              .setKey("lat")
                                                              .setValue(String.valueOf(link.lat))
                                                              .build());
                iface.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                              .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                              .setKey("lon")
                                                              .setValue(String.valueOf(link.lon))
                                                              .build());
                if (!Strings.isNullOrEmpty(link.ipv6Address)) {
                    iface.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                                  .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                                  .setKey("ipv6Address")
                                                                  .setValue(link.ipv6Address)
                                                                  .build());
                }
                if (!Strings.isNullOrEmpty(link.linkMode)) {
                    iface.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                                                  .setContext(VELOCLOUD_METADATA_CONTEXT)
                                                                  .setKey("linkMode")
                                                                  .setValue(link.linkMode)
                                                                  .build());
                }

                iface.addMonitoredService("VelocloudLinkConnection");
                iface.addMonitoredService("VelocloudLinkService");

                node.addInterface(iface.build());
            }

            node.addAsset("description", Strings.nullToEmpty(edge.description));
            node.addAsset("operatingSystem", Strings.isNullOrEmpty(edge.softwareVersion) ? "" : String.format("VMware SD-WAN %s", edge.softwareVersion));
            node.addAsset("address1", Strings.nullToEmpty(edge.address));
            node.addAsset("address2", Strings.nullToEmpty(edge.address2));
            node.addAsset("city", Strings.nullToEmpty(edge.city));
            node.addAsset("state", Strings.nullToEmpty(edge.state));
            node.addAsset("zip", Strings.nullToEmpty(edge.zip));
            node.addAsset("country", Strings.nullToEmpty(edge.country));

            if (edge.longitude != null) {
                node.addAsset("longitude", String.valueOf(edge.longitude));
            }
            if (edge.latitude != null) {
                node.addAsset("latitude", String.valueOf(edge.latitude));
            }

            requisition.addNode(node.build());
        }

        return requisition.build();
    }

    public static class Request extends AbstractRequisitionProvider.Request {

        private Integer enterpriseId;

        public Request() {
        }

        public Request(final Connection connection) {
            super(VELOCLOUD_CUSTOMER_IDENTIFIER, connection);
        }

        public Integer getEnterpriseId() {
            return this.enterpriseId;
        }

        public void setEnterpriseId(final Integer enterpriseId) {
            this.enterpriseId = enterpriseId;
        }
    }
}
