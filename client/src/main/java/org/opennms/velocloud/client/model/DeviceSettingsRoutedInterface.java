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
import org.opennms.velocloud.client.model.BaseApiResource10;
import org.opennms.velocloud.client.model.DeviceSettingsDHCP;
import org.opennms.velocloud.client.model.DeviceSettingsLANL2;
import org.opennms.velocloud.client.model.DeviceSettingsRoutedInterfaceAddressing;
import org.opennms.velocloud.client.model.DeviceSettingsRoutedInterfaceOSPF;
import org.opennms.velocloud.client.model.DeviceSettingsRoutedInterfacev6Detail;
import org.opennms.velocloud.client.model.DeviceSettingsSubInterface;
import org.opennms.velocloud.client.model.EnterpriseDeviceSettingsCellular;
import org.opennms.velocloud.client.model.EnterpriseDeviceSettingsDslSettings;
import org.opennms.velocloud.client.model.EnterpriseDeviceSettingsMulticast;
import org.opennms.velocloud.client.model.EnterpriseDeviceSettingsRadiusAuthentication;
/**
 * DeviceSettingsRoutedInterface
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")
public class DeviceSettingsRoutedInterface {
  @JsonProperty("override")
  private Boolean override = null;

  @JsonProperty("disabled")
  private Boolean disabled = null;

  @JsonProperty("disableV4")
  private Boolean disableV4 = null;

  @JsonProperty("disableV6")
  private Boolean disableV6 = null;

  /**
   * Gets or Sets overlayPreference
   */
  public enum OverlayPreferenceEnum {
    IPV4("IPv4"),
    IPV6("IPv6"),
    IPV4V6("IPv4v6");

    private String value;

    OverlayPreferenceEnum(String value) {
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
    public static OverlayPreferenceEnum fromValue(String text) {
      for (OverlayPreferenceEnum b : OverlayPreferenceEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("overlayPreference")
  private OverlayPreferenceEnum overlayPreference = null;

  @JsonProperty("addressing")
  private DeviceSettingsRoutedInterfaceAddressing addressing = null;

  @JsonProperty("v6Detail")
  private DeviceSettingsRoutedInterfacev6Detail v6Detail = null;

  @JsonProperty("advertise")
  private Boolean advertise = null;

  @JsonProperty("cellular")
  private EnterpriseDeviceSettingsCellular cellular = null;

  @JsonProperty("dhcpServer")
  private DeviceSettingsDHCP dhcpServer = null;

  @JsonProperty("dslSettings")
  private EnterpriseDeviceSettingsDslSettings dslSettings = null;

  @JsonProperty("encryptOverlay")
  private Boolean encryptOverlay = null;

  @JsonProperty("l2")
  private DeviceSettingsLANL2 l2 = null;

  @JsonProperty("multicast")
  private EnterpriseDeviceSettingsMulticast multicast = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("natDirect")
  private Boolean natDirect = null;

  @JsonProperty("ospf")
  private DeviceSettingsRoutedInterfaceOSPF ospf = null;

  @JsonProperty("pingResponse")
  private Boolean pingResponse = true;

  @JsonProperty("dnsProxy")
  private Boolean dnsProxy = true;

  @JsonProperty("evdslModemAttached")
  private Boolean evdslModemAttached = false;

  @JsonProperty("radiusAuthentication")
  private EnterpriseDeviceSettingsRadiusAuthentication radiusAuthentication = null;

  @JsonProperty("segmentId")
  private Integer segmentId = null;

  @JsonProperty("segment")
  private BaseApiResource10 segment = null;

  /**
   * Gets or Sets sfpType
   */
  public enum SfpTypeEnum {
    STANDARD("standard"),
    DSL("dsl"),
    GPON("gpon");

    private String value;

    SfpTypeEnum(String value) {
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
    public static SfpTypeEnum fromValue(String text) {
      for (SfpTypeEnum b : SfpTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("sfpType")
  private SfpTypeEnum sfpType = null;

  @JsonProperty("subinterfaces")
  private List<DeviceSettingsSubInterface> subinterfaces = null;

  /**
   * Gets or Sets rpf
   */
  public enum RpfEnum {
    SPECIFIC("SPECIFIC"),
    LOOSE("LOOSE"),
    DISABLED("DISABLED");

    private String value;

    RpfEnum(String value) {
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
    public static RpfEnum fromValue(String text) {
      for (RpfEnum b : RpfEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("rpf")
  private RpfEnum rpf = null;

  @JsonProperty("trusted")
  private Boolean trusted = null;

  @JsonProperty("underlayAccounting")
  private Boolean underlayAccounting = true;

  @JsonProperty("vlanId")
  private Integer vlanId = null;

  /**
   * Gets or Sets wanOverlay
   */
  public enum WanOverlayEnum {
    DISABLED("DISABLED"),
    AUTO_DISCOVERED("AUTO_DISCOVERED"),
    USER_DEFINED("USER_DEFINED");

    private String value;

    WanOverlayEnum(String value) {
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
    public static WanOverlayEnum fromValue(String text) {
      for (WanOverlayEnum b : WanOverlayEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("wanOverlay")
  private WanOverlayEnum wanOverlay = null;

  public DeviceSettingsRoutedInterface override(Boolean override) {
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

  public DeviceSettingsRoutedInterface disabled(Boolean disabled) {
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

  public DeviceSettingsRoutedInterface disableV4(Boolean disableV4) {
    this.disableV4 = disableV4;
    return this;
  }

   /**
   * Get disableV4
   * @return disableV4
  **/
  @Schema(description = "")
  public Boolean isDisableV4() {
    return disableV4;
  }

  public void setDisableV4(Boolean disableV4) {
    this.disableV4 = disableV4;
  }

  public DeviceSettingsRoutedInterface disableV6(Boolean disableV6) {
    this.disableV6 = disableV6;
    return this;
  }

   /**
   * Get disableV6
   * @return disableV6
  **/
  @Schema(description = "")
  public Boolean isDisableV6() {
    return disableV6;
  }

  public void setDisableV6(Boolean disableV6) {
    this.disableV6 = disableV6;
  }

  public DeviceSettingsRoutedInterface overlayPreference(OverlayPreferenceEnum overlayPreference) {
    this.overlayPreference = overlayPreference;
    return this;
  }

   /**
   * Get overlayPreference
   * @return overlayPreference
  **/
  @Schema(description = "")
  public OverlayPreferenceEnum getOverlayPreference() {
    return overlayPreference;
  }

  public void setOverlayPreference(OverlayPreferenceEnum overlayPreference) {
    this.overlayPreference = overlayPreference;
  }

  public DeviceSettingsRoutedInterface addressing(DeviceSettingsRoutedInterfaceAddressing addressing) {
    this.addressing = addressing;
    return this;
  }

   /**
   * Get addressing
   * @return addressing
  **/
  @Schema(description = "")
  public DeviceSettingsRoutedInterfaceAddressing getAddressing() {
    return addressing;
  }

  public void setAddressing(DeviceSettingsRoutedInterfaceAddressing addressing) {
    this.addressing = addressing;
  }

  public DeviceSettingsRoutedInterface v6Detail(DeviceSettingsRoutedInterfacev6Detail v6Detail) {
    this.v6Detail = v6Detail;
    return this;
  }

   /**
   * Get v6Detail
   * @return v6Detail
  **/
  @Schema(description = "")
  public DeviceSettingsRoutedInterfacev6Detail getV6Detail() {
    return v6Detail;
  }

  public void setV6Detail(DeviceSettingsRoutedInterfacev6Detail v6Detail) {
    this.v6Detail = v6Detail;
  }

  public DeviceSettingsRoutedInterface advertise(Boolean advertise) {
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

  public DeviceSettingsRoutedInterface cellular(EnterpriseDeviceSettingsCellular cellular) {
    this.cellular = cellular;
    return this;
  }

   /**
   * Get cellular
   * @return cellular
  **/
  @Schema(description = "")
  public EnterpriseDeviceSettingsCellular getCellular() {
    return cellular;
  }

  public void setCellular(EnterpriseDeviceSettingsCellular cellular) {
    this.cellular = cellular;
  }

  public DeviceSettingsRoutedInterface dhcpServer(DeviceSettingsDHCP dhcpServer) {
    this.dhcpServer = dhcpServer;
    return this;
  }

   /**
   * Get dhcpServer
   * @return dhcpServer
  **/
  @Schema(description = "")
  public DeviceSettingsDHCP getDhcpServer() {
    return dhcpServer;
  }

  public void setDhcpServer(DeviceSettingsDHCP dhcpServer) {
    this.dhcpServer = dhcpServer;
  }

  public DeviceSettingsRoutedInterface dslSettings(EnterpriseDeviceSettingsDslSettings dslSettings) {
    this.dslSettings = dslSettings;
    return this;
  }

   /**
   * Get dslSettings
   * @return dslSettings
  **/
  @Schema(description = "")
  public EnterpriseDeviceSettingsDslSettings getDslSettings() {
    return dslSettings;
  }

  public void setDslSettings(EnterpriseDeviceSettingsDslSettings dslSettings) {
    this.dslSettings = dslSettings;
  }

  public DeviceSettingsRoutedInterface encryptOverlay(Boolean encryptOverlay) {
    this.encryptOverlay = encryptOverlay;
    return this;
  }

   /**
   * Get encryptOverlay
   * @return encryptOverlay
  **/
  @Schema(description = "")
  public Boolean isEncryptOverlay() {
    return encryptOverlay;
  }

  public void setEncryptOverlay(Boolean encryptOverlay) {
    this.encryptOverlay = encryptOverlay;
  }

  public DeviceSettingsRoutedInterface l2(DeviceSettingsLANL2 l2) {
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

  public DeviceSettingsRoutedInterface multicast(EnterpriseDeviceSettingsMulticast multicast) {
    this.multicast = multicast;
    return this;
  }

   /**
   * Get multicast
   * @return multicast
  **/
  @Schema(description = "")
  public EnterpriseDeviceSettingsMulticast getMulticast() {
    return multicast;
  }

  public void setMulticast(EnterpriseDeviceSettingsMulticast multicast) {
    this.multicast = multicast;
  }

  public DeviceSettingsRoutedInterface name(String name) {
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

  public DeviceSettingsRoutedInterface natDirect(Boolean natDirect) {
    this.natDirect = natDirect;
    return this;
  }

   /**
   * Get natDirect
   * @return natDirect
  **/
  @Schema(description = "")
  public Boolean isNatDirect() {
    return natDirect;
  }

  public void setNatDirect(Boolean natDirect) {
    this.natDirect = natDirect;
  }

  public DeviceSettingsRoutedInterface ospf(DeviceSettingsRoutedInterfaceOSPF ospf) {
    this.ospf = ospf;
    return this;
  }

   /**
   * Get ospf
   * @return ospf
  **/
  @Schema(description = "")
  public DeviceSettingsRoutedInterfaceOSPF getOspf() {
    return ospf;
  }

  public void setOspf(DeviceSettingsRoutedInterfaceOSPF ospf) {
    this.ospf = ospf;
  }

  public DeviceSettingsRoutedInterface pingResponse(Boolean pingResponse) {
    this.pingResponse = pingResponse;
    return this;
  }

   /**
   * Get pingResponse
   * @return pingResponse
  **/
  @Schema(description = "")
  public Boolean isPingResponse() {
    return pingResponse;
  }

  public void setPingResponse(Boolean pingResponse) {
    this.pingResponse = pingResponse;
  }

  public DeviceSettingsRoutedInterface dnsProxy(Boolean dnsProxy) {
    this.dnsProxy = dnsProxy;
    return this;
  }

   /**
   * Get dnsProxy
   * @return dnsProxy
  **/
  @Schema(description = "")
  public Boolean isDnsProxy() {
    return dnsProxy;
  }

  public void setDnsProxy(Boolean dnsProxy) {
    this.dnsProxy = dnsProxy;
  }

  public DeviceSettingsRoutedInterface evdslModemAttached(Boolean evdslModemAttached) {
    this.evdslModemAttached = evdslModemAttached;
    return this;
  }

   /**
   * Get evdslModemAttached
   * @return evdslModemAttached
  **/
  @Schema(description = "")
  public Boolean isEvdslModemAttached() {
    return evdslModemAttached;
  }

  public void setEvdslModemAttached(Boolean evdslModemAttached) {
    this.evdslModemAttached = evdslModemAttached;
  }

  public DeviceSettingsRoutedInterface radiusAuthentication(EnterpriseDeviceSettingsRadiusAuthentication radiusAuthentication) {
    this.radiusAuthentication = radiusAuthentication;
    return this;
  }

   /**
   * Get radiusAuthentication
   * @return radiusAuthentication
  **/
  @Schema(description = "")
  public EnterpriseDeviceSettingsRadiusAuthentication getRadiusAuthentication() {
    return radiusAuthentication;
  }

  public void setRadiusAuthentication(EnterpriseDeviceSettingsRadiusAuthentication radiusAuthentication) {
    this.radiusAuthentication = radiusAuthentication;
  }

  public DeviceSettingsRoutedInterface segmentId(Integer segmentId) {
    this.segmentId = segmentId;
    return this;
  }

   /**
   * Get segmentId
   * @return segmentId
  **/
  @Schema(description = "")
  public Integer getSegmentId() {
    return segmentId;
  }

  public void setSegmentId(Integer segmentId) {
    this.segmentId = segmentId;
  }

  public DeviceSettingsRoutedInterface segment(BaseApiResource10 segment) {
    this.segment = segment;
    return this;
  }

   /**
   * Get segment
   * @return segment
  **/
  @Schema(description = "")
  public BaseApiResource10 getSegment() {
    return segment;
  }

  public void setSegment(BaseApiResource10 segment) {
    this.segment = segment;
  }

  public DeviceSettingsRoutedInterface sfpType(SfpTypeEnum sfpType) {
    this.sfpType = sfpType;
    return this;
  }

   /**
   * Get sfpType
   * @return sfpType
  **/
  @Schema(description = "")
  public SfpTypeEnum getSfpType() {
    return sfpType;
  }

  public void setSfpType(SfpTypeEnum sfpType) {
    this.sfpType = sfpType;
  }

  public DeviceSettingsRoutedInterface subinterfaces(List<DeviceSettingsSubInterface> subinterfaces) {
    this.subinterfaces = subinterfaces;
    return this;
  }

  public DeviceSettingsRoutedInterface addSubinterfacesItem(DeviceSettingsSubInterface subinterfacesItem) {
    if (this.subinterfaces == null) {
      this.subinterfaces = new ArrayList<>();
    }
    this.subinterfaces.add(subinterfacesItem);
    return this;
  }

   /**
   * Get subinterfaces
   * @return subinterfaces
  **/
  @Schema(description = "")
  public List<DeviceSettingsSubInterface> getSubinterfaces() {
    return subinterfaces;
  }

  public void setSubinterfaces(List<DeviceSettingsSubInterface> subinterfaces) {
    this.subinterfaces = subinterfaces;
  }

  public DeviceSettingsRoutedInterface rpf(RpfEnum rpf) {
    this.rpf = rpf;
    return this;
  }

   /**
   * Get rpf
   * @return rpf
  **/
  @Schema(description = "")
  public RpfEnum getRpf() {
    return rpf;
  }

  public void setRpf(RpfEnum rpf) {
    this.rpf = rpf;
  }

  public DeviceSettingsRoutedInterface trusted(Boolean trusted) {
    this.trusted = trusted;
    return this;
  }

   /**
   * Get trusted
   * @return trusted
  **/
  @Schema(description = "")
  public Boolean isTrusted() {
    return trusted;
  }

  public void setTrusted(Boolean trusted) {
    this.trusted = trusted;
  }

  public DeviceSettingsRoutedInterface underlayAccounting(Boolean underlayAccounting) {
    this.underlayAccounting = underlayAccounting;
    return this;
  }

   /**
   * Get underlayAccounting
   * @return underlayAccounting
  **/
  @Schema(description = "")
  public Boolean isUnderlayAccounting() {
    return underlayAccounting;
  }

  public void setUnderlayAccounting(Boolean underlayAccounting) {
    this.underlayAccounting = underlayAccounting;
  }

  public DeviceSettingsRoutedInterface vlanId(Integer vlanId) {
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

  public DeviceSettingsRoutedInterface wanOverlay(WanOverlayEnum wanOverlay) {
    this.wanOverlay = wanOverlay;
    return this;
  }

   /**
   * Get wanOverlay
   * @return wanOverlay
  **/
  @Schema(description = "")
  public WanOverlayEnum getWanOverlay() {
    return wanOverlay;
  }

  public void setWanOverlay(WanOverlayEnum wanOverlay) {
    this.wanOverlay = wanOverlay;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceSettingsRoutedInterface deviceSettingsRoutedInterface = (DeviceSettingsRoutedInterface) o;
    return Objects.equals(this.override, deviceSettingsRoutedInterface.override) &&
        Objects.equals(this.disabled, deviceSettingsRoutedInterface.disabled) &&
        Objects.equals(this.disableV4, deviceSettingsRoutedInterface.disableV4) &&
        Objects.equals(this.disableV6, deviceSettingsRoutedInterface.disableV6) &&
        Objects.equals(this.overlayPreference, deviceSettingsRoutedInterface.overlayPreference) &&
        Objects.equals(this.addressing, deviceSettingsRoutedInterface.addressing) &&
        Objects.equals(this.v6Detail, deviceSettingsRoutedInterface.v6Detail) &&
        Objects.equals(this.advertise, deviceSettingsRoutedInterface.advertise) &&
        Objects.equals(this.cellular, deviceSettingsRoutedInterface.cellular) &&
        Objects.equals(this.dhcpServer, deviceSettingsRoutedInterface.dhcpServer) &&
        Objects.equals(this.dslSettings, deviceSettingsRoutedInterface.dslSettings) &&
        Objects.equals(this.encryptOverlay, deviceSettingsRoutedInterface.encryptOverlay) &&
        Objects.equals(this.l2, deviceSettingsRoutedInterface.l2) &&
        Objects.equals(this.multicast, deviceSettingsRoutedInterface.multicast) &&
        Objects.equals(this.name, deviceSettingsRoutedInterface.name) &&
        Objects.equals(this.natDirect, deviceSettingsRoutedInterface.natDirect) &&
        Objects.equals(this.ospf, deviceSettingsRoutedInterface.ospf) &&
        Objects.equals(this.pingResponse, deviceSettingsRoutedInterface.pingResponse) &&
        Objects.equals(this.dnsProxy, deviceSettingsRoutedInterface.dnsProxy) &&
        Objects.equals(this.evdslModemAttached, deviceSettingsRoutedInterface.evdslModemAttached) &&
        Objects.equals(this.radiusAuthentication, deviceSettingsRoutedInterface.radiusAuthentication) &&
        Objects.equals(this.segmentId, deviceSettingsRoutedInterface.segmentId) &&
        Objects.equals(this.segment, deviceSettingsRoutedInterface.segment) &&
        Objects.equals(this.sfpType, deviceSettingsRoutedInterface.sfpType) &&
        Objects.equals(this.subinterfaces, deviceSettingsRoutedInterface.subinterfaces) &&
        Objects.equals(this.rpf, deviceSettingsRoutedInterface.rpf) &&
        Objects.equals(this.trusted, deviceSettingsRoutedInterface.trusted) &&
        Objects.equals(this.underlayAccounting, deviceSettingsRoutedInterface.underlayAccounting) &&
        Objects.equals(this.vlanId, deviceSettingsRoutedInterface.vlanId) &&
        Objects.equals(this.wanOverlay, deviceSettingsRoutedInterface.wanOverlay);
  }

  @Override
  public int hashCode() {
    return Objects.hash(override, disabled, disableV4, disableV6, overlayPreference, addressing, v6Detail, advertise, cellular, dhcpServer, dslSettings, encryptOverlay, l2, multicast, name, natDirect, ospf, pingResponse, dnsProxy, evdslModemAttached, radiusAuthentication, segmentId, segment, sfpType, subinterfaces, rpf, trusted, underlayAccounting, vlanId, wanOverlay);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceSettingsRoutedInterface {\n");
    
    sb.append("    override: ").append(toIndentedString(override)).append("\n");
    sb.append("    disabled: ").append(toIndentedString(disabled)).append("\n");
    sb.append("    disableV4: ").append(toIndentedString(disableV4)).append("\n");
    sb.append("    disableV6: ").append(toIndentedString(disableV6)).append("\n");
    sb.append("    overlayPreference: ").append(toIndentedString(overlayPreference)).append("\n");
    sb.append("    addressing: ").append(toIndentedString(addressing)).append("\n");
    sb.append("    v6Detail: ").append(toIndentedString(v6Detail)).append("\n");
    sb.append("    advertise: ").append(toIndentedString(advertise)).append("\n");
    sb.append("    cellular: ").append(toIndentedString(cellular)).append("\n");
    sb.append("    dhcpServer: ").append(toIndentedString(dhcpServer)).append("\n");
    sb.append("    dslSettings: ").append(toIndentedString(dslSettings)).append("\n");
    sb.append("    encryptOverlay: ").append(toIndentedString(encryptOverlay)).append("\n");
    sb.append("    l2: ").append(toIndentedString(l2)).append("\n");
    sb.append("    multicast: ").append(toIndentedString(multicast)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    natDirect: ").append(toIndentedString(natDirect)).append("\n");
    sb.append("    ospf: ").append(toIndentedString(ospf)).append("\n");
    sb.append("    pingResponse: ").append(toIndentedString(pingResponse)).append("\n");
    sb.append("    dnsProxy: ").append(toIndentedString(dnsProxy)).append("\n");
    sb.append("    evdslModemAttached: ").append(toIndentedString(evdslModemAttached)).append("\n");
    sb.append("    radiusAuthentication: ").append(toIndentedString(radiusAuthentication)).append("\n");
    sb.append("    segmentId: ").append(toIndentedString(segmentId)).append("\n");
    sb.append("    segment: ").append(toIndentedString(segment)).append("\n");
    sb.append("    sfpType: ").append(toIndentedString(sfpType)).append("\n");
    sb.append("    subinterfaces: ").append(toIndentedString(subinterfaces)).append("\n");
    sb.append("    rpf: ").append(toIndentedString(rpf)).append("\n");
    sb.append("    trusted: ").append(toIndentedString(trusted)).append("\n");
    sb.append("    underlayAccounting: ").append(toIndentedString(underlayAccounting)).append("\n");
    sb.append("    vlanId: ").append(toIndentedString(vlanId)).append("\n");
    sb.append("    wanOverlay: ").append(toIndentedString(wanOverlay)).append("\n");
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
