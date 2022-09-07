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


public class User {
    public final Integer id;
    public final String userType;
    public final String domain;
    public final String username;
    public final String firstName;
    public final String lastName;
    public final String email;
    public final Boolean isNative;
    public final Boolean isActive;
    public final Boolean isLocked;
    public final Integer roleId;
    public final String roleName;
    public final String accessLevel;
    public final String sshUsername;

    private User(final Builder builder){

        this.id = Objects.requireNonNull(builder.id);
        this.userType = Objects.requireNonNull(builder.userType);
        this.domain = Objects.requireNonNull(builder.domain);
        this.username = Objects.requireNonNull(builder.username);
        this.firstName = Objects.requireNonNull(builder.firstName);
        this.lastName = Objects.requireNonNull(builder.lastName);
        this.email = Objects.requireNonNull(builder.email);
        this.isActive = Objects.requireNonNull(builder.isActive);
        this.isLocked = Objects.requireNonNull(builder.isLocked);
        this.isNative = Objects.requireNonNull(builder.isNative);
        this.roleId = Objects.requireNonNull(builder.roleId);
        this.roleName = Objects.requireNonNull(builder.roleName);
        this.accessLevel = Objects.requireNonNull(builder.accessLevel);
        this.sshUsername = Objects.requireNonNull(builder.sshUsername);
    }

    public static class Builder{
        private Integer id;
        private String userType;
        private String domain;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private Boolean isNative;
        private Boolean isActive;
        private Boolean isLocked;
        private Integer roleId;
        private String roleName;
        private String accessLevel;
        private String sshUsername;

        private Builder(){

        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setUserType(String userType) {
            this.userType = userType;
            return this;
        }

        public Builder setDomain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setNative(Boolean isNative) {
            this.isNative = isNative;
            return this;
        }

        public Builder setActive(Boolean active) {
            isActive = active;
            return this;
        }

        public Builder setLocked(Boolean locked) {
            isLocked = locked;
            return this;
        }

        public Builder setRoleId(Integer roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder setRoleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public Builder setAccessLevel(String accessLevel) {
            this.accessLevel = accessLevel;
            return this;
        }

        public Builder setSshUsername(String sshUsername) {
            this.sshUsername = sshUsername;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }

    public static Builder builder(){
        return new User.Builder();
    }

    public String toString() {
        return new StringJoiner(", ")
                .add("id:" + this.id)
                .add("userType:" + this.userType)
                .add("domain:" + this.domain)
                .add("username:" + this.username)
                .add("firstName:" + this.firstName)
                .add("lastName:" + this.lastName)
                .add("email:" + this.email)
                .add("isNative:" + this.isNative)
                .add("isActive:" + this.isActive)
                .add("isLocked:" + this.isLocked)
                .add("roleId:" + this.roleId)
                .add("roleName:" + this.roleName)
                .add("accessLevel:" + this.accessLevel)
                .add("sshUsername:" + this.sshUsername)
                .toString();
    }
}
