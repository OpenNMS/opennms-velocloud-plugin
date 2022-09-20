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

import com.google.common.base.Strings;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.opennms.integration.api.v1.scv.Credentials;
import org.opennms.integration.api.v1.scv.SecureCredentialsVault;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.api.VelocloudApiException;

@Command(scope = "opennms-velocloud", name = "list-msp-customers", description = "List MSP Customers", detailedDescription = "List Enterprises for MSP")
@Service
public class VelocloudListCustomersCommand implements Action {

    @Reference
    public SecureCredentialsVault secureCredentialsVault;

    @Reference
    private VelocloudApiClientProvider clientProvider;

    @Argument(index = 0, name = "alias", description = "Velocloud URL", required = true, multiValued = false)
    @Completion(AliasCompleter.class)
    public String alias = null;

    @Override
    public Object execute() throws Exception {

        final Credentials credentials = secureCredentialsVault.getCredentials(alias);
        final String username = credentials.getUsername();
        if (Strings.isNullOrEmpty(username)) {
            throw new VelocloudApiException("Unable to retrieve velocloud credentials");
        }
        final String url = credentials.getAttribute("url");
        final String token = credentials.getAttribute("token");
        if (Strings.isNullOrEmpty(token)) {
            throw new VelocloudApiException("Unable to retrieve velocloud token");
        }
        clientProvider.connect(url, token).getEnterprises().forEach(e -> System.out.println(e.toString()));
        return null;
    }
}
