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

import java.util.List;

import com.google.common.base.MoreObjects;

public class MetricsEdge {

    private final Aggregate cpuPct;
    private final Aggregate cpuCoreTemp;
    private final Aggregate memoryPct;
    private final Aggregate flowCount;
    private final Aggregate handoffQueueDrops;
    private final Aggregate tunnelCount;
    private final Aggregate tunnelCountV6;
    private final List<TrafficApplication> trafficApplications;
    private final List<TrafficSource> trafficSources;
    private final List<TrafficDestination> trafficDestinations;
    private final Score scoreAfterOptimization;
    private final Long timestamp;

    private MetricsEdge(Builder builder) {
        this.cpuPct = builder.cpuPct;
        this.cpuCoreTemp = builder.cpuCoreTemp;
        this.memoryPct = builder.memoryPct;
        this.flowCount = builder.flowCount;
        this.handoffQueueDrops = builder.handoffQueueDrops;
        this.tunnelCount = builder.tunnelCount;
        this.tunnelCountV6 = builder.tunnelCountV6;
        this.trafficApplications = builder.trafficApplications;
        this.trafficSources = builder.trafficSources;
        this.trafficDestinations = builder.trafficDestinations;
        this.scoreAfterOptimization = builder.scoreAfterOptimization;
        this.timestamp = builder.timestamp;
    }

    public static class Builder {
        private Aggregate cpuPct;
        private Aggregate cpuCoreTemp;
        private Aggregate memoryPct;
        private Aggregate flowCount;
        private Aggregate handoffQueueDrops;
        private Aggregate tunnelCount;
        private Aggregate tunnelCountV6;
        private List<TrafficApplication> trafficApplications;
        private List<TrafficSource> trafficSources;
        private List<TrafficDestination> trafficDestinations;
        private Score scoreAfterOptimization;
        private Long timestamp;

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

        public Builder withTrafficApplications(List<TrafficApplication> trafficApplications) {
            this.trafficApplications = trafficApplications;
            return this;
        }

        public Builder withTrafficSources(List<TrafficSource> trafficSources) {
            this.trafficSources = trafficSources;
            return this;
        }

        public Builder withTrafficDestinations(List<TrafficDestination> trafficDestinations) {
            this.trafficDestinations = trafficDestinations;
            return this;
        }

        public Builder withScoreAfterOptimization(Score scoreAfterOptimization) {
            this.scoreAfterOptimization = scoreAfterOptimization;
            return this;
        }

        public Builder withTimestamp(final Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public MetricsEdge build() {
            return new MetricsEdge(this);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public Aggregate getCpuPct() {
        return cpuPct;
    }

    public Aggregate getCpuCoreTemp() {
        return cpuCoreTemp;
    }

    public Aggregate getMemoryPct() {
        return memoryPct;
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

    public List<TrafficApplication> getTrafficApplications() {
        return trafficApplications;
    }

    public List<TrafficSource> getTrafficSources() {
        return trafficSources;
    }

    public List<TrafficDestination> getTrafficDestinations() {
        return trafficDestinations;
    }

    public Score getScoreAfterOptimization() {
        return scoreAfterOptimization;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cpuPct", cpuPct)
                .add("cpuCoreTemp", cpuCoreTemp)
                .add("memoryPct", memoryPct)
                .add("flowCount", flowCount)
                .add("handoffQueueDrops", handoffQueueDrops)
                .add("tunnelCount", tunnelCount)
                .add("tunnelCountV6", tunnelCountV6)
                .add("trafficApplications", trafficApplications)
                .add("trafficSources", trafficSources)
                .add("scoreAfterOptimization", scoreAfterOptimization)
                .add("timestamp", timestamp)
                .toString();
    }
}
