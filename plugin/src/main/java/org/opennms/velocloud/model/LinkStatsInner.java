/*
 * VMware SD-WAN Orchestration API v2
 * ##     <h2>Introduction</h2>         <p style=\"font-weight: 100;\">VMware SD-WAN Orchestrator release 4.2.0 introduced a new SD-WAN Orchestration REST API, informally referred to as “APIv2” because it is intended to gradually replace the VCO Portal API (“v1”) as the primary interface supported for Partner and Customer development. The API remains under active development; it currently supports:</p>             <ul>                 <li style=\"font-weight: 100;\">Monitoring of Customer SD-WAN deployments (roughly the same feature set that is available via the web UI's Customer -> Monitoring pane) - Min VCO version `4.2.0` </li>                 <li style=\"font-weight: 100;\">Configuration of Edge device settings (both via profiles and Edge-specific overrides) - Min VCO version `5.0.0.0` </li>             </ul>         <p style=\"font-weight: 100;\">Designed with extensive input from users of the Portal API, APIv2 offers an improved developer experience to new and experienced users alike. The API embraces the REST architectural style and draws on design conventions and best practices that are well-established within VMware and beyond, among these: </p>         <ul>             <li style=\"font-weight: 100;\">The usage of standard HTTP methods to denote how API operations behave </li>             <li style=\"font-weight: 100;\">The usage of a range of standard HTTP status codes in a manner consistent with the definitions specified in RFC 7231 (e.g. 201 is used to indicate that a resource was successfully created, 400 is used when the server detects a request syntax error, etc.)</li>             <li style=\"font-weight: 100;\">The usage of hyperlinks to represent relations among API resources and facilitate navigation of the API (e.g. Customer Edges are represented with a URL path like `/enterprises/a980c178-6c32-45ca-aaf9-1c74d441eb69/edges/ca607949-0263-4bc7-afb0-d37a165da82c`, and any given Edge may be fetched via a simple HTTP GET request to that path)</li>         </ul>     <h2>Getting Started</h2>         <h3>Accessing the API</h3>             <p style=\"font-weight: 100;\">On Orchestrators running software version 5.0.0.0 or newer, the API is accessible over HTTPS via the `/api/sdwan/v2` URL base path. For all earlier Orchestrator software releases, the `/sdwan` base path must be used. While newer Orchestrator releases currently still attempt to honor API requests to the `/sdwan` base path (by issuing HTTP 308 redirects to the corresponding `/api/sdwan/v2` route), the `/sdwan` base path is considered to be deprecated as of the 5.0.0.0 release. Unless otherwise noted, readers should interpret all URL paths that appear in this document as relative paths, to which the `/api/sdwan/v2` base path must be prepended in order to produce complete, valid URL paths. For example, to fetch a collection of Customers, an Operator Admin would make an HTTP GET call to the `/api/sdwan/v2/enterprises` endpoint.</p>         <h3>Errors</h3>             <p style=\"font-weight: 100;\">All error responses that originate from the API include a JSON response body containing the following attributes: </p>             <ul>                 <li style=\"font-weight: 100;\">`code`: A short string that uniquely identifies the error</li>                 <li style=\"font-weight: 100;\">`message`: A brief, user-friendly message elaborating on the error that occurred</li>                 <li style=\"font-weight: 100;\">`developerMessage`: A technical error message intended to help the developer of the client script or application to better understand the error </li>                 <li style=\"font-weight: 100;\">`errors`: A detailed list of syntax validation errors, if any </li>             </ul>             <p style=\"font-weight: 100;\">Note that the content of the `message` and `developerMessage` strings are subject to change at any time; clients should not make any assumptions about the content of these strings. </p>         <h3>API Polling Frequency</h3>             <p style=\"font-weight: 100;\">For stats APIs like link stats, flow stats, health stats, etc. we don’t recommend polling any more frequently than once every 10 minutes, as various factors (time skew, processing latency, quirks in the query logic) can cause statistics to appear to be missing over shorter timeframes. Events APIs should be called max once per minute.</p>     <h2>Common Parameters</h2>         <h3>Graph Traversal (Experimental)</h3>             <p style=\"font-weight: 100;\">All HTTP GET operations support a query parameter named include, which may be used to dynamically resolve “nested” resource attributes beyond the `_href` pointer that is produced by default for all linked resources. The server treats the value of the include parameter as a comma-separated list of paths referring to nested parameters. e.g. If a user were to submit a request to `GET /enterprises/<enterpriseLogicalId>/edges`, that user could instruct the server to resolve geographic site coordinates for each Edge using the HTTP query option `include=site.lat,site.lon`). The server also supports the use of a special wildcard operator (`*`) in include expressions, which may be used to resolve all attributes associated with a particular linked resource (e.g. `include=site.*` can be used to instruct the server to produce all attributes associated with an Edge site).</p>             <p style=\"font-weight: 100;\">This feature is considered experimental at this time; it is not yet supported for all linked resources, and the server does not support resolution of doubly-nested (or triply-nested, etc.) attributes.</p>         <h3>Pagination</h3>             <p style=\"font-weight: 100;\">HTTP GET operations that query resource collections of indeterminate size produce paginated result sets. This helps ensure that the API user experience does not degrade as a user's resource footprint expands across various dimensions (e.g. number of Edges deployed, number of Customer events generated, etc.). Alongside a `data` array which contains a (possibly truncated) result set, paginated responses include a `metaData` object. In the event that a query matches a result set that is larger than the server-enforced maximum size (or a user-specified `limit`, if one was provided), a `nextPageLink` token is included in the response `metaData` object. That token may be passed to the server in a subsequent GET request as a query parameter called `nextPageLink` in order to fetch the next page in the result set.</p>             <p style=\"font-weight: 100;\">For example, suppose a Customer has 3000 Edges and an API user with access to that Customer makes an initial query to `GET /enterprises/<enterpriseLogicalId>/edges`. We expect that this query will \"match\" all 3000 Customer Edges. The server, however, enforces a maximum result set size of 2048, so it produces a `data` array containing 2048 Edges, and a `metaData` object alongside that array which includes a `nextPageLink` token. The user would fetch the next page in the result set by making a request to: `GET /enterprises/<enterpriseLogicalId>/edges?nextPageLink=<nextPageToken>`.</p>     
 *
 * OpenAPI spec version: 2021-05-01
 * Contact: velo-sdk-support@vmware.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package org.opennms.velocloud.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
/**
 * LinkStatsInner
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class LinkStatsInner {
  @SerializedName("link")
  private Object link = null;

  @SerializedName("bytesTx")
  private Float bytesTx = null;

  @SerializedName("bytesRx")
  private Float bytesRx = null;

  @SerializedName("packetsTx")
  private Float packetsTx = null;

  @SerializedName("packetsRx")
  private Float packetsRx = null;

  @SerializedName("totalBytes")
  private Float totalBytes = null;

  @SerializedName("totalPackets")
  private Float totalPackets = null;

  @SerializedName("p1BytesRx")
  private Float p1BytesRx = null;

  @SerializedName("p1BytesTx")
  private Float p1BytesTx = null;

  @SerializedName("p1PacketsRx")
  private Float p1PacketsRx = null;

  @SerializedName("p1PacketsTx")
  private Float p1PacketsTx = null;

  @SerializedName("p2BytesRx")
  private Float p2BytesRx = null;

  @SerializedName("p2BytesTx")
  private Float p2BytesTx = null;

  @SerializedName("p2PacketsRx")
  private Float p2PacketsRx = null;

  @SerializedName("p2PacketsTx")
  private Float p2PacketsTx = null;

  @SerializedName("p3BytesRx")
  private Float p3BytesRx = null;

  @SerializedName("p3BytesTx")
  private Float p3BytesTx = null;

  @SerializedName("p3PacketsRx")
  private Float p3PacketsRx = null;

  @SerializedName("p3PacketsTx")
  private Float p3PacketsTx = null;

  @SerializedName("controlBytesRx")
  private Float controlBytesRx = null;

  @SerializedName("controlBytesTx")
  private Float controlBytesTx = null;

  @SerializedName("controlPacketsRx")
  private Float controlPacketsRx = null;

  @SerializedName("controlPacketsTx")
  private Float controlPacketsTx = null;

  @SerializedName("bpsOfBestPathRx")
  private Float bpsOfBestPathRx = null;

  @SerializedName("bpsOfBestPathTx")
  private Float bpsOfBestPathTx = null;

  @SerializedName("bestJitterMsRx")
  private Float bestJitterMsRx = null;

  @SerializedName("bestJitterMsTx")
  private Float bestJitterMsTx = null;

  @SerializedName("bestLatencyMsRx")
  private Float bestLatencyMsRx = null;

  @SerializedName("bestLatencyMsTx")
  private Float bestLatencyMsTx = null;

  @SerializedName("bestLossPctRx")
  private Float bestLossPctRx = null;

  @SerializedName("bestLossPctTx")
  private Float bestLossPctTx = null;

  @SerializedName("scoreTx")
  private Float scoreTx = null;

  @SerializedName("scoreRx")
  private Float scoreRx = null;

  @SerializedName("signalStrength")
  private Float signalStrength = null;

  @SerializedName("name")
  private String name = null;

  public LinkStatsInner link(Object link) {
    this.link = link;
    return this;
  }

   /**
   * Get link
   * @return link
  **/
  @Schema(description = "")
  public Object getLink() {
    return link;
  }

  public void setLink(Object link) {
    this.link = link;
  }

  public LinkStatsInner bytesTx(Float bytesTx) {
    this.bytesTx = bytesTx;
    return this;
  }

   /**
   * Get bytesTx
   * @return bytesTx
  **/
  @Schema(description = "")
  public Float getBytesTx() {
    return bytesTx;
  }

  public void setBytesTx(Float bytesTx) {
    this.bytesTx = bytesTx;
  }

  public LinkStatsInner bytesRx(Float bytesRx) {
    this.bytesRx = bytesRx;
    return this;
  }

   /**
   * Get bytesRx
   * @return bytesRx
  **/
  @Schema(description = "")
  public Float getBytesRx() {
    return bytesRx;
  }

  public void setBytesRx(Float bytesRx) {
    this.bytesRx = bytesRx;
  }

  public LinkStatsInner packetsTx(Float packetsTx) {
    this.packetsTx = packetsTx;
    return this;
  }

   /**
   * Get packetsTx
   * @return packetsTx
  **/
  @Schema(description = "")
  public Float getPacketsTx() {
    return packetsTx;
  }

  public void setPacketsTx(Float packetsTx) {
    this.packetsTx = packetsTx;
  }

  public LinkStatsInner packetsRx(Float packetsRx) {
    this.packetsRx = packetsRx;
    return this;
  }

   /**
   * Get packetsRx
   * @return packetsRx
  **/
  @Schema(description = "")
  public Float getPacketsRx() {
    return packetsRx;
  }

  public void setPacketsRx(Float packetsRx) {
    this.packetsRx = packetsRx;
  }

  public LinkStatsInner totalBytes(Float totalBytes) {
    this.totalBytes = totalBytes;
    return this;
  }

   /**
   * Get totalBytes
   * @return totalBytes
  **/
  @Schema(description = "")
  public Float getTotalBytes() {
    return totalBytes;
  }

  public void setTotalBytes(Float totalBytes) {
    this.totalBytes = totalBytes;
  }

  public LinkStatsInner totalPackets(Float totalPackets) {
    this.totalPackets = totalPackets;
    return this;
  }

   /**
   * Get totalPackets
   * @return totalPackets
  **/
  @Schema(description = "")
  public Float getTotalPackets() {
    return totalPackets;
  }

  public void setTotalPackets(Float totalPackets) {
    this.totalPackets = totalPackets;
  }

  public LinkStatsInner p1BytesRx(Float p1BytesRx) {
    this.p1BytesRx = p1BytesRx;
    return this;
  }

   /**
   * Get p1BytesRx
   * @return p1BytesRx
  **/
  @Schema(description = "")
  public Float getP1BytesRx() {
    return p1BytesRx;
  }

  public void setP1BytesRx(Float p1BytesRx) {
    this.p1BytesRx = p1BytesRx;
  }

  public LinkStatsInner p1BytesTx(Float p1BytesTx) {
    this.p1BytesTx = p1BytesTx;
    return this;
  }

   /**
   * Get p1BytesTx
   * @return p1BytesTx
  **/
  @Schema(description = "")
  public Float getP1BytesTx() {
    return p1BytesTx;
  }

  public void setP1BytesTx(Float p1BytesTx) {
    this.p1BytesTx = p1BytesTx;
  }

  public LinkStatsInner p1PacketsRx(Float p1PacketsRx) {
    this.p1PacketsRx = p1PacketsRx;
    return this;
  }

   /**
   * Get p1PacketsRx
   * @return p1PacketsRx
  **/
  @Schema(description = "")
  public Float getP1PacketsRx() {
    return p1PacketsRx;
  }

  public void setP1PacketsRx(Float p1PacketsRx) {
    this.p1PacketsRx = p1PacketsRx;
  }

  public LinkStatsInner p1PacketsTx(Float p1PacketsTx) {
    this.p1PacketsTx = p1PacketsTx;
    return this;
  }

   /**
   * Get p1PacketsTx
   * @return p1PacketsTx
  **/
  @Schema(description = "")
  public Float getP1PacketsTx() {
    return p1PacketsTx;
  }

  public void setP1PacketsTx(Float p1PacketsTx) {
    this.p1PacketsTx = p1PacketsTx;
  }

  public LinkStatsInner p2BytesRx(Float p2BytesRx) {
    this.p2BytesRx = p2BytesRx;
    return this;
  }

   /**
   * Get p2BytesRx
   * @return p2BytesRx
  **/
  @Schema(description = "")
  public Float getP2BytesRx() {
    return p2BytesRx;
  }

  public void setP2BytesRx(Float p2BytesRx) {
    this.p2BytesRx = p2BytesRx;
  }

  public LinkStatsInner p2BytesTx(Float p2BytesTx) {
    this.p2BytesTx = p2BytesTx;
    return this;
  }

   /**
   * Get p2BytesTx
   * @return p2BytesTx
  **/
  @Schema(description = "")
  public Float getP2BytesTx() {
    return p2BytesTx;
  }

  public void setP2BytesTx(Float p2BytesTx) {
    this.p2BytesTx = p2BytesTx;
  }

  public LinkStatsInner p2PacketsRx(Float p2PacketsRx) {
    this.p2PacketsRx = p2PacketsRx;
    return this;
  }

   /**
   * Get p2PacketsRx
   * @return p2PacketsRx
  **/
  @Schema(description = "")
  public Float getP2PacketsRx() {
    return p2PacketsRx;
  }

  public void setP2PacketsRx(Float p2PacketsRx) {
    this.p2PacketsRx = p2PacketsRx;
  }

  public LinkStatsInner p2PacketsTx(Float p2PacketsTx) {
    this.p2PacketsTx = p2PacketsTx;
    return this;
  }

   /**
   * Get p2PacketsTx
   * @return p2PacketsTx
  **/
  @Schema(description = "")
  public Float getP2PacketsTx() {
    return p2PacketsTx;
  }

  public void setP2PacketsTx(Float p2PacketsTx) {
    this.p2PacketsTx = p2PacketsTx;
  }

  public LinkStatsInner p3BytesRx(Float p3BytesRx) {
    this.p3BytesRx = p3BytesRx;
    return this;
  }

   /**
   * Get p3BytesRx
   * @return p3BytesRx
  **/
  @Schema(description = "")
  public Float getP3BytesRx() {
    return p3BytesRx;
  }

  public void setP3BytesRx(Float p3BytesRx) {
    this.p3BytesRx = p3BytesRx;
  }

  public LinkStatsInner p3BytesTx(Float p3BytesTx) {
    this.p3BytesTx = p3BytesTx;
    return this;
  }

   /**
   * Get p3BytesTx
   * @return p3BytesTx
  **/
  @Schema(description = "")
  public Float getP3BytesTx() {
    return p3BytesTx;
  }

  public void setP3BytesTx(Float p3BytesTx) {
    this.p3BytesTx = p3BytesTx;
  }

  public LinkStatsInner p3PacketsRx(Float p3PacketsRx) {
    this.p3PacketsRx = p3PacketsRx;
    return this;
  }

   /**
   * Get p3PacketsRx
   * @return p3PacketsRx
  **/
  @Schema(description = "")
  public Float getP3PacketsRx() {
    return p3PacketsRx;
  }

  public void setP3PacketsRx(Float p3PacketsRx) {
    this.p3PacketsRx = p3PacketsRx;
  }

  public LinkStatsInner p3PacketsTx(Float p3PacketsTx) {
    this.p3PacketsTx = p3PacketsTx;
    return this;
  }

   /**
   * Get p3PacketsTx
   * @return p3PacketsTx
  **/
  @Schema(description = "")
  public Float getP3PacketsTx() {
    return p3PacketsTx;
  }

  public void setP3PacketsTx(Float p3PacketsTx) {
    this.p3PacketsTx = p3PacketsTx;
  }

  public LinkStatsInner controlBytesRx(Float controlBytesRx) {
    this.controlBytesRx = controlBytesRx;
    return this;
  }

   /**
   * Get controlBytesRx
   * @return controlBytesRx
  **/
  @Schema(description = "")
  public Float getControlBytesRx() {
    return controlBytesRx;
  }

  public void setControlBytesRx(Float controlBytesRx) {
    this.controlBytesRx = controlBytesRx;
  }

  public LinkStatsInner controlBytesTx(Float controlBytesTx) {
    this.controlBytesTx = controlBytesTx;
    return this;
  }

   /**
   * Get controlBytesTx
   * @return controlBytesTx
  **/
  @Schema(description = "")
  public Float getControlBytesTx() {
    return controlBytesTx;
  }

  public void setControlBytesTx(Float controlBytesTx) {
    this.controlBytesTx = controlBytesTx;
  }

  public LinkStatsInner controlPacketsRx(Float controlPacketsRx) {
    this.controlPacketsRx = controlPacketsRx;
    return this;
  }

   /**
   * Get controlPacketsRx
   * @return controlPacketsRx
  **/
  @Schema(description = "")
  public Float getControlPacketsRx() {
    return controlPacketsRx;
  }

  public void setControlPacketsRx(Float controlPacketsRx) {
    this.controlPacketsRx = controlPacketsRx;
  }

  public LinkStatsInner controlPacketsTx(Float controlPacketsTx) {
    this.controlPacketsTx = controlPacketsTx;
    return this;
  }

   /**
   * Get controlPacketsTx
   * @return controlPacketsTx
  **/
  @Schema(description = "")
  public Float getControlPacketsTx() {
    return controlPacketsTx;
  }

  public void setControlPacketsTx(Float controlPacketsTx) {
    this.controlPacketsTx = controlPacketsTx;
  }

  public LinkStatsInner bpsOfBestPathRx(Float bpsOfBestPathRx) {
    this.bpsOfBestPathRx = bpsOfBestPathRx;
    return this;
  }

   /**
   * Get bpsOfBestPathRx
   * @return bpsOfBestPathRx
  **/
  @Schema(description = "")
  public Float getBpsOfBestPathRx() {
    return bpsOfBestPathRx;
  }

  public void setBpsOfBestPathRx(Float bpsOfBestPathRx) {
    this.bpsOfBestPathRx = bpsOfBestPathRx;
  }

  public LinkStatsInner bpsOfBestPathTx(Float bpsOfBestPathTx) {
    this.bpsOfBestPathTx = bpsOfBestPathTx;
    return this;
  }

   /**
   * Get bpsOfBestPathTx
   * @return bpsOfBestPathTx
  **/
  @Schema(description = "")
  public Float getBpsOfBestPathTx() {
    return bpsOfBestPathTx;
  }

  public void setBpsOfBestPathTx(Float bpsOfBestPathTx) {
    this.bpsOfBestPathTx = bpsOfBestPathTx;
  }

  public LinkStatsInner bestJitterMsRx(Float bestJitterMsRx) {
    this.bestJitterMsRx = bestJitterMsRx;
    return this;
  }

   /**
   * Get bestJitterMsRx
   * @return bestJitterMsRx
  **/
  @Schema(description = "")
  public Float getBestJitterMsRx() {
    return bestJitterMsRx;
  }

  public void setBestJitterMsRx(Float bestJitterMsRx) {
    this.bestJitterMsRx = bestJitterMsRx;
  }

  public LinkStatsInner bestJitterMsTx(Float bestJitterMsTx) {
    this.bestJitterMsTx = bestJitterMsTx;
    return this;
  }

   /**
   * Get bestJitterMsTx
   * @return bestJitterMsTx
  **/
  @Schema(description = "")
  public Float getBestJitterMsTx() {
    return bestJitterMsTx;
  }

  public void setBestJitterMsTx(Float bestJitterMsTx) {
    this.bestJitterMsTx = bestJitterMsTx;
  }

  public LinkStatsInner bestLatencyMsRx(Float bestLatencyMsRx) {
    this.bestLatencyMsRx = bestLatencyMsRx;
    return this;
  }

   /**
   * Get bestLatencyMsRx
   * @return bestLatencyMsRx
  **/
  @Schema(description = "")
  public Float getBestLatencyMsRx() {
    return bestLatencyMsRx;
  }

  public void setBestLatencyMsRx(Float bestLatencyMsRx) {
    this.bestLatencyMsRx = bestLatencyMsRx;
  }

  public LinkStatsInner bestLatencyMsTx(Float bestLatencyMsTx) {
    this.bestLatencyMsTx = bestLatencyMsTx;
    return this;
  }

   /**
   * Get bestLatencyMsTx
   * @return bestLatencyMsTx
  **/
  @Schema(description = "")
  public Float getBestLatencyMsTx() {
    return bestLatencyMsTx;
  }

  public void setBestLatencyMsTx(Float bestLatencyMsTx) {
    this.bestLatencyMsTx = bestLatencyMsTx;
  }

  public LinkStatsInner bestLossPctRx(Float bestLossPctRx) {
    this.bestLossPctRx = bestLossPctRx;
    return this;
  }

   /**
   * Get bestLossPctRx
   * @return bestLossPctRx
  **/
  @Schema(description = "")
  public Float getBestLossPctRx() {
    return bestLossPctRx;
  }

  public void setBestLossPctRx(Float bestLossPctRx) {
    this.bestLossPctRx = bestLossPctRx;
  }

  public LinkStatsInner bestLossPctTx(Float bestLossPctTx) {
    this.bestLossPctTx = bestLossPctTx;
    return this;
  }

   /**
   * Get bestLossPctTx
   * @return bestLossPctTx
  **/
  @Schema(description = "")
  public Float getBestLossPctTx() {
    return bestLossPctTx;
  }

  public void setBestLossPctTx(Float bestLossPctTx) {
    this.bestLossPctTx = bestLossPctTx;
  }

  public LinkStatsInner scoreTx(Float scoreTx) {
    this.scoreTx = scoreTx;
    return this;
  }

   /**
   * Get scoreTx
   * @return scoreTx
  **/
  @Schema(description = "")
  public Float getScoreTx() {
    return scoreTx;
  }

  public void setScoreTx(Float scoreTx) {
    this.scoreTx = scoreTx;
  }

  public LinkStatsInner scoreRx(Float scoreRx) {
    this.scoreRx = scoreRx;
    return this;
  }

   /**
   * Get scoreRx
   * @return scoreRx
  **/
  @Schema(description = "")
  public Float getScoreRx() {
    return scoreRx;
  }

  public void setScoreRx(Float scoreRx) {
    this.scoreRx = scoreRx;
  }

  public LinkStatsInner signalStrength(Float signalStrength) {
    this.signalStrength = signalStrength;
    return this;
  }

   /**
   * Get signalStrength
   * @return signalStrength
  **/
  @Schema(description = "")
  public Float getSignalStrength() {
    return signalStrength;
  }

  public void setSignalStrength(Float signalStrength) {
    this.signalStrength = signalStrength;
  }

  public LinkStatsInner name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @Schema(description = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkStatsInner linkStatsInner = (LinkStatsInner) o;
    return Objects.equals(this.link, linkStatsInner.link) &&
        Objects.equals(this.bytesTx, linkStatsInner.bytesTx) &&
        Objects.equals(this.bytesRx, linkStatsInner.bytesRx) &&
        Objects.equals(this.packetsTx, linkStatsInner.packetsTx) &&
        Objects.equals(this.packetsRx, linkStatsInner.packetsRx) &&
        Objects.equals(this.totalBytes, linkStatsInner.totalBytes) &&
        Objects.equals(this.totalPackets, linkStatsInner.totalPackets) &&
        Objects.equals(this.p1BytesRx, linkStatsInner.p1BytesRx) &&
        Objects.equals(this.p1BytesTx, linkStatsInner.p1BytesTx) &&
        Objects.equals(this.p1PacketsRx, linkStatsInner.p1PacketsRx) &&
        Objects.equals(this.p1PacketsTx, linkStatsInner.p1PacketsTx) &&
        Objects.equals(this.p2BytesRx, linkStatsInner.p2BytesRx) &&
        Objects.equals(this.p2BytesTx, linkStatsInner.p2BytesTx) &&
        Objects.equals(this.p2PacketsRx, linkStatsInner.p2PacketsRx) &&
        Objects.equals(this.p2PacketsTx, linkStatsInner.p2PacketsTx) &&
        Objects.equals(this.p3BytesRx, linkStatsInner.p3BytesRx) &&
        Objects.equals(this.p3BytesTx, linkStatsInner.p3BytesTx) &&
        Objects.equals(this.p3PacketsRx, linkStatsInner.p3PacketsRx) &&
        Objects.equals(this.p3PacketsTx, linkStatsInner.p3PacketsTx) &&
        Objects.equals(this.controlBytesRx, linkStatsInner.controlBytesRx) &&
        Objects.equals(this.controlBytesTx, linkStatsInner.controlBytesTx) &&
        Objects.equals(this.controlPacketsRx, linkStatsInner.controlPacketsRx) &&
        Objects.equals(this.controlPacketsTx, linkStatsInner.controlPacketsTx) &&
        Objects.equals(this.bpsOfBestPathRx, linkStatsInner.bpsOfBestPathRx) &&
        Objects.equals(this.bpsOfBestPathTx, linkStatsInner.bpsOfBestPathTx) &&
        Objects.equals(this.bestJitterMsRx, linkStatsInner.bestJitterMsRx) &&
        Objects.equals(this.bestJitterMsTx, linkStatsInner.bestJitterMsTx) &&
        Objects.equals(this.bestLatencyMsRx, linkStatsInner.bestLatencyMsRx) &&
        Objects.equals(this.bestLatencyMsTx, linkStatsInner.bestLatencyMsTx) &&
        Objects.equals(this.bestLossPctRx, linkStatsInner.bestLossPctRx) &&
        Objects.equals(this.bestLossPctTx, linkStatsInner.bestLossPctTx) &&
        Objects.equals(this.scoreTx, linkStatsInner.scoreTx) &&
        Objects.equals(this.scoreRx, linkStatsInner.scoreRx) &&
        Objects.equals(this.signalStrength, linkStatsInner.signalStrength) &&
        Objects.equals(this.name, linkStatsInner.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(link, bytesTx, bytesRx, packetsTx, packetsRx, totalBytes, totalPackets, p1BytesRx, p1BytesTx, p1PacketsRx, p1PacketsTx, p2BytesRx, p2BytesTx, p2PacketsRx, p2PacketsTx, p3BytesRx, p3BytesTx, p3PacketsRx, p3PacketsTx, controlBytesRx, controlBytesTx, controlPacketsRx, controlPacketsTx, bpsOfBestPathRx, bpsOfBestPathTx, bestJitterMsRx, bestJitterMsTx, bestLatencyMsRx, bestLatencyMsTx, bestLossPctRx, bestLossPctTx, scoreTx, scoreRx, signalStrength, name);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LinkStatsInner {\n");
    
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    bytesTx: ").append(toIndentedString(bytesTx)).append("\n");
    sb.append("    bytesRx: ").append(toIndentedString(bytesRx)).append("\n");
    sb.append("    packetsTx: ").append(toIndentedString(packetsTx)).append("\n");
    sb.append("    packetsRx: ").append(toIndentedString(packetsRx)).append("\n");
    sb.append("    totalBytes: ").append(toIndentedString(totalBytes)).append("\n");
    sb.append("    totalPackets: ").append(toIndentedString(totalPackets)).append("\n");
    sb.append("    p1BytesRx: ").append(toIndentedString(p1BytesRx)).append("\n");
    sb.append("    p1BytesTx: ").append(toIndentedString(p1BytesTx)).append("\n");
    sb.append("    p1PacketsRx: ").append(toIndentedString(p1PacketsRx)).append("\n");
    sb.append("    p1PacketsTx: ").append(toIndentedString(p1PacketsTx)).append("\n");
    sb.append("    p2BytesRx: ").append(toIndentedString(p2BytesRx)).append("\n");
    sb.append("    p2BytesTx: ").append(toIndentedString(p2BytesTx)).append("\n");
    sb.append("    p2PacketsRx: ").append(toIndentedString(p2PacketsRx)).append("\n");
    sb.append("    p2PacketsTx: ").append(toIndentedString(p2PacketsTx)).append("\n");
    sb.append("    p3BytesRx: ").append(toIndentedString(p3BytesRx)).append("\n");
    sb.append("    p3BytesTx: ").append(toIndentedString(p3BytesTx)).append("\n");
    sb.append("    p3PacketsRx: ").append(toIndentedString(p3PacketsRx)).append("\n");
    sb.append("    p3PacketsTx: ").append(toIndentedString(p3PacketsTx)).append("\n");
    sb.append("    controlBytesRx: ").append(toIndentedString(controlBytesRx)).append("\n");
    sb.append("    controlBytesTx: ").append(toIndentedString(controlBytesTx)).append("\n");
    sb.append("    controlPacketsRx: ").append(toIndentedString(controlPacketsRx)).append("\n");
    sb.append("    controlPacketsTx: ").append(toIndentedString(controlPacketsTx)).append("\n");
    sb.append("    bpsOfBestPathRx: ").append(toIndentedString(bpsOfBestPathRx)).append("\n");
    sb.append("    bpsOfBestPathTx: ").append(toIndentedString(bpsOfBestPathTx)).append("\n");
    sb.append("    bestJitterMsRx: ").append(toIndentedString(bestJitterMsRx)).append("\n");
    sb.append("    bestJitterMsTx: ").append(toIndentedString(bestJitterMsTx)).append("\n");
    sb.append("    bestLatencyMsRx: ").append(toIndentedString(bestLatencyMsRx)).append("\n");
    sb.append("    bestLatencyMsTx: ").append(toIndentedString(bestLatencyMsTx)).append("\n");
    sb.append("    bestLossPctRx: ").append(toIndentedString(bestLossPctRx)).append("\n");
    sb.append("    bestLossPctTx: ").append(toIndentedString(bestLossPctTx)).append("\n");
    sb.append("    scoreTx: ").append(toIndentedString(scoreTx)).append("\n");
    sb.append("    scoreRx: ").append(toIndentedString(scoreRx)).append("\n");
    sb.append("    signalStrength: ").append(toIndentedString(signalStrength)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
