/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2023 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2023 The OpenNMS Group, Inc.
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

public class Datacenter {
    public final String name;
    public final String logicalId;
    public final String primaryState;
    public final String secondaryState;

    private Datacenter(final Datacenter.Builder builder) {
        this.name = Objects.requireNonNull(builder.name);
        this.logicalId = Objects.requireNonNull(builder.logicalId);
        this.primaryState = builder.primaryState;
        this.secondaryState = builder.secondaryState;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("logicalId", logicalId)
                .add("primaryState", primaryState)
                .add("secondaryState", secondaryState)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Datacenter that = (Datacenter) o;
        return Objects.equals(name, that.name) && Objects.equals(logicalId, that.logicalId) && Objects.equals(primaryState, that.primaryState) && Objects.equals(secondaryState, that.secondaryState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, logicalId, primaryState, secondaryState);
    }

    public static class Builder {
        private String name;
        private String logicalId;
        private String primaryState;
        private String secondaryState;

        private Builder() {
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withLogicalId(final String logicalId) {
            this.logicalId = logicalId;
            return this;
        }

        public Builder withPrimaryState(final String primaryState) {
            this.primaryState = primaryState;
            return this;
        }

        public Builder withSecondaryState(final String secondaryState) {
            this.secondaryState = secondaryState;
            return this;
        }

        public Datacenter build() {
            return new Datacenter(this);
        }
    }
    
    public static Datacenter.Builder builder() {
        return new Builder();
    }
}