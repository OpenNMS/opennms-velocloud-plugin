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

import java.util.Map;
import java.util.Objects;

import com.google.common.base.MoreObjects;

public class MetricsEdge {

    private final Map<Integer, MetricsEdgeLink> metricsEdgeLinks;

    private final Traffic trafficPriority1;
    private final Traffic trafficPriority2;
    private final Traffic trafficPriority3;
    private final Traffic trafficControl;

    private MetricsEdge(final MetricsEdge.Builder builder) {
        this.metricsEdgeLinks = Objects.requireNonNull(builder.metricsEdgeLinks);
        this.trafficPriority1 = Objects.requireNonNull(builder.trafficPriority1);
        this.trafficPriority2 = Objects.requireNonNull(builder.trafficPriority2);
        this.trafficPriority3 = Objects.requireNonNull(builder.trafficPriority3);
        this.trafficControl = Objects.requireNonNull(builder.trafficControl);
    }

    public static class Builder {
        private Map<Integer, MetricsEdgeLink> metricsEdgeLinks;

        private Traffic trafficPriority1;
        private Traffic trafficPriority2;
        private Traffic trafficPriority3;
        private Traffic trafficControl;

        private Builder() {}

        public MetricsEdge.Builder withTrafficPriority1(Traffic trafficPriority1) {
            this.trafficPriority1 = trafficPriority1;
            return this;
        }

        public MetricsEdge.Builder withTrafficPriority2(Traffic trafficPriority2) {
            this.trafficPriority1 = trafficPriority2;
            return this;
        }

        public MetricsEdge.Builder withTrafficPriority3(Traffic trafficPriority3) {
            this.trafficPriority1 = trafficPriority3;
            return this;
        }

        public MetricsEdge.Builder withTrafficControl(Traffic trafficControl) {
            this.trafficPriority1 = trafficControl;
            return this;
        }

        public MetricsEdge.Builder withMetricsEdgeLinks(Map<Integer, MetricsEdgeLink> metricsEdgeLinks) {
            this.metricsEdgeLinks = metricsEdgeLinks;
            return this;
        }

        public MetricsEdge build() {
            return new MetricsEdge(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<Integer, MetricsEdgeLink> getMetricsEdgeLinks() {
        return metricsEdgeLinks;
    }

    public Traffic getTrafficPriority1() {
        return trafficPriority1;
    }

    public Traffic getTrafficPriority2() {
        return trafficPriority2;
    }

    public Traffic getTrafficPriority3() {
        return trafficPriority3;
    }

    public Traffic getTrafficControl() {
        return trafficControl;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("metricsEdgeLinks", metricsEdgeLinks)
                .add("trafficPriority1", trafficPriority1)
                .add("trafficPriority2", trafficPriority2)
                .add("trafficPriority3", trafficPriority3)
                .add("trafficControl", trafficControl)
                .toString();
    }
}
