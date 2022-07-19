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
import org.opennms.velocloud.model.DeviceSettingsBFD;
import org.opennms.velocloud.model.DeviceSettingsGlobalIPv6Settings;
import org.opennms.velocloud.model.DeviceSettingsHA;
import org.opennms.velocloud.model.DeviceSettingsL2Settings;
import org.opennms.velocloud.model.DeviceSettingsLAN;
import org.opennms.velocloud.model.DeviceSettingsLoopbackInterfaces;
import org.opennms.velocloud.model.DeviceSettingsMultiSourceQOS;
import org.opennms.velocloud.model.DeviceSettingsNTP;
import org.opennms.velocloud.model.DeviceSettingsNTPAsServer;
import org.opennms.velocloud.model.DeviceSettingsRadioSettings;
import org.opennms.velocloud.model.DeviceSettingsRoutedInterface;
import org.opennms.velocloud.model.DeviceSettingsSNMPOuter;
import org.opennms.velocloud.model.DeviceSettingsSegment;
import org.opennms.velocloud.model.DeviceSettingsSoftwareUpdate;
import org.opennms.velocloud.model.DeviceSettingsTACACS;
import org.opennms.velocloud.model.DeviceSettingsVNFS;
import org.opennms.velocloud.model.DeviceSettingsZscaler;
/**
 * EdgeDeviceSettings
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class EdgeDeviceSettings {
  @SerializedName("bfd")
  private DeviceSettingsBFD bfd = null;

  @SerializedName("ha")
  private DeviceSettingsHA ha = null;

  @SerializedName("lan")
  private DeviceSettingsLAN lan = null;

  @SerializedName("loopbackInterfaces")
  private DeviceSettingsLoopbackInterfaces loopbackInterfaces = null;

  @SerializedName("models")
  private Object models = null;

  @SerializedName("multiSourceQos")
  private DeviceSettingsMultiSourceQOS multiSourceQos = null;

  @SerializedName("ntp")
  private DeviceSettingsNTP ntp = null;

  @SerializedName("ntpServer")
  private DeviceSettingsNTPAsServer ntpServer = null;

  @SerializedName("radioSettings")
  private DeviceSettingsRadioSettings radioSettings = null;

  @SerializedName("l2Settings")
  private DeviceSettingsL2Settings l2Settings = null;

  @SerializedName("globalIPv6Settings")
  private DeviceSettingsGlobalIPv6Settings globalIPv6Settings = null;

  @SerializedName("routedInterfaces")
  private List<DeviceSettingsRoutedInterface> routedInterfaces = null;

  @SerializedName("segments")
  private List<DeviceSettingsSegment> segments = null;

  @SerializedName("snmp")
  private DeviceSettingsSNMPOuter snmp = null;

  @SerializedName("softwareUpdate")
  private DeviceSettingsSoftwareUpdate softwareUpdate = null;

  @SerializedName("tacacs")
  private DeviceSettingsTACACS tacacs = null;

  @SerializedName("vnfs")
  private DeviceSettingsVNFS vnfs = null;

  @SerializedName("zscaler")
  private DeviceSettingsZscaler zscaler = null;

  @SerializedName("_href")
  private String _href = null;

  public EdgeDeviceSettings bfd(DeviceSettingsBFD bfd) {
    this.bfd = bfd;
    return this;
  }

   /**
   * Get bfd
   * @return bfd
  **/
  @Schema(description = "")
  public DeviceSettingsBFD getBfd() {
    return bfd;
  }

  public void setBfd(DeviceSettingsBFD bfd) {
    this.bfd = bfd;
  }

  public EdgeDeviceSettings ha(DeviceSettingsHA ha) {
    this.ha = ha;
    return this;
  }

   /**
   * Get ha
   * @return ha
  **/
  @Schema(description = "")
  public DeviceSettingsHA getHa() {
    return ha;
  }

  public void setHa(DeviceSettingsHA ha) {
    this.ha = ha;
  }

  public EdgeDeviceSettings lan(DeviceSettingsLAN lan) {
    this.lan = lan;
    return this;
  }

   /**
   * Get lan
   * @return lan
  **/
  @Schema(description = "")
  public DeviceSettingsLAN getLan() {
    return lan;
  }

  public void setLan(DeviceSettingsLAN lan) {
    this.lan = lan;
  }

  public EdgeDeviceSettings loopbackInterfaces(DeviceSettingsLoopbackInterfaces loopbackInterfaces) {
    this.loopbackInterfaces = loopbackInterfaces;
    return this;
  }

   /**
   * Get loopbackInterfaces
   * @return loopbackInterfaces
  **/
  @Schema(description = "")
  public DeviceSettingsLoopbackInterfaces getLoopbackInterfaces() {
    return loopbackInterfaces;
  }

  public void setLoopbackInterfaces(DeviceSettingsLoopbackInterfaces loopbackInterfaces) {
    this.loopbackInterfaces = loopbackInterfaces;
  }

  public EdgeDeviceSettings models(Object models) {
    this.models = models;
    return this;
  }

   /**
   * Get models
   * @return models
  **/
  @Schema(description = "")
  public Object getModels() {
    return models;
  }

  public void setModels(Object models) {
    this.models = models;
  }

  public EdgeDeviceSettings multiSourceQos(DeviceSettingsMultiSourceQOS multiSourceQos) {
    this.multiSourceQos = multiSourceQos;
    return this;
  }

   /**
   * Get multiSourceQos
   * @return multiSourceQos
  **/
  @Schema(description = "")
  public DeviceSettingsMultiSourceQOS getMultiSourceQos() {
    return multiSourceQos;
  }

  public void setMultiSourceQos(DeviceSettingsMultiSourceQOS multiSourceQos) {
    this.multiSourceQos = multiSourceQos;
  }

  public EdgeDeviceSettings ntp(DeviceSettingsNTP ntp) {
    this.ntp = ntp;
    return this;
  }

   /**
   * Get ntp
   * @return ntp
  **/
  @Schema(description = "")
  public DeviceSettingsNTP getNtp() {
    return ntp;
  }

  public void setNtp(DeviceSettingsNTP ntp) {
    this.ntp = ntp;
  }

  public EdgeDeviceSettings ntpServer(DeviceSettingsNTPAsServer ntpServer) {
    this.ntpServer = ntpServer;
    return this;
  }

   /**
   * Get ntpServer
   * @return ntpServer
  **/
  @Schema(description = "")
  public DeviceSettingsNTPAsServer getNtpServer() {
    return ntpServer;
  }

  public void setNtpServer(DeviceSettingsNTPAsServer ntpServer) {
    this.ntpServer = ntpServer;
  }

  public EdgeDeviceSettings radioSettings(DeviceSettingsRadioSettings radioSettings) {
    this.radioSettings = radioSettings;
    return this;
  }

   /**
   * Get radioSettings
   * @return radioSettings
  **/
  @Schema(description = "")
  public DeviceSettingsRadioSettings getRadioSettings() {
    return radioSettings;
  }

  public void setRadioSettings(DeviceSettingsRadioSettings radioSettings) {
    this.radioSettings = radioSettings;
  }

  public EdgeDeviceSettings l2Settings(DeviceSettingsL2Settings l2Settings) {
    this.l2Settings = l2Settings;
    return this;
  }

   /**
   * Get l2Settings
   * @return l2Settings
  **/
  @Schema(description = "")
  public DeviceSettingsL2Settings getL2Settings() {
    return l2Settings;
  }

  public void setL2Settings(DeviceSettingsL2Settings l2Settings) {
    this.l2Settings = l2Settings;
  }

  public EdgeDeviceSettings globalIPv6Settings(DeviceSettingsGlobalIPv6Settings globalIPv6Settings) {
    this.globalIPv6Settings = globalIPv6Settings;
    return this;
  }

   /**
   * Get globalIPv6Settings
   * @return globalIPv6Settings
  **/
  @Schema(description = "")
  public DeviceSettingsGlobalIPv6Settings getGlobalIPv6Settings() {
    return globalIPv6Settings;
  }

  public void setGlobalIPv6Settings(DeviceSettingsGlobalIPv6Settings globalIPv6Settings) {
    this.globalIPv6Settings = globalIPv6Settings;
  }

  public EdgeDeviceSettings routedInterfaces(List<DeviceSettingsRoutedInterface> routedInterfaces) {
    this.routedInterfaces = routedInterfaces;
    return this;
  }

  public EdgeDeviceSettings addRoutedInterfacesItem(DeviceSettingsRoutedInterface routedInterfacesItem) {
    if (this.routedInterfaces == null) {
      this.routedInterfaces = new ArrayList<>();
    }
    this.routedInterfaces.add(routedInterfacesItem);
    return this;
  }

   /**
   * Get routedInterfaces
   * @return routedInterfaces
  **/
  @Schema(description = "")
  public List<DeviceSettingsRoutedInterface> getRoutedInterfaces() {
    return routedInterfaces;
  }

  public void setRoutedInterfaces(List<DeviceSettingsRoutedInterface> routedInterfaces) {
    this.routedInterfaces = routedInterfaces;
  }

  public EdgeDeviceSettings segments(List<DeviceSettingsSegment> segments) {
    this.segments = segments;
    return this;
  }

  public EdgeDeviceSettings addSegmentsItem(DeviceSettingsSegment segmentsItem) {
    if (this.segments == null) {
      this.segments = new ArrayList<>();
    }
    this.segments.add(segmentsItem);
    return this;
  }

   /**
   * Get segments
   * @return segments
  **/
  @Schema(description = "")
  public List<DeviceSettingsSegment> getSegments() {
    return segments;
  }

  public void setSegments(List<DeviceSettingsSegment> segments) {
    this.segments = segments;
  }

  public EdgeDeviceSettings snmp(DeviceSettingsSNMPOuter snmp) {
    this.snmp = snmp;
    return this;
  }

   /**
   * Get snmp
   * @return snmp
  **/
  @Schema(description = "")
  public DeviceSettingsSNMPOuter getSnmp() {
    return snmp;
  }

  public void setSnmp(DeviceSettingsSNMPOuter snmp) {
    this.snmp = snmp;
  }

  public EdgeDeviceSettings softwareUpdate(DeviceSettingsSoftwareUpdate softwareUpdate) {
    this.softwareUpdate = softwareUpdate;
    return this;
  }

   /**
   * Get softwareUpdate
   * @return softwareUpdate
  **/
  @Schema(description = "")
  public DeviceSettingsSoftwareUpdate getSoftwareUpdate() {
    return softwareUpdate;
  }

  public void setSoftwareUpdate(DeviceSettingsSoftwareUpdate softwareUpdate) {
    this.softwareUpdate = softwareUpdate;
  }

  public EdgeDeviceSettings tacacs(DeviceSettingsTACACS tacacs) {
    this.tacacs = tacacs;
    return this;
  }

   /**
   * Get tacacs
   * @return tacacs
  **/
  @Schema(description = "")
  public DeviceSettingsTACACS getTacacs() {
    return tacacs;
  }

  public void setTacacs(DeviceSettingsTACACS tacacs) {
    this.tacacs = tacacs;
  }

  public EdgeDeviceSettings vnfs(DeviceSettingsVNFS vnfs) {
    this.vnfs = vnfs;
    return this;
  }

   /**
   * Get vnfs
   * @return vnfs
  **/
  @Schema(description = "")
  public DeviceSettingsVNFS getVnfs() {
    return vnfs;
  }

  public void setVnfs(DeviceSettingsVNFS vnfs) {
    this.vnfs = vnfs;
  }

  public EdgeDeviceSettings zscaler(DeviceSettingsZscaler zscaler) {
    this.zscaler = zscaler;
    return this;
  }

   /**
   * Get zscaler
   * @return zscaler
  **/
  @Schema(description = "")
  public DeviceSettingsZscaler getZscaler() {
    return zscaler;
  }

  public void setZscaler(DeviceSettingsZscaler zscaler) {
    this.zscaler = zscaler;
  }

  public EdgeDeviceSettings _href(String _href) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EdgeDeviceSettings edgeDeviceSettings = (EdgeDeviceSettings) o;
    return Objects.equals(this.bfd, edgeDeviceSettings.bfd) &&
        Objects.equals(this.ha, edgeDeviceSettings.ha) &&
        Objects.equals(this.lan, edgeDeviceSettings.lan) &&
        Objects.equals(this.loopbackInterfaces, edgeDeviceSettings.loopbackInterfaces) &&
        Objects.equals(this.models, edgeDeviceSettings.models) &&
        Objects.equals(this.multiSourceQos, edgeDeviceSettings.multiSourceQos) &&
        Objects.equals(this.ntp, edgeDeviceSettings.ntp) &&
        Objects.equals(this.ntpServer, edgeDeviceSettings.ntpServer) &&
        Objects.equals(this.radioSettings, edgeDeviceSettings.radioSettings) &&
        Objects.equals(this.l2Settings, edgeDeviceSettings.l2Settings) &&
        Objects.equals(this.globalIPv6Settings, edgeDeviceSettings.globalIPv6Settings) &&
        Objects.equals(this.routedInterfaces, edgeDeviceSettings.routedInterfaces) &&
        Objects.equals(this.segments, edgeDeviceSettings.segments) &&
        Objects.equals(this.snmp, edgeDeviceSettings.snmp) &&
        Objects.equals(this.softwareUpdate, edgeDeviceSettings.softwareUpdate) &&
        Objects.equals(this.tacacs, edgeDeviceSettings.tacacs) &&
        Objects.equals(this.vnfs, edgeDeviceSettings.vnfs) &&
        Objects.equals(this.zscaler, edgeDeviceSettings.zscaler) &&
        Objects.equals(this._href, edgeDeviceSettings._href);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bfd, ha, lan, loopbackInterfaces, models, multiSourceQos, ntp, ntpServer, radioSettings, l2Settings, globalIPv6Settings, routedInterfaces, segments, snmp, softwareUpdate, tacacs, vnfs, zscaler, _href);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EdgeDeviceSettings {\n");
    
    sb.append("    bfd: ").append(toIndentedString(bfd)).append("\n");
    sb.append("    ha: ").append(toIndentedString(ha)).append("\n");
    sb.append("    lan: ").append(toIndentedString(lan)).append("\n");
    sb.append("    loopbackInterfaces: ").append(toIndentedString(loopbackInterfaces)).append("\n");
    sb.append("    models: ").append(toIndentedString(models)).append("\n");
    sb.append("    multiSourceQos: ").append(toIndentedString(multiSourceQos)).append("\n");
    sb.append("    ntp: ").append(toIndentedString(ntp)).append("\n");
    sb.append("    ntpServer: ").append(toIndentedString(ntpServer)).append("\n");
    sb.append("    radioSettings: ").append(toIndentedString(radioSettings)).append("\n");
    sb.append("    l2Settings: ").append(toIndentedString(l2Settings)).append("\n");
    sb.append("    globalIPv6Settings: ").append(toIndentedString(globalIPv6Settings)).append("\n");
    sb.append("    routedInterfaces: ").append(toIndentedString(routedInterfaces)).append("\n");
    sb.append("    segments: ").append(toIndentedString(segments)).append("\n");
    sb.append("    snmp: ").append(toIndentedString(snmp)).append("\n");
    sb.append("    softwareUpdate: ").append(toIndentedString(softwareUpdate)).append("\n");
    sb.append("    tacacs: ").append(toIndentedString(tacacs)).append("\n");
    sb.append("    vnfs: ").append(toIndentedString(vnfs)).append("\n");
    sb.append("    zscaler: ").append(toIndentedString(zscaler)).append("\n");
    sb.append("    _href: ").append(toIndentedString(_href)).append("\n");
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
