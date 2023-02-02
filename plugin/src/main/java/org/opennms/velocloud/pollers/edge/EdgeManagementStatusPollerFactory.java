package org.opennms.velocloud.pollers.edge;

import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.connections.ConnectionManager;

public class EdgeManagementStatusPollerFactory extends AbstractEdgeStatusPoller.Factory<EdgeManagementStatusPoller> {

    public EdgeManagementStatusPollerFactory(final ClientManager clientManager,
                                             final ConnectionManager connectionManager) {
        super(clientManager, connectionManager, EdgeManagementStatusPoller.class);
    }

    @Override
    protected EdgeManagementStatusPoller createPoller(final ClientManager clientManager) {
        return new EdgeManagementStatusPoller(clientManager);
    }
}
