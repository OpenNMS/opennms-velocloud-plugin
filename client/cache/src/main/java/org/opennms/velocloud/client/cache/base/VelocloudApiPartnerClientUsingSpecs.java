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
package org.opennms.velocloud.client.cache.base;

import java.util.List;
import java.util.function.Function;

import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.client.api.model.Customer;
import org.opennms.velocloud.client.api.model.Gateway;
import org.opennms.velocloud.client.cache.VelocloudSupplier;

public class VelocloudApiPartnerClientUsingSpecs implements VelocloudApiPartnerClient {

    private final Function<Integer, VelocloudApiCustomerClient> getCustomerClientSpecification;
    private final VelocloudSupplier<List<Gateway>> getGatewaysSpecification;
    private final VelocloudSupplier<List<Customer>> getCustomersSpecification;

    public VelocloudApiPartnerClientUsingSpecs(
            Function<Integer, VelocloudApiCustomerClient> getCustomerClientSpecification,
            VelocloudSupplier<List<Gateway>> getGatewaysSpecification,
            VelocloudSupplier<List<Customer>> getCustomersSpecification
    ) {
        this.getCustomerClientSpecification = getCustomerClientSpecification;
        this.getGatewaysSpecification = getGatewaysSpecification;
        this.getCustomersSpecification = getCustomersSpecification;
    }

    @Override
    public VelocloudApiCustomerClient getCustomerClient(final Integer enterpriseId) {
        return getCustomerClientSpecification.apply(enterpriseId);
    }

    @Override
    public List<Gateway> getGateways() throws VelocloudApiException {
        return getGatewaysSpecification.get();
    }

    @Override
    public List<Customer> getCustomers() throws VelocloudApiException {
        return getCustomersSpecification.get();
    }
}
