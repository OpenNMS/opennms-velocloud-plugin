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

import static java.util.Objects.requireNonNull;

/**
 * This class is a wrapper and is used to return from a function call 2 values.
 * Type parameters are called &lt;API&gt; and &lt;C&gt; for consistency, see extend()
 * @param <API> type of first value
 * @param <C> type of second value
 */
public class ExtendedResult<API, C> {
    final API api;
    final C c;

    public ExtendedResult(API api, C c) {
        this.api = requireNonNull(api);
        this.c = requireNonNull(c);
    }

    public API getApi() {
        return api;
    }

    public C getC() {
        return c;
    }

    /**
     * This method is used to pass API additionally to function result.
     * @param apiCall function which return value should be replaced with pair: <ul>
     *                <li>api(first parameter of the consumed function)</li>
     *                <li>result of this extended function</li>
     * </ul>
     * @return extended result consisting of API and C
     * @param <API> type of the first param of consumed function
     * @param <T> type of the second param of consumed function
     * @param <C> type of the result of consumed function
     * @param <E> type of exception that can be thrown by consumed function
     */
    public static <API, T, C, E extends Exception> ApiCall<API, T, ExtendedResult<API,C>, E> extend(final ApiCall<API, T, C, E> apiCall) {
        return (api, parameter) -> new ExtendedResult<>(api, apiCall.doCall(api, parameter));
    }
}
