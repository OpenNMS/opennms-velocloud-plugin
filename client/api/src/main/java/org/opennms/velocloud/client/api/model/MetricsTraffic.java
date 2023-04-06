/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2023 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2023 The OpenNMS Group, Inc.
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

import java.util.Map;

import com.google.common.base.MoreObjects;

public class MetricsTraffic {

    private final Map<String, Traffic> perApplication;
    private final Map<String, Traffic> perSource;
    private final Map<String, Traffic> perDestination;
    private final Map<String, Traffic> perBusiness;

    private MetricsTraffic(final MetricsTraffic.Builder builder) {
        this.perApplication = builder.perApplication;
        this.perSource = builder.perSource;
        this.perDestination = builder.perDestination;
        this.perBusiness = builder.perBusiness;
    }

    public static class Builder {
        private Map<String, Traffic> perApplication;
        private Map<String, Traffic> perSource;
        private Map<String, Traffic> perDestination;
        private Map<String, Traffic> perBusiness;

        public MetricsTraffic.Builder withPerApplication(Map<String, Traffic> perApplication) {
            this.perApplication = perApplication;
            return this;
        }

        public MetricsTraffic.Builder withPerSource(Map<String, Traffic> perSource) {
            this.perSource = perSource;
            return this;
        }

        public MetricsTraffic.Builder withPerDestination(Map<String, Traffic> perDestination) {
            this.perDestination = perDestination;
            return this;
        }

        public MetricsTraffic.Builder withPerBusiness(Map<String, Traffic> perBusiness) {
            this.perBusiness = perBusiness;
            return this;
        }

        public MetricsTraffic build() {
            return new MetricsTraffic(this);
        }
    }

    public static MetricsTraffic.Builder builder() {
        return new MetricsTraffic.Builder();
    }

    public Map<String, Traffic> getPerApplication() {
        return perApplication;
    }

    public Map<String, Traffic> getPerSource() {
        return perSource;
    }

    public Map<String, Traffic> getPerDestination() {
        return perDestination;
    }

    public Map<String, Traffic> getPerBusiness() {
        return perBusiness;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("perApplication", perApplication)
                .add("perSource", perSource)
                .add("perDestination", perDestination)
                .add("perBusiness", perBusiness)
                .toString();
    }
}
