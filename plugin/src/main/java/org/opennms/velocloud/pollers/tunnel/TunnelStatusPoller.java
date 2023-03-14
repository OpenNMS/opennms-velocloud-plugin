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

package org.opennms.velocloud.pollers.tunnel;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.pollers.PollerResult;
import org.opennms.integration.api.v1.pollers.Status;
import org.opennms.integration.api.v1.pollers.immutables.ImmutablePollerResult;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.client.api.model.Tunnel;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.pollers.AbstractStatusPoller;
import org.opennms.velocloud.pollers.edge.AbstractEdgeStatusPoller;

public class TunnelStatusPoller extends AbstractEdgeStatusPoller {
    private final String ATTR_NAME = "name";
    private final String ATTR_ROLE = "role";
    private final String ATTR_DATAKEY = "dataKey";

    public TunnelStatusPoller(final ClientManager clientManager) {
        super(clientManager);
    }

    @Override
    protected PollerResult poll(final Context context, final Edge edge) throws VelocloudApiException {
        final var name = Objects.requireNonNull(context.getPollerAttributes().get(ATTR_NAME),
                "Missing attribute: " + ATTR_NAME);
        final var role = Objects.requireNonNull(context.getPollerAttributes().get(ATTR_ROLE),
                "Missing attribute: " + ATTR_ROLE);
        final var dataKey = Objects.requireNonNull(context.getPollerAttributes().get(ATTR_DATAKEY),
                "Missing attribute: " + ATTR_DATAKEY);

        final Optional<Tunnel> tunnel = context.customerClient().getTunnelState(dataKey);

        if (tunnel.isEmpty()) {
            return ImmutablePollerResult.newBuilder()
                    .setStatus(Status.Down)
                    .setReason("No tunnel with name " + name)
                    .build();
        }

        if (!Objects.equals(tunnel.get().state, "UP")) {
            return ImmutablePollerResult.newBuilder()
                    .setStatus(Status.Down)
                    .setReason("Tunnel not up")
                    .build();
        }

        return ImmutablePollerResult.newBuilder()
                .setStatus(Status.Up)
                .build();
    }
}
