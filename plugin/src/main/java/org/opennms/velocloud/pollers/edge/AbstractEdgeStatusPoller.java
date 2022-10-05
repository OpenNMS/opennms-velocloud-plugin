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

package org.opennms.velocloud.pollers.edge;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.pollers.PollerResult;
import org.opennms.integration.api.v1.pollers.Status;
import org.opennms.integration.api.v1.pollers.immutables.ImmutablePollerResult;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.connections.ConnectionManager;
import org.opennms.velocloud.pollers.AbstractStatusPoller;

public abstract class AbstractEdgeStatusPoller extends AbstractStatusPoller {
    public static final String ATTR_EDGE_ID = "edgeId";

    protected AbstractEdgeStatusPoller(final ClientManager clientManager) {
        super(clientManager);
    }

    protected abstract PollerResult poll(final Edge edge) throws VelocloudApiException;

    @Override
    public CompletableFuture<PollerResult> poll(final Context context) throws VelocloudApiException {
        final var edgeId = Objects.requireNonNull(context.getPollerAttributes().get(ATTR_EDGE_ID),
                                                  "Missing attribute: " + ATTR_EDGE_ID);

        final var edge = context.customerClient().getEdges().stream()
                                .filter(e -> Objects.equals(e.logicalId, edgeId))
                                .findAny();

        if (edge.isEmpty()) {
            return CompletableFuture.completedFuture(ImmutablePollerResult.newBuilder()
                                                                          .setStatus(Status.Down)
                                                                          .setReason("No edge with id " + edgeId)
                                                                          .build());
        }

        return CompletableFuture.completedFuture(this.poll(edge.get()));
    }

    public static abstract class Factory<T extends AbstractEdgeStatusPoller> extends AbstractStatusPoller.Factory<T> {

        protected Factory(final ClientManager clientManager,
                          final ConnectionManager connectionManager,
                          final Class<T> clazz) {
            super(clientManager, connectionManager, clazz);
        }
    }
}
