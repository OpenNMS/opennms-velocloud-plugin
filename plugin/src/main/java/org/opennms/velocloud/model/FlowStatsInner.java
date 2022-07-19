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
 * FlowStatsInner
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class FlowStatsInner {
  @SerializedName("bytesRx")
  private Float bytesRx = null;

  @SerializedName("bytesTx")
  private Float bytesTx = null;

  @SerializedName("totalBytes")
  private Float totalBytes = null;

  @SerializedName("packetsRx")
  private Float packetsRx = null;

  @SerializedName("packetsTx")
  private Float packetsTx = null;

  @SerializedName("totalPackets")
  private Float totalPackets = null;

  @SerializedName("flowCount")
  private Float flowCount = null;

  @SerializedName("link")
  private Object link = null;

  @SerializedName("clientDevice")
  private Object clientDevice = null;

  @SerializedName("application")
  private Object application = null;

  @SerializedName("applicationClass")
  private Object applicationClass = null;

  @SerializedName("destFQDN")
  private String destFQDN = null;

  @SerializedName("destIp")
  private String destIp = null;

  @SerializedName("destPort")
  private Float destPort = null;

  @SerializedName("destDomain")
  private String destDomain = null;

  public FlowStatsInner bytesRx(Float bytesRx) {
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

  public FlowStatsInner bytesTx(Float bytesTx) {
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

  public FlowStatsInner totalBytes(Float totalBytes) {
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

  public FlowStatsInner packetsRx(Float packetsRx) {
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

  public FlowStatsInner packetsTx(Float packetsTx) {
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

  public FlowStatsInner totalPackets(Float totalPackets) {
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

  public FlowStatsInner flowCount(Float flowCount) {
    this.flowCount = flowCount;
    return this;
  }

   /**
   * Get flowCount
   * @return flowCount
  **/
  @Schema(description = "")
  public Float getFlowCount() {
    return flowCount;
  }

  public void setFlowCount(Float flowCount) {
    this.flowCount = flowCount;
  }

  public FlowStatsInner link(Object link) {
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

  public FlowStatsInner clientDevice(Object clientDevice) {
    this.clientDevice = clientDevice;
    return this;
  }

   /**
   * Get clientDevice
   * @return clientDevice
  **/
  @Schema(description = "")
  public Object getClientDevice() {
    return clientDevice;
  }

  public void setClientDevice(Object clientDevice) {
    this.clientDevice = clientDevice;
  }

  public FlowStatsInner application(Object application) {
    this.application = application;
    return this;
  }

   /**
   * Get application
   * @return application
  **/
  @Schema(description = "")
  public Object getApplication() {
    return application;
  }

  public void setApplication(Object application) {
    this.application = application;
  }

  public FlowStatsInner applicationClass(Object applicationClass) {
    this.applicationClass = applicationClass;
    return this;
  }

   /**
   * Get applicationClass
   * @return applicationClass
  **/
  @Schema(description = "")
  public Object getApplicationClass() {
    return applicationClass;
  }

  public void setApplicationClass(Object applicationClass) {
    this.applicationClass = applicationClass;
  }

  public FlowStatsInner destFQDN(String destFQDN) {
    this.destFQDN = destFQDN;
    return this;
  }

   /**
   * Get destFQDN
   * @return destFQDN
  **/
  @Schema(description = "")
  public String getDestFQDN() {
    return destFQDN;
  }

  public void setDestFQDN(String destFQDN) {
    this.destFQDN = destFQDN;
  }

  public FlowStatsInner destIp(String destIp) {
    this.destIp = destIp;
    return this;
  }

   /**
   * Get destIp
   * @return destIp
  **/
  @Schema(description = "")
  public String getDestIp() {
    return destIp;
  }

  public void setDestIp(String destIp) {
    this.destIp = destIp;
  }

  public FlowStatsInner destPort(Float destPort) {
    this.destPort = destPort;
    return this;
  }

   /**
   * Get destPort
   * @return destPort
  **/
  @Schema(description = "")
  public Float getDestPort() {
    return destPort;
  }

  public void setDestPort(Float destPort) {
    this.destPort = destPort;
  }

  public FlowStatsInner destDomain(String destDomain) {
    this.destDomain = destDomain;
    return this;
  }

   /**
   * Get destDomain
   * @return destDomain
  **/
  @Schema(description = "")
  public String getDestDomain() {
    return destDomain;
  }

  public void setDestDomain(String destDomain) {
    this.destDomain = destDomain;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FlowStatsInner flowStatsInner = (FlowStatsInner) o;
    return Objects.equals(this.bytesRx, flowStatsInner.bytesRx) &&
        Objects.equals(this.bytesTx, flowStatsInner.bytesTx) &&
        Objects.equals(this.totalBytes, flowStatsInner.totalBytes) &&
        Objects.equals(this.packetsRx, flowStatsInner.packetsRx) &&
        Objects.equals(this.packetsTx, flowStatsInner.packetsTx) &&
        Objects.equals(this.totalPackets, flowStatsInner.totalPackets) &&
        Objects.equals(this.flowCount, flowStatsInner.flowCount) &&
        Objects.equals(this.link, flowStatsInner.link) &&
        Objects.equals(this.clientDevice, flowStatsInner.clientDevice) &&
        Objects.equals(this.application, flowStatsInner.application) &&
        Objects.equals(this.applicationClass, flowStatsInner.applicationClass) &&
        Objects.equals(this.destFQDN, flowStatsInner.destFQDN) &&
        Objects.equals(this.destIp, flowStatsInner.destIp) &&
        Objects.equals(this.destPort, flowStatsInner.destPort) &&
        Objects.equals(this.destDomain, flowStatsInner.destDomain);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bytesRx, bytesTx, totalBytes, packetsRx, packetsTx, totalPackets, flowCount, link, clientDevice, application, applicationClass, destFQDN, destIp, destPort, destDomain);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FlowStatsInner {\n");
    
    sb.append("    bytesRx: ").append(toIndentedString(bytesRx)).append("\n");
    sb.append("    bytesTx: ").append(toIndentedString(bytesTx)).append("\n");
    sb.append("    totalBytes: ").append(toIndentedString(totalBytes)).append("\n");
    sb.append("    packetsRx: ").append(toIndentedString(packetsRx)).append("\n");
    sb.append("    packetsTx: ").append(toIndentedString(packetsTx)).append("\n");
    sb.append("    totalPackets: ").append(toIndentedString(totalPackets)).append("\n");
    sb.append("    flowCount: ").append(toIndentedString(flowCount)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    clientDevice: ").append(toIndentedString(clientDevice)).append("\n");
    sb.append("    application: ").append(toIndentedString(application)).append("\n");
    sb.append("    applicationClass: ").append(toIndentedString(applicationClass)).append("\n");
    sb.append("    destFQDN: ").append(toIndentedString(destFQDN)).append("\n");
    sb.append("    destIp: ").append(toIndentedString(destIp)).append("\n");
    sb.append("    destPort: ").append(toIndentedString(destPort)).append("\n");
    sb.append("    destDomain: ").append(toIndentedString(destDomain)).append("\n");
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
