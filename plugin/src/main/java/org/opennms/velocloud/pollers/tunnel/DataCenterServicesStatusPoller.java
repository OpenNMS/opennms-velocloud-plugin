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

package org.opennms.velocloud.pollers.tunnel;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.opennms.integration.api.v1.pollers.PollerResult;
import org.opennms.integration.api.v1.pollers.Status;
import org.opennms.integration.api.v1.pollers.immutables.ImmutablePollerResult;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Datacenter;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.pollers.AbstractStatusPoller;

public class DataCenterServicesStatusPoller extends AbstractStatusPoller {
    private final String ATTR_LOGICAL_ID = "logicalId";
    private final String ATTR_TYPE = "type";
    public DataCenterServicesStatusPoller(ClientManager clientManager) {
        super(clientManager);
    }

    @Override
    protected CompletableFuture<PollerResult> poll(final Context context) throws VelocloudApiException {
        final String logicalId = Objects.requireNonNull(context.getPollerAttributes().get(ATTR_LOGICAL_ID),
                "Missing attribute: " + ATTR_LOGICAL_ID);
        final String type = Objects.requireNonNull(context.getPollerAttributes().get(ATTR_TYPE),
                "Missing attribute: " + ATTR_TYPE);

        final Optional<Datacenter> datacenter = context.customerClient().getDatacenters().stream()
                .filter(d -> logicalId.equals(d.logicalId))
                .findAny();

        if (datacenter.isEmpty()) {
            return CompletableFuture.completedFuture(ImmutablePollerResult.newBuilder()
                    .setStatus(Status.Down)
                    .setReason("No data center with logicalId " + logicalId)
                    .build());
        }

        if (type.equals("primary")) {
            if ("STABLE".equals(datacenter.get().primaryState)) {
                return CompletableFuture.completedFuture(ImmutablePollerResult.newBuilder()
                        .setStatus(Status.Up)
                        .build());
            }
        } else {
            if ("STABLE".equals(datacenter.get().secondaryState)) {
                return CompletableFuture.completedFuture(ImmutablePollerResult.newBuilder()
                        .setStatus(Status.Up)
                        .build());
            }
        }

        return CompletableFuture.completedFuture(ImmutablePollerResult.newBuilder()
                .setStatus(Status.Down)
                .setReason("Data center state not STABLE")
                .build());
    }
}
