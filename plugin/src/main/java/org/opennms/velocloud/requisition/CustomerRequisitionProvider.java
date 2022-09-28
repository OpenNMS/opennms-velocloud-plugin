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

package org.opennms.velocloud.requisition;

import java.util.UUID;

import org.opennms.integration.api.v1.config.requisition.Requisition;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisition;
import org.opennms.velocloud.client.api.VelocloudApiClient;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.connections.Connection;
import org.opennms.velocloud.connections.ConnectionManager;

public class CustomerRequisitionProvider extends AbstractRequisitionProvider<CustomerRequisitionProvider.Request> {

    public final static String TYPE = "velocloud-customer";

    public CustomerRequisitionProvider(final VelocloudApiClientProvider clientProvider,
                                       final ConnectionManager connectionManager) {
        super(clientProvider, connectionManager);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    protected Request createRequest(final Connection connection) {
        return new Request(connection);
    }

    @Override
    protected Requisition handleRequest(final Request request, final VelocloudApiClient client) {
        final var requisition = ImmutableRequisition.newBuilder()
                                                    .setForeignSource(request.getForeignSource());

        return requisition.build();
    }

    public static class Request extends AbstractRequisitionProvider.Request {

        public Request() {}

        public Request(final Connection connection) {
            super("velocloud-customer", connection);
            this.enterpriseId = connection.getEnterpriseId().orElse(null);
        }

        private UUID enterpriseId;

        public UUID getEnterpriseId() {
            return this.enterpriseId;
        }

        public void setEnterpriseId(final UUID enterpriseId) {
            this.enterpriseId = enterpriseId;
        }
    }

}
