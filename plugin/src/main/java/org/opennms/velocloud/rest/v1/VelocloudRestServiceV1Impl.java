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
package org.opennms.velocloud.rest.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Customer;
import org.opennms.velocloud.connections.ConnectionManager;
import org.opennms.velocloud.connections.ConnectionValidationError;
import org.opennms.velocloud.rest.api.VelocloudRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

public class VelocloudRestServiceV1Impl implements VelocloudRestService {

    private static final Logger LOG = LoggerFactory.getLogger(VelocloudRestServiceV1Impl.class);

    private final ConnectionManager connectionManager;

    public VelocloudRestServiceV1Impl(final ConnectionManager connectionManager) {
        this.connectionManager = Objects.requireNonNull(connectionManager);
    }

    @Override
    public Response getCustomersForMspPartner(final String alias) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            final var client = this.connectionManager.getPartnerClient(alias)
                                  .orElseThrow(null);
            List<Customer> enterprises = client.getCustomers();
            String json = mapper.writeValueAsString(enterprises);
            return Response.ok().entity(json).build();
        } catch (VelocloudApiException | ConnectionValidationError e) {
            LOG.error("An error occurred trying to retrieve customers for MSP Partner" , e.getCause());
            return Response.serverError().build();
        } catch (JsonProcessingException e) {
            LOG.error("An error occurred trying to parse json" , e.getCause());
            return Response.serverError().build();
        }

    }


}
