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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.handler.ApiException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;


public class ApiCache {

    private static class Key<B, R> {
        private final Endpoint<B, R> apiCall;
        private final VelocloudApiClientCredentials credentials;
        private final B body;

        public Key(final Endpoint<B, R> apiCall,
                   final VelocloudApiClientCredentials credentials,
                   final B body) {
            this.apiCall = Objects.requireNonNull(apiCall);
            this.credentials = Objects.requireNonNull(credentials);
            this.body = Objects.requireNonNull(body);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Key<?, ?> other = (Key<?, ?>) o;
            return this.apiCall == other.apiCall
                   && Objects.equals(this.credentials, other.credentials)
                   && Objects.equals(this.body, other.body);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.apiCall, this.credentials, this.body);
        }
    }

    private final Cache<Object, Object> cache;

    public ApiCache(final long expiringAfterMilliseconds) {
        this.cache = CacheBuilder.newBuilder()
                            .expireAfterWrite(expiringAfterMilliseconds, TimeUnit.MILLISECONDS)
                            .build();
    }

    public Api createApi(final AllApi allApi,
                         final VelocloudApiClientCredentials credentials) {
        return new Api(allApi, credentials);
    }

    public class Api {
        private final AllApi allApi;
        private final VelocloudApiClientCredentials credentials;

        public Api(final AllApi allApi,
                   final VelocloudApiClientCredentials credentials) {
            this.allApi = Objects.requireNonNull(allApi);
            this.credentials = Objects.requireNonNull(credentials);
        }

        @SuppressWarnings("unchecked")
        public <B, R> R call(final String desc,
                             final Endpoint<B, R> endpoint,
                             final B body) throws VelocloudApiException {
            final Key<B, R> key = new Key<>(endpoint, this.credentials, body);

            try {
                return (R) ApiCache.this.cache.get(key, () -> endpoint.doCall(this.allApi, body));
            } catch (final Exception e) {
                throw new VelocloudApiException("Failed to execute API call: " + desc, e);
            }

        }
    }

    public interface Endpoint<B, R> {
        R doCall(final AllApi api, final B body) throws ApiException;
    }

}
