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
package org.opennms.velocloud.shell.connections;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.connections.Connection;
import org.opennms.velocloud.connections.ConnectionManager;

import javax.ws.rs.ProcessingException;

@Command(scope = "opennms-velocloud", name = "connection-validate", description = "Edit a connection", detailedDescription = "Edit an existing connection to a velocloud orchestrator")
@Service
public class ValidateConnectionCommand implements Action {

    @Reference
    private ConnectionManager connectionManager;

    @Option (name = "-t", aliases = "--connection-type", required = true)
    public String type;


    @Argument(index = 0, name = "alias", description = "Alias", required = true, multiValued = false)
    public String alias = null;

    @Override
    public Object execute() throws Exception {
        if (!this.connectionManager.getAliases().contains(this.alias)) {
            System.err.println("No connection with the given alias exists: " + this.alias);
            return null;
        }
        Connection conn = this.connectionManager.getConnection(this.alias).orElseThrow();
        try {
            if (type.equals("p")) {
                this.connectionManager.validatePartnerCredentials(conn.getOrchestratorUrl(), conn.getApiKey());
            }
            else if (type.equals("c")) {
                this.connectionManager.validateCustomerCredentials(conn.getOrchestratorUrl(), conn.getApiKey());
            }
            else {
                System.err.println("Invalid connection type");
                return null;
            }
            System.out.println("Connection is valid!");
            return null;
        }
        catch (VelocloudApiException | ProcessingException e) {
            System.err.println("Invalid connection: " + e.getMessage());
            return null;
        }
    }
}
