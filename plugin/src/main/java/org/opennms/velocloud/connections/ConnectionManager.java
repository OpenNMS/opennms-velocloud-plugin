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

package org.opennms.velocloud.connections;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.MoreObjects;

import org.opennms.integration.api.v1.runtime.Container;
import org.opennms.integration.api.v1.runtime.RuntimeInfo;
import org.opennms.integration.api.v1.scv.Credentials;
import org.opennms.integration.api.v1.scv.SecureCredentialsVault;
import org.opennms.integration.api.v1.scv.immutables.ImmutableCredentials;
import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.clients.ClientManager;

import com.google.common.base.Strings;

public class ConnectionManager {

    private static final String PREFIX = "velocloud_connection_";

    private final SecureCredentialsVault vault;

    private final ClientManager clientManager;

    public ConnectionManager(final RuntimeInfo runtimeInfo,
                             final SecureCredentialsVault vault,
                             final ClientManager clientManager) {

        if (runtimeInfo.getContainer() != Container.OPENNMS) {
            throw new IllegalStateException("Operation only allowed on OpenNMS instance");
        }

        this.vault = Objects.requireNonNull(vault);
        this.clientManager = Objects.requireNonNull(clientManager);
    }

    /**
     * Returns a list of all available connection aliases.
     *
     * @return the list of aliases
     */
    public Set<String> getAliases() {
        return this.vault.getAliases().stream()
                         .filter(alias -> alias.startsWith(PREFIX))
                         .map(alias -> alias.substring(PREFIX.length()))
                         .collect(Collectors.toSet());
    }

    /**
     * Returns a connection config for the given alias.
     *
     * @param alias the alias of the connection config to retrieve
     * @return The connection config or {@code Optional#empty()} of no such alias exists
     */
    public Optional<Connection> getConnection(final String alias) {
        final var credentials = this.vault.getCredentials(PREFIX + alias.toLowerCase());
        if (credentials == null) {
            return Optional.empty();
        }

        return Optional.of(new ConnectionImpl(alias, ConnectionManager.fromStore(credentials)));
    }

    /**
     * Creates a connection under the given alias.
     *
     * @param alias           the alias of the connection to add
     * @param orchestratorUrl the URL of the orchestrator
     * @param apiKey          the API key used to authenticate the connection
     */
    public Connection newConnection(final String alias, final String orchestratorUrl, final String apiKey) {
        return new ConnectionImpl(alias, VelocloudApiClientCredentials.builder()
                                                                      .withOrchestratorUrl(orchestratorUrl)
                                                                      .withApiKey(apiKey)
                                                                      .build());
    }

    public Optional<VelocloudApiPartnerClient> getPartnerClient(final String alias) throws VelocloudApiException {
        final var connection = this.getConnection(alias);
        if (connection.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(this.clientManager.getPartnerClient(asVelocloudCredentials(connection.get())));
    }

    public Optional<VelocloudApiCustomerClient> getCustomerClient(final String alias) throws VelocloudApiException {
        final var connection = this.getConnection(alias);
        if (connection.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(this.clientManager.getCustomerClient(asVelocloudCredentials(connection.get())));
    }

    private static VelocloudApiClientCredentials asVelocloudCredentials(Connection connection) {
        return VelocloudApiClientCredentials.builder()
                                            .withApiKey(connection.getApiKey())
                                            .withOrchestratorUrl(connection.getOrchestratorUrl())
                                            .build();
    }

    private static VelocloudApiClientCredentials fromStore(final Credentials credentials) {
        if (Strings.isNullOrEmpty(credentials.getUsername())) {
            throw new IllegalStateException("Orchestrator URL (username) is missing");
        }
        final var orchestratorUrl = credentials.getUsername();

        if (Strings.isNullOrEmpty(credentials.getPassword())) {
            throw new IllegalStateException("API key (password) is missing");
        }
        final var apiKey = credentials.getPassword();

        return VelocloudApiClientCredentials.builder()
                                            .withOrchestratorUrl(orchestratorUrl)
                                            .withApiKey(apiKey)
                                            .build();
    }

    private class ConnectionImpl implements Connection {
        private final String alias;

        private VelocloudApiClientCredentials credentials;

        private ConnectionImpl(final String alias, final VelocloudApiClientCredentials credentials) {
            this.alias = Objects.requireNonNull(alias).toLowerCase();
            this.credentials = Objects.requireNonNull(credentials);
        }

        @Override
        public String getAlias() {
            return this.alias;
        }

        @Override
        public String getOrchestratorUrl() {
            return this.credentials.orchestratorUrl;
        }

        @Override
        public void setOrchestratorUrl(final String url) {
            this.credentials = VelocloudApiClientCredentials.builder(this.credentials)
                                                            .withOrchestratorUrl(url)
                                                            .build();
        }

        @Override
        public String getApiKey() {
            return this.credentials.apiKey;
        }

        @Override
        public void setApiKey(final String apiKey) {
            this.credentials = VelocloudApiClientCredentials.builder(this.credentials)
                                                            .withApiKey(apiKey)
                                                            .build();
        }

        @Override
        public void save() {
            // Purge cached client with old credentials
            final var oldCredentials = ConnectionManager.this.vault.getCredentials(PREFIX + this.alias);
            if (oldCredentials != null) {
                ConnectionManager.this.clientManager.purgeClient(ConnectionManager.fromStore(oldCredentials));
            }

            ConnectionManager.this.vault.setCredentials(PREFIX + this.alias, this.asCredentials());
        }

        @Override
        public Optional<ConnectionValidationError> validate() {
            return ConnectionManager.this.clientManager.validate(ConnectionManager.asVelocloudCredentials(this));
        }

        private Credentials asCredentials() {
            return new ImmutableCredentials(this.credentials.orchestratorUrl, this.credentials.apiKey, Collections.emptyMap());
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                              .add("alias", this.alias)
                              .add("orchestratorUrl", this.credentials.orchestratorUrl)
                              .add("apiKey", "******")
                              .toString();
        }
    }
}
