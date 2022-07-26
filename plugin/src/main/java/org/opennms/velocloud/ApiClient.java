package org.opennms.velocloud;

import java.util.concurrent.CompletableFuture;

import org.opennms.velocloud.model.Alert;
import org.opennms.velocloud.model.Topology;

public class ApiClient {

    public ApiClient(String url, String apiKey) {
    }

    public CompletableFuture<Void> forwardTopology(Topology topology) {
        return doPost();
    }

    private CompletableFuture<Void> doPost() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        return future;
    }

}
