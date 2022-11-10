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
import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.clients.ClientManager;
import org.opennms.velocloud.connections.ConnectionManager;

@Command(scope = "opennms-velocloud", name = "connection-add", description = "Add a connection", detailedDescription = "Add a connection to a velocloud orchestrator")
@Service
public class AddConnectionCommand implements Action {

    @Reference
    private ConnectionManager connectionManager;

    @Reference
    private ClientManager clientManager;

    @Option(name = "-t", aliases = "--test", description = "Dry run mode, test the credentials but do not save them")
    boolean dryRun = false;

    @Argument(index = 0, name = "alias", description = "Alias", required = true, multiValued = false)
    public String alias = null;

    @Argument(index = 1, name = "url", description = "Orchestrator Url", required = true, multiValued = false)
    public String url = null;

    @Argument(index = 2, name = "apiKey", description = "Orchestrator API Key", required = true, multiValued = false, censor = true)
    public String apiKey = null;

    @Override
    public Object execute() throws Exception {
        if (this.connectionManager.getAliases().contains(this.alias)) {
            System.err.println("Connection with alias already exists: " + this.alias);
            return null;
        }

        try {
            clientManager.validate(VelocloudApiClientCredentials.builder()
                                                                .withOrchestratorUrl(url)
                                                                .withApiKey(apiKey)
                                                                .build());
            System.out.println("Credentials are valid");
        }
        catch (VelocloudApiException e) {
            System.err.println(String.format("Failed to validate credentials: %s", e.getMessage()));
            return null;
        }
        if (!dryRun) {
            this.connectionManager.addConnection(this.alias, this.url, this.apiKey);
            System.out.println("Connection saved");
        }
        else {
            System.out.println("In dry-run mode, connection not saved");
        }
        return null;
    }
}
