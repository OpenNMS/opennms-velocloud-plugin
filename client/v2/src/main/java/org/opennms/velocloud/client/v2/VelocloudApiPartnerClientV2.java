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

import java.time.Instant;
import java.util.List;

import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Customer;
import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.client.api.model.Gateway;
import org.opennms.velocloud.client.api.model.MetricsGateway;
import org.opennms.velocloud.client.api.model.PartnerEvent;
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


public class VelocloudApiPartnerClientV2 extends ApiClient implements VelocloudApiPartnerClient {

    /**
     * Authentication parameter for ApiKeyAuth used in header parameter value
     *
     * @see org.opennms.velocloud.client.v2.handler.auth.ApiKeyAuth
     */
    private static final String AUTH_HEADER_PREFIX = "Token";

    private final AlertsApi alerts = new AlertsApi(this);
    private final ApplicationMapsApi applicationMaps = new ApplicationMapsApi(this);
    private final ClientsApi clients = new ClientsApi(this);
    private final ConfigureApi configure = new ConfigureApi(this);
    private final CustomerProfilesApi customerProfiles = new CustomerProfilesApi(this);
    private final CustomersApi customers = new CustomersApi(this);
    private final EdgesApi edges = new EdgesApi(this);
    private final EdgeSpecificProfilesApi edgeSpecificProfiles = new EdgeSpecificProfilesApi(this);
    private final EventsApi events = new EventsApi(this);
    private final MonitorApi monitor = new MonitorApi(this);
    private final NetworkServicesApi networkServices = new NetworkServicesApi(this);

    public VelocloudApiPartnerClientV2(final String url, final String apiKey) {
        super();
        setBasePath(url);
        setApiKeyPrefix(AUTH_HEADER_PREFIX);
        setApiKey(apiKey);
    }


    @Override
    public VelocloudApiCustomerClient getCustomerClient(final Integer enterpriseId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Gateway> getGateways() throws VelocloudApiException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Customer> getCustomers() throws VelocloudApiException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<PartnerEvent> getEvents(Instant start, Instant end) throws VelocloudApiException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Edge> getEdgeAssignments(int gatewayId) throws VelocloudApiException {
        throw new UnsupportedOperationException();
    }

    @Override
    public MetricsGateway getGatewayMetrics(int gatewayId, int intervalMillis) throws VelocloudApiException {
        throw new UnsupportedOperationException();
    }
}

