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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.config.requisition.Requisition;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisition;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionInterface;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionMetaData;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisitionNode;
import org.opennms.velocloud.client.api.VelocloudApiClient;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PartnerRequisitionProvider extends AbstractRequisitionProvider<PartnerRequisitionProvider.Request> {

    private static final Logger LOG = LoggerFactory.getLogger(PartnerRequisitionProvider.class);

    public final static String TYPE = "VelocloudPartnerRequisition";

    public PartnerRequisitionProvider(final VelocloudApiClientProvider clientProvider) {
        super(clientProvider);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    protected Request createRequest(final Map<String, String> parameters) {
        final var request = new Request();

        request.setForeignSource("velocloud-partner");

        return request;
    }

    @Override
    protected Requisition handleRequest(final Request request, final VelocloudApiClient client) {
        final var requisition = ImmutableRequisition.newBuilder()
                .setForeignSource(request.getForeignSource());

        try {
            for (var enterprise : client.getEnterprises()) {
                final List<User> users = client.getUsers(enterprise.id);

                //TODO: confirm  gateway roles are coming from enterprise users
                //TODO: confirm Gateway AssetNodes destination.
                client.getGateways(enterprise.enterpriseId).stream().forEach(gateway -> {
                    requisition.addNode(ImmutableRequisitionNode.newBuilder()
                            .setForeignId(gateway.gatewayId)
                            .setNodeLabel(gateway.gatewayName)
                            .setLocation(gateway.siteName)
                            .setInterfaces(List.of(ImmutableRequisitionInterface.newBuilder()
                                    .setIpAddress(gateway.ipAddress)
                                    .build()))
                            .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                    .setContext(VELOCLOUD_METADATA_CONTEXT)
                                    .setKey("roles")
                                    .setValue(users.stream().map(u ->
                                            String.format("%s:%s", u.roleId, u.roleName)).collect(Collectors.joining("\n")))
                                    .build())
                            .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                    .setContext(VELOCLOUD_METADATA_CONTEXT)
                                    .setKey("deviceId")
                                    .setValue(gateway.deviceId)
                                    .build())
                            .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                    .setContext(VELOCLOUD_METADATA_CONTEXT)
                                    .setKey("gatewayId")
                                    .setValue(gateway.gatewayId)
                                    .build())
                            .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                    .setContext(VELOCLOUD_METADATA_CONTEXT)
                                    .setKey("gatewayName")
                                    .setValue(gateway.gatewayName)
                                    .build())
                            .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                    .setContext(VELOCLOUD_METADATA_CONTEXT)
                                    .setKey("enterpriseId")
                                    .setValue(gateway.enterpriseId.toString())
                                    .build())
                            .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                    .setContext(VELOCLOUD_METADATA_CONTEXT)
                                    .setKey("buildNumber")
                                    .setValue(gateway.buildNumber)
                                    .build())
                            .addMetaData(ImmutableRequisitionMetaData.newBuilder()
                                    .setContext(VELOCLOUD_METADATA_CONTEXT)
                                    .setKey("softwareVersion")
                                    .setValue(gateway.softwareVersion)
                                    .build())
                            .build());
                });

            }
        } catch (VelocloudApiException e) {
            LOG.error("Building requisition failed" ,e);
        }
        return requisition.build();
    }

    public static class Request extends AbstractRequisitionProvider.Request {
    }
}
