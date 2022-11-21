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

import java.util.List;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

public class Edge {
    public final String operator;
    public final String site;
    public final boolean hub;
    public final List<Link> links;
    public final boolean alertsEnabled;
    public final String buildNumber;
    public final String customInfo;
    public final String description;
    public final String deviceFamily;
    public final String deviceId;
    public final String dnsName;
    public final String lteRegion;
    public final String logicalId;
    public final String modelNumber;
    public final String name;
    public final boolean operatorAlertsEnabled;
    public final String selfMacAddress;
    public final int siteId;
    public final String softwareVersion;
    public final String edgeState;
    public final String serviceState;
    public final String siteName;
    public final String address;
    public final String address2;
    public final String zip;
    public final String city;
    public final String state;
    public final String country;
    public final Double latitude;
    public final Double longitude;

    private Edge(final Builder builder) {
        this.operator = Objects.requireNonNull(builder.operator);
        this.site = builder.site;
        this.hub = builder.hub;
        this.links = Objects.requireNonNull(builder.links);
        this.alertsEnabled = builder.alertsEnabled;
        this.buildNumber = Objects.requireNonNull(builder.buildNumber);
        this.customInfo = Strings.nullToEmpty(builder.customInfo);
        this.description = Strings.nullToEmpty(builder.description);
        this.deviceFamily = Strings.emptyToNull(builder.deviceFamily);
        this.deviceId = Strings.nullToEmpty(builder.deviceId);
        this.dnsName = Strings.emptyToNull(builder.dnsName);
        this.lteRegion = Strings.emptyToNull(builder.lteRegion);
        this.logicalId = Objects.requireNonNull(builder.logicalId);
        this.modelNumber = Strings.emptyToNull(builder.modelNumber);
        this.name = Objects.requireNonNull(builder.name);
        this.operatorAlertsEnabled = builder.operatorAlertsEnabled;
        this.selfMacAddress = Objects.requireNonNull(builder.selfMacAddress);
        this.siteId = builder.siteId;
        this.softwareVersion = Objects.requireNonNull(builder.softwareVersion);
        this.edgeState = Objects.requireNonNull(builder.edgeState);
        this.serviceState = Objects.requireNonNull(builder.serviceState);
        this.siteName = Strings.emptyToNull(builder.siteName);
        this.address = Strings.emptyToNull(builder.address);
        this.address2 = Strings.emptyToNull(builder.address2);
        this.zip = Strings.emptyToNull(builder.zip);
        this.city = Strings.emptyToNull(builder.city);
        this.state = Strings.emptyToNull(builder.state);
        this.country = Strings.emptyToNull(builder.country);
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("operator", this.operator)
                          .add("site", this.site)
                          .add("hub", this.hub)
                          .add("links", this.links)
                          .add("alertsEnabled", this.alertsEnabled)
                          .add("buildNumber", this.buildNumber)
                          .add("customInfo", this.customInfo)
                          .add("description", this.description)
                          .add("deviceFamily", this.deviceFamily)
                          .add("deviceId", this.deviceId)
                          .add("dnsName", this.dnsName)
                          .add("lteRegion", this.lteRegion)
                          .add("logicalId", this.logicalId)
                          .add("modelNumber", this.modelNumber)
                          .add("name", this.name)
                          .add("operatorAlertsEnabled", this.operatorAlertsEnabled)
                          .add("selfMacAddress", this.selfMacAddress)
                          .add("edgeState", this.edgeState)
                          .add("serviceState", this.serviceState)
                          .add("siteId", this.siteId)
                          .add("siteName:", this.siteName)
                          .add("address:", this.address)
                          .add("address2:", this.address2)
                          .add("zip:", this.zip)
                          .add("city:", this.city)
                          .add("state:", this.state)
                          .add("country:", this.country)
                          .add("latitude:", this.latitude)
                          .add("longitude:", this.longitude)
                          .add("softwareVersion", this.softwareVersion)
                          .add("edgeStatus", this.edgeState)
                          .add("serviceStatus", this.serviceState)
                          .toString();
    }

    public static class Builder {
        private String operator;
        private String site;
        private boolean hub;
        private List<Link> links;
        private boolean alertsEnabled;
        private String buildNumber;
        private String customInfo;
        private String description;
        private String deviceFamily;
        private String deviceId;
        private String dnsName;
        private String lteRegion;
        private String logicalId;
        private String modelNumber;
        private String name;
        private boolean operatorAlertsEnabled;
        private String selfMacAddress;
        private int siteId;
        private String softwareVersion;
        private String edgeState;
        private String serviceState;
        private String siteName;
        private String address;
        private String address2;
        private String zip;
        private String city;
        private String state;
        private String country;
        private Double latitude;
        private Double longitude;

        private Builder() {
        }

        public Builder withOperator(final String operator) {
            this.operator = operator;
            return this;
        }

        public Builder withSite(final String site) {
            this.site = site;
            return this;
        }

        public Builder withHub(final boolean hub) {
            this.hub = hub;
            return this;
        }

        public Builder withLinks(final List<Link> links) {
            this.links = links;
            return this;
        }

        public Builder withAlertsEnabled(Boolean alertsEnabled) {
            this.alertsEnabled = alertsEnabled;
            return this;
        }

        public Builder withBuildNumber(String buildNumber) {
            this.buildNumber = buildNumber;
            return this;
        }

        public Builder withCustomInfo(String customInfo) {
            this.customInfo = customInfo;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withDeviceFamily(String deviceFamily) {
            this.deviceFamily = deviceFamily;
            return this;
        }

        public Builder withDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder withDnsName(String dnsName) {
            this.dnsName = dnsName;
            return this;
        }

        public Builder withLteRegion(String lteRegion) {
            this.lteRegion = lteRegion;
            return this;
        }

        public Builder withLogicalId(String logicalId) {
            this.logicalId = logicalId;
            return this;
        }

        public Builder withModelNumber(String modelNumber) {
            this.modelNumber = modelNumber;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withOperatorAlertsEnabled(Boolean operatorAlertsEnabled) {
            this.operatorAlertsEnabled = operatorAlertsEnabled;
            return this;
        }

        public Builder withSelfMacAddress(String selfMacAddress) {
            this.selfMacAddress = selfMacAddress;
            return this;
        }

        public Builder withSiteId(Integer siteId) {
            this.siteId = siteId;
            return this;
        }

        public Builder withSoftwareVersion(String softwareVersion) {
            this.softwareVersion = softwareVersion;
            return this;
        }

        public Builder withEdgeState(final String edgeState) {
            this.edgeState = edgeState;
            return this;
        }

        public Builder withServiceState(final String serviceState) {
            this.serviceState = serviceState;
            return this;
        }

        public Builder withSiteName(final String siteName) {
            this.siteName = siteName;
            return this;
        }

        public Builder withAddress(final String address) {
            this.address = address;
            return this;
        }

        public Builder withAddress2(final String address2) {
            this.address2 = address2;
            return this;
        }

        public Builder withZip(final String zip) {
            this.zip = zip;
            return this;
        }

        public Builder withCity(final String city) {
            this.city = city;
            return this;
        }

        public Builder withState(final String state) {
            this.state = state;
            return this;
        }

        public Builder withCountry(final String country) {
            this.country = country;
            return this;
        }

        public Builder withLatitude(final Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder withLongitude(final Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Edge build() {
            return new Edge(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
