package org.opennms.velocloud.rest.api;

import org.opennms.velocloud.client.api.VelocloudApiClient;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.Response;

public class AbstractVelocloudRestService implements VelocloudRestService {

    private final VelocloudApiClientProvider clientProvider;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractVelocloudRestService.class);

    public AbstractVelocloudRestService(final VelocloudApiClientProvider clientProvider) {
        this.clientProvider = clientProvider;
    }

    @Override
    public Response getCustomersForMSPPartnerConnections(String id) {
        throw new NotSupportedException();
    }

    @Override
    public Response getStatus() {
        throw new NotSupportedException();
    }

    @Override
    public VelocloudApiClient getClient(final String url, final String token) throws VelocloudApiException {
        return clientProvider.connect(url, token);
    }
}
