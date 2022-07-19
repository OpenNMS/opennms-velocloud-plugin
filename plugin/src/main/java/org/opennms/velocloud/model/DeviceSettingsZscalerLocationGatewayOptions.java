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
 * DeviceSettingsZscalerLocationGatewayOptions
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class DeviceSettingsZscalerLocationGatewayOptions {
  @SerializedName("xffForwardEnabled")
  private Boolean xffForwardEnabled = null;

  @SerializedName("authRequired")
  private Boolean authRequired = null;

  @SerializedName("aupEnabled")
  private Boolean aupEnabled = null;

  @SerializedName("aupBlockInternetUntilAccepted")
  private Boolean aupBlockInternetUntilAccepted = null;

  @SerializedName("aupForceSslInspection")
  private Boolean aupForceSslInspection = null;

  @SerializedName("aupTimeoutInDays")
  private Float aupTimeoutInDays = null;

  @SerializedName("cautionEnabled")
  private Boolean cautionEnabled = null;

  @SerializedName("surrogateIP")
  private Boolean surrogateIP = null;

  @SerializedName("idleTimeInMinutes")
  private Float idleTimeInMinutes = null;

  /**
   * Gets or Sets displayTimeUnit
   */
  @JsonAdapter(DisplayTimeUnitEnum.Adapter.class)
  public enum DisplayTimeUnitEnum {
    DAY("DAY"),
    HOUR("HOUR"),
    MINUTE("MINUTE");

    private String value;

    DisplayTimeUnitEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static DisplayTimeUnitEnum fromValue(String text) {
      for (DisplayTimeUnitEnum b : DisplayTimeUnitEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<DisplayTimeUnitEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DisplayTimeUnitEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public DisplayTimeUnitEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return DisplayTimeUnitEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("displayTimeUnit")
  private DisplayTimeUnitEnum displayTimeUnit = null;

  @SerializedName("surrogateIPEnforcedForKnownBrowsers")
  private Boolean surrogateIPEnforcedForKnownBrowsers = null;

  @SerializedName("surrogateRefreshTimeInMinutes")
  private Float surrogateRefreshTimeInMinutes = null;

  /**
   * Gets or Sets surrogateRefreshTimeUnit
   */
  @JsonAdapter(SurrogateRefreshTimeUnitEnum.Adapter.class)
  public enum SurrogateRefreshTimeUnitEnum {
    DAY("DAY"),
    HOUR("HOUR"),
    MINUTE("MINUTE");

    private String value;

    SurrogateRefreshTimeUnitEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static SurrogateRefreshTimeUnitEnum fromValue(String text) {
      for (SurrogateRefreshTimeUnitEnum b : SurrogateRefreshTimeUnitEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<SurrogateRefreshTimeUnitEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final SurrogateRefreshTimeUnitEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public SurrogateRefreshTimeUnitEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return SurrogateRefreshTimeUnitEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("surrogateRefreshTimeUnit")
  private SurrogateRefreshTimeUnitEnum surrogateRefreshTimeUnit = null;

  @SerializedName("dnBandwidth")
  private Float dnBandwidth = null;

  @SerializedName("upBandwidth")
  private Float upBandwidth = null;

  @SerializedName("ipsControl")
  private Boolean ipsControl = null;

  @SerializedName("ofwEnabled")
  private Boolean ofwEnabled = null;

  public DeviceSettingsZscalerLocationGatewayOptions xffForwardEnabled(Boolean xffForwardEnabled) {
    this.xffForwardEnabled = xffForwardEnabled;
    return this;
  }

   /**
   * Get xffForwardEnabled
   * @return xffForwardEnabled
  **/
  @Schema(description = "")
  public Boolean isXffForwardEnabled() {
    return xffForwardEnabled;
  }

  public void setXffForwardEnabled(Boolean xffForwardEnabled) {
    this.xffForwardEnabled = xffForwardEnabled;
  }

  public DeviceSettingsZscalerLocationGatewayOptions authRequired(Boolean authRequired) {
    this.authRequired = authRequired;
    return this;
  }

   /**
   * Get authRequired
   * @return authRequired
  **/
  @Schema(description = "")
  public Boolean isAuthRequired() {
    return authRequired;
  }

  public void setAuthRequired(Boolean authRequired) {
    this.authRequired = authRequired;
  }

  public DeviceSettingsZscalerLocationGatewayOptions aupEnabled(Boolean aupEnabled) {
    this.aupEnabled = aupEnabled;
    return this;
  }

   /**
   * Get aupEnabled
   * @return aupEnabled
  **/
  @Schema(description = "")
  public Boolean isAupEnabled() {
    return aupEnabled;
  }

  public void setAupEnabled(Boolean aupEnabled) {
    this.aupEnabled = aupEnabled;
  }

  public DeviceSettingsZscalerLocationGatewayOptions aupBlockInternetUntilAccepted(Boolean aupBlockInternetUntilAccepted) {
    this.aupBlockInternetUntilAccepted = aupBlockInternetUntilAccepted;
    return this;
  }

   /**
   * Get aupBlockInternetUntilAccepted
   * @return aupBlockInternetUntilAccepted
  **/
  @Schema(description = "")
  public Boolean isAupBlockInternetUntilAccepted() {
    return aupBlockInternetUntilAccepted;
  }

  public void setAupBlockInternetUntilAccepted(Boolean aupBlockInternetUntilAccepted) {
    this.aupBlockInternetUntilAccepted = aupBlockInternetUntilAccepted;
  }

  public DeviceSettingsZscalerLocationGatewayOptions aupForceSslInspection(Boolean aupForceSslInspection) {
    this.aupForceSslInspection = aupForceSslInspection;
    return this;
  }

   /**
   * Get aupForceSslInspection
   * @return aupForceSslInspection
  **/
  @Schema(description = "")
  public Boolean isAupForceSslInspection() {
    return aupForceSslInspection;
  }

  public void setAupForceSslInspection(Boolean aupForceSslInspection) {
    this.aupForceSslInspection = aupForceSslInspection;
  }

  public DeviceSettingsZscalerLocationGatewayOptions aupTimeoutInDays(Float aupTimeoutInDays) {
    this.aupTimeoutInDays = aupTimeoutInDays;
    return this;
  }

   /**
   * Get aupTimeoutInDays
   * @return aupTimeoutInDays
  **/
  @Schema(description = "")
  public Float getAupTimeoutInDays() {
    return aupTimeoutInDays;
  }

  public void setAupTimeoutInDays(Float aupTimeoutInDays) {
    this.aupTimeoutInDays = aupTimeoutInDays;
  }

  public DeviceSettingsZscalerLocationGatewayOptions cautionEnabled(Boolean cautionEnabled) {
    this.cautionEnabled = cautionEnabled;
    return this;
  }

   /**
   * Get cautionEnabled
   * @return cautionEnabled
  **/
  @Schema(description = "")
  public Boolean isCautionEnabled() {
    return cautionEnabled;
  }

  public void setCautionEnabled(Boolean cautionEnabled) {
    this.cautionEnabled = cautionEnabled;
  }

  public DeviceSettingsZscalerLocationGatewayOptions surrogateIP(Boolean surrogateIP) {
    this.surrogateIP = surrogateIP;
    return this;
  }

   /**
   * Get surrogateIP
   * @return surrogateIP
  **/
  @Schema(description = "")
  public Boolean isSurrogateIP() {
    return surrogateIP;
  }

  public void setSurrogateIP(Boolean surrogateIP) {
    this.surrogateIP = surrogateIP;
  }

  public DeviceSettingsZscalerLocationGatewayOptions idleTimeInMinutes(Float idleTimeInMinutes) {
    this.idleTimeInMinutes = idleTimeInMinutes;
    return this;
  }

   /**
   * Get idleTimeInMinutes
   * @return idleTimeInMinutes
  **/
  @Schema(description = "")
  public Float getIdleTimeInMinutes() {
    return idleTimeInMinutes;
  }

  public void setIdleTimeInMinutes(Float idleTimeInMinutes) {
    this.idleTimeInMinutes = idleTimeInMinutes;
  }

  public DeviceSettingsZscalerLocationGatewayOptions displayTimeUnit(DisplayTimeUnitEnum displayTimeUnit) {
    this.displayTimeUnit = displayTimeUnit;
    return this;
  }

   /**
   * Get displayTimeUnit
   * @return displayTimeUnit
  **/
  @Schema(description = "")
  public DisplayTimeUnitEnum getDisplayTimeUnit() {
    return displayTimeUnit;
  }

  public void setDisplayTimeUnit(DisplayTimeUnitEnum displayTimeUnit) {
    this.displayTimeUnit = displayTimeUnit;
  }

  public DeviceSettingsZscalerLocationGatewayOptions surrogateIPEnforcedForKnownBrowsers(Boolean surrogateIPEnforcedForKnownBrowsers) {
    this.surrogateIPEnforcedForKnownBrowsers = surrogateIPEnforcedForKnownBrowsers;
    return this;
  }

   /**
   * Get surrogateIPEnforcedForKnownBrowsers
   * @return surrogateIPEnforcedForKnownBrowsers
  **/
  @Schema(description = "")
  public Boolean isSurrogateIPEnforcedForKnownBrowsers() {
    return surrogateIPEnforcedForKnownBrowsers;
  }

  public void setSurrogateIPEnforcedForKnownBrowsers(Boolean surrogateIPEnforcedForKnownBrowsers) {
    this.surrogateIPEnforcedForKnownBrowsers = surrogateIPEnforcedForKnownBrowsers;
  }

  public DeviceSettingsZscalerLocationGatewayOptions surrogateRefreshTimeInMinutes(Float surrogateRefreshTimeInMinutes) {
    this.surrogateRefreshTimeInMinutes = surrogateRefreshTimeInMinutes;
    return this;
  }

   /**
   * Get surrogateRefreshTimeInMinutes
   * @return surrogateRefreshTimeInMinutes
  **/
  @Schema(description = "")
  public Float getSurrogateRefreshTimeInMinutes() {
    return surrogateRefreshTimeInMinutes;
  }

  public void setSurrogateRefreshTimeInMinutes(Float surrogateRefreshTimeInMinutes) {
    this.surrogateRefreshTimeInMinutes = surrogateRefreshTimeInMinutes;
  }

  public DeviceSettingsZscalerLocationGatewayOptions surrogateRefreshTimeUnit(SurrogateRefreshTimeUnitEnum surrogateRefreshTimeUnit) {
    this.surrogateRefreshTimeUnit = surrogateRefreshTimeUnit;
    return this;
  }

   /**
   * Get surrogateRefreshTimeUnit
   * @return surrogateRefreshTimeUnit
  **/
  @Schema(description = "")
  public SurrogateRefreshTimeUnitEnum getSurrogateRefreshTimeUnit() {
    return surrogateRefreshTimeUnit;
  }

  public void setSurrogateRefreshTimeUnit(SurrogateRefreshTimeUnitEnum surrogateRefreshTimeUnit) {
    this.surrogateRefreshTimeUnit = surrogateRefreshTimeUnit;
  }

  public DeviceSettingsZscalerLocationGatewayOptions dnBandwidth(Float dnBandwidth) {
    this.dnBandwidth = dnBandwidth;
    return this;
  }

   /**
   * Get dnBandwidth
   * @return dnBandwidth
  **/
  @Schema(description = "")
  public Float getDnBandwidth() {
    return dnBandwidth;
  }

  public void setDnBandwidth(Float dnBandwidth) {
    this.dnBandwidth = dnBandwidth;
  }

  public DeviceSettingsZscalerLocationGatewayOptions upBandwidth(Float upBandwidth) {
    this.upBandwidth = upBandwidth;
    return this;
  }

   /**
   * Get upBandwidth
   * @return upBandwidth
  **/
  @Schema(description = "")
  public Float getUpBandwidth() {
    return upBandwidth;
  }

  public void setUpBandwidth(Float upBandwidth) {
    this.upBandwidth = upBandwidth;
  }

  public DeviceSettingsZscalerLocationGatewayOptions ipsControl(Boolean ipsControl) {
    this.ipsControl = ipsControl;
    return this;
  }

   /**
   * Get ipsControl
   * @return ipsControl
  **/
  @Schema(description = "")
  public Boolean isIpsControl() {
    return ipsControl;
  }

  public void setIpsControl(Boolean ipsControl) {
    this.ipsControl = ipsControl;
  }

  public DeviceSettingsZscalerLocationGatewayOptions ofwEnabled(Boolean ofwEnabled) {
    this.ofwEnabled = ofwEnabled;
    return this;
  }

   /**
   * Get ofwEnabled
   * @return ofwEnabled
  **/
  @Schema(description = "")
  public Boolean isOfwEnabled() {
    return ofwEnabled;
  }

  public void setOfwEnabled(Boolean ofwEnabled) {
    this.ofwEnabled = ofwEnabled;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceSettingsZscalerLocationGatewayOptions deviceSettingsZscalerLocationGatewayOptions = (DeviceSettingsZscalerLocationGatewayOptions) o;
    return Objects.equals(this.xffForwardEnabled, deviceSettingsZscalerLocationGatewayOptions.xffForwardEnabled) &&
        Objects.equals(this.authRequired, deviceSettingsZscalerLocationGatewayOptions.authRequired) &&
        Objects.equals(this.aupEnabled, deviceSettingsZscalerLocationGatewayOptions.aupEnabled) &&
        Objects.equals(this.aupBlockInternetUntilAccepted, deviceSettingsZscalerLocationGatewayOptions.aupBlockInternetUntilAccepted) &&
        Objects.equals(this.aupForceSslInspection, deviceSettingsZscalerLocationGatewayOptions.aupForceSslInspection) &&
        Objects.equals(this.aupTimeoutInDays, deviceSettingsZscalerLocationGatewayOptions.aupTimeoutInDays) &&
        Objects.equals(this.cautionEnabled, deviceSettingsZscalerLocationGatewayOptions.cautionEnabled) &&
        Objects.equals(this.surrogateIP, deviceSettingsZscalerLocationGatewayOptions.surrogateIP) &&
        Objects.equals(this.idleTimeInMinutes, deviceSettingsZscalerLocationGatewayOptions.idleTimeInMinutes) &&
        Objects.equals(this.displayTimeUnit, deviceSettingsZscalerLocationGatewayOptions.displayTimeUnit) &&
        Objects.equals(this.surrogateIPEnforcedForKnownBrowsers, deviceSettingsZscalerLocationGatewayOptions.surrogateIPEnforcedForKnownBrowsers) &&
        Objects.equals(this.surrogateRefreshTimeInMinutes, deviceSettingsZscalerLocationGatewayOptions.surrogateRefreshTimeInMinutes) &&
        Objects.equals(this.surrogateRefreshTimeUnit, deviceSettingsZscalerLocationGatewayOptions.surrogateRefreshTimeUnit) &&
        Objects.equals(this.dnBandwidth, deviceSettingsZscalerLocationGatewayOptions.dnBandwidth) &&
        Objects.equals(this.upBandwidth, deviceSettingsZscalerLocationGatewayOptions.upBandwidth) &&
        Objects.equals(this.ipsControl, deviceSettingsZscalerLocationGatewayOptions.ipsControl) &&
        Objects.equals(this.ofwEnabled, deviceSettingsZscalerLocationGatewayOptions.ofwEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(xffForwardEnabled, authRequired, aupEnabled, aupBlockInternetUntilAccepted, aupForceSslInspection, aupTimeoutInDays, cautionEnabled, surrogateIP, idleTimeInMinutes, displayTimeUnit, surrogateIPEnforcedForKnownBrowsers, surrogateRefreshTimeInMinutes, surrogateRefreshTimeUnit, dnBandwidth, upBandwidth, ipsControl, ofwEnabled);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceSettingsZscalerLocationGatewayOptions {\n");
    
    sb.append("    xffForwardEnabled: ").append(toIndentedString(xffForwardEnabled)).append("\n");
    sb.append("    authRequired: ").append(toIndentedString(authRequired)).append("\n");
    sb.append("    aupEnabled: ").append(toIndentedString(aupEnabled)).append("\n");
    sb.append("    aupBlockInternetUntilAccepted: ").append(toIndentedString(aupBlockInternetUntilAccepted)).append("\n");
    sb.append("    aupForceSslInspection: ").append(toIndentedString(aupForceSslInspection)).append("\n");
    sb.append("    aupTimeoutInDays: ").append(toIndentedString(aupTimeoutInDays)).append("\n");
    sb.append("    cautionEnabled: ").append(toIndentedString(cautionEnabled)).append("\n");
    sb.append("    surrogateIP: ").append(toIndentedString(surrogateIP)).append("\n");
    sb.append("    idleTimeInMinutes: ").append(toIndentedString(idleTimeInMinutes)).append("\n");
    sb.append("    displayTimeUnit: ").append(toIndentedString(displayTimeUnit)).append("\n");
    sb.append("    surrogateIPEnforcedForKnownBrowsers: ").append(toIndentedString(surrogateIPEnforcedForKnownBrowsers)).append("\n");
    sb.append("    surrogateRefreshTimeInMinutes: ").append(toIndentedString(surrogateRefreshTimeInMinutes)).append("\n");
    sb.append("    surrogateRefreshTimeUnit: ").append(toIndentedString(surrogateRefreshTimeUnit)).append("\n");
    sb.append("    dnBandwidth: ").append(toIndentedString(dnBandwidth)).append("\n");
    sb.append("    upBandwidth: ").append(toIndentedString(upBandwidth)).append("\n");
    sb.append("    ipsControl: ").append(toIndentedString(ipsControl)).append("\n");
    sb.append("    ofwEnabled: ").append(toIndentedString(ofwEnabled)).append("\n");
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
