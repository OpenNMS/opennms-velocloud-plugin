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

import com.google.common.base.MoreObjects;

public class MetricsGateway {
    private final Aggregate cpuPercentage;
    private final Aggregate memoryUsage;
    private final Aggregate flowCounts;
    private final Aggregate handoffQueueDrops;
    private final Aggregate tunnelCount;
    private final Aggregate tunnelCountV6;

    //TODO: Question: may values be nullable
    public MetricsGateway(MetricsGateway.Builder builder) {
        this.cpuPercentage = builder.cpuPercentage;
        this.memoryUsage = builder.memoryUsage;
        this.flowCounts = builder.flowCounts;
        this.handoffQueueDrops = builder.handoffQueueDrops;
        this.tunnelCount = builder.tunnelCount;
        this.tunnelCountV6 = builder.tunnelCountV6;
    }

    public static class Builder {
        private Aggregate cpuPercentage;
        private Aggregate memoryUsage;
        private Aggregate flowCounts;
        private Aggregate handoffQueueDrops;
        private Aggregate tunnelCount;
        private Aggregate tunnelCountV6;

        public MetricsGateway.Builder withCpuPercentage(final Aggregate cpuPercentage) {
            this.cpuPercentage = cpuPercentage;
            return this;
        }
        public MetricsGateway.Builder withMemoryUsage(final Aggregate memoryUsage) {
            this.memoryUsage = memoryUsage;
            return this;
        }
        public MetricsGateway.Builder withFlowCounts(final Aggregate flowCounts) {
            this.flowCounts = flowCounts;
            return this;
        }
        public MetricsGateway.Builder withHandoffQueueDrops(final Aggregate handoffQueueDrops) {
            this.handoffQueueDrops = handoffQueueDrops;
            return this;
        }
        public MetricsGateway.Builder withTunnelCount(final Aggregate tunnelCount) {
            this.tunnelCount = tunnelCount;
            return this;
        }

        public MetricsGateway.Builder withTunnelCountV6(final Aggregate tunnelCountV6) {
            this.tunnelCountV6 = tunnelCountV6;
            return this;
        }

        public MetricsGateway build() {
            return new MetricsGateway(this);
        }
    }

    public static MetricsGateway.Builder builder() {
        return new MetricsGateway.Builder();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cpuPercentage", cpuPercentage)
                .add("memoryUsage", memoryUsage)
                .add("flowCounts", flowCounts)
                .add("handoffQueueDrops", handoffQueueDrops)
                .add("tunnelCount", tunnelCount)
                .add("tunnelCountV6", tunnelCountV6)
                .toString();
    }

}
