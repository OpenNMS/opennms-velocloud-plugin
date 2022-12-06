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

package org.opennms.velocloud.pollers;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.pollers.PollerRequest;
import org.opennms.integration.api.v1.pollers.PollerResult;
import org.opennms.integration.api.v1.pollers.ServicePoller;
import org.opennms.integration.api.v1.pollers.ServicePollerFactory;
import org.opennms.integration.api.v1.pollers.Status;
import org.opennms.integration.api.v1.pollers.immutables.ImmutablePollerResult;
import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.connections.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

public abstract class AbstractStatusPoller implements ServicePoller {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractStatusPoller.class);

    private static final String ATTR_ALIAS = "alias";

    private static final String ATTR_ORCHESTRATOR_URL = "orchestratorUrl";
    private static final String ATTR_API_KEY = "apiKey";

    private final ClientManager clientManager;

    protected AbstractStatusPoller(final ClientManager clientManager) {
        this.clientManager = Objects.requireNonNull(clientManager);
    }

    protected abstract CompletableFuture<PollerResult> poll(final Context context) throws VelocloudApiException;

    @Override
    public final CompletableFuture<PollerResult> poll(final PollerRequest pollerRequest) {
        try {
            return this.poll(new Context(pollerRequest));

        } catch (final VelocloudApiException e) {
            LOG.error("Velocloud orchestrator communication failed", e);
            return CompletableFuture.completedFuture(ImmutablePollerResult.newBuilder()
                                                                          .setStatus(Status.Down)
                                                                          .setReason(e.getMessage())
                                                                          .build());
        }
    }

    public static abstract class Factory<T extends AbstractStatusPoller> implements ServicePollerFactory<T> {

        private final ClientManager clientManager;

        private final ConnectionManager connectionManager;

        private final Class<T> clazz;

        protected Factory(final ClientManager clientManager,
                          final ConnectionManager connectionManager,
                          final Class<T> clazz) {
            this.clientManager = Objects.requireNonNull(clientManager);
            this.connectionManager = Objects.requireNonNull(connectionManager);

            this.clazz = Objects.requireNonNull(clazz);
        }

        protected abstract T createPoller(ClientManager clientManager);

        @Override
        public final T createPoller() {
            return this.createPoller(this.clientManager);
        }

        @Override
        public final String getPollerClassName() {
            return this.clazz.getCanonicalName();
        }



        //TODO get this to my factory
        @Override
        public final Map<String, String> getRuntimeAttributes(final PollerRequest pollerRequest) {
            final var alias = Objects.requireNonNull(pollerRequest.getPollerAttributes().get(ATTR_ALIAS), "Missing property: " + ATTR_ALIAS);
            final var connection = this.connectionManager.getConnection(alias)
                                                         .orElseThrow(() -> new NullPointerException("Connection not found for alias: " + alias));

            final var attrs = ImmutableMap.<String, String>builder();
            attrs.put(ATTR_ORCHESTRATOR_URL, connection.getOrchestratorUrl());
            attrs.put(ATTR_API_KEY, connection.getApiKey());
            return attrs.build();
        }
    }

    public class Context {
        public final PollerRequest request;

        public Context(final PollerRequest request) {
            this.request = Objects.requireNonNull(request);
        }

        public VelocloudApiClientCredentials getClientCredentials() {
            final var orchestratorUrl = Objects.requireNonNull(this.request.getPollerAttributes().get(ATTR_ORCHESTRATOR_URL),
                                                               "Missing attribute: " + ATTR_ORCHESTRATOR_URL);
            final var apiKey = Objects.requireNonNull(this.request.getPollerAttributes().get(ATTR_API_KEY),
                                                      "Missing attribute: " + ATTR_API_KEY);

            return VelocloudApiClientCredentials.builder()
                                                .withOrchestratorUrl(orchestratorUrl)
                                                .withApiKey(apiKey)
                                                .build();
        }

        public VelocloudApiPartnerClient partnerClient() throws VelocloudApiException {
            return AbstractStatusPoller.this.clientManager.getPartnerClient(this.getClientCredentials());
        }

        public VelocloudApiCustomerClient customerClient() throws VelocloudApiException {
            final String enterpriseId = this.request.getPollerAttributes().get("enterpriseId");

            if (Strings.isNullOrEmpty(enterpriseId)) {
                return AbstractStatusPoller.this.clientManager.getCustomerClient(this.getClientCredentials());
            } else {
                return AbstractStatusPoller.this.clientManager.getPartnerClient(this.getClientCredentials()).getCustomerClient(Integer.parseInt(enterpriseId));
            }
        }

        public Map<String, String> getPollerAttributes() {
            return this.request.getPollerAttributes();
        }
    }
}
