package org.opennms.velocloud.client.v1;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.mockito.Mockito;
import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.handler.ApiException;
import org.opennms.velocloud.client.v1.model.EdgeGetEdge;
import org.opennms.velocloud.client.v1.model.EdgeGetEdgeResult;

public class ApiExecutorTest {

    private final static String ORCHESTRATOR_URL = "https:/some.url:12345/";
    private final static String API_KEY = "SOME KEY";
    private final static VelocloudApiClientCredentials CREDENTIALS = new VelocloudApiClientCredentials(ORCHESTRATOR_URL, API_KEY);

    @Test
    public void sameVelocloudCall() throws Exception {
        final ApiExecutor instance = new ApiExecutor(Long.MAX_VALUE);
        final AtomicInteger counter = new AtomicInteger(0);

        //increase counter on each function call
        ApiCall<String, Integer> function = (api, parameter) -> counter.incrementAndGet();

        final VelocloudCall<String, Integer> testCall = instance.createApiCall("some text", function, CREDENTIALS, Mockito.mock(AllApi.class));

        assertEquals(1, testCall.call("BAR").intValue());
        //no increasing on same parameter
        assertEquals(1, testCall.call("BAR").intValue());
        assertEquals(1, testCall.call("BAR").intValue());
        //increasing on other parameter
        assertEquals(2, testCall.call("FOO").intValue());
        //no increasing on same parameter
        assertEquals(2, testCall.call("FOO").intValue());
        //still cached "1" for "BAR"
        assertEquals(1, testCall.call("BAR").intValue());
    }

    @Test
    public void differentCallsButSameFunction() throws Exception {
        final ApiExecutor instance = new ApiExecutor(Long.MAX_VALUE);
        final AtomicInteger counter = new AtomicInteger(0);
        ApiCall<String, Integer> function = (api, parameter) -> counter.incrementAndGet();

        final VelocloudCall<String, Integer> testCall1 = instance.createApiCall("some text", function, CREDENTIALS, Mockito.mock(AllApi.class));
        final VelocloudCall<String, Integer> testCall2 = instance.createApiCall("another text", function, CREDENTIALS, Mockito.mock(AllApi.class));

        assertEquals(1, testCall1.call("BAR").intValue());
        //no increasing on same parameter but different VelocloudCall
        assertEquals(1, testCall2.call("BAR").intValue());
        //increasing on other parameter
        assertEquals(2, testCall2.call("FOO").intValue());
        assertEquals(2, testCall1.call("FOO").intValue());
        //still cached "1" for "BAR"
        assertEquals(1, testCall1.call("BAR").intValue());
        assertEquals(1, testCall2.call("BAR").intValue());
    }

    @Test
    public void differentCredentials() throws Exception {
        final ApiExecutor instance = new ApiExecutor(Long.MAX_VALUE);
        final AtomicInteger counter = new AtomicInteger(0);
        ApiCall<String, Integer> function = (api, parameter) -> counter.incrementAndGet();
        var credentialsSame = new VelocloudApiClientCredentials(ORCHESTRATOR_URL, API_KEY);
        var credentialsDifferent = new VelocloudApiClientCredentials(ORCHESTRATOR_URL, API_KEY + "suffix");

        final VelocloudCall<String, Integer> testCall = instance.createApiCall("some text", function, CREDENTIALS, Mockito.mock(AllApi.class));
        final VelocloudCall<String, Integer> testCallSame = instance.createApiCall("another text", function, credentialsSame, Mockito.mock(AllApi.class));
        final VelocloudCall<String, Integer> testCallDifferent = instance.createApiCall("another text", function, credentialsDifferent, Mockito.mock(AllApi.class));

        assertEquals(1, testCall.call("BAR").intValue());
        //no increasing on same credentials
        assertEquals(1, testCallSame.call("BAR").intValue());
        //increasing on other credentials
        assertEquals(2, testCallDifferent.call("BAR").intValue());
        //increasing on other parameter
        assertEquals(3, testCall.call("FOO").intValue());
        assertEquals(4, testCallDifferent.call("FOO").intValue());
        //still cached "1" for first credentials with "BAR"
        assertEquals(1, testCall.call("BAR").intValue());
        assertEquals(1, testCallSame.call("BAR").intValue());
    }

    @Test
    public void getTestWhenExpired() throws Exception {
        long cacheTime = 100;
        final ApiExecutor instance = new ApiExecutor(cacheTime);
        final AtomicInteger counter = new AtomicInteger(0);
        ApiCall<String, Integer> function = (api, parameter) -> counter.incrementAndGet();

        final VelocloudCall<String, Integer> testCall = instance.createApiCall("some text", function, CREDENTIALS, Mockito.mock(AllApi.class));

        assertEquals(1, testCall.call("BAR").intValue());
        //no increment when cache is not expired
        assertEquals(1, testCall.call("BAR").intValue());
        //incremented when cache is expired
        Thread.sleep(cacheTime + 1);
        assertEquals(2, testCall.call("BAR").intValue());
    }

    @Test(expected = VelocloudApiException.class)
    public void testException() throws Exception {
        final ApiExecutor instance = new ApiExecutor(Long.MAX_VALUE);

        ApiCall<String, Integer> function = (api, parameter) -> {throw new ApiException();};

        final String str = "TestCall";
        final VelocloudCall<String, Integer> testCall = instance.createApiCall(str, function, CREDENTIALS, Mockito.mock(AllApi.class));

        try {
            testCall.call("BAR");
        } catch (VelocloudApiException e) {
            assertEquals("Failed to execute API call: " + str, e.getMessage());
            throw e;
        }
    }

    @Test
    public void providedApiCalledWithProvidedParameter() throws Exception {
        final ApiExecutor instance = new ApiExecutor(Long.MAX_VALUE);

        final AllApi mockedApi = Mockito.mock(AllApi.class);
        EdgeGetEdge parameter = new EdgeGetEdge();
        EdgeGetEdgeResult response = new EdgeGetEdgeResult();
        Mockito.when(mockedApi.edgeGetEdge(parameter)).thenReturn(response);

        final VelocloudCall<EdgeGetEdge, EdgeGetEdgeResult> call =
                instance.createApiCall("doesn't matter", AllApi::edgeGetEdge, CREDENTIALS, mockedApi);

        assertEquals(response, call.call(parameter));
        //verify that specified method of mockedApi was one time called with specified parameter
        Mockito.verify(mockedApi, Mockito.times(1)).edgeGetEdge(parameter);

        assertEquals(response, call.call(parameter));
        //still 1 time called
        Mockito.verify(mockedApi, Mockito.times(1)).edgeGetEdge(Mockito.any(EdgeGetEdge.class));
    }

}
