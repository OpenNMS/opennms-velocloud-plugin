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

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.StringJoiner;

import org.json.JSONObject;

public class ProxyEvent {
    private String detail;
    private String category;
    private String event;
    private Integer id;
    private OffsetDateTime eventTime;
    private String proxyUsername;
    private String networkName;
    private String enterpriseName;
    private String gatewayName;
    private String message;
    private String severity;

    private ProxyEvent(final ProxyEvent.Builder builder) {
        this.detail = Objects.requireNonNull(builder.detail);
        this.category = Objects.requireNonNull(builder.category);
        this.event = Objects.requireNonNull(builder.event);
        this.id = Objects.requireNonNull(builder.id);
        this.eventTime = Objects.requireNonNull(builder.eventTime);
        this.message = Objects.requireNonNull(builder.message);
        this.severity = Objects.requireNonNull(builder.severity);

        this.proxyUsername = builder.proxyUsername;
        this.networkName = builder.networkName;
        this.enterpriseName = builder.enterpriseName;
        this.gatewayName = builder.gatewayName;
    }

    public String getDetail() {
        return detail;
    }

    public String getCategory() {
        return category;
    }

    public String getEvent() {
        return event;
    }

    public Integer getId() {
        return id;
    }

    public OffsetDateTime getEventTime() {
        return eventTime;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getNetworkName() {
        return networkName;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public String getMessage() {
        return message;
    }

    public String getSeverity() {
        return severity;
    }

    public static class Builder {
        private String detail;
        private String category;
        private String event;
        private Integer id;
        private OffsetDateTime eventTime;
        private String proxyUsername;
        private String networkName;
        private String enterpriseName;
        private String gatewayName;
        private String message;
        private String severity;

        public ProxyEvent.Builder withProxyUsername(final String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        public ProxyEvent.Builder withNetworkName(final String networkName) {
            this.networkName = networkName;
            return this;
        }

        public ProxyEvent.Builder withEnterpriseName(final String enterpriseName) {
            this.enterpriseName = enterpriseName;
            return this;
        }

        public ProxyEvent.Builder withGatewayName(final String gatewayName) {
            this.gatewayName = gatewayName;
            return this;
        }

        public ProxyEvent.Builder withDetail(final String detail) {
            this.detail = detail;
            return this;
        }

        public ProxyEvent.Builder withCategory(final String category) {
            this.category = category;
            return this;
        }

        public ProxyEvent.Builder withEvent(final String event) {
            this.event = event;
            return this;
        }

        public ProxyEvent.Builder withId(final Integer id) {
            this.id = id;
            return this;
        }

        public ProxyEvent.Builder withEventTime(final OffsetDateTime eventTime) {
            this.eventTime = eventTime;
            return this;
        }

        public ProxyEvent.Builder withMessage(final String message) {
            this.message = message;
            return this;
        }

        public ProxyEvent.Builder withSeverity(final String severity) {
            this.severity = severity;
            return this;
        }

        public ProxyEvent build() {
            return new ProxyEvent(this);
        }
    }

    public static ProxyEvent.Builder builder() {
        return new ProxyEvent.Builder();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ")
                .add("detail:" + this.detail)
                .add("category:" + this.category)
                .add("event:" + this.event)
                .add("id:" + this.id)
                .add("eventTime:" + this.eventTime)
                .add("message:" + this.message)
                .add("severity:" + this.severity)
                .add("proxyUsername:" + this.proxyUsername)
                .add("networkName:" + this.networkName)
                .add("enterpriseName:" + this.enterpriseName)
                .add("gatewayName:" + this.gatewayName)
                .toString();
    }
}
