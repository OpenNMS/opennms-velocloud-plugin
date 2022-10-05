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

package org.opennms.velocloud.client.shell;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Command(scope = "opennms-velocloud", name = "client", description = "Execute client requests")
@Service
public class ClientCommand implements Action {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
            .registerModule(new JavaTimeModule());

    @Reference
    private VelocloudApiClientProvider clientProvider;

    @Option(name = "--enterprise", required = false)
    private String enterprise;

    @Option(name = "--url", required = true)
    private String orchestratorUrl;

    @Option(name = "--key", required = true)
    private String apiKey;

    @Argument(index = 0, required = true)
    private String method;

    @Argument(index = 1, required = false)
    private String body;

    public Object execute() throws Exception {
        final var credentials = VelocloudApiClientCredentials.builder()
                .withOrchestratorUrl(this.orchestratorUrl)
                .withApiKey(this.apiKey)
                .build();

        final Object client;
        if (this.enterprise == null) {
            client = this.clientProvider.partnerClient(credentials);
        } else if (this.enterprise.equals("customer")) {
            client = this.clientProvider.customerClient(credentials);
        } else {
            client = this.clientProvider.partnerClient(credentials).getCustomerClient(Integer.parseInt(this.enterprise));
        }

        final var api = client.getClass().getField("api").get(client);

        final var method = Arrays.stream(api.getClass().getMethods())
                                 .filter(m -> Objects.equals(m.getName(), this.method))
                                 .filter(m -> m.getParameterCount() == (this.body != null ? 1 : 0))
                                 .collect(toSnowflake())
                                 .getOrThrow(() -> new NoSuchMethodException("Method not found: " + this.method),
                                             () -> new NoSuchElementException("Method ambiguous: " + this.method));

        final Object result;
        if (this.body != null) {
            final var type = method.getParameters()[0].getType();
            final var body = OBJECT_MAPPER.readValue(this.body, type);

            result = method.invoke(api, body);
        } else {
            result = method.invoke(api);
        }

        final var output = OBJECT_MAPPER.writerWithDefaultPrettyPrinter()
                                        .writeValueAsString(result);

        System.out.println(output);

        return null;
    }

    public static class Snowflake<T> {
        private static final Snowflake<?> EMPTY = new Snowflake<>(null);
        private static final Snowflake<?> CROWDED = new Snowflake<>(null);

        public static <T> Snowflake<T> empty() {
            return (Snowflake<T>) EMPTY;
        }

        public static <T> Snowflake<T> crowded() {
            return (Snowflake<T>) CROWDED;
        }

        private final T value;

        private Snowflake(final T value) {
            this.value = value;
        }

        public static <T> Snowflake<T> of(T value) {
            return new Snowflake<>(Objects.requireNonNull(value));
        }

        public T get() {
            return this.getOrThrow(() -> new IllegalStateException("No such element"),
                                   () -> new IllegalStateException("To many elements"));
        }

        public <E1 extends Throwable, E2 extends Throwable> T getOrThrow(final Supplier<E1> empty,
                                                                         final Supplier<E2> crowded) throws E1, E2 {
            if (this == EMPTY) {
                throw empty.get();
            }
            if (this == CROWDED) {
                throw crowded.get();
            }

            return this.value;
        }

        public boolean isEmpty() {
            return this == EMPTY;
        }

        public boolean isCrowded() {
            return this == CROWDED;
        }

        public Snowflake<T> and(final Snowflake<T> that) {
            if (this == EMPTY) {
                return that;
            }
            if (that == EMPTY) {
                return this;
            }

            return Snowflake.crowded();
        }
    }

    public static <T> Collector<T, ?, Snowflake<T>> toSnowflake() {
        return Collectors.reducing(Snowflake.empty(),
                                   Snowflake::of,
                                   Snowflake::and);
    }
}
