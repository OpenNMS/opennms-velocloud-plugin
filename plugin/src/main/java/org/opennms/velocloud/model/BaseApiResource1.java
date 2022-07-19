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
/**
 * BaseApiResource1
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class BaseApiResource1 {
  @SerializedName("_href")
  private String _href = null;

  @SerializedName("created")
  private OffsetDateTime created = null;

  @SerializedName("alertsEnabled")
  private Boolean alertsEnabled = null;

  @SerializedName("operatorAlertsEnabled")
  private Boolean operatorAlertsEnabled = null;

  /**
   * Gets or Sets endpointPkiMode
   */
  @JsonAdapter(EndpointPkiModeEnum.Adapter.class)
  public enum EndpointPkiModeEnum {
    DISABLED("CERTIFICATE_DISABLED"),
    OPTIONAL("CERTIFICATE_OPTIONAL"),
    REQUIRED("CERTIFICATE_REQUIRED");

    private String value;

    EndpointPkiModeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static EndpointPkiModeEnum fromValue(String text) {
      for (EndpointPkiModeEnum b : EndpointPkiModeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<EndpointPkiModeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final EndpointPkiModeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public EndpointPkiModeEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return EndpointPkiModeEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("endpointPkiMode")
  private EndpointPkiModeEnum endpointPkiMode = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("domain")
  private String domain = null;

  @SerializedName("prefix")
  private String prefix = null;

  @SerializedName("logicalId")
  private String logicalId = null;

  @SerializedName("accountNumber")
  private String accountNumber = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("contactName")
  private String contactName = null;

  @SerializedName("contactPhone")
  private String contactPhone = null;

  @SerializedName("contactMobile")
  private String contactMobile = null;

  @SerializedName("contactEmail")
  private String contactEmail = null;

  @SerializedName("streetAddress")
  private String streetAddress = null;

  @SerializedName("streetAddress2")
  private String streetAddress2 = null;

  @SerializedName("city")
  private String city = null;

  @SerializedName("state")
  private String state = null;

  @SerializedName("postalCode")
  private String postalCode = null;

  @SerializedName("country")
  private String country = null;

  @SerializedName("lat")
  private Float lat = null;

  @SerializedName("lon")
  private Float lon = null;

  @SerializedName("timezone")
  private String timezone = null;

  @SerializedName("locale")
  private String locale = null;

  @SerializedName("modified")
  private OffsetDateTime modified = null;

  @SerializedName("gatewayPool")
  private BaseApiResource gatewayPool = null;

  /**
   * Gets or Sets bastionState
   */
  @JsonAdapter(BastionStateEnum.Adapter.class)
  public enum BastionStateEnum {
    UNCONFIGURED("UNCONFIGURED"),
    STAGE_REQUESTED("STAGE_REQUESTED"),
    UNSTAGE_REQUESTED("UNSTAGE_REQUESTED"),
    STAGED("STAGED"),
    UNSTAGED("UNSTAGED");

    private String value;

    BastionStateEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static BastionStateEnum fromValue(String text) {
      for (BastionStateEnum b : BastionStateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<BastionStateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final BastionStateEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public BastionStateEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return BastionStateEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("bastionState")
  private BastionStateEnum bastionState = null;

  public BaseApiResource1 _href(String _href) {
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

  public BaseApiResource1 created(OffsetDateTime created) {
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

  public BaseApiResource1 alertsEnabled(Boolean alertsEnabled) {
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

  public BaseApiResource1 operatorAlertsEnabled(Boolean operatorAlertsEnabled) {
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

  public BaseApiResource1 endpointPkiMode(EndpointPkiModeEnum endpointPkiMode) {
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

  public BaseApiResource1 name(String name) {
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

  public BaseApiResource1 domain(String domain) {
    this.domain = domain;
    return this;
  }

   /**
   * Get domain
   * @return domain
  **/
  @Schema(description = "")
  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public BaseApiResource1 prefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

   /**
   * Get prefix
   * @return prefix
  **/
  @Schema(description = "")
  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public BaseApiResource1 logicalId(String logicalId) {
    this.logicalId = logicalId;
    return this;
  }

   /**
   * A globally-unique UUIDv4-formatted identifer
   * @return logicalId
  **/
  @Schema(description = "A globally-unique UUIDv4-formatted identifer")
  public String getLogicalId() {
    return logicalId;
  }

  public void setLogicalId(String logicalId) {
    this.logicalId = logicalId;
  }

  public BaseApiResource1 accountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
    return this;
  }

   /**
   * Get accountNumber
   * @return accountNumber
  **/
  @Schema(description = "")
  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public BaseApiResource1 description(String description) {
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

  public BaseApiResource1 contactName(String contactName) {
    this.contactName = contactName;
    return this;
  }

   /**
   * Get contactName
   * @return contactName
  **/
  @Schema(description = "")
  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public BaseApiResource1 contactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
    return this;
  }

   /**
   * Get contactPhone
   * @return contactPhone
  **/
  @Schema(description = "")
  public String getContactPhone() {
    return contactPhone;
  }

  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
  }

  public BaseApiResource1 contactMobile(String contactMobile) {
    this.contactMobile = contactMobile;
    return this;
  }

   /**
   * Get contactMobile
   * @return contactMobile
  **/
  @Schema(description = "")
  public String getContactMobile() {
    return contactMobile;
  }

  public void setContactMobile(String contactMobile) {
    this.contactMobile = contactMobile;
  }

  public BaseApiResource1 contactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
    return this;
  }

   /**
   * Get contactEmail
   * @return contactEmail
  **/
  @Schema(description = "")
  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public BaseApiResource1 streetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
    return this;
  }

   /**
   * Get streetAddress
   * @return streetAddress
  **/
  @Schema(description = "")
  public String getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  public BaseApiResource1 streetAddress2(String streetAddress2) {
    this.streetAddress2 = streetAddress2;
    return this;
  }

   /**
   * Get streetAddress2
   * @return streetAddress2
  **/
  @Schema(description = "")
  public String getStreetAddress2() {
    return streetAddress2;
  }

  public void setStreetAddress2(String streetAddress2) {
    this.streetAddress2 = streetAddress2;
  }

  public BaseApiResource1 city(String city) {
    this.city = city;
    return this;
  }

   /**
   * Get city
   * @return city
  **/
  @Schema(description = "")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public BaseApiResource1 state(String state) {
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @Schema(description = "")
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public BaseApiResource1 postalCode(String postalCode) {
    this.postalCode = postalCode;
    return this;
  }

   /**
   * Get postalCode
   * @return postalCode
  **/
  @Schema(description = "")
  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public BaseApiResource1 country(String country) {
    this.country = country;
    return this;
  }

   /**
   * Get country
   * @return country
  **/
  @Schema(description = "")
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public BaseApiResource1 lat(Float lat) {
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

  public BaseApiResource1 lon(Float lon) {
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

  public BaseApiResource1 timezone(String timezone) {
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

  public BaseApiResource1 locale(String locale) {
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

  public BaseApiResource1 modified(OffsetDateTime modified) {
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

  public BaseApiResource1 gatewayPool(BaseApiResource gatewayPool) {
    this.gatewayPool = gatewayPool;
    return this;
  }

   /**
   * Get gatewayPool
   * @return gatewayPool
  **/
  @Schema(description = "")
  public BaseApiResource getGatewayPool() {
    return gatewayPool;
  }

  public void setGatewayPool(BaseApiResource gatewayPool) {
    this.gatewayPool = gatewayPool;
  }

  public BaseApiResource1 bastionState(BastionStateEnum bastionState) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseApiResource1 baseApiResource1 = (BaseApiResource1) o;
    return Objects.equals(this._href, baseApiResource1._href) &&
        Objects.equals(this.created, baseApiResource1.created) &&
        Objects.equals(this.alertsEnabled, baseApiResource1.alertsEnabled) &&
        Objects.equals(this.operatorAlertsEnabled, baseApiResource1.operatorAlertsEnabled) &&
        Objects.equals(this.endpointPkiMode, baseApiResource1.endpointPkiMode) &&
        Objects.equals(this.name, baseApiResource1.name) &&
        Objects.equals(this.domain, baseApiResource1.domain) &&
        Objects.equals(this.prefix, baseApiResource1.prefix) &&
        Objects.equals(this.logicalId, baseApiResource1.logicalId) &&
        Objects.equals(this.accountNumber, baseApiResource1.accountNumber) &&
        Objects.equals(this.description, baseApiResource1.description) &&
        Objects.equals(this.contactName, baseApiResource1.contactName) &&
        Objects.equals(this.contactPhone, baseApiResource1.contactPhone) &&
        Objects.equals(this.contactMobile, baseApiResource1.contactMobile) &&
        Objects.equals(this.contactEmail, baseApiResource1.contactEmail) &&
        Objects.equals(this.streetAddress, baseApiResource1.streetAddress) &&
        Objects.equals(this.streetAddress2, baseApiResource1.streetAddress2) &&
        Objects.equals(this.city, baseApiResource1.city) &&
        Objects.equals(this.state, baseApiResource1.state) &&
        Objects.equals(this.postalCode, baseApiResource1.postalCode) &&
        Objects.equals(this.country, baseApiResource1.country) &&
        Objects.equals(this.lat, baseApiResource1.lat) &&
        Objects.equals(this.lon, baseApiResource1.lon) &&
        Objects.equals(this.timezone, baseApiResource1.timezone) &&
        Objects.equals(this.locale, baseApiResource1.locale) &&
        Objects.equals(this.modified, baseApiResource1.modified) &&
        Objects.equals(this.gatewayPool, baseApiResource1.gatewayPool) &&
        Objects.equals(this.bastionState, baseApiResource1.bastionState);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_href, created, alertsEnabled, operatorAlertsEnabled, endpointPkiMode, name, domain, prefix, logicalId, accountNumber, description, contactName, contactPhone, contactMobile, contactEmail, streetAddress, streetAddress2, city, state, postalCode, country, lat, lon, timezone, locale, modified, gatewayPool, bastionState);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BaseApiResource1 {\n");
    
    sb.append("    _href: ").append(toIndentedString(_href)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    alertsEnabled: ").append(toIndentedString(alertsEnabled)).append("\n");
    sb.append("    operatorAlertsEnabled: ").append(toIndentedString(operatorAlertsEnabled)).append("\n");
    sb.append("    endpointPkiMode: ").append(toIndentedString(endpointPkiMode)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    prefix: ").append(toIndentedString(prefix)).append("\n");
    sb.append("    logicalId: ").append(toIndentedString(logicalId)).append("\n");
    sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    contactName: ").append(toIndentedString(contactName)).append("\n");
    sb.append("    contactPhone: ").append(toIndentedString(contactPhone)).append("\n");
    sb.append("    contactMobile: ").append(toIndentedString(contactMobile)).append("\n");
    sb.append("    contactEmail: ").append(toIndentedString(contactEmail)).append("\n");
    sb.append("    streetAddress: ").append(toIndentedString(streetAddress)).append("\n");
    sb.append("    streetAddress2: ").append(toIndentedString(streetAddress2)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    lat: ").append(toIndentedString(lat)).append("\n");
    sb.append("    lon: ").append(toIndentedString(lon)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
    sb.append("    gatewayPool: ").append(toIndentedString(gatewayPool)).append("\n");
    sb.append("    bastionState: ").append(toIndentedString(bastionState)).append("\n");
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
