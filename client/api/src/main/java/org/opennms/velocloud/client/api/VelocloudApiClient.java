package org.opennms.velocloud.client.api;

import java.util.List;
import java.util.UUID;

import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.client.api.model.Enterprise;
import org.opennms.velocloud.client.api.model.Gateway;
import org.opennms.velocloud.client.api.model.User;

public interface VelocloudApiClient {

    List<Edge> getEdges(final UUID enterpriseId) throws VelocloudApiException;

    List<Gateway> getGateways(final UUID enterpriseId) throws VelocloudApiException;

    List<Enterprise> getEnterprises() throws VelocloudApiException;

    List<User> getUsers(final Integer enterpriseId) throws VelocloudApiException;
}
