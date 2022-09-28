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

package org.opennms.velocloud.shell;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.opennms.integration.api.v1.requisition.RequisitionProvider;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.connections.ConnectionManager;

import java.util.Objects;

@Command(scope = "opennms-velocloud", name = "delete-connection", description = "Delete connection")
@Service
public class VelocloudDeleteConnectionCommand implements Action {

    @Reference
    private ConnectionManager connectionManager;

    @Argument(index = 0, name = "alias", required = true, description = "Alias of credentials to delete")
    @Completion(AliasCompleter.class)
    public String alias;

    @Override
    public Object execute() {
        final var connection = Objects.requireNonNull(this.connectionManager.getConnection(alias),
                "No credentials found with given alias");
        // TODO need to confirm that connection is not active
        // TODO is it possible to remove credentials from SCV?
        return null;
    }
}
