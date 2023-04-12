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
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.handler.ApiClient;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterprise;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseResult;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyProperty;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyPropertyResult;
import org.opennms.velocloud.client.v1.model.Interval;

public class VelocloudApiClientProviderV1 implements VelocloudApiClientProvider {

    public final static ApiCache.Endpoint<EnterpriseProxyGetEnterpriseProxyProperty, EnterpriseProxyGetEnterpriseProxyPropertyResult>
            ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY = AllApi::enterpriseProxyGetEnterpriseProxyProperty;
    public final static ApiCache.Endpoint<EnterpriseGetEnterprise, EnterpriseGetEnterpriseResult>
            ENTERPRISE_GET_ENTERPRISE = AllApi::enterpriseGetEnterprise;

    /**
     * Authentication parameter for ApiKeyAuth used in header parameter value
     *
     * @see org.opennms.velocloud.client.v1.handler.auth.ApiKeyAuth
     */
    public static final String AUTH_HEADER_PREFIX = "Token";
    public static final String PATH = "portal/rest";
    private final int delayInMilliseconds;

    public static Interval getInterval(long intervalMillis, int delayInMilliseconds) {
        // to increase cache usage we align time either to 10s, 5s, 1s, 100ms or 10ms when the interval is big enough
        final long millis = Instant.now().toEpochMilli() - delayInMilliseconds;
        final Instant end;
        if (intervalMillis > 10_000) {
            end = Instant.ofEpochMilli(millis / 10_000 * 10_000);
        } else if (intervalMillis > 5_000) {
            end = Instant.ofEpochMilli(millis / 5_000 * 5_000);
        } else if (intervalMillis > 1_000) {
            end = Instant.ofEpochMilli(millis / 1_000 * 1_000);
        } else if (intervalMillis > 100) {
            end = Instant.ofEpochMilli(millis / 100 * 100);
        } else if (intervalMillis > 10) {
            end = Instant.ofEpochMilli(millis / 10 * 10);
        } else {
            end = Instant.ofEpochMilli(millis);
        }
        return new Interval()
                .end(OffsetDateTime.ofInstant(end, ZoneId.systemDefault()))
                .start(OffsetDateTime.ofInstant(end.minusMillis(intervalMillis),ZoneId.systemDefault()));
    }

    public static AllApi connectApi(final VelocloudApiClientCredentials credentials) {
        final var client = new ApiClient();
        String url = credentials.orchestratorUrl.endsWith("/") ? credentials.orchestratorUrl : credentials.orchestratorUrl + "/";
        client.setBasePath(URI.create(url).resolve(PATH).toString());
        client.setApiKeyPrefix(AUTH_HEADER_PREFIX);
        client.setApiKey(credentials.apiKey);
        return new AllApi(client);
    }

    private final ApiCache executor;

    public VelocloudApiClientProviderV1(ApiCache executor, int delayInMinutes) {
        this.executor = executor;
        if (delayInMinutes <= 0 ) {
            throw new IllegalArgumentException("Delay must be bigger than 0");
        }
        this.delayInMilliseconds = delayInMinutes * 60 * 1000;
    }

    @Override
    public VelocloudApiPartnerClientV1 partnerClient(final VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        final ApiCache.Api api = this.executor.createApi(connectApi(credentials), credentials);

        final var enterpriseProxyId = Optional.ofNullable(api.call("get partner info", ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY,
                                                                   new EnterpriseProxyGetEnterpriseProxyProperty())
                                                             .getEnterpriseProxyId())
                                              .orElseThrow(() -> new VelocloudApiException("Not a partner account"));

        return new VelocloudApiPartnerClientV1(api, enterpriseProxyId, delayInMilliseconds);
    }

    @Override
    public VelocloudApiCustomerClientV1 customerClient(final VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        final ApiCache.Api api = this.executor.createApi(connectApi(credentials), credentials);

        final var enterpriseId = Optional.ofNullable(api.call("get user info", ENTERPRISE_GET_ENTERPRISE,
                                                              new EnterpriseGetEnterprise())
                                                        .getId())
                                         .orElseThrow(() -> new VelocloudApiException("Not a customer account"));
        return new VelocloudApiCustomerClientV1(api, enterpriseId, delayInMilliseconds);
    }
}
