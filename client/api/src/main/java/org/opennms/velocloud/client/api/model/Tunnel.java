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

import java.util.Objects;

public class Tunnel {
    public final int id;

    public final String tag;

    public final String dataKey;

    public final String state;

    public final String link;

    public final String name;

    public final String destination;

    private Tunnel(final Builder builder) {
        this.id = builder.id;
        this.tag = Objects.requireNonNull(builder.tag);
        this.dataKey = Objects.requireNonNull(builder.dataKey);
        this.state = Objects.requireNonNull(builder.state);
        this.link = Objects.requireNonNull(builder.link);
        this.name = Objects.requireNonNull(builder.name);
        this.destination = builder.destination;
    }

    public static class Builder {

        private int id;

        private String tag;

        private String dataKey;

        private String state;

        private String link;

        private String name;

        private String destination;

        private Builder() {
        }

        public Builder withId(final int id) {
            this.id = id;
            return this;
        }

        public Builder withTag(final String tag) {
            this.tag = tag;
            return this;
        }

        public Builder withDataKey(final String dataKey) {
            this.dataKey = dataKey;
            return this;
        }

        public Builder withState(final String state) {
            this.state = state;
            return this;
        }

        public Builder withLink(final String link) {
            this.link = link;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withDestination(final String destination) {
            this.destination = destination;
            return this;
        }

        public Tunnel build() {
            return new Tunnel(this);
        }
    }

    public static Builder builder() {
        return new Tunnel.Builder();
    }

    @Override
    public String toString() {
        return "Tunnel ["
                + "id=" + id
                + ", tag=" + tag
                + ", dataKey=" + dataKey
                + ", state=" + state
                + ", link=" + link
                + ", name=" + name
                + ", destination=" + destination
                + "]";
    }

}
