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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.v1.handler.auth.ApiKeyAuth;

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
}
