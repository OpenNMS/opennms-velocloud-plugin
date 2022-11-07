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

import static org.opennms.velocloud.client.v1.ApiCallV1.cachedCall;
import static org.opennms.velocloud.client.v1.FunctionRefsHolder.ENTERPRISE_GET_ENTERPRISE;
import static org.opennms.velocloud.client.v1.FunctionRefsHolder.ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY;

import java.net.URI;
import java.util.Optional;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.cache.Cache;
import org.opennms.velocloud.client.cache.CacheFactory;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.handler.ApiClient;
import org.opennms.velocloud.client.v1.handler.ApiException;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterprise;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseResult;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyProperty;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyPropertyResult;

public class VelocloudApiClientProviderV1 implements VelocloudApiClientProvider {

    /**
     * Authentication parameter for ApiKeyAuth used in header parameter value
     *
     * @see org.opennms.velocloud.client.v1.handler.auth.ApiKeyAuth
     */
    public static final String AUTH_HEADER_PREFIX = "Token";

    static AllApi connectApi(final VelocloudApiClientCredentials credentials) {
        final var client = new ApiClient();
        client.setBasePath(URI.create(credentials.orchestratorUrl).resolve("portal/rest").toString());
        client.setApiKeyPrefix(AUTH_HEADER_PREFIX);
        client.setApiKey(credentials.apiKey);
        return new AllApi(client);
    }

    final Cache<AllApi, EnterpriseProxyGetEnterpriseProxyProperty, EnterpriseProxyGetEnterpriseProxyPropertyResult, ApiException> cachePartnerClient;
    final Cache<AllApi, EnterpriseGetEnterprise, EnterpriseGetEnterpriseResult, ApiException> cashCustomerClient;

    public VelocloudApiClientProviderV1() {
        cachePartnerClient = CacheFactory.getCache(ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY);
        cashCustomerClient = CacheFactory.getCache(ENTERPRISE_GET_ENTERPRISE);
    }

    @Override
    public VelocloudApiPartnerClientV1 partnerClient(final VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        final var api = connectApi(credentials);

        final var enterpriseProxyId = Optional.ofNullable(
                cachedCall(cachePartnerClient, api, "get partner info", new EnterpriseProxyGetEnterpriseProxyProperty(), credentials)
                        .getEnterpriseProxyId()
        ).orElseThrow(() -> new VelocloudApiException("Not a partner account"));

        return new VelocloudApiPartnerClientV1(api, enterpriseProxyId, credentials);
    }

    @Override
    public VelocloudApiCustomerClientV1 customerClient(final VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        final var api = connectApi(credentials);

        final var enterpriseId = Optional.ofNullable(
                cachedCall(cashCustomerClient, api, "get user info",new EnterpriseGetEnterprise(), credentials).getId()
        ).orElseThrow(() -> new VelocloudApiException("Not a customer account"));
        return new VelocloudApiCustomerClientV1(api, enterpriseId, credentials);
    }
}
