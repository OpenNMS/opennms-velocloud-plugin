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

public class MetricsEdgeLink {

    private final Integer linkId;
    private final Integer bpsOfBestPathRx;
    private final Integer bpsOfBestPathTx;
    private MetricsEdgeLink(final MetricsEdgeLink.Builder builder) {
        this.linkId = Objects.requireNonNull(builder.linkId);
        this.bpsOfBestPathRx = Objects.requireNonNull(builder.bpsOfBestPathRx);
        this.bpsOfBestPathTx = Objects.requireNonNull(builder.bpsOfBestPathTx);
    }

    public static class Builder {
        public Integer linkId;
        private Integer bpsOfBestPathRx = null;
        private Integer bpsOfBestPathTx = null;

        public Builder withLinkId(Integer linkId) {
            this.linkId = linkId;
            return this;
        }

        public Builder withBpsOfBestPathRx(Integer bpsOfBestPathRx) {
            this.bpsOfBestPathRx = bpsOfBestPathRx;
            return this;
        }

        public Builder withBpsOfBestPathTx(Integer bpsOfBestPathTx) {
            this.bpsOfBestPathTx = bpsOfBestPathTx;
            return this;
        }

        public MetricsEdgeLink build() {
            return new MetricsEdgeLink(this);
        }
    }

    public static MetricsEdgeLink.Builder builder() {
        return new MetricsEdgeLink.Builder();
    }

}
