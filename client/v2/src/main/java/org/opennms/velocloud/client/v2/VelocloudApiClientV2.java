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

import org.opennms.velocloud.client.api.VelocloudApiClient;
import org.opennms.velocloud.client.v2.api.AlertsApi;
import org.opennms.velocloud.client.v2.api.ApplicationMapsApi;
import org.opennms.velocloud.client.v2.api.ClientsApi;
import org.opennms.velocloud.client.v2.api.ConfigureApi;
import org.opennms.velocloud.client.v2.api.CustomerProfilesApi;
import org.opennms.velocloud.client.v2.api.CustomersApi;
import org.opennms.velocloud.client.v2.api.EdgeSpecificProfilesApi;
import org.opennms.velocloud.client.v2.api.EdgesApi;
import org.opennms.velocloud.client.v2.api.EventsApi;
import org.opennms.velocloud.client.v2.api.MonitorApi;
import org.opennms.velocloud.client.v2.api.NetworkServicesApi;
import org.opennms.velocloud.client.v2.handler.ApiClient;


public class VelocloudApiClientV2 extends ApiClient implements VelocloudApiClient {

    /**
     * Authentication parameter for ApiKeyAuth heather parameter name/key
     * @see org.opennms.velocloud.client.handler.auth.ApiKeyAuth
     */
    public static final String AUTH_HEADER_NAME = "Authorization";
    /**
     * Authentication parameter for ApiKeyAuth header location
     * @see org.opennms.velocloud.client.handler.auth.ApiKeyAuth
     */
    public static final String AUTH_HEADER_LOCATION = "header";
    /**
     * Authentication parameter for ApiKeyAuth used in header parameter value
     * @see org.opennms.velocloud.client.handler.auth.ApiKeyAuth
     */
    public static final String AUTH_HEADER_PREFIX = "Token";

    public final AlertsApi alerts = new AlertsApi(this);
    public final ApplicationMapsApi applicationMaps = new ApplicationMapsApi(this);
    public final ClientsApi clients = new ClientsApi(this);
    public final ConfigureApi configure = new ConfigureApi(this);
    public final CustomerProfilesApi customerProfiles = new CustomerProfilesApi(this);
    public final CustomersApi customers = new CustomersApi(this);
    public final EdgesApi edges = new EdgesApi(this);
    public final EdgeSpecificProfilesApi edgeSpecificProfiles = new EdgeSpecificProfilesApi(this);
    public final EventsApi events = new EventsApi(this);
    public final MonitorApi monitor = new MonitorApi(this);
    public final NetworkServicesApi networkServices = new NetworkServicesApi(this);

    public VelocloudApiClientV2(final String url, final String apiKey) {
        super();
        setBasePath(url);
        setApiKeyPrefix(AUTH_HEADER_PREFIX);
        setApiKey(apiKey);
    }
}

