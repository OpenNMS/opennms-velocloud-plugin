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

import java.util.function.Supplier;

/**
 * Extends an API call with a converter to the final result
 * @param <C> Typ of the (C)cacheable result of API call
 * @param <R> type of the final (R)result
 */
public class SupplierSpecificationWithCache<A, P, C, R, E extends Exception>
        extends SupplierSpecification<C, R> {

    public SupplierSpecificationWithCache(
            final Supplier<ParamsForApiCall<A, P>> prepare,
            final ApiCall<A, P, C, E> apiCall,
            String desc,
            final ResultAdapter<C, R> adapter
    ) {
        super(new ApiGetSpecificationWithCache<>(prepare, apiCall, desc), adapter);
    }
}
