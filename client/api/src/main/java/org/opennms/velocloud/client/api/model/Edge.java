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
import java.util.stream.Collectors;

import com.google.common.base.MoreObjects;

public class Edge {
    public final String operator;
    public final String site;
    public final boolean hub;
    public final List<Link> links;
    public final Boolean alertsEnabled;
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
    public final Boolean operatorAlertsEnabled;
    public final String selfMacAddress;
    public final Integer siteId;
    public final String softwareVersion;

    private Edge(final Builder builder) {
        this.operator = Objects.requireNonNull(builder.operator);
        this.site = Objects.requireNonNull(builder.site);
        this.hub = builder.hub;
        this.links = Objects.requireNonNull(builder.links);
        this.alertsEnabled = Objects.requireNonNull(builder.alertsEnabled);
        this.buildNumber = Objects.requireNonNull(builder.buildNumber);
        this.customInfo = Objects.requireNonNull(builder.customInfo);
        this.description = Objects.requireNonNull(builder.description);
        this.deviceFamily = Objects.requireNonNull(builder.deviceFamily);
        this.deviceId = Objects.requireNonNull(builder.deviceId);
        this.dnsName = Objects.requireNonNull(builder.dnsName);
        this.lteRegion = Objects.requireNonNull(builder.lteRegion);
        this.logicalId = Objects.requireNonNull(builder.logicalId);
        this.modelNumber = Objects.requireNonNull(builder.modelNumber);
        this.name = Objects.requireNonNull(builder.name);
        this.operatorAlertsEnabled = Objects.requireNonNull(builder.operatorAlertsEnabled);
        this.selfMacAddress = Objects.requireNonNull(builder.selfMacAddress);
        this.siteId = Objects.requireNonNull(builder.siteId);
        this.softwareVersion = Objects.requireNonNull(builder.softwareVersion);

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("operator", this.operator)
                          .add("site", this.site)
                          .add("hub", this.hub)
                          .add("links", this.links.stream()
                                                        .map(l -> l.toString())
                                                        .collect(Collectors.joining(",")))
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
                          .add("siteId", this.siteId)
                          .add("softwareVersion", this.softwareVersion)
                          .toString();
    }

    public static class Builder {
        private String operator;
        private String site;
        private boolean hub;
        private List<Link> links;
        private Boolean alertsEnabled;
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
        private Boolean operatorAlertsEnabled;
        private String selfMacAddress;
        private Integer siteId;
        private String softwareVersion;

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

        public Edge build() {
            return new Edge(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
