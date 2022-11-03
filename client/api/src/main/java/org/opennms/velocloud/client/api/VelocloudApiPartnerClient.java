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
package org.opennms.velocloud.client.api;

import java.time.Instant;
import java.util.List;

import org.opennms.velocloud.client.api.model.Customer;
import org.opennms.velocloud.client.api.model.Gateway;
import org.opennms.velocloud.client.api.model.PartnerEvent;

/**
 * A client for the velocloud API authenticated as a partner.
 */
public interface VelocloudApiPartnerClient {

    /** Derive a customer connection from this partner connection.
     *
     * The created connection uses the same credential as this connection. The specified enterprise ID will be used for
     * every request done by the created customer connection.
     *
     * @param enterpriseId the enterprise ID of the customer
     * @return the customer connection
     */
    VelocloudApiCustomerClient getCustomerClient(final Integer enterpriseId);

    /**
     * Get all gateways of the partner.
     * @return list of {@link Gateway}s
     * @throws VelocloudApiException
     */
    List<Gateway> getGateways() throws VelocloudApiException;

    /**
     * Get all customers of the partner.
     * @return list of {@link Customer}s
     * @throws VelocloudApiException
     */
    List<Customer> getCustomers() throws VelocloudApiException;

    List<PartnerEvent> getEvents(Instant start, Instant end) throws VelocloudApiException;
}
