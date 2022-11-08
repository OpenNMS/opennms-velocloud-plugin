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

import java.util.List;

import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterprise;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdges;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdgesResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseResult;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseUsers;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseUsersResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyEnterprises;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyEnterprisesResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyGateways;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyGatewaysResultItem;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyProperty;
import org.opennms.velocloud.client.v1.model.EnterpriseProxyGetEnterpriseProxyPropertyResult;
import org.opennms.velocloud.client.v1.model.EventGetEnterpriseEvents;
import org.opennms.velocloud.client.v1.model.EventGetEnterpriseEventsResult;
import org.opennms.velocloud.client.v1.model.EventGetProxyEvents;
import org.opennms.velocloud.client.v1.model.EventGetProxyEventsResult;
import org.opennms.velocloud.client.v1.model.MonitoringGetEnterpriseEdgeNvsTunnelStatusBody;
import org.opennms.velocloud.client.v1.model.MonitoringGetEnterpriseEdgeNvsTunnelStatusResultItem;

/**
 * Used to store all functions that are used in caches
 * Only by providing the same object into the CacheFactory the same cache can be reused
 */
public class FunctionRefsHolder {
    public final static ApiCall<EnterpriseProxyGetEnterpriseProxyProperty, EnterpriseProxyGetEnterpriseProxyPropertyResult>
            ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_PROPERTY = AllApi::enterpriseProxyGetEnterpriseProxyProperty;

    public final static ApiCall<EnterpriseGetEnterprise, EnterpriseGetEnterpriseResult>
            ENTERPRISE_GET_ENTERPRISE = AllApi::enterpriseGetEnterprise;

    public final static ApiCall<EnterpriseGetEnterpriseEdges, List<EnterpriseGetEnterpriseEdgesResultItem>>
            ENTERPRISE_GET_ENTERPRISE_EDGES = AllApi::enterpriseGetEnterpriseEdges;

    public final static ApiCall<EnterpriseGetEnterpriseUsers, List<EnterpriseGetEnterpriseUsersResultItem>>
            ENTERPRISE_GET_ENTERPRISE_USERS = AllApi::enterpriseGetEnterpriseUsers;

    public final static ApiCall<EventGetEnterpriseEvents, EventGetEnterpriseEventsResult>
            EVENT_GET_ENTERPRISE_EVENTS  = AllApi::eventGetEnterpriseEvents;

    public final static ApiCall<MonitoringGetEnterpriseEdgeNvsTunnelStatusBody, List<MonitoringGetEnterpriseEdgeNvsTunnelStatusResultItem>>
            MONITORING_GET_ENTERPRISE_EDGE_NVS_TUNNEL_STATUS = AllApi::monitoringGetEnterpriseEdgeNvsTunnelStatus;

    public final static ApiCall<EnterpriseProxyGetEnterpriseProxyGateways, List<EnterpriseProxyGetEnterpriseProxyGatewaysResultItem>>
            ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_GATEWAYS = AllApi::enterpriseProxyGetEnterpriseProxyGateways;

    public final static ApiCall<EnterpriseProxyGetEnterpriseProxyEnterprises, List<EnterpriseProxyGetEnterpriseProxyEnterprisesResultItem>>
            ENTERPRISE_PROXY_GET_ENTERPRISE_PROXY_ENTERPRISES = AllApi::enterpriseProxyGetEnterpriseProxyEnterprises;

    public final static ApiCall<EventGetProxyEvents, EventGetProxyEventsResult>
            EVENT_GET_PROXY_EVENTS = AllApi::eventGetProxyEvents;
}
