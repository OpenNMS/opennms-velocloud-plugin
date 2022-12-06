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

public class Traffic {

    private final long bytesRx;
    private final long bytesTx;
    private final long packetsRx;
    private final long packetsTx;

    private Traffic(final Builder builder) {
        this.bytesRx = builder.bytesRx;
        this.bytesTx = builder.bytesTx;
        this.packetsRx = builder.packetsRx;
        this.packetsTx = builder.packetsTx;
    }

    public static class Builder {
        private long bytesRx = 0;
        private long bytesTx = 0;
        private long packetsRx = 0;
        private long packetsTx = 0;

        public Builder withBytesRx(long bytesRx) {
            this.bytesRx = bytesRx;
            return this;
        }

        public Builder withBytesTx(long bytesTx) {
            this.bytesTx = bytesTx;
            return this;
        }

        public Builder withPacketsRx(long packetsRx) {
            this.packetsRx = packetsRx;
            return this;
        }

        public Builder withPacketsTx(long packetsTx) {
            this.packetsTx = packetsTx;
            return this;
        }

        public Traffic build() {
            return new Traffic(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public long getBytesRx() {
        return bytesRx;
    }

    public long getBytesTx() {
        return bytesTx;
    }

    public long getPacketsRx() {
        return packetsRx;
    }

    public long getPacketsTx() {
        return packetsTx;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bytesRx", bytesRx)
                .add("bytesTx", bytesTx)
                .add("packetsRx", packetsRx)
                .add("packetsTx", packetsTx)
                .toString();
    }
}
