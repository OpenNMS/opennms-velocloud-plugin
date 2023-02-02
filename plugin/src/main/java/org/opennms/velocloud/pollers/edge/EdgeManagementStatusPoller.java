package org.opennms.velocloud.pollers.edge;

import java.util.Objects;

import org.opennms.integration.api.v1.pollers.PollerResult;
import org.opennms.integration.api.v1.pollers.Status;
import org.opennms.integration.api.v1.pollers.immutables.ImmutablePollerResult;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.clients.ClientManager;

public class EdgeManagementStatusPoller extends AbstractEdgeStatusPoller {
    public EdgeManagementStatusPoller(ClientManager clientManager) {
        super(clientManager);
    }

    protected PollerResult poll(final Context context, final Edge edge) throws VelocloudApiException {
        if (!Objects.equals(edge.edgeState, "CONNECTED")) {
            return ImmutablePollerResult.newBuilder()
                    .setStatus(Status.Down)
                    .setReason("Edge monitoring down")
                    .build();
        }

        return ImmutablePollerResult.newBuilder()
                .addProperty("links", edge.links.size())
                .setStatus(Status.Up)
                .build();
    }
}
