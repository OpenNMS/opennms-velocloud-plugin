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
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.config.requisition.Requisition;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisition;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionInterface;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionMetaData;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionNode;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.internal.Utils;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.connections.Connection;
import org.opennms.velocloud.connections.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class CustomerRequisitionProvider extends AbstractRequisitionProvider<CustomerRequisitionProvider.Request> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerRequisitionProvider.class);

    public final static String TYPE = "velocloud-customer";
    
    public static final String PARAMETER_ENTERPRISE_ID = "enterpriseId";

    public static final String VELOCLOUD_LINK_SERVICE_NAME = "VelocloudLink";

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

        try {
            for (var edge : client.getEdges()) {
                final var node = ImmutableRequisitionNode.newBuilder()
                        .setForeignId(edge.logicalId)
                        .setNodeLabel(edge.name)
                        .setLocation(edge.site)
                        .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                .setContext(VELOCLOUD_METADATA_CONTEXT)
                                .setKey("operator")
                                .setValue(edge.operator)
                                .build())
                        .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                .setContext(VELOCLOUD_METADATA_CONTEXT)
                                .setKey("hub")
                                .setValue(String.valueOf(edge.hub))
                                .build())
                        .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                .setContext(VELOCLOUD_METADATA_CONTEXT)
                                .setKey("alertsEnabled")
                                .setValue(String.valueOf(edge.alertsEnabled))
                                .build())
                        .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                .setContext(VELOCLOUD_METADATA_CONTEXT)
                                .setKey("customInfo")
                                .setValue(edge.customInfo)
                                .build())
                        .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                .setContext(VELOCLOUD_METADATA_CONTEXT)
                                .setKey("description")
                                .setValue(edge.description)
                                .build())
                        .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                .setContext(VELOCLOUD_METADATA_CONTEXT)
                                .setKey("deviceFamily")
                                .setValue(edge.deviceFamily)
                                .build())
                        .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                .setContext(VELOCLOUD_METADATA_CONTEXT)
                                .setKey("deviceId")
                                .setValue(edge.deviceId)
                                .build())
                        .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                .setContext(VELOCLOUD_METADATA_CONTEXT)
                                .setKey("dnsName")
                                .setValue(edge.dnsName)
                                .build())
                        .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                .setContext(VELOCLOUD_METADATA_CONTEXT)
                                .setKey("lteRegion")
                                .setValue(edge.lteRegion)
                                .build())
                        .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                .setContext(VELOCLOUD_METADATA_CONTEXT)
                                .setKey("logicalId")
                                .setValue(edge.logicalId)
                                .build())
                        .setInterfaces(edge.links.stream().map(link -> {
                            final var intf =ImmutableRequisitionInterface.newBuilder()
                                    .setIpAddress(Utils.getValidInetAddress(link.ipAddress))
                                    .setDescription(link.displayName)
                                    .addMonitoredService(VELOCLOUD_LINK_SERVICE_NAME)
                                    .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                            .setContext(VELOCLOUD_METADATA_CONTEXT)
                                            .setKey("edgeId")
                                            .setValue(String.valueOf(link.edgeId))
                                            .build())
                                    .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                            .setContext(VELOCLOUD_METADATA_CONTEXT)
                                            .setKey("logicalId")
                                            .setValue(link.logicalId)
                                            .build())
                                    .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                            .setContext(VELOCLOUD_METADATA_CONTEXT)
                                            .setKey("internalId")
                                            .setValue(link.internalId)
                                            .build())
                                    .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                            .setContext(VELOCLOUD_METADATA_CONTEXT)
                                            .setKey("interface")
                                            .setValue(link._interface)
                                            .build())
                                    .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                            .setContext(VELOCLOUD_METADATA_CONTEXT)
                                            .setKey("macAddress")
                                            .setValue(link.macAddress)
                                            .build())
                                    .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                            .setContext(VELOCLOUD_METADATA_CONTEXT)
                                            .setKey("netmask")
                                            .setValue(link.netmask)
                                            .build())
                                    .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                            .setContext(VELOCLOUD_METADATA_CONTEXT)
                                            .setKey("networkSide")
                                            .setValue(link.networkSide)
                                            .build())
                                    .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                            .setContext(VELOCLOUD_METADATA_CONTEXT)
                                            .setKey("networkType")
                                            .setValue(link.networkType)
                                            .build())
                                    .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                            .setContext(VELOCLOUD_METADATA_CONTEXT)
                                            .setKey("isp")
                                            .setValue(link.isp)
                                            .build())
                                    .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                            .setContext(VELOCLOUD_METADATA_CONTEXT)
                                            .setKey("org")
                                            .setValue(link.org)
                                            .build())
                                    .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                            .setContext(VELOCLOUD_METADATA_CONTEXT)
                                            .setKey("lat")
                                            .setValue(String.valueOf(link.lat))
                                            .build())
                                    .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                            .setContext(VELOCLOUD_METADATA_CONTEXT)
                                            .setKey("lon")
                                            .setValue(String.valueOf(link.lon))
                                            .build());
                            if (!Strings.isNullOrEmpty(link.ipv6Address)) {
                                intf.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                    .setContext(VELOCLOUD_METADATA_CONTEXT)
                                    .setKey("ipv6Address")
                                    .setValue(link.ipv6Address)
                                    .build());
                            }
                            if (!Strings.isNullOrEmpty(link.linkMode)) {
                                intf.addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                        .setContext(VELOCLOUD_METADATA_CONTEXT)
                                        .setKey("linkMode")
                                        .setValue(link.linkMode)
                                        .build());
                            }
                            return intf.build();
                        }).collect(Collectors.toList()))
                        .build();
                requisition.addNode(node);
            }
        }
        catch (VelocloudApiException vae) {
            LOG.error("Unable to build requisition", vae);
        }

        return requisition.build();
    }

    public static class Request extends AbstractRequisitionProvider.Request {

        private Integer enterpriseId;

        public Request() {
        }

        public Request(final Connection connection) {
            super("velocloud-customer", connection);
        }

        public Integer getEnterpriseId() {
            return this.enterpriseId;
        }

        public void setEnterpriseId(final Integer enterpriseId) {
            this.enterpriseId = enterpriseId;
        }
    }
}
