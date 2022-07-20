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
/**
 * BaseApiResource10
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")
public class BaseApiResource10 {
  @JsonProperty("_href")
  private String _href = null;

  @JsonProperty("created")
  private OffsetDateTime created = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("object")
  private String object = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("type")
  private String type = null;

  /**
   * Gets or Sets alertsEnabled
   */
  public enum AlertsEnabledEnum {
    NUMBER_0(0),
    NUMBER_1(1);

    private Integer value;

    AlertsEnabledEnum(Integer value) {
      this.value = value;
    }
    @JsonValue
    public Integer getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static AlertsEnabledEnum fromValue(String text) {
      for (AlertsEnabledEnum b : AlertsEnabledEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("alertsEnabled")
  private AlertsEnabledEnum alertsEnabled = null;

  /**
   * Gets or Sets operatorAlertsEnabled
   */
  public enum OperatorAlertsEnabledEnum {
    NUMBER_0(0),
    NUMBER_1(1);

    private Integer value;

    OperatorAlertsEnabledEnum(Integer value) {
      this.value = value;
    }
    @JsonValue
    public Integer getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static OperatorAlertsEnabledEnum fromValue(String text) {
      for (OperatorAlertsEnabledEnum b : OperatorAlertsEnabledEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("operatorAlertsEnabled")
  private OperatorAlertsEnabledEnum operatorAlertsEnabled = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("statusModified")
  private OffsetDateTime statusModified = null;

  @JsonProperty("previousData")
  private Object previousData = null;

  @JsonProperty("previousCreated")
  private OffsetDateTime previousCreated = null;

  @JsonProperty("draftData")
  private String draftData = null;

  @JsonProperty("draftCreated")
  private OffsetDateTime draftCreated = null;

  @JsonProperty("draftComment")
  private String draftComment = null;

  @JsonProperty("data")
  private Object data = null;

  @JsonProperty("lastContact")
  private OffsetDateTime lastContact = null;

  @JsonProperty("version")
  private String version = null;

  @JsonProperty("modified")
  private OffsetDateTime modified = null;

  public BaseApiResource10 _href(String _href) {
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

  public BaseApiResource10 created(OffsetDateTime created) {
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

  public BaseApiResource10 description(String description) {
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

  public BaseApiResource10 object(String object) {
    this.object = object;
    return this;
  }

   /**
   * Get object
   * @return object
  **/
  @Schema(description = "")
  public String getObject() {
    return object;
  }

  public void setObject(String object) {
    this.object = object;
  }

  public BaseApiResource10 name(String name) {
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

  public BaseApiResource10 type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @Schema(description = "")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public BaseApiResource10 alertsEnabled(AlertsEnabledEnum alertsEnabled) {
    this.alertsEnabled = alertsEnabled;
    return this;
  }

   /**
   * Get alertsEnabled
   * @return alertsEnabled
  **/
  @Schema(description = "")
  public AlertsEnabledEnum getAlertsEnabled() {
    return alertsEnabled;
  }

  public void setAlertsEnabled(AlertsEnabledEnum alertsEnabled) {
    this.alertsEnabled = alertsEnabled;
  }

  public BaseApiResource10 operatorAlertsEnabled(OperatorAlertsEnabledEnum operatorAlertsEnabled) {
    this.operatorAlertsEnabled = operatorAlertsEnabled;
    return this;
  }

   /**
   * Get operatorAlertsEnabled
   * @return operatorAlertsEnabled
  **/
  @Schema(description = "")
  public OperatorAlertsEnabledEnum getOperatorAlertsEnabled() {
    return operatorAlertsEnabled;
  }

  public void setOperatorAlertsEnabled(OperatorAlertsEnabledEnum operatorAlertsEnabled) {
    this.operatorAlertsEnabled = operatorAlertsEnabled;
  }

  public BaseApiResource10 status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @Schema(description = "")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public BaseApiResource10 statusModified(OffsetDateTime statusModified) {
    this.statusModified = statusModified;
    return this;
  }

   /**
   * Get statusModified
   * @return statusModified
  **/
  @Schema(description = "")
  public OffsetDateTime getStatusModified() {
    return statusModified;
  }

  public void setStatusModified(OffsetDateTime statusModified) {
    this.statusModified = statusModified;
  }

  public BaseApiResource10 previousData(Object previousData) {
    this.previousData = previousData;
    return this;
  }

   /**
   * Get previousData
   * @return previousData
  **/
  @Schema(description = "")
  public Object getPreviousData() {
    return previousData;
  }

  public void setPreviousData(Object previousData) {
    this.previousData = previousData;
  }

  public BaseApiResource10 previousCreated(OffsetDateTime previousCreated) {
    this.previousCreated = previousCreated;
    return this;
  }

   /**
   * Get previousCreated
   * @return previousCreated
  **/
  @Schema(description = "")
  public OffsetDateTime getPreviousCreated() {
    return previousCreated;
  }

  public void setPreviousCreated(OffsetDateTime previousCreated) {
    this.previousCreated = previousCreated;
  }

  public BaseApiResource10 draftData(String draftData) {
    this.draftData = draftData;
    return this;
  }

   /**
   * Get draftData
   * @return draftData
  **/
  @Schema(description = "")
  public String getDraftData() {
    return draftData;
  }

  public void setDraftData(String draftData) {
    this.draftData = draftData;
  }

  public BaseApiResource10 draftCreated(OffsetDateTime draftCreated) {
    this.draftCreated = draftCreated;
    return this;
  }

   /**
   * Get draftCreated
   * @return draftCreated
  **/
  @Schema(description = "")
  public OffsetDateTime getDraftCreated() {
    return draftCreated;
  }

  public void setDraftCreated(OffsetDateTime draftCreated) {
    this.draftCreated = draftCreated;
  }

  public BaseApiResource10 draftComment(String draftComment) {
    this.draftComment = draftComment;
    return this;
  }

   /**
   * Get draftComment
   * @return draftComment
  **/
  @Schema(description = "")
  public String getDraftComment() {
    return draftComment;
  }

  public void setDraftComment(String draftComment) {
    this.draftComment = draftComment;
  }

  public BaseApiResource10 data(Object data) {
    this.data = data;
    return this;
  }

   /**
   * Get data
   * @return data
  **/
  @Schema(description = "")
  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public BaseApiResource10 lastContact(OffsetDateTime lastContact) {
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

  public BaseApiResource10 version(String version) {
    this.version = version;
    return this;
  }

   /**
   * Get version
   * @return version
  **/
  @Schema(description = "")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public BaseApiResource10 modified(OffsetDateTime modified) {
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
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseApiResource10 baseApiResource10 = (BaseApiResource10) o;
    return Objects.equals(this._href, baseApiResource10._href) &&
        Objects.equals(this.created, baseApiResource10.created) &&
        Objects.equals(this.description, baseApiResource10.description) &&
        Objects.equals(this.object, baseApiResource10.object) &&
        Objects.equals(this.name, baseApiResource10.name) &&
        Objects.equals(this.type, baseApiResource10.type) &&
        Objects.equals(this.alertsEnabled, baseApiResource10.alertsEnabled) &&
        Objects.equals(this.operatorAlertsEnabled, baseApiResource10.operatorAlertsEnabled) &&
        Objects.equals(this.status, baseApiResource10.status) &&
        Objects.equals(this.statusModified, baseApiResource10.statusModified) &&
        Objects.equals(this.previousData, baseApiResource10.previousData) &&
        Objects.equals(this.previousCreated, baseApiResource10.previousCreated) &&
        Objects.equals(this.draftData, baseApiResource10.draftData) &&
        Objects.equals(this.draftCreated, baseApiResource10.draftCreated) &&
        Objects.equals(this.draftComment, baseApiResource10.draftComment) &&
        Objects.equals(this.data, baseApiResource10.data) &&
        Objects.equals(this.lastContact, baseApiResource10.lastContact) &&
        Objects.equals(this.version, baseApiResource10.version) &&
        Objects.equals(this.modified, baseApiResource10.modified);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_href, created, description, object, name, type, alertsEnabled, operatorAlertsEnabled, status, statusModified, previousData, previousCreated, draftData, draftCreated, draftComment, data, lastContact, version, modified);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BaseApiResource10 {\n");
    
    sb.append("    _href: ").append(toIndentedString(_href)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    object: ").append(toIndentedString(object)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    alertsEnabled: ").append(toIndentedString(alertsEnabled)).append("\n");
    sb.append("    operatorAlertsEnabled: ").append(toIndentedString(operatorAlertsEnabled)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    statusModified: ").append(toIndentedString(statusModified)).append("\n");
    sb.append("    previousData: ").append(toIndentedString(previousData)).append("\n");
    sb.append("    previousCreated: ").append(toIndentedString(previousCreated)).append("\n");
    sb.append("    draftData: ").append(toIndentedString(draftData)).append("\n");
    sb.append("    draftCreated: ").append(toIndentedString(draftCreated)).append("\n");
    sb.append("    draftComment: ").append(toIndentedString(draftComment)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    lastContact: ").append(toIndentedString(lastContact)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
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
