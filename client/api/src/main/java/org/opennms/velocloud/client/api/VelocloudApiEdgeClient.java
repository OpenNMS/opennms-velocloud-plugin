package org.opennms.velocloud.client.api;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.opennms.velocloud.client.api.model.ApplicationTraffic;
import org.opennms.velocloud.client.api.model.DestinationTraffic;
import org.opennms.velocloud.client.api.model.MetricsEdgeLink;
import org.opennms.velocloud.client.api.model.MetricsEdgeQoe;
import org.opennms.velocloud.client.api.model.MetricsEdgeSystem;
import org.opennms.velocloud.client.api.model.SourceTraffic;
import org.opennms.velocloud.client.api.model.Traffic;

public interface VelocloudApiEdgeClient {

    Map<Integer, MetricsEdgeLink> getEdgeLinkMetrics(Instant start, Instant end) throws VelocloudApiException;

    List<ApplicationTraffic> getEdgeAppTraffic(Instant start, Instant end) throws VelocloudApiException;

    List<SourceTraffic> getEdgeSourceTraffic(Instant start, Instant end) throws VelocloudApiException;

    List<DestinationTraffic> getEdgeDestinationTrafficMetrics(Instant start, Instant end) throws VelocloudApiException;

    Map<String, Traffic> getEdgeBusinessTrafficMetrics(Instant start, Instant end) throws VelocloudApiException;

    List<MetricsEdgeQoe> getEdgeQoeMetrics(Instant start, Instant end) throws VelocloudApiException;

    MetricsEdgeSystem getEdgeSystemMetrics(Instant start, Instant end) throws VelocloudApiException;
}
