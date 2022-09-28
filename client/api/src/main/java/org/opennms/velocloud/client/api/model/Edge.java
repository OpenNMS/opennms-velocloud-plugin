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

package org.opennms.velocloud.client.api.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class Edge {
    public final String enterpriseId;
    public final String operator;
    public final String site;
    public final boolean hub;

    private Edge(final Builder builder) {
        this.enterpriseId = Objects.requireNonNull(builder.enterpriseId);
        this.operator = Objects.requireNonNull(builder.operator);
        this.site = Objects.requireNonNull(builder.site);
        this.hub = builder.hub;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("enterpriseId", this.enterpriseId)
                          .add("operator", this.operator)
                          .add("site", this.site)
                          .add("hub", this.hub)
                          .toString();
    }

    public static class Builder {
        private String enterpriseId;
        private String operator;
        private String site;
        private boolean hub;

        private Builder() {
        }

        public Builder withEnterpriseId(final String enterpriseId) {
            this.enterpriseId = enterpriseId;
            return this;
        }

        public Builder withOperator(final String operator) {
            this.operator = operator;
            return this;
        }

        public Builder withSite(final String site) {
            this.site = site;
            return this;
        }

        public Builder withHub(final boolean hub) {
            this.hub = hub;
            return this;
        }

        public Edge build() {
            return new Edge(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
