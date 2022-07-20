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
import org.opennms.velocloud.client.model.BaseApiResource10;
import org.opennms.velocloud.client.model.DeviceSettingsAuthentication;
import org.opennms.velocloud.client.model.DeviceSettingsBFD;
import org.opennms.velocloud.client.model.DeviceSettingsBGP;
import org.opennms.velocloud.client.model.DeviceSettingsCloudSecurity;
import org.opennms.velocloud.client.model.DeviceSettingsDNS;
import org.opennms.velocloud.client.model.DeviceSettingsHandoffGateways;
import org.opennms.velocloud.client.model.DeviceSettingsMultiSourceQOS;
import org.opennms.velocloud.client.model.DeviceSettingsMulticast;
import org.opennms.velocloud.client.model.DeviceSettingsNAT;
import org.opennms.velocloud.client.model.DeviceSettingsNTP;
import org.opennms.velocloud.client.model.DeviceSettingsNVSFromEdge;
import org.opennms.velocloud.client.model.DeviceSettingsNetflow;
import org.opennms.velocloud.client.model.DeviceSettingsOSPF;
import org.opennms.velocloud.client.model.DeviceSettingsSNMP;
import org.opennms.velocloud.client.model.DeviceSettingsSecureAccess;
import org.opennms.velocloud.client.model.DeviceSettingsSyslog;
import org.opennms.velocloud.client.model.DeviceSettingsVPN;
import org.opennms.velocloud.client.model.DeviceSettingsVQM;
import org.opennms.velocloud.client.model.DeviceSettingsVRRP;
import org.opennms.velocloud.client.model.EnterpriseDeviceSettingsAnalyticsSettings;
import org.opennms.velocloud.client.model.EnterpriseDeviceSettingsRoutes;
/**
 * DeviceSettingsSegment
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")
public class DeviceSettingsSegment {
  @JsonProperty("authentication")
  private DeviceSettingsAuthentication authentication = null;

  @JsonProperty("bgp")
  private DeviceSettingsBGP bgp = null;

  @JsonProperty("css")
  private DeviceSettingsCloudSecurity css = null;

  @JsonProperty("secureAccess")
  private DeviceSettingsSecureAccess secureAccess = null;

  @JsonProperty("edgeDirect")
  private DeviceSettingsNVSFromEdge edgeDirect = null;

  @JsonProperty("dns")
  private DeviceSettingsDNS dns = null;

  @JsonProperty("handOffControllers")
  private DeviceSettingsHandoffGateways handOffControllers = null;

  @JsonProperty("handOffGateways")
  private DeviceSettingsHandoffGateways handOffGateways = null;

  @JsonProperty("multiSourceQos")
  private DeviceSettingsMultiSourceQOS multiSourceQos = null;

  @JsonProperty("multicast")
  private DeviceSettingsMulticast multicast = null;

  @JsonProperty("nat")
  private DeviceSettingsNAT nat = null;

  @JsonProperty("netflow")
  private DeviceSettingsNetflow netflow = null;

  @JsonProperty("ntp")
  private DeviceSettingsNTP ntp = null;

  @JsonProperty("ospf")
  private DeviceSettingsOSPF ospf = null;

  @JsonProperty("routes")
  private EnterpriseDeviceSettingsRoutes routes = null;

  @JsonProperty("segment")
  private BaseApiResource10 segment = null;

  @JsonProperty("snmp")
  private DeviceSettingsSNMP snmp = null;

  @JsonProperty("syslog")
  private DeviceSettingsSyslog syslog = null;

  @JsonProperty("vpn")
  private DeviceSettingsVPN vpn = null;

  @JsonProperty("vqm")
  private DeviceSettingsVQM vqm = null;

  @JsonProperty("vrrp")
  private DeviceSettingsVRRP vrrp = null;

  @JsonProperty("analyticsSettings")
  private EnterpriseDeviceSettingsAnalyticsSettings analyticsSettings = null;

  @JsonProperty("bfd")
  private DeviceSettingsBFD bfd = null;

  public DeviceSettingsSegment authentication(DeviceSettingsAuthentication authentication) {
    this.authentication = authentication;
    return this;
  }

   /**
   * Get authentication
   * @return authentication
  **/
  @Schema(description = "")
  public DeviceSettingsAuthentication getAuthentication() {
    return authentication;
  }

  public void setAuthentication(DeviceSettingsAuthentication authentication) {
    this.authentication = authentication;
  }

  public DeviceSettingsSegment bgp(DeviceSettingsBGP bgp) {
    this.bgp = bgp;
    return this;
  }

   /**
   * Get bgp
   * @return bgp
  **/
  @Schema(description = "")
  public DeviceSettingsBGP getBgp() {
    return bgp;
  }

  public void setBgp(DeviceSettingsBGP bgp) {
    this.bgp = bgp;
  }

  public DeviceSettingsSegment css(DeviceSettingsCloudSecurity css) {
    this.css = css;
    return this;
  }

   /**
   * Get css
   * @return css
  **/
  @Schema(description = "")
  public DeviceSettingsCloudSecurity getCss() {
    return css;
  }

  public void setCss(DeviceSettingsCloudSecurity css) {
    this.css = css;
  }

  public DeviceSettingsSegment secureAccess(DeviceSettingsSecureAccess secureAccess) {
    this.secureAccess = secureAccess;
    return this;
  }

   /**
   * Get secureAccess
   * @return secureAccess
  **/
  @Schema(description = "")
  public DeviceSettingsSecureAccess getSecureAccess() {
    return secureAccess;
  }

  public void setSecureAccess(DeviceSettingsSecureAccess secureAccess) {
    this.secureAccess = secureAccess;
  }

  public DeviceSettingsSegment edgeDirect(DeviceSettingsNVSFromEdge edgeDirect) {
    this.edgeDirect = edgeDirect;
    return this;
  }

   /**
   * Get edgeDirect
   * @return edgeDirect
  **/
  @Schema(description = "")
  public DeviceSettingsNVSFromEdge getEdgeDirect() {
    return edgeDirect;
  }

  public void setEdgeDirect(DeviceSettingsNVSFromEdge edgeDirect) {
    this.edgeDirect = edgeDirect;
  }

  public DeviceSettingsSegment dns(DeviceSettingsDNS dns) {
    this.dns = dns;
    return this;
  }

   /**
   * Get dns
   * @return dns
  **/
  @Schema(description = "")
  public DeviceSettingsDNS getDns() {
    return dns;
  }

  public void setDns(DeviceSettingsDNS dns) {
    this.dns = dns;
  }

  public DeviceSettingsSegment handOffControllers(DeviceSettingsHandoffGateways handOffControllers) {
    this.handOffControllers = handOffControllers;
    return this;
  }

   /**
   * Get handOffControllers
   * @return handOffControllers
  **/
  @Schema(description = "")
  public DeviceSettingsHandoffGateways getHandOffControllers() {
    return handOffControllers;
  }

  public void setHandOffControllers(DeviceSettingsHandoffGateways handOffControllers) {
    this.handOffControllers = handOffControllers;
  }

  public DeviceSettingsSegment handOffGateways(DeviceSettingsHandoffGateways handOffGateways) {
    this.handOffGateways = handOffGateways;
    return this;
  }

   /**
   * Get handOffGateways
   * @return handOffGateways
  **/
  @Schema(description = "")
  public DeviceSettingsHandoffGateways getHandOffGateways() {
    return handOffGateways;
  }

  public void setHandOffGateways(DeviceSettingsHandoffGateways handOffGateways) {
    this.handOffGateways = handOffGateways;
  }

  public DeviceSettingsSegment multiSourceQos(DeviceSettingsMultiSourceQOS multiSourceQos) {
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

  public DeviceSettingsSegment multicast(DeviceSettingsMulticast multicast) {
    this.multicast = multicast;
    return this;
  }

   /**
   * Get multicast
   * @return multicast
  **/
  @Schema(description = "")
  public DeviceSettingsMulticast getMulticast() {
    return multicast;
  }

  public void setMulticast(DeviceSettingsMulticast multicast) {
    this.multicast = multicast;
  }

  public DeviceSettingsSegment nat(DeviceSettingsNAT nat) {
    this.nat = nat;
    return this;
  }

   /**
   * Get nat
   * @return nat
  **/
  @Schema(description = "")
  public DeviceSettingsNAT getNat() {
    return nat;
  }

  public void setNat(DeviceSettingsNAT nat) {
    this.nat = nat;
  }

  public DeviceSettingsSegment netflow(DeviceSettingsNetflow netflow) {
    this.netflow = netflow;
    return this;
  }

   /**
   * Get netflow
   * @return netflow
  **/
  @Schema(description = "")
  public DeviceSettingsNetflow getNetflow() {
    return netflow;
  }

  public void setNetflow(DeviceSettingsNetflow netflow) {
    this.netflow = netflow;
  }

  public DeviceSettingsSegment ntp(DeviceSettingsNTP ntp) {
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

  public DeviceSettingsSegment ospf(DeviceSettingsOSPF ospf) {
    this.ospf = ospf;
    return this;
  }

   /**
   * Get ospf
   * @return ospf
  **/
  @Schema(description = "")
  public DeviceSettingsOSPF getOspf() {
    return ospf;
  }

  public void setOspf(DeviceSettingsOSPF ospf) {
    this.ospf = ospf;
  }

  public DeviceSettingsSegment routes(EnterpriseDeviceSettingsRoutes routes) {
    this.routes = routes;
    return this;
  }

   /**
   * Get routes
   * @return routes
  **/
  @Schema(description = "")
  public EnterpriseDeviceSettingsRoutes getRoutes() {
    return routes;
  }

  public void setRoutes(EnterpriseDeviceSettingsRoutes routes) {
    this.routes = routes;
  }

  public DeviceSettingsSegment segment(BaseApiResource10 segment) {
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

  public DeviceSettingsSegment snmp(DeviceSettingsSNMP snmp) {
    this.snmp = snmp;
    return this;
  }

   /**
   * Get snmp
   * @return snmp
  **/
  @Schema(description = "")
  public DeviceSettingsSNMP getSnmp() {
    return snmp;
  }

  public void setSnmp(DeviceSettingsSNMP snmp) {
    this.snmp = snmp;
  }

  public DeviceSettingsSegment syslog(DeviceSettingsSyslog syslog) {
    this.syslog = syslog;
    return this;
  }

   /**
   * Get syslog
   * @return syslog
  **/
  @Schema(description = "")
  public DeviceSettingsSyslog getSyslog() {
    return syslog;
  }

  public void setSyslog(DeviceSettingsSyslog syslog) {
    this.syslog = syslog;
  }

  public DeviceSettingsSegment vpn(DeviceSettingsVPN vpn) {
    this.vpn = vpn;
    return this;
  }

   /**
   * Get vpn
   * @return vpn
  **/
  @Schema(description = "")
  public DeviceSettingsVPN getVpn() {
    return vpn;
  }

  public void setVpn(DeviceSettingsVPN vpn) {
    this.vpn = vpn;
  }

  public DeviceSettingsSegment vqm(DeviceSettingsVQM vqm) {
    this.vqm = vqm;
    return this;
  }

   /**
   * Get vqm
   * @return vqm
  **/
  @Schema(description = "")
  public DeviceSettingsVQM getVqm() {
    return vqm;
  }

  public void setVqm(DeviceSettingsVQM vqm) {
    this.vqm = vqm;
  }

  public DeviceSettingsSegment vrrp(DeviceSettingsVRRP vrrp) {
    this.vrrp = vrrp;
    return this;
  }

   /**
   * Get vrrp
   * @return vrrp
  **/
  @Schema(description = "")
  public DeviceSettingsVRRP getVrrp() {
    return vrrp;
  }

  public void setVrrp(DeviceSettingsVRRP vrrp) {
    this.vrrp = vrrp;
  }

  public DeviceSettingsSegment analyticsSettings(EnterpriseDeviceSettingsAnalyticsSettings analyticsSettings) {
    this.analyticsSettings = analyticsSettings;
    return this;
  }

   /**
   * Get analyticsSettings
   * @return analyticsSettings
  **/
  @Schema(description = "")
  public EnterpriseDeviceSettingsAnalyticsSettings getAnalyticsSettings() {
    return analyticsSettings;
  }

  public void setAnalyticsSettings(EnterpriseDeviceSettingsAnalyticsSettings analyticsSettings) {
    this.analyticsSettings = analyticsSettings;
  }

  public DeviceSettingsSegment bfd(DeviceSettingsBFD bfd) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceSettingsSegment deviceSettingsSegment = (DeviceSettingsSegment) o;
    return Objects.equals(this.authentication, deviceSettingsSegment.authentication) &&
        Objects.equals(this.bgp, deviceSettingsSegment.bgp) &&
        Objects.equals(this.css, deviceSettingsSegment.css) &&
        Objects.equals(this.secureAccess, deviceSettingsSegment.secureAccess) &&
        Objects.equals(this.edgeDirect, deviceSettingsSegment.edgeDirect) &&
        Objects.equals(this.dns, deviceSettingsSegment.dns) &&
        Objects.equals(this.handOffControllers, deviceSettingsSegment.handOffControllers) &&
        Objects.equals(this.handOffGateways, deviceSettingsSegment.handOffGateways) &&
        Objects.equals(this.multiSourceQos, deviceSettingsSegment.multiSourceQos) &&
        Objects.equals(this.multicast, deviceSettingsSegment.multicast) &&
        Objects.equals(this.nat, deviceSettingsSegment.nat) &&
        Objects.equals(this.netflow, deviceSettingsSegment.netflow) &&
        Objects.equals(this.ntp, deviceSettingsSegment.ntp) &&
        Objects.equals(this.ospf, deviceSettingsSegment.ospf) &&
        Objects.equals(this.routes, deviceSettingsSegment.routes) &&
        Objects.equals(this.segment, deviceSettingsSegment.segment) &&
        Objects.equals(this.snmp, deviceSettingsSegment.snmp) &&
        Objects.equals(this.syslog, deviceSettingsSegment.syslog) &&
        Objects.equals(this.vpn, deviceSettingsSegment.vpn) &&
        Objects.equals(this.vqm, deviceSettingsSegment.vqm) &&
        Objects.equals(this.vrrp, deviceSettingsSegment.vrrp) &&
        Objects.equals(this.analyticsSettings, deviceSettingsSegment.analyticsSettings) &&
        Objects.equals(this.bfd, deviceSettingsSegment.bfd);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authentication, bgp, css, secureAccess, edgeDirect, dns, handOffControllers, handOffGateways, multiSourceQos, multicast, nat, netflow, ntp, ospf, routes, segment, snmp, syslog, vpn, vqm, vrrp, analyticsSettings, bfd);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceSettingsSegment {\n");
    
    sb.append("    authentication: ").append(toIndentedString(authentication)).append("\n");
    sb.append("    bgp: ").append(toIndentedString(bgp)).append("\n");
    sb.append("    css: ").append(toIndentedString(css)).append("\n");
    sb.append("    secureAccess: ").append(toIndentedString(secureAccess)).append("\n");
    sb.append("    edgeDirect: ").append(toIndentedString(edgeDirect)).append("\n");
    sb.append("    dns: ").append(toIndentedString(dns)).append("\n");
    sb.append("    handOffControllers: ").append(toIndentedString(handOffControllers)).append("\n");
    sb.append("    handOffGateways: ").append(toIndentedString(handOffGateways)).append("\n");
    sb.append("    multiSourceQos: ").append(toIndentedString(multiSourceQos)).append("\n");
    sb.append("    multicast: ").append(toIndentedString(multicast)).append("\n");
    sb.append("    nat: ").append(toIndentedString(nat)).append("\n");
    sb.append("    netflow: ").append(toIndentedString(netflow)).append("\n");
    sb.append("    ntp: ").append(toIndentedString(ntp)).append("\n");
    sb.append("    ospf: ").append(toIndentedString(ospf)).append("\n");
    sb.append("    routes: ").append(toIndentedString(routes)).append("\n");
    sb.append("    segment: ").append(toIndentedString(segment)).append("\n");
    sb.append("    snmp: ").append(toIndentedString(snmp)).append("\n");
    sb.append("    syslog: ").append(toIndentedString(syslog)).append("\n");
    sb.append("    vpn: ").append(toIndentedString(vpn)).append("\n");
    sb.append("    vqm: ").append(toIndentedString(vqm)).append("\n");
    sb.append("    vrrp: ").append(toIndentedString(vrrp)).append("\n");
    sb.append("    analyticsSettings: ").append(toIndentedString(analyticsSettings)).append("\n");
    sb.append("    bfd: ").append(toIndentedString(bfd)).append("\n");
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
