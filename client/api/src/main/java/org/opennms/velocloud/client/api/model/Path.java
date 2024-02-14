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

import java.util.Objects;

public class Path {
    public final String peerType;
    public final String peerName;
    public final String description;
    public final String edgeLogicalId;
    public final String deviceLogicalId;
    public final int pathCountStable;
    public final int pathCountUnstable;
    public final int pathCountStandBy;
    public final int pathCountDead;
    public final int pathCountUnknown;
    public final int pathCountTotal;

    private Path(Builder builder) {
        peerType = builder.peerType;
        peerName = Objects.requireNonNull(builder.peerName);
        description = builder.description;
        edgeLogicalId = builder.edgeLogicalId;
        deviceLogicalId = Objects.requireNonNull(builder.deviceLogicalId);
        pathCountStable = builder.pathCountStable;
        pathCountUnstable = Objects.requireNonNull(builder.pathCountUnstable);
        pathCountStandBy = Objects.requireNonNull(builder.pathCountStandBy);
        pathCountDead = Objects.requireNonNull(builder.pathCountDead);
        pathCountUnknown = Objects.requireNonNull(builder.pathCountUnknown);
        pathCountTotal = Objects.requireNonNull(builder.pathCountTotal);
    }

    @Override
    public String toString() {
        return "Path ["
                + "peerType=" + peerType
                + ", peerName=" + peerName
                + ", description=" + description
                + ", edgeLogicalId=" + edgeLogicalId
                + ", deviceLogicalId=" + deviceLogicalId
                + ", pathCountStable=" + pathCountStable
                + ", pathCountUnstable=" + pathCountUnstable
                + ", pathCountStandBy=" + pathCountStandBy
                + ", pathCountDead=" + pathCountDead
                + ", pathCountUnknown=" + pathCountUnknown
                + ", pathCountTotal=" + pathCountTotal
                + "]";
    }

    public static class Builder {
        private String peerType;
        private String peerName;
        private String description;
        private String edgeLogicalId;
        private String deviceLogicalId;
        private int pathCountStable;
        private int pathCountUnstable;
        private int pathCountStandBy;
        private int pathCountDead;
        private int pathCountUnknown;
        private int pathCountTotal;

        private Builder() {
        }

        public Builder withPeerType(String peerType) {
            this.peerType = peerType;
            return this;
        }

        public Builder withPeerName(String peerName) {
            this.peerName = peerName;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withEdgeLogicalId(String edgeLogicalId) {
            this.edgeLogicalId = edgeLogicalId;
            return this;
        }

        public Builder withDeviceLogicalId(String deviceLogicalId) {
            this.deviceLogicalId = deviceLogicalId;
            return this;
        }

        public Builder withPathCountStable(int pathCountStable) {
            this.pathCountStable = pathCountStable;
            return this;
        }

        public Builder withPathCountUnstable(int pathCountUnstable) {
            this.pathCountUnstable = pathCountUnstable;
            return this;
        }

        public Builder withPathCountStandBy(int pathCountStandBy) {
            this.pathCountStandBy = pathCountStandBy;
            return this;
        }

        public Builder withPathCountDead(int pathCountDead) {
            this.pathCountDead = pathCountDead;
            return this;
        }

        public Builder withPathCountUnknown(int pathCountUnknown) {
            this.pathCountUnknown = pathCountUnknown;
            return this;
        }

        public Builder withPathCountTotal(int pathCountTotal) {
            this.pathCountTotal = pathCountTotal;
            return this;
        }

        public Path build() {
            return new Path(this);
        }
    }

    public static Path.Builder builder() {
        return new Path.Builder();
    }
}
