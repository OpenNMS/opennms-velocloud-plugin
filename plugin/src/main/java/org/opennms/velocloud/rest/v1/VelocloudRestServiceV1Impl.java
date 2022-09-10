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
        try {
            getClient("", "").getEnterprises();
            Response.ok().build();
        } catch (VelocloudApiException e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @Override
    public Response getStatus() {
        //TODO: check for api connection status
        return Response
                .status(Response.Status.OK)
                .entity("{\"status\":TBD\"}")
                .build();
    }
}
