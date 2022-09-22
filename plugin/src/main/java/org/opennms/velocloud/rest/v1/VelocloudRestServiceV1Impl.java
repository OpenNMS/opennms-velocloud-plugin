package org.opennms.velocloud.rest.v1;

import org.opennms.integration.api.v1.scv.SecureCredentialsVault;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Enterprise;
import org.opennms.velocloud.rest.api.AbstractVelocloudRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.List;

public class VelocloudRestServiceV1Impl extends AbstractVelocloudRestService implements VelocloudRestServiceV1 {

    private static final Logger LOG = LoggerFactory.getLogger(VelocloudRestServiceV1Impl.class);

    public VelocloudRestServiceV1Impl(VelocloudApiClientProvider clientProvider, SecureCredentialsVault secureCredentialsVault) {
        super(clientProvider, secureCredentialsVault);
    }

    @Override
    public Response getMspPartnerConnections(final String alias) {
        try {
            getClientFor(alias).getEnterpriseProxyConnections(0,0);
            return Response.ok().build();
        } catch (VelocloudApiException e) {
            LOG.error("An error occured trying to retrieve MSP Partner connections" , e.getCause());
            return Response.serverError().build();
        }
    }

    @Override
    public Response getCustomersForMspPartner(final String alias) {
        try {
            List<Enterprise> enterprises = getClientFor(alias).getEnterpriseProxies();
            return Response.ok().entity(enterprises).build();
        } catch (VelocloudApiException e) {
            LOG.error("An error occured trying to retrieve customers for MSP Partner" , e.getCause());
            return Response.serverError().build();
        }

    }

    @Override
    public Response getStatus() {
        //TODO: check for api connection status
        // An opennms user should be associated to an Operator user???
        // and that way we can use that operator credentials or token?
        // and use /login/operatorLogin
        // or use /login/enterpriseLogin
        return Response
                .status(Response.Status.OK)
                .entity("{\"status\":TBD\"}")
                .build();
    }
}
