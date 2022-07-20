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

package org.opennms.velocloud.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.opennms.velocloud.client.model.DeviceSettingsBGPDefaultRoute;
import org.opennms.velocloud.client.model.DeviceSettingsBGPFilter;
import org.opennms.velocloud.client.model.DeviceSettingsBGPNeighbor;
import org.opennms.velocloud.client.model.DeviceSettingsBGPNetwork;
import org.opennms.velocloud.client.model.DeviceSettingsBGPOSPFRedistribution;
import org.opennms.velocloud.client.model.DeviceSettingsBGPV6Detail;
/**
 * DeviceSettingsBGP
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")
public class DeviceSettingsBGP {
  @JsonProperty("enabled")
  private Boolean enabled = null;

  @JsonProperty("override")
  private Boolean override = null;

  @JsonProperty("asn")
  private String asn = null;

  @JsonProperty("ASN")
  private String ASN = null;

  @JsonProperty("connectedRoutes")
  private Boolean connectedRoutes = null;

  @JsonProperty("defaultRoute")
  private DeviceSettingsBGPDefaultRoute defaultRoute = null;

  @JsonProperty("disableASPathCarryOver")
  private Boolean disableASPathCarryOver = null;

  @JsonProperty("filters")
  private List<DeviceSettingsBGPFilter> filters = null;

  @JsonProperty("holdtime")
  private String holdtime = null;

  @JsonProperty("isEdge")
  private Boolean isEdge = null;

  @JsonProperty("keepalive")
  private String keepalive = null;

  @JsonProperty("neighbors")
  private List<DeviceSettingsBGPNeighbor> neighbors = null;

  @JsonProperty("networks")
  private List<DeviceSettingsBGPNetwork> networks = null;

  @JsonProperty("ospf")
  private DeviceSettingsBGPOSPFRedistribution ospf = null;

  @JsonProperty("overlayPrefix")
  private Boolean overlayPrefix = null;

  @JsonProperty("propagateUplink")
  private Boolean propagateUplink = null;

  @JsonProperty("routerId")
  private String routerId = null;

  @JsonProperty("uplinkCommunity")
  private String uplinkCommunity = null;

  @JsonProperty("v6Detail")
  private DeviceSettingsBGPV6Detail v6Detail = null;

  public DeviceSettingsBGP enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

   /**
   * Get enabled
   * @return enabled
  **/
  @Schema(description = "")
  public Boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public DeviceSettingsBGP override(Boolean override) {
    this.override = override;
    return this;
  }

   /**
   * Get override
   * @return override
  **/
  @Schema(description = "")
  public Boolean isOverride() {
    return override;
  }

  public void setOverride(Boolean override) {
    this.override = override;
  }

  public DeviceSettingsBGP asn(String asn) {
    this.asn = asn;
    return this;
  }

   /**
   * Get asn
   * @return asn
  **/
  @Schema(description = "")
  public String getAsn() {
    return asn;
  }

  public void setAsn(String asn) {
    this.asn = asn;
  }

  public DeviceSettingsBGP ASN(String ASN) {
    this.ASN = ASN;
    return this;
  }

   /**
   * Get ASN
   * @return ASN
  **/
  @Schema(description = "")
  public String getASN() {
    return ASN;
  }

  public void setASN(String ASN) {
    this.ASN = ASN;
  }

  public DeviceSettingsBGP connectedRoutes(Boolean connectedRoutes) {
    this.connectedRoutes = connectedRoutes;
    return this;
  }

   /**
   * Get connectedRoutes
   * @return connectedRoutes
  **/
  @Schema(description = "")
  public Boolean isConnectedRoutes() {
    return connectedRoutes;
  }

  public void setConnectedRoutes(Boolean connectedRoutes) {
    this.connectedRoutes = connectedRoutes;
  }

  public DeviceSettingsBGP defaultRoute(DeviceSettingsBGPDefaultRoute defaultRoute) {
    this.defaultRoute = defaultRoute;
    return this;
  }

   /**
   * Get defaultRoute
   * @return defaultRoute
  **/
  @Schema(description = "")
  public DeviceSettingsBGPDefaultRoute getDefaultRoute() {
    return defaultRoute;
  }

  public void setDefaultRoute(DeviceSettingsBGPDefaultRoute defaultRoute) {
    this.defaultRoute = defaultRoute;
  }

  public DeviceSettingsBGP disableASPathCarryOver(Boolean disableASPathCarryOver) {
    this.disableASPathCarryOver = disableASPathCarryOver;
    return this;
  }

   /**
   * Get disableASPathCarryOver
   * @return disableASPathCarryOver
  **/
  @Schema(description = "")
  public Boolean isDisableASPathCarryOver() {
    return disableASPathCarryOver;
  }

  public void setDisableASPathCarryOver(Boolean disableASPathCarryOver) {
    this.disableASPathCarryOver = disableASPathCarryOver;
  }

  public DeviceSettingsBGP filters(List<DeviceSettingsBGPFilter> filters) {
    this.filters = filters;
    return this;
  }

  public DeviceSettingsBGP addFiltersItem(DeviceSettingsBGPFilter filtersItem) {
    if (this.filters == null) {
      this.filters = new ArrayList<>();
    }
    this.filters.add(filtersItem);
    return this;
  }

   /**
   * Get filters
   * @return filters
  **/
  @Schema(description = "")
  public List<DeviceSettingsBGPFilter> getFilters() {
    return filters;
  }

  public void setFilters(List<DeviceSettingsBGPFilter> filters) {
    this.filters = filters;
  }

  public DeviceSettingsBGP holdtime(String holdtime) {
    this.holdtime = holdtime;
    return this;
  }

   /**
   * Get holdtime
   * @return holdtime
  **/
  @Schema(description = "")
  public String getHoldtime() {
    return holdtime;
  }

  public void setHoldtime(String holdtime) {
    this.holdtime = holdtime;
  }

  public DeviceSettingsBGP isEdge(Boolean isEdge) {
    this.isEdge = isEdge;
    return this;
  }

   /**
   * Get isEdge
   * @return isEdge
  **/
  @Schema(description = "")
  public Boolean isIsEdge() {
    return isEdge;
  }

  public void setIsEdge(Boolean isEdge) {
    this.isEdge = isEdge;
  }

  public DeviceSettingsBGP keepalive(String keepalive) {
    this.keepalive = keepalive;
    return this;
  }

   /**
   * Get keepalive
   * @return keepalive
  **/
  @Schema(description = "")
  public String getKeepalive() {
    return keepalive;
  }

  public void setKeepalive(String keepalive) {
    this.keepalive = keepalive;
  }

  public DeviceSettingsBGP neighbors(List<DeviceSettingsBGPNeighbor> neighbors) {
    this.neighbors = neighbors;
    return this;
  }

  public DeviceSettingsBGP addNeighborsItem(DeviceSettingsBGPNeighbor neighborsItem) {
    if (this.neighbors == null) {
      this.neighbors = new ArrayList<>();
    }
    this.neighbors.add(neighborsItem);
    return this;
  }

   /**
   * Get neighbors
   * @return neighbors
  **/
  @Schema(description = "")
  public List<DeviceSettingsBGPNeighbor> getNeighbors() {
    return neighbors;
  }

  public void setNeighbors(List<DeviceSettingsBGPNeighbor> neighbors) {
    this.neighbors = neighbors;
  }

  public DeviceSettingsBGP networks(List<DeviceSettingsBGPNetwork> networks) {
    this.networks = networks;
    return this;
  }

  public DeviceSettingsBGP addNetworksItem(DeviceSettingsBGPNetwork networksItem) {
    if (this.networks == null) {
      this.networks = new ArrayList<>();
    }
    this.networks.add(networksItem);
    return this;
  }

   /**
   * Get networks
   * @return networks
  **/
  @Schema(description = "")
  public List<DeviceSettingsBGPNetwork> getNetworks() {
    return networks;
  }

  public void setNetworks(List<DeviceSettingsBGPNetwork> networks) {
    this.networks = networks;
  }

  public DeviceSettingsBGP ospf(DeviceSettingsBGPOSPFRedistribution ospf) {
    this.ospf = ospf;
    return this;
  }

   /**
   * Get ospf
   * @return ospf
  **/
  @Schema(description = "")
  public DeviceSettingsBGPOSPFRedistribution getOspf() {
    return ospf;
  }

  public void setOspf(DeviceSettingsBGPOSPFRedistribution ospf) {
    this.ospf = ospf;
  }

  public DeviceSettingsBGP overlayPrefix(Boolean overlayPrefix) {
    this.overlayPrefix = overlayPrefix;
    return this;
  }

   /**
   * Get overlayPrefix
   * @return overlayPrefix
  **/
  @Schema(description = "")
  public Boolean isOverlayPrefix() {
    return overlayPrefix;
  }

  public void setOverlayPrefix(Boolean overlayPrefix) {
    this.overlayPrefix = overlayPrefix;
  }

  public DeviceSettingsBGP propagateUplink(Boolean propagateUplink) {
    this.propagateUplink = propagateUplink;
    return this;
  }

   /**
   * Get propagateUplink
   * @return propagateUplink
  **/
  @Schema(description = "")
  public Boolean isPropagateUplink() {
    return propagateUplink;
  }

  public void setPropagateUplink(Boolean propagateUplink) {
    this.propagateUplink = propagateUplink;
  }

  public DeviceSettingsBGP routerId(String routerId) {
    this.routerId = routerId;
    return this;
  }

   /**
   * Get routerId
   * @return routerId
  **/
  @Schema(description = "")
  public String getRouterId() {
    return routerId;
  }

  public void setRouterId(String routerId) {
    this.routerId = routerId;
  }

  public DeviceSettingsBGP uplinkCommunity(String uplinkCommunity) {
    this.uplinkCommunity = uplinkCommunity;
    return this;
  }

   /**
   * Get uplinkCommunity
   * @return uplinkCommunity
  **/
  @Schema(description = "")
  public String getUplinkCommunity() {
    return uplinkCommunity;
  }

  public void setUplinkCommunity(String uplinkCommunity) {
    this.uplinkCommunity = uplinkCommunity;
  }

  public DeviceSettingsBGP v6Detail(DeviceSettingsBGPV6Detail v6Detail) {
    this.v6Detail = v6Detail;
    return this;
  }

   /**
   * Get v6Detail
   * @return v6Detail
  **/
  @Schema(description = "")
  public DeviceSettingsBGPV6Detail getV6Detail() {
    return v6Detail;
  }

  public void setV6Detail(DeviceSettingsBGPV6Detail v6Detail) {
    this.v6Detail = v6Detail;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceSettingsBGP deviceSettingsBGP = (DeviceSettingsBGP) o;
    return Objects.equals(this.enabled, deviceSettingsBGP.enabled) &&
        Objects.equals(this.override, deviceSettingsBGP.override) &&
        Objects.equals(this.asn, deviceSettingsBGP.asn) &&
        Objects.equals(this.ASN, deviceSettingsBGP.ASN) &&
        Objects.equals(this.connectedRoutes, deviceSettingsBGP.connectedRoutes) &&
        Objects.equals(this.defaultRoute, deviceSettingsBGP.defaultRoute) &&
        Objects.equals(this.disableASPathCarryOver, deviceSettingsBGP.disableASPathCarryOver) &&
        Objects.equals(this.filters, deviceSettingsBGP.filters) &&
        Objects.equals(this.holdtime, deviceSettingsBGP.holdtime) &&
        Objects.equals(this.isEdge, deviceSettingsBGP.isEdge) &&
        Objects.equals(this.keepalive, deviceSettingsBGP.keepalive) &&
        Objects.equals(this.neighbors, deviceSettingsBGP.neighbors) &&
        Objects.equals(this.networks, deviceSettingsBGP.networks) &&
        Objects.equals(this.ospf, deviceSettingsBGP.ospf) &&
        Objects.equals(this.overlayPrefix, deviceSettingsBGP.overlayPrefix) &&
        Objects.equals(this.propagateUplink, deviceSettingsBGP.propagateUplink) &&
        Objects.equals(this.routerId, deviceSettingsBGP.routerId) &&
        Objects.equals(this.uplinkCommunity, deviceSettingsBGP.uplinkCommunity) &&
        Objects.equals(this.v6Detail, deviceSettingsBGP.v6Detail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled, override, asn, ASN, connectedRoutes, defaultRoute, disableASPathCarryOver, filters, holdtime, isEdge, keepalive, neighbors, networks, ospf, overlayPrefix, propagateUplink, routerId, uplinkCommunity, v6Detail);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceSettingsBGP {\n");
    
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    override: ").append(toIndentedString(override)).append("\n");
    sb.append("    asn: ").append(toIndentedString(asn)).append("\n");
    sb.append("    ASN: ").append(toIndentedString(ASN)).append("\n");
    sb.append("    connectedRoutes: ").append(toIndentedString(connectedRoutes)).append("\n");
    sb.append("    defaultRoute: ").append(toIndentedString(defaultRoute)).append("\n");
    sb.append("    disableASPathCarryOver: ").append(toIndentedString(disableASPathCarryOver)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
    sb.append("    holdtime: ").append(toIndentedString(holdtime)).append("\n");
    sb.append("    isEdge: ").append(toIndentedString(isEdge)).append("\n");
    sb.append("    keepalive: ").append(toIndentedString(keepalive)).append("\n");
    sb.append("    neighbors: ").append(toIndentedString(neighbors)).append("\n");
    sb.append("    networks: ").append(toIndentedString(networks)).append("\n");
    sb.append("    ospf: ").append(toIndentedString(ospf)).append("\n");
    sb.append("    overlayPrefix: ").append(toIndentedString(overlayPrefix)).append("\n");
    sb.append("    propagateUplink: ").append(toIndentedString(propagateUplink)).append("\n");
    sb.append("    routerId: ").append(toIndentedString(routerId)).append("\n");
    sb.append("    uplinkCommunity: ").append(toIndentedString(uplinkCommunity)).append("\n");
    sb.append("    v6Detail: ").append(toIndentedString(v6Detail)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
