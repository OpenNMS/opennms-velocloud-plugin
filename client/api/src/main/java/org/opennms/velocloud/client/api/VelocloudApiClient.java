package org.opennms.velocloud.client.api;

import java.util.List;
import java.util.UUID;

import org.opennms.velocloud.client.api.model.Edge;

public interface VelocloudApiClient {

    List<Edge> getEdges(final UUID enterpriseId) throws VelocloudApiException;

}
