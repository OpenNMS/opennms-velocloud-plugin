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

import java.util.Optional;

@Command(scope = "opennms-velocloud", name = "connection-edit", description = "Edit a connection", detailedDescription = "Edit an existing connection to a velocloud orchestrator")
@Service
public class EditConnectionCommand implements Action {

    @Reference
    private ConnectionManager connectionManager;

    @Option(name="-f", aliases="--force", description="Skip validation and save the connection as-is")
    public boolean skipValidation = false;

    @Argument(index = 0, name = "alias", description = "Alias", required = true, multiValued = false)
    public String alias = null;

    @Argument(index = 1, name = "url", description = "Orchestrator Url", required = true, multiValued = false)
    public String url = null;

    @Argument(index = 2, name = "apiKey", description = "Orchestrator API Key", required = true, multiValued = false, censor = true)
    public String apiKey = null;

    @Override
    public Object execute() throws Exception {
        if (!this.connectionManager.contains(this.alias)) {
            System.err.println(String.format("No connection with the given alias exists: %s",  this.alias));
            return null;
        }
        if (!skipValidation) {
            final var exception = Optional.ofNullable(this.connectionManager.testConnection(url, apiKey));
            if (exception.isPresent()) {
                System.err.println(String.format("Failed to validate credentials: %s", exception.get().getMessage()));
                return null;
            }
            else {
                System.out.println("Credentials are valid");
            }
        }
        else {
            System.out.println("Skipping validation");
        }
        this.connectionManager.getConnection(alias).get().update(url, apiKey);
        System.out.println("Connection updated");
        return null;
    }
}
