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
import org.opennms.velocloud.client.model.BaseApiResource11;
/**
 * DeviceSettingsSecurityVNFVM
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")
public class DeviceSettingsSecurityVNFVM {
  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    SECURITYVNF("securityVnf");

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

  /**
   * Gets or Sets vendor
   */
  public enum VendorEnum {
    PALOALTO("PaloAlto"),
    CHECKPOINT("CheckPoint"),
    FORTINET("Fortinet"),
    CENTOS("CentOS");

    private String value;

    VendorEnum(String value) {
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
    public static VendorEnum fromValue(String text) {
      for (VendorEnum b : VendorEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("vendor")
  private VendorEnum vendor = null;

  @JsonProperty("cidrIp")
  private String cidrIp = null;

  @JsonProperty("hostname")
  private String hostname = null;

  @JsonProperty("insertionEnabled")
  private Boolean insertionEnabled = null;

  @JsonProperty("instanceId")
  private Integer instanceId = null;

  @JsonProperty("vlanId")
  private Integer vlanId = null;

  @JsonProperty("vmDeploy")
  private Boolean vmDeploy = null;

  @JsonProperty("vmPowerOff")
  private Boolean vmPowerOff = null;

  @JsonProperty("vendorSpecificData")
  private OneOfDeviceSettingsSecurityVNFVMVendorSpecificData vendorSpecificData = null;

  @JsonProperty("edge")
  private Object edge = null;

  @JsonProperty("license")
  private BaseApiResource11 license = null;

  @JsonProperty("service")
  private BaseApiResource11 service = null;

  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("uuidTimestamp")
  private OffsetDateTime uuidTimestamp = null;

  @JsonProperty("modifiedTimestamp")
  private Integer modifiedTimestamp = null;

  public DeviceSettingsSecurityVNFVM type(TypeEnum type) {
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

  public DeviceSettingsSecurityVNFVM vendor(VendorEnum vendor) {
    this.vendor = vendor;
    return this;
  }

   /**
   * Get vendor
   * @return vendor
  **/
  @Schema(description = "")
  public VendorEnum getVendor() {
    return vendor;
  }

  public void setVendor(VendorEnum vendor) {
    this.vendor = vendor;
  }

  public DeviceSettingsSecurityVNFVM cidrIp(String cidrIp) {
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

  public DeviceSettingsSecurityVNFVM hostname(String hostname) {
    this.hostname = hostname;
    return this;
  }

   /**
   * Get hostname
   * @return hostname
  **/
  @Schema(description = "")
  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public DeviceSettingsSecurityVNFVM insertionEnabled(Boolean insertionEnabled) {
    this.insertionEnabled = insertionEnabled;
    return this;
  }

   /**
   * Get insertionEnabled
   * @return insertionEnabled
  **/
  @Schema(description = "")
  public Boolean isInsertionEnabled() {
    return insertionEnabled;
  }

  public void setInsertionEnabled(Boolean insertionEnabled) {
    this.insertionEnabled = insertionEnabled;
  }

  public DeviceSettingsSecurityVNFVM instanceId(Integer instanceId) {
    this.instanceId = instanceId;
    return this;
  }

   /**
   * Get instanceId
   * @return instanceId
  **/
  @Schema(description = "")
  public Integer getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(Integer instanceId) {
    this.instanceId = instanceId;
  }

  public DeviceSettingsSecurityVNFVM vlanId(Integer vlanId) {
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

  public DeviceSettingsSecurityVNFVM vmDeploy(Boolean vmDeploy) {
    this.vmDeploy = vmDeploy;
    return this;
  }

   /**
   * Get vmDeploy
   * @return vmDeploy
  **/
  @Schema(description = "")
  public Boolean isVmDeploy() {
    return vmDeploy;
  }

  public void setVmDeploy(Boolean vmDeploy) {
    this.vmDeploy = vmDeploy;
  }

  public DeviceSettingsSecurityVNFVM vmPowerOff(Boolean vmPowerOff) {
    this.vmPowerOff = vmPowerOff;
    return this;
  }

   /**
   * Get vmPowerOff
   * @return vmPowerOff
  **/
  @Schema(description = "")
  public Boolean isVmPowerOff() {
    return vmPowerOff;
  }

  public void setVmPowerOff(Boolean vmPowerOff) {
    this.vmPowerOff = vmPowerOff;
  }

  public DeviceSettingsSecurityVNFVM vendorSpecificData(OneOfDeviceSettingsSecurityVNFVMVendorSpecificData vendorSpecificData) {
    this.vendorSpecificData = vendorSpecificData;
    return this;
  }

   /**
   * Get vendorSpecificData
   * @return vendorSpecificData
  **/
  @Schema(description = "")
  public OneOfDeviceSettingsSecurityVNFVMVendorSpecificData getVendorSpecificData() {
    return vendorSpecificData;
  }

  public void setVendorSpecificData(OneOfDeviceSettingsSecurityVNFVMVendorSpecificData vendorSpecificData) {
    this.vendorSpecificData = vendorSpecificData;
  }

  public DeviceSettingsSecurityVNFVM edge(Object edge) {
    this.edge = edge;
    return this;
  }

   /**
   * Get edge
   * @return edge
  **/
  @Schema(description = "")
  public Object getEdge() {
    return edge;
  }

  public void setEdge(Object edge) {
    this.edge = edge;
  }

  public DeviceSettingsSecurityVNFVM license(BaseApiResource11 license) {
    this.license = license;
    return this;
  }

   /**
   * Get license
   * @return license
  **/
  @Schema(description = "")
  public BaseApiResource11 getLicense() {
    return license;
  }

  public void setLicense(BaseApiResource11 license) {
    this.license = license;
  }

  public DeviceSettingsSecurityVNFVM service(BaseApiResource11 service) {
    this.service = service;
    return this;
  }

   /**
   * Get service
   * @return service
  **/
  @Schema(description = "")
  public BaseApiResource11 getService() {
    return service;
  }

  public void setService(BaseApiResource11 service) {
    this.service = service;
  }

  public DeviceSettingsSecurityVNFVM uuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

   /**
   * Get uuid
   * @return uuid
  **/
  @Schema(description = "")
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public DeviceSettingsSecurityVNFVM uuidTimestamp(OffsetDateTime uuidTimestamp) {
    this.uuidTimestamp = uuidTimestamp;
    return this;
  }

   /**
   * Get uuidTimestamp
   * @return uuidTimestamp
  **/
  @Schema(description = "")
  public OffsetDateTime getUuidTimestamp() {
    return uuidTimestamp;
  }

  public void setUuidTimestamp(OffsetDateTime uuidTimestamp) {
    this.uuidTimestamp = uuidTimestamp;
  }

  public DeviceSettingsSecurityVNFVM modifiedTimestamp(Integer modifiedTimestamp) {
    this.modifiedTimestamp = modifiedTimestamp;
    return this;
  }

   /**
   * Get modifiedTimestamp
   * @return modifiedTimestamp
  **/
  @Schema(description = "")
  public Integer getModifiedTimestamp() {
    return modifiedTimestamp;
  }

  public void setModifiedTimestamp(Integer modifiedTimestamp) {
    this.modifiedTimestamp = modifiedTimestamp;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceSettingsSecurityVNFVM deviceSettingsSecurityVNFVM = (DeviceSettingsSecurityVNFVM) o;
    return Objects.equals(this.type, deviceSettingsSecurityVNFVM.type) &&
        Objects.equals(this.vendor, deviceSettingsSecurityVNFVM.vendor) &&
        Objects.equals(this.cidrIp, deviceSettingsSecurityVNFVM.cidrIp) &&
        Objects.equals(this.hostname, deviceSettingsSecurityVNFVM.hostname) &&
        Objects.equals(this.insertionEnabled, deviceSettingsSecurityVNFVM.insertionEnabled) &&
        Objects.equals(this.instanceId, deviceSettingsSecurityVNFVM.instanceId) &&
        Objects.equals(this.vlanId, deviceSettingsSecurityVNFVM.vlanId) &&
        Objects.equals(this.vmDeploy, deviceSettingsSecurityVNFVM.vmDeploy) &&
        Objects.equals(this.vmPowerOff, deviceSettingsSecurityVNFVM.vmPowerOff) &&
        Objects.equals(this.vendorSpecificData, deviceSettingsSecurityVNFVM.vendorSpecificData) &&
        Objects.equals(this.edge, deviceSettingsSecurityVNFVM.edge) &&
        Objects.equals(this.license, deviceSettingsSecurityVNFVM.license) &&
        Objects.equals(this.service, deviceSettingsSecurityVNFVM.service) &&
        Objects.equals(this.uuid, deviceSettingsSecurityVNFVM.uuid) &&
        Objects.equals(this.uuidTimestamp, deviceSettingsSecurityVNFVM.uuidTimestamp) &&
        Objects.equals(this.modifiedTimestamp, deviceSettingsSecurityVNFVM.modifiedTimestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, vendor, cidrIp, hostname, insertionEnabled, instanceId, vlanId, vmDeploy, vmPowerOff, vendorSpecificData, edge, license, service, uuid, uuidTimestamp, modifiedTimestamp);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceSettingsSecurityVNFVM {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    vendor: ").append(toIndentedString(vendor)).append("\n");
    sb.append("    cidrIp: ").append(toIndentedString(cidrIp)).append("\n");
    sb.append("    hostname: ").append(toIndentedString(hostname)).append("\n");
    sb.append("    insertionEnabled: ").append(toIndentedString(insertionEnabled)).append("\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    vlanId: ").append(toIndentedString(vlanId)).append("\n");
    sb.append("    vmDeploy: ").append(toIndentedString(vmDeploy)).append("\n");
    sb.append("    vmPowerOff: ").append(toIndentedString(vmPowerOff)).append("\n");
    sb.append("    vendorSpecificData: ").append(toIndentedString(vendorSpecificData)).append("\n");
    sb.append("    edge: ").append(toIndentedString(edge)).append("\n");
    sb.append("    license: ").append(toIndentedString(license)).append("\n");
    sb.append("    service: ").append(toIndentedString(service)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    uuidTimestamp: ").append(toIndentedString(uuidTimestamp)).append("\n");
    sb.append("    modifiedTimestamp: ").append(toIndentedString(modifiedTimestamp)).append("\n");
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
