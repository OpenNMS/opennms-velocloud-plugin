/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

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

public class ApiCacheTest {

    private final static String ORCHESTRATOR_URL = "https:/some.url:12345/";
    private final static String API_KEY = "SOME KEY";
    private final static VelocloudApiClientCredentials CREDENTIALS = new VelocloudApiClientCredentials(ORCHESTRATOR_URL, API_KEY);

    @Test
    public void sameVelocloudCall() throws Exception {
        final ApiCache instance = new ApiCache(Long.MAX_VALUE);
        final AtomicInteger counter = new AtomicInteger(0);
        final ApiCache.Endpoint<String, Integer> function = (api, parameter) -> counter.incrementAndGet();

        final ApiCache.Api api = instance.createApi(Mockito.mock(AllApi.class), CREDENTIALS);

        assertEquals(1, api.call("some text", function, "BAR").intValue());
        //no increasing on same parameter
        assertEquals(1, api.call("some text", function, "BAR").intValue());
        assertEquals(1, api.call("some text", function, "BAR").intValue());
        //increasing on other parameter
        assertEquals(2, api.call("some text", function, "FOO").intValue());
        //no increasing on same parameter
        assertEquals(2, api.call("some text", function, "FOO").intValue());
        //still cached "1" for "BAR"
        assertEquals(1, api.call("some text", function, "BAR").intValue());
    }

    @Test
    public void differentCallsButSameFunction() throws Exception {
        final ApiCache instance = new ApiCache(Long.MAX_VALUE);
        final AtomicInteger counter = new AtomicInteger(0);
        final ApiCache.Endpoint<String, Integer> function = (api, parameter) -> counter.incrementAndGet();

        final ApiCache.Api api = instance.createApi(Mockito.mock(AllApi.class), CREDENTIALS);

        assertEquals(1, api.call("some text", function, "BAR").intValue());
        //no increasing on same parameter but different VelocloudCall
        assertEquals(1, api.call("another text", function, "BAR").intValue());
        //increasing on other parameter
        assertEquals(2, api.call("another text", function, "FOO").intValue());
        assertEquals(2, api.call("some text", function, "FOO").intValue());
        //still cached "1" for "BAR"
        assertEquals(1, api.call("some text", function, "BAR").intValue());
        assertEquals(1, api.call("another text", function, "BAR").intValue());
    }

    @Test
    public void differentCredentials() throws Exception {
        final ApiCache instance = new ApiCache(Long.MAX_VALUE);
        final AtomicInteger counter = new AtomicInteger(0);
        final ApiCache.Endpoint<String, Integer> function = (api, parameter) -> counter.incrementAndGet();

        final ApiCache.Api apiOrig = instance.createApi(Mockito.mock(AllApi.class), CREDENTIALS);
        final ApiCache.Api apiSame = instance.createApi(Mockito.mock(AllApi.class), new VelocloudApiClientCredentials(ORCHESTRATOR_URL, API_KEY)); // Same credentials as original
        final ApiCache.Api apiDiff = instance.createApi(Mockito.mock(AllApi.class), new VelocloudApiClientCredentials(ORCHESTRATOR_URL, API_KEY + "suffix"));

        assertEquals(1, apiOrig.call("some text", function, "BAR").intValue());
        //no increasing on same credentials
        assertEquals(1, apiSame.call("another text", function, "BAR").intValue());
        //increasing on other credentials
        assertEquals(2, apiDiff.call("another text", function, "BAR").intValue());
        //increasing on other parameter
        assertEquals(3, apiOrig.call("some text", function, "FOO").intValue());
        assertEquals(4, apiDiff.call("another text", function, "FOO").intValue());
        //still cached "1" for first credentials with "BAR"
        assertEquals(1, apiOrig.call("some text", function, "BAR").intValue());
        assertEquals(1, apiSame.call("another text", function, "BAR").intValue());
    }

    @Test
    public void getTestWhenExpired() throws Exception {
        final long cacheTime = 100;
        final ApiCache instance = new ApiCache(cacheTime);
        final AtomicInteger counter = new AtomicInteger(0);
        final ApiCache.Endpoint<String, Integer> function = (api, parameter) -> counter.incrementAndGet();
        final ApiCache.Api api = instance.createApi(Mockito.mock(AllApi.class), CREDENTIALS);

        assertEquals(1, api.call("some text", function, "BAR").intValue());
        //no increment when cache is not expired
        assertEquals(1, api.call("some text", function, "BAR").intValue());
        //incremented when cache is expired
        Thread.sleep(cacheTime + 1);
        assertEquals(2, api.call("some text", function, "BAR").intValue());
    }

    @Test(expected = VelocloudApiException.class)
    public void testException() throws Exception {
        final ApiCache instance = new ApiCache(Long.MAX_VALUE);
        final ApiCache.Endpoint<String, Integer> function = (api, parameter) -> {throw new ApiException();};
        final ApiCache.Api api = instance.createApi(Mockito.mock(AllApi.class), CREDENTIALS);

        final String desc = "test-call";

        try {
            api.call(desc, function, "BAR");
        } catch (VelocloudApiException e) {
            assertEquals("Failed to execute API call: " + desc, e.getMessage());
            throw e;
        }
    }

    @Test
    public void providedApiCalledWithProvidedParameter() throws Exception {
        final ApiCache instance = new ApiCache(Long.MAX_VALUE);

        final AllApi mockedApi = Mockito.mock(AllApi.class);
        final EdgeGetEdge parameter = new EdgeGetEdge();
        final EdgeGetEdgeResult response = new EdgeGetEdgeResult();
        Mockito.when(mockedApi.edgeGetEdge(parameter)).thenReturn(response);

        final ApiCache.Api api = instance.createApi(mockedApi, CREDENTIALS);

        final ApiCache.Endpoint<EdgeGetEdge, EdgeGetEdgeResult> endpoint = AllApi::edgeGetEdge;

        assertEquals(response, api.call("doesn't matter", endpoint, parameter));
        //verify that specified method of mockedApi was one time called with specified parameter
        Mockito.verify(mockedApi, Mockito.times(1)).edgeGetEdge(parameter);

        assertEquals(response, api.call("doesn't matter", endpoint, parameter));
        //still 1 time called
        Mockito.verify(mockedApi, Mockito.times(1)).edgeGetEdge(Mockito.any(EdgeGetEdge.class));
    }

}
