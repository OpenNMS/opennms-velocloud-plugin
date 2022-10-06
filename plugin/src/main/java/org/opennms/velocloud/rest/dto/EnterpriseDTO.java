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
package org.opennms.velocloud.rest.dto;

import java.util.StringJoiner;

public class EnterpriseDTO {

    private String enterpriseId;
    private int id;
    private int networkId;
    private int gatewayPoolId;
    private String bastionState;
    private boolean alertsEnabled;
    private boolean operatorAlertsEnabled;
    private String name;
    private String domain;
    private String accountNumber;
    private String description;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private double latitude;
    private double longitude;
    private String timezone;
    private String locale;

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }

    public int getGatewayPoolId() {
        return gatewayPoolId;
    }

    public void setGatewayPoolId(int gatewayPoolId) {
        this.gatewayPoolId = gatewayPoolId;
    }

    public String getBastionState() {
        return bastionState;
    }

    public void setBastionState(String bastionState) {
        this.bastionState = bastionState;
    }

    public boolean isAlertsEnabled() {
        return alertsEnabled;
    }

    public void setAlertsEnabled(boolean alertsEnabled) {
        this.alertsEnabled = alertsEnabled;
    }

    public boolean isOperatorAlertsEnabled() {
        return operatorAlertsEnabled;
    }

    public void setOperatorAlertsEnabled(boolean operatorAlertsEnabled) {
        this.operatorAlertsEnabled = operatorAlertsEnabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
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
