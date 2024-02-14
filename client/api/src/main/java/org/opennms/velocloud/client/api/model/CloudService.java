/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2023-2024 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2024 The OpenNMS Group, Inc.
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

package org.opennms.velocloud.client.api.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public class CloudService {
    public final String link;
    public final String state;
    public final String pathId;
    public final OffsetDateTime timestamp;
    public final String name;
    public final String role;

    private CloudService(final Builder builder) {
        this.link = Objects.requireNonNull(builder.link);
        this.state = Objects.requireNonNull(builder.state);
        this.pathId = Objects.requireNonNull(builder.pathId);
        this.timestamp = Objects.requireNonNull(builder.timestamp);
        this.name = Objects.requireNonNull(builder.name);
        this.role = Objects.requireNonNull(builder.role);
    }


    @Override
    public String toString() {
        return "CloudService ["
                + "link=" + link
                + ", state=" + state
                + ", pathId=" + pathId
                + ", timestamp=" + timestamp
                + ", name=" + name
                + ", role=" + role
                + "]";
    }

    public static class Builder {
        public String link;
        public String state;
        public String pathId;
        public OffsetDateTime timestamp;
        private String name;
        private String role;

        private Builder() {
        }

        public Builder withLink(final String link) {
            this.link = link;
            return this;
        }

        public Builder withTimestamp(OffsetDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withPathId(final String pathId) {
            this.pathId = pathId;
            return this;
        }

        public Builder withState(String state) {
            this.state = state;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withRole(String role) {
            this.role = role;
            return this;
        }

        public CloudService build() {
            return new CloudService(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
