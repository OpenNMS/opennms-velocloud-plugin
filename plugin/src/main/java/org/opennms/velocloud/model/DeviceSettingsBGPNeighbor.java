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
import org.opennms.velocloud.model.DeviceSettingsBGPFilterSet;
/**
 * DeviceSettingsBGPNeighbor
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class DeviceSettingsBGPNeighbor {
  @SerializedName("neighborAS")
  private String neighborAS = null;

  @SerializedName("neighborIp")
  private String neighborIp = null;

  /**
   * Gets or Sets neighborTag
   */
  @JsonAdapter(NeighborTagEnum.Adapter.class)
  public enum NeighborTagEnum {
    UPLINK("UPLINK");

    private String value;

    NeighborTagEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static NeighborTagEnum fromValue(String text) {
      for (NeighborTagEnum b : NeighborTagEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<NeighborTagEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final NeighborTagEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public NeighborTagEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return NeighborTagEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("neighborTag")
  private NeighborTagEnum neighborTag = null;

  @SerializedName("sourceInterface")
  private String sourceInterface = null;

  @SerializedName("inboundFilter")
  private DeviceSettingsBGPFilterSet inboundFilter = null;

  @SerializedName("outboundFilter")
  private DeviceSettingsBGPFilterSet outboundFilter = null;

  @SerializedName("localIp")
  private String localIp = null;

  @SerializedName("maxHop")
  private String maxHop = null;

  @SerializedName("allowAS")
  private Boolean allowAS = null;

  @SerializedName("connect")
  private String connect = null;

  @SerializedName("defaultRoute")
  private Boolean defaultRoute = null;

  @SerializedName("holdtime")
  private String holdtime = null;

  @SerializedName("keepalive")
  private String keepalive = null;

  @SerializedName("enableMd5")
  private Boolean enableMd5 = null;

  @SerializedName("md5Password")
  private String md5Password = null;

  public DeviceSettingsBGPNeighbor neighborAS(String neighborAS) {
    this.neighborAS = neighborAS;
    return this;
  }

   /**
   * Get neighborAS
   * @return neighborAS
  **/
  @Schema(description = "")
  public String getNeighborAS() {
    return neighborAS;
  }

  public void setNeighborAS(String neighborAS) {
    this.neighborAS = neighborAS;
  }

  public DeviceSettingsBGPNeighbor neighborIp(String neighborIp) {
    this.neighborIp = neighborIp;
    return this;
  }

   /**
   * Get neighborIp
   * @return neighborIp
  **/
  @Schema(description = "")
  public String getNeighborIp() {
    return neighborIp;
  }

  public void setNeighborIp(String neighborIp) {
    this.neighborIp = neighborIp;
  }

  public DeviceSettingsBGPNeighbor neighborTag(NeighborTagEnum neighborTag) {
    this.neighborTag = neighborTag;
    return this;
  }

   /**
   * Get neighborTag
   * @return neighborTag
  **/
  @Schema(description = "")
  public NeighborTagEnum getNeighborTag() {
    return neighborTag;
  }

  public void setNeighborTag(NeighborTagEnum neighborTag) {
    this.neighborTag = neighborTag;
  }

  public DeviceSettingsBGPNeighbor sourceInterface(String sourceInterface) {
    this.sourceInterface = sourceInterface;
    return this;
  }

   /**
   * Get sourceInterface
   * @return sourceInterface
  **/
  @Schema(description = "")
  public String getSourceInterface() {
    return sourceInterface;
  }

  public void setSourceInterface(String sourceInterface) {
    this.sourceInterface = sourceInterface;
  }

  public DeviceSettingsBGPNeighbor inboundFilter(DeviceSettingsBGPFilterSet inboundFilter) {
    this.inboundFilter = inboundFilter;
    return this;
  }

   /**
   * Get inboundFilter
   * @return inboundFilter
  **/
  @Schema(description = "")
  public DeviceSettingsBGPFilterSet getInboundFilter() {
    return inboundFilter;
  }

  public void setInboundFilter(DeviceSettingsBGPFilterSet inboundFilter) {
    this.inboundFilter = inboundFilter;
  }

  public DeviceSettingsBGPNeighbor outboundFilter(DeviceSettingsBGPFilterSet outboundFilter) {
    this.outboundFilter = outboundFilter;
    return this;
  }

   /**
   * Get outboundFilter
   * @return outboundFilter
  **/
  @Schema(description = "")
  public DeviceSettingsBGPFilterSet getOutboundFilter() {
    return outboundFilter;
  }

  public void setOutboundFilter(DeviceSettingsBGPFilterSet outboundFilter) {
    this.outboundFilter = outboundFilter;
  }

  public DeviceSettingsBGPNeighbor localIp(String localIp) {
    this.localIp = localIp;
    return this;
  }

   /**
   * Get localIp
   * @return localIp
  **/
  @Schema(description = "")
  public String getLocalIp() {
    return localIp;
  }

  public void setLocalIp(String localIp) {
    this.localIp = localIp;
  }

  public DeviceSettingsBGPNeighbor maxHop(String maxHop) {
    this.maxHop = maxHop;
    return this;
  }

   /**
   * Get maxHop
   * @return maxHop
  **/
  @Schema(description = "")
  public String getMaxHop() {
    return maxHop;
  }

  public void setMaxHop(String maxHop) {
    this.maxHop = maxHop;
  }

  public DeviceSettingsBGPNeighbor allowAS(Boolean allowAS) {
    this.allowAS = allowAS;
    return this;
  }

   /**
   * Get allowAS
   * @return allowAS
  **/
  @Schema(description = "")
  public Boolean isAllowAS() {
    return allowAS;
  }

  public void setAllowAS(Boolean allowAS) {
    this.allowAS = allowAS;
  }

  public DeviceSettingsBGPNeighbor connect(String connect) {
    this.connect = connect;
    return this;
  }

   /**
   * Get connect
   * @return connect
  **/
  @Schema(description = "")
  public String getConnect() {
    return connect;
  }

  public void setConnect(String connect) {
    this.connect = connect;
  }

  public DeviceSettingsBGPNeighbor defaultRoute(Boolean defaultRoute) {
    this.defaultRoute = defaultRoute;
    return this;
  }

   /**
   * Get defaultRoute
   * @return defaultRoute
  **/
  @Schema(description = "")
  public Boolean isDefaultRoute() {
    return defaultRoute;
  }

  public void setDefaultRoute(Boolean defaultRoute) {
    this.defaultRoute = defaultRoute;
  }

  public DeviceSettingsBGPNeighbor holdtime(String holdtime) {
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

  public DeviceSettingsBGPNeighbor keepalive(String keepalive) {
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

  public DeviceSettingsBGPNeighbor enableMd5(Boolean enableMd5) {
    this.enableMd5 = enableMd5;
    return this;
  }

   /**
   * Get enableMd5
   * @return enableMd5
  **/
  @Schema(description = "")
  public Boolean isEnableMd5() {
    return enableMd5;
  }

  public void setEnableMd5(Boolean enableMd5) {
    this.enableMd5 = enableMd5;
  }

  public DeviceSettingsBGPNeighbor md5Password(String md5Password) {
    this.md5Password = md5Password;
    return this;
  }

   /**
   * Get md5Password
   * @return md5Password
  **/
  @Schema(description = "")
  public String getMd5Password() {
    return md5Password;
  }

  public void setMd5Password(String md5Password) {
    this.md5Password = md5Password;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceSettingsBGPNeighbor deviceSettingsBGPNeighbor = (DeviceSettingsBGPNeighbor) o;
    return Objects.equals(this.neighborAS, deviceSettingsBGPNeighbor.neighborAS) &&
        Objects.equals(this.neighborIp, deviceSettingsBGPNeighbor.neighborIp) &&
        Objects.equals(this.neighborTag, deviceSettingsBGPNeighbor.neighborTag) &&
        Objects.equals(this.sourceInterface, deviceSettingsBGPNeighbor.sourceInterface) &&
        Objects.equals(this.inboundFilter, deviceSettingsBGPNeighbor.inboundFilter) &&
        Objects.equals(this.outboundFilter, deviceSettingsBGPNeighbor.outboundFilter) &&
        Objects.equals(this.localIp, deviceSettingsBGPNeighbor.localIp) &&
        Objects.equals(this.maxHop, deviceSettingsBGPNeighbor.maxHop) &&
        Objects.equals(this.allowAS, deviceSettingsBGPNeighbor.allowAS) &&
        Objects.equals(this.connect, deviceSettingsBGPNeighbor.connect) &&
        Objects.equals(this.defaultRoute, deviceSettingsBGPNeighbor.defaultRoute) &&
        Objects.equals(this.holdtime, deviceSettingsBGPNeighbor.holdtime) &&
        Objects.equals(this.keepalive, deviceSettingsBGPNeighbor.keepalive) &&
        Objects.equals(this.enableMd5, deviceSettingsBGPNeighbor.enableMd5) &&
        Objects.equals(this.md5Password, deviceSettingsBGPNeighbor.md5Password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(neighborAS, neighborIp, neighborTag, sourceInterface, inboundFilter, outboundFilter, localIp, maxHop, allowAS, connect, defaultRoute, holdtime, keepalive, enableMd5, md5Password);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceSettingsBGPNeighbor {\n");
    
    sb.append("    neighborAS: ").append(toIndentedString(neighborAS)).append("\n");
    sb.append("    neighborIp: ").append(toIndentedString(neighborIp)).append("\n");
    sb.append("    neighborTag: ").append(toIndentedString(neighborTag)).append("\n");
    sb.append("    sourceInterface: ").append(toIndentedString(sourceInterface)).append("\n");
    sb.append("    inboundFilter: ").append(toIndentedString(inboundFilter)).append("\n");
    sb.append("    outboundFilter: ").append(toIndentedString(outboundFilter)).append("\n");
    sb.append("    localIp: ").append(toIndentedString(localIp)).append("\n");
    sb.append("    maxHop: ").append(toIndentedString(maxHop)).append("\n");
    sb.append("    allowAS: ").append(toIndentedString(allowAS)).append("\n");
    sb.append("    connect: ").append(toIndentedString(connect)).append("\n");
    sb.append("    defaultRoute: ").append(toIndentedString(defaultRoute)).append("\n");
    sb.append("    holdtime: ").append(toIndentedString(holdtime)).append("\n");
    sb.append("    keepalive: ").append(toIndentedString(keepalive)).append("\n");
    sb.append("    enableMd5: ").append(toIndentedString(enableMd5)).append("\n");
    sb.append("    md5Password: ").append(toIndentedString(md5Password)).append("\n");
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
