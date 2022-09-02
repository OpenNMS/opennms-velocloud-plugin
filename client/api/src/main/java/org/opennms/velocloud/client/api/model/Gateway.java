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

import java.net.InetAddress;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Gateway {

    public final UUID enterpriseId;
    public final String gatewayId;
    public final String gatewayName;
    public final InetAddress ipAddress;
    public final String gatewayState;
    public final String serviceState;
    public final String deviceId;
    public final Integer siteId;
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


    private Gateway(final Builder builder){
        this.enterpriseId = Objects.requireNonNull(builder.enterpriseId);
        this.gatewayId = Objects.requireNonNull(builder.gatewayId);
        this.gatewayName = Objects.requireNonNull(builder.gatewayName);
        this.ipAddress = Objects.requireNonNull(builder.ipAddress);
        this.gatewayState = Objects.requireNonNull(builder.gatewayState);
        this.serviceState = Objects.requireNonNull(builder.serviceState);
        this.deviceId = Objects.requireNonNull(builder.deviceId);
        this.siteId = Objects.requireNonNull(builder.siteId);
        this.siteName = Objects.requireNonNull(builder.siteName);
        this.address = Objects.requireNonNull(builder.address);
        this.address2 = Objects.requireNonNull(builder.address2);
        this.zip = Objects.requireNonNull(builder.zip);
        this.city = Objects.requireNonNull(builder.city);
        this.state = Objects.requireNonNull(builder.state);
        this.country = Objects.requireNonNull(builder.country);
        this.latitude = Objects.requireNonNull(builder.latitude);
        this.longitude = Objects.requireNonNull(builder.longitude);
        this.softwareVersion = Objects.requireNonNull(builder.softwareVersion);
        this.buildNumber = Objects.requireNonNull(builder.buildNumber);
    }

    public static class Builder {

        private UUID enterpriseId;
        private String gatewayId;
        private String gatewayName;
        private InetAddress ipAddress;
        private String gatewayState;
        private String serviceState;
        private String deviceId;
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


        private Builder() {
        }

        public Gateway.Builder withEnterpriseId(final UUID enterpriseId) {
            this.enterpriseId = Objects.requireNonNull(enterpriseId);
            return this;
        }

        public Gateway.Builder withGatewayId(final String gatewayId) {
            this.gatewayId = Objects.requireNonNull(gatewayId);
            return this;
        }

        public Gateway.Builder withGatewayName(final String gatewayName) {
            this.gatewayName = Objects.requireNonNull(gatewayName);
            return this;
        }

        public Gateway.Builder withIpAddress(final InetAddress ipAddress) {
            this.ipAddress = Objects.requireNonNull(ipAddress);
            return this;
        }

        public Gateway.Builder withGatewayState(final String gatewayState) {
            this.gatewayState = Objects.requireNonNull(gatewayState);
            return this;
        }

        public Gateway.Builder withServiceState(final String serviceState) {
            this.serviceState = Objects.requireNonNull(serviceState);
            return this;
        }

        public Gateway.Builder withDeviceId(final String deviceId) {
            this.deviceId = Objects.requireNonNull(deviceId);
            return this;
        }

        public Gateway.Builder withSiteId(final Integer siteId) {
            this.siteId = Objects.requireNonNull(siteId);
            return this;
        }

        public Gateway.Builder withSiteName(final String siteName) {
            this.siteName = Objects.requireNonNull(siteName);
            return this;
        }

        public Gateway.Builder withAddress(final String address) {
            this.address = Objects.requireNonNull(address);
            return this;
        }

        public Gateway.Builder withAddress2(final String address2) {
            this.address2 = Objects.requireNonNull(address2);
            return this;
        }

        public Gateway.Builder withZip(final String zip) {
            this.zip = Objects.requireNonNull(zip);
            return this;
        }

        public Gateway.Builder withCity(final String city) {
            this.city = Objects.requireNonNull(city);
            return this;
        }

        public Gateway.Builder withState(final String state) {
            this.state = Objects.requireNonNull(state);
            return this;
        }

        public Gateway.Builder withCountry(final String country) {
            this.country = Objects.requireNonNull(country);
            return this;
        }

        public Gateway.Builder withLatitude(final Double latitude) {
            this.latitude = Objects.requireNonNull(latitude);
            return this;
        }

        public Gateway.Builder withLongitude(final Double longitude) {
            this.longitude = Objects.requireNonNull(longitude);
            return this;
        }

        public Gateway.Builder withSoftwareVersion(final String softwareVersion) {
            this.softwareVersion = Objects.requireNonNull(softwareVersion);
            return this;
        }

        public Gateway.Builder withBuildNumber(final String buildNumber) {
            this.buildNumber = Objects.requireNonNull(buildNumber);
            return this;
        }

        public Gateway build(){
            return new Gateway(this);
        }

    }

    public static Builder builder(){
        return new Builder();
    }

   public String toString(){
       return new StringJoiner(", ")
               .add("enterpriseId:" + this.enterpriseId)
               .add("enterpriseId:" + this.gatewayId)
               .add("enterpriseId:" + this.gatewayName)
               .add("enterpriseId:" + this.gatewayState)
               .add("enterpriseId:" + this.deviceId)
               .add("enterpriseId:" + this.serviceState)
               .add("enterpriseId:" + this.ipAddress)
               .add("enterpriseId:" + this.siteId)
               .add("enterpriseId:" + this.address)
               .add("enterpriseId:" + this.address2)
               .add("enterpriseId:" + this.zip)
               .add("enterpriseId:" + this.city)
               .add("enterpriseId:" + this.state)
               .add("enterpriseId:" + this.country)
               .add("enterpriseId:" + this.longitude)
               .add("enterpriseId:" + this.latitude)
               .add("enterpriseId:" + this.softwareVersion)
               .add("enterpriseId:" + this.buildNumber)
               .toString();




   }
}

