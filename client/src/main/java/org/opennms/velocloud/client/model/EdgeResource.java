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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.opennms.velocloud.client.model.BaseApiResource;
import org.opennms.velocloud.client.model.BaseApiResource1;
import org.opennms.velocloud.client.model.BaseApiResource2;
/**
 * EdgeResource
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")
public class EdgeResource {
  @JsonProperty("_href")
  private String _href = null;

  @JsonProperty("activationKey")
  private String activationKey = null;

  @JsonProperty("activationKeyExpires")
  private OffsetDateTime activationKeyExpires = null;

  /**
   * Gets or Sets activationState
   */
  public enum ActivationStateEnum {
    UNASSIGNED("UNASSIGNED"),
    PENDING("PENDING"),
    ACTIVATED("ACTIVATED"),
    REACTIVATION_PENDING("REACTIVATION_PENDING");

    private String value;

    ActivationStateEnum(String value) {
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
    public static ActivationStateEnum fromValue(String text) {
      for (ActivationStateEnum b : ActivationStateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("activationState")
  private ActivationStateEnum activationState = null;

  @JsonProperty("activationTime")
  private OffsetDateTime activationTime = null;

  @JsonProperty("alertsEnabled")
  private Boolean alertsEnabled = null;

  /**
   * Gets or Sets bastionState
   */
  public enum BastionStateEnum {
    UNCONFIGURED("UNCONFIGURED"),
    STAGE_REQUESTED("STAGE_REQUESTED"),
    UNSTAGE_REQUESTED("UNSTAGE_REQUESTED"),
    STAGED("STAGED"),
    UNSTAGED("UNSTAGED"),
    PROMOTION_REQUESTED("PROMOTION_REQUESTED"),
    PROMOTION_PENDING("PROMOTION_PENDING"),
    PROMOTED("PROMOTED");

    private String value;

    BastionStateEnum(String value) {
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
    public static BastionStateEnum fromValue(String text) {
      for (BastionStateEnum b : BastionStateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("bastionState")
  private BastionStateEnum bastionState = null;

  @JsonProperty("buildNumber")
  private String buildNumber = null;

  @JsonProperty("created")
  private OffsetDateTime created = null;

  @JsonProperty("customInfo")
  private String customInfo = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("deviceFamily")
  private String deviceFamily = null;

  @JsonProperty("deviceId")
  private String deviceId = null;

  @JsonProperty("dnsName")
  private String dnsName = null;

  /**
   * Gets or Sets edgeState
   */
  public enum EdgeStateEnum {
    NEVER_ACTIVATED("NEVER_ACTIVATED"),
    DEGRADED("DEGRADED"),
    OFFLINE("OFFLINE"),
    DISABLED("DISABLED"),
    EXPIRED("EXPIRED"),
    CONNECTED("CONNECTED");

    private String value;

    EdgeStateEnum(String value) {
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
    public static EdgeStateEnum fromValue(String text) {
      for (EdgeStateEnum b : EdgeStateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("edgeState")
  private EdgeStateEnum edgeState = null;

  @JsonProperty("edgeStateTime")
  private OffsetDateTime edgeStateTime = null;

  /**
   * Gets or Sets endpointPkiMode
   */
  public enum EndpointPkiModeEnum {
    DISABLED("CERTIFICATE_DISABLED"),
    OPTIONAL("CERTIFICATE_OPTIONAL"),
    REQUIRED("CERTIFICATE_REQUIRED");

    private String value;

    EndpointPkiModeEnum(String value) {
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
    public static EndpointPkiModeEnum fromValue(String text) {
      for (EndpointPkiModeEnum b : EndpointPkiModeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("endpointPkiMode")
  private EndpointPkiModeEnum endpointPkiMode = null;

  @JsonProperty("factoryBuildNumber")
  private String factoryBuildNumber = null;

  @JsonProperty("factorySoftwareVersion")
  private String factorySoftwareVersion = null;

  @JsonProperty("platformBuildNumber")
  private String platformBuildNumber = null;

  @JsonProperty("platformFirmwareVersion")
  private String platformFirmwareVersion = null;

  @JsonProperty("modemBuildNumber")
  private String modemBuildNumber = null;

  @JsonProperty("modemFirmwareVersion")
  private String modemFirmwareVersion = null;

  @JsonProperty("lteRegion")
  private String lteRegion = null;

  @JsonProperty("haLastContact")
  private OffsetDateTime haLastContact = null;

  /**
   * Gets or Sets haPreviousState
   */
  public enum HaPreviousStateEnum {
    UNCONFIGURED("UNCONFIGURED"),
    PENDING_INIT("PENDING_INIT"),
    PENDING_CONFIRMATION("PENDING_CONFIRMATION"),
    PENDING_CONFIRMED("PENDING_CONFIRMED"),
    PENDING_DISSOCIATION("PENDING_DISSOCIATION"),
    READY("READY"),
    FAILED("FAILED");

    private String value;

    HaPreviousStateEnum(String value) {
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
    public static HaPreviousStateEnum fromValue(String text) {
      for (HaPreviousStateEnum b : HaPreviousStateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("haPreviousState")
  private HaPreviousStateEnum haPreviousState = null;

  @JsonProperty("haSerialNumber")
  private String haSerialNumber = null;

  /**
   * Gets or Sets haState
   */
  public enum HaStateEnum {
    UNCONFIGURED("UNCONFIGURED"),
    PENDING_INIT("PENDING_INIT"),
    PENDING_CONFIRMATION("PENDING_CONFIRMATION"),
    PENDING_CONFIRMED("PENDING_CONFIRMED"),
    PENDING_DISSOCIATION("PENDING_DISSOCIATION"),
    READY("READY"),
    FAILED("FAILED");

    private String value;

    HaStateEnum(String value) {
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
    public static HaStateEnum fromValue(String text) {
      for (HaStateEnum b : HaStateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("haState")
  private HaStateEnum haState = null;

  @JsonProperty("isLive")
  private Boolean isLive = null;

  @JsonProperty("lastContact")
  private OffsetDateTime lastContact = null;

  @JsonProperty("logicalId")
  private String logicalId = null;

  @JsonProperty("modelNumber")
  private String modelNumber = null;

  @JsonProperty("modified")
  private OffsetDateTime modified = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("operatorAlertsEnabled")
  private Boolean operatorAlertsEnabled = null;

  @JsonProperty("selfMacAddress")
  private String selfMacAddress = null;

  @JsonProperty("serialNumber")
  private String serialNumber = null;

  /**
   * Gets or Sets serviceState
   */
  public enum ServiceStateEnum {
    IN_SERVICE("IN_SERVICE"),
    OUT_OF_SERVICE("OUT_OF_SERVICE"),
    PENDING_SERVICE("PENDING_SERVICE");

    private String value;

    ServiceStateEnum(String value) {
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
    public static ServiceStateEnum fromValue(String text) {
      for (ServiceStateEnum b : ServiceStateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("serviceState")
  private ServiceStateEnum serviceState = null;

  @JsonProperty("serviceUpSince")
  private OffsetDateTime serviceUpSince = null;

  @JsonProperty("softwareUpdated")
  private OffsetDateTime softwareUpdated = null;

  @JsonProperty("softwareVersion")
  private String softwareVersion = null;

  @JsonProperty("systemUpSince")
  private OffsetDateTime systemUpSince = null;

  @JsonProperty("links")
  private List<BaseApiResource> links = null;

  @JsonProperty("site")
  private BaseApiResource2 site = null;

  @JsonProperty("enterprise")
  private BaseApiResource1 enterprise = null;

  public EdgeResource _href(String _href) {
    this._href = _href;
    return this;
  }

   /**
   * Get _href
   * @return _href
  **/
  @Schema(description = "")
  public String getHref() {
    return _href;
  }

  public void setHref(String _href) {
    this._href = _href;
  }

  public EdgeResource activationKey(String activationKey) {
    this.activationKey = activationKey;
    return this;
  }

   /**
   * Get activationKey
   * @return activationKey
  **/
  @Schema(description = "")
  public String getActivationKey() {
    return activationKey;
  }

  public void setActivationKey(String activationKey) {
    this.activationKey = activationKey;
  }

  public EdgeResource activationKeyExpires(OffsetDateTime activationKeyExpires) {
    this.activationKeyExpires = activationKeyExpires;
    return this;
  }

   /**
   * Get activationKeyExpires
   * @return activationKeyExpires
  **/
  @Schema(description = "")
  public OffsetDateTime getActivationKeyExpires() {
    return activationKeyExpires;
  }

  public void setActivationKeyExpires(OffsetDateTime activationKeyExpires) {
    this.activationKeyExpires = activationKeyExpires;
  }

  public EdgeResource activationState(ActivationStateEnum activationState) {
    this.activationState = activationState;
    return this;
  }

   /**
   * Get activationState
   * @return activationState
  **/
  @Schema(description = "")
  public ActivationStateEnum getActivationState() {
    return activationState;
  }

  public void setActivationState(ActivationStateEnum activationState) {
    this.activationState = activationState;
  }

  public EdgeResource activationTime(OffsetDateTime activationTime) {
    this.activationTime = activationTime;
    return this;
  }

   /**
   * Get activationTime
   * @return activationTime
  **/
  @Schema(description = "")
  public OffsetDateTime getActivationTime() {
    return activationTime;
  }

  public void setActivationTime(OffsetDateTime activationTime) {
    this.activationTime = activationTime;
  }

  public EdgeResource alertsEnabled(Boolean alertsEnabled) {
    this.alertsEnabled = alertsEnabled;
    return this;
  }

   /**
   * Get alertsEnabled
   * @return alertsEnabled
  **/
  @Schema(description = "")
  public Boolean isAlertsEnabled() {
    return alertsEnabled;
  }

  public void setAlertsEnabled(Boolean alertsEnabled) {
    this.alertsEnabled = alertsEnabled;
  }

  public EdgeResource bastionState(BastionStateEnum bastionState) {
    this.bastionState = bastionState;
    return this;
  }

   /**
   * Get bastionState
   * @return bastionState
  **/
  @Schema(description = "")
  public BastionStateEnum getBastionState() {
    return bastionState;
  }

  public void setBastionState(BastionStateEnum bastionState) {
    this.bastionState = bastionState;
  }

  public EdgeResource buildNumber(String buildNumber) {
    this.buildNumber = buildNumber;
    return this;
  }

   /**
   * Get buildNumber
   * @return buildNumber
  **/
  @Schema(description = "")
  public String getBuildNumber() {
    return buildNumber;
  }

  public void setBuildNumber(String buildNumber) {
    this.buildNumber = buildNumber;
  }

  public EdgeResource created(OffsetDateTime created) {
    this.created = created;
    return this;
  }

   /**
   * Get created
   * @return created
  **/
  @Schema(description = "")
  public OffsetDateTime getCreated() {
    return created;
  }

  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  public EdgeResource customInfo(String customInfo) {
    this.customInfo = customInfo;
    return this;
  }

   /**
   * Get customInfo
   * @return customInfo
  **/
  @Schema(description = "")
  public String getCustomInfo() {
    return customInfo;
  }

  public void setCustomInfo(String customInfo) {
    this.customInfo = customInfo;
  }

  public EdgeResource description(String description) {
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

  public EdgeResource deviceFamily(String deviceFamily) {
    this.deviceFamily = deviceFamily;
    return this;
  }

   /**
   * Get deviceFamily
   * @return deviceFamily
  **/
  @Schema(description = "")
  public String getDeviceFamily() {
    return deviceFamily;
  }

  public void setDeviceFamily(String deviceFamily) {
    this.deviceFamily = deviceFamily;
  }

  public EdgeResource deviceId(String deviceId) {
    this.deviceId = deviceId;
    return this;
  }

   /**
   * Get deviceId
   * @return deviceId
  **/
  @Schema(description = "")
  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public EdgeResource dnsName(String dnsName) {
    this.dnsName = dnsName;
    return this;
  }

   /**
   * Get dnsName
   * @return dnsName
  **/
  @Schema(description = "")
  public String getDnsName() {
    return dnsName;
  }

  public void setDnsName(String dnsName) {
    this.dnsName = dnsName;
  }

  public EdgeResource edgeState(EdgeStateEnum edgeState) {
    this.edgeState = edgeState;
    return this;
  }

   /**
   * Get edgeState
   * @return edgeState
  **/
  @Schema(description = "")
  public EdgeStateEnum getEdgeState() {
    return edgeState;
  }

  public void setEdgeState(EdgeStateEnum edgeState) {
    this.edgeState = edgeState;
  }

  public EdgeResource edgeStateTime(OffsetDateTime edgeStateTime) {
    this.edgeStateTime = edgeStateTime;
    return this;
  }

   /**
   * Get edgeStateTime
   * @return edgeStateTime
  **/
  @Schema(description = "")
  public OffsetDateTime getEdgeStateTime() {
    return edgeStateTime;
  }

  public void setEdgeStateTime(OffsetDateTime edgeStateTime) {
    this.edgeStateTime = edgeStateTime;
  }

  public EdgeResource endpointPkiMode(EndpointPkiModeEnum endpointPkiMode) {
    this.endpointPkiMode = endpointPkiMode;
    return this;
  }

   /**
   * Get endpointPkiMode
   * @return endpointPkiMode
  **/
  @Schema(description = "")
  public EndpointPkiModeEnum getEndpointPkiMode() {
    return endpointPkiMode;
  }

  public void setEndpointPkiMode(EndpointPkiModeEnum endpointPkiMode) {
    this.endpointPkiMode = endpointPkiMode;
  }

  public EdgeResource factoryBuildNumber(String factoryBuildNumber) {
    this.factoryBuildNumber = factoryBuildNumber;
    return this;
  }

   /**
   * Get factoryBuildNumber
   * @return factoryBuildNumber
  **/
  @Schema(description = "")
  public String getFactoryBuildNumber() {
    return factoryBuildNumber;
  }

  public void setFactoryBuildNumber(String factoryBuildNumber) {
    this.factoryBuildNumber = factoryBuildNumber;
  }

  public EdgeResource factorySoftwareVersion(String factorySoftwareVersion) {
    this.factorySoftwareVersion = factorySoftwareVersion;
    return this;
  }

   /**
   * Get factorySoftwareVersion
   * @return factorySoftwareVersion
  **/
  @Schema(description = "")
  public String getFactorySoftwareVersion() {
    return factorySoftwareVersion;
  }

  public void setFactorySoftwareVersion(String factorySoftwareVersion) {
    this.factorySoftwareVersion = factorySoftwareVersion;
  }

  public EdgeResource platformBuildNumber(String platformBuildNumber) {
    this.platformBuildNumber = platformBuildNumber;
    return this;
  }

   /**
   * Get platformBuildNumber
   * @return platformBuildNumber
  **/
  @Schema(description = "")
  public String getPlatformBuildNumber() {
    return platformBuildNumber;
  }

  public void setPlatformBuildNumber(String platformBuildNumber) {
    this.platformBuildNumber = platformBuildNumber;
  }

  public EdgeResource platformFirmwareVersion(String platformFirmwareVersion) {
    this.platformFirmwareVersion = platformFirmwareVersion;
    return this;
  }

   /**
   * Get platformFirmwareVersion
   * @return platformFirmwareVersion
  **/
  @Schema(description = "")
  public String getPlatformFirmwareVersion() {
    return platformFirmwareVersion;
  }

  public void setPlatformFirmwareVersion(String platformFirmwareVersion) {
    this.platformFirmwareVersion = platformFirmwareVersion;
  }

  public EdgeResource modemBuildNumber(String modemBuildNumber) {
    this.modemBuildNumber = modemBuildNumber;
    return this;
  }

   /**
   * Get modemBuildNumber
   * @return modemBuildNumber
  **/
  @Schema(description = "")
  public String getModemBuildNumber() {
    return modemBuildNumber;
  }

  public void setModemBuildNumber(String modemBuildNumber) {
    this.modemBuildNumber = modemBuildNumber;
  }

  public EdgeResource modemFirmwareVersion(String modemFirmwareVersion) {
    this.modemFirmwareVersion = modemFirmwareVersion;
    return this;
  }

   /**
   * Get modemFirmwareVersion
   * @return modemFirmwareVersion
  **/
  @Schema(description = "")
  public String getModemFirmwareVersion() {
    return modemFirmwareVersion;
  }

  public void setModemFirmwareVersion(String modemFirmwareVersion) {
    this.modemFirmwareVersion = modemFirmwareVersion;
  }

  public EdgeResource lteRegion(String lteRegion) {
    this.lteRegion = lteRegion;
    return this;
  }

   /**
   * Get lteRegion
   * @return lteRegion
  **/
  @Schema(description = "")
  public String getLteRegion() {
    return lteRegion;
  }

  public void setLteRegion(String lteRegion) {
    this.lteRegion = lteRegion;
  }

  public EdgeResource haLastContact(OffsetDateTime haLastContact) {
    this.haLastContact = haLastContact;
    return this;
  }

   /**
   * Get haLastContact
   * @return haLastContact
  **/
  @Schema(description = "")
  public OffsetDateTime getHaLastContact() {
    return haLastContact;
  }

  public void setHaLastContact(OffsetDateTime haLastContact) {
    this.haLastContact = haLastContact;
  }

  public EdgeResource haPreviousState(HaPreviousStateEnum haPreviousState) {
    this.haPreviousState = haPreviousState;
    return this;
  }

   /**
   * Get haPreviousState
   * @return haPreviousState
  **/
  @Schema(description = "")
  public HaPreviousStateEnum getHaPreviousState() {
    return haPreviousState;
  }

  public void setHaPreviousState(HaPreviousStateEnum haPreviousState) {
    this.haPreviousState = haPreviousState;
  }

  public EdgeResource haSerialNumber(String haSerialNumber) {
    this.haSerialNumber = haSerialNumber;
    return this;
  }

   /**
   * Get haSerialNumber
   * @return haSerialNumber
  **/
  @Schema(description = "")
  public String getHaSerialNumber() {
    return haSerialNumber;
  }

  public void setHaSerialNumber(String haSerialNumber) {
    this.haSerialNumber = haSerialNumber;
  }

  public EdgeResource haState(HaStateEnum haState) {
    this.haState = haState;
    return this;
  }

   /**
   * Get haState
   * @return haState
  **/
  @Schema(description = "")
  public HaStateEnum getHaState() {
    return haState;
  }

  public void setHaState(HaStateEnum haState) {
    this.haState = haState;
  }

  public EdgeResource isLive(Boolean isLive) {
    this.isLive = isLive;
    return this;
  }

   /**
   * Get isLive
   * @return isLive
  **/
  @Schema(description = "")
  public Boolean isIsLive() {
    return isLive;
  }

  public void setIsLive(Boolean isLive) {
    this.isLive = isLive;
  }

  public EdgeResource lastContact(OffsetDateTime lastContact) {
    this.lastContact = lastContact;
    return this;
  }

   /**
   * Get lastContact
   * @return lastContact
  **/
  @Schema(description = "")
  public OffsetDateTime getLastContact() {
    return lastContact;
  }

  public void setLastContact(OffsetDateTime lastContact) {
    this.lastContact = lastContact;
  }

  public EdgeResource logicalId(String logicalId) {
    this.logicalId = logicalId;
    return this;
  }

   /**
   * Get logicalId
   * @return logicalId
  **/
  @Schema(description = "")
  public String getLogicalId() {
    return logicalId;
  }

  public void setLogicalId(String logicalId) {
    this.logicalId = logicalId;
  }

  public EdgeResource modelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
    return this;
  }

   /**
   * Get modelNumber
   * @return modelNumber
  **/
  @Schema(description = "")
  public String getModelNumber() {
    return modelNumber;
  }

  public void setModelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
  }

  public EdgeResource modified(OffsetDateTime modified) {
    this.modified = modified;
    return this;
  }

   /**
   * Get modified
   * @return modified
  **/
  @Schema(description = "")
  public OffsetDateTime getModified() {
    return modified;
  }

  public void setModified(OffsetDateTime modified) {
    this.modified = modified;
  }

  public EdgeResource name(String name) {
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

  public EdgeResource operatorAlertsEnabled(Boolean operatorAlertsEnabled) {
    this.operatorAlertsEnabled = operatorAlertsEnabled;
    return this;
  }

   /**
   * Get operatorAlertsEnabled
   * @return operatorAlertsEnabled
  **/
  @Schema(description = "")
  public Boolean isOperatorAlertsEnabled() {
    return operatorAlertsEnabled;
  }

  public void setOperatorAlertsEnabled(Boolean operatorAlertsEnabled) {
    this.operatorAlertsEnabled = operatorAlertsEnabled;
  }

  public EdgeResource selfMacAddress(String selfMacAddress) {
    this.selfMacAddress = selfMacAddress;
    return this;
  }

   /**
   * Get selfMacAddress
   * @return selfMacAddress
  **/
  @Schema(description = "")
  public String getSelfMacAddress() {
    return selfMacAddress;
  }

  public void setSelfMacAddress(String selfMacAddress) {
    this.selfMacAddress = selfMacAddress;
  }

  public EdgeResource serialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
    return this;
  }

   /**
   * Get serialNumber
   * @return serialNumber
  **/
  @Schema(description = "")
  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public EdgeResource serviceState(ServiceStateEnum serviceState) {
    this.serviceState = serviceState;
    return this;
  }

   /**
   * Get serviceState
   * @return serviceState
  **/
  @Schema(description = "")
  public ServiceStateEnum getServiceState() {
    return serviceState;
  }

  public void setServiceState(ServiceStateEnum serviceState) {
    this.serviceState = serviceState;
  }

  public EdgeResource serviceUpSince(OffsetDateTime serviceUpSince) {
    this.serviceUpSince = serviceUpSince;
    return this;
  }

   /**
   * Get serviceUpSince
   * @return serviceUpSince
  **/
  @Schema(description = "")
  public OffsetDateTime getServiceUpSince() {
    return serviceUpSince;
  }

  public void setServiceUpSince(OffsetDateTime serviceUpSince) {
    this.serviceUpSince = serviceUpSince;
  }

  public EdgeResource softwareUpdated(OffsetDateTime softwareUpdated) {
    this.softwareUpdated = softwareUpdated;
    return this;
  }

   /**
   * Get softwareUpdated
   * @return softwareUpdated
  **/
  @Schema(description = "")
  public OffsetDateTime getSoftwareUpdated() {
    return softwareUpdated;
  }

  public void setSoftwareUpdated(OffsetDateTime softwareUpdated) {
    this.softwareUpdated = softwareUpdated;
  }

  public EdgeResource softwareVersion(String softwareVersion) {
    this.softwareVersion = softwareVersion;
    return this;
  }

   /**
   * Get softwareVersion
   * @return softwareVersion
  **/
  @Schema(description = "")
  public String getSoftwareVersion() {
    return softwareVersion;
  }

  public void setSoftwareVersion(String softwareVersion) {
    this.softwareVersion = softwareVersion;
  }

  public EdgeResource systemUpSince(OffsetDateTime systemUpSince) {
    this.systemUpSince = systemUpSince;
    return this;
  }

   /**
   * Get systemUpSince
   * @return systemUpSince
  **/
  @Schema(description = "")
  public OffsetDateTime getSystemUpSince() {
    return systemUpSince;
  }

  public void setSystemUpSince(OffsetDateTime systemUpSince) {
    this.systemUpSince = systemUpSince;
  }

  public EdgeResource links(List<BaseApiResource> links) {
    this.links = links;
    return this;
  }

  public EdgeResource addLinksItem(BaseApiResource linksItem) {
    if (this.links == null) {
      this.links = new ArrayList<>();
    }
    this.links.add(linksItem);
    return this;
  }

   /**
   * Get links
   * @return links
  **/
  @Schema(description = "")
  public List<BaseApiResource> getLinks() {
    return links;
  }

  public void setLinks(List<BaseApiResource> links) {
    this.links = links;
  }

  public EdgeResource site(BaseApiResource2 site) {
    this.site = site;
    return this;
  }

   /**
   * Get site
   * @return site
  **/
  @Schema(description = "")
  public BaseApiResource2 getSite() {
    return site;
  }

  public void setSite(BaseApiResource2 site) {
    this.site = site;
  }

  public EdgeResource enterprise(BaseApiResource1 enterprise) {
    this.enterprise = enterprise;
    return this;
  }

   /**
   * Get enterprise
   * @return enterprise
  **/
  @Schema(description = "")
  public BaseApiResource1 getEnterprise() {
    return enterprise;
  }

  public void setEnterprise(BaseApiResource1 enterprise) {
    this.enterprise = enterprise;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EdgeResource edgeResource = (EdgeResource) o;
    return Objects.equals(this._href, edgeResource._href) &&
        Objects.equals(this.activationKey, edgeResource.activationKey) &&
        Objects.equals(this.activationKeyExpires, edgeResource.activationKeyExpires) &&
        Objects.equals(this.activationState, edgeResource.activationState) &&
        Objects.equals(this.activationTime, edgeResource.activationTime) &&
        Objects.equals(this.alertsEnabled, edgeResource.alertsEnabled) &&
        Objects.equals(this.bastionState, edgeResource.bastionState) &&
        Objects.equals(this.buildNumber, edgeResource.buildNumber) &&
        Objects.equals(this.created, edgeResource.created) &&
        Objects.equals(this.customInfo, edgeResource.customInfo) &&
        Objects.equals(this.description, edgeResource.description) &&
        Objects.equals(this.deviceFamily, edgeResource.deviceFamily) &&
        Objects.equals(this.deviceId, edgeResource.deviceId) &&
        Objects.equals(this.dnsName, edgeResource.dnsName) &&
        Objects.equals(this.edgeState, edgeResource.edgeState) &&
        Objects.equals(this.edgeStateTime, edgeResource.edgeStateTime) &&
        Objects.equals(this.endpointPkiMode, edgeResource.endpointPkiMode) &&
        Objects.equals(this.factoryBuildNumber, edgeResource.factoryBuildNumber) &&
        Objects.equals(this.factorySoftwareVersion, edgeResource.factorySoftwareVersion) &&
        Objects.equals(this.platformBuildNumber, edgeResource.platformBuildNumber) &&
        Objects.equals(this.platformFirmwareVersion, edgeResource.platformFirmwareVersion) &&
        Objects.equals(this.modemBuildNumber, edgeResource.modemBuildNumber) &&
        Objects.equals(this.modemFirmwareVersion, edgeResource.modemFirmwareVersion) &&
        Objects.equals(this.lteRegion, edgeResource.lteRegion) &&
        Objects.equals(this.haLastContact, edgeResource.haLastContact) &&
        Objects.equals(this.haPreviousState, edgeResource.haPreviousState) &&
        Objects.equals(this.haSerialNumber, edgeResource.haSerialNumber) &&
        Objects.equals(this.haState, edgeResource.haState) &&
        Objects.equals(this.isLive, edgeResource.isLive) &&
        Objects.equals(this.lastContact, edgeResource.lastContact) &&
        Objects.equals(this.logicalId, edgeResource.logicalId) &&
        Objects.equals(this.modelNumber, edgeResource.modelNumber) &&
        Objects.equals(this.modified, edgeResource.modified) &&
        Objects.equals(this.name, edgeResource.name) &&
        Objects.equals(this.operatorAlertsEnabled, edgeResource.operatorAlertsEnabled) &&
        Objects.equals(this.selfMacAddress, edgeResource.selfMacAddress) &&
        Objects.equals(this.serialNumber, edgeResource.serialNumber) &&
        Objects.equals(this.serviceState, edgeResource.serviceState) &&
        Objects.equals(this.serviceUpSince, edgeResource.serviceUpSince) &&
        Objects.equals(this.softwareUpdated, edgeResource.softwareUpdated) &&
        Objects.equals(this.softwareVersion, edgeResource.softwareVersion) &&
        Objects.equals(this.systemUpSince, edgeResource.systemUpSince) &&
        Objects.equals(this.links, edgeResource.links) &&
        Objects.equals(this.site, edgeResource.site) &&
        Objects.equals(this.enterprise, edgeResource.enterprise);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_href, activationKey, activationKeyExpires, activationState, activationTime, alertsEnabled, bastionState, buildNumber, created, customInfo, description, deviceFamily, deviceId, dnsName, edgeState, edgeStateTime, endpointPkiMode, factoryBuildNumber, factorySoftwareVersion, platformBuildNumber, platformFirmwareVersion, modemBuildNumber, modemFirmwareVersion, lteRegion, haLastContact, haPreviousState, haSerialNumber, haState, isLive, lastContact, logicalId, modelNumber, modified, name, operatorAlertsEnabled, selfMacAddress, serialNumber, serviceState, serviceUpSince, softwareUpdated, softwareVersion, systemUpSince, links, site, enterprise);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EdgeResource {\n");
    
    sb.append("    _href: ").append(toIndentedString(_href)).append("\n");
    sb.append("    activationKey: ").append(toIndentedString(activationKey)).append("\n");
    sb.append("    activationKeyExpires: ").append(toIndentedString(activationKeyExpires)).append("\n");
    sb.append("    activationState: ").append(toIndentedString(activationState)).append("\n");
    sb.append("    activationTime: ").append(toIndentedString(activationTime)).append("\n");
    sb.append("    alertsEnabled: ").append(toIndentedString(alertsEnabled)).append("\n");
    sb.append("    bastionState: ").append(toIndentedString(bastionState)).append("\n");
    sb.append("    buildNumber: ").append(toIndentedString(buildNumber)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    customInfo: ").append(toIndentedString(customInfo)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    deviceFamily: ").append(toIndentedString(deviceFamily)).append("\n");
    sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
    sb.append("    dnsName: ").append(toIndentedString(dnsName)).append("\n");
    sb.append("    edgeState: ").append(toIndentedString(edgeState)).append("\n");
    sb.append("    edgeStateTime: ").append(toIndentedString(edgeStateTime)).append("\n");
    sb.append("    endpointPkiMode: ").append(toIndentedString(endpointPkiMode)).append("\n");
    sb.append("    factoryBuildNumber: ").append(toIndentedString(factoryBuildNumber)).append("\n");
    sb.append("    factorySoftwareVersion: ").append(toIndentedString(factorySoftwareVersion)).append("\n");
    sb.append("    platformBuildNumber: ").append(toIndentedString(platformBuildNumber)).append("\n");
    sb.append("    platformFirmwareVersion: ").append(toIndentedString(platformFirmwareVersion)).append("\n");
    sb.append("    modemBuildNumber: ").append(toIndentedString(modemBuildNumber)).append("\n");
    sb.append("    modemFirmwareVersion: ").append(toIndentedString(modemFirmwareVersion)).append("\n");
    sb.append("    lteRegion: ").append(toIndentedString(lteRegion)).append("\n");
    sb.append("    haLastContact: ").append(toIndentedString(haLastContact)).append("\n");
    sb.append("    haPreviousState: ").append(toIndentedString(haPreviousState)).append("\n");
    sb.append("    haSerialNumber: ").append(toIndentedString(haSerialNumber)).append("\n");
    sb.append("    haState: ").append(toIndentedString(haState)).append("\n");
    sb.append("    isLive: ").append(toIndentedString(isLive)).append("\n");
    sb.append("    lastContact: ").append(toIndentedString(lastContact)).append("\n");
    sb.append("    logicalId: ").append(toIndentedString(logicalId)).append("\n");
    sb.append("    modelNumber: ").append(toIndentedString(modelNumber)).append("\n");
    sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    operatorAlertsEnabled: ").append(toIndentedString(operatorAlertsEnabled)).append("\n");
    sb.append("    selfMacAddress: ").append(toIndentedString(selfMacAddress)).append("\n");
    sb.append("    serialNumber: ").append(toIndentedString(serialNumber)).append("\n");
    sb.append("    serviceState: ").append(toIndentedString(serviceState)).append("\n");
    sb.append("    serviceUpSince: ").append(toIndentedString(serviceUpSince)).append("\n");
    sb.append("    softwareUpdated: ").append(toIndentedString(softwareUpdated)).append("\n");
    sb.append("    softwareVersion: ").append(toIndentedString(softwareVersion)).append("\n");
    sb.append("    systemUpSince: ").append(toIndentedString(systemUpSince)).append("\n");
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
    sb.append("    site: ").append(toIndentedString(site)).append("\n");
    sb.append("    enterprise: ").append(toIndentedString(enterprise)).append("\n");
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
