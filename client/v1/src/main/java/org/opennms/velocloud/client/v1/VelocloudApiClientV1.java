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

import org.opennms.velocloud.client.api.VelocloudApiClient;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.v1.api.AllApi;
import org.opennms.velocloud.client.v1.api.ApiTokenApi;
import org.opennms.velocloud.client.v1.api.ClientDeviceApi;
import org.opennms.velocloud.client.v1.api.CompositeRoleApi;
import org.opennms.velocloud.client.v1.api.ConfigurationApi;
import org.opennms.velocloud.client.v1.api.CreateApi;
import org.opennms.velocloud.client.v1.api.DeleteApi;
import org.opennms.velocloud.client.v1.api.DisasterRecoveryApi;
import org.opennms.velocloud.client.v1.api.EdgeApi;
import org.opennms.velocloud.client.v1.api.EnterpriseApi;
import org.opennms.velocloud.client.v1.api.EnterpriseProxyApi;
import org.opennms.velocloud.client.v1.api.EventApi;
import org.opennms.velocloud.client.v1.api.FirewallApi;
import org.opennms.velocloud.client.v1.api.FunctionalRoleApi;
import org.opennms.velocloud.client.v1.api.GatewayApi;
import org.opennms.velocloud.client.v1.api.GetApi;
import org.opennms.velocloud.client.v1.api.ImageApi;
import org.opennms.velocloud.client.v1.api.LinkQualityEventApi;
import org.opennms.velocloud.client.v1.api.LoginApi;
import org.opennms.velocloud.client.v1.api.MetricsApi;
import org.opennms.velocloud.client.v1.api.MigrationApi;
import org.opennms.velocloud.client.v1.api.MonitoringApi;
import org.opennms.velocloud.client.v1.api.MspApi;
import org.opennms.velocloud.client.v1.api.NetworkApi;
import org.opennms.velocloud.client.v1.api.OperatorApi;
import org.opennms.velocloud.client.v1.api.OperatorUserApi;
import org.opennms.velocloud.client.v1.api.PartnerApi;
import org.opennms.velocloud.client.v1.api.ProxyApi;
import org.opennms.velocloud.client.v1.api.RoleApi;
import org.opennms.velocloud.client.v1.api.SystemApi;
import org.opennms.velocloud.client.v1.api.SystemPropertyApi;
import org.opennms.velocloud.client.v1.api.UpdateApi;
import org.opennms.velocloud.client.v1.api.UserMaintenanceApi;
import org.opennms.velocloud.client.v1.api.VcoDiagnosticsApi;
import org.opennms.velocloud.client.v1.api.VcoInventoryApi;
import org.opennms.velocloud.client.v1.api.VpnApi;
import org.opennms.velocloud.client.v1.handler.ApiClient;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.model.Edge;
import org.opennms.velocloud.client.v1.handler.ApiException;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdges;
import org.opennms.velocloud.client.v1.model.EnterpriseGetEnterpriseEdgesResultItem;

public class VelocloudApiClientV1 extends ApiClient implements VelocloudApiClient {

    /**
     * Authentication parameter for ApiKeyAuth used in header parameter value
     *
     * @see org.opennms.velocloud.client.v1.handler.auth.ApiKeyAuth
     */
    public static final String AUTH_HEADER_PREFIX = "Token";

    public final AllApi alerts = new AllApi(this);
    public final ApiTokenApi apiTokenApi = new ApiTokenApi(this);
    public final ClientDeviceApi clientDeviceApi = new ClientDeviceApi(this);
    public final CompositeRoleApi compositeRoleApi = new CompositeRoleApi(this);
    public final ConfigurationApi configurationApi = new ConfigurationApi(this);
    public final CreateApi createApi = new CreateApi(this);
    public final DeleteApi deleteApi = new DeleteApi(this);
    public final DisasterRecoveryApi disasterRecoveryApi = new DisasterRecoveryApi(this);
    public final EdgeApi edgeApi = new EdgeApi(this);
    public final EnterpriseApi enterpriseApi = new EnterpriseApi(this);
    public final EnterpriseProxyApi enterpriseProxyApi = new EnterpriseProxyApi(this);
    public final EventApi eventApi = new EventApi(this);
    public final FirewallApi firewallApi = new FirewallApi(this);
    public final FunctionalRoleApi functionalRoleApi = new FunctionalRoleApi(this);
    public final GatewayApi gatewayApi = new GatewayApi(this);
    public final GetApi getApi = new GetApi(this);
    public final ImageApi imageApi = new ImageApi(this);
    public final LinkQualityEventApi linkQualityEventApi = new LinkQualityEventApi(this);
    public final LoginApi loginApi = new LoginApi(this);
    public final MetricsApi metricsApi = new MetricsApi(this);
    public final MigrationApi migrationApi = new MigrationApi(this);
    public final MonitoringApi monitoringApi = new MonitoringApi(this);
    public final MspApi mspApi = new MspApi(this);
    public final NetworkApi networkApi = new NetworkApi(this);
    public final OperatorApi operatorApi = new OperatorApi(this);
    public final OperatorUserApi operatorUserApi = new OperatorUserApi(this);
    public final PartnerApi partnerApi = new PartnerApi(this);
    public final ProxyApi proxyApi = new ProxyApi(this);
    public final RoleApi roleApi = new RoleApi(this);
    public final SystemApi systemApi = new SystemApi(this);
    public final SystemPropertyApi systemPropertyApi = new SystemPropertyApi(this);
    public final UpdateApi updateApi = new UpdateApi(this);
    public final UserMaintenanceApi userMaintenanceApi = new UserMaintenanceApi(this);
    public final VcoDiagnosticsApi vcoDiagnosticsApi = new VcoDiagnosticsApi(this);
    public final VcoInventoryApi vcoInventoryApi = new VcoInventoryApi(this);
    public final VpnApi vpnApi = new VpnApi(this);

    public VelocloudApiClientV1(final String url, final String apiKey) {
        super();
        setBasePath(url);
        setApiKeyPrefix(AUTH_HEADER_PREFIX);
        setApiKey(apiKey);
    }

    @Override
    public List<Edge> getEdges(final UUID enterpriseId) throws VelocloudApiException {
        try {
            final List<EnterpriseGetEnterpriseEdgesResultItem> edges = this.enterpriseApi.enterpriseGetEnterpriseEdges(new EnterpriseGetEnterpriseEdges());
            return edges.stream()
                        .map(e -> Edge.builder()
                                      .withEnterpriseId(enterpriseId)
                                      .withSite(e.getSite().getName())
                                      .withOperator(e.getConfiguration().getOperator().getName())
                                      .withHub(e.isIsHub())
                                      .build())
                        .collect(Collectors.toList());
        } catch (final ApiException e) {
            throw new VelocloudApiException("Error requesting edges", e);
        }
    }

}
