package org.opennms.velocloud.client.api;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.opennms.velocloud.client.api.model.ApplicationTraffic;
import org.opennms.velocloud.client.api.model.MetricsEdgeLink;
import org.opennms.velocloud.client.api.model.MetricsEdgeQoe;
import org.opennms.velocloud.client.api.model.MetricsEdgeSystem;
import org.opennms.velocloud.client.api.model.Traffic;

public interface VelocloudApiEdgeClient {

    Map<Integer, MetricsEdgeLink> getEdgeLinkMetrics(Instant start, Instant end) throws VelocloudApiException;

    public List<ApplicationTraffic> getEdgeAppTrafficMetrics(Instant start, Instant end) throws VelocloudApiException;

    public Map<String, Traffic> getEdgeSourceTrafficMetrics(Instant start, Instant end) throws VelocloudApiException;

    public Map<String, Traffic> getEdgeDestinationTrafficMetrics(Instant start, Instant end) throws VelocloudApiException;

    public Map<String, Traffic> getEdgeBusinessTrafficMetrics(Instant start, Instant end) throws VelocloudApiException;

    List<MetricsEdgeQoe> getEdgeQoeMetrics(Instant start, Instant end) throws VelocloudApiException;

    MetricsEdgeSystem getEdgeSystemMetrics(Instant start, Instant end) throws VelocloudApiException;
}
