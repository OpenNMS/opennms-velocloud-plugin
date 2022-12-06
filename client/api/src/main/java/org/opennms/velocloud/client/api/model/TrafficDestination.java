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

public class TrafficDestination {

    private final String destinationDomain;
    private final String name;
    private final Traffic traffic;

    private TrafficDestination(final TrafficDestination.Builder builder) {
        this.destinationDomain = Objects.requireNonNull(builder.destinationDomain);
        this.name = Objects.requireNonNull(builder.name);
        this.traffic = Objects.requireNonNull(builder.traffic);
    }

    public static class Builder {
        private String destinationDomain = null;
        private String name = null;
        private Traffic traffic = null;

        public TrafficDestination.Builder withDestinationDomain(String destinationDomain) {
            this.destinationDomain = destinationDomain;
            return this;
        }

        public TrafficDestination.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public TrafficDestination.Builder withTraffic(Traffic traffic) {
            this.traffic = traffic;
            return this;
        }

        public TrafficDestination build() {
            return new TrafficDestination(this);
        }
    }

    public static TrafficDestination.Builder builder() {
        return new TrafficDestination.Builder();
    }

    public String getDestinationDomain() {
        return destinationDomain;
    }

    public String getName() {
        return name;
    }

    public Traffic getTraffic() {
        return traffic;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("destinationDomain", destinationDomain)
                .add("name", name)
                .add("traffic", traffic)
                .toString();
    }
}
