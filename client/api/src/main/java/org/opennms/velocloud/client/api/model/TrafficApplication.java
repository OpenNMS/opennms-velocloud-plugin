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

public class TrafficApplication {

    private final String name;
    private final Traffic traffic;

    private TrafficApplication(final TrafficApplication.Builder builder) {
        this.name = Objects.requireNonNull(builder.name);
        this.traffic = builder.traffic;
    }

    public static class Builder {
        private String name = null;
        private Traffic traffic = null;

        public TrafficApplication.Builder withName(String name) {
            this.name = name;
            return this;
        }
        public TrafficApplication.Builder withTraffic(Traffic traffic) {
            this.traffic = traffic;
            return this;
        }

        public TrafficApplication build() {
            return new TrafficApplication(this);
        }
    }

    public static TrafficApplication.Builder builder() {
        return new TrafficApplication.Builder();
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
                .add("name", name)
                .add("traffic", traffic)
                .toString();
    }
}
