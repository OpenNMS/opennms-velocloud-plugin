package org.opennms.velocloud.client.v1;

import static org.junit.Assert.assertEquals;
import static org.opennms.velocloud.client.v1.FunctionRefsHolder.ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY;

import org.junit.Test;
import org.mockito.Mockito;
import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.handler.auth.ApiKeyAuth;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyProperty;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyPropertyResult;

public class ApiExecutorTest {

    private final String ORCHESTRATOR_URL = "https:/some.url:12345/";
    final String API_KEY = "SOME KEY";
    final VelocloudApiClientCredentials CREDENTIALS = new VelocloudApiClientCredentials(ORCHESTRATOR_URL, API_KEY);

    @Test
    public void connectApi() {
        final AllApi result = ApiExecutor.connectApi(CREDENTIALS);

        assertEquals(
                ORCHESTRATOR_URL + ApiExecutor.PATH,
                result.getApiClient().getBasePath()
        );

        assertEquals(
                ApiExecutor.AUTH_HEADER_PREFIX,
                result.getApiClient().getAuthentications().values().stream()
                        .filter(a -> a instanceof ApiKeyAuth)
                        .map(a -> ((ApiKeyAuth) a).getApiKeyPrefix()).findFirst().get()
        );

        assertEquals(
                API_KEY,
                result.getApiClient().getAuthentications().values().stream()
                        .filter(a -> a instanceof ApiKeyAuth)
                        .map(a -> ((ApiKeyAuth) a).getApiKey()).findFirst().get()
        );
    }

    @Test
    public void get() throws Exception {
        final ApiExecutor instance = new ApiExecutor(Long.MAX_VALUE);

        final AllApi mockedApi = Mockito.mock(AllApi.class);

        //Add to cacheApi mocked AllApi
        instance.cacheApi.put(CREDENTIALS, mockedApi);
        final EnterpriseProxyGetEnterpriseProxyProperty proxyProperty1 = new EnterpriseProxyGetEnterpriseProxyProperty();
        final EnterpriseProxyGetEnterpriseProxyProperty proxyProperty2 = new EnterpriseProxyGetEnterpriseProxyProperty();
        proxyProperty1.setId(1);
        proxyProperty2.setId(2);

        final EnterpriseProxyGetEnterpriseProxyPropertyResult propertyResult1 = new EnterpriseProxyGetEnterpriseProxyPropertyResult();
        final EnterpriseProxyGetEnterpriseProxyPropertyResult propertyResult2 = new EnterpriseProxyGetEnterpriseProxyPropertyResult();
        propertyResult1.setId(1);
        propertyResult2.setId(2);

        Mockito.when((mockedApi.enterpriseProxyGetEnterpriseProxyProperty(proxyProperty1))).thenReturn(propertyResult1);
        Mockito.when((mockedApi.enterpriseProxyGetEnterpriseProxyProperty(proxyProperty2))).thenReturn(propertyResult2);

        //Assert that calls are executed and proper values are returned
        assertEquals(
                propertyResult1,
                instance.get("some description", ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY, CREDENTIALS, proxyProperty1)
        );
        assertEquals(
                propertyResult2,
                instance.get("some description", ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY, CREDENTIALS, proxyProperty2)
        );
        Mockito.verify(mockedApi, Mockito.times(1)).enterpriseProxyGetEnterpriseProxyProperty(proxyProperty1);
        Mockito.verify(mockedApi, Mockito.times(1)).enterpriseProxyGetEnterpriseProxyProperty(proxyProperty2);
        Mockito.verify(mockedApi, Mockito.times(2)).enterpriseProxyGetEnterpriseProxyProperty(Mockito.any(EnterpriseProxyGetEnterpriseProxyProperty.class));

        //second time the same calls
        assertEquals(
                propertyResult1,
                instance.get("some description", ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY, CREDENTIALS, proxyProperty1)
        );
        assertEquals(
                propertyResult2,
                instance.get("some description", ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY, CREDENTIALS, proxyProperty2)
        );
        //Api was called still 2 times, the last two calls were cached
        Mockito.verify(mockedApi, Mockito.times(2)).enterpriseProxyGetEnterpriseProxyProperty(Mockito.any(EnterpriseProxyGetEnterpriseProxyProperty.class));

    }
}