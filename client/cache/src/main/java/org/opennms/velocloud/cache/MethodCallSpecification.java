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

import org.opennms.velocloud.client.api.VelocloudApiException;

/**
 * Expanded an API call with a converter to the final result
 * @param <I> type of (I)initial parameter
 * @param <C> Typ of the (C)cacheable result of API call
 * @param <R> type of the final (R)result
 */
public class MethodCallSpecification<I, C, R> implements VelocloudFunction<I, R> {
    private final ApiCallExecution<I, C> apiCallExecution;
    private final ResultAdapter<C, R> converter;

    public MethodCallSpecification(ApiCallExecution<I, C> apiCallExecution, ResultAdapter<C, R> converter) {
        this.apiCallExecution = apiCallExecution;
        this.converter = converter;
    }

    @Override
    public R apply(I initialParameter) throws VelocloudApiException {
        final C apiCallResult = apiCallExecution.doApiCall(initialParameter);
        return converter.apply(apiCallResult);
    }
}
