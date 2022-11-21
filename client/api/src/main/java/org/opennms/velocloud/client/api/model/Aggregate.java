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

import java.math.BigDecimal;
import java.util.Objects;

import com.google.common.base.MoreObjects;

public class Aggregate {

    private final BigDecimal min;
    private final BigDecimal max;
    private final BigDecimal average;

    private Aggregate(final Aggregate.Builder builder) {
        this.min = Objects.requireNonNull(builder.min);
        this.max = Objects.requireNonNull(builder.max);
        this.average = Objects.requireNonNull(builder.average);
    }

    public static class Builder {
        private BigDecimal min = null;
        private BigDecimal max = null;
        private BigDecimal average = null;

        public Aggregate.Builder withMin(BigDecimal min) {
            this.min = min;
            return this;
        }

        public Aggregate.Builder withMax(BigDecimal max) {
            this.max = max;
            return this;
        }

        public Aggregate.Builder withAverage(BigDecimal average) {
            this.average = average;
            return this;
        }

        public static Aggregate.Builder builder() {
            return new Aggregate.Builder();
        }

        public Aggregate build() {
            return new Aggregate(this);
        }
    }

    public static Aggregate.Builder builder() {
        return new Aggregate.Builder();
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public BigDecimal getAverage() {
        return average;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("min", min)
                .add("max", max)
                .add("average", average)
                .toString();
    }
}
