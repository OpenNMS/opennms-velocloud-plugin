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
import org.opennms.velocloud.model.BaseApiResource10;
import org.opennms.velocloud.model.DeviceSettingsDHCP;
import org.opennms.velocloud.model.DeviceSettingsLANFixedIp;
import org.opennms.velocloud.model.DeviceSettingsLANMulticast;
import org.opennms.velocloud.model.DeviceSettingsLANOspf;
import org.opennms.velocloud.model.DeviceSettingsLANV6Detail;
/**
 * DeviceSettingsLANNetwork
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class DeviceSettingsLANNetwork {
  @SerializedName("override")
  private Boolean override = null;

  @SerializedName("advertise")
  private Boolean advertise = null;

  @SerializedName("pingResponse")
  private Boolean pingResponse = true;

  @SerializedName("dnsProxy")
  private Boolean dnsProxy = true;

  @SerializedName("bindEdgeAddress")
  private Boolean bindEdgeAddress = null;

  @SerializedName("baseDhcpAddr")
  private AnyOfDeviceSettingsLANNetworkBaseDhcpAddr baseDhcpAddr = null;

  @SerializedName("cidrIp")
  private String cidrIp = null;

  @SerializedName("cidrPrefix")
  private Integer cidrPrefix = null;

  @SerializedName("cost")
  private Integer cost = null;

  @SerializedName("dhcp")
  private DeviceSettingsDHCP dhcp = null;

  @SerializedName("disabled")
  private Boolean disabled = null;

  @SerializedName("fixedIp")
  private List<DeviceSettingsLANFixedIp> fixedIp = null;

  @SerializedName("interfaces")
  private List<String> interfaces = null;

  @SerializedName("multicast")
  private DeviceSettingsLANMulticast multicast = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("netmask")
  private String netmask = null;

  @SerializedName("numDhcpAddr")
  private Integer numDhcpAddr = null;

  @SerializedName("ospf")
  private DeviceSettingsLANOspf ospf = null;

  @SerializedName("segmentId")
  private Integer segmentId = null;

  @SerializedName("segment")
  private BaseApiResource10 segment = null;

  @SerializedName("staticReserved")
  private Integer staticReserved = null;

  @SerializedName("v6Detail")
  private DeviceSettingsLANV6Detail v6Detail = null;

  @SerializedName("vlanId")
  private Integer vlanId = null;

  @SerializedName("vnfInsertion")
  private Boolean vnfInsertion = null;

  public DeviceSettingsLANNetwork override(Boolean override) {
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

  public DeviceSettingsLANNetwork advertise(Boolean advertise) {
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

  public DeviceSettingsLANNetwork pingResponse(Boolean pingResponse) {
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

  public DeviceSettingsLANNetwork dnsProxy(Boolean dnsProxy) {
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

  public DeviceSettingsLANNetwork bindEdgeAddress(Boolean bindEdgeAddress) {
    this.bindEdgeAddress = bindEdgeAddress;
    return this;
  }

   /**
   * Get bindEdgeAddress
   * @return bindEdgeAddress
  **/
  @Schema(description = "")
  public Boolean isBindEdgeAddress() {
    return bindEdgeAddress;
  }

  public void setBindEdgeAddress(Boolean bindEdgeAddress) {
    this.bindEdgeAddress = bindEdgeAddress;
  }

  public DeviceSettingsLANNetwork baseDhcpAddr(AnyOfDeviceSettingsLANNetworkBaseDhcpAddr baseDhcpAddr) {
    this.baseDhcpAddr = baseDhcpAddr;
    return this;
  }

   /**
   * Get baseDhcpAddr
   * @return baseDhcpAddr
  **/
  @Schema(description = "")
  public AnyOfDeviceSettingsLANNetworkBaseDhcpAddr getBaseDhcpAddr() {
    return baseDhcpAddr;
  }

  public void setBaseDhcpAddr(AnyOfDeviceSettingsLANNetworkBaseDhcpAddr baseDhcpAddr) {
    this.baseDhcpAddr = baseDhcpAddr;
  }

  public DeviceSettingsLANNetwork cidrIp(String cidrIp) {
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

  public DeviceSettingsLANNetwork cidrPrefix(Integer cidrPrefix) {
    this.cidrPrefix = cidrPrefix;
    return this;
  }

   /**
   * Get cidrPrefix
   * @return cidrPrefix
  **/
  @Schema(description = "")
  public Integer getCidrPrefix() {
    return cidrPrefix;
  }

  public void setCidrPrefix(Integer cidrPrefix) {
    this.cidrPrefix = cidrPrefix;
  }

  public DeviceSettingsLANNetwork cost(Integer cost) {
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

  public DeviceSettingsLANNetwork dhcp(DeviceSettingsDHCP dhcp) {
    this.dhcp = dhcp;
    return this;
  }

   /**
   * Get dhcp
   * @return dhcp
  **/
  @Schema(description = "")
  public DeviceSettingsDHCP getDhcp() {
    return dhcp;
  }

  public void setDhcp(DeviceSettingsDHCP dhcp) {
    this.dhcp = dhcp;
  }

  public DeviceSettingsLANNetwork disabled(Boolean disabled) {
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

  public DeviceSettingsLANNetwork fixedIp(List<DeviceSettingsLANFixedIp> fixedIp) {
    this.fixedIp = fixedIp;
    return this;
  }

  public DeviceSettingsLANNetwork addFixedIpItem(DeviceSettingsLANFixedIp fixedIpItem) {
    if (this.fixedIp == null) {
      this.fixedIp = new ArrayList<>();
    }
    this.fixedIp.add(fixedIpItem);
    return this;
  }

   /**
   * Get fixedIp
   * @return fixedIp
  **/
  @Schema(description = "")
  public List<DeviceSettingsLANFixedIp> getFixedIp() {
    return fixedIp;
  }

  public void setFixedIp(List<DeviceSettingsLANFixedIp> fixedIp) {
    this.fixedIp = fixedIp;
  }

  public DeviceSettingsLANNetwork interfaces(List<String> interfaces) {
    this.interfaces = interfaces;
    return this;
  }

  public DeviceSettingsLANNetwork addInterfacesItem(String interfacesItem) {
    if (this.interfaces == null) {
      this.interfaces = new ArrayList<>();
    }
    this.interfaces.add(interfacesItem);
    return this;
  }

   /**
   * Get interfaces
   * @return interfaces
  **/
  @Schema(description = "")
  public List<String> getInterfaces() {
    return interfaces;
  }

  public void setInterfaces(List<String> interfaces) {
    this.interfaces = interfaces;
  }

  public DeviceSettingsLANNetwork multicast(DeviceSettingsLANMulticast multicast) {
    this.multicast = multicast;
    return this;
  }

   /**
   * Get multicast
   * @return multicast
  **/
  @Schema(description = "")
  public DeviceSettingsLANMulticast getMulticast() {
    return multicast;
  }

  public void setMulticast(DeviceSettingsLANMulticast multicast) {
    this.multicast = multicast;
  }

  public DeviceSettingsLANNetwork name(String name) {
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

  public DeviceSettingsLANNetwork netmask(String netmask) {
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

  public DeviceSettingsLANNetwork numDhcpAddr(Integer numDhcpAddr) {
    this.numDhcpAddr = numDhcpAddr;
    return this;
  }

   /**
   * Get numDhcpAddr
   * @return numDhcpAddr
  **/
  @Schema(description = "")
  public Integer getNumDhcpAddr() {
    return numDhcpAddr;
  }

  public void setNumDhcpAddr(Integer numDhcpAddr) {
    this.numDhcpAddr = numDhcpAddr;
  }

  public DeviceSettingsLANNetwork ospf(DeviceSettingsLANOspf ospf) {
    this.ospf = ospf;
    return this;
  }

   /**
   * Get ospf
   * @return ospf
  **/
  @Schema(description = "")
  public DeviceSettingsLANOspf getOspf() {
    return ospf;
  }

  public void setOspf(DeviceSettingsLANOspf ospf) {
    this.ospf = ospf;
  }

  public DeviceSettingsLANNetwork segmentId(Integer segmentId) {
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

  public DeviceSettingsLANNetwork segment(BaseApiResource10 segment) {
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

  public DeviceSettingsLANNetwork staticReserved(Integer staticReserved) {
    this.staticReserved = staticReserved;
    return this;
  }

   /**
   * Get staticReserved
   * @return staticReserved
  **/
  @Schema(description = "")
  public Integer getStaticReserved() {
    return staticReserved;
  }

  public void setStaticReserved(Integer staticReserved) {
    this.staticReserved = staticReserved;
  }

  public DeviceSettingsLANNetwork v6Detail(DeviceSettingsLANV6Detail v6Detail) {
    this.v6Detail = v6Detail;
    return this;
  }

   /**
   * Get v6Detail
   * @return v6Detail
  **/
  @Schema(description = "")
  public DeviceSettingsLANV6Detail getV6Detail() {
    return v6Detail;
  }

  public void setV6Detail(DeviceSettingsLANV6Detail v6Detail) {
    this.v6Detail = v6Detail;
  }

  public DeviceSettingsLANNetwork vlanId(Integer vlanId) {
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

  public DeviceSettingsLANNetwork vnfInsertion(Boolean vnfInsertion) {
    this.vnfInsertion = vnfInsertion;
    return this;
  }

   /**
   * Get vnfInsertion
   * @return vnfInsertion
  **/
  @Schema(description = "")
  public Boolean isVnfInsertion() {
    return vnfInsertion;
  }

  public void setVnfInsertion(Boolean vnfInsertion) {
    this.vnfInsertion = vnfInsertion;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceSettingsLANNetwork deviceSettingsLANNetwork = (DeviceSettingsLANNetwork) o;
    return Objects.equals(this.override, deviceSettingsLANNetwork.override) &&
        Objects.equals(this.advertise, deviceSettingsLANNetwork.advertise) &&
        Objects.equals(this.pingResponse, deviceSettingsLANNetwork.pingResponse) &&
        Objects.equals(this.dnsProxy, deviceSettingsLANNetwork.dnsProxy) &&
        Objects.equals(this.bindEdgeAddress, deviceSettingsLANNetwork.bindEdgeAddress) &&
        Objects.equals(this.baseDhcpAddr, deviceSettingsLANNetwork.baseDhcpAddr) &&
        Objects.equals(this.cidrIp, deviceSettingsLANNetwork.cidrIp) &&
        Objects.equals(this.cidrPrefix, deviceSettingsLANNetwork.cidrPrefix) &&
        Objects.equals(this.cost, deviceSettingsLANNetwork.cost) &&
        Objects.equals(this.dhcp, deviceSettingsLANNetwork.dhcp) &&
        Objects.equals(this.disabled, deviceSettingsLANNetwork.disabled) &&
        Objects.equals(this.fixedIp, deviceSettingsLANNetwork.fixedIp) &&
        Objects.equals(this.interfaces, deviceSettingsLANNetwork.interfaces) &&
        Objects.equals(this.multicast, deviceSettingsLANNetwork.multicast) &&
        Objects.equals(this.name, deviceSettingsLANNetwork.name) &&
        Objects.equals(this.netmask, deviceSettingsLANNetwork.netmask) &&
        Objects.equals(this.numDhcpAddr, deviceSettingsLANNetwork.numDhcpAddr) &&
        Objects.equals(this.ospf, deviceSettingsLANNetwork.ospf) &&
        Objects.equals(this.segmentId, deviceSettingsLANNetwork.segmentId) &&
        Objects.equals(this.segment, deviceSettingsLANNetwork.segment) &&
        Objects.equals(this.staticReserved, deviceSettingsLANNetwork.staticReserved) &&
        Objects.equals(this.v6Detail, deviceSettingsLANNetwork.v6Detail) &&
        Objects.equals(this.vlanId, deviceSettingsLANNetwork.vlanId) &&
        Objects.equals(this.vnfInsertion, deviceSettingsLANNetwork.vnfInsertion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(override, advertise, pingResponse, dnsProxy, bindEdgeAddress, baseDhcpAddr, cidrIp, cidrPrefix, cost, dhcp, disabled, fixedIp, interfaces, multicast, name, netmask, numDhcpAddr, ospf, segmentId, segment, staticReserved, v6Detail, vlanId, vnfInsertion);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceSettingsLANNetwork {\n");
    
    sb.append("    override: ").append(toIndentedString(override)).append("\n");
    sb.append("    advertise: ").append(toIndentedString(advertise)).append("\n");
    sb.append("    pingResponse: ").append(toIndentedString(pingResponse)).append("\n");
    sb.append("    dnsProxy: ").append(toIndentedString(dnsProxy)).append("\n");
    sb.append("    bindEdgeAddress: ").append(toIndentedString(bindEdgeAddress)).append("\n");
    sb.append("    baseDhcpAddr: ").append(toIndentedString(baseDhcpAddr)).append("\n");
    sb.append("    cidrIp: ").append(toIndentedString(cidrIp)).append("\n");
    sb.append("    cidrPrefix: ").append(toIndentedString(cidrPrefix)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    dhcp: ").append(toIndentedString(dhcp)).append("\n");
    sb.append("    disabled: ").append(toIndentedString(disabled)).append("\n");
    sb.append("    fixedIp: ").append(toIndentedString(fixedIp)).append("\n");
    sb.append("    interfaces: ").append(toIndentedString(interfaces)).append("\n");
    sb.append("    multicast: ").append(toIndentedString(multicast)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    netmask: ").append(toIndentedString(netmask)).append("\n");
    sb.append("    numDhcpAddr: ").append(toIndentedString(numDhcpAddr)).append("\n");
    sb.append("    ospf: ").append(toIndentedString(ospf)).append("\n");
    sb.append("    segmentId: ").append(toIndentedString(segmentId)).append("\n");
    sb.append("    segment: ").append(toIndentedString(segment)).append("\n");
    sb.append("    staticReserved: ").append(toIndentedString(staticReserved)).append("\n");
    sb.append("    v6Detail: ").append(toIndentedString(v6Detail)).append("\n");
    sb.append("    vlanId: ").append(toIndentedString(vlanId)).append("\n");
    sb.append("    vnfInsertion: ").append(toIndentedString(vnfInsertion)).append("\n");
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
