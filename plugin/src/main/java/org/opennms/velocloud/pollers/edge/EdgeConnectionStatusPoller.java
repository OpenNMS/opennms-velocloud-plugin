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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.opennms.integration.api.v1.pollers.PollerResult;
import org.opennms.integration.api.v1.pollers.Status;
import org.opennms.integration.api.v1.pollers.immutables.ImmutablePollerResult;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.clients.ClientManager;

import com.google.common.base.Strings;

public class EdgeConnectionStatusPoller extends AbstractEdgeStatusPoller {
    public EdgeConnectionStatusPoller(ClientManager clientManager) {
        super(clientManager);
    }

    protected PollerResult poll(final Context context, final Edge edge) throws VelocloudApiException {
        if (!Objects.equals(edge.edgeState, "CONNECTED")) {
            final Optional<Integer> superGatewayId = context.customerClient().getSuperGateway(edge.edgeId);

            if (superGatewayId.isEmpty()) {
                return ImmutablePollerResult.newBuilder()
                        .setStatus(Status.Down)
                        .setReason("Edge is down (no super gateway found)")
                        .build();
            }

            final VelocloudApiPartnerClient partnerClient = context.partnerClient();
            final List<Edge> edgesAssigned = partnerClient.getEdgeAssignments(superGatewayId.get());

            final Optional<Edge> edgeAssigned = edgesAssigned.stream()
                    .filter(e -> Objects.equals(e.edgeId, edge.edgeId))
                    .findAny();

            if (edgeAssigned.isEmpty()) {
                return ImmutablePollerResult.newBuilder()
                        .setStatus(Status.Down)
                        .setReason("Edge is down (edge not assigned on super gateway)")
                        .build();
            }

            if (!Objects.equals(edgeAssigned.get().edgeState, "CONNECTED")) {
                return ImmutablePollerResult.newBuilder()
                        .setStatus(Status.Down)
                        .setReason("Edge is down (verified by false-positive check)")
                        .build();
            }
        }

        return ImmutablePollerResult.newBuilder()
                .setStatus(Status.Up)
                .build();
    }
}
