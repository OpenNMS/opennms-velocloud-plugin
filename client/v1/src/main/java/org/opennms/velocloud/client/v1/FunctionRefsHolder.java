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

import static org.opennms.velocloud.client.cache.ExtendedResult.extend;

import java.util.List;

import org.opennms.velocloud.client.cache.ApiCall;
import org.opennms.velocloud.client.cache.ExtendedResult;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.handler.ApiException;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterprise;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdges;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdgesResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseResult;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseUsers;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseUsersResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyEnterprises;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyEnterprisesResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyGateways;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyGatewaysResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyProperty;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyPropertyResult;

/**
 * Used to store all functions that are used in caches
 * Only by providing the same object into the CacheFactory the same cache can be reused
 */
public class FunctionRefsHolder {
    public final static ApiCall<AllApi, EnterpriseProxyGetEnterpriseProxyProperty, ExtendedResult<AllApi, EnterpriseProxyGetEnterpriseProxyPropertyResult>, ApiException>
            EXTENDED_ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY = extend(AllApi::enterpriseProxyGetEnterpriseProxyProperty);

    public final static ApiCall<AllApi, EnterpriseGetEnterprise, ExtendedResult<AllApi, EnterpriseGetEnterpriseResult>, ApiException>
            EXTENDED_ENTERPRISE_GET_ENTERPRISE = extend(AllApi::enterpriseGetEnterprise);

    public final static ApiCall<AllApi, EnterpriseGetEnterpriseEdges, List<EnterpriseGetEnterpriseEdgesResultItem>, ApiException>
            ENTERPRISE_GET_ENTERPRISE_EDGES = AllApi::enterpriseGetEnterpriseEdges;

    public final static ApiCall<AllApi, EnterpriseGetEnterpriseUsers, List<EnterpriseGetEnterpriseUsersResultItem>, ApiException>
            ENTERPRISE_GET_ENTERPRISE_USERS = AllApi::enterpriseGetEnterpriseUsers;

    public final static ApiCall<AllApi, EnterpriseProxyGetEnterpriseProxyGateways, List<EnterpriseProxyGetEnterpriseProxyGatewaysResultItem>, ApiException>
            ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_GATEWAYS = AllApi::enterpriseProxyGetEnterpriseProxyGateways;

    public final static ApiCall<AllApi, EnterpriseProxyGetEnterpriseProxyEnterprises, List<EnterpriseProxyGetEnterpriseProxyEnterprisesResultItem>, ApiException>
            ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_ENTERPRISES = AllApi::enterpriseProxyGetEnterpriseProxyEnterprises;
}
