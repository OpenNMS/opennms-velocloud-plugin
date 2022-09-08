package org.opennms.velocloud.rest.v1;

import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.rest.api.AbstractVelocloudRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

public class VelocloudRestServiceV1Impl extends AbstractVelocloudRestService implements VelocloudRestServiceV1 {

    private static final Logger LOG = LoggerFactory.getLogger(VelocloudRestServiceV1Impl.class);

    public VelocloudRestServiceV1Impl(VelocloudApiClientProvider clientProvider) {
        super(clientProvider);
    }

    @Override
    public Response getCustomersForMSPPartnerConnections(String id) {
        //TODO: figure a way to connect to orchestrator from rest

        return Response.ok().build();
    }
}
