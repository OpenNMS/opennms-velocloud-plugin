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

public class MetricsEdgeSystem {

    private final Aggregate cpuPct;
    private final Aggregate cpuCoreTemp;
    private final Aggregate memoryPct;
    private final Aggregate flowCount;
    private final Aggregate handoffQueueDrops;
    private final Aggregate tunnelCount;
    private final Aggregate tunnelCountV6;

    private MetricsEdgeSystem(Builder builder) {
        this.cpuPct = builder.cpuPct;
        this.cpuCoreTemp = builder.cpuCoreTemp;
        this.memoryPct = builder.memoryPct;
        this.flowCount = builder.flowCount;
        this.handoffQueueDrops = builder.handoffQueueDrops;
        this.tunnelCount = builder.tunnelCount;
        this.tunnelCountV6 = builder.tunnelCountV6;
    }

    public static class Builder {
        private Aggregate cpuPct;
        private Aggregate cpuCoreTemp;
        private Aggregate memoryPct;
        private Aggregate flowCount;
        private Aggregate handoffQueueDrops;
        private Aggregate tunnelCount;
        private Aggregate tunnelCountV6;

        private Builder() {}

        public Builder withCpuPct(Aggregate cpuPct) {
            this.cpuPct = cpuPct;
            return this;
        }
        public Builder withCpuCoreTemp(Aggregate cpuCoreTemp) {
            this.cpuCoreTemp = cpuCoreTemp;
            return this;
        }
        public Builder withMemoryPct(Aggregate memoryPct) {
            this.memoryPct = memoryPct;
            return this;
        }
        public Builder withFlowCount(Aggregate flowCount) {
            this.flowCount = flowCount;
            return this;
        }
        public Builder withHandoffQueueDrops(Aggregate handoffQueueDrops) {
            this.handoffQueueDrops = handoffQueueDrops;
            return this;
        }
        public Builder withTunnelCount(Aggregate tunnelCount) {
            this.tunnelCount = tunnelCount;
            return this;
        }
        public Builder withTunnelCountV6(Aggregate tunnelCountV6) {
            this.tunnelCountV6 = tunnelCountV6;
            return this;
        }

        public MetricsEdgeSystem build() {
            return new MetricsEdgeSystem(this);
        }

    }

    public static Builder builder() {
        return new Builder();
    }
}
