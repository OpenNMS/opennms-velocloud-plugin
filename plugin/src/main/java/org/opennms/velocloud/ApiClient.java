package org.opennms.velocloud;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.opennms.velocloud.client.handler.Configuration;
import org.opennms.velocloud.client.handler.auth.ApiKeyAuth;
import org.opennms.velocloud.client.handler.auth.HttpBasicAuth;
import org.opennms.velocloud.client.handler.auth.OAuth;
import org.opennms.velocloud.model.Topology;

public class ApiClient extends org.opennms.velocloud.client.handler.ApiClient {

    public static final String AUTHORIZATION = "Authorization";
    public static final String HEADER = "header";
    public static final String AUTH_HEADER _PREFIX = "Token";

    public ApiClient(final String url, final String params, final String authType) {
        super();
        setBasePath(url);
        switch (authType){
            case AuthTypes.API_KEY_AUTH:
                authentications = Map.of("ApiKeyAuth", new ApiKeyAuth(HEADER, AUTHORIZATION));
                setApiKeyPrefix(TOKEN);
                setApiKey(params);
                break;
            case AuthTypes.BASIC_AUTH:
                authentications = Map.of("BasicAuth", new HttpBasicAuth());
                //TODO: handle params username/password
//                var creds = params.split("|");
//                setUsername(creds[0]);
//                setPassword(creds[1]);
                break;
            case AuthTypes.OAUTH:
                authentications = Map.of("OAuth", new OAuth());
                setAccessToken(params);
                break;
            default:
                break;
        }
        Configuration.setDefaultApiClient(this);


    }

    public CompletableFuture<Void> forwardTopology(Topology topology) {
        return doPost();
    }

    private CompletableFuture<Void> doPost() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        return future;
    }

    public static class AuthTypes{
        public static final String API_KEY_AUTH = "ApiKeyAuth";
        public static final String OAUTH = "OAuth";
        public static final String BASIC_AUTH = "BasicAuth";
    }
}
