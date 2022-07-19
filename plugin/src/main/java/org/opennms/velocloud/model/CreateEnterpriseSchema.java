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
import org.opennms.velocloud.model.CreateEnterpriseSchemaServiceLicenses;
import org.opennms.velocloud.model.CreateEnterpriseSchemaUser;
/**
 * CreateEnterpriseSchema
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-19T15:40:29.419486-04:00[America/Toronto]")
public class CreateEnterpriseSchema {
  @SerializedName("gatewayPool")
  private String gatewayPool = null;

  @SerializedName("operatorProfiles")
  private List<String> operatorProfiles = new ArrayList<>();

  @SerializedName("name")
  private String name = null;

  @SerializedName("accountNumber")
  private String accountNumber = null;

  @SerializedName("domain")
  private String domain = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("licenses")
  private List<String> licenses = null;

  @SerializedName("enableExportRestriction")
  private Boolean enableExportRestriction = false;

  @SerializedName("serviceLicenses")
  private CreateEnterpriseSchemaServiceLicenses serviceLicenses = null;

  @SerializedName("enableEnterpriseDelegationToOperator")
  private Boolean enableEnterpriseDelegationToOperator = true;

  @SerializedName("enableEnterpriseDelegationToProxy")
  private Boolean enableEnterpriseDelegationToProxy = false;

  @SerializedName("enableEnterpriseUserManagementDelegationToOperator")
  private Boolean enableEnterpriseUserManagementDelegationToOperator = false;

  @SerializedName("delegateEdgeImageManagementToEnterprise")
  private Boolean delegateEdgeImageManagementToEnterprise = false;

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
  private EndpointPkiModeEnum endpointPkiMode = EndpointPkiModeEnum.OPTIONAL;

  @SerializedName("user")
  private CreateEnterpriseSchemaUser user = null;

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

  public CreateEnterpriseSchema gatewayPool(String gatewayPool) {
    this.gatewayPool = gatewayPool;
    return this;
  }

   /**
   * A globally-unique UUIDv4-formatted identifer
   * @return gatewayPool
  **/
  @Schema(required = true, description = "A globally-unique UUIDv4-formatted identifer")
  public String getGatewayPool() {
    return gatewayPool;
  }

  public void setGatewayPool(String gatewayPool) {
    this.gatewayPool = gatewayPool;
  }

  public CreateEnterpriseSchema operatorProfiles(List<String> operatorProfiles) {
    this.operatorProfiles = operatorProfiles;
    return this;
  }

  public CreateEnterpriseSchema addOperatorProfilesItem(String operatorProfilesItem) {
    this.operatorProfiles.add(operatorProfilesItem);
    return this;
  }

   /**
   * Array of globally-unique UUIDv4-formatted identifers representing operator Profile logicalIds.  First item in the array is the default operator profile (inherited by all Edges by default)
   * @return operatorProfiles
  **/
  @Schema(required = true, description = "Array of globally-unique UUIDv4-formatted identifers representing operator Profile logicalIds.  First item in the array is the default operator profile (inherited by all Edges by default)")
  public List<String> getOperatorProfiles() {
    return operatorProfiles;
  }

  public void setOperatorProfiles(List<String> operatorProfiles) {
    this.operatorProfiles = operatorProfiles;
  }

  public CreateEnterpriseSchema name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Enterprise unique name
   * @return name
  **/
  @Schema(required = true, description = "Enterprise unique name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CreateEnterpriseSchema accountNumber(String accountNumber) {
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

  public CreateEnterpriseSchema domain(String domain) {
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

  public CreateEnterpriseSchema description(String description) {
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

  public CreateEnterpriseSchema licenses(List<String> licenses) {
    this.licenses = licenses;
    return this;
  }

  public CreateEnterpriseSchema addLicensesItem(String licensesItem) {
    if (this.licenses == null) {
      this.licenses = new ArrayList<>();
    }
    this.licenses.add(licensesItem);
    return this;
  }

   /**
   * Array of globally-unique UUIDv4-formatted identifers
   * @return licenses
  **/
  @Schema(description = "Array of globally-unique UUIDv4-formatted identifers")
  public List<String> getLicenses() {
    return licenses;
  }

  public void setLicenses(List<String> licenses) {
    this.licenses = licenses;
  }

  public CreateEnterpriseSchema enableExportRestriction(Boolean enableExportRestriction) {
    this.enableExportRestriction = enableExportRestriction;
    return this;
  }

   /**
   * Get enableExportRestriction
   * @return enableExportRestriction
  **/
  @Schema(description = "")
  public Boolean isEnableExportRestriction() {
    return enableExportRestriction;
  }

  public void setEnableExportRestriction(Boolean enableExportRestriction) {
    this.enableExportRestriction = enableExportRestriction;
  }

  public CreateEnterpriseSchema serviceLicenses(CreateEnterpriseSchemaServiceLicenses serviceLicenses) {
    this.serviceLicenses = serviceLicenses;
    return this;
  }

   /**
   * Get serviceLicenses
   * @return serviceLicenses
  **/
  @Schema(description = "")
  public CreateEnterpriseSchemaServiceLicenses getServiceLicenses() {
    return serviceLicenses;
  }

  public void setServiceLicenses(CreateEnterpriseSchemaServiceLicenses serviceLicenses) {
    this.serviceLicenses = serviceLicenses;
  }

  public CreateEnterpriseSchema enableEnterpriseDelegationToOperator(Boolean enableEnterpriseDelegationToOperator) {
    this.enableEnterpriseDelegationToOperator = enableEnterpriseDelegationToOperator;
    return this;
  }

   /**
   * Get enableEnterpriseDelegationToOperator
   * @return enableEnterpriseDelegationToOperator
  **/
  @Schema(description = "")
  public Boolean isEnableEnterpriseDelegationToOperator() {
    return enableEnterpriseDelegationToOperator;
  }

  public void setEnableEnterpriseDelegationToOperator(Boolean enableEnterpriseDelegationToOperator) {
    this.enableEnterpriseDelegationToOperator = enableEnterpriseDelegationToOperator;
  }

  public CreateEnterpriseSchema enableEnterpriseDelegationToProxy(Boolean enableEnterpriseDelegationToProxy) {
    this.enableEnterpriseDelegationToProxy = enableEnterpriseDelegationToProxy;
    return this;
  }

   /**
   * Get enableEnterpriseDelegationToProxy
   * @return enableEnterpriseDelegationToProxy
  **/
  @Schema(description = "")
  public Boolean isEnableEnterpriseDelegationToProxy() {
    return enableEnterpriseDelegationToProxy;
  }

  public void setEnableEnterpriseDelegationToProxy(Boolean enableEnterpriseDelegationToProxy) {
    this.enableEnterpriseDelegationToProxy = enableEnterpriseDelegationToProxy;
  }

  public CreateEnterpriseSchema enableEnterpriseUserManagementDelegationToOperator(Boolean enableEnterpriseUserManagementDelegationToOperator) {
    this.enableEnterpriseUserManagementDelegationToOperator = enableEnterpriseUserManagementDelegationToOperator;
    return this;
  }

   /**
   * Get enableEnterpriseUserManagementDelegationToOperator
   * @return enableEnterpriseUserManagementDelegationToOperator
  **/
  @Schema(description = "")
  public Boolean isEnableEnterpriseUserManagementDelegationToOperator() {
    return enableEnterpriseUserManagementDelegationToOperator;
  }

  public void setEnableEnterpriseUserManagementDelegationToOperator(Boolean enableEnterpriseUserManagementDelegationToOperator) {
    this.enableEnterpriseUserManagementDelegationToOperator = enableEnterpriseUserManagementDelegationToOperator;
  }

  public CreateEnterpriseSchema delegateEdgeImageManagementToEnterprise(Boolean delegateEdgeImageManagementToEnterprise) {
    this.delegateEdgeImageManagementToEnterprise = delegateEdgeImageManagementToEnterprise;
    return this;
  }

   /**
   * Get delegateEdgeImageManagementToEnterprise
   * @return delegateEdgeImageManagementToEnterprise
  **/
  @Schema(description = "")
  public Boolean isDelegateEdgeImageManagementToEnterprise() {
    return delegateEdgeImageManagementToEnterprise;
  }

  public void setDelegateEdgeImageManagementToEnterprise(Boolean delegateEdgeImageManagementToEnterprise) {
    this.delegateEdgeImageManagementToEnterprise = delegateEdgeImageManagementToEnterprise;
  }

  public CreateEnterpriseSchema endpointPkiMode(EndpointPkiModeEnum endpointPkiMode) {
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

  public CreateEnterpriseSchema user(CreateEnterpriseSchemaUser user) {
    this.user = user;
    return this;
  }

   /**
   * Get user
   * @return user
  **/
  @Schema(description = "")
  public CreateEnterpriseSchemaUser getUser() {
    return user;
  }

  public void setUser(CreateEnterpriseSchemaUser user) {
    this.user = user;
  }

  public CreateEnterpriseSchema contactName(String contactName) {
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

  public CreateEnterpriseSchema contactPhone(String contactPhone) {
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

  public CreateEnterpriseSchema contactMobile(String contactMobile) {
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

  public CreateEnterpriseSchema contactEmail(String contactEmail) {
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

  public CreateEnterpriseSchema streetAddress(String streetAddress) {
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

  public CreateEnterpriseSchema streetAddress2(String streetAddress2) {
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

  public CreateEnterpriseSchema city(String city) {
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

  public CreateEnterpriseSchema state(String state) {
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

  public CreateEnterpriseSchema postalCode(String postalCode) {
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

  public CreateEnterpriseSchema country(String country) {
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

  public CreateEnterpriseSchema lat(Float lat) {
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

  public CreateEnterpriseSchema lon(Float lon) {
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

  public CreateEnterpriseSchema timezone(String timezone) {
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

  public CreateEnterpriseSchema locale(String locale) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateEnterpriseSchema createEnterpriseSchema = (CreateEnterpriseSchema) o;
    return Objects.equals(this.gatewayPool, createEnterpriseSchema.gatewayPool) &&
        Objects.equals(this.operatorProfiles, createEnterpriseSchema.operatorProfiles) &&
        Objects.equals(this.name, createEnterpriseSchema.name) &&
        Objects.equals(this.accountNumber, createEnterpriseSchema.accountNumber) &&
        Objects.equals(this.domain, createEnterpriseSchema.domain) &&
        Objects.equals(this.description, createEnterpriseSchema.description) &&
        Objects.equals(this.licenses, createEnterpriseSchema.licenses) &&
        Objects.equals(this.enableExportRestriction, createEnterpriseSchema.enableExportRestriction) &&
        Objects.equals(this.serviceLicenses, createEnterpriseSchema.serviceLicenses) &&
        Objects.equals(this.enableEnterpriseDelegationToOperator, createEnterpriseSchema.enableEnterpriseDelegationToOperator) &&
        Objects.equals(this.enableEnterpriseDelegationToProxy, createEnterpriseSchema.enableEnterpriseDelegationToProxy) &&
        Objects.equals(this.enableEnterpriseUserManagementDelegationToOperator, createEnterpriseSchema.enableEnterpriseUserManagementDelegationToOperator) &&
        Objects.equals(this.delegateEdgeImageManagementToEnterprise, createEnterpriseSchema.delegateEdgeImageManagementToEnterprise) &&
        Objects.equals(this.endpointPkiMode, createEnterpriseSchema.endpointPkiMode) &&
        Objects.equals(this.user, createEnterpriseSchema.user) &&
        Objects.equals(this.contactName, createEnterpriseSchema.contactName) &&
        Objects.equals(this.contactPhone, createEnterpriseSchema.contactPhone) &&
        Objects.equals(this.contactMobile, createEnterpriseSchema.contactMobile) &&
        Objects.equals(this.contactEmail, createEnterpriseSchema.contactEmail) &&
        Objects.equals(this.streetAddress, createEnterpriseSchema.streetAddress) &&
        Objects.equals(this.streetAddress2, createEnterpriseSchema.streetAddress2) &&
        Objects.equals(this.city, createEnterpriseSchema.city) &&
        Objects.equals(this.state, createEnterpriseSchema.state) &&
        Objects.equals(this.postalCode, createEnterpriseSchema.postalCode) &&
        Objects.equals(this.country, createEnterpriseSchema.country) &&
        Objects.equals(this.lat, createEnterpriseSchema.lat) &&
        Objects.equals(this.lon, createEnterpriseSchema.lon) &&
        Objects.equals(this.timezone, createEnterpriseSchema.timezone) &&
        Objects.equals(this.locale, createEnterpriseSchema.locale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gatewayPool, operatorProfiles, name, accountNumber, domain, description, licenses, enableExportRestriction, serviceLicenses, enableEnterpriseDelegationToOperator, enableEnterpriseDelegationToProxy, enableEnterpriseUserManagementDelegationToOperator, delegateEdgeImageManagementToEnterprise, endpointPkiMode, user, contactName, contactPhone, contactMobile, contactEmail, streetAddress, streetAddress2, city, state, postalCode, country, lat, lon, timezone, locale);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateEnterpriseSchema {\n");
    
    sb.append("    gatewayPool: ").append(toIndentedString(gatewayPool)).append("\n");
    sb.append("    operatorProfiles: ").append(toIndentedString(operatorProfiles)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    licenses: ").append(toIndentedString(licenses)).append("\n");
    sb.append("    enableExportRestriction: ").append(toIndentedString(enableExportRestriction)).append("\n");
    sb.append("    serviceLicenses: ").append(toIndentedString(serviceLicenses)).append("\n");
    sb.append("    enableEnterpriseDelegationToOperator: ").append(toIndentedString(enableEnterpriseDelegationToOperator)).append("\n");
    sb.append("    enableEnterpriseDelegationToProxy: ").append(toIndentedString(enableEnterpriseDelegationToProxy)).append("\n");
    sb.append("    enableEnterpriseUserManagementDelegationToOperator: ").append(toIndentedString(enableEnterpriseUserManagementDelegationToOperator)).append("\n");
    sb.append("    delegateEdgeImageManagementToEnterprise: ").append(toIndentedString(delegateEdgeImageManagementToEnterprise)).append("\n");
    sb.append("    endpointPkiMode: ").append(toIndentedString(endpointPkiMode)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
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
