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
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.pollers.PollerResult;
import org.opennms.integration.api.v1.pollers.Status;
import org.opennms.integration.api.v1.pollers.immutables.ImmutablePollerResult;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Tunnel;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.pollers.AbstractStatusPoller;

public class TunnelStatusPoller extends AbstractStatusPoller {
    public static final String ATTR_DATA_KEY = "dataKey";

    public TunnelStatusPoller(final ClientManager clientManager) {
        super(clientManager);
    }

    @Override
    protected CompletableFuture<PollerResult> poll(final Context context) throws VelocloudApiException {
        final var dataKey = Objects.requireNonNull(context.getPollerAttributes().get(ATTR_DATA_KEY),
                                                   "Missing attribute: " + ATTR_DATA_KEY);

        final var tunnel = context.customerClient().getNvsTunnels().stream()
                                  .filter(t -> Objects.equals(t.dataKey, dataKey))
                                  .findAny();

        if (tunnel.isEmpty()) {
            return CompletableFuture.completedFuture(ImmutablePollerResult.newBuilder()
                                                                          .setStatus(Status.Down)
                                                                          .setReason("No tunnel with dataKey " + dataKey)
                                                                          .build());
        }

        return CompletableFuture.completedFuture(this.poll(tunnel.get()));
    }

    private PollerResult poll(final Tunnel tunnel) {
        if (!Objects.equals(tunnel.state, "UP")) {
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
