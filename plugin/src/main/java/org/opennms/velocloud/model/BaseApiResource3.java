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
import java.time.OffsetDateTime;
import org.opennms.velocloud.model.BaseApiResource2;
/**
 * BaseApiResource3
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class BaseApiResource3 {
  @SerializedName("_href")
  private String _href = null;

  @SerializedName("created")
  private OffsetDateTime created = null;

  @SerializedName("edge")
  private BaseApiResource2 edge = null;

  @SerializedName("logicalId")
  private String logicalId = null;

  @SerializedName("internalId")
  private String internalId = null;

  @SerializedName("interface")
  private String _interface = null;

  @SerializedName("macAddress")
  private String macAddress = null;

  @SerializedName("ipAddress")
  private String ipAddress = null;

  @SerializedName("ipV6Address")
  private String ipV6Address = null;

  @SerializedName("netmask")
  private String netmask = null;

  /**
   * Gets or Sets networkSide
   */
  @JsonAdapter(NetworkSideEnum.Adapter.class)
  public enum NetworkSideEnum {
    UNKOWN("UNKOWN"),
    WAN("WAN"),
    LAN("LAN");

    private String value;

    NetworkSideEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static NetworkSideEnum fromValue(String text) {
      for (NetworkSideEnum b : NetworkSideEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<NetworkSideEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final NetworkSideEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public NetworkSideEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return NetworkSideEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("networkSide")
  private NetworkSideEnum networkSide = null;

  /**
   * Gets or Sets networkType
   */
  @JsonAdapter(NetworkTypeEnum.Adapter.class)
  public enum NetworkTypeEnum {
    UNKNOWN("UNKNOWN"),
    WIRELESS("WIRELESS"),
    ETHERNET("ETHERNET"),
    WIFI("WIFI");

    private String value;

    NetworkTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static NetworkTypeEnum fromValue(String text) {
      for (NetworkTypeEnum b : NetworkTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<NetworkTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final NetworkTypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public NetworkTypeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return NetworkTypeEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("networkType")
  private NetworkTypeEnum networkType = null;

  @SerializedName("displayName")
  private String displayName = null;

  @SerializedName("userOverride")
  private Boolean userOverride = null;

  @SerializedName("isp")
  private String isp = null;

  @SerializedName("org")
  private String org = null;

  @SerializedName("lat")
  private Float lat = null;

  @SerializedName("lon")
  private Float lon = null;

  @SerializedName("lastActive")
  private OffsetDateTime lastActive = null;

  /**
   * Gets or Sets state
   */
  @JsonAdapter(StateEnum.Adapter.class)
  public enum StateEnum {
    UNKNOWN("UNKNOWN"),
    STABLE("STABLE"),
    UNSTABLE("UNSTABLE"),
    DISCONNECTED("DISCONNECTED"),
    QUIET("QUIET"),
    INITIAL("INITIAL"),
    STANDBY("STANDBY");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StateEnum fromValue(String text) {
      for (StateEnum b : StateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StateEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public StateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return StateEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("state")
  private StateEnum state = null;

  /**
   * Gets or Sets backupState
   */
  @JsonAdapter(BackupStateEnum.Adapter.class)
  public enum BackupStateEnum {
    UNCONFIGURED("UNCONFIGURED"),
    STANDBY("STANDBY"),
    ACTIVE("ACTIVE");

    private String value;

    BackupStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static BackupStateEnum fromValue(String text) {
      for (BackupStateEnum b : BackupStateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<BackupStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final BackupStateEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public BackupStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return BackupStateEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("backupState")
  private BackupStateEnum backupState = null;

  /**
   * Gets or Sets linkMode
   */
  @JsonAdapter(LinkModeEnum.Adapter.class)
  public enum LinkModeEnum {
    ACTIVE("ACTIVE"),
    BACKUP("BACKUP"),
    HOTSTANDBY("HOTSTANDBY");

    private String value;

    LinkModeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static LinkModeEnum fromValue(String text) {
      for (LinkModeEnum b : LinkModeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<LinkModeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final LinkModeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public LinkModeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return LinkModeEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("linkMode")
  private LinkModeEnum linkMode = null;

  @SerializedName("lastEvent")
  private OffsetDateTime lastEvent = null;

  /**
   * Gets or Sets lastEventState
   */
  @JsonAdapter(LastEventStateEnum.Adapter.class)
  public enum LastEventStateEnum {
    UNKNOWN("UNKNOWN"),
    STABLE("STABLE"),
    UNSTABLE("UNSTABLE"),
    DISCONNECTED("DISCONNECTED"),
    QUIET("QUIET"),
    INITIAL("INITIAL"),
    STANDBY("STANDBY");

    private String value;

    LastEventStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static LastEventStateEnum fromValue(String text) {
      for (LastEventStateEnum b : LastEventStateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<LastEventStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final LastEventStateEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public LastEventStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return LastEventStateEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("lastEventState")
  private LastEventStateEnum lastEventState = null;

  @SerializedName("alertsEnabled")
  private Boolean alertsEnabled = null;

  @SerializedName("operatorAlertsEnabled")
  private Boolean operatorAlertsEnabled = null;

  /**
   * Gets or Sets serviceState
   */
  @JsonAdapter(ServiceStateEnum.Adapter.class)
  public enum ServiceStateEnum {
    IN_SERVICE("IN_SERVICE"),
    OUT_OF_SERVICE("OUT_OF_SERVICE"),
    HISTORICAL("HISTORICAL");

    private String value;

    ServiceStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ServiceStateEnum fromValue(String text) {
      for (ServiceStateEnum b : ServiceStateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ServiceStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ServiceStateEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public ServiceStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return ServiceStateEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("serviceState")
  private ServiceStateEnum serviceState = null;

  @SerializedName("modified")
  private OffsetDateTime modified = null;

  public BaseApiResource3 _href(String _href) {
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

  public BaseApiResource3 created(OffsetDateTime created) {
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

  public BaseApiResource3 edge(BaseApiResource2 edge) {
    this.edge = edge;
    return this;
  }

   /**
   * Get edge
   * @return edge
  **/
  @Schema(description = "")
  public BaseApiResource2 getEdge() {
    return edge;
  }

  public void setEdge(BaseApiResource2 edge) {
    this.edge = edge;
  }

  public BaseApiResource3 logicalId(String logicalId) {
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

  public BaseApiResource3 internalId(String internalId) {
    this.internalId = internalId;
    return this;
  }

   /**
   * Get internalId
   * @return internalId
  **/
  @Schema(description = "")
  public String getInternalId() {
    return internalId;
  }

  public void setInternalId(String internalId) {
    this.internalId = internalId;
  }

  public BaseApiResource3 _interface(String _interface) {
    this._interface = _interface;
    return this;
  }

   /**
   * Get _interface
   * @return _interface
  **/
  @Schema(description = "")
  public String getInterface() {
    return _interface;
  }

  public void setInterface(String _interface) {
    this._interface = _interface;
  }

  public BaseApiResource3 macAddress(String macAddress) {
    this.macAddress = macAddress;
    return this;
  }

   /**
   * Get macAddress
   * @return macAddress
  **/
  @Schema(description = "")
  public String getMacAddress() {
    return macAddress;
  }

  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
  }

  public BaseApiResource3 ipAddress(String ipAddress) {
    this.ipAddress = ipAddress;
    return this;
  }

   /**
   * Get ipAddress
   * @return ipAddress
  **/
  @Schema(description = "")
  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public BaseApiResource3 ipV6Address(String ipV6Address) {
    this.ipV6Address = ipV6Address;
    return this;
  }

   /**
   * Get ipV6Address
   * @return ipV6Address
  **/
  @Schema(description = "")
  public String getIpV6Address() {
    return ipV6Address;
  }

  public void setIpV6Address(String ipV6Address) {
    this.ipV6Address = ipV6Address;
  }

  public BaseApiResource3 netmask(String netmask) {
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

  public BaseApiResource3 networkSide(NetworkSideEnum networkSide) {
    this.networkSide = networkSide;
    return this;
  }

   /**
   * Get networkSide
   * @return networkSide
  **/
  @Schema(description = "")
  public NetworkSideEnum getNetworkSide() {
    return networkSide;
  }

  public void setNetworkSide(NetworkSideEnum networkSide) {
    this.networkSide = networkSide;
  }

  public BaseApiResource3 networkType(NetworkTypeEnum networkType) {
    this.networkType = networkType;
    return this;
  }

   /**
   * Get networkType
   * @return networkType
  **/
  @Schema(description = "")
  public NetworkTypeEnum getNetworkType() {
    return networkType;
  }

  public void setNetworkType(NetworkTypeEnum networkType) {
    this.networkType = networkType;
  }

  public BaseApiResource3 displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

   /**
   * Get displayName
   * @return displayName
  **/
  @Schema(description = "")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public BaseApiResource3 userOverride(Boolean userOverride) {
    this.userOverride = userOverride;
    return this;
  }

   /**
   * Get userOverride
   * @return userOverride
  **/
  @Schema(description = "")
  public Boolean isUserOverride() {
    return userOverride;
  }

  public void setUserOverride(Boolean userOverride) {
    this.userOverride = userOverride;
  }

  public BaseApiResource3 isp(String isp) {
    this.isp = isp;
    return this;
  }

   /**
   * Get isp
   * @return isp
  **/
  @Schema(description = "")
  public String getIsp() {
    return isp;
  }

  public void setIsp(String isp) {
    this.isp = isp;
  }

  public BaseApiResource3 org(String org) {
    this.org = org;
    return this;
  }

   /**
   * Get org
   * @return org
  **/
  @Schema(description = "")
  public String getOrg() {
    return org;
  }

  public void setOrg(String org) {
    this.org = org;
  }

  public BaseApiResource3 lat(Float lat) {
    this.lat = lat;
    return this;
  }

   /**
   * Get lat
   * @return lat
  **/
  @Schema(description = "")
  public Float getLat() {
    return lat;
  }

  public void setLat(Float lat) {
    this.lat = lat;
  }

  public BaseApiResource3 lon(Float lon) {
    this.lon = lon;
    return this;
  }

   /**
   * Get lon
   * @return lon
  **/
  @Schema(description = "")
  public Float getLon() {
    return lon;
  }

  public void setLon(Float lon) {
    this.lon = lon;
  }

  public BaseApiResource3 lastActive(OffsetDateTime lastActive) {
    this.lastActive = lastActive;
    return this;
  }

   /**
   * Get lastActive
   * @return lastActive
  **/
  @Schema(description = "")
  public OffsetDateTime getLastActive() {
    return lastActive;
  }

  public void setLastActive(OffsetDateTime lastActive) {
    this.lastActive = lastActive;
  }

  public BaseApiResource3 state(StateEnum state) {
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @Schema(description = "")
  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public BaseApiResource3 backupState(BackupStateEnum backupState) {
    this.backupState = backupState;
    return this;
  }

   /**
   * Get backupState
   * @return backupState
  **/
  @Schema(description = "")
  public BackupStateEnum getBackupState() {
    return backupState;
  }

  public void setBackupState(BackupStateEnum backupState) {
    this.backupState = backupState;
  }

  public BaseApiResource3 linkMode(LinkModeEnum linkMode) {
    this.linkMode = linkMode;
    return this;
  }

   /**
   * Get linkMode
   * @return linkMode
  **/
  @Schema(description = "")
  public LinkModeEnum getLinkMode() {
    return linkMode;
  }

  public void setLinkMode(LinkModeEnum linkMode) {
    this.linkMode = linkMode;
  }

  public BaseApiResource3 lastEvent(OffsetDateTime lastEvent) {
    this.lastEvent = lastEvent;
    return this;
  }

   /**
   * Get lastEvent
   * @return lastEvent
  **/
  @Schema(description = "")
  public OffsetDateTime getLastEvent() {
    return lastEvent;
  }

  public void setLastEvent(OffsetDateTime lastEvent) {
    this.lastEvent = lastEvent;
  }

  public BaseApiResource3 lastEventState(LastEventStateEnum lastEventState) {
    this.lastEventState = lastEventState;
    return this;
  }

   /**
   * Get lastEventState
   * @return lastEventState
  **/
  @Schema(description = "")
  public LastEventStateEnum getLastEventState() {
    return lastEventState;
  }

  public void setLastEventState(LastEventStateEnum lastEventState) {
    this.lastEventState = lastEventState;
  }

  public BaseApiResource3 alertsEnabled(Boolean alertsEnabled) {
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

  public BaseApiResource3 operatorAlertsEnabled(Boolean operatorAlertsEnabled) {
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

  public BaseApiResource3 serviceState(ServiceStateEnum serviceState) {
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

  public BaseApiResource3 modified(OffsetDateTime modified) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseApiResource3 baseApiResource3 = (BaseApiResource3) o;
    return Objects.equals(this._href, baseApiResource3._href) &&
        Objects.equals(this.created, baseApiResource3.created) &&
        Objects.equals(this.edge, baseApiResource3.edge) &&
        Objects.equals(this.logicalId, baseApiResource3.logicalId) &&
        Objects.equals(this.internalId, baseApiResource3.internalId) &&
        Objects.equals(this._interface, baseApiResource3._interface) &&
        Objects.equals(this.macAddress, baseApiResource3.macAddress) &&
        Objects.equals(this.ipAddress, baseApiResource3.ipAddress) &&
        Objects.equals(this.ipV6Address, baseApiResource3.ipV6Address) &&
        Objects.equals(this.netmask, baseApiResource3.netmask) &&
        Objects.equals(this.networkSide, baseApiResource3.networkSide) &&
        Objects.equals(this.networkType, baseApiResource3.networkType) &&
        Objects.equals(this.displayName, baseApiResource3.displayName) &&
        Objects.equals(this.userOverride, baseApiResource3.userOverride) &&
        Objects.equals(this.isp, baseApiResource3.isp) &&
        Objects.equals(this.org, baseApiResource3.org) &&
        Objects.equals(this.lat, baseApiResource3.lat) &&
        Objects.equals(this.lon, baseApiResource3.lon) &&
        Objects.equals(this.lastActive, baseApiResource3.lastActive) &&
        Objects.equals(this.state, baseApiResource3.state) &&
        Objects.equals(this.backupState, baseApiResource3.backupState) &&
        Objects.equals(this.linkMode, baseApiResource3.linkMode) &&
        Objects.equals(this.lastEvent, baseApiResource3.lastEvent) &&
        Objects.equals(this.lastEventState, baseApiResource3.lastEventState) &&
        Objects.equals(this.alertsEnabled, baseApiResource3.alertsEnabled) &&
        Objects.equals(this.operatorAlertsEnabled, baseApiResource3.operatorAlertsEnabled) &&
        Objects.equals(this.serviceState, baseApiResource3.serviceState) &&
        Objects.equals(this.modified, baseApiResource3.modified);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_href, created, edge, logicalId, internalId, _interface, macAddress, ipAddress, ipV6Address, netmask, networkSide, networkType, displayName, userOverride, isp, org, lat, lon, lastActive, state, backupState, linkMode, lastEvent, lastEventState, alertsEnabled, operatorAlertsEnabled, serviceState, modified);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BaseApiResource3 {\n");
    
    sb.append("    _href: ").append(toIndentedString(_href)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    edge: ").append(toIndentedString(edge)).append("\n");
    sb.append("    logicalId: ").append(toIndentedString(logicalId)).append("\n");
    sb.append("    internalId: ").append(toIndentedString(internalId)).append("\n");
    sb.append("    _interface: ").append(toIndentedString(_interface)).append("\n");
    sb.append("    macAddress: ").append(toIndentedString(macAddress)).append("\n");
    sb.append("    ipAddress: ").append(toIndentedString(ipAddress)).append("\n");
    sb.append("    ipV6Address: ").append(toIndentedString(ipV6Address)).append("\n");
    sb.append("    netmask: ").append(toIndentedString(netmask)).append("\n");
    sb.append("    networkSide: ").append(toIndentedString(networkSide)).append("\n");
    sb.append("    networkType: ").append(toIndentedString(networkType)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    userOverride: ").append(toIndentedString(userOverride)).append("\n");
    sb.append("    isp: ").append(toIndentedString(isp)).append("\n");
    sb.append("    org: ").append(toIndentedString(org)).append("\n");
    sb.append("    lat: ").append(toIndentedString(lat)).append("\n");
    sb.append("    lon: ").append(toIndentedString(lon)).append("\n");
    sb.append("    lastActive: ").append(toIndentedString(lastActive)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    backupState: ").append(toIndentedString(backupState)).append("\n");
    sb.append("    linkMode: ").append(toIndentedString(linkMode)).append("\n");
    sb.append("    lastEvent: ").append(toIndentedString(lastEvent)).append("\n");
    sb.append("    lastEventState: ").append(toIndentedString(lastEventState)).append("\n");
    sb.append("    alertsEnabled: ").append(toIndentedString(alertsEnabled)).append("\n");
    sb.append("    operatorAlertsEnabled: ").append(toIndentedString(operatorAlertsEnabled)).append("\n");
    sb.append("    serviceState: ").append(toIndentedString(serviceState)).append("\n");
    sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
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
