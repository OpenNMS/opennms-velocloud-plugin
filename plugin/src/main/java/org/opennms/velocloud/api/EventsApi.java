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

package org.opennms.velocloud.api;

import org.opennms.velocloud.handler.ApiCallback;
import org.opennms.velocloud.handler.ApiClient;
import org.opennms.velocloud.handler.ApiException;
import org.opennms.velocloud.handler.ApiResponse;
import org.opennms.velocloud.handler.Configuration;
import org.opennms.velocloud.handler.Pair;
import org.opennms.velocloud.handler.ProgressRequestBody;
import org.opennms.velocloud.handler.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import org.opennms.velocloud.model.EnterpriseEventsSchema;
import org.opennms.velocloud.model.InternalServerError;
import org.opennms.velocloud.model.RateLimitExceededError;
import org.opennms.velocloud.model.ResourceNotFoundError;
import org.opennms.velocloud.model.UnAuthorized;
import org.opennms.velocloud.model.ValidationError;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsApi {
    private ApiClient apiClient;

    public EventsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public EventsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getEnterpriseEvents
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param nextPageLink A nextPageLink value, as fetched via a past call to a list API method. When a nextPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param prevPageLink A prevPageLink value, as fetched via a past call to a list API method. When a prevPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param eventIs Filter by event[is] (optional)
     * @param eventIsNot Filter by event[isNot] (optional)
     * @param eventIn Filter by event[in] (optional)
     * @param eventNotIn Filter by event[notIn] (optional)
     * @param severityIs Filter by severity[is] (optional)
     * @param severityIsNot Filter by severity[isNot] (optional)
     * @param severityIn Filter by severity[in] (optional)
     * @param severityNotIn Filter by severity[notIn] (optional)
     * @param enterpriseUsernameIs Filter by enterpriseUsername[is] (optional)
     * @param enterpriseUsernameIsNot Filter by enterpriseUsername[isNot] (optional)
     * @param enterpriseUsernameIsNull Filter by enterpriseUsername[isNull] (optional)
     * @param enterpriseUsernameIsNotNull Filter by enterpriseUsername[isNotNull] (optional)
     * @param enterpriseUsernameStartsWith Filter by enterpriseUsername[startsWith] (optional)
     * @param enterpriseUsernameNotStartsWith Filter by enterpriseUsername[notStartsWith] (optional)
     * @param enterpriseUsernameEndsWith Filter by enterpriseUsername[endsWith] (optional)
     * @param enterpriseUsernameNotEndsWith Filter by enterpriseUsername[notEndsWith] (optional)
     * @param enterpriseUsernameContains Filter by enterpriseUsername[contains] (optional)
     * @param enterpriseUsernameNotContains Filter by enterpriseUsername[notContains] (optional)
     * @param enterpriseUsernameIn Filter by enterpriseUsername[in] (optional)
     * @param enterpriseUsernameNotIn Filter by enterpriseUsername[notIn] (optional)
     * @param detailIs Filter by detail[is] (optional)
     * @param detailIsNot Filter by detail[isNot] (optional)
     * @param detailIsNull Filter by detail[isNull] (optional)
     * @param detailIsNotNull Filter by detail[isNotNull] (optional)
     * @param detailStartsWith Filter by detail[startsWith] (optional)
     * @param detailNotStartsWith Filter by detail[notStartsWith] (optional)
     * @param detailEndsWith Filter by detail[endsWith] (optional)
     * @param detailNotEndsWith Filter by detail[notEndsWith] (optional)
     * @param detailContains Filter by detail[contains] (optional)
     * @param detailNotContains Filter by detail[notContains] (optional)
     * @param detailIn Filter by detail[in] (optional)
     * @param detailNotIn Filter by detail[notIn] (optional)
     * @param segmentNameIs Filter by segmentName[is] (optional)
     * @param segmentNameIsNot Filter by segmentName[isNot] (optional)
     * @param segmentNameIsNull Filter by segmentName[isNull] (optional)
     * @param segmentNameIsNotNull Filter by segmentName[isNotNull] (optional)
     * @param segmentNameStartsWith Filter by segmentName[startsWith] (optional)
     * @param segmentNameNotStartsWith Filter by segmentName[notStartsWith] (optional)
     * @param segmentNameEndsWith Filter by segmentName[endsWith] (optional)
     * @param segmentNameNotEndsWith Filter by segmentName[notEndsWith] (optional)
     * @param segmentNameContains Filter by segmentName[contains] (optional)
     * @param segmentNameNotContains Filter by segmentName[notContains] (optional)
     * @param segmentNameIn Filter by segmentName[in] (optional)
     * @param segmentNameNotIn Filter by segmentName[notIn] (optional)
     * @param messageIs Filter by message[is] (optional)
     * @param messageIsNot Filter by message[isNot] (optional)
     * @param messageIsNull Filter by message[isNull] (optional)
     * @param messageIsNotNull Filter by message[isNotNull] (optional)
     * @param messageStartsWith Filter by message[startsWith] (optional)
     * @param messageNotStartsWith Filter by message[notStartsWith] (optional)
     * @param messageEndsWith Filter by message[endsWith] (optional)
     * @param messageNotEndsWith Filter by message[notEndsWith] (optional)
     * @param messageContains Filter by message[contains] (optional)
     * @param messageNotContains Filter by message[notContains] (optional)
     * @param messageIn Filter by message[in] (optional)
     * @param messageNotIn Filter by message[notIn] (optional)
     * @param edgeNameIs Filter by edgeName[is] (optional)
     * @param edgeNameIsNot Filter by edgeName[isNot] (optional)
     * @param edgeNameIsNull Filter by edgeName[isNull] (optional)
     * @param edgeNameIsNotNull Filter by edgeName[isNotNull] (optional)
     * @param edgeNameStartsWith Filter by edgeName[startsWith] (optional)
     * @param edgeNameNotStartsWith Filter by edgeName[notStartsWith] (optional)
     * @param edgeNameEndsWith Filter by edgeName[endsWith] (optional)
     * @param edgeNameNotEndsWith Filter by edgeName[notEndsWith] (optional)
     * @param edgeNameContains Filter by edgeName[contains] (optional)
     * @param edgeNameNotContains Filter by edgeName[notContains] (optional)
     * @param edgeNameIn Filter by edgeName[in] (optional)
     * @param edgeNameNotIn Filter by edgeName[notIn] (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseEventsCall(String enterpriseLogicalId, Integer start, Integer end, Integer limit, String nextPageLink, String prevPageLink, String include, String eventIs, String eventIsNot, String eventIn, String eventNotIn, String severityIs, String severityIsNot, String severityIn, String severityNotIn, String enterpriseUsernameIs, String enterpriseUsernameIsNot, String enterpriseUsernameIsNull, String enterpriseUsernameIsNotNull, String enterpriseUsernameStartsWith, String enterpriseUsernameNotStartsWith, String enterpriseUsernameEndsWith, String enterpriseUsernameNotEndsWith, String enterpriseUsernameContains, String enterpriseUsernameNotContains, String enterpriseUsernameIn, String enterpriseUsernameNotIn, String detailIs, String detailIsNot, String detailIsNull, String detailIsNotNull, String detailStartsWith, String detailNotStartsWith, String detailEndsWith, String detailNotEndsWith, String detailContains, String detailNotContains, String detailIn, String detailNotIn, String segmentNameIs, String segmentNameIsNot, String segmentNameIsNull, String segmentNameIsNotNull, String segmentNameStartsWith, String segmentNameNotStartsWith, String segmentNameEndsWith, String segmentNameNotEndsWith, String segmentNameContains, String segmentNameNotContains, String segmentNameIn, String segmentNameNotIn, String messageIs, String messageIsNot, String messageIsNull, String messageIsNotNull, String messageStartsWith, String messageNotStartsWith, String messageEndsWith, String messageNotEndsWith, String messageContains, String messageNotContains, String messageIn, String messageNotIn, String edgeNameIs, String edgeNameIsNot, String edgeNameIsNull, String edgeNameIsNotNull, String edgeNameStartsWith, String edgeNameNotStartsWith, String edgeNameEndsWith, String edgeNameNotEndsWith, String edgeNameContains, String edgeNameNotContains, String edgeNameIn, String edgeNameNotIn, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/events"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));
        if (limit != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("limit", limit));
        if (nextPageLink != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("nextPageLink", nextPageLink));
        if (prevPageLink != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("prevPageLink", prevPageLink));
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (eventIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("event[is]", eventIs));
        if (eventIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("event[isNot]", eventIsNot));
        if (eventIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("event[in]", eventIn));
        if (eventNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("event[notIn]", eventNotIn));
        if (severityIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("severity[is]", severityIs));
        if (severityIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("severity[isNot]", severityIsNot));
        if (severityIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("severity[in]", severityIn));
        if (severityNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("severity[notIn]", severityNotIn));
        if (enterpriseUsernameIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseUsername[is]", enterpriseUsernameIs));
        if (enterpriseUsernameIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseUsername[isNot]", enterpriseUsernameIsNot));
        if (enterpriseUsernameIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseUsername[isNull]", enterpriseUsernameIsNull));
        if (enterpriseUsernameIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseUsername[isNotNull]", enterpriseUsernameIsNotNull));
        if (enterpriseUsernameStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseUsername[startsWith]", enterpriseUsernameStartsWith));
        if (enterpriseUsernameNotStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseUsername[notStartsWith]", enterpriseUsernameNotStartsWith));
        if (enterpriseUsernameEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseUsername[endsWith]", enterpriseUsernameEndsWith));
        if (enterpriseUsernameNotEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseUsername[notEndsWith]", enterpriseUsernameNotEndsWith));
        if (enterpriseUsernameContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseUsername[contains]", enterpriseUsernameContains));
        if (enterpriseUsernameNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseUsername[notContains]", enterpriseUsernameNotContains));
        if (enterpriseUsernameIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseUsername[in]", enterpriseUsernameIn));
        if (enterpriseUsernameNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseUsername[notIn]", enterpriseUsernameNotIn));
        if (detailIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detail[is]", detailIs));
        if (detailIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detail[isNot]", detailIsNot));
        if (detailIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detail[isNull]", detailIsNull));
        if (detailIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detail[isNotNull]", detailIsNotNull));
        if (detailStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detail[startsWith]", detailStartsWith));
        if (detailNotStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detail[notStartsWith]", detailNotStartsWith));
        if (detailEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detail[endsWith]", detailEndsWith));
        if (detailNotEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detail[notEndsWith]", detailNotEndsWith));
        if (detailContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detail[contains]", detailContains));
        if (detailNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detail[notContains]", detailNotContains));
        if (detailIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detail[in]", detailIn));
        if (detailNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("detail[notIn]", detailNotIn));
        if (segmentNameIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("segmentName[is]", segmentNameIs));
        if (segmentNameIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("segmentName[isNot]", segmentNameIsNot));
        if (segmentNameIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("segmentName[isNull]", segmentNameIsNull));
        if (segmentNameIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("segmentName[isNotNull]", segmentNameIsNotNull));
        if (segmentNameStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("segmentName[startsWith]", segmentNameStartsWith));
        if (segmentNameNotStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("segmentName[notStartsWith]", segmentNameNotStartsWith));
        if (segmentNameEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("segmentName[endsWith]", segmentNameEndsWith));
        if (segmentNameNotEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("segmentName[notEndsWith]", segmentNameNotEndsWith));
        if (segmentNameContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("segmentName[contains]", segmentNameContains));
        if (segmentNameNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("segmentName[notContains]", segmentNameNotContains));
        if (segmentNameIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("segmentName[in]", segmentNameIn));
        if (segmentNameNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("segmentName[notIn]", segmentNameNotIn));
        if (messageIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("message[is]", messageIs));
        if (messageIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("message[isNot]", messageIsNot));
        if (messageIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("message[isNull]", messageIsNull));
        if (messageIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("message[isNotNull]", messageIsNotNull));
        if (messageStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("message[startsWith]", messageStartsWith));
        if (messageNotStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("message[notStartsWith]", messageNotStartsWith));
        if (messageEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("message[endsWith]", messageEndsWith));
        if (messageNotEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("message[notEndsWith]", messageNotEndsWith));
        if (messageContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("message[contains]", messageContains));
        if (messageNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("message[notContains]", messageNotContains));
        if (messageIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("message[in]", messageIn));
        if (messageNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("message[notIn]", messageNotIn));
        if (edgeNameIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[is]", edgeNameIs));
        if (edgeNameIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[isNot]", edgeNameIsNot));
        if (edgeNameIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[isNull]", edgeNameIsNull));
        if (edgeNameIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[isNotNull]", edgeNameIsNotNull));
        if (edgeNameStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[startsWith]", edgeNameStartsWith));
        if (edgeNameNotStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[notStartsWith]", edgeNameNotStartsWith));
        if (edgeNameEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[endsWith]", edgeNameEndsWith));
        if (edgeNameNotEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[notEndsWith]", edgeNameNotEndsWith));
        if (edgeNameContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[contains]", edgeNameContains));
        if (edgeNameNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[notContains]", edgeNameNotContains));
        if (edgeNameIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[in]", edgeNameIn));
        if (edgeNameNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[notIn]", edgeNameNotIn));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new okhttp3.Interceptor() {
                @Override
                public okhttp3.Response intercept(okhttp3.Interceptor.Chain chain) throws IOException {
                    okhttp3.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private okhttp3.Call getEnterpriseEventsValidateBeforeCall(String enterpriseLogicalId, Integer start, Integer end, Integer limit, String nextPageLink, String prevPageLink, String include, String eventIs, String eventIsNot, String eventIn, String eventNotIn, String severityIs, String severityIsNot, String severityIn, String severityNotIn, String enterpriseUsernameIs, String enterpriseUsernameIsNot, String enterpriseUsernameIsNull, String enterpriseUsernameIsNotNull, String enterpriseUsernameStartsWith, String enterpriseUsernameNotStartsWith, String enterpriseUsernameEndsWith, String enterpriseUsernameNotEndsWith, String enterpriseUsernameContains, String enterpriseUsernameNotContains, String enterpriseUsernameIn, String enterpriseUsernameNotIn, String detailIs, String detailIsNot, String detailIsNull, String detailIsNotNull, String detailStartsWith, String detailNotStartsWith, String detailEndsWith, String detailNotEndsWith, String detailContains, String detailNotContains, String detailIn, String detailNotIn, String segmentNameIs, String segmentNameIsNot, String segmentNameIsNull, String segmentNameIsNotNull, String segmentNameStartsWith, String segmentNameNotStartsWith, String segmentNameEndsWith, String segmentNameNotEndsWith, String segmentNameContains, String segmentNameNotContains, String segmentNameIn, String segmentNameNotIn, String messageIs, String messageIsNot, String messageIsNull, String messageIsNotNull, String messageStartsWith, String messageNotStartsWith, String messageEndsWith, String messageNotEndsWith, String messageContains, String messageNotContains, String messageIn, String messageNotIn, String edgeNameIs, String edgeNameIsNot, String edgeNameIsNull, String edgeNameIsNotNull, String edgeNameStartsWith, String edgeNameNotStartsWith, String edgeNameEndsWith, String edgeNameNotEndsWith, String edgeNameContains, String edgeNameNotContains, String edgeNameIn, String edgeNameNotIn, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseEvents(Async)");
        }
        
        okhttp3.Call call = getEnterpriseEventsCall(enterpriseLogicalId, start, end, limit, nextPageLink, prevPageLink, include, eventIs, eventIsNot, eventIn, eventNotIn, severityIs, severityIsNot, severityIn, severityNotIn, enterpriseUsernameIs, enterpriseUsernameIsNot, enterpriseUsernameIsNull, enterpriseUsernameIsNotNull, enterpriseUsernameStartsWith, enterpriseUsernameNotStartsWith, enterpriseUsernameEndsWith, enterpriseUsernameNotEndsWith, enterpriseUsernameContains, enterpriseUsernameNotContains, enterpriseUsernameIn, enterpriseUsernameNotIn, detailIs, detailIsNot, detailIsNull, detailIsNotNull, detailStartsWith, detailNotStartsWith, detailEndsWith, detailNotEndsWith, detailContains, detailNotContains, detailIn, detailNotIn, segmentNameIs, segmentNameIsNot, segmentNameIsNull, segmentNameIsNotNull, segmentNameStartsWith, segmentNameNotStartsWith, segmentNameEndsWith, segmentNameNotEndsWith, segmentNameContains, segmentNameNotContains, segmentNameIn, segmentNameNotIn, messageIs, messageIsNot, messageIsNull, messageIsNotNull, messageStartsWith, messageNotStartsWith, messageEndsWith, messageNotEndsWith, messageContains, messageNotContains, messageIn, messageNotIn, edgeNameIs, edgeNameIsNot, edgeNameIsNull, edgeNameIsNotNull, edgeNameStartsWith, edgeNameNotStartsWith, edgeNameEndsWith, edgeNameNotEndsWith, edgeNameContains, edgeNameNotContains, edgeNameIn, edgeNameNotIn, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch time-ordered list of events &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param nextPageLink A nextPageLink value, as fetched via a past call to a list API method. When a nextPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param prevPageLink A prevPageLink value, as fetched via a past call to a list API method. When a prevPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param eventIs Filter by event[is] (optional)
     * @param eventIsNot Filter by event[isNot] (optional)
     * @param eventIn Filter by event[in] (optional)
     * @param eventNotIn Filter by event[notIn] (optional)
     * @param severityIs Filter by severity[is] (optional)
     * @param severityIsNot Filter by severity[isNot] (optional)
     * @param severityIn Filter by severity[in] (optional)
     * @param severityNotIn Filter by severity[notIn] (optional)
     * @param enterpriseUsernameIs Filter by enterpriseUsername[is] (optional)
     * @param enterpriseUsernameIsNot Filter by enterpriseUsername[isNot] (optional)
     * @param enterpriseUsernameIsNull Filter by enterpriseUsername[isNull] (optional)
     * @param enterpriseUsernameIsNotNull Filter by enterpriseUsername[isNotNull] (optional)
     * @param enterpriseUsernameStartsWith Filter by enterpriseUsername[startsWith] (optional)
     * @param enterpriseUsernameNotStartsWith Filter by enterpriseUsername[notStartsWith] (optional)
     * @param enterpriseUsernameEndsWith Filter by enterpriseUsername[endsWith] (optional)
     * @param enterpriseUsernameNotEndsWith Filter by enterpriseUsername[notEndsWith] (optional)
     * @param enterpriseUsernameContains Filter by enterpriseUsername[contains] (optional)
     * @param enterpriseUsernameNotContains Filter by enterpriseUsername[notContains] (optional)
     * @param enterpriseUsernameIn Filter by enterpriseUsername[in] (optional)
     * @param enterpriseUsernameNotIn Filter by enterpriseUsername[notIn] (optional)
     * @param detailIs Filter by detail[is] (optional)
     * @param detailIsNot Filter by detail[isNot] (optional)
     * @param detailIsNull Filter by detail[isNull] (optional)
     * @param detailIsNotNull Filter by detail[isNotNull] (optional)
     * @param detailStartsWith Filter by detail[startsWith] (optional)
     * @param detailNotStartsWith Filter by detail[notStartsWith] (optional)
     * @param detailEndsWith Filter by detail[endsWith] (optional)
     * @param detailNotEndsWith Filter by detail[notEndsWith] (optional)
     * @param detailContains Filter by detail[contains] (optional)
     * @param detailNotContains Filter by detail[notContains] (optional)
     * @param detailIn Filter by detail[in] (optional)
     * @param detailNotIn Filter by detail[notIn] (optional)
     * @param segmentNameIs Filter by segmentName[is] (optional)
     * @param segmentNameIsNot Filter by segmentName[isNot] (optional)
     * @param segmentNameIsNull Filter by segmentName[isNull] (optional)
     * @param segmentNameIsNotNull Filter by segmentName[isNotNull] (optional)
     * @param segmentNameStartsWith Filter by segmentName[startsWith] (optional)
     * @param segmentNameNotStartsWith Filter by segmentName[notStartsWith] (optional)
     * @param segmentNameEndsWith Filter by segmentName[endsWith] (optional)
     * @param segmentNameNotEndsWith Filter by segmentName[notEndsWith] (optional)
     * @param segmentNameContains Filter by segmentName[contains] (optional)
     * @param segmentNameNotContains Filter by segmentName[notContains] (optional)
     * @param segmentNameIn Filter by segmentName[in] (optional)
     * @param segmentNameNotIn Filter by segmentName[notIn] (optional)
     * @param messageIs Filter by message[is] (optional)
     * @param messageIsNot Filter by message[isNot] (optional)
     * @param messageIsNull Filter by message[isNull] (optional)
     * @param messageIsNotNull Filter by message[isNotNull] (optional)
     * @param messageStartsWith Filter by message[startsWith] (optional)
     * @param messageNotStartsWith Filter by message[notStartsWith] (optional)
     * @param messageEndsWith Filter by message[endsWith] (optional)
     * @param messageNotEndsWith Filter by message[notEndsWith] (optional)
     * @param messageContains Filter by message[contains] (optional)
     * @param messageNotContains Filter by message[notContains] (optional)
     * @param messageIn Filter by message[in] (optional)
     * @param messageNotIn Filter by message[notIn] (optional)
     * @param edgeNameIs Filter by edgeName[is] (optional)
     * @param edgeNameIsNot Filter by edgeName[isNot] (optional)
     * @param edgeNameIsNull Filter by edgeName[isNull] (optional)
     * @param edgeNameIsNotNull Filter by edgeName[isNotNull] (optional)
     * @param edgeNameStartsWith Filter by edgeName[startsWith] (optional)
     * @param edgeNameNotStartsWith Filter by edgeName[notStartsWith] (optional)
     * @param edgeNameEndsWith Filter by edgeName[endsWith] (optional)
     * @param edgeNameNotEndsWith Filter by edgeName[notEndsWith] (optional)
     * @param edgeNameContains Filter by edgeName[contains] (optional)
     * @param edgeNameNotContains Filter by edgeName[notContains] (optional)
     * @param edgeNameIn Filter by edgeName[in] (optional)
     * @param edgeNameNotIn Filter by edgeName[notIn] (optional)
     * @return EnterpriseEventsSchema
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EnterpriseEventsSchema getEnterpriseEvents(String enterpriseLogicalId, Integer start, Integer end, Integer limit, String nextPageLink, String prevPageLink, String include, String eventIs, String eventIsNot, String eventIn, String eventNotIn, String severityIs, String severityIsNot, String severityIn, String severityNotIn, String enterpriseUsernameIs, String enterpriseUsernameIsNot, String enterpriseUsernameIsNull, String enterpriseUsernameIsNotNull, String enterpriseUsernameStartsWith, String enterpriseUsernameNotStartsWith, String enterpriseUsernameEndsWith, String enterpriseUsernameNotEndsWith, String enterpriseUsernameContains, String enterpriseUsernameNotContains, String enterpriseUsernameIn, String enterpriseUsernameNotIn, String detailIs, String detailIsNot, String detailIsNull, String detailIsNotNull, String detailStartsWith, String detailNotStartsWith, String detailEndsWith, String detailNotEndsWith, String detailContains, String detailNotContains, String detailIn, String detailNotIn, String segmentNameIs, String segmentNameIsNot, String segmentNameIsNull, String segmentNameIsNotNull, String segmentNameStartsWith, String segmentNameNotStartsWith, String segmentNameEndsWith, String segmentNameNotEndsWith, String segmentNameContains, String segmentNameNotContains, String segmentNameIn, String segmentNameNotIn, String messageIs, String messageIsNot, String messageIsNull, String messageIsNotNull, String messageStartsWith, String messageNotStartsWith, String messageEndsWith, String messageNotEndsWith, String messageContains, String messageNotContains, String messageIn, String messageNotIn, String edgeNameIs, String edgeNameIsNot, String edgeNameIsNull, String edgeNameIsNotNull, String edgeNameStartsWith, String edgeNameNotStartsWith, String edgeNameEndsWith, String edgeNameNotEndsWith, String edgeNameContains, String edgeNameNotContains, String edgeNameIn, String edgeNameNotIn) throws ApiException {
        ApiResponse<EnterpriseEventsSchema> resp = getEnterpriseEventsWithHttpInfo(enterpriseLogicalId, start, end, limit, nextPageLink, prevPageLink, include, eventIs, eventIsNot, eventIn, eventNotIn, severityIs, severityIsNot, severityIn, severityNotIn, enterpriseUsernameIs, enterpriseUsernameIsNot, enterpriseUsernameIsNull, enterpriseUsernameIsNotNull, enterpriseUsernameStartsWith, enterpriseUsernameNotStartsWith, enterpriseUsernameEndsWith, enterpriseUsernameNotEndsWith, enterpriseUsernameContains, enterpriseUsernameNotContains, enterpriseUsernameIn, enterpriseUsernameNotIn, detailIs, detailIsNot, detailIsNull, detailIsNotNull, detailStartsWith, detailNotStartsWith, detailEndsWith, detailNotEndsWith, detailContains, detailNotContains, detailIn, detailNotIn, segmentNameIs, segmentNameIsNot, segmentNameIsNull, segmentNameIsNotNull, segmentNameStartsWith, segmentNameNotStartsWith, segmentNameEndsWith, segmentNameNotEndsWith, segmentNameContains, segmentNameNotContains, segmentNameIn, segmentNameNotIn, messageIs, messageIsNot, messageIsNull, messageIsNotNull, messageStartsWith, messageNotStartsWith, messageEndsWith, messageNotEndsWith, messageContains, messageNotContains, messageIn, messageNotIn, edgeNameIs, edgeNameIsNot, edgeNameIsNull, edgeNameIsNotNull, edgeNameStartsWith, edgeNameNotStartsWith, edgeNameEndsWith, edgeNameNotEndsWith, edgeNameContains, edgeNameNotContains, edgeNameIn, edgeNameNotIn);
        return resp.getData();
    }

    /**
     * 
     * Fetch time-ordered list of events &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param nextPageLink A nextPageLink value, as fetched via a past call to a list API method. When a nextPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param prevPageLink A prevPageLink value, as fetched via a past call to a list API method. When a prevPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param eventIs Filter by event[is] (optional)
     * @param eventIsNot Filter by event[isNot] (optional)
     * @param eventIn Filter by event[in] (optional)
     * @param eventNotIn Filter by event[notIn] (optional)
     * @param severityIs Filter by severity[is] (optional)
     * @param severityIsNot Filter by severity[isNot] (optional)
     * @param severityIn Filter by severity[in] (optional)
     * @param severityNotIn Filter by severity[notIn] (optional)
     * @param enterpriseUsernameIs Filter by enterpriseUsername[is] (optional)
     * @param enterpriseUsernameIsNot Filter by enterpriseUsername[isNot] (optional)
     * @param enterpriseUsernameIsNull Filter by enterpriseUsername[isNull] (optional)
     * @param enterpriseUsernameIsNotNull Filter by enterpriseUsername[isNotNull] (optional)
     * @param enterpriseUsernameStartsWith Filter by enterpriseUsername[startsWith] (optional)
     * @param enterpriseUsernameNotStartsWith Filter by enterpriseUsername[notStartsWith] (optional)
     * @param enterpriseUsernameEndsWith Filter by enterpriseUsername[endsWith] (optional)
     * @param enterpriseUsernameNotEndsWith Filter by enterpriseUsername[notEndsWith] (optional)
     * @param enterpriseUsernameContains Filter by enterpriseUsername[contains] (optional)
     * @param enterpriseUsernameNotContains Filter by enterpriseUsername[notContains] (optional)
     * @param enterpriseUsernameIn Filter by enterpriseUsername[in] (optional)
     * @param enterpriseUsernameNotIn Filter by enterpriseUsername[notIn] (optional)
     * @param detailIs Filter by detail[is] (optional)
     * @param detailIsNot Filter by detail[isNot] (optional)
     * @param detailIsNull Filter by detail[isNull] (optional)
     * @param detailIsNotNull Filter by detail[isNotNull] (optional)
     * @param detailStartsWith Filter by detail[startsWith] (optional)
     * @param detailNotStartsWith Filter by detail[notStartsWith] (optional)
     * @param detailEndsWith Filter by detail[endsWith] (optional)
     * @param detailNotEndsWith Filter by detail[notEndsWith] (optional)
     * @param detailContains Filter by detail[contains] (optional)
     * @param detailNotContains Filter by detail[notContains] (optional)
     * @param detailIn Filter by detail[in] (optional)
     * @param detailNotIn Filter by detail[notIn] (optional)
     * @param segmentNameIs Filter by segmentName[is] (optional)
     * @param segmentNameIsNot Filter by segmentName[isNot] (optional)
     * @param segmentNameIsNull Filter by segmentName[isNull] (optional)
     * @param segmentNameIsNotNull Filter by segmentName[isNotNull] (optional)
     * @param segmentNameStartsWith Filter by segmentName[startsWith] (optional)
     * @param segmentNameNotStartsWith Filter by segmentName[notStartsWith] (optional)
     * @param segmentNameEndsWith Filter by segmentName[endsWith] (optional)
     * @param segmentNameNotEndsWith Filter by segmentName[notEndsWith] (optional)
     * @param segmentNameContains Filter by segmentName[contains] (optional)
     * @param segmentNameNotContains Filter by segmentName[notContains] (optional)
     * @param segmentNameIn Filter by segmentName[in] (optional)
     * @param segmentNameNotIn Filter by segmentName[notIn] (optional)
     * @param messageIs Filter by message[is] (optional)
     * @param messageIsNot Filter by message[isNot] (optional)
     * @param messageIsNull Filter by message[isNull] (optional)
     * @param messageIsNotNull Filter by message[isNotNull] (optional)
     * @param messageStartsWith Filter by message[startsWith] (optional)
     * @param messageNotStartsWith Filter by message[notStartsWith] (optional)
     * @param messageEndsWith Filter by message[endsWith] (optional)
     * @param messageNotEndsWith Filter by message[notEndsWith] (optional)
     * @param messageContains Filter by message[contains] (optional)
     * @param messageNotContains Filter by message[notContains] (optional)
     * @param messageIn Filter by message[in] (optional)
     * @param messageNotIn Filter by message[notIn] (optional)
     * @param edgeNameIs Filter by edgeName[is] (optional)
     * @param edgeNameIsNot Filter by edgeName[isNot] (optional)
     * @param edgeNameIsNull Filter by edgeName[isNull] (optional)
     * @param edgeNameIsNotNull Filter by edgeName[isNotNull] (optional)
     * @param edgeNameStartsWith Filter by edgeName[startsWith] (optional)
     * @param edgeNameNotStartsWith Filter by edgeName[notStartsWith] (optional)
     * @param edgeNameEndsWith Filter by edgeName[endsWith] (optional)
     * @param edgeNameNotEndsWith Filter by edgeName[notEndsWith] (optional)
     * @param edgeNameContains Filter by edgeName[contains] (optional)
     * @param edgeNameNotContains Filter by edgeName[notContains] (optional)
     * @param edgeNameIn Filter by edgeName[in] (optional)
     * @param edgeNameNotIn Filter by edgeName[notIn] (optional)
     * @return ApiResponse&lt;EnterpriseEventsSchema&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EnterpriseEventsSchema> getEnterpriseEventsWithHttpInfo(String enterpriseLogicalId, Integer start, Integer end, Integer limit, String nextPageLink, String prevPageLink, String include, String eventIs, String eventIsNot, String eventIn, String eventNotIn, String severityIs, String severityIsNot, String severityIn, String severityNotIn, String enterpriseUsernameIs, String enterpriseUsernameIsNot, String enterpriseUsernameIsNull, String enterpriseUsernameIsNotNull, String enterpriseUsernameStartsWith, String enterpriseUsernameNotStartsWith, String enterpriseUsernameEndsWith, String enterpriseUsernameNotEndsWith, String enterpriseUsernameContains, String enterpriseUsernameNotContains, String enterpriseUsernameIn, String enterpriseUsernameNotIn, String detailIs, String detailIsNot, String detailIsNull, String detailIsNotNull, String detailStartsWith, String detailNotStartsWith, String detailEndsWith, String detailNotEndsWith, String detailContains, String detailNotContains, String detailIn, String detailNotIn, String segmentNameIs, String segmentNameIsNot, String segmentNameIsNull, String segmentNameIsNotNull, String segmentNameStartsWith, String segmentNameNotStartsWith, String segmentNameEndsWith, String segmentNameNotEndsWith, String segmentNameContains, String segmentNameNotContains, String segmentNameIn, String segmentNameNotIn, String messageIs, String messageIsNot, String messageIsNull, String messageIsNotNull, String messageStartsWith, String messageNotStartsWith, String messageEndsWith, String messageNotEndsWith, String messageContains, String messageNotContains, String messageIn, String messageNotIn, String edgeNameIs, String edgeNameIsNot, String edgeNameIsNull, String edgeNameIsNotNull, String edgeNameStartsWith, String edgeNameNotStartsWith, String edgeNameEndsWith, String edgeNameNotEndsWith, String edgeNameContains, String edgeNameNotContains, String edgeNameIn, String edgeNameNotIn) throws ApiException {
        okhttp3.Call call = getEnterpriseEventsValidateBeforeCall(enterpriseLogicalId, start, end, limit, nextPageLink, prevPageLink, include, eventIs, eventIsNot, eventIn, eventNotIn, severityIs, severityIsNot, severityIn, severityNotIn, enterpriseUsernameIs, enterpriseUsernameIsNot, enterpriseUsernameIsNull, enterpriseUsernameIsNotNull, enterpriseUsernameStartsWith, enterpriseUsernameNotStartsWith, enterpriseUsernameEndsWith, enterpriseUsernameNotEndsWith, enterpriseUsernameContains, enterpriseUsernameNotContains, enterpriseUsernameIn, enterpriseUsernameNotIn, detailIs, detailIsNot, detailIsNull, detailIsNotNull, detailStartsWith, detailNotStartsWith, detailEndsWith, detailNotEndsWith, detailContains, detailNotContains, detailIn, detailNotIn, segmentNameIs, segmentNameIsNot, segmentNameIsNull, segmentNameIsNotNull, segmentNameStartsWith, segmentNameNotStartsWith, segmentNameEndsWith, segmentNameNotEndsWith, segmentNameContains, segmentNameNotContains, segmentNameIn, segmentNameNotIn, messageIs, messageIsNot, messageIsNull, messageIsNotNull, messageStartsWith, messageNotStartsWith, messageEndsWith, messageNotEndsWith, messageContains, messageNotContains, messageIn, messageNotIn, edgeNameIs, edgeNameIsNot, edgeNameIsNull, edgeNameIsNotNull, edgeNameStartsWith, edgeNameNotStartsWith, edgeNameEndsWith, edgeNameNotEndsWith, edgeNameContains, edgeNameNotContains, edgeNameIn, edgeNameNotIn, null, null);
        Type localVarReturnType = new TypeToken<EnterpriseEventsSchema>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch time-ordered list of events &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param nextPageLink A nextPageLink value, as fetched via a past call to a list API method. When a nextPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param prevPageLink A prevPageLink value, as fetched via a past call to a list API method. When a prevPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param eventIs Filter by event[is] (optional)
     * @param eventIsNot Filter by event[isNot] (optional)
     * @param eventIn Filter by event[in] (optional)
     * @param eventNotIn Filter by event[notIn] (optional)
     * @param severityIs Filter by severity[is] (optional)
     * @param severityIsNot Filter by severity[isNot] (optional)
     * @param severityIn Filter by severity[in] (optional)
     * @param severityNotIn Filter by severity[notIn] (optional)
     * @param enterpriseUsernameIs Filter by enterpriseUsername[is] (optional)
     * @param enterpriseUsernameIsNot Filter by enterpriseUsername[isNot] (optional)
     * @param enterpriseUsernameIsNull Filter by enterpriseUsername[isNull] (optional)
     * @param enterpriseUsernameIsNotNull Filter by enterpriseUsername[isNotNull] (optional)
     * @param enterpriseUsernameStartsWith Filter by enterpriseUsername[startsWith] (optional)
     * @param enterpriseUsernameNotStartsWith Filter by enterpriseUsername[notStartsWith] (optional)
     * @param enterpriseUsernameEndsWith Filter by enterpriseUsername[endsWith] (optional)
     * @param enterpriseUsernameNotEndsWith Filter by enterpriseUsername[notEndsWith] (optional)
     * @param enterpriseUsernameContains Filter by enterpriseUsername[contains] (optional)
     * @param enterpriseUsernameNotContains Filter by enterpriseUsername[notContains] (optional)
     * @param enterpriseUsernameIn Filter by enterpriseUsername[in] (optional)
     * @param enterpriseUsernameNotIn Filter by enterpriseUsername[notIn] (optional)
     * @param detailIs Filter by detail[is] (optional)
     * @param detailIsNot Filter by detail[isNot] (optional)
     * @param detailIsNull Filter by detail[isNull] (optional)
     * @param detailIsNotNull Filter by detail[isNotNull] (optional)
     * @param detailStartsWith Filter by detail[startsWith] (optional)
     * @param detailNotStartsWith Filter by detail[notStartsWith] (optional)
     * @param detailEndsWith Filter by detail[endsWith] (optional)
     * @param detailNotEndsWith Filter by detail[notEndsWith] (optional)
     * @param detailContains Filter by detail[contains] (optional)
     * @param detailNotContains Filter by detail[notContains] (optional)
     * @param detailIn Filter by detail[in] (optional)
     * @param detailNotIn Filter by detail[notIn] (optional)
     * @param segmentNameIs Filter by segmentName[is] (optional)
     * @param segmentNameIsNot Filter by segmentName[isNot] (optional)
     * @param segmentNameIsNull Filter by segmentName[isNull] (optional)
     * @param segmentNameIsNotNull Filter by segmentName[isNotNull] (optional)
     * @param segmentNameStartsWith Filter by segmentName[startsWith] (optional)
     * @param segmentNameNotStartsWith Filter by segmentName[notStartsWith] (optional)
     * @param segmentNameEndsWith Filter by segmentName[endsWith] (optional)
     * @param segmentNameNotEndsWith Filter by segmentName[notEndsWith] (optional)
     * @param segmentNameContains Filter by segmentName[contains] (optional)
     * @param segmentNameNotContains Filter by segmentName[notContains] (optional)
     * @param segmentNameIn Filter by segmentName[in] (optional)
     * @param segmentNameNotIn Filter by segmentName[notIn] (optional)
     * @param messageIs Filter by message[is] (optional)
     * @param messageIsNot Filter by message[isNot] (optional)
     * @param messageIsNull Filter by message[isNull] (optional)
     * @param messageIsNotNull Filter by message[isNotNull] (optional)
     * @param messageStartsWith Filter by message[startsWith] (optional)
     * @param messageNotStartsWith Filter by message[notStartsWith] (optional)
     * @param messageEndsWith Filter by message[endsWith] (optional)
     * @param messageNotEndsWith Filter by message[notEndsWith] (optional)
     * @param messageContains Filter by message[contains] (optional)
     * @param messageNotContains Filter by message[notContains] (optional)
     * @param messageIn Filter by message[in] (optional)
     * @param messageNotIn Filter by message[notIn] (optional)
     * @param edgeNameIs Filter by edgeName[is] (optional)
     * @param edgeNameIsNot Filter by edgeName[isNot] (optional)
     * @param edgeNameIsNull Filter by edgeName[isNull] (optional)
     * @param edgeNameIsNotNull Filter by edgeName[isNotNull] (optional)
     * @param edgeNameStartsWith Filter by edgeName[startsWith] (optional)
     * @param edgeNameNotStartsWith Filter by edgeName[notStartsWith] (optional)
     * @param edgeNameEndsWith Filter by edgeName[endsWith] (optional)
     * @param edgeNameNotEndsWith Filter by edgeName[notEndsWith] (optional)
     * @param edgeNameContains Filter by edgeName[contains] (optional)
     * @param edgeNameNotContains Filter by edgeName[notContains] (optional)
     * @param edgeNameIn Filter by edgeName[in] (optional)
     * @param edgeNameNotIn Filter by edgeName[notIn] (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseEventsAsync(String enterpriseLogicalId, Integer start, Integer end, Integer limit, String nextPageLink, String prevPageLink, String include, String eventIs, String eventIsNot, String eventIn, String eventNotIn, String severityIs, String severityIsNot, String severityIn, String severityNotIn, String enterpriseUsernameIs, String enterpriseUsernameIsNot, String enterpriseUsernameIsNull, String enterpriseUsernameIsNotNull, String enterpriseUsernameStartsWith, String enterpriseUsernameNotStartsWith, String enterpriseUsernameEndsWith, String enterpriseUsernameNotEndsWith, String enterpriseUsernameContains, String enterpriseUsernameNotContains, String enterpriseUsernameIn, String enterpriseUsernameNotIn, String detailIs, String detailIsNot, String detailIsNull, String detailIsNotNull, String detailStartsWith, String detailNotStartsWith, String detailEndsWith, String detailNotEndsWith, String detailContains, String detailNotContains, String detailIn, String detailNotIn, String segmentNameIs, String segmentNameIsNot, String segmentNameIsNull, String segmentNameIsNotNull, String segmentNameStartsWith, String segmentNameNotStartsWith, String segmentNameEndsWith, String segmentNameNotEndsWith, String segmentNameContains, String segmentNameNotContains, String segmentNameIn, String segmentNameNotIn, String messageIs, String messageIsNot, String messageIsNull, String messageIsNotNull, String messageStartsWith, String messageNotStartsWith, String messageEndsWith, String messageNotEndsWith, String messageContains, String messageNotContains, String messageIn, String messageNotIn, String edgeNameIs, String edgeNameIsNot, String edgeNameIsNull, String edgeNameIsNotNull, String edgeNameStartsWith, String edgeNameNotStartsWith, String edgeNameEndsWith, String edgeNameNotEndsWith, String edgeNameContains, String edgeNameNotContains, String edgeNameIn, String edgeNameNotIn, final ApiCallback<EnterpriseEventsSchema> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        okhttp3.Call call = getEnterpriseEventsValidateBeforeCall(enterpriseLogicalId, start, end, limit, nextPageLink, prevPageLink, include, eventIs, eventIsNot, eventIn, eventNotIn, severityIs, severityIsNot, severityIn, severityNotIn, enterpriseUsernameIs, enterpriseUsernameIsNot, enterpriseUsernameIsNull, enterpriseUsernameIsNotNull, enterpriseUsernameStartsWith, enterpriseUsernameNotStartsWith, enterpriseUsernameEndsWith, enterpriseUsernameNotEndsWith, enterpriseUsernameContains, enterpriseUsernameNotContains, enterpriseUsernameIn, enterpriseUsernameNotIn, detailIs, detailIsNot, detailIsNull, detailIsNotNull, detailStartsWith, detailNotStartsWith, detailEndsWith, detailNotEndsWith, detailContains, detailNotContains, detailIn, detailNotIn, segmentNameIs, segmentNameIsNot, segmentNameIsNull, segmentNameIsNotNull, segmentNameStartsWith, segmentNameNotStartsWith, segmentNameEndsWith, segmentNameNotEndsWith, segmentNameContains, segmentNameNotContains, segmentNameIn, segmentNameNotIn, messageIs, messageIsNot, messageIsNull, messageIsNotNull, messageStartsWith, messageNotStartsWith, messageEndsWith, messageNotEndsWith, messageContains, messageNotContains, messageIn, messageNotIn, edgeNameIs, edgeNameIsNot, edgeNameIsNull, edgeNameIsNotNull, edgeNameStartsWith, edgeNameNotStartsWith, edgeNameEndsWith, edgeNameNotEndsWith, edgeNameContains, edgeNameNotContains, edgeNameIn, edgeNameNotIn, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EnterpriseEventsSchema>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
