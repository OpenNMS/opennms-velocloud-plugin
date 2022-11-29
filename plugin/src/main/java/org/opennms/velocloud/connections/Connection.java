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

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;

import java.util.Optional;
import java.util.UUID;

public interface Connection {
    /**
     * Returns the alias of the connection.
     * The alias is a unique identifier representing a connection configuration.
     *
     * @return the alias
     */
    String getAlias();

    /**
     * Retuerns the URL of the orchestrator.
     * @return the orchestrator URL
     */
    String getOrchestratorUrl();

    /**
     * Changes the URL of the orchestrator.
     * @param url the new URL
     */
    void setOrchestratorUrl(final String url);

    /**
     * Returns the API key used to authenticate the connection.
     * @return the API key
     */
    String getApiKey();

    /**
     * Changes the API key used to authenticate the connection.
     * @param apiKey the new API key
     */
    void setApiKey(final String apiKey);

    /**
     * Save the altered connection config in the underlying store.
     */
    void save();

    /**
     * Build the {@link VelocloudApiClientCredentials} of this connection.
     * @return the credentials used by the connection
     */
    default VelocloudApiClientCredentials asVelocloudCredentials() {
        return VelocloudApiClientCredentials.builder()
                .withOrchestratorUrl(this.getOrchestratorUrl())
                .withApiKey(this.getApiKey())
                .build();
    }
}
