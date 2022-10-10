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
import java.util.Optional;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.handler.ApiClient;
import org.opennms.velocloud.client.v1.handler.ApiException;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterprise;
import org.opennms.velocloud.client.v1.model.EnterpriseUserGetEnterpriseUser;

public class VelocloudApiClientProviderV1 implements VelocloudApiClientProvider {

    /**
     * Authentication parameter for ApiKeyAuth used in header parameter value
     *
     * @see org.opennms.velocloud.client.v1.handler.auth.ApiKeyAuth
     */
    public static final String AUTH_HEADER_PREFIX = "Token";

    private AllApi connectApi(final VelocloudApiClientCredentials credentials) {
        final var client = new ApiClient();
        client.setBasePath(URI.create(credentials.orchestratorUrl).resolve("portal/rest").toString());
        client.setApiKeyPrefix(AUTH_HEADER_PREFIX);
        client.setApiKey(credentials.apiKey);

        return new AllApi(client);
    }

    @Override
    public VelocloudApiPartnerClientV1 partnerClient(final VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        final var client = connectApi(credentials);
        return new VelocloudApiPartnerClientV1(client);
    }

    @Override
    public VelocloudApiCustomerClientV1 customerClient(final VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        final var api = connectApi(credentials);

        final var enterpriseId = Optional.ofNullable(ApiCall.call(api, "get user info",
                                                                  AllApi::enterpriseGetEnterprise,
                                                                  new EnterpriseGetEnterprise())
                                                            .getId())
                                         .orElseThrow(() -> new VelocloudApiException("Not a customer account"));

        return new VelocloudApiCustomerClientV1(api, enterpriseId);
    }

    @FunctionalInterface
    public interface ApiCall<B, R> {
        R apply(final AllApi api, final B body) throws ApiException;

        static <B, R> R call(final AllApi api,
                             final String desc,
                             final ApiCall<B, R> f,
                             final B body) throws VelocloudApiException {
            try {
                return f.apply(api, body);
            } catch (final ApiException e) {
                throw new VelocloudApiException("Failed to execute API call: " + desc, e);
            }
        }
    }
}
