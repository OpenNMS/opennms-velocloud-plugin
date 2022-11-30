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
import com.google.common.collect.ImmutableMap;

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
     *  Key store implementations may not be case-sensitive
     */
    public boolean contains(String alias) {
        return this.getAliases().stream()
                .anyMatch(str -> str.equalsIgnoreCase(alias));
    }

    /**
     * Returns a connection config for the given alias.
     *
     * @param alias the alias of the connection config to retrieve
     * @return The connection config or {@code Optional#empty()} of no such alias exists
     * @throws ConnectionValidationError if the connection config is invalid
     */
    public Optional<Connection> getConnection(final String alias) throws ConnectionValidationError {
        final var credentials = this.vault.getCredentials(PREFIX + alias.toLowerCase());
        if (credentials == null) {
            return Optional.empty();
        }

        return Optional.of(new ConnectionImpl(alias.toLowerCase(), credentials));
    }

    /**
     * Creates and saves a connection under the given alias.
     * If a connection with the same alias already exists, it will be overwritten.
     *
     * @param alias           the alias of the connection to add
     * @param orchestratorUrl the URL of the orchestrator
     * @param apiKey          the API key used to authenticate the connection
     * @throws ConnectionValidationError
     */
    public Connection addConnection(final String alias, final String orchestratorUrl, final String apiKey) throws ConnectionValidationError {
        final Connection connection = new ConnectionImpl(alias.toLowerCase(), orchestratorUrl, apiKey);
        connection.save();

        return connection;
    }

    /**
     * Attempts to connect a client with the given credentials to the given orchestrator
     *
     * @return    A ConnectionValidationError if an issue occurred, or null if the connection is valid.
     */
    public ConnectionValidationError validateConnection(final String alias) {
        ConnectionValidationError e;
        final var connection = getConnection(alias);
        if (connection.isEmpty()) {
            e = new ConnectionValidationError(alias, "No such connection exists");
        }
        else {
            e = connection.get().validate();
        }
        return e;
    }

    /**
     *  Validate a connection that is not currently saved in SCV
     *
     * @param orchestratorUrl       the URL of the orchestrator
     * @param apiKey                The API key used to authenticate the connection
     * @return  A ConnectionValidationError if an issue occurred, or null if the connection is valid
     */
    public ConnectionValidationError testConnection(final String orchestratorUrl, final String apiKey) {
        String fixedUrl = orchestratorUrl.endsWith("/") ? orchestratorUrl : orchestratorUrl + "/";
        final var connection = new ConnectionImpl("test", fixedUrl, apiKey);
        return connection.validate();
    }

    public Optional<VelocloudApiPartnerClient> getPartnerClient(final String alias) throws ConnectionValidationError, VelocloudApiException {
        final var connection = this.getConnection(alias);
        if (connection.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(this.clientManager.getPartnerClient(asVelocloudCredentials(connection.get())));
    }

    public Optional<VelocloudApiCustomerClient> getCustomerClient(final String alias) throws ConnectionValidationError, VelocloudApiException {
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

    private class ConnectionImpl implements Connection {
        private final String alias;
        private String orchestratorUrl;
        private String apiKey;

        private ConnectionImpl(final String alias, final String orchestratorUrl, final String apiKey) {
            this.alias = Objects.requireNonNull(alias);
            this.orchestratorUrl = Objects.requireNonNull(orchestratorUrl);
            this.apiKey = Objects.requireNonNull(apiKey);
        }

        private ConnectionImpl(final String alias, final Credentials credentials) throws ConnectionValidationError {
            this.alias = Objects.requireNonNull(alias);

            if (Strings.isNullOrEmpty(credentials.getUsername())) {
                throw new ConnectionValidationError(alias, "Orchestrator URL (username) is missing");
            }
            this.orchestratorUrl = credentials.getUsername();

            if (Strings.isNullOrEmpty(credentials.getPassword())) {
                throw new ConnectionValidationError(alias, "API key (password) is missing");
            }
            this.apiKey = credentials.getPassword();
        }

        @Override
        public String getAlias() {
            return this.alias;
        }

        @Override
        public String getOrchestratorUrl() {
            return this.orchestratorUrl;
        }

        @Override
        public void setOrchestratorUrl(final String url) {
            this.orchestratorUrl = Objects.requireNonNull(url);
        }

        @Override
        public String getApiKey() {
            return this.apiKey;
        }

        @Override
        public void setApiKey(final String apiKey) {
            this.apiKey = Objects.requireNonNull(apiKey);
        }

        @Override
        public void save() {
            ConnectionManager.this.vault.setCredentials(PREFIX + this.alias, this.asCredentials());
        }

        public void update(String orchestratorUrl, String apiKey) {
            final var old_creds = this.asVelocloudCredentials();
            this.setOrchestratorUrl(orchestratorUrl);
            this.setApiKey(apiKey);
            this.save();
            ConnectionManager.this.clientManager.purgeClient(old_creds);
        }

        @Override
        public ConnectionValidationError validate() {
            return ConnectionManager.this.clientManager.validate(asVelocloudCredentials());
        }

        private Credentials asCredentials() {
            final var attributes = ImmutableMap.<String, String>builder();

            return new ImmutableCredentials(this.orchestratorUrl, this.apiKey, attributes.build());
        }

        private VelocloudApiClientCredentials asVelocloudCredentials() {
            return ConnectionManager.asVelocloudCredentials(this);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("alias", this.alias)
                    .add("orchestratorUrl", this.orchestratorUrl)
                    .add("apiKey", "******")
                    .toString();
        }
    }
}
