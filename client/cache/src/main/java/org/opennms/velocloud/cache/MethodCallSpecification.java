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
 * MethodCallSpecification adds a final result adapter to ApiCallSpecification what allows to use same cache for same
 * API calls, but calculate different results.
 *
 * @param <T> Parameter type that is required for API call
 * @param <API> API Class
 * @param <C> Typ of the result of API call
 * @param <R> Final result type
 */
public class MethodCallSpecification<API, T, C, R, E extends Exception> extends ApiCallSpecification <API, T, C, E> {

    private final ParameterSupplier<T> parameterProvider;
    private final ResultAdapter<C, R> adapter;

    public MethodCallSpecification(
            final ParameterSupplier<T> parameterProvider,
            final Function<VelocloudApiClientCredentials, API> apiProvider,
            final ApiCall<API, T, C, E> apiCall,
            final ResultAdapter<C, R> adapter,
            final String desc
    ) {
        super(apiProvider, apiCall, desc);
        this.parameterProvider = parameterProvider;
        this.adapter = adapter;
    }

    public ParameterSupplier<T> getParameterProvider() {
        return parameterProvider;
    }

    public ResultAdapter<C, R> getAdapter() {
        return adapter;
    }

    public R doCall(VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        final T parameter = parameterProvider.get();
        final C apiCallResult = getCache().doCall(credentials, parameter);
        return adapter.convert(apiCallResult);
    }

    public R doCall() {
        //TODO
        return null;
    }
}
