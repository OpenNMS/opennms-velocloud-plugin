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
import org.opennms.velocloud.connections.Connection;
import org.opennms.velocloud.connections.ConnectionManager;
import org.opennms.velocloud.rest.api.VelocloudRestService;
import org.opennms.velocloud.rest.dto.ConnectionDTO;
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
    public Response editPartnerConnection(final String alias, final ConnectionDTO connectionDTO) throws VelocloudApiException {
        connectionManager.validatePartnerCredentials(connectionDTO.getOrchestratorUrl(), connectionDTO.getApiKey());
        updateConnection(alias, connectionDTO);
        return Response.ok().build();
    }

    @Override
    public Response editCustomerConnection(final String alias, final ConnectionDTO connectionDTO) throws VelocloudApiException {
        connectionManager.validateCustomerCredentials(connectionDTO.getOrchestratorUrl(), connectionDTO.getApiKey());
        updateConnection(alias, connectionDTO);
        return Response.ok().build();
    }

    @Override
    public Response validatePartnerConnection(final String alias) throws VelocloudApiException {
        final var connection = connectionManager.getConnection(alias)
                .orElseThrow(() -> new VelocloudApiException("connection does not exist"));
        connectionManager.validatePartnerCredentials(connection.getOrchestratorUrl(), connection.getApiKey());
        return Response.ok().build();
    }

    @Override
    public Response validateCustomerConnection(final String alias) throws VelocloudApiException {
        final var connection = connectionManager.getConnection(alias)
                .orElseThrow(() -> new VelocloudApiException("connection does not exist"));
        connectionManager.validateCustomerCredentials(connection.getOrchestratorUrl(), connection.getApiKey());
        return Response.ok().build();
    }

    private void updateConnection(String alias, ConnectionDTO connectionDTO) throws VelocloudApiException {
        Connection connection = connectionManager.getConnection(alias)
                .orElseThrow(() -> new VelocloudApiException("connection does not exist"));
        connection.setOrchestratorUrl(connectionDTO.getOrchestratorUrl());
        connection.setApiKey(connectionDTO.getOrchestratorUrl());
        connection.save();

    }
}
