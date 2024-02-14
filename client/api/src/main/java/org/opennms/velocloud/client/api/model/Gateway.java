/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022-2024 The OpenNMS Group, Inc.
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
import static org.opennms.velocloud.client.api.internal.Utils.emptyToNull;

import java.net.InetAddress;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class Gateway {

    public final String gatewayId;
    public final int id;
    public final String gatewayName;
    public final String description;
    public final InetAddress ipAddress;
    public final boolean isLoadBalanced;
    public final String gatewayState;
    public final String serviceState;
    public final String bastionState;
    public final String deviceId;
    public final String dnsName;
    public final int siteId;
    public final String siteName;
    public final String address;
    public final String address2;
    public final String zip;
    public final String city;
    public final String state;
    public final String country;
    public final Double latitude;
    public final Double longitude;
    public final String softwareVersion;
    public final String buildNumber;
    public final int networkId;
    public final InetAddress privateIpAddress;
    public final List<String> roles;
    public final int connectedEdges;

    private Gateway(final Builder builder) {
        this.gatewayId = Objects.requireNonNull(builder.gatewayId);
        this.id = Objects.requireNonNull(builder.id);
        this.gatewayName = Objects.requireNonNull(builder.gatewayName);
        this.description = Objects.toString(builder.description, "");
        this.ipAddress = Objects.requireNonNull(builder.ipAddress);
        this.isLoadBalanced = Objects.requireNonNull(builder.isLoadBalanced);
        this.gatewayState = Objects.requireNonNull(builder.gatewayState);
        this.serviceState = Objects.requireNonNull(builder.serviceState);
        this.bastionState = Objects.toString(builder.bastionState, "");
        this.deviceId = Objects.requireNonNull(builder.deviceId);
        this.dnsName = Objects.toString(builder.dnsName, "");
        this.siteId = Objects.requireNonNull(builder.siteId);
        this.siteName = Objects.toString(builder.siteName, "");
        this.address = emptyToNull(builder.address);
        this.address2 = emptyToNull(builder.address2);
        this.zip = emptyToNull(builder.zip);
        this.city = emptyToNull(builder.city);
        this.state = emptyToNull(builder.state);
        this.country = emptyToNull(builder.country);
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.softwareVersion = Objects.requireNonNull(builder.softwareVersion);
        this.buildNumber = Objects.requireNonNull(builder.buildNumber);
        this.networkId = Objects.requireNonNull(builder.networkId);
        this.privateIpAddress = builder.privateIpAddress;
        this.roles = Objects.requireNonNull(builder.roles);
        this.connectedEdges = Objects.requireNonNull(builder.connectedEdges);
    }

    public static class Builder {

        private String gatewayId;
        private Integer id;
        private String gatewayName;
        private String description;
        private InetAddress ipAddress;
        private Boolean isLoadBalanced;
        private String gatewayState;
        private String serviceState;
        private String bastionState;
        private String deviceId;
        private String dnsName;
        private Integer siteId;
        private String siteName;
        private String address;
        private String address2;
        private String zip;
        private String city;
        private String state;
        private String country;
        private Double latitude;
        private Double longitude;
        private String softwareVersion;
        private String buildNumber;
        private Integer networkId;
        private InetAddress privateIpAddress;
        private List<String> roles;
        private Integer connectedEdges;

        private Builder() {
        }

        public Gateway.Builder withGatewayId(final String gatewayId) {
            this.gatewayId = gatewayId;
            return this;
        }

        public Gateway.Builder withId(final Integer id) {
            this.id = id;
            return this;
        }

        public Gateway.Builder withName(final String gatewayName) {
            this.gatewayName = gatewayName;
            return this;
        }

        public Gateway.Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public Gateway.Builder withIpAddress(final InetAddress ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Gateway.Builder withIsLoadBalanceed(final Boolean isLoadBalanced) {
            this.isLoadBalanced = isLoadBalanced;
            return this;
        }

        public Gateway.Builder withGatewayState(final String gatewayState) {
            this.gatewayState = gatewayState;
            return this;
        }

        public Gateway.Builder withServiceState(final String serviceState) {
            this.serviceState = serviceState;
            return this;
        }

        public Gateway.Builder withBastionState(final String bastionState) {
            this.bastionState = bastionState;
            return this;
        }

        public Gateway.Builder withDeviceId(final String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Gateway.Builder withDnsName(final String dnsName) {
            this.dnsName = dnsName;
            return this;
        }

        public Gateway.Builder withSiteId(final Integer siteId) {
            this.siteId = siteId;
            return this;
        }

        public Gateway.Builder withSiteName(final String siteName) {
            this.siteName = siteName;
            return this;
        }

        public Gateway.Builder withAddress(final String address) {
            this.address = address;
            return this;
        }

        public Gateway.Builder withAddress2(final String address2) {
            this.address2 = address2;
            return this;
        }

        public Gateway.Builder withZip(final String zip) {
            this.zip = zip;
            return this;
        }

        public Gateway.Builder withCity(final String city) {
            this.city = city;
            return this;
        }

        public Gateway.Builder withState(final String state) {
            this.state = state;
            return this;
        }

        public Gateway.Builder withCountry(final String country) {
            this.country = country;
            return this;
        }

        public Gateway.Builder withLatitude(final Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Gateway.Builder withLongitude(final Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Gateway.Builder withSoftwareVersion(final String softwareVersion) {
            this.softwareVersion = softwareVersion;
            return this;
        }

        public Gateway.Builder withBuildNumber(final String buildNumber) {
            this.buildNumber = buildNumber;
            return this;
        }

        public Builder withNetworkId(Integer networkId) {
            this.networkId = networkId;
            return this;
        }

        public Builder withPrivateIpAddress(InetAddress privateIpAddress) {
            this.privateIpAddress = privateIpAddress;
            return this;
        }

        public Builder withRoles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder withConnectedEdges(Integer connectedEdges) {
            this.connectedEdges = connectedEdges;
            return this;
        }

        public Gateway build() {
            return new Gateway(this);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public String toString() {
        return new StringJoiner(", ")
                .add("gatewayId:" + this.gatewayId)
                .add("id:" + this.id)
                .add("gatewayName:" + this.gatewayName)
                .add("description:" + this.description)
                .add("ipAddress:" + this.ipAddress)
                .add("isLoadBalanced:" + this.isLoadBalanced)
                .add("gatewayState:" + this.gatewayState)
                .add("serviceState:" + this.serviceState)
                .add("bastionState:" + this.bastionState)
                .add("deviceId:" + this.deviceId)
                .add("dnsName:" + this.dnsName)
                .add("siteId:" + this.siteId)
                .add("siteName:" + this.siteName)
                .add("address:" + this.address)
                .add("address2:" + this.address2)
                .add("zip:" + this.zip)
                .add("city:" + this.city)
                .add("state:" + this.state)
                .add("country:" + this.country)
                .add("latitude:" + this.latitude)
                .add("longitude:" + this.longitude)
                .add("softwareVersion:" + this.softwareVersion)
                .add("buildNumber:" + this.buildNumber)
                .add("networkId:" + this.networkId)
                .add("privateIpAddress:" + this.privateIpAddress)
                .add("roles:" + this.roles)
                .toString();
    }
}

