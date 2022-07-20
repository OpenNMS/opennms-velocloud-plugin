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
import org.opennms.velocloud.client.model.DeviceSettingsRoutedInterfaceOSPFFilter;
/**
 * DeviceSettingsRoutedInterfaceOSPF
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")
public class DeviceSettingsRoutedInterfaceOSPF {
  @JsonProperty("area")
  private AnyOfDeviceSettingsRoutedInterfaceOSPFArea area = null;

  @JsonProperty("authentication")
  private Boolean authentication = null;

  @JsonProperty("authId")
  private Integer authId = null;

  @JsonProperty("authPassphrase")
  private String authPassphrase = null;

  @JsonProperty("cost")
  private Integer cost = null;

  @JsonProperty("deadTimer")
  private Integer deadTimer = null;

  @JsonProperty("mode")
  private String mode = null;

  @JsonProperty("enabled")
  private Boolean enabled = null;

  @JsonProperty("exclusionRoutes")
  private List<Object> exclusionRoutes = null;

  @JsonProperty("helloTimer")
  private Integer helloTimer = null;

  @JsonProperty("inboundRouteLearning")
  private DeviceSettingsRoutedInterfaceOSPFFilter inboundRouteLearning = null;

  @JsonProperty("md5Authentication")
  private Boolean md5Authentication = null;

  @JsonProperty("MTU")
  private Integer MTU = null;

  @JsonProperty("outboundRouteAdvertisement")
  private DeviceSettingsRoutedInterfaceOSPFFilter outboundRouteAdvertisement = null;

  @JsonProperty("passive")
  private Boolean passive = null;

  @JsonProperty("vlanId")
  private Integer vlanId = null;

  public DeviceSettingsRoutedInterfaceOSPF area(AnyOfDeviceSettingsRoutedInterfaceOSPFArea area) {
    this.area = area;
    return this;
  }

   /**
   * Get area
   * @return area
  **/
  @Schema(description = "")
  public AnyOfDeviceSettingsRoutedInterfaceOSPFArea getArea() {
    return area;
  }

  public void setArea(AnyOfDeviceSettingsRoutedInterfaceOSPFArea area) {
    this.area = area;
  }

  public DeviceSettingsRoutedInterfaceOSPF authentication(Boolean authentication) {
    this.authentication = authentication;
    return this;
  }

   /**
   * Get authentication
   * @return authentication
  **/
  @Schema(description = "")
  public Boolean isAuthentication() {
    return authentication;
  }

  public void setAuthentication(Boolean authentication) {
    this.authentication = authentication;
  }

  public DeviceSettingsRoutedInterfaceOSPF authId(Integer authId) {
    this.authId = authId;
    return this;
  }

   /**
   * Get authId
   * @return authId
  **/
  @Schema(description = "")
  public Integer getAuthId() {
    return authId;
  }

  public void setAuthId(Integer authId) {
    this.authId = authId;
  }

  public DeviceSettingsRoutedInterfaceOSPF authPassphrase(String authPassphrase) {
    this.authPassphrase = authPassphrase;
    return this;
  }

   /**
   * Get authPassphrase
   * @return authPassphrase
  **/
  @Schema(description = "")
  public String getAuthPassphrase() {
    return authPassphrase;
  }

  public void setAuthPassphrase(String authPassphrase) {
    this.authPassphrase = authPassphrase;
  }

  public DeviceSettingsRoutedInterfaceOSPF cost(Integer cost) {
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

  public DeviceSettingsRoutedInterfaceOSPF deadTimer(Integer deadTimer) {
    this.deadTimer = deadTimer;
    return this;
  }

   /**
   * Get deadTimer
   * @return deadTimer
  **/
  @Schema(description = "")
  public Integer getDeadTimer() {
    return deadTimer;
  }

  public void setDeadTimer(Integer deadTimer) {
    this.deadTimer = deadTimer;
  }

  public DeviceSettingsRoutedInterfaceOSPF mode(String mode) {
    this.mode = mode;
    return this;
  }

   /**
   * Get mode
   * @return mode
  **/
  @Schema(description = "")
  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public DeviceSettingsRoutedInterfaceOSPF enabled(Boolean enabled) {
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

  public DeviceSettingsRoutedInterfaceOSPF exclusionRoutes(List<Object> exclusionRoutes) {
    this.exclusionRoutes = exclusionRoutes;
    return this;
  }

  public DeviceSettingsRoutedInterfaceOSPF addExclusionRoutesItem(Object exclusionRoutesItem) {
    if (this.exclusionRoutes == null) {
      this.exclusionRoutes = new ArrayList<>();
    }
    this.exclusionRoutes.add(exclusionRoutesItem);
    return this;
  }

   /**
   * Get exclusionRoutes
   * @return exclusionRoutes
  **/
  @Schema(description = "")
  public List<Object> getExclusionRoutes() {
    return exclusionRoutes;
  }

  public void setExclusionRoutes(List<Object> exclusionRoutes) {
    this.exclusionRoutes = exclusionRoutes;
  }

  public DeviceSettingsRoutedInterfaceOSPF helloTimer(Integer helloTimer) {
    this.helloTimer = helloTimer;
    return this;
  }

   /**
   * Get helloTimer
   * @return helloTimer
  **/
  @Schema(description = "")
  public Integer getHelloTimer() {
    return helloTimer;
  }

  public void setHelloTimer(Integer helloTimer) {
    this.helloTimer = helloTimer;
  }

  public DeviceSettingsRoutedInterfaceOSPF inboundRouteLearning(DeviceSettingsRoutedInterfaceOSPFFilter inboundRouteLearning) {
    this.inboundRouteLearning = inboundRouteLearning;
    return this;
  }

   /**
   * Get inboundRouteLearning
   * @return inboundRouteLearning
  **/
  @Schema(description = "")
  public DeviceSettingsRoutedInterfaceOSPFFilter getInboundRouteLearning() {
    return inboundRouteLearning;
  }

  public void setInboundRouteLearning(DeviceSettingsRoutedInterfaceOSPFFilter inboundRouteLearning) {
    this.inboundRouteLearning = inboundRouteLearning;
  }

  public DeviceSettingsRoutedInterfaceOSPF md5Authentication(Boolean md5Authentication) {
    this.md5Authentication = md5Authentication;
    return this;
  }

   /**
   * Get md5Authentication
   * @return md5Authentication
  **/
  @Schema(description = "")
  public Boolean isMd5Authentication() {
    return md5Authentication;
  }

  public void setMd5Authentication(Boolean md5Authentication) {
    this.md5Authentication = md5Authentication;
  }

  public DeviceSettingsRoutedInterfaceOSPF MTU(Integer MTU) {
    this.MTU = MTU;
    return this;
  }

   /**
   * Get MTU
   * @return MTU
  **/
  @Schema(description = "")
  public Integer getMTU() {
    return MTU;
  }

  public void setMTU(Integer MTU) {
    this.MTU = MTU;
  }

  public DeviceSettingsRoutedInterfaceOSPF outboundRouteAdvertisement(DeviceSettingsRoutedInterfaceOSPFFilter outboundRouteAdvertisement) {
    this.outboundRouteAdvertisement = outboundRouteAdvertisement;
    return this;
  }

   /**
   * Get outboundRouteAdvertisement
   * @return outboundRouteAdvertisement
  **/
  @Schema(description = "")
  public DeviceSettingsRoutedInterfaceOSPFFilter getOutboundRouteAdvertisement() {
    return outboundRouteAdvertisement;
  }

  public void setOutboundRouteAdvertisement(DeviceSettingsRoutedInterfaceOSPFFilter outboundRouteAdvertisement) {
    this.outboundRouteAdvertisement = outboundRouteAdvertisement;
  }

  public DeviceSettingsRoutedInterfaceOSPF passive(Boolean passive) {
    this.passive = passive;
    return this;
  }

   /**
   * Get passive
   * @return passive
  **/
  @Schema(description = "")
  public Boolean isPassive() {
    return passive;
  }

  public void setPassive(Boolean passive) {
    this.passive = passive;
  }

  public DeviceSettingsRoutedInterfaceOSPF vlanId(Integer vlanId) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceSettingsRoutedInterfaceOSPF deviceSettingsRoutedInterfaceOSPF = (DeviceSettingsRoutedInterfaceOSPF) o;
    return Objects.equals(this.area, deviceSettingsRoutedInterfaceOSPF.area) &&
        Objects.equals(this.authentication, deviceSettingsRoutedInterfaceOSPF.authentication) &&
        Objects.equals(this.authId, deviceSettingsRoutedInterfaceOSPF.authId) &&
        Objects.equals(this.authPassphrase, deviceSettingsRoutedInterfaceOSPF.authPassphrase) &&
        Objects.equals(this.cost, deviceSettingsRoutedInterfaceOSPF.cost) &&
        Objects.equals(this.deadTimer, deviceSettingsRoutedInterfaceOSPF.deadTimer) &&
        Objects.equals(this.mode, deviceSettingsRoutedInterfaceOSPF.mode) &&
        Objects.equals(this.enabled, deviceSettingsRoutedInterfaceOSPF.enabled) &&
        Objects.equals(this.exclusionRoutes, deviceSettingsRoutedInterfaceOSPF.exclusionRoutes) &&
        Objects.equals(this.helloTimer, deviceSettingsRoutedInterfaceOSPF.helloTimer) &&
        Objects.equals(this.inboundRouteLearning, deviceSettingsRoutedInterfaceOSPF.inboundRouteLearning) &&
        Objects.equals(this.md5Authentication, deviceSettingsRoutedInterfaceOSPF.md5Authentication) &&
        Objects.equals(this.MTU, deviceSettingsRoutedInterfaceOSPF.MTU) &&
        Objects.equals(this.outboundRouteAdvertisement, deviceSettingsRoutedInterfaceOSPF.outboundRouteAdvertisement) &&
        Objects.equals(this.passive, deviceSettingsRoutedInterfaceOSPF.passive) &&
        Objects.equals(this.vlanId, deviceSettingsRoutedInterfaceOSPF.vlanId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(area, authentication, authId, authPassphrase, cost, deadTimer, mode, enabled, exclusionRoutes, helloTimer, inboundRouteLearning, md5Authentication, MTU, outboundRouteAdvertisement, passive, vlanId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceSettingsRoutedInterfaceOSPF {\n");
    
    sb.append("    area: ").append(toIndentedString(area)).append("\n");
    sb.append("    authentication: ").append(toIndentedString(authentication)).append("\n");
    sb.append("    authId: ").append(toIndentedString(authId)).append("\n");
    sb.append("    authPassphrase: ").append(toIndentedString(authPassphrase)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    deadTimer: ").append(toIndentedString(deadTimer)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    exclusionRoutes: ").append(toIndentedString(exclusionRoutes)).append("\n");
    sb.append("    helloTimer: ").append(toIndentedString(helloTimer)).append("\n");
    sb.append("    inboundRouteLearning: ").append(toIndentedString(inboundRouteLearning)).append("\n");
    sb.append("    md5Authentication: ").append(toIndentedString(md5Authentication)).append("\n");
    sb.append("    MTU: ").append(toIndentedString(MTU)).append("\n");
    sb.append("    outboundRouteAdvertisement: ").append(toIndentedString(outboundRouteAdvertisement)).append("\n");
    sb.append("    passive: ").append(toIndentedString(passive)).append("\n");
    sb.append("    vlanId: ").append(toIndentedString(vlanId)).append("\n");
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
