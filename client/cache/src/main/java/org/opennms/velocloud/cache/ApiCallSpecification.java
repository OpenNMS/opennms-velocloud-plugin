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

package org.opennms.velocloud.cache;

import java.util.function.Function;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiException;

/**
 * This class is used specify a Cacheable API call by providing its functional parts
 *
 * @param <T> Type of the parameter that is required for API call
 * @param <API> API Class
 * @param <C> Typ of the result of API call
 */
public class ApiCallSpecification<API, T, C, E extends Exception> {

    private final Function<VelocloudApiClientCredentials, API> apiProvider;
    private final ApiCall<API, T, C, E> apiCall;
    private final CacheableFunction<T, C> cacheableFunction;
    private final String desc;
    private final Cache<T, C> cache;

    public ApiCallSpecification(
            final Function<VelocloudApiClientCredentials, API> apiProvider,
            final ApiCall<API, T, C, E> apiCall,
            final String desc
    ) {
        this.apiProvider = apiProvider;
        this.apiCall = apiCall;
        this.desc = desc;

        //cacheableFunction is precalculated to ensure that cacheFactory returns the same Cache after comparing
        //results of different calls of
        this.cacheableFunction = (credentials, t) -> {
            final API api = this.apiProvider.apply(credentials);
            try {
                return this.apiCall.doCall(api, t);
            } catch (Exception e) {
                throw new VelocloudApiException("Failed to execute API call: " + this.desc, e);
            }
        };

        this.cache = CacheFactory.getCache(this.cacheableFunction);
    }

    public CacheableFunction<T, C> getCacheableFunction() {
        return cacheableFunction;
    }

    public Function<VelocloudApiClientCredentials, API> getApiProvider() {
        return apiProvider;
    }

    public ApiCall<API, T, C, E> getApiCall() {
        return apiCall;
    }

    public String getDesc() {
        return desc;
    }

    public Cache<T, C> getCache() {
        return cache;
    }
}
