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

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.config.requisition.Requisition;
import org.opennms.integration.api.v1.requisition.RequisitionProvider;
import org.opennms.integration.api.v1.requisition.RequisitionRequest;
import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.connections.Connection;
import org.opennms.velocloud.connections.ConnectionManager;
import org.opennms.velocloud.connections.ConnectionValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractRequisitionProvider<Req extends AbstractRequisitionProvider.Request> implements RequisitionProvider {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRequisitionProvider.class);

    public static final String VELOCLOUD_METADATA_CONTEXT = "velocloud";

    public static final String VELOCLOUD_LINK_SERVICE_NAME = "VelocloudLink";

    private final ClientManager clientManager;

    private final ConnectionManager connectionManager;

    protected AbstractRequisitionProvider(final ClientManager clientManager,
                                          final ConnectionManager connectionManager) {
        this.clientManager = Objects.requireNonNull(clientManager);
        this.connectionManager = Objects.requireNonNull(connectionManager);
    }

    protected abstract Req createRequest(final Connection connection, final Map<String, String> parameters);

    protected abstract Requisition handleRequest(final RequestContext context) throws VelocloudApiException;

    @Override
    public final RequisitionRequest getRequest(final Map<String, String> parameters) {
        final var alias = Objects.requireNonNull(parameters.get("alias"), "Missing requisition parameter: alias");
        try {
            final var connection = this.connectionManager.getConnection(alias)
                                                         .orElseThrow(() -> new NullPointerException("Connection not found for alias: " + alias));

            return Objects.requireNonNull(this.createRequest(connection, parameters));

        } catch (final ConnectionValidationError e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final Requisition getRequisition(final RequisitionRequest rawRequest) {
        final var request = (Req) rawRequest;

        try {
            return this.handleRequest(new RequestContext(request));

        } catch (VelocloudApiException e) {
            LOG.error("Velocloud Orchestrator communication failed", e);
            throw new RuntimeException("Velocloud orchestrator communication failed", e);
        }
    }

    @Override
    public final byte[] marshalRequest(final RequisitionRequest request) {
        final var mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsBytes(request);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final RequisitionRequest unmarshalRequest(final byte[] bytes) {
        final var mapper = new ObjectMapper();
        try {
            return mapper.readValue(bytes, RequisitionRequest.class);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static abstract class Request implements RequisitionRequest {

        private String foreignSource;

        private String alias;

        private String orchestratorUrl;

        private String apiKey;

        public Request() {
        }

        public Request(final String type, final Connection connection) {
            this.foreignSource = String.format("%s-%s", type, connection.getAlias());
            this.alias = Objects.requireNonNull(connection.getAlias());
            this.orchestratorUrl = Objects.requireNonNull(connection.getOrchestratorUrl());
            this.apiKey = Objects.requireNonNull(connection.getApiKey());
        }

        public String getForeignSource() {
            return this.foreignSource;
        }

        public void setForeignSource(final String foreignSource) {
            this.foreignSource = foreignSource;
        }

        public String getAlias() {
            return this.alias;
        }

        public void setAlias(final String alias) {
            this.alias = Objects.requireNonNull(alias);
        }

        public String getOrchestratorUrl() {
            return this.orchestratorUrl;
        }

        public void setOrchestratorUrl(final String orchestratorUrl) {
            this.orchestratorUrl = orchestratorUrl;
        }

        public String getApiKey() {
            return this.apiKey;
        }

        public void setApiKey(final String apiKey) {
            this.apiKey = apiKey;
        }
    }

    public class RequestContext {
        private final Req request;

        public RequestContext(final Req request) {
            this.request = Objects.requireNonNull(request);
        }

        public VelocloudApiPartnerClient getPartnerClient() throws VelocloudApiException {
            return AbstractRequisitionProvider.this.clientManager.getPartnerClient(VelocloudApiClientCredentials.builder()
                                                                                                                .withOrchestratorUrl(this.request.getOrchestratorUrl())
                                                                                                                .withApiKey(this.request.getApiKey())
                                                                                                                .build());
        }

        public VelocloudApiCustomerClient getCustomerClient() throws VelocloudApiException {
            return AbstractRequisitionProvider.this.clientManager.getCustomerClient(VelocloudApiClientCredentials.builder()
                                                                                                                 .withOrchestratorUrl(this.request.getOrchestratorUrl())
                                                                                                                 .withApiKey(this.request.getApiKey())
                                                                                                                 .build());
        }

        public Req getRequest() {
            return this.request;
        }

        public String getForeignSource() {
            return this.request.getForeignSource();
        }

        public String getAlias() {
            return this.request.getAlias();
        }
    }
}
