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

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Link {
    public final Integer id;
    public final OffsetDateTime created;
    public final Integer edgeId;
    public final String logicalId;
    public final String internalId;
    public final String _interface;
    public final String macAddress;
    public final String ipAddress;
    public final String ipv6Address;
    public final String netmask;
    public final String networkSide;
    public final String networkType;
    public final String displayName;
    public final String isp;
    public final String org;
    public final Double lat;
    public final Double lon;
    public final String linkMode;
    public final Boolean alertsEnabled;
    public final Boolean operatorAlertsEnabled;
    public final String serviceGroups;

    private Link(final Builder builder) {
        this.id = Objects.requireNonNull(builder.id);
        this.created = Objects.requireNonNull(builder.created);
        this.edgeId = Objects.requireNonNull(builder.edgeId);
        this.logicalId = Objects.requireNonNull(builder.logicalId);
        this.internalId = Objects.requireNonNull(builder.internalId);
        this._interface = Objects.requireNonNull(builder._interface);
        this.macAddress = Objects.requireNonNull(builder.macAddress);
        this.ipAddress = Objects.requireNonNull(builder.ipAddress);
        this.ipv6Address = builder.ipv6Address;
        this.netmask = Objects.requireNonNull(builder.netmask);
        this.networkSide = Objects.requireNonNull(builder.networkSide);
        this.networkType = Objects.requireNonNull(builder.networkType);
        this.displayName = Objects.requireNonNull(builder.displayName);
        this.isp = Objects.requireNonNull(builder.isp);
        this.org = Objects.requireNonNull(builder.org);
        this.lat = Objects.requireNonNull(builder.lat);
        this.lon = Objects.requireNonNull(builder.lon);
        this.linkMode = builder.linkMode;
        this.alertsEnabled = Objects.requireNonNull(builder.alertsEnabled);
        this.operatorAlertsEnabled = Objects.requireNonNull(builder.operatorAlertsEnabled);
        this.serviceGroups = builder.serviceGroups;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", this.id)
                .add("created", this.created)
                .add("edgeId", this.edgeId)
                .add("logicalId", this.logicalId)
                .add("internalId", this.internalId)
                .add("interface", this._interface)
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
                .add("serviceGroups", this.serviceGroups)
                .toString();
    }

    public static class Builder {
        private Integer id;
        private OffsetDateTime created;
        private Integer edgeId;
        private String logicalId;
        private String internalId;
        private String _interface;
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
        private Boolean alertsEnabled;
        private Boolean operatorAlertsEnabled;
        private String serviceGroups;

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

        public Builder withInterface(String _interface) {
            this._interface = _interface;
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

        public Builder withServiceGroups(String serviceGroups) {
            this.serviceGroups = serviceGroups;
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
