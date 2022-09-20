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
import java.util.StringJoiner;
import java.util.UUID;

public class Enterprise {
    public final UUID enterpriseId;
    public final Integer id;
    public final Integer networkId;
    public final Integer gatewayPoolId;
    public final String bastionState;
    public final Boolean alertsEnabled;
    public final Boolean operatorAlertsEnabled;
    public final String name;
    public final String domain;
    public final String accountNumber;
    public final String description;
    public final String address;
    public final String city;
    public final String state;
    public final String zip;
    public final String country;
    public final Double latitude;
    public final Double longitude;
    public final String timezone;
    public final String locale;


    private Enterprise(final Builder builder) {
        this.enterpriseId = Objects.requireNonNull(builder.enterpriseId);
        this.id = Objects.requireNonNull(builder.id);
        this.networkId = Objects.requireNonNull(builder.networkId);
        this.gatewayPoolId = Objects.requireNonNull(builder.gatewayPoolId);
        this.bastionState = Objects.requireNonNull(builder.bastionState);
        this.alertsEnabled = Objects.requireNonNull(builder.alertsEnabled);
        this.operatorAlertsEnabled = Objects.requireNonNull(builder.operatorAlertsEnabled);
        this.name = Objects.requireNonNull(builder.name);
        this.domain = Objects.requireNonNull(builder.domain);
        this.accountNumber = Objects.requireNonNull(builder.accountNumber);
        this.description = Objects.requireNonNull(builder.description);
        this.address = Objects.requireNonNull(builder.address);
        this.city = Objects.requireNonNull(builder.city);
        this.state = Objects.requireNonNull(builder.state);
        this.zip = Objects.requireNonNull(builder.zip);
        this.country = Objects.requireNonNull(builder.country);
        this.latitude = Objects.requireNonNull(builder.latitude);
        this.longitude = Objects.requireNonNull(builder.longitude);
        this.timezone = Objects.requireNonNull(builder.timezone);
        this.locale = Objects.requireNonNull(builder.locale);
    }

    public static class Builder {

        private UUID enterpriseId;
        private Integer id;
        private Integer networkId;
        private Integer gatewayPoolId;
        private String bastionState;
        private Boolean alertsEnabled;
        private Boolean operatorAlertsEnabled;
        private String name;
        private String domain;
        private String accountNumber;
        private String description;
        private String address;
        private String city;
        private String state;
        private String zip;
        private String country;
        private Double latitude;
        private Double longitude;
        private String timezone;
        private String locale;

        private Builder() {
        }

        public Builder withNetworkId(Integer networkId) {
            this.networkId = networkId;
            return this;
        }

        public Builder withGatewayPoolId(Integer gatewayPoolId) {
            this.gatewayPoolId = gatewayPoolId;
            return this;
        }

        public Builder withBastionState(String bastionState) {
            this.bastionState = bastionState;
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

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDomain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder withAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withState(String state) {
            this.state = state;
            return this;
        }

        public Builder withZip(String zip) {
            this.zip = zip;
            return this;
        }

        public Builder withCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder withLatitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder withLongitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder withTimezone(String timezone) {
            this.timezone = timezone;
            return this;
        }

        public Builder withLocale(String locale) {
            this.locale = locale;
            return this;
        }

        public Enterprise.Builder withEnterpriseId(final UUID enterpriseId) {
            this.enterpriseId = enterpriseId;
            return this;
        }

        public Enterprise.Builder withId(final Integer id) {
            this.id = id;
            return this;
        }

        public Enterprise build() {
            return new Enterprise(this);
        }
    }

    public static Builder builder() {
        return new Enterprise.Builder();
    }

    public String toString() {
        return new StringJoiner(", ")
                .add("id:" + this.id)
                .add("enterpriseId:" + this.enterpriseId)
                .add("networkId:" + this.networkId)
                .add("gatewayPoolId:" + this.gatewayPoolId)
                .add("bastionState:" + this.bastionState)
                .add("alertsEnabled:" + this.alertsEnabled)
                .add("operatorAlertsEnabled:" + this.operatorAlertsEnabled)
                .add("name:" + this.name)
                .add("domain:" + this.domain)
                .add("accountNumber:" + this.accountNumber)
                .add("description:" + this.description)
                .add("address:" + this.address)
                .add("city:" + this.city)
                .add("state:" + this.state)
                .add("zip:" + this.zip)
                .add("country:" + this.country)
                .add("latitude:" + this.latitude)
                .add("longitude:" + this.longitude)
                .add("timezone:" + this.timezone)
                .add("locale:" + this.locale)
                .toString();
    }
}
