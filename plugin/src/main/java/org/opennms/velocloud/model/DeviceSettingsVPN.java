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
import java.util.ArrayList;
import java.util.List;
import org.opennms.velocloud.model.BaseApiResource11;
import org.opennms.velocloud.model.DeviceSettingsVPNEdgeToEdgeDetail;
import org.opennms.velocloud.model.DeviceSettingsVPNEdgeToEdgeHub;
import org.opennms.velocloud.model.DeviceSettingsVPNHub;
/**
 * DeviceSettingsVPN
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class DeviceSettingsVPN {
  @SerializedName("enabled")
  private Boolean enabled = null;

  @SerializedName("edgeToDataCenter")
  private Boolean edgeToDataCenter = null;

  /**
   * Gets or Sets ref
   */
  @JsonAdapter(RefEnum.Adapter.class)
  public enum RefEnum {
    DATACENTER("deviceSettings:vpn:dataCenter");

    private String value;

    RefEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static RefEnum fromValue(String text) {
      for (RefEnum b : RefEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<RefEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RefEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public RefEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return RefEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("ref")
  private RefEnum ref = null;

  @SerializedName("gatewayNonSdwanSites")
  private List<BaseApiResource11> gatewayNonSdwanSites = null;

  @SerializedName("isolationGroupId")
  private String isolationGroupId = null;

  @SerializedName("edgeToEdgeHub")
  private DeviceSettingsVPNEdgeToEdgeHub edgeToEdgeHub = null;

  @SerializedName("edgeToEdge")
  private Boolean edgeToEdge = null;

  @SerializedName("edgeToEdgeDetail")
  private DeviceSettingsVPNEdgeToEdgeDetail edgeToEdgeDetail = null;

  @SerializedName("conditionalBackhaul")
  private Boolean conditionalBackhaul = null;

  @SerializedName("backHaulEdges")
  private List<DeviceSettingsVPNHub> backHaulEdges = null;

  public DeviceSettingsVPN enabled(Boolean enabled) {
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

  public DeviceSettingsVPN edgeToDataCenter(Boolean edgeToDataCenter) {
    this.edgeToDataCenter = edgeToDataCenter;
    return this;
  }

   /**
   * Get edgeToDataCenter
   * @return edgeToDataCenter
  **/
  @Schema(description = "")
  public Boolean isEdgeToDataCenter() {
    return edgeToDataCenter;
  }

  public void setEdgeToDataCenter(Boolean edgeToDataCenter) {
    this.edgeToDataCenter = edgeToDataCenter;
  }

  public DeviceSettingsVPN ref(RefEnum ref) {
    this.ref = ref;
    return this;
  }

   /**
   * Get ref
   * @return ref
  **/
  @Schema(description = "")
  public RefEnum getRef() {
    return ref;
  }

  public void setRef(RefEnum ref) {
    this.ref = ref;
  }

  public DeviceSettingsVPN gatewayNonSdwanSites(List<BaseApiResource11> gatewayNonSdwanSites) {
    this.gatewayNonSdwanSites = gatewayNonSdwanSites;
    return this;
  }

  public DeviceSettingsVPN addGatewayNonSdwanSitesItem(BaseApiResource11 gatewayNonSdwanSitesItem) {
    if (this.gatewayNonSdwanSites == null) {
      this.gatewayNonSdwanSites = new ArrayList<>();
    }
    this.gatewayNonSdwanSites.add(gatewayNonSdwanSitesItem);
    return this;
  }

   /**
   * Get gatewayNonSdwanSites
   * @return gatewayNonSdwanSites
  **/
  @Schema(description = "")
  public List<BaseApiResource11> getGatewayNonSdwanSites() {
    return gatewayNonSdwanSites;
  }

  public void setGatewayNonSdwanSites(List<BaseApiResource11> gatewayNonSdwanSites) {
    this.gatewayNonSdwanSites = gatewayNonSdwanSites;
  }

  public DeviceSettingsVPN isolationGroupId(String isolationGroupId) {
    this.isolationGroupId = isolationGroupId;
    return this;
  }

   /**
   * Get isolationGroupId
   * @return isolationGroupId
  **/
  @Schema(description = "")
  public String getIsolationGroupId() {
    return isolationGroupId;
  }

  public void setIsolationGroupId(String isolationGroupId) {
    this.isolationGroupId = isolationGroupId;
  }

  public DeviceSettingsVPN edgeToEdgeHub(DeviceSettingsVPNEdgeToEdgeHub edgeToEdgeHub) {
    this.edgeToEdgeHub = edgeToEdgeHub;
    return this;
  }

   /**
   * Get edgeToEdgeHub
   * @return edgeToEdgeHub
  **/
  @Schema(description = "")
  public DeviceSettingsVPNEdgeToEdgeHub getEdgeToEdgeHub() {
    return edgeToEdgeHub;
  }

  public void setEdgeToEdgeHub(DeviceSettingsVPNEdgeToEdgeHub edgeToEdgeHub) {
    this.edgeToEdgeHub = edgeToEdgeHub;
  }

  public DeviceSettingsVPN edgeToEdge(Boolean edgeToEdge) {
    this.edgeToEdge = edgeToEdge;
    return this;
  }

   /**
   * Get edgeToEdge
   * @return edgeToEdge
  **/
  @Schema(description = "")
  public Boolean isEdgeToEdge() {
    return edgeToEdge;
  }

  public void setEdgeToEdge(Boolean edgeToEdge) {
    this.edgeToEdge = edgeToEdge;
  }

  public DeviceSettingsVPN edgeToEdgeDetail(DeviceSettingsVPNEdgeToEdgeDetail edgeToEdgeDetail) {
    this.edgeToEdgeDetail = edgeToEdgeDetail;
    return this;
  }

   /**
   * Get edgeToEdgeDetail
   * @return edgeToEdgeDetail
  **/
  @Schema(description = "")
  public DeviceSettingsVPNEdgeToEdgeDetail getEdgeToEdgeDetail() {
    return edgeToEdgeDetail;
  }

  public void setEdgeToEdgeDetail(DeviceSettingsVPNEdgeToEdgeDetail edgeToEdgeDetail) {
    this.edgeToEdgeDetail = edgeToEdgeDetail;
  }

  public DeviceSettingsVPN conditionalBackhaul(Boolean conditionalBackhaul) {
    this.conditionalBackhaul = conditionalBackhaul;
    return this;
  }

   /**
   * Get conditionalBackhaul
   * @return conditionalBackhaul
  **/
  @Schema(description = "")
  public Boolean isConditionalBackhaul() {
    return conditionalBackhaul;
  }

  public void setConditionalBackhaul(Boolean conditionalBackhaul) {
    this.conditionalBackhaul = conditionalBackhaul;
  }

  public DeviceSettingsVPN backHaulEdges(List<DeviceSettingsVPNHub> backHaulEdges) {
    this.backHaulEdges = backHaulEdges;
    return this;
  }

  public DeviceSettingsVPN addBackHaulEdgesItem(DeviceSettingsVPNHub backHaulEdgesItem) {
    if (this.backHaulEdges == null) {
      this.backHaulEdges = new ArrayList<>();
    }
    this.backHaulEdges.add(backHaulEdgesItem);
    return this;
  }

   /**
   * Get backHaulEdges
   * @return backHaulEdges
  **/
  @Schema(description = "")
  public List<DeviceSettingsVPNHub> getBackHaulEdges() {
    return backHaulEdges;
  }

  public void setBackHaulEdges(List<DeviceSettingsVPNHub> backHaulEdges) {
    this.backHaulEdges = backHaulEdges;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceSettingsVPN deviceSettingsVPN = (DeviceSettingsVPN) o;
    return Objects.equals(this.enabled, deviceSettingsVPN.enabled) &&
        Objects.equals(this.edgeToDataCenter, deviceSettingsVPN.edgeToDataCenter) &&
        Objects.equals(this.ref, deviceSettingsVPN.ref) &&
        Objects.equals(this.gatewayNonSdwanSites, deviceSettingsVPN.gatewayNonSdwanSites) &&
        Objects.equals(this.isolationGroupId, deviceSettingsVPN.isolationGroupId) &&
        Objects.equals(this.edgeToEdgeHub, deviceSettingsVPN.edgeToEdgeHub) &&
        Objects.equals(this.edgeToEdge, deviceSettingsVPN.edgeToEdge) &&
        Objects.equals(this.edgeToEdgeDetail, deviceSettingsVPN.edgeToEdgeDetail) &&
        Objects.equals(this.conditionalBackhaul, deviceSettingsVPN.conditionalBackhaul) &&
        Objects.equals(this.backHaulEdges, deviceSettingsVPN.backHaulEdges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled, edgeToDataCenter, ref, gatewayNonSdwanSites, isolationGroupId, edgeToEdgeHub, edgeToEdge, edgeToEdgeDetail, conditionalBackhaul, backHaulEdges);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceSettingsVPN {\n");
    
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    edgeToDataCenter: ").append(toIndentedString(edgeToDataCenter)).append("\n");
    sb.append("    ref: ").append(toIndentedString(ref)).append("\n");
    sb.append("    gatewayNonSdwanSites: ").append(toIndentedString(gatewayNonSdwanSites)).append("\n");
    sb.append("    isolationGroupId: ").append(toIndentedString(isolationGroupId)).append("\n");
    sb.append("    edgeToEdgeHub: ").append(toIndentedString(edgeToEdgeHub)).append("\n");
    sb.append("    edgeToEdge: ").append(toIndentedString(edgeToEdge)).append("\n");
    sb.append("    edgeToEdgeDetail: ").append(toIndentedString(edgeToEdgeDetail)).append("\n");
    sb.append("    conditionalBackhaul: ").append(toIndentedString(conditionalBackhaul)).append("\n");
    sb.append("    backHaulEdges: ").append(toIndentedString(backHaulEdges)).append("\n");
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
