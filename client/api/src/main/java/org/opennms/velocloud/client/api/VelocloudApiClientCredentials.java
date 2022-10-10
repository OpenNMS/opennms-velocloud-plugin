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

package org.opennms.velocloud.client.api;

import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * Credentials for a velocloud API connection.
 */
public class VelocloudApiClientCredentials {
    /**
     * The URL of the velocloud orchestrator.
     */
    public final String orchestratorUrl;

    /**
     * The API key used to authenticate the connection to the orchestrator.
     */
    public final String apiKey;

    public VelocloudApiClientCredentials(final String orchestratorUrl,
                                         final String apiKey) {
        this.orchestratorUrl = Objects.requireNonNull(orchestratorUrl);
        this.apiKey = Objects.requireNonNull(apiKey);
    }

    private VelocloudApiClientCredentials(final Builder builder) {
        this.orchestratorUrl = Objects.requireNonNull(builder.orchestratorUrl);
        this.apiKey = Objects.requireNonNull(builder.apiKey);
    }

    public static class Builder {
        private String orchestratorUrl;
        private String apiKey;

        private Builder() {
        }

        public Builder withOrchestratorUrl(final String orchestratorUrl) {
            this.orchestratorUrl = orchestratorUrl;
            return this;
        }

        public Builder withApiKey(final String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public VelocloudApiClientCredentials build() {
            return new VelocloudApiClientCredentials(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("orchestratorUrl", this.orchestratorUrl)
                          .add("apiKey", this.apiKey)
                          .toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VelocloudApiClientCredentials)) {
            return false;
        }
        final VelocloudApiClientCredentials that = (VelocloudApiClientCredentials) o;
        return Objects.equals(this.orchestratorUrl, that.orchestratorUrl) &&
               Objects.equals(this.apiKey, that.apiKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.orchestratorUrl,
                            this.apiKey);
    }
}
