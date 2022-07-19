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
 * DeviceSettingsNSDRoute
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class DeviceSettingsNSDRoute {
  @SerializedName("advertise")
  private Boolean advertise = null;

  @SerializedName("cidrIp")
  private String cidrIp = null;

  @SerializedName("cidrIpEnd")
  private String cidrIpEnd = null;

  @SerializedName("cidrIpStart")
  private String cidrIpStart = null;

  @SerializedName("cidrPrefix")
  private String cidrPrefix = null;

  @SerializedName("cost")
  private Integer cost = null;

  @SerializedName("destination")
  private String destination = null;

  @SerializedName("gatewayLogicalId")
  private String gatewayLogicalId = null;

  @SerializedName("gatewayName")
  private String gatewayName = null;

  @SerializedName("isStatic")
  private Boolean isStatic = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("netMask")
  private String netMask = null;

  @SerializedName("nsd")
  private String nsd = null;

  @SerializedName("nsdLogicalId")
  private String nsdLogicalId = null;

  /**
   * Gets or Sets nsdType
   */
  @JsonAdapter(NsdTypeEnum.Adapter.class)
  public enum NsdTypeEnum {
    NVSVIAEDGESERVICE("nvsViaEdgeService"),
    DATACENTER("dataCenter");

    private String value;

    NsdTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static NsdTypeEnum fromValue(String text) {
      for (NsdTypeEnum b : NsdTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<NsdTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final NsdTypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public NsdTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return NsdTypeEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("nsdType")
  private NsdTypeEnum nsdType = null;

  @SerializedName("preferred")
  private Boolean preferred = null;

  public DeviceSettingsNSDRoute advertise(Boolean advertise) {
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

  public DeviceSettingsNSDRoute cidrIp(String cidrIp) {
    this.cidrIp = cidrIp;
    return this;
  }

   /**
   * Get cidrIp
   * @return cidrIp
  **/
  @Schema(description = "")
  public String getCidrIp() {
    return cidrIp;
  }

  public void setCidrIp(String cidrIp) {
    this.cidrIp = cidrIp;
  }

  public DeviceSettingsNSDRoute cidrIpEnd(String cidrIpEnd) {
    this.cidrIpEnd = cidrIpEnd;
    return this;
  }

   /**
   * Get cidrIpEnd
   * @return cidrIpEnd
  **/
  @Schema(description = "")
  public String getCidrIpEnd() {
    return cidrIpEnd;
  }

  public void setCidrIpEnd(String cidrIpEnd) {
    this.cidrIpEnd = cidrIpEnd;
  }

  public DeviceSettingsNSDRoute cidrIpStart(String cidrIpStart) {
    this.cidrIpStart = cidrIpStart;
    return this;
  }

   /**
   * Get cidrIpStart
   * @return cidrIpStart
  **/
  @Schema(description = "")
  public String getCidrIpStart() {
    return cidrIpStart;
  }

  public void setCidrIpStart(String cidrIpStart) {
    this.cidrIpStart = cidrIpStart;
  }

  public DeviceSettingsNSDRoute cidrPrefix(String cidrPrefix) {
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

  public DeviceSettingsNSDRoute cost(Integer cost) {
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

  public DeviceSettingsNSDRoute destination(String destination) {
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

  public DeviceSettingsNSDRoute gatewayLogicalId(String gatewayLogicalId) {
    this.gatewayLogicalId = gatewayLogicalId;
    return this;
  }

   /**
   * Get gatewayLogicalId
   * @return gatewayLogicalId
  **/
  @Schema(description = "")
  public String getGatewayLogicalId() {
    return gatewayLogicalId;
  }

  public void setGatewayLogicalId(String gatewayLogicalId) {
    this.gatewayLogicalId = gatewayLogicalId;
  }

  public DeviceSettingsNSDRoute gatewayName(String gatewayName) {
    this.gatewayName = gatewayName;
    return this;
  }

   /**
   * Get gatewayName
   * @return gatewayName
  **/
  @Schema(description = "")
  public String getGatewayName() {
    return gatewayName;
  }

  public void setGatewayName(String gatewayName) {
    this.gatewayName = gatewayName;
  }

  public DeviceSettingsNSDRoute isStatic(Boolean isStatic) {
    this.isStatic = isStatic;
    return this;
  }

   /**
   * Get isStatic
   * @return isStatic
  **/
  @Schema(description = "")
  public Boolean isIsStatic() {
    return isStatic;
  }

  public void setIsStatic(Boolean isStatic) {
    this.isStatic = isStatic;
  }

  public DeviceSettingsNSDRoute name(String name) {
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

  public DeviceSettingsNSDRoute netMask(String netMask) {
    this.netMask = netMask;
    return this;
  }

   /**
   * Get netMask
   * @return netMask
  **/
  @Schema(description = "")
  public String getNetMask() {
    return netMask;
  }

  public void setNetMask(String netMask) {
    this.netMask = netMask;
  }

  public DeviceSettingsNSDRoute nsd(String nsd) {
    this.nsd = nsd;
    return this;
  }

   /**
   * Get nsd
   * @return nsd
  **/
  @Schema(description = "")
  public String getNsd() {
    return nsd;
  }

  public void setNsd(String nsd) {
    this.nsd = nsd;
  }

  public DeviceSettingsNSDRoute nsdLogicalId(String nsdLogicalId) {
    this.nsdLogicalId = nsdLogicalId;
    return this;
  }

   /**
   * Get nsdLogicalId
   * @return nsdLogicalId
  **/
  @Schema(description = "")
  public String getNsdLogicalId() {
    return nsdLogicalId;
  }

  public void setNsdLogicalId(String nsdLogicalId) {
    this.nsdLogicalId = nsdLogicalId;
  }

  public DeviceSettingsNSDRoute nsdType(NsdTypeEnum nsdType) {
    this.nsdType = nsdType;
    return this;
  }

   /**
   * Get nsdType
   * @return nsdType
  **/
  @Schema(description = "")
  public NsdTypeEnum getNsdType() {
    return nsdType;
  }

  public void setNsdType(NsdTypeEnum nsdType) {
    this.nsdType = nsdType;
  }

  public DeviceSettingsNSDRoute preferred(Boolean preferred) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceSettingsNSDRoute deviceSettingsNSDRoute = (DeviceSettingsNSDRoute) o;
    return Objects.equals(this.advertise, deviceSettingsNSDRoute.advertise) &&
        Objects.equals(this.cidrIp, deviceSettingsNSDRoute.cidrIp) &&
        Objects.equals(this.cidrIpEnd, deviceSettingsNSDRoute.cidrIpEnd) &&
        Objects.equals(this.cidrIpStart, deviceSettingsNSDRoute.cidrIpStart) &&
        Objects.equals(this.cidrPrefix, deviceSettingsNSDRoute.cidrPrefix) &&
        Objects.equals(this.cost, deviceSettingsNSDRoute.cost) &&
        Objects.equals(this.destination, deviceSettingsNSDRoute.destination) &&
        Objects.equals(this.gatewayLogicalId, deviceSettingsNSDRoute.gatewayLogicalId) &&
        Objects.equals(this.gatewayName, deviceSettingsNSDRoute.gatewayName) &&
        Objects.equals(this.isStatic, deviceSettingsNSDRoute.isStatic) &&
        Objects.equals(this.name, deviceSettingsNSDRoute.name) &&
        Objects.equals(this.netMask, deviceSettingsNSDRoute.netMask) &&
        Objects.equals(this.nsd, deviceSettingsNSDRoute.nsd) &&
        Objects.equals(this.nsdLogicalId, deviceSettingsNSDRoute.nsdLogicalId) &&
        Objects.equals(this.nsdType, deviceSettingsNSDRoute.nsdType) &&
        Objects.equals(this.preferred, deviceSettingsNSDRoute.preferred);
  }

  @Override
  public int hashCode() {
    return Objects.hash(advertise, cidrIp, cidrIpEnd, cidrIpStart, cidrPrefix, cost, destination, gatewayLogicalId, gatewayName, isStatic, name, netMask, nsd, nsdLogicalId, nsdType, preferred);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceSettingsNSDRoute {\n");
    
    sb.append("    advertise: ").append(toIndentedString(advertise)).append("\n");
    sb.append("    cidrIp: ").append(toIndentedString(cidrIp)).append("\n");
    sb.append("    cidrIpEnd: ").append(toIndentedString(cidrIpEnd)).append("\n");
    sb.append("    cidrIpStart: ").append(toIndentedString(cidrIpStart)).append("\n");
    sb.append("    cidrPrefix: ").append(toIndentedString(cidrPrefix)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    destination: ").append(toIndentedString(destination)).append("\n");
    sb.append("    gatewayLogicalId: ").append(toIndentedString(gatewayLogicalId)).append("\n");
    sb.append("    gatewayName: ").append(toIndentedString(gatewayName)).append("\n");
    sb.append("    isStatic: ").append(toIndentedString(isStatic)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    netMask: ").append(toIndentedString(netMask)).append("\n");
    sb.append("    nsd: ").append(toIndentedString(nsd)).append("\n");
    sb.append("    nsdLogicalId: ").append(toIndentedString(nsdLogicalId)).append("\n");
    sb.append("    nsdType: ").append(toIndentedString(nsdType)).append("\n");
    sb.append("    preferred: ").append(toIndentedString(preferred)).append("\n");
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
