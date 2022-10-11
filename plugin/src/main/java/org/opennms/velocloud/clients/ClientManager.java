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

package org.opennms.velocloud.clients;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;

import com.google.common.collect.Maps;

public class ClientManager {

    private final VelocloudApiClientProvider clientProvider;

    private final ConcurrentMap<VelocloudApiClientCredentials, ClientEntry> clients = Maps.newConcurrentMap();

    public ClientManager(final VelocloudApiClientProvider clientProvider) {
        this.clientProvider = Objects.requireNonNull(clientProvider);
    }

    public VelocloudApiPartnerClient getPartnerClient(final VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        synchronized (this.clients) {
            final var entry = this.clients.get(credentials);
            if (entry != null) {
                return entry.asPartnerClient()
                            .orElseThrow(() -> new VelocloudApiException("Not a partner client"));
            }

            final var client = this.clientProvider.partnerClient(credentials);
            this.clients.put(credentials, new PartnerClientEntry(client));

            return client;
        }
    }

    public VelocloudApiCustomerClient getCustomerClient(final VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        synchronized (this.clients) {
            final var entry = this.clients.get(credentials);
            if (entry != null) {
                return entry.asCustomerClient()
                            .orElseThrow(() -> new VelocloudApiException("Not a customer client"));
            }

            final var client = this.clientProvider.customerClient(credentials);
            this.clients.put(credentials, new CustomerClientEntry(client));

            return client;
        }
    }

    public static abstract class ClientEntry {

        protected ClientEntry() {
        }

        public abstract Optional<VelocloudApiPartnerClient> asPartnerClient();

        public abstract Optional<VelocloudApiCustomerClient> asCustomerClient();
    }

    private static class PartnerClientEntry extends ClientEntry {

        private final VelocloudApiPartnerClient client;

        private PartnerClientEntry(final VelocloudApiPartnerClient client) {
            this.client = Objects.requireNonNull(client);
        }

        @Override
        public Optional<VelocloudApiPartnerClient> asPartnerClient() {
            return Optional.of(this.client);
        }

        @Override
        public Optional<VelocloudApiCustomerClient> asCustomerClient() {
            return Optional.empty();
        }
    }

    private static class CustomerClientEntry extends ClientEntry {

        private final VelocloudApiCustomerClient client;

        private CustomerClientEntry(final VelocloudApiCustomerClient client) {
            this.client = Objects.requireNonNull(client);
        }

        @Override
        public Optional<VelocloudApiPartnerClient> asPartnerClient() {
            return Optional.empty();
        }

        @Override
        public Optional<VelocloudApiCustomerClient> asCustomerClient() {
            return Optional.of(this.client);
        }
    }
}
