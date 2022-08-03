package org.opennms.velocloud;


import org.junit.Test;
import org.opennms.velocloud.client.handler.auth.ApiKeyAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ApiClientTest {

    @Test
    public void TestAuth(){
        ApiClient client = new ApiClient(
                "https://localhost:9999/",
                "kjsncdkjdnsckdjsfncfs",
                ApiClient.AuthTypes.API_KEY_AUTH);
        var auth = (ApiKeyAuth)client.getAuthentication(ApiClient.AuthTypes.API_KEY_AUTH);
        assertEquals(auth.getApiKey(), "kjsncdkjdnsckdjsfncfs");
        assertEquals(auth.getLocation(), client.AUTH_HEADER_LOCATION);
        assertEquals(auth.getParamName(), client.AUTH_HEADER_NAME);
        assertEquals(auth.getApiKeyPrefix(), client.AUTH_HEADER_PREFIX);
        Map<String, String> mapResult = new HashMap<String, String>();
        auth.applyToParams(new ArrayList<>(), mapResult);
        assertEquals(mapResult.get(client.AUTH_HEADER_NAME), "Token kjsncdkjdnsckdjsfncfs" );

    }
}