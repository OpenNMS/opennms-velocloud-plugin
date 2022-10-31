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

import static org.opennms.velocloud.client.v1.FunctionRefsHolder.EXTENDED_ENTERPRISE_GET_ENTERPRISE;
import static org.opennms.velocloud.client.v1.FunctionRefsHolder.EXTENDED_ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY;

import java.net.URI;
import java.util.Optional;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.cache.FunctionSpecificationWithCache;
import org.opennms.velocloud.client.cache.ParamsForApiCall;
import org.opennms.velocloud.client.cache.base.VelocloudApiClientProviderUsingSpecs;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.handler.ApiClient;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterprise;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyProperty;

public class VelocloudApiClientProviderV1 extends VelocloudApiClientProviderUsingSpecs {

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

    public VelocloudApiClientProviderV1() {
        super(
                new FunctionSpecificationWithCache<>(
                        credentials ->
                            new ParamsForApiCall<>(
                                    connectApi(credentials),
                                    new EnterpriseProxyGetEnterpriseProxyProperty(),
                                    credentials
                            ),
                        EXTENDED_ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY,
                        "get partner info",
                        (extended) -> {
                            final Integer enterpriseProxyId = Optional.ofNullable(extended.getC().getEnterpriseProxyId())
                                    .orElseThrow(() -> new VelocloudApiException("Not a partner account"));
                            return new VelocloudApiPartnerClientV1(extended.getApi(), enterpriseProxyId);
                        }
                ),
                new FunctionSpecificationWithCache<>(
                        credentials ->
                                new ParamsForApiCall<>(
                                        connectApi(credentials),
                                        new EnterpriseGetEnterprise(),
                                        credentials
                                ),
                        EXTENDED_ENTERPRISE_GET_ENTERPRISE,
                        "get user info",
                        (extended) -> {
                            final Integer enterpriseId = Optional.ofNullable(extended.getC().getId())
                                    .orElseThrow(() -> new VelocloudApiException("Not a customer account"));
                            return new VelocloudApiCustomerClientV1(extended.getApi(), enterpriseId);
                        }
                )
        );
    }
}
