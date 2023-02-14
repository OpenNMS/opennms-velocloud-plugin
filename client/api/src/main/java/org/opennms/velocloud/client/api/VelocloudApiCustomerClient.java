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
import java.util.Optional;

import org.opennms.velocloud.client.api.model.Datacenter;
import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.client.api.model.CustomerEvent;
import org.opennms.velocloud.client.api.model.Path;
import org.opennms.velocloud.client.api.model.Tunnel;
import org.opennms.velocloud.client.api.model.User;

/**
 * A client for the velocloud API authenticated as a customer.
 */
public interface VelocloudApiCustomerClient {

    /**
     * Get the edges of the customer.
     * @return a list of {@link Edge}s
     * @throws VelocloudApiException
     */
    List<Edge> getEdges() throws VelocloudApiException;

    List<Path> getPaths(int edgeId) throws VelocloudApiException;

    List<User> getUsers() throws VelocloudApiException;

    List<CustomerEvent> getEvents(Instant start, Instant end) throws VelocloudApiException;

    List<Tunnel> getNvsTunnels() throws VelocloudApiException;

    List<Tunnel> getNvsTunnels(String tag) throws VelocloudApiException;

    List<Datacenter> getDatacenters() throws VelocloudApiException;

    Optional<Integer> getSuperGateway(final int edgeId) throws VelocloudApiException;
}
