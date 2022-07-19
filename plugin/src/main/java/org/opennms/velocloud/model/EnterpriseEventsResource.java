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
import org.opennms.velocloud.model.BaseApiResource;
import org.opennms.velocloud.model.BaseApiResource2;
import org.opennms.velocloud.model.BaseApiResource3;
/**
 * EnterpriseEventsResource
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class EnterpriseEventsResource {
  @SerializedName("_href")
  private String _href = null;

  @SerializedName("eventTime")
  private OffsetDateTime eventTime = null;

  @SerializedName("event")
  private String event = null;

  /**
   * Gets or Sets category
   */
  @JsonAdapter(CategoryEnum.Adapter.class)
  public enum CategoryEnum {
    SYSTEM("SYSTEM"),
    NETWORK("NETWORK"),
    APPLICATION("APPLICATION"),
    USER("USER"),
    SECURITY("SECURITY"),
    EDGE("EDGE"),
    GATEWAY("GATEWAY"),
    CONFIGURATION("CONFIGURATION"),
    ENTERPRISE("ENTERPRISE");

    private String value;

    CategoryEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static CategoryEnum fromValue(String text) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<CategoryEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final CategoryEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public CategoryEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return CategoryEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("category")
  private CategoryEnum category = null;

  /**
   * Gets or Sets severity
   */
  @JsonAdapter(SeverityEnum.Adapter.class)
  public enum SeverityEnum {
    EMERGENCY("EMERGENCY"),
    ALERT("ALERT"),
    CRITICAL("CRITICAL"),
    ERROR("ERROR"),
    WARNING("WARNING"),
    NOTICE("NOTICE"),
    INFO("INFO"),
    DEBUG("DEBUG");

    private String value;

    SeverityEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static SeverityEnum fromValue(String text) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<SeverityEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final SeverityEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public SeverityEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return SeverityEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("severity")
  private SeverityEnum severity = null;

  @SerializedName("message")
  private String message = null;

  @SerializedName("detail")
  private String detail = null;

  @SerializedName("edge")
  private BaseApiResource2 edge = null;

  @SerializedName("link")
  private BaseApiResource3 link = null;

  @SerializedName("segment")
  private BaseApiResource segment = null;

  @SerializedName("service")
  private BaseApiResource service = null;

  @SerializedName("user")
  private BaseApiResource user = null;

  public EnterpriseEventsResource _href(String _href) {
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

  public EnterpriseEventsResource eventTime(OffsetDateTime eventTime) {
    this.eventTime = eventTime;
    return this;
  }

   /**
   * Get eventTime
   * @return eventTime
  **/
  @Schema(description = "")
  public OffsetDateTime getEventTime() {
    return eventTime;
  }

  public void setEventTime(OffsetDateTime eventTime) {
    this.eventTime = eventTime;
  }

  public EnterpriseEventsResource event(String event) {
    this.event = event;
    return this;
  }

   /**
   * Get event
   * @return event
  **/
  @Schema(description = "")
  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public EnterpriseEventsResource category(CategoryEnum category) {
    this.category = category;
    return this;
  }

   /**
   * Get category
   * @return category
  **/
  @Schema(description = "")
  public CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(CategoryEnum category) {
    this.category = category;
  }

  public EnterpriseEventsResource severity(SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

   /**
   * Get severity
   * @return severity
  **/
  @Schema(description = "")
  public SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(SeverityEnum severity) {
    this.severity = severity;
  }

  public EnterpriseEventsResource message(String message) {
    this.message = message;
    return this;
  }

   /**
   * Get message
   * @return message
  **/
  @Schema(description = "")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public EnterpriseEventsResource detail(String detail) {
    this.detail = detail;
    return this;
  }

   /**
   * Get detail
   * @return detail
  **/
  @Schema(description = "")
  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public EnterpriseEventsResource edge(BaseApiResource2 edge) {
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

  public EnterpriseEventsResource link(BaseApiResource3 link) {
    this.link = link;
    return this;
  }

   /**
   * Get link
   * @return link
  **/
  @Schema(description = "")
  public BaseApiResource3 getLink() {
    return link;
  }

  public void setLink(BaseApiResource3 link) {
    this.link = link;
  }

  public EnterpriseEventsResource segment(BaseApiResource segment) {
    this.segment = segment;
    return this;
  }

   /**
   * Get segment
   * @return segment
  **/
  @Schema(description = "")
  public BaseApiResource getSegment() {
    return segment;
  }

  public void setSegment(BaseApiResource segment) {
    this.segment = segment;
  }

  public EnterpriseEventsResource service(BaseApiResource service) {
    this.service = service;
    return this;
  }

   /**
   * Get service
   * @return service
  **/
  @Schema(description = "")
  public BaseApiResource getService() {
    return service;
  }

  public void setService(BaseApiResource service) {
    this.service = service;
  }

  public EnterpriseEventsResource user(BaseApiResource user) {
    this.user = user;
    return this;
  }

   /**
   * Get user
   * @return user
  **/
  @Schema(description = "")
  public BaseApiResource getUser() {
    return user;
  }

  public void setUser(BaseApiResource user) {
    this.user = user;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnterpriseEventsResource enterpriseEventsResource = (EnterpriseEventsResource) o;
    return Objects.equals(this._href, enterpriseEventsResource._href) &&
        Objects.equals(this.eventTime, enterpriseEventsResource.eventTime) &&
        Objects.equals(this.event, enterpriseEventsResource.event) &&
        Objects.equals(this.category, enterpriseEventsResource.category) &&
        Objects.equals(this.severity, enterpriseEventsResource.severity) &&
        Objects.equals(this.message, enterpriseEventsResource.message) &&
        Objects.equals(this.detail, enterpriseEventsResource.detail) &&
        Objects.equals(this.edge, enterpriseEventsResource.edge) &&
        Objects.equals(this.link, enterpriseEventsResource.link) &&
        Objects.equals(this.segment, enterpriseEventsResource.segment) &&
        Objects.equals(this.service, enterpriseEventsResource.service) &&
        Objects.equals(this.user, enterpriseEventsResource.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_href, eventTime, event, category, severity, message, detail, edge, link, segment, service, user);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnterpriseEventsResource {\n");
    
    sb.append("    _href: ").append(toIndentedString(_href)).append("\n");
    sb.append("    eventTime: ").append(toIndentedString(eventTime)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
    sb.append("    edge: ").append(toIndentedString(edge)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    segment: ").append(toIndentedString(segment)).append("\n");
    sb.append("    service: ").append(toIndentedString(service)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
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
