package org.opennms.velocloud.rest.api;

import org.opennms.velocloud.client.api.VelocloudApiClient;
import org.opennms.velocloud.client.api.VelocloudApiException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/velocloud/api")
public interface VelocloudRestService {

    @GET
    @Path("/customers/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    Response getCustomersForMSPPartnerConnections(@PathParam("id") String id) throws VelocloudApiException;

    @GET
    @Path("/status")
    @Produces(value = {MediaType.APPLICATION_JSON})
    Response getStatus();

    VelocloudApiClient getClient(final String url, final String token) throws VelocloudApiException;
}
