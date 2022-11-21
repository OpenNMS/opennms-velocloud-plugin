package org.opennms.velocloud.client.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.opennms.velocloud.client.v1.FunctionRefsHolder.ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY;

import java.util.concurrent.atomic.AtomicInteger;

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
    public void getUsesCache() throws Exception {
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
                instance.call("some description", ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY, CREDENTIALS, proxyProperty1)
        );
        assertEquals(
                propertyResult2,
                instance.call("some description", ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY, CREDENTIALS, proxyProperty2)
        );
        Mockito.verify(mockedApi, Mockito.times(1)).enterpriseProxyGetEnterpriseProxyProperty(proxyProperty1);
        Mockito.verify(mockedApi, Mockito.times(1)).enterpriseProxyGetEnterpriseProxyProperty(proxyProperty2);
        Mockito.verify(mockedApi, Mockito.times(2)).enterpriseProxyGetEnterpriseProxyProperty(Mockito.any(EnterpriseProxyGetEnterpriseProxyProperty.class));

        //second time the same calls
        assertEquals(
                propertyResult1,
                instance.call("some description", ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY, CREDENTIALS, proxyProperty1)
        );
        assertEquals(
                propertyResult2,
                instance.call("some description", ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY, CREDENTIALS, proxyProperty2)
        );
        //Api was called still 2 times, the last two calls were cached
        Mockito.verify(mockedApi, Mockito.times(2)).enterpriseProxyGetEnterpriseProxyProperty(Mockito.any(EnterpriseProxyGetEnterpriseProxyProperty.class));

    }

    @Test
    public void getCorrectCachesApi() throws Exception {
        final ApiExecutor instance = new ApiExecutor(Long.MAX_VALUE);
        final AtomicInteger counter = new AtomicInteger(0);
        final ApiCall<String, AllApi> func = (api, parameter) -> {
            //count calls
            counter.incrementAndGet();
            //return api to check it
            return api;
        };

        //counter is incremented to 1 and api is provided
        AllApi first = instance.call("some description", func, CREDENTIALS, "foo");
        assertEquals(1, counter.get());
        assertEquals(
                ApiExecutor.connectApi(CREDENTIALS).getApiClient().getBasePath(),
                first.getApiClient().getBasePath()
        );

        //same result and counter is still 1 after second call -> cache value used
        AllApi second = instance.call("some description", func, CREDENTIALS, "foo");
        assertSame(first, second);
        assertEquals(1, counter.get());

        //counter is incremented when a new parameter provided, but still same "api" used for call of func()
        AllApi third = instance.call("some description", func, CREDENTIALS, "bar");
        assertSame(first, third);
        assertEquals(2, counter.get());

        //counter is incremented when the same parameter but NEW credentials, and "api" is new
        final var otherCredentials = new VelocloudApiClientCredentials(ORCHESTRATOR_URL, "some other key");
        AllApi fourth = instance.call("some description", func, otherCredentials, "bar");
        assertNotSame(first, fourth); //<--not same as first
        assertEquals(3, counter.get());

        //after using same parameter as at first time we get same result as at first time without calling func()
        AllApi fifth = instance.call("some description", func, CREDENTIALS, "foo");
        assertSame(first, fifth);
        assertEquals(3, counter.get());
    }

    @Test
    public void getTestWhenExpired() throws Exception {
        long cacheTime = 100;
        final ApiExecutor instance = new ApiExecutor(cacheTime);

        final AtomicInteger counter = new AtomicInteger(0);
        final ApiCall<String, Integer> func = (api, parameter) -> counter.incrementAndGet();

        assertEquals(1, instance.call("some description", func, CREDENTIALS, "foo").longValue());
        //no increment when cache is not expired
        assertEquals(1, instance.call("some description", func, CREDENTIALS, "foo").longValue());
        Thread.sleep(cacheTime + 1);
        //incremented when cache is expired
        assertEquals(2, instance.call("some description", func, CREDENTIALS, "foo").longValue());
    }

}
