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
package org.opennms.velocloud.client.v2;


import org.junit.Test;
import org.opennms.velocloud.client.v2.handler.auth.ApiKeyAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class VelocloudApiClientV2Test {

    @Test
    public void TestAuth(){
        VelocloudApiClientV2 client = new VelocloudApiClientV2(
                "https://localhost:9999/",
                "kjsncdkjdnsckdjsfncfs");
        var auth = (ApiKeyAuth)client.getAuthentication("ApiKeyAuth");
        assertEquals(auth.getApiKey(), "kjsncdkjdnsckdjsfncfs");
        assertEquals(auth.getLocation(), client.AUTH_HEADER_LOCATION);
        assertEquals(auth.getParamName(), client.AUTH_HEADER_NAME);
        assertEquals(auth.getApiKeyPrefix(), client.AUTH_HEADER_PREFIX);
        Map<String, String> mapResult = new HashMap<String, String>();
        auth.applyToParams(new ArrayList<>(), mapResult);
        assertEquals(mapResult.get(client.AUTH_HEADER_NAME), "Token kjsncdkjdnsckdjsfncfs" );

    }
}