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
/**
 * SiteSchema
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class SiteSchema {
  @SerializedName("name")
  private String name = null;

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

  @SerializedName("country")
  private String country = null;

  @SerializedName("postalCode")
  private String postalCode = null;

  @SerializedName("lat")
  private Float lat = null;

  @SerializedName("lon")
  private Float lon = null;

  @SerializedName("timezone")
  private String timezone = null;

  @SerializedName("locale")
  private String locale = null;

  @SerializedName("shippingSameAsLocation")
  private Boolean shippingSameAsLocation = null;

  @SerializedName("shippingContactName")
  private String shippingContactName = null;

  @SerializedName("shippingAddress")
  private String shippingAddress = null;

  @SerializedName("shippingAddress2")
  private String shippingAddress2 = null;

  @SerializedName("shippingCity")
  private String shippingCity = null;

  @SerializedName("shippingState")
  private String shippingState = null;

  @SerializedName("shippingCountry")
  private String shippingCountry = null;

  @SerializedName("shippingPostalCode")
  private String shippingPostalCode = null;

  public SiteSchema name(String name) {
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

  public SiteSchema contactName(String contactName) {
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

  public SiteSchema contactPhone(String contactPhone) {
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

  public SiteSchema contactMobile(String contactMobile) {
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

  public SiteSchema contactEmail(String contactEmail) {
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

  public SiteSchema streetAddress(String streetAddress) {
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

  public SiteSchema streetAddress2(String streetAddress2) {
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

  public SiteSchema city(String city) {
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

  public SiteSchema state(String state) {
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

  public SiteSchema country(String country) {
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

  public SiteSchema postalCode(String postalCode) {
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

  public SiteSchema lat(Float lat) {
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

  public SiteSchema lon(Float lon) {
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

  public SiteSchema timezone(String timezone) {
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

  public SiteSchema locale(String locale) {
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

  public SiteSchema shippingSameAsLocation(Boolean shippingSameAsLocation) {
    this.shippingSameAsLocation = shippingSameAsLocation;
    return this;
  }

   /**
   * Get shippingSameAsLocation
   * @return shippingSameAsLocation
  **/
  @Schema(description = "")
  public Boolean isShippingSameAsLocation() {
    return shippingSameAsLocation;
  }

  public void setShippingSameAsLocation(Boolean shippingSameAsLocation) {
    this.shippingSameAsLocation = shippingSameAsLocation;
  }

  public SiteSchema shippingContactName(String shippingContactName) {
    this.shippingContactName = shippingContactName;
    return this;
  }

   /**
   * Get shippingContactName
   * @return shippingContactName
  **/
  @Schema(description = "")
  public String getShippingContactName() {
    return shippingContactName;
  }

  public void setShippingContactName(String shippingContactName) {
    this.shippingContactName = shippingContactName;
  }

  public SiteSchema shippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
    return this;
  }

   /**
   * Get shippingAddress
   * @return shippingAddress
  **/
  @Schema(description = "")
  public String getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public SiteSchema shippingAddress2(String shippingAddress2) {
    this.shippingAddress2 = shippingAddress2;
    return this;
  }

   /**
   * Get shippingAddress2
   * @return shippingAddress2
  **/
  @Schema(description = "")
  public String getShippingAddress2() {
    return shippingAddress2;
  }

  public void setShippingAddress2(String shippingAddress2) {
    this.shippingAddress2 = shippingAddress2;
  }

  public SiteSchema shippingCity(String shippingCity) {
    this.shippingCity = shippingCity;
    return this;
  }

   /**
   * Get shippingCity
   * @return shippingCity
  **/
  @Schema(description = "")
  public String getShippingCity() {
    return shippingCity;
  }

  public void setShippingCity(String shippingCity) {
    this.shippingCity = shippingCity;
  }

  public SiteSchema shippingState(String shippingState) {
    this.shippingState = shippingState;
    return this;
  }

   /**
   * Get shippingState
   * @return shippingState
  **/
  @Schema(description = "")
  public String getShippingState() {
    return shippingState;
  }

  public void setShippingState(String shippingState) {
    this.shippingState = shippingState;
  }

  public SiteSchema shippingCountry(String shippingCountry) {
    this.shippingCountry = shippingCountry;
    return this;
  }

   /**
   * Get shippingCountry
   * @return shippingCountry
  **/
  @Schema(description = "")
  public String getShippingCountry() {
    return shippingCountry;
  }

  public void setShippingCountry(String shippingCountry) {
    this.shippingCountry = shippingCountry;
  }

  public SiteSchema shippingPostalCode(String shippingPostalCode) {
    this.shippingPostalCode = shippingPostalCode;
    return this;
  }

   /**
   * Get shippingPostalCode
   * @return shippingPostalCode
  **/
  @Schema(description = "")
  public String getShippingPostalCode() {
    return shippingPostalCode;
  }

  public void setShippingPostalCode(String shippingPostalCode) {
    this.shippingPostalCode = shippingPostalCode;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SiteSchema siteSchema = (SiteSchema) o;
    return Objects.equals(this.name, siteSchema.name) &&
        Objects.equals(this.contactName, siteSchema.contactName) &&
        Objects.equals(this.contactPhone, siteSchema.contactPhone) &&
        Objects.equals(this.contactMobile, siteSchema.contactMobile) &&
        Objects.equals(this.contactEmail, siteSchema.contactEmail) &&
        Objects.equals(this.streetAddress, siteSchema.streetAddress) &&
        Objects.equals(this.streetAddress2, siteSchema.streetAddress2) &&
        Objects.equals(this.city, siteSchema.city) &&
        Objects.equals(this.state, siteSchema.state) &&
        Objects.equals(this.country, siteSchema.country) &&
        Objects.equals(this.postalCode, siteSchema.postalCode) &&
        Objects.equals(this.lat, siteSchema.lat) &&
        Objects.equals(this.lon, siteSchema.lon) &&
        Objects.equals(this.timezone, siteSchema.timezone) &&
        Objects.equals(this.locale, siteSchema.locale) &&
        Objects.equals(this.shippingSameAsLocation, siteSchema.shippingSameAsLocation) &&
        Objects.equals(this.shippingContactName, siteSchema.shippingContactName) &&
        Objects.equals(this.shippingAddress, siteSchema.shippingAddress) &&
        Objects.equals(this.shippingAddress2, siteSchema.shippingAddress2) &&
        Objects.equals(this.shippingCity, siteSchema.shippingCity) &&
        Objects.equals(this.shippingState, siteSchema.shippingState) &&
        Objects.equals(this.shippingCountry, siteSchema.shippingCountry) &&
        Objects.equals(this.shippingPostalCode, siteSchema.shippingPostalCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, contactName, contactPhone, contactMobile, contactEmail, streetAddress, streetAddress2, city, state, country, postalCode, lat, lon, timezone, locale, shippingSameAsLocation, shippingContactName, shippingAddress, shippingAddress2, shippingCity, shippingState, shippingCountry, shippingPostalCode);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SiteSchema {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    contactName: ").append(toIndentedString(contactName)).append("\n");
    sb.append("    contactPhone: ").append(toIndentedString(contactPhone)).append("\n");
    sb.append("    contactMobile: ").append(toIndentedString(contactMobile)).append("\n");
    sb.append("    contactEmail: ").append(toIndentedString(contactEmail)).append("\n");
    sb.append("    streetAddress: ").append(toIndentedString(streetAddress)).append("\n");
    sb.append("    streetAddress2: ").append(toIndentedString(streetAddress2)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
    sb.append("    lat: ").append(toIndentedString(lat)).append("\n");
    sb.append("    lon: ").append(toIndentedString(lon)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    shippingSameAsLocation: ").append(toIndentedString(shippingSameAsLocation)).append("\n");
    sb.append("    shippingContactName: ").append(toIndentedString(shippingContactName)).append("\n");
    sb.append("    shippingAddress: ").append(toIndentedString(shippingAddress)).append("\n");
    sb.append("    shippingAddress2: ").append(toIndentedString(shippingAddress2)).append("\n");
    sb.append("    shippingCity: ").append(toIndentedString(shippingCity)).append("\n");
    sb.append("    shippingState: ").append(toIndentedString(shippingState)).append("\n");
    sb.append("    shippingCountry: ").append(toIndentedString(shippingCountry)).append("\n");
    sb.append("    shippingPostalCode: ").append(toIndentedString(shippingPostalCode)).append("\n");
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
