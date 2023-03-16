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
import com.google.common.base.Strings;

import java.time.OffsetDateTime;
import java.util.Objects;

public class Link {
    public final int id;
    public final OffsetDateTime created;
    public final int edgeId;
    public final String logicalId;
    public final String internalId;
    public final String deviceInterface;
    public final String macAddress;
    public final String ipAddress;
    public final String ipv6Address;
    public final String netmask;
    public final String networkSide;
    public final String networkType;
    public final String displayName;
    public final String isp;
    public final String org;
    public final double lat;
    public final double lon;
    public final String linkMode;
    public final boolean alertsEnabled;
    public final boolean operatorAlertsEnabled;
    public final String linkState;
    public final String serviceState;

    private Link(final Builder builder) {
        this.id = Objects.requireNonNull(builder.id);
        this.created = Objects.requireNonNull(builder.created);
        this.edgeId = Objects.requireNonNull(builder.edgeId);
        this.logicalId = Objects.requireNonNull(builder.logicalId);
        this.internalId = Objects.requireNonNull(builder.internalId);
        this.deviceInterface = Objects.requireNonNull(builder.deviceInterface);
        this.macAddress = Strings.emptyToNull(builder.macAddress);
        this.ipAddress = Strings.emptyToNull(builder.ipAddress);
        this.ipv6Address = Strings.emptyToNull(builder.ipv6Address);
        this.netmask = Strings.emptyToNull(builder.netmask);
        this.networkSide = Objects.requireNonNull(builder.networkSide);
        this.networkType = Objects.requireNonNull(builder.networkType);
        this.displayName = Objects.requireNonNull(builder.displayName);
        this.isp = Strings.emptyToNull(builder.isp);
        this.org = Strings.emptyToNull(builder.org);
        this.lat = Objects.requireNonNull(builder.lat);
        this.lon = Objects.requireNonNull(builder.lon);
        this.linkMode = builder.linkMode;
        this.alertsEnabled = builder.alertsEnabled;
        this.operatorAlertsEnabled = builder.operatorAlertsEnabled;
        this.linkState = Objects.requireNonNull(builder.linkState);
        this.serviceState = Objects.requireNonNull(builder.serviceState);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", this.id)
                .add("created", this.created)
                .add("edgeId", this.edgeId)
                .add("logicalId", this.logicalId)
                .add("internalId", this.internalId)
                .add("interface", this.deviceInterface)
                .add("macAddress", this.macAddress)
                .add("ipAddress", this.ipAddress)
                .add("ipv6Address", this.ipv6Address)
                .add("netmask", this.netmask)
                .add("networkSide", this.networkSide)
                .add("networkType", this.networkType)
                .add("displayName", this.displayName)
                .add("isp", this.isp)
                .add("org", this.org)
                .add("lat", this.lat)
                .add("lon", this.lon)
                .add("linkMode", this.linkMode)
                .add("alertsEnabled", this.alertsEnabled)
                .add("operatorAlertsEnabled", this.operatorAlertsEnabled)
                .add("linkState", this.linkState)
                .add("serviceStatus", this.serviceState)
                .toString();
    }

    public static class Builder {
        private Integer id;
        private OffsetDateTime created;
        private Integer edgeId;
        private String logicalId;
        private String internalId;
        private String deviceInterface;
        private String macAddress;
        private String ipAddress;
        private String ipv6Address;
        private String netmask;
        private String networkSide;
        private String networkType;
        private String displayName;
        private String isp;
        private String org;
        private Double lat;
        private Double lon;
        private String linkMode;
        private boolean alertsEnabled;
        private boolean operatorAlertsEnabled;
        private String linkState;
        private String serviceState;

        private Builder() {
        }

        public Builder withId(final Integer id) {
            this.id = id;
            return this;
        }

        public Builder withCreated(OffsetDateTime created) {
            this.created = created;
            return this;
        }

        public Builder withEdgeId(final Integer edgeId) {
            this.edgeId = edgeId;
            return this;
        }

        public Builder withLogicalId(String logicalId) {
            this.logicalId = logicalId;
            return this;
        }

        public Builder withInternalId(String internalId) {
            this.internalId = internalId;
            return this;
        }

        public Builder withInterface(String deviceInterface) {
            this.deviceInterface = deviceInterface;
            return this;
        }

        public Builder withMacAddress(String macAddress) {
            this.macAddress = macAddress;
            return this;
        }

        public Builder withIpAddress(final String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder withIpv6Address(final String ipv6Address) {
            this.ipv6Address = ipv6Address;
            return this;
        }

        public Builder withNetmask(final String netmask) {
            this.netmask = netmask;
            return this;
        }

        public Builder withNetworkSide(String networkSide) {
            this.networkSide = networkSide;
            return this;
        }

        public Builder withNetworkType(String networkType) {
            this.networkType = networkType;
            return this;
        }

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder withIsp(String isp) {
            this.isp = isp;
            return this;
        }

        public Builder withOrg(String org) {
            this.org = org;
            return this;
        }

        public Builder withLat(Double lat) {
            this.lat = lat;
            return this;
        }

        public Builder withLon(Double lon) {
            this.lon = lon;
            return this;
        }

        public Builder withLinkMode(String linkMode) {
            this.linkMode = linkMode;
            return this;
        }

        public Builder withAlertsEnabled(Boolean alertsEnabled) {
            this.alertsEnabled = alertsEnabled;
            return this;
        }

        public Builder withOperatorAlertsEnabled(Boolean operatorAlertsEnabled) {
            this.operatorAlertsEnabled = operatorAlertsEnabled;
            return this;
        }

        public Builder withLinkState(final String linkState) {
            this.linkState = linkState;
            return this;
        }

        public Builder withServiceState(final String serviceState) {
            this.serviceState = serviceState;
            return this;
        }

        public Link build() {
            return new Link(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

