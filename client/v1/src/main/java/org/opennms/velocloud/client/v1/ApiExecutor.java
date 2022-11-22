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

package org.opennms.velocloud.client.v1;

import java.util.concurrent.TimeUnit;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.v1.api.AllApi;

import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;


public class ApiExecutor {

    private static class Key<K, V> {
        final ApiCall<K, V> apiCall;
        final VelocloudApiClientCredentials credentials;
        final K parameter;

        public Key(ApiCall<K, V> apiCall, VelocloudApiClientCredentials credentials, K parameter) {
            this.apiCall = apiCall;
            this.credentials = credentials;
            this.parameter = parameter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key<?, ?> other = (Key<?, ?>) o;
            return this.apiCall == other.apiCall
                    && this.credentials.equals(other.credentials)
                    && Objects.equal(parameter, other.parameter);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(apiCall, credentials, parameter);
        }
    }

    final Cache<Object, Object> cache;

    public ApiExecutor(long expiringAfterMilliseconds) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiringAfterMilliseconds, TimeUnit.MILLISECONDS)
                .build();
    }

    @SuppressWarnings("unchecked")
    <K, V> VelocloudCall<K, V> createApiCall(final String desc, final ApiCall<K, V> apiCall,
                                             final VelocloudApiClientCredentials credentials, final AllApi api) {
        return parameter -> {
            try {
                final Key<K, V> key = new Key<>(apiCall, credentials, parameter);
                return (V) cache.get(key, () -> key.apiCall.doCall(api, parameter));
            } catch(Exception e) {
                throw new VelocloudApiException("Failed to execute API call: " + desc, e);
            }
        };
    }

}