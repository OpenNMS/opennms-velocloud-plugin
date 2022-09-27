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

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.opennms.integration.api.v1.scv.Credentials;
import org.opennms.integration.api.v1.scv.SecureCredentialsVault;
import org.opennms.integration.api.v1.scv.immutables.ImmutableCredentials;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

public class ConnectionManager {

    private static final String ATTR_ENTERPRISE_ID = "enterpriseId";

    private final SecureCredentialsVault vault;

    public ConnectionManager(final SecureCredentialsVault vault) {
        this.vault = Objects.requireNonNull(vault);
    }

    /**
     * Returns a list of all available connection aliases.
     *
     * @return the list of aliases
     */
    public Set<String> getAliases() {
        return this.vault.getAliases();
    }

    /**
     * Returns a connection config for the given alias.
     *
     * @param alias the alias of the connection config to retrieve
     * @return The connection config or {@code Optional#empty()} of no such alias exists
     * @throws ConnectionValidationError if the connection config is invalid
     */
    public Optional<Connection> getConnection(final String alias) throws ConnectionValidationError {
        final var credentials = this.vault.getCredentials(alias);
        if (credentials == null) {
            return Optional.empty();
        }

        return Optional.of(new ConnectionImpl(alias, credentials));
    }

    /**
     * Creates and saves a connection under the given alias.
     * If a connection with the same alias already exists, it will be overwritten.
     *
     * @param alias the alias of the connection to add
     * @param orchestratorUrl the URL of the orchestrator
     * @param apiKey the API key used to authenticate the connection
     * @param enterpriseId the optional enterprise ID of a client connection
     *
     * @throws ConnectionValidationError
     */
    public Connection addConnection(final String alias, final String orchestratorUrl, final String apiKey, final String enterpriseId) throws ConnectionValidationError {
        final Connection connection;

        if (Strings.isNullOrEmpty(enterpriseId)) {
            connection = new ConnectionImpl(alias, orchestratorUrl, apiKey);
        } else {
            connection = new ConnectionImpl(alias, orchestratorUrl, apiKey, UUID.fromString(enterpriseId));
        }

        connection.save();

        return connection;
    }

    private class ConnectionImpl implements Connection {
        private final String alias;
        private String orchestratorUrl;
        private String apiKey;
        private UUID enterpriseId;

        private ConnectionImpl(final String alias, final String orchestratorUrl, final String apiKey) {
            this(alias, orchestratorUrl, apiKey, null);
        }

        private ConnectionImpl(final String alias, final String orchestratorUrl, final String apiKey, final UUID enterpriseId) {
            this.alias = Objects.requireNonNull(alias);
            this.orchestratorUrl = Objects.requireNonNull(orchestratorUrl);
            this.apiKey = Objects.requireNonNull(apiKey);
            this.enterpriseId = enterpriseId;
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

            if (!Strings.isNullOrEmpty(credentials.getAttribute(ATTR_ENTERPRISE_ID))) {
                try {
                    this.enterpriseId = UUID.fromString(credentials.getAttribute(ATTR_ENTERPRISE_ID));
                } catch (final IllegalArgumentException e) {
                    throw new ConnectionValidationError(alias, "Enterprise ID (" + ATTR_ENTERPRISE_ID + ") is invalid: " + e.getMessage());
                }
            }
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
        public Optional<UUID> getEnterpriseId() {
            return Optional.ofNullable(this.enterpriseId);
        }

        @Override
        public void setEnterpriseId(final UUID enterpriseId) {
            this.enterpriseId = enterpriseId;
        }

        @Override
        public void save() {
            ConnectionManager.this.vault.setCredentials(this.alias, this.asCredentials());
        }

        private Credentials asCredentials() {
            final var attributes = ImmutableMap.<String, String>builder();

            if (this.enterpriseId != null) {
                attributes.put(ATTR_ENTERPRISE_ID, this.enterpriseId.toString());
            }

            return new ImmutableCredentials(this.orchestratorUrl, this.apiKey, attributes.build());
        }
    }
}
