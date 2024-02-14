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

public class MetricsGateway {
    private final Aggregate cpuPercentage;
    private final Aggregate memoryUsage;
    private final Aggregate flowCount;
    private final Aggregate handoffQueueDrops;
    private final Aggregate tunnelCount;
    private final Aggregate tunnelCountV6;
    private final Long timestamp;

    public MetricsGateway(MetricsGateway.Builder builder) {
        this.cpuPercentage = builder.cpuPercentage;
        this.memoryUsage = builder.memoryUsage;
        this.flowCount = builder.flowCount;
        this.handoffQueueDrops = builder.handoffQueueDrops;
        this.tunnelCount = builder.tunnelCount;
        this.tunnelCountV6 = builder.tunnelCountV6;
        this.timestamp = builder.timestamp;
    }

    public static class Builder {
        private Aggregate cpuPercentage;
        private Aggregate memoryUsage;
        private Aggregate flowCount;
        private Aggregate handoffQueueDrops;
        private Aggregate tunnelCount;
        private Aggregate tunnelCountV6;
        private Long timestamp;

        public Builder withCpuPercentage(final Aggregate cpuPercentage) {
            this.cpuPercentage = cpuPercentage;
            return this;
        }
        public Builder withMemoryUsage(final Aggregate memoryUsage) {
            this.memoryUsage = memoryUsage;
            return this;
        }
        public Builder withFlowCounts(final Aggregate flowCounts) {
            this.flowCount = flowCounts;
            return this;
        }
        public Builder withHandoffQueueDrops(final Aggregate handoffQueueDrops) {
            this.handoffQueueDrops = handoffQueueDrops;
            return this;
        }
        public Builder withTunnelCount(final Aggregate tunnelCount) {
            this.tunnelCount = tunnelCount;
            return this;
        }

        public Builder withTunnelCountV6(final Aggregate tunnelCountV6) {
            this.tunnelCountV6 = tunnelCountV6;
            return this;
        }

        public Builder withTimestamp(final Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public MetricsGateway build() {
            return new MetricsGateway(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Aggregate getCpuPercentage() {
        return cpuPercentage;
    }

    public Aggregate getMemoryUsage() {
        return memoryUsage;
    }

    public Aggregate getFlowCount() {
        return flowCount;
    }

    public Aggregate getHandoffQueueDrops() {
        return handoffQueueDrops;
    }

    public Aggregate getTunnelCount() {
        return tunnelCount;
    }

    public Aggregate getTunnelCountV6() {
        return tunnelCountV6;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "MetricsGateway ["
                + "cpuPercentage=" + cpuPercentage
                + ", memoryUsage=" + memoryUsage
                + ", flowCount=" + flowCount
                + ", handoffQueueDrops=" + handoffQueueDrops
                + ", tunnelCount=" + tunnelCount
                + ", tunnelCountV6=" + tunnelCountV6
                + ", timestamp=" + timestamp
                + "]";
    }

}
