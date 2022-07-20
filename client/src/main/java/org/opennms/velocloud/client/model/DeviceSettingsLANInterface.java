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
import org.opennms.velocloud.client.model.DeviceSettingsLANL2;
/**
 * DeviceSettingsLANInterface
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")
public class DeviceSettingsLANInterface {
  @JsonProperty("override")
  private Boolean override = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("cwp")
  private Boolean cwp = null;

  @JsonProperty("disabled")
  private Boolean disabled = null;

  @JsonProperty("l2")
  private DeviceSettingsLANL2 l2 = null;

  /**
   * Gets or Sets portMode
   */
  public enum PortModeEnum {
    ACCESS("access"),
    TRUNK("trunk");

    private String value;

    PortModeEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static PortModeEnum fromValue(String text) {
      for (PortModeEnum b : PortModeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("portMode")
  private PortModeEnum portMode = null;

  @JsonProperty("space")
  private String space = null;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    WIRED("wired"),
    WIRELESS("wireless");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("untaggedVlan")
  private AnyOfDeviceSettingsLANInterfaceUntaggedVlan untaggedVlan = null;

  @JsonProperty("vlanIds")
  private List<Integer> vlanIds = null;

  /**
   * Gets or Sets authenticationType
   */
  public enum AuthenticationTypeEnum {
    NONE("none"),
    _802_1X("802.1x");

    private String value;

    AuthenticationTypeEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static AuthenticationTypeEnum fromValue(String text) {
      for (AuthenticationTypeEnum b : AuthenticationTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("authenticationType")
  private AuthenticationTypeEnum authenticationType = null;

  @JsonProperty("broadcastSsid")
  private Boolean broadcastSsid = null;

  @JsonProperty("passphrase")
  private String passphrase = null;

  @JsonProperty("ssid")
  private String ssid = null;

  /**
   * Gets or Sets securityMode
   */
  public enum SecurityModeEnum {
    OPEN("Open"),
    WPA2ENTERPRISE("WPA2Enterprise"),
    WPA2PERSONAL("WPA2Personal");

    private String value;

    SecurityModeEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static SecurityModeEnum fromValue(String text) {
      for (SecurityModeEnum b : SecurityModeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("securityMode")
  private SecurityModeEnum securityMode = null;

  public DeviceSettingsLANInterface override(Boolean override) {
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

  public DeviceSettingsLANInterface name(String name) {
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

  public DeviceSettingsLANInterface cwp(Boolean cwp) {
    this.cwp = cwp;
    return this;
  }

   /**
   * Get cwp
   * @return cwp
  **/
  @Schema(description = "")
  public Boolean isCwp() {
    return cwp;
  }

  public void setCwp(Boolean cwp) {
    this.cwp = cwp;
  }

  public DeviceSettingsLANInterface disabled(Boolean disabled) {
    this.disabled = disabled;
    return this;
  }

   /**
   * Get disabled
   * @return disabled
  **/
  @Schema(description = "")
  public Boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(Boolean disabled) {
    this.disabled = disabled;
  }

  public DeviceSettingsLANInterface l2(DeviceSettingsLANL2 l2) {
    this.l2 = l2;
    return this;
  }

   /**
   * Get l2
   * @return l2
  **/
  @Schema(description = "")
  public DeviceSettingsLANL2 getL2() {
    return l2;
  }

  public void setL2(DeviceSettingsLANL2 l2) {
    this.l2 = l2;
  }

  public DeviceSettingsLANInterface portMode(PortModeEnum portMode) {
    this.portMode = portMode;
    return this;
  }

   /**
   * Get portMode
   * @return portMode
  **/
  @Schema(description = "")
  public PortModeEnum getPortMode() {
    return portMode;
  }

  public void setPortMode(PortModeEnum portMode) {
    this.portMode = portMode;
  }

  public DeviceSettingsLANInterface space(String space) {
    this.space = space;
    return this;
  }

   /**
   * Get space
   * @return space
  **/
  @Schema(description = "")
  public String getSpace() {
    return space;
  }

  public void setSpace(String space) {
    this.space = space;
  }

  public DeviceSettingsLANInterface type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @Schema(description = "")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public DeviceSettingsLANInterface untaggedVlan(AnyOfDeviceSettingsLANInterfaceUntaggedVlan untaggedVlan) {
    this.untaggedVlan = untaggedVlan;
    return this;
  }

   /**
   * Get untaggedVlan
   * @return untaggedVlan
  **/
  @Schema(description = "")
  public AnyOfDeviceSettingsLANInterfaceUntaggedVlan getUntaggedVlan() {
    return untaggedVlan;
  }

  public void setUntaggedVlan(AnyOfDeviceSettingsLANInterfaceUntaggedVlan untaggedVlan) {
    this.untaggedVlan = untaggedVlan;
  }

  public DeviceSettingsLANInterface vlanIds(List<Integer> vlanIds) {
    this.vlanIds = vlanIds;
    return this;
  }

  public DeviceSettingsLANInterface addVlanIdsItem(Integer vlanIdsItem) {
    if (this.vlanIds == null) {
      this.vlanIds = new ArrayList<>();
    }
    this.vlanIds.add(vlanIdsItem);
    return this;
  }

   /**
   * Get vlanIds
   * @return vlanIds
  **/
  @Schema(description = "")
  public List<Integer> getVlanIds() {
    return vlanIds;
  }

  public void setVlanIds(List<Integer> vlanIds) {
    this.vlanIds = vlanIds;
  }

  public DeviceSettingsLANInterface authenticationType(AuthenticationTypeEnum authenticationType) {
    this.authenticationType = authenticationType;
    return this;
  }

   /**
   * Get authenticationType
   * @return authenticationType
  **/
  @Schema(description = "")
  public AuthenticationTypeEnum getAuthenticationType() {
    return authenticationType;
  }

  public void setAuthenticationType(AuthenticationTypeEnum authenticationType) {
    this.authenticationType = authenticationType;
  }

  public DeviceSettingsLANInterface broadcastSsid(Boolean broadcastSsid) {
    this.broadcastSsid = broadcastSsid;
    return this;
  }

   /**
   * Get broadcastSsid
   * @return broadcastSsid
  **/
  @Schema(description = "")
  public Boolean isBroadcastSsid() {
    return broadcastSsid;
  }

  public void setBroadcastSsid(Boolean broadcastSsid) {
    this.broadcastSsid = broadcastSsid;
  }

  public DeviceSettingsLANInterface passphrase(String passphrase) {
    this.passphrase = passphrase;
    return this;
  }

   /**
   * Get passphrase
   * @return passphrase
  **/
  @Schema(description = "")
  public String getPassphrase() {
    return passphrase;
  }

  public void setPassphrase(String passphrase) {
    this.passphrase = passphrase;
  }

  public DeviceSettingsLANInterface ssid(String ssid) {
    this.ssid = ssid;
    return this;
  }

   /**
   * Get ssid
   * @return ssid
  **/
  @Schema(description = "")
  public String getSsid() {
    return ssid;
  }

  public void setSsid(String ssid) {
    this.ssid = ssid;
  }

  public DeviceSettingsLANInterface securityMode(SecurityModeEnum securityMode) {
    this.securityMode = securityMode;
    return this;
  }

   /**
   * Get securityMode
   * @return securityMode
  **/
  @Schema(description = "")
  public SecurityModeEnum getSecurityMode() {
    return securityMode;
  }

  public void setSecurityMode(SecurityModeEnum securityMode) {
    this.securityMode = securityMode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceSettingsLANInterface deviceSettingsLANInterface = (DeviceSettingsLANInterface) o;
    return Objects.equals(this.override, deviceSettingsLANInterface.override) &&
        Objects.equals(this.name, deviceSettingsLANInterface.name) &&
        Objects.equals(this.cwp, deviceSettingsLANInterface.cwp) &&
        Objects.equals(this.disabled, deviceSettingsLANInterface.disabled) &&
        Objects.equals(this.l2, deviceSettingsLANInterface.l2) &&
        Objects.equals(this.portMode, deviceSettingsLANInterface.portMode) &&
        Objects.equals(this.space, deviceSettingsLANInterface.space) &&
        Objects.equals(this.type, deviceSettingsLANInterface.type) &&
        Objects.equals(this.untaggedVlan, deviceSettingsLANInterface.untaggedVlan) &&
        Objects.equals(this.vlanIds, deviceSettingsLANInterface.vlanIds) &&
        Objects.equals(this.authenticationType, deviceSettingsLANInterface.authenticationType) &&
        Objects.equals(this.broadcastSsid, deviceSettingsLANInterface.broadcastSsid) &&
        Objects.equals(this.passphrase, deviceSettingsLANInterface.passphrase) &&
        Objects.equals(this.ssid, deviceSettingsLANInterface.ssid) &&
        Objects.equals(this.securityMode, deviceSettingsLANInterface.securityMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(override, name, cwp, disabled, l2, portMode, space, type, untaggedVlan, vlanIds, authenticationType, broadcastSsid, passphrase, ssid, securityMode);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceSettingsLANInterface {\n");
    
    sb.append("    override: ").append(toIndentedString(override)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    cwp: ").append(toIndentedString(cwp)).append("\n");
    sb.append("    disabled: ").append(toIndentedString(disabled)).append("\n");
    sb.append("    l2: ").append(toIndentedString(l2)).append("\n");
    sb.append("    portMode: ").append(toIndentedString(portMode)).append("\n");
    sb.append("    space: ").append(toIndentedString(space)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    untaggedVlan: ").append(toIndentedString(untaggedVlan)).append("\n");
    sb.append("    vlanIds: ").append(toIndentedString(vlanIds)).append("\n");
    sb.append("    authenticationType: ").append(toIndentedString(authenticationType)).append("\n");
    sb.append("    broadcastSsid: ").append(toIndentedString(broadcastSsid)).append("\n");
    sb.append("    passphrase: ").append(toIndentedString(passphrase)).append("\n");
    sb.append("    ssid: ").append(toIndentedString(ssid)).append("\n");
    sb.append("    securityMode: ").append(toIndentedString(securityMode)).append("\n");
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
