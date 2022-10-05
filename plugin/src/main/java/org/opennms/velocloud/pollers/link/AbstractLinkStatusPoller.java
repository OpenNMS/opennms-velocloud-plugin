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

package org.opennms.velocloud.pollers.link;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.pollers.PollerResult;
import org.opennms.integration.api.v1.pollers.Status;
import org.opennms.integration.api.v1.pollers.immutables.ImmutablePollerResult;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Link;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.connections.ConnectionManager;
import org.opennms.velocloud.pollers.AbstractStatusPoller;

public abstract class AbstractLinkStatusPoller extends AbstractStatusPoller {
    public static final String ATTR_EDGE_ID = "edgeId";
    public static final String ATTR_LINK_ID = "linkId";

    protected AbstractLinkStatusPoller(final ClientManager clientManager) {
        super(clientManager);
    }

    protected abstract PollerResult poll(final Link link) throws VelocloudApiException;

    @Override
    public CompletableFuture<PollerResult> poll(final Context context) throws VelocloudApiException {
        final var edgeId = Objects.requireNonNull(context.getPollerAttributes().get(ATTR_EDGE_ID),
                                                  "Missing attribute: " + ATTR_EDGE_ID);
        final var linkId = Objects.requireNonNull(context.getPollerAttributes().get(ATTR_LINK_ID),
                                                  "Missing attribute: " + ATTR_LINK_ID);

        final var link = context.customerClient().getEdges().stream()
                                .filter(e -> Objects.equals(e.logicalId, edgeId))
                                .flatMap(e -> e.links.stream())
                                .filter(l -> Objects.equals(l.logicalId, linkId))
                                .findAny();

        if (link.isEmpty()) {
            return CompletableFuture.completedFuture(ImmutablePollerResult.newBuilder()
                                                                          .setStatus(Status.Down)
                                                                          .setReason("No link with id " + edgeId + "/" + linkId)
                                                                          .build());
        }

        return CompletableFuture.completedFuture(this.poll(link.get()));
    }

    public static abstract class Factory<T extends AbstractLinkStatusPoller> extends AbstractStatusPoller.Factory<T> {

        protected Factory(final ClientManager clientManager,
                          final ConnectionManager connectionManager,
                          final Class<T> clazz) {
            super(clientManager, connectionManager, clazz);
        }
    }
}
