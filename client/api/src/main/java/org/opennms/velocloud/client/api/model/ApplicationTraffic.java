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

public class ApplicationTraffic {

    private final String description;
    private final String displayName;
    private final String name;
    private final String propertyClass;
    private final Traffic traffic;

    private ApplicationTraffic(final ApplicationTraffic.Builder builder) {
        this.description = Objects.requireNonNull(builder.description);
        this.displayName = Objects.requireNonNull(builder.displayName);
        this.name = Objects.requireNonNull(builder.name);
        this.propertyClass = Objects.requireNonNull(builder.propertyClass);
        this.traffic = Objects.requireNonNull(builder.traffic);
    }

    public static class Builder {
        private String description = null;
        private String displayName = null;
        private String propertyClass = null;
        private String name = null;
        private Traffic traffic = null;

        public ApplicationTraffic.Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ApplicationTraffic.Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public ApplicationTraffic.Builder withName(String name) {
            this.name = name;
            return this;
        }
        public ApplicationTraffic.Builder withPropertyClass(String propertyClass) {
            this.propertyClass = propertyClass;
            return this;
        }

        public ApplicationTraffic.Builder withTraffic(Traffic traffic) {
            this.traffic = traffic;
            return this;
        }

        public ApplicationTraffic build() {
            return new ApplicationTraffic(this);
        }
    }

    public static ApplicationTraffic.Builder builder() {
        return new ApplicationTraffic.Builder();
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPropertyClass() {
        return propertyClass;
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
                .add("description", description)
                .add("displayName", displayName)
                .add("propertyClass", propertyClass)
                .toString();
    }    
}
