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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.mockito.Mockito;
import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.handler.ApiClient;
import org.opennms.velocloud.client.v1.handler.auth.ApiKeyAuth;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdgesResultItem;
import org.opennms.velocloud.client.v1.model.Link;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

public class VelocloudApiClientV1Test {
    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTH_HEADER_PREFIX = "Token";
    public static final String AUTH_HEADER_LOCATION = "header";

    @Test
    public void testBaseUrl() {
        final var api = VelocloudApiClientProviderV1.connectApi(VelocloudApiClientCredentials.builder()
                .withOrchestratorUrl("https://localhost:9999/")
                .withApiKey("")
                .build());

        assertEquals(api.getApiClient().getBasePath(), "https://localhost:9999/portal/rest");
    }

    @Test
    public void testAuth() {
        final var key = "kjsncdkjdnsckdjsfncfs";

        final var api = VelocloudApiClientProviderV1.connectApi(VelocloudApiClientCredentials.builder()
                                                                                             .withOrchestratorUrl("https://localhost:9999/")
                                                                                             .withApiKey(key)
                                                                                             .build());

        var auth = (ApiKeyAuth) api.getApiClient().getAuthentication("ApiKeyAuth");
        assertEquals(auth.getApiKey(), key);
        assertEquals(auth.getLocation(), AUTH_HEADER_LOCATION);
        assertEquals(auth.getParamName(), AUTH_HEADER_NAME);
        assertEquals(auth.getApiKeyPrefix(), AUTH_HEADER_PREFIX);

        final Map<String, String> mapResult = new HashMap<>();
        auth.applyToParams(new ArrayList<>(), mapResult);

        assertEquals(mapResult.get(AUTH_HEADER_NAME), AUTH_HEADER_PREFIX + " " + key);
    }

    @Test
    public void testDeserialization() throws Exception {
        // prepare ApiClient
        final VelocloudApiClientCredentials credentials = new VelocloudApiClientCredentials("orchestratorUrl", "apiKey");
        final AllApi allApi = VelocloudApiClientProviderV1.connectApi(credentials);
        final ApiClient apiClient = allApi.getApiClient();

        // prepare invalid json data
        final OffsetDateTime currentDate = OffsetDateTime.now();
        final DateTimeFormatter customDateTimeFormatter = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .optionalStart()
                .appendOffset("+HH:MM", "+00:00")
                .optionalEnd()
                .toFormatter();

        final EnterpriseGetEnterpriseEdgesResultItem edge = new EnterpriseGetEnterpriseEdgesResultItem();
        final Link link = new Link();
        link.setCreated(currentDate);
        edge.setLinks(Lists.newArrayList(link));

        final ObjectMapper objectMapper = apiClient.getJSON().getContext(null);

        final String originalJsonString = objectMapper.writeValueAsString(edge);
        final String currentDateString = customDateTimeFormatter.format(currentDate);

        assertTrue(originalJsonString.contains(currentDateString));

        final String invalidDateString = "0000-00-00 00:00:00";
        final String invalidJsonString = originalJsonString.replace(currentDateString, invalidDateString);

        // check invalid created
        final EnterpriseGetEnterpriseEdgesResultItem edgeInvalidCreated = objectMapper.readValue(invalidJsonString, EnterpriseGetEnterpriseEdgesResultItem.class);
        assertNull(edgeInvalidCreated.getLinks().get(0).getCreated());

        // check correct created
        final EnterpriseGetEnterpriseEdgesResultItem edgeCorrectCreated = objectMapper.readValue(originalJsonString, EnterpriseGetEnterpriseEdgesResultItem.class);
        assertNotNull(edgeCorrectCreated.getLinks().get(0).getCreated());
    }
}
