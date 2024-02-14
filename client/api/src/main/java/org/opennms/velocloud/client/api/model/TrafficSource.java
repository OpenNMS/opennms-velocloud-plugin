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

public class TrafficSource {

    private final String sourceMac;
    private final String ipAddress;
    private final String name;
    private final String hostName;
    private final String osName;
    private final String osVersion;
    private final String deviceType;
    private final String deviceModel;
    private final Traffic traffic;

    private TrafficSource(final TrafficSource.Builder builder) {
        this.sourceMac = Objects.requireNonNull(builder.sourceMac);
        this.ipAddress = Objects.requireNonNull(builder.ipAddress);
        this.name = Objects.requireNonNull(builder.name);
        this.hostName = Objects.requireNonNull(builder.hostName);
        this.osName = builder.osName;
        this.osVersion = builder.osVersion;
        this.deviceType = builder.deviceType;
        this.deviceModel = builder.deviceModel;
        this.traffic = Objects.requireNonNull(builder.traffic);
    }

    public static class Builder {
        private String sourceMac = null;
        private String ipAddress = null;
        private String hostName = null;
        private String name = null;
        private String osName = null;
        private String osVersion = null;
        private String deviceType = null;
        private String deviceModel = null;
        private Traffic traffic = null;

        public TrafficSource.Builder withSourceMac(String sourceMac) {
            this.sourceMac = sourceMac;
            return this;
        }

        public TrafficSource.Builder withIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public TrafficSource.Builder withName(String name) {
            this.name = name;
            return this;
        }
        public TrafficSource.Builder withHostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public TrafficSource.Builder withOsName(String osName) {
            this.osName = osName;
            return this;
        }

        public TrafficSource.Builder withOsVersion(String osVersion) {
            this.osVersion = osVersion;
            return this;
        }


        public TrafficSource.Builder withDeviceType(String deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public TrafficSource.Builder withDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
            return this;
        }

        public TrafficSource.Builder withTraffic(Traffic traffic) {
            this.traffic = traffic;
            return this;
        }

        public TrafficSource build() {
            return new TrafficSource(this);
        }
    }

    public static TrafficSource.Builder builder() {
        return new TrafficSource.Builder();
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

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    @Override
    public String toString() {
        return "TrafficSource ["
                + "sourceMac=" + sourceMac
                + ", ipAddress=" + ipAddress
                + ", name=" + name
                + ", hostName=" + hostName
                + ", osName=" + osName
                + ", osVersion=" + osVersion
                + ", deviceType=" + deviceType
                + ", deviceModel=" + deviceModel
                + ", traffic=" + traffic
                + "]";
    }
}
