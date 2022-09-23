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
package org.opennms.velocloud.rest.api;

import com.google.common.base.Strings;
import org.opennms.integration.api.v1.scv.Credentials;
import org.opennms.integration.api.v1.scv.SecureCredentialsVault;
import org.opennms.velocloud.client.api.VelocloudApiClient;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.connections.Connection;
import org.opennms.velocloud.connections.ConnectionManager;
import org.opennms.velocloud.connections.ConnectionValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.Response;

import java.util.Optional;
import java.util.UUID;

import static org.opennms.velocloud.common.VelocloudCommon.VELOCLOUD_TOKEN;
import static org.opennms.velocloud.common.VelocloudCommon.VELOCLOUD_URL;

public class AbstractVelocloudRestService implements VelocloudRestService {

    private final VelocloudApiClientProvider clientProvider;

    private final ConnectionManager connectionManager;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractVelocloudRestService.class);

    public AbstractVelocloudRestService(final VelocloudApiClientProvider clientProvider, final ConnectionManager connectionManager) {
        this.clientProvider = clientProvider;
        this.connectionManager = connectionManager;
    }

    @Override
    public Response getMspPartnerConnections(String alias) {
        throw new NotSupportedException();
    }

    @Override
    public Response getCustomersForMspPartner(String alias) {
        throw new NotSupportedException();
    }

    @Override
    public Response getStatus() {
        throw new NotSupportedException();
    }

    protected VelocloudApiClient getClientFor(final String alias) throws ConnectionValidationError, VelocloudApiException {

        Optional<Connection> connection = this.connectionManager.getConnection(alias);
        final String url = connection.get().getOrchestratorUrl();
        final String token = connection.get().getApiKey();
        if (Strings.isNullOrEmpty(url) || Strings.isNullOrEmpty(token)) {
            throw new ConnectionValidationError(alias, "Couldn't find Velocloud credentials for alias");
        }
        return clientProvider.connect(url, token);
    }

    protected Optional<UUID> getEnterpriseId(final String alias) throws ConnectionValidationError {
        return this.connectionManager.getConnection(alias).get().getEnterpriseId();
    }
}
