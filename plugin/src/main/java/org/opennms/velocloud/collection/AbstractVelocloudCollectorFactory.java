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

package org.opennms.velocloud.collection;

import static org.opennms.velocloud.pollers.AbstractStatusPoller.ATTR_ALIAS;
import static org.opennms.velocloud.pollers.AbstractStatusPoller.ATTR_API_KEY;
import static org.opennms.velocloud.pollers.AbstractStatusPoller.ATTR_ORCHESTRATOR_URL;
import static org.opennms.velocloud.requisition.AbstractRequisitionProvider.PARAMETER_FOREIGN_SOURCE;
import static org.opennms.velocloud.requisition.AbstractRequisitionProvider.VELOCLOUD_METADATA_CONTEXT;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.collectors.CollectionRequest;
import org.opennms.integration.api.v1.collectors.ServiceCollectorFactory;
import org.opennms.integration.api.v1.dao.NodeDao;
import org.opennms.integration.api.v1.model.MetaData;
import org.opennms.integration.api.v1.model.Node;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.connections.Connection;
import org.opennms.velocloud.connections.ConnectionManager;

abstract public class AbstractVelocloudCollectorFactory<T extends AbstractVelocloudServiceCollector> implements ServiceCollectorFactory<T> {

    protected final ClientManager clientManager;
    protected final ConnectionManager connectionManager;

    public AbstractVelocloudCollectorFactory(final ClientManager clientManager, final ConnectionManager connectionManager) {
        this.clientManager = Objects.requireNonNull(clientManager);
        this.connectionManager = Objects.requireNonNull(connectionManager);
    }

    @Override
    public Map<String, Object> getRuntimeAttributes(CollectionRequest collectionRequest, Map<String, Object> parameters) {
        final var alias = Objects.requireNonNull(parameters.get(ATTR_ALIAS), "Missing property: " + ATTR_ALIAS);
        final var connection = this.connectionManager.getConnection(Objects.toString(alias))
                .orElseThrow(() -> new NullPointerException("Connection not found for alias: " + alias));

        final var runtimeAttributes = new HashMap<>(parameters);

        runtimeAttributes.put(ATTR_ORCHESTRATOR_URL, connection.getOrchestratorUrl());
        runtimeAttributes.put(ATTR_API_KEY, connection.getApiKey());

        return runtimeAttributes;
    }

    @Override
    public Map<String, String> marshalParameters(Map<String, Object> parameters) {
        return parameters.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toString()));
    }

    @Override
    public Map<String, Object> unmarshalParameters(Map<String, String> parameters) {
        return parameters.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
