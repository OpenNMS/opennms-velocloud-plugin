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

package org.opennms.velocloud.cache.clieant;

import java.net.URI;
import java.util.function.Function;

import org.opennms.velocloud.cache.MethodCallSpecification;
import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.client.v1.handler.ApiException;

public class CachedVelocloudApiClientProvider implements VelocloudApiClientProvider {

    final MethodCallSpecification<?, ?, ?, VelocloudApiPartnerClient, ?> partnerClientSpecification;
    final MethodCallSpecification<?, ?, ?, VelocloudApiCustomerClient, ?> customerClientSpecification;


    public CachedVelocloudApiClientProvider(
            MethodCallSpecification<?, ?, ?, VelocloudApiPartnerClient, ?> partnerClientSpecification,
            MethodCallSpecification<?, ?, ?, VelocloudApiCustomerClient, ?> customerClientSpecification

    ) {
        this.partnerClientSpecification = partnerClientSpecification;
        this.customerClientSpecification = customerClientSpecification;
    }

    @Override
    public VelocloudApiPartnerClient partnerClient(VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        return partnerClientSpecification.doCall(credentials);
    }

    @Override
    public VelocloudApiCustomerClient customerClient(VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        return customerClientSpecification.doCall(credentials);
    }
}
