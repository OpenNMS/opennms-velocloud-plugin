/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2023 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2023 The OpenNMS Group, Inc.
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

package org.opennms.velocloud.pollers.path;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.pollers.PollerResult;
import org.opennms.integration.api.v1.pollers.Status;
import org.opennms.integration.api.v1.pollers.immutables.ImmutablePollerResult;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Path;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.pollers.AbstractStatusPoller;

public class PathStatusPoller extends AbstractStatusPoller {
    public static final String ATTR_EDGE_ID = "edgeId";
    public static final String ATTR_DEVICE_LOGICAL_ID = "deviceLogicalId";

    public PathStatusPoller(final ClientManager clientManager) {
        super(clientManager);
    }

    @Override
    protected CompletableFuture<PollerResult> poll(final Context context) throws VelocloudApiException {
        final var edgeId = Integer.valueOf(Objects.requireNonNull(context.getPollerAttributes().get(ATTR_EDGE_ID), "Missing attribute: " + ATTR_EDGE_ID));
        final var deviceLogicalId = Objects.requireNonNull(context.getPollerAttributes().get(ATTR_DEVICE_LOGICAL_ID), "Missing attribute: " + ATTR_DEVICE_LOGICAL_ID);

        final var path = context.customerClient().getPaths(edgeId).stream().filter(p -> Objects.equals(p.deviceLogicalId, deviceLogicalId)).findAny();

        if (path.isEmpty()) {
            return CompletableFuture.completedFuture(ImmutablePollerResult.newBuilder()
                    .setStatus(Status.Down)
                    .setReason("No path with " + ATTR_DEVICE_LOGICAL_ID + " " + deviceLogicalId)
                    .build());
        }

        return CompletableFuture.completedFuture(this.poll(path.get()));
    }

    private PollerResult poll(final Path path) {
        if (path.pathCountStable > 0) {
            return ImmutablePollerResult.newBuilder()
                    .setStatus(Status.Up)
                    .build();
        } else {
            return ImmutablePollerResult.newBuilder()
                    .setStatus(Status.Down)
                    .setReason("Not all paths are STABLE or STANDBY")
                    .build();
        }
    }
}
