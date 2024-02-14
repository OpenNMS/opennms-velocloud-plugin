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


public class MetricsLink {

    private final Integer bandwidthRx;
    private final Integer bandwidthTx;
    private final Traffic trafficPriority1;
    private final Traffic trafficPriority2;
    private final Traffic trafficPriority3;
    private final Traffic trafficControl;
    private final Score scoreBeforeOptimization;
    private final Float bestLatencyMsRx;
    private final Float bestLatencyMsTx;
    private final Float bestJitterMsRx;
    private final Float bestJitterMsTx;
    private final Float bestLossPctRx;
    private final Float bestLossPctTx;
    private final Float scoreRx;
    private final Float scoreTx;
    private final Long timestamp;

    private MetricsLink(final MetricsLink.Builder builder) {
        this.bandwidthRx = Objects.requireNonNull(builder.bandwidthRx);
        this.bandwidthTx = Objects.requireNonNull(builder.bandwidthTx);
        this.trafficPriority1 = Objects.requireNonNull(builder.trafficPriority1);
        this.trafficPriority2 = Objects.requireNonNull(builder.trafficPriority2);
        this.trafficPriority3 = Objects.requireNonNull(builder.trafficPriority3);
        this.trafficControl = Objects.requireNonNull(builder.trafficControl);
        this.scoreBeforeOptimization = Objects.requireNonNull(builder.scoreBeforeOptimization);
        this.bestLatencyMsRx = builder.bestLatencyMsRx;
        this.bestLatencyMsTx = builder.bestLatencyMsTx;
        this.bestJitterMsRx = builder.bestJitterMsRx;
        this.bestJitterMsTx = builder.bestJitterMsTx;
        this.bestLossPctRx = builder.bestLossPctRx;
        this.bestLossPctTx = builder.bestLossPctTx;
        this.scoreRx = builder.scoreRx;
        this.scoreTx = builder.scoreTx;
        this.timestamp = builder.timestamp;
    }

    public static class Builder {
        public Integer linkId;
        private Integer bandwidthRx;
        private Integer bandwidthTx;
        private Traffic trafficPriority1;
        private Traffic trafficPriority2;
        private Traffic trafficPriority3;
        private Traffic trafficControl;
        private Score scoreBeforeOptimization;
        private Float bestLatencyMsRx;
        private Float bestLatencyMsTx;
        private Float bestJitterMsRx;
        private Float bestJitterMsTx;
        private Float bestLossPctRx;
        private Float bestLossPctTx;
        private Float scoreRx;
        private Float scoreTx;
        private Long timestamp;

        private Builder() {}

        public MetricsLink.Builder withBandwidthRx(Integer bandwidthRx) {
            this.bandwidthRx = bandwidthRx;
            return this;
        }

        public MetricsLink.Builder withBandwidthTx(Integer bandwidthTx) {
            this.bandwidthTx = bandwidthTx;
            return this;
        }

        public MetricsLink.Builder withTrafficPriority1(Traffic trafficPriority1) {
            this.trafficPriority1 = trafficPriority1;
            return this;
        }

        public MetricsLink.Builder withTrafficPriority2(Traffic trafficPriority2) {
            this.trafficPriority2 = trafficPriority2;
            return this;
        }

        public MetricsLink.Builder withTrafficPriority3(Traffic trafficPriority3) {
            this.trafficPriority3 = trafficPriority3;
            return this;
        }

        public MetricsLink.Builder withTrafficControl(Traffic trafficControl) {
            this.trafficControl = trafficControl;
            return this;
        }

        public MetricsLink.Builder withScoreBeforeOptimization(Score scoreBeforeOptimization) {
            this.scoreBeforeOptimization = scoreBeforeOptimization;
            return this;
        }


        public MetricsLink.Builder withBestLatencyMsRx(Float bestLatencyMsRx) {
            this.bestLatencyMsRx = bestLatencyMsRx;
            return this;
        }

        public MetricsLink.Builder withBestLatencyMsTx(Float bestLatencyMsTx) {
            this.bestLatencyMsTx = bestLatencyMsTx;
            return this;
        }

        public MetricsLink.Builder withBestJitterMsRx(Float bestJitterMsRx) {
            this.bestJitterMsRx = bestJitterMsRx;
            return this;
        }

        public MetricsLink.Builder withBestJitterMsTx(Float bestJitterMsTx) {
            this.bestJitterMsTx = bestJitterMsTx;
            return this;
        }

        public MetricsLink.Builder withBestLossPctRx(Float bestLossPctRx) {
            this.bestLossPctRx = bestLossPctRx;
            return this;
        }

        public MetricsLink.Builder withBestLossPctTx(Float bestLossPctTx) {
            this.bestLossPctTx = bestLossPctTx;
            return this;
        }

        public MetricsLink.Builder withScoreRx(Float scoreRx) {
            this.scoreRx = scoreRx;
            return this;
        }

        public MetricsLink.Builder withScoreTx(Float scoreTx) {
            this.scoreTx = scoreTx;
            return this;
        }

        public MetricsLink.Builder withTimestamp(final Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public MetricsLink build() {
            return new MetricsLink(this);
        }
    }

    public static MetricsLink.Builder builder() {
        return new MetricsLink.Builder();
    }

    public Integer getBandwidthRx() {
        return bandwidthRx;
    }

    public Integer getBandwidthTx() {
        return bandwidthTx;
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

    public Score getScoreBeforeOptimization() {
        return scoreBeforeOptimization;
    }

    public float getBestLatencyMsRx() {
        return bestLatencyMsRx;
    }

    public float getBestLatencyMsTx() {
        return bestLatencyMsTx;
    }

    public float getBestJitterMsRx() {
        return bestJitterMsRx;
    }

    public float getBestJitterMsTx() {
        return bestJitterMsTx;
    }

    public float getBestLossPctRx() {
        return bestLossPctRx;
    }

    public float getBestLossPctTx() {
        return bestLossPctTx;
    }

    public float getScoreRx() {
        return scoreRx;
    }

    public float getScoreTx() {
        return scoreTx;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "MetricsLink ["
                + "bandwidthRx=" + bandwidthRx
                + ", bandwidthTx=" + bandwidthTx
                + ", trafficPriority1=" + trafficPriority1
                + ", trafficPriority2=" + trafficPriority2
                + ", trafficPriority3=" + trafficPriority3
                + ", trafficControl=" + trafficControl
                + ", scoreBeforeOptimization=" + scoreBeforeOptimization
                + ", bestLatencyMsRx=" + bestLatencyMsRx
                + ", bestLatencyMsTx=" + bestLatencyMsTx
                + ", bestJitterMsRx=" + bestJitterMsRx
                + ", bestJitterMsTx=" + bestJitterMsTx
                + ", bestLossPctRx=" + bestLossPctRx
                + ", bestLossPctTx=" + bestLossPctTx
                + ", scoreRx=" + scoreRx
                + ", scoreTx=" + scoreTx
                + ", timestamp=" + timestamp
                + "]";
    }

}
