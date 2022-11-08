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

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.handler.ApiClient;

import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;


public class ApiCache {

    /**
     * Authentication parameter for ApiKeyAuth used in header parameter value
     *
     * @see org.opennms.velocloud.client.v1.handler.auth.ApiKeyAuth
     */
    public static final String AUTH_HEADER_PREFIX = "Token";

    public static AllApi connectApi(final VelocloudApiClientCredentials credentials) {
        final var client = new ApiClient();
        client.setBasePath(URI.create(credentials.orchestratorUrl).resolve("portal/rest").toString());
        client.setApiKeyPrefix(AUTH_HEADER_PREFIX);
        client.setApiKey(credentials.apiKey);
        return new AllApi(client);
    }

    private static class Key {
        final ApiCall<?, ?> apiCall;
        final VelocloudApiClientCredentials credential;
        final Object parameter;

        public Key(ApiCall<?, ?> apiCall, VelocloudApiClientCredentials credential, Object parameter) {
            this.apiCall = apiCall;
            this.credential = credential;
            this.parameter = parameter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key other = (Key) o;
            return this.apiCall == other.apiCall
                    && this.credential.equals(other.credential)
                    && Objects.equal(parameter, other.parameter);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(apiCall, credential, parameter);
        }
    }

    private final Cache<Object, Object> cache;

    public ApiCache(long expiringAfterMilliseconds) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiringAfterMilliseconds, TimeUnit.MILLISECONDS)
                .build();
    }

    public <K, V> V get(final String desc, final ApiCall<K, V> apiCall, final VelocloudApiClientCredentials credential, final K parameter) throws VelocloudApiException {
        try {
            return (V) cache.get(new Key(apiCall, credential, parameter), () -> {
               final AllApi api = (AllApi) cache.get(credential, () -> connectApi(credential));
               return apiCall.doCall(api, parameter);
            });
        } catch(Exception e) {
            throw new VelocloudApiException("Failed to execute API call: " + desc, e);
        }
    }
}
