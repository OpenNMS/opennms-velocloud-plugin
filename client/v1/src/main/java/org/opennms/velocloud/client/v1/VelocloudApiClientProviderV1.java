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

import static org.opennms.velocloud.client.v1.FunctionRefsHolder.ENTERPRISE_GET_ENTERPRISE;
import static org.opennms.velocloud.client.v1.FunctionRefsHolder.ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY;

import java.util.Optional;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterprise;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyProperty;

public class VelocloudApiClientProviderV1 implements VelocloudApiClientProvider {

    final ApiCache cache;

    public VelocloudApiClientProviderV1(ApiCache cache) {
        this.cache = cache;
    }

    @Override
    public VelocloudApiPartnerClientV1 partnerClient(final VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        final var enterpriseProxyId = Optional.ofNullable(
                cache.get(
                        "get partner info",
                        ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY,
                        credentials,
                        new EnterpriseProxyGetEnterpriseProxyProperty()
                ).getEnterpriseProxyId()
        ).orElseThrow(() -> new VelocloudApiException("Not a partner account"));

        return new VelocloudApiPartnerClientV1(cache, enterpriseProxyId, credentials);
    }

    @Override
    public VelocloudApiCustomerClientV1 customerClient(final VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        final var enterpriseId = Optional.ofNullable(
                cache.get(
                        "get user info",
                        ENTERPRISE_GET_ENTERPRISE,
                        credentials,
                        new EnterpriseGetEnterprise()
                ).getId()
        ).orElseThrow(() -> new VelocloudApiException("Not a customer account"));
        return new VelocloudApiCustomerClientV1(cache, enterpriseId, credentials);
    }
}
