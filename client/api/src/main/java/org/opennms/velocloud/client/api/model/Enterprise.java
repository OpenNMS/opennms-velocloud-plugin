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

    private Enterprise(final Builder builder){
        this.enterpriseId = Objects.requireNonNull(builder.enterpriseId);
        this.id = Objects.requireNonNull(builder.id);
    }

    public static class Builder{

        private UUID enterpriseId;
        private Integer id;

        private Builder() {}

        public Enterprise.Builder setEnterpriseId(final UUID enterpriseId){
            this.enterpriseId = enterpriseId;
            return this;
        }

        public Enterprise.Builder setId(final Integer id){
            this.id = id;
            return this;
        }

        public Enterprise build(){
            return new Enterprise(this);
        }
    }

    public static Builder builder(){
        return new Enterprise.Builder();
    }

    public String toString(){
        return new StringJoiner(", ")
                .add("id:" + this.id)
                .add("enterpriseId:" + this.enterpriseId)
                .toString();
    }
}
