package org.opennms.velocloud;


import org.junit.Test;
import org.opennms.velocloud.client.AuthTypes;
import org.opennms.velocloud.client.VelocloudApiClient;
import org.opennms.velocloud.client.handler.auth.ApiKeyAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class VelocloudApiClientTest {

    @Test
    public void TestAuth(){
        VelocloudApiClient client = new VelocloudApiClient(
                "https://localhost:9999/",
                "kjsncdkjdnsckdjsfncfs");
        var auth = (ApiKeyAuth)client.getAuthentication(AuthTypes.API_KEY_AUTH.toString());
        assertEquals(auth.getApiKey(), "kjsncdkjdnsckdjsfncfs");
        assertEquals(auth.getLocation(), client.AUTH_HEADER_LOCATION);
        assertEquals(auth.getParamName(), client.AUTH_HEADER_NAME);
        assertEquals(auth.getApiKeyPrefix(), client.AUTH_HEADER_PREFIX);
        Map<String, String> mapResult = new HashMap<String, String>();
        auth.applyToParams(new ArrayList<>(), mapResult);
        assertEquals(mapResult.get(client.AUTH_HEADER_NAME), "Token kjsncdkjdnsckdjsfncfs" );

    }
}