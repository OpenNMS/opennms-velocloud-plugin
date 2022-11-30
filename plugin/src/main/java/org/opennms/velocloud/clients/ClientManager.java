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

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;
import org.opennms.velocloud.client.api.VelocloudApiCustomerClient;
import org.opennms.velocloud.client.api.VelocloudApiException;
import org.opennms.velocloud.client.api.VelocloudApiPartnerClient;
import org.opennms.velocloud.connections.ConnectionValidationError;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class ClientManager implements ServiceListener {
    private static final Logger LOG = LoggerFactory.getLogger(ClientManager.class);
    private static final String VELOCLOUD_API_CLIENT_PROVIDER_SERVICE_FILTER = "(objectClass=org.opennms.velocloud.client.api.VelocloudApiClientProvider)";

    private final VelocloudApiClientProvider clientProvider;

    private final Cache<VelocloudApiClientCredentials, ClientEntry> clients;

    public ClientManager(final VelocloudApiClientProvider clientProvider, final long cacheRetentionMs) {
        this.clientProvider = Objects.requireNonNull(clientProvider);
        this.clients = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.ofMillis(cacheRetentionMs))
                .build();

        final BundleContext bundleContext = FrameworkUtil.getBundle(ClientManager.class).getBundleContext();
        try {
            bundleContext.addServiceListener(this, VELOCLOUD_API_CLIENT_PROVIDER_SERVICE_FILTER);
            LOG.debug("ClientManager: added service listener");
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        this.clients.invalidateAll();
        LOG.debug("ClientManager: Dependent service was changed, clearing cache");
    }

    public void destroy() {
        final BundleContext bundleContext = FrameworkUtil.getBundle(ClientManager.class).getBundleContext();
        bundleContext.removeServiceListener(this);
        LOG.debug("ClientManager: removed service listener");
    }

    public VelocloudApiPartnerClient getPartnerClient(final VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        synchronized(this.clients) {
            try {
                return this.clients.get(credentials, () -> new PartnerClientEntry(this.clientProvider.partnerClient(credentials)))
                        .asPartnerClient()
                        .orElseThrow(() -> new VelocloudApiException("Not a partner client"));
            } catch (ExecutionException e) {
                throw new VelocloudApiException("Error creating partner client", e);
            }
        }
    }

    public VelocloudApiCustomerClient getCustomerClient(final VelocloudApiClientCredentials credentials) throws VelocloudApiException {
        synchronized(this.clients) {
            try {
                return this.clients.get(credentials, () -> new CustomerClientEntry(this.clientProvider.customerClient(credentials)))
                        .asCustomerClient()
                        .orElseThrow(() -> new VelocloudApiException("Not a customer client"));
            } catch (ExecutionException e) {
                throw new VelocloudApiException("Error creating customer client", e);
            }
        }
    }

    public ConnectionValidationError validate(final VelocloudApiClientCredentials credentials) {
        try {
            this.clientProvider.partnerClient(credentials);
        } catch (VelocloudApiException e) {
            try {
                this.clientProvider.customerClient(credentials);
            }
            catch (VelocloudApiException ex) {
                return new ConnectionValidationError("Credentials could not be validated");
            }
        }
        return null;
    }

    public void purgeClient(VelocloudApiClientCredentials credentials) {
        synchronized(this.clients) {
            this.clients.invalidate(credentials);
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
