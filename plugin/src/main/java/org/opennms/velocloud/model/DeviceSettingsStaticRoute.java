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
 * DeviceSettingsStaticRoute
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class DeviceSettingsStaticRoute {
  @SerializedName("destination")
  private String destination = null;

  @SerializedName("netmask")
  private String netmask = null;

  @SerializedName("sourceIp")
  private String sourceIp = null;

  @SerializedName("gateway")
  private String gateway = null;

  @SerializedName("cost")
  private Integer cost = null;

  @SerializedName("preferred")
  private Boolean preferred = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("cidrPrefix")
  private String cidrPrefix = null;

  @SerializedName("wanInterface")
  private String wanInterface = null;

  @SerializedName("icmpProbeLogicalId")
  private String icmpProbeLogicalId = null;

  @SerializedName("vlanId")
  private Integer vlanId = null;

  @SerializedName("advertise")
  private Boolean advertise = null;

  @SerializedName("subinterfaceId")
  private Integer subinterfaceId = null;

  public DeviceSettingsStaticRoute destination(String destination) {
    this.destination = destination;
    return this;
  }

   /**
   * Get destination
   * @return destination
  **/
  @Schema(description = "")
  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public DeviceSettingsStaticRoute netmask(String netmask) {
    this.netmask = netmask;
    return this;
  }

   /**
   * Get netmask
   * @return netmask
  **/
  @Schema(description = "")
  public String getNetmask() {
    return netmask;
  }

  public void setNetmask(String netmask) {
    this.netmask = netmask;
  }

  public DeviceSettingsStaticRoute sourceIp(String sourceIp) {
    this.sourceIp = sourceIp;
    return this;
  }

   /**
   * Get sourceIp
   * @return sourceIp
  **/
  @Schema(description = "")
  public String getSourceIp() {
    return sourceIp;
  }

  public void setSourceIp(String sourceIp) {
    this.sourceIp = sourceIp;
  }

  public DeviceSettingsStaticRoute gateway(String gateway) {
    this.gateway = gateway;
    return this;
  }

   /**
   * Get gateway
   * @return gateway
  **/
  @Schema(description = "")
  public String getGateway() {
    return gateway;
  }

  public void setGateway(String gateway) {
    this.gateway = gateway;
  }

  public DeviceSettingsStaticRoute cost(Integer cost) {
    this.cost = cost;
    return this;
  }

   /**
   * Get cost
   * @return cost
  **/
  @Schema(description = "")
  public Integer getCost() {
    return cost;
  }

  public void setCost(Integer cost) {
    this.cost = cost;
  }

  public DeviceSettingsStaticRoute preferred(Boolean preferred) {
    this.preferred = preferred;
    return this;
  }

   /**
   * Get preferred
   * @return preferred
  **/
  @Schema(description = "")
  public Boolean isPreferred() {
    return preferred;
  }

  public void setPreferred(Boolean preferred) {
    this.preferred = preferred;
  }

  public DeviceSettingsStaticRoute description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @Schema(description = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public DeviceSettingsStaticRoute cidrPrefix(String cidrPrefix) {
    this.cidrPrefix = cidrPrefix;
    return this;
  }

   /**
   * Get cidrPrefix
   * @return cidrPrefix
  **/
  @Schema(description = "")
  public String getCidrPrefix() {
    return cidrPrefix;
  }

  public void setCidrPrefix(String cidrPrefix) {
    this.cidrPrefix = cidrPrefix;
  }

  public DeviceSettingsStaticRoute wanInterface(String wanInterface) {
    this.wanInterface = wanInterface;
    return this;
  }

   /**
   * Get wanInterface
   * @return wanInterface
  **/
  @Schema(description = "")
  public String getWanInterface() {
    return wanInterface;
  }

  public void setWanInterface(String wanInterface) {
    this.wanInterface = wanInterface;
  }

  public DeviceSettingsStaticRoute icmpProbeLogicalId(String icmpProbeLogicalId) {
    this.icmpProbeLogicalId = icmpProbeLogicalId;
    return this;
  }

   /**
   * Get icmpProbeLogicalId
   * @return icmpProbeLogicalId
  **/
  @Schema(description = "")
  public String getIcmpProbeLogicalId() {
    return icmpProbeLogicalId;
  }

  public void setIcmpProbeLogicalId(String icmpProbeLogicalId) {
    this.icmpProbeLogicalId = icmpProbeLogicalId;
  }

  public DeviceSettingsStaticRoute vlanId(Integer vlanId) {
    this.vlanId = vlanId;
    return this;
  }

   /**
   * Get vlanId
   * @return vlanId
  **/
  @Schema(description = "")
  public Integer getVlanId() {
    return vlanId;
  }

  public void setVlanId(Integer vlanId) {
    this.vlanId = vlanId;
  }

  public DeviceSettingsStaticRoute advertise(Boolean advertise) {
    this.advertise = advertise;
    return this;
  }

   /**
   * Get advertise
   * @return advertise
  **/
  @Schema(description = "")
  public Boolean isAdvertise() {
    return advertise;
  }

  public void setAdvertise(Boolean advertise) {
    this.advertise = advertise;
  }

  public DeviceSettingsStaticRoute subinterfaceId(Integer subinterfaceId) {
    this.subinterfaceId = subinterfaceId;
    return this;
  }

   /**
   * Get subinterfaceId
   * @return subinterfaceId
  **/
  @Schema(description = "")
  public Integer getSubinterfaceId() {
    return subinterfaceId;
  }

  public void setSubinterfaceId(Integer subinterfaceId) {
    this.subinterfaceId = subinterfaceId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceSettingsStaticRoute deviceSettingsStaticRoute = (DeviceSettingsStaticRoute) o;
    return Objects.equals(this.destination, deviceSettingsStaticRoute.destination) &&
        Objects.equals(this.netmask, deviceSettingsStaticRoute.netmask) &&
        Objects.equals(this.sourceIp, deviceSettingsStaticRoute.sourceIp) &&
        Objects.equals(this.gateway, deviceSettingsStaticRoute.gateway) &&
        Objects.equals(this.cost, deviceSettingsStaticRoute.cost) &&
        Objects.equals(this.preferred, deviceSettingsStaticRoute.preferred) &&
        Objects.equals(this.description, deviceSettingsStaticRoute.description) &&
        Objects.equals(this.cidrPrefix, deviceSettingsStaticRoute.cidrPrefix) &&
        Objects.equals(this.wanInterface, deviceSettingsStaticRoute.wanInterface) &&
        Objects.equals(this.icmpProbeLogicalId, deviceSettingsStaticRoute.icmpProbeLogicalId) &&
        Objects.equals(this.vlanId, deviceSettingsStaticRoute.vlanId) &&
        Objects.equals(this.advertise, deviceSettingsStaticRoute.advertise) &&
        Objects.equals(this.subinterfaceId, deviceSettingsStaticRoute.subinterfaceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(destination, netmask, sourceIp, gateway, cost, preferred, description, cidrPrefix, wanInterface, icmpProbeLogicalId, vlanId, advertise, subinterfaceId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceSettingsStaticRoute {\n");
    
    sb.append("    destination: ").append(toIndentedString(destination)).append("\n");
    sb.append("    netmask: ").append(toIndentedString(netmask)).append("\n");
    sb.append("    sourceIp: ").append(toIndentedString(sourceIp)).append("\n");
    sb.append("    gateway: ").append(toIndentedString(gateway)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    preferred: ").append(toIndentedString(preferred)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    cidrPrefix: ").append(toIndentedString(cidrPrefix)).append("\n");
    sb.append("    wanInterface: ").append(toIndentedString(wanInterface)).append("\n");
    sb.append("    icmpProbeLogicalId: ").append(toIndentedString(icmpProbeLogicalId)).append("\n");
    sb.append("    vlanId: ").append(toIndentedString(vlanId)).append("\n");
    sb.append("    advertise: ").append(toIndentedString(advertise)).append("\n");
    sb.append("    subinterfaceId: ").append(toIndentedString(subinterfaceId)).append("\n");
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
