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
import org.opennms.velocloud.client.model.BaseApiResource2;
import org.opennms.velocloud.client.model.BaseApiResource3;
import org.opennms.velocloud.client.model.EnterpriseAlertNotification;
/**
 * EnterpriseAlertsResource
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")
public class EnterpriseAlertsResource {
  @JsonProperty("_href")
  private String _href = null;

  @JsonProperty("created")
  private OffsetDateTime created = null;

  @JsonProperty("triggerTime")
  private OffsetDateTime triggerTime = null;

  @JsonProperty("edge")
  private BaseApiResource2 edge = null;

  @JsonProperty("link")
  private BaseApiResource3 link = null;

  /**
   * Gets or Sets name
   */
  public enum NameEnum {
    EDGE_DOWN("EDGE_DOWN"),
    EDGE_UP("EDGE_UP"),
    LINK_DOWN("LINK_DOWN"),
    LINK_UP("LINK_UP"),
    VPN_TUNNEL_DOWN("VPN_TUNNEL_DOWN"),
    EDGE_HA_FAILOVER("EDGE_HA_FAILOVER"),
    EDGE_SERVICE_DOWN("EDGE_SERVICE_DOWN"),
    GATEWAY_SERVICE_DOWN("GATEWAY_SERVICE_DOWN"),
    VNF_VM_EVENT("VNF_VM_EVENT"),
    VNF_VM_DEPLOYED("VNF_VM_DEPLOYED"),
    VNF_VM_POWERED_ON("VNF_VM_POWERED_ON"),
    VNF_VM_POWERED_OFF("VNF_VM_POWERED_OFF"),
    VNF_VM_DEPLOYED_AND_POWERED_OFF("VNF_VM_DEPLOYED_AND_POWERED_OFF"),
    VNF_VM_DELETED("VNF_VM_DELETED"),
    VNF_VM_ERROR("VNF_VM_ERROR"),
    VNF_INSERTION_EVENT("VNF_INSERTION_EVENT"),
    VNF_INSERTION_ENABLED("VNF_INSERTION_ENABLED"),
    VNF_INSERTION_DISABLED("VNF_INSERTION_DISABLED"),
    TEST_ALERT("TEST_ALERT"),
    EDGE_CSS_TUNNEL_DOWN("EDGE_CSS_TUNNEL_DOWN"),
    EDGE_CSS_TUNNEL_UP("EDGE_CSS_TUNNEL_UP"),
    NVS_FROM_EDGE_TUNNEL_DOWN("NVS_FROM_EDGE_TUNNEL_DOWN"),
    NVS_FROM_EDGE_TUNNEL_UP("NVS_FROM_EDGE_TUNNEL_UP"),
    VNF_IMAGE_DOWNLOAD_EVENT("VNF_IMAGE_DOWNLOAD_EVENT"),
    VNF_IMAGE_DOWNLOAD_IN_PROGRESS("VNF_IMAGE_DOWNLOAD_IN_PROGRESS"),
    VNF_IMAGE_DOWNLOAD_COMPLETED("VNF_IMAGE_DOWNLOAD_COMPLETED"),
    VNF_IMAGE_DOWNLOAD_FAILED("VNF_IMAGE_DOWNLOAD_FAILED");

    private String value;

    NameEnum(String value) {
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
    public static NameEnum fromValue(String text) {
      for (NameEnum b : NameEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("name")
  private NameEnum name = null;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    EDGE_DOWN("EDGE_DOWN"),
    EDGE_UP("EDGE_UP"),
    LINK_DOWN("LINK_DOWN"),
    LINK_UP("LINK_UP"),
    VPN_TUNNEL_DOWN("VPN_TUNNEL_DOWN"),
    EDGE_HA_FAILOVER("EDGE_HA_FAILOVER"),
    EDGE_SERVICE_DOWN("EDGE_SERVICE_DOWN"),
    GATEWAY_SERVICE_DOWN("GATEWAY_SERVICE_DOWN"),
    VNF_VM_EVENT("VNF_VM_EVENT"),
    VNF_VM_DEPLOYED("VNF_VM_DEPLOYED"),
    VNF_VM_POWERED_ON("VNF_VM_POWERED_ON"),
    VNF_VM_POWERED_OFF("VNF_VM_POWERED_OFF"),
    VNF_VM_DEPLOYED_AND_POWERED_OFF("VNF_VM_DEPLOYED_AND_POWERED_OFF"),
    VNF_VM_DELETED("VNF_VM_DELETED"),
    VNF_VM_ERROR("VNF_VM_ERROR"),
    VNF_INSERTION_EVENT("VNF_INSERTION_EVENT"),
    VNF_INSERTION_ENABLED("VNF_INSERTION_ENABLED"),
    VNF_INSERTION_DISABLED("VNF_INSERTION_DISABLED"),
    TEST_ALERT("TEST_ALERT"),
    EDGE_CSS_TUNNEL_DOWN("EDGE_CSS_TUNNEL_DOWN"),
    EDGE_CSS_TUNNEL_UP("EDGE_CSS_TUNNEL_UP"),
    NVS_FROM_EDGE_TUNNEL_DOWN("NVS_FROM_EDGE_TUNNEL_DOWN"),
    NVS_FROM_EDGE_TUNNEL_UP("NVS_FROM_EDGE_TUNNEL_UP"),
    VNF_IMAGE_DOWNLOAD_EVENT("VNF_IMAGE_DOWNLOAD_EVENT"),
    VNF_IMAGE_DOWNLOAD_IN_PROGRESS("VNF_IMAGE_DOWNLOAD_IN_PROGRESS"),
    VNF_IMAGE_DOWNLOAD_COMPLETED("VNF_IMAGE_DOWNLOAD_COMPLETED"),
    VNF_IMAGE_DOWNLOAD_FAILED("VNF_IMAGE_DOWNLOAD_FAILED");

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
   * Gets or Sets state
   */
  public enum StateEnum {
    PENDING("PENDING"),
    ACTIVE("ACTIVE"),
    CLOSED("CLOSED");

    private String value;

    StateEnum(String value) {
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
    public static StateEnum fromValue(String text) {
      for (StateEnum b : StateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("state")
  private StateEnum state = null;

  @JsonProperty("stateSetTime")
  private OffsetDateTime stateSetTime = null;

  @JsonProperty("lastContact")
  private OffsetDateTime lastContact = null;

  @JsonProperty("firstNotificationSeconds")
  private Integer firstNotificationSeconds = null;

  @JsonProperty("maxNotifications")
  private Integer maxNotifications = null;

  @JsonProperty("notificationIntervalSeconds")
  private Integer notificationIntervalSeconds = null;

  @JsonProperty("resetIntervalSeconds")
  private Integer resetIntervalSeconds = null;

  @JsonProperty("comment")
  private String comment = null;

  @JsonProperty("nextNotificationTime")
  private OffsetDateTime nextNotificationTime = null;

  @JsonProperty("remainingNotifications")
  private Integer remainingNotifications = null;

  @JsonProperty("timezone")
  private String timezone = null;

  @JsonProperty("locale")
  private String locale = null;

  @JsonProperty("modified")
  private OffsetDateTime modified = null;

  @JsonProperty("notifications")
  private List<EnterpriseAlertNotification> notifications = null;

  public EnterpriseAlertsResource _href(String _href) {
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

  public EnterpriseAlertsResource created(OffsetDateTime created) {
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

  public EnterpriseAlertsResource triggerTime(OffsetDateTime triggerTime) {
    this.triggerTime = triggerTime;
    return this;
  }

   /**
   * Get triggerTime
   * @return triggerTime
  **/
  @Schema(description = "")
  public OffsetDateTime getTriggerTime() {
    return triggerTime;
  }

  public void setTriggerTime(OffsetDateTime triggerTime) {
    this.triggerTime = triggerTime;
  }

  public EnterpriseAlertsResource edge(BaseApiResource2 edge) {
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

  public EnterpriseAlertsResource link(BaseApiResource3 link) {
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

  public EnterpriseAlertsResource name(NameEnum name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @Schema(description = "")
  public NameEnum getName() {
    return name;
  }

  public void setName(NameEnum name) {
    this.name = name;
  }

  public EnterpriseAlertsResource type(TypeEnum type) {
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

  public EnterpriseAlertsResource state(StateEnum state) {
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

  public EnterpriseAlertsResource stateSetTime(OffsetDateTime stateSetTime) {
    this.stateSetTime = stateSetTime;
    return this;
  }

   /**
   * Get stateSetTime
   * @return stateSetTime
  **/
  @Schema(description = "")
  public OffsetDateTime getStateSetTime() {
    return stateSetTime;
  }

  public void setStateSetTime(OffsetDateTime stateSetTime) {
    this.stateSetTime = stateSetTime;
  }

  public EnterpriseAlertsResource lastContact(OffsetDateTime lastContact) {
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

  public EnterpriseAlertsResource firstNotificationSeconds(Integer firstNotificationSeconds) {
    this.firstNotificationSeconds = firstNotificationSeconds;
    return this;
  }

   /**
   * Get firstNotificationSeconds
   * @return firstNotificationSeconds
  **/
  @Schema(description = "")
  public Integer getFirstNotificationSeconds() {
    return firstNotificationSeconds;
  }

  public void setFirstNotificationSeconds(Integer firstNotificationSeconds) {
    this.firstNotificationSeconds = firstNotificationSeconds;
  }

  public EnterpriseAlertsResource maxNotifications(Integer maxNotifications) {
    this.maxNotifications = maxNotifications;
    return this;
  }

   /**
   * Get maxNotifications
   * @return maxNotifications
  **/
  @Schema(description = "")
  public Integer getMaxNotifications() {
    return maxNotifications;
  }

  public void setMaxNotifications(Integer maxNotifications) {
    this.maxNotifications = maxNotifications;
  }

  public EnterpriseAlertsResource notificationIntervalSeconds(Integer notificationIntervalSeconds) {
    this.notificationIntervalSeconds = notificationIntervalSeconds;
    return this;
  }

   /**
   * Get notificationIntervalSeconds
   * @return notificationIntervalSeconds
  **/
  @Schema(description = "")
  public Integer getNotificationIntervalSeconds() {
    return notificationIntervalSeconds;
  }

  public void setNotificationIntervalSeconds(Integer notificationIntervalSeconds) {
    this.notificationIntervalSeconds = notificationIntervalSeconds;
  }

  public EnterpriseAlertsResource resetIntervalSeconds(Integer resetIntervalSeconds) {
    this.resetIntervalSeconds = resetIntervalSeconds;
    return this;
  }

   /**
   * Get resetIntervalSeconds
   * @return resetIntervalSeconds
  **/
  @Schema(description = "")
  public Integer getResetIntervalSeconds() {
    return resetIntervalSeconds;
  }

  public void setResetIntervalSeconds(Integer resetIntervalSeconds) {
    this.resetIntervalSeconds = resetIntervalSeconds;
  }

  public EnterpriseAlertsResource comment(String comment) {
    this.comment = comment;
    return this;
  }

   /**
   * Get comment
   * @return comment
  **/
  @Schema(description = "")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public EnterpriseAlertsResource nextNotificationTime(OffsetDateTime nextNotificationTime) {
    this.nextNotificationTime = nextNotificationTime;
    return this;
  }

   /**
   * Get nextNotificationTime
   * @return nextNotificationTime
  **/
  @Schema(description = "")
  public OffsetDateTime getNextNotificationTime() {
    return nextNotificationTime;
  }

  public void setNextNotificationTime(OffsetDateTime nextNotificationTime) {
    this.nextNotificationTime = nextNotificationTime;
  }

  public EnterpriseAlertsResource remainingNotifications(Integer remainingNotifications) {
    this.remainingNotifications = remainingNotifications;
    return this;
  }

   /**
   * Get remainingNotifications
   * @return remainingNotifications
  **/
  @Schema(description = "")
  public Integer getRemainingNotifications() {
    return remainingNotifications;
  }

  public void setRemainingNotifications(Integer remainingNotifications) {
    this.remainingNotifications = remainingNotifications;
  }

  public EnterpriseAlertsResource timezone(String timezone) {
    this.timezone = timezone;
    return this;
  }

   /**
   * Get timezone
   * @return timezone
  **/
  @Schema(description = "")
  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public EnterpriseAlertsResource locale(String locale) {
    this.locale = locale;
    return this;
  }

   /**
   * Get locale
   * @return locale
  **/
  @Schema(description = "")
  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public EnterpriseAlertsResource modified(OffsetDateTime modified) {
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

  public EnterpriseAlertsResource notifications(List<EnterpriseAlertNotification> notifications) {
    this.notifications = notifications;
    return this;
  }

  public EnterpriseAlertsResource addNotificationsItem(EnterpriseAlertNotification notificationsItem) {
    if (this.notifications == null) {
      this.notifications = new ArrayList<>();
    }
    this.notifications.add(notificationsItem);
    return this;
  }

   /**
   * Get notifications
   * @return notifications
  **/
  @Schema(description = "")
  public List<EnterpriseAlertNotification> getNotifications() {
    return notifications;
  }

  public void setNotifications(List<EnterpriseAlertNotification> notifications) {
    this.notifications = notifications;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnterpriseAlertsResource enterpriseAlertsResource = (EnterpriseAlertsResource) o;
    return Objects.equals(this._href, enterpriseAlertsResource._href) &&
        Objects.equals(this.created, enterpriseAlertsResource.created) &&
        Objects.equals(this.triggerTime, enterpriseAlertsResource.triggerTime) &&
        Objects.equals(this.edge, enterpriseAlertsResource.edge) &&
        Objects.equals(this.link, enterpriseAlertsResource.link) &&
        Objects.equals(this.name, enterpriseAlertsResource.name) &&
        Objects.equals(this.type, enterpriseAlertsResource.type) &&
        Objects.equals(this.state, enterpriseAlertsResource.state) &&
        Objects.equals(this.stateSetTime, enterpriseAlertsResource.stateSetTime) &&
        Objects.equals(this.lastContact, enterpriseAlertsResource.lastContact) &&
        Objects.equals(this.firstNotificationSeconds, enterpriseAlertsResource.firstNotificationSeconds) &&
        Objects.equals(this.maxNotifications, enterpriseAlertsResource.maxNotifications) &&
        Objects.equals(this.notificationIntervalSeconds, enterpriseAlertsResource.notificationIntervalSeconds) &&
        Objects.equals(this.resetIntervalSeconds, enterpriseAlertsResource.resetIntervalSeconds) &&
        Objects.equals(this.comment, enterpriseAlertsResource.comment) &&
        Objects.equals(this.nextNotificationTime, enterpriseAlertsResource.nextNotificationTime) &&
        Objects.equals(this.remainingNotifications, enterpriseAlertsResource.remainingNotifications) &&
        Objects.equals(this.timezone, enterpriseAlertsResource.timezone) &&
        Objects.equals(this.locale, enterpriseAlertsResource.locale) &&
        Objects.equals(this.modified, enterpriseAlertsResource.modified) &&
        Objects.equals(this.notifications, enterpriseAlertsResource.notifications);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_href, created, triggerTime, edge, link, name, type, state, stateSetTime, lastContact, firstNotificationSeconds, maxNotifications, notificationIntervalSeconds, resetIntervalSeconds, comment, nextNotificationTime, remainingNotifications, timezone, locale, modified, notifications);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnterpriseAlertsResource {\n");
    
    sb.append("    _href: ").append(toIndentedString(_href)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    triggerTime: ").append(toIndentedString(triggerTime)).append("\n");
    sb.append("    edge: ").append(toIndentedString(edge)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    stateSetTime: ").append(toIndentedString(stateSetTime)).append("\n");
    sb.append("    lastContact: ").append(toIndentedString(lastContact)).append("\n");
    sb.append("    firstNotificationSeconds: ").append(toIndentedString(firstNotificationSeconds)).append("\n");
    sb.append("    maxNotifications: ").append(toIndentedString(maxNotifications)).append("\n");
    sb.append("    notificationIntervalSeconds: ").append(toIndentedString(notificationIntervalSeconds)).append("\n");
    sb.append("    resetIntervalSeconds: ").append(toIndentedString(resetIntervalSeconds)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    nextNotificationTime: ").append(toIndentedString(nextNotificationTime)).append("\n");
    sb.append("    remainingNotifications: ").append(toIndentedString(remainingNotifications)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
    sb.append("    notifications: ").append(toIndentedString(notifications)).append("\n");
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
