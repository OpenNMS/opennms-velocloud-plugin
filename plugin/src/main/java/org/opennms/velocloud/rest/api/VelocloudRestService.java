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
package org.opennms.velocloud.rest.api;

import org.opennms.velocloud.client.api.VelocloudApiException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/velocloud/api")
public interface VelocloudRestService {

    @GET
    @Path("/partner/connections")
    @Produces(value = {MediaType.APPLICATION_JSON})
    Response getMspPartnerConnections(@QueryParam("alias") String alias) throws VelocloudApiException;

    @GET
    @Path("/partner/customers/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    Response getCustomersForMspPartner(@QueryParam("alias") String alias) throws VelocloudApiException;

    @GET
    @Path("/status")
    @Produces(value = {MediaType.APPLICATION_JSON})
    Response getStatus();
}
