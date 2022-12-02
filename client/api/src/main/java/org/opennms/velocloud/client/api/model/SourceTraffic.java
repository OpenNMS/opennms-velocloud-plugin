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

public class SourceTraffic {

    private final String sourceMac;
    private final String ipAddress;
    private final String name;
    private final String hostName;
    private final Traffic traffic;

    private SourceTraffic(final SourceTraffic.Builder builder) {
        this.sourceMac = Objects.requireNonNull(builder.sourceMac);
        this.ipAddress = Objects.requireNonNull(builder.ipAddress);
        this.name = Objects.requireNonNull(builder.name);
        this.hostName = Objects.requireNonNull(builder.hostName);
        this.traffic = Objects.requireNonNull(builder.traffic);
    }

    public static class Builder {
        private String sourceMac = null;
        private String ipAddress = null;
        private String hostName = null;
        private String name = null;
        private Traffic traffic = null;

        public SourceTraffic.Builder withSourceMac(String sourceMac) {
            this.sourceMac = sourceMac;
            return this;
        }

        public SourceTraffic.Builder withIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public SourceTraffic.Builder withName(String name) {
            this.name = name;
            return this;
        }
        public SourceTraffic.Builder withHostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public SourceTraffic.Builder withTraffic(Traffic traffic) {
            this.traffic = traffic;
            return this;
        }

        public SourceTraffic build() {
            return new SourceTraffic(this);
        }
    }

    public static SourceTraffic.Builder builder() {
        return new SourceTraffic.Builder();
    }

    public String getSourceMac() {
        return sourceMac;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getHostName() {
        return hostName;
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
                .add("sourceMac", sourceMac)
                .add("ipAddress", ipAddress)
                .add("name", name)
                .add("hostName", hostName)
                .add("traffic", traffic)
                .toString();
    }
}
