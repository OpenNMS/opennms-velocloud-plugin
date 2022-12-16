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
import org.opennms.velocloud.connections.ConnectionManager;

@Command(scope = "opennms-velocloud", name = "connection-add", description = "Add a connection", detailedDescription = "Add a connection to a velocloud orchestrator")
@Service
public class AddConnectionCommand implements Action {

    @Reference
    private ConnectionManager connectionManager;

    @Option(name = "-t", aliases = "--test", description = "Dry run mode, test the credentials but do not save them")
    boolean dryRun = false;

    @Option(name="-f", aliases="--force", description="Skip validation and save the connection as-is")
    public boolean skipValidation = false;

    @Argument(index = 0, name = "alias", description = "Alias", required = true, multiValued = false)
    public String alias = null;

    @Argument(index = 1, name = "url", description = "Orchestrator Url", required = true, multiValued = false)
    public String url = null;

    @Argument(index = 2, name = "apiKey", description = "Orchestrator API Key", required = true, multiValued = false, censor = true)
    public String apiKey = null;

    @Override
    public Object execute() {
        if (this.connectionManager.getConnection(this.alias).isPresent()) {
            System.err.println("Connection with alias already exists: " + this.alias);
            return null;
        }

        final var connection = this.connectionManager.newConnection(this.alias,
                                                                    this.url,
                                                                    this.apiKey);

        if (!this.skipValidation) {
            final var error = connection.validate();
            if (error.isPresent()) {
                System.err.println("Failed to validate credentials: " + error.get().message);
                return null;
            }
        }

        if (this.dryRun) {
            System.out.println("Connection valid");
            return null;
        }

        connection.save();
        System.out.println("Connection created");

        return null;
    }
}
