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
        if (path.pathCountStable + path.pathCountStandBy == path.pathCountTotal) {
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
