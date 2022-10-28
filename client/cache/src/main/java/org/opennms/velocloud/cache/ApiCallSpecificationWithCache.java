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

import org.opennms.velocloud.client.api.VelocloudApiException;

/**
 * This class is used specify a Cacheable API call by providing its functional parts: <ul>
 * <li>prepare: prepares cacheable call</li>
 * <li>apiCall: cacheable call</li>
 * </ul>
 *
 * @param <I> type of (I)initial parameter
 * @param <A> (A)API Class to call its method
 * @param <P> type of (P)parameter for API method: A.method(P param)
 * @param <C> Typ of the (C)cacheable result of API call
 * @param <E> type of the (E)exception that can be thrown in A.apiCall(P)
 */
public class ApiCallSpecificationWithCache<I, A, P, C, E extends Exception> implements ApiCallExecution<I, C> {

    private final Function<I, ParamsForApiCall<A, P>> prepare;
    private final ApiCall<A, P, C, E> apiCall;
    private final String desc;

    /**
     * Creates a specification for an API call
     * @param prepare preparation for API call: providing API, parameter and the key for identifying values in cache
     * @param apiCall function to call or get cashed value of
     * @param desc description
     */
    public ApiCallSpecificationWithCache(
            final Function<I, ParamsForApiCall<A, P>> prepare,
            final ApiCall<A, P, C, E> apiCall,
            String desc
    ) {
        this.prepare = prepare;
        this.apiCall = apiCall;
        this.desc = desc;
        this.cache = CacheFactory.getCache(this.apiCall);
    }

    private final Cache<A, P, C, E> cache;

    public Function<I, ParamsForApiCall<A, P>> getPrepare() {
        return prepare;
    }

    public ApiCall<A, P, C, E> getApiCall() {
        return apiCall;
    }

    public Cache<A, P, C, E> getCache() {
        return cache;
    }

    @Override
    public C doApiCall(I initialParameter) throws VelocloudApiException {
        final ParamsForApiCall<A, P> params = this.prepare.apply(initialParameter);
        try {
            return cache.doCall(params.api, params.apiCallParameter, params.key);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new VelocloudApiException("Failed to execute API call: " + this.desc, e);
        }
    }
}
