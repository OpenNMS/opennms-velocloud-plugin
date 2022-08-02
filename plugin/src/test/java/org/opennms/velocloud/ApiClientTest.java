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
        assertEquals(auth.getLocation(), client.HEADER);
        assertEquals(auth.getParamName(), client.AUTHORIZATION);
        assertEquals(auth.getApiKeyPrefix(), client.TOKEN);
        Map<String, String> mapResult = new HashMap<String, String>();
        auth.applyToParams(new ArrayList<>(), mapResult);
        assertEquals(mapResult.get(client.AUTHORIZATION), "Token kjsncdkjdnsckdjsfncfs" );

    }
}