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

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.config.requisition.Requisition;
import org.opennms.integration.api.v1.requisition.RequisitionProvider;
import org.opennms.integration.api.v1.requisition.RequisitionRequest;
import org.opennms.velocloud.client.api.VelocloudApiClient;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractRequisitionProvider<Req extends AbstractRequisitionProvider.Request> implements RequisitionProvider {

    protected abstract Req createRequest(final Map<String, String> parameters);

    protected abstract Requisition handleRequest(final Req request, final VelocloudApiClient client);

    @Override
    public final RequisitionRequest getRequest(final Map<String, String> parameters) {
        final var request = Objects.requireNonNull(this.createRequest(parameters));
        request.setForeignSource(Objects.requireNonNullElseGet(parameters.get("name"), request::getForeignSource));
        request.setOrchestratorUrl(Objects.requireNonNull(parameters.get("url")));
        request.setApiKey(Objects.requireNonNull(parameters.get("apiKey")));

        return request;
    }

    @Override
    public final Requisition getRequisition(final RequisitionRequest rawRequest) {
        final var request = (Req) rawRequest;

       // final var client = new VelocloudApiClientV2(request.getOrchestratorUrl(),
       //                                           request.getApiKey());

        return this.handleRequest(request, null);
    }

    @Override
    public final byte[] marshalRequest(final RequisitionRequest request) {
        final var mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsBytes(request);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final RequisitionRequest unmarshalRequest(final byte[] bytes) {
        final var mapper = new ObjectMapper();
        try {
            return mapper.readValue(bytes, RequisitionRequest.class);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static abstract class Request implements RequisitionRequest {

        public Request() {}

        private String foreignSource;

        private String orchestratorUrl;

        private String apiKey;

        public String getForeignSource() {
            return this.foreignSource;
        }

        public void setForeignSource(final String foreignSource) {
            this.foreignSource = foreignSource;
        }

        public String getOrchestratorUrl() {
            return this.orchestratorUrl;
        }

        public void setOrchestratorUrl(final String orchestratorUrl) {
            this.orchestratorUrl = orchestratorUrl;
        }

        public String getApiKey() {
            return this.apiKey;
        }

        public void setApiKey(final String apiKey) {
            this.apiKey = apiKey;
        }
    }
}