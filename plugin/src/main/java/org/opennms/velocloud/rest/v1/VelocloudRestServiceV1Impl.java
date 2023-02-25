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
package org.opennms.velocloud.rest.v1;

import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.connections.ConnectionManager;
import org.opennms.velocloud.rest.api.VelocloudRestService;
import org.opennms.velocloud.rest.dto.ConnectionDTO;
import org.opennms.velocloud.rest.dto.ConnectionListElementDTO;
import org.opennms.velocloud.rest.dto.ConnectionStateDTO;
import org.opennms.velocloud.rest.dto.EnterpriseDTO;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VelocloudRestServiceV1Impl implements VelocloudRestService {

    private final ConnectionManager connectionManager;

    public VelocloudRestServiceV1Impl(final ConnectionManager connectionManager) {
        this.connectionManager = Objects.requireNonNull(connectionManager);
    }

    @Override
    public List<EnterpriseDTO> getCustomersForMspPartner(final String alias) throws VelocloudApiException {
        final var client = this.connectionManager.getPartnerClient(alias)
                                                 .orElseThrow(null);
        return client.getCustomers().stream()
                     .map(customer -> Mapper.ENTERPRISE_INSTANCE.sourceToTarget(customer))
                     .collect(Collectors.toList());
    }

    @Override
    public List<ConnectionListElementDTO> getConnectionList() {
        return this.connectionManager.getAliases().stream()
                .map(alias -> {
                    var connection = this.connectionManager.getConnection(alias).orElseThrow();

                    final var connectionDTO = new ConnectionListElementDTO();
                    connectionDTO.setAlias(alias);
                    connectionDTO.setOrchestratorUrl(connection.getOrchestratorUrl());

                    return connectionDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Response addConnection(ConnectionDTO connectionDTO, boolean dryRun, boolean skipValidation) {
        if (this.connectionManager.getConnection(connectionDTO.getAlias()).isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Connection already exists")
                           .build();
        }

        final var connection = this.connectionManager.newConnection(connectionDTO.getAlias(),
                                             connectionDTO.getOrchestratorUrl(),
                                             connectionDTO.getApiKey());

        if (!skipValidation) {
            final var error = connection.validate();
            if (error.isPresent()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity(String.format("Failed to validate credentials: %s", error.get().message))
                               .build();
            }
        }

        if (dryRun) {
            return Response.ok()
                           .entity("Dry run successful")
                           .build();
        }

        connection.save();

        return Response.ok()
                       .entity("Connection successfully added")
                       .build();

    }

    @Override
    public Response editConnection(final String alias, final ConnectionDTO connectionDTO, boolean skipValidation) {
        final var connection = this.connectionManager.getConnection(alias);
        if (connection.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No such connection exists")
                           .build();
        }

        connection.get().setOrchestratorUrl(connectionDTO.getOrchestratorUrl());
        connection.get().setApiKey(connectionDTO.getApiKey());

        if (!skipValidation) {
            final var error = connection.get().validate();
            if (error.isPresent()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity(String.format("Failed to validate credentials: %s", error.get().message))
                               .build();
            }
        }

        connection.get().save();

        return Response.ok().entity("Connection successfully updated").build();
    }

    @Override
    public Response validateConnection(final String alias) {
        final var connection = this.connectionManager.getConnection(alias);
        if (connection.isPresent()) {
            final var response = new ConnectionStateDTO();
            response.setAlias(alias);
            response.setOrchestratorUrl(connection.get().getOrchestratorUrl());
            response.setValid(connection.get().validate().isEmpty());

            return Response.ok()
                           .entity(response)
                           .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No such connection exists")
                           .build();
        }
    }

    @Override
    public Response deleteConnection(String alias) {
        if (this.connectionManager.deleteConnection(alias)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No such connection exists")
                    .build();
        }
    }
}
