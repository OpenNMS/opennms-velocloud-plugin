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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.opennms.integration.api.v1.config.requisition.Requisition;
import org.opennms.integration.api.v1.config.requisition.immutables.ImmutableRequisition;
import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.model.IpInterface;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.velocloud.client.VelocloudApiClient;
import org.opennms.velocloud.client.handler.ApiException;
import org.opennms.velocloud.client.model.EdgeResourceCollection;
import org.opennms.velocloud.model.EdgeIpInterface;
import org.opennms.velocloud.model.EdgeMetaData;
import org.opennms.velocloud.model.EdgeNode;

public class CustomerRequisitionProvider extends AbstractRequisitionProvider<CustomerRequisitionProvider.Request> {

    public final static String TYPE = "VelocloudCustomerRequisition";
    private final List<IpInterface> ipInterfaces = new ArrayList<>();
    private final List<Node> nodes = new ArrayList<>();

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    protected Request createRequest(final Map<String, String> parameters) {
        final var request = new Request();

        final var enterpriseId = UUID.fromString(parameters.get("enterprise"));
        request.setEnterpriseId(enterpriseId);
        request.setForeignSource(String.format("velocloud-customer-%s", enterpriseId));

        return request;
    }

    @Override
    protected Requisition handleRequest(final Request request, final VelocloudApiClient client) {
        final var requisition = ImmutableRequisition.newBuilder()
                .setForeignSource(request.getForeignSource());


        try {
            var enterpriseEdges = client.edges.getEnterpriseEdges(request.enterpriseId.toString(),
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null);
        List<EdgeNode> nodes = getEdgeNodes(enterpriseEdges);
        //TODO find how to fetch link info since it not available from edges (just an _href)

        } catch (ApiException e) {
            e.printStackTrace();
        }

        return requisition.build();
    }

    private List<EdgeNode> getEdgeNodes(EdgeResourceCollection enterpriseEdges) {
        ArrayList<EdgeNode> edgeNodes = new ArrayList<EdgeNode>();

        enterpriseEdges.getData().stream().forEach(resource->{
            //Edge is interpreted as a node
            EdgeNode edgeNode = new EdgeNode();
            //Edge link (link between edge and gateway)
            //Edge can have many links
            var links = resource.getLinks();
            //TODO map links to a resources and generate ipInterfaces NMS-14596
            //edgeNode.getIpInterfaces().add(new EdgeIpInterface(""){});

            //TODO map link Status NMS-14596

            edgeNode.setLabel(resource.getName());
            //capture all 3 identifiers for each Velocloud Edge as OpenNMS Node meta-data
            edgeNode.getMetaData().add(new EdgeMetaData("deviceId", resource.getDeviceId()));
            edgeNode.getMetaData().add(new EdgeMetaData("logicalId", resource.getLogicalId()));
            edgeNode.getMetaData().add(new EdgeMetaData("selfMacAddress", resource.getSelfMacAddress()));

        });
        return edgeNodes;
    }

    public static class Request extends AbstractRequisitionProvider.Request {

        private UUID enterpriseId;

        public UUID getEnterpriseId() {
            return this.enterpriseId;
        }

        public void setEnterpriseId(final UUID enterpriseId) {
            this.enterpriseId = enterpriseId;
        }
    }

}
