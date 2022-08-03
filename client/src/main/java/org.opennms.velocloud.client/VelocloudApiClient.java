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
package org.opennms.velocloud.client;

import org.opennms.velocloud.client.api.AlertsApi;
import org.opennms.velocloud.client.api.ApplicationMapsApi;
import org.opennms.velocloud.client.api.ClientsApi;
import org.opennms.velocloud.client.api.ConfigureApi;
import org.opennms.velocloud.client.api.CustomerProfilesApi;
import org.opennms.velocloud.client.api.CustomersApi;
import org.opennms.velocloud.client.api.EdgeSpecificProfilesApi;
import org.opennms.velocloud.client.api.EdgesApi;
import org.opennms.velocloud.client.api.EventsApi;
import org.opennms.velocloud.client.api.MonitorApi;
import org.opennms.velocloud.client.api.NetworkServicesApi;
import org.opennms.velocloud.client.handler.ApiClient;


public class VelocloudApiClient extends ApiClient {

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

    public final AlertsApi alertsApi = new AlertsApi(this);
    public final ApplicationMapsApi applicationMapsApi = new ApplicationMapsApi(this);
    public final ClientsApi clientsApi = new ClientsApi(this);
    public final ConfigureApi configureApi = new ConfigureApi(this);
    public final CustomerProfilesApi customerProfilesApi = new CustomerProfilesApi(this);
    public final CustomersApi customersApi = new CustomersApi(this);
    public final EdgesApi edgesApi = new EdgesApi(this);
    public final EdgeSpecificProfilesApi edgeSpecificProfilesApi = new EdgeSpecificProfilesApi(this);
    public final EventsApi eventsApi = new EventsApi(this);
    public final MonitorApi monitorApi = new MonitorApi(this);
    public final NetworkServicesApi networkServicesApi = new NetworkServicesApi(this);

    public VelocloudApiClient(final String url, final String apiKey) {
        super();
        setBasePath(url);
        setApiKeyPrefix(AUTH_HEADER_PREFIX);
        setApiKey(apiKey);
    }
}

