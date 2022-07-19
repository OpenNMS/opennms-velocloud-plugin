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


import org.opennms.velocloud.model.EnterpriseAlertsSchema;
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

public class AlertsApi {
    private ApiClient apiClient;

    public AlertsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public AlertsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getEnterpriseAlerts
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param nextPageLink A nextPageLink value, as fetched via a past call to a list API method. When a nextPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param prevPageLink A prevPageLink value, as fetched via a past call to a list API method. When a prevPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param edgeNameContains Filter by edgeName[contains] (optional)
     * @param edgeNameNotContains Filter by edgeName[notContains] (optional)
     * @param linkNameContains Filter by linkName[contains] (optional)
     * @param linkNameNotContains Filter by linkName[notContains] (optional)
     * @param typeIs Filter by type[is] (optional)
     * @param typeIsNot Filter by type[isNot] (optional)
     * @param typeIn Filter by type[in] (optional)
     * @param typeNotIn Filter by type[notIn] (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @param enterpriseAlertConfigurationIdIsNull Filter by enterpriseAlertConfigurationId[isNull] (optional)
     * @param enterpriseAlertConfigurationIdIsNotNull Filter by enterpriseAlertConfigurationId[isNotNull] (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseAlertsCall(String enterpriseLogicalId, Integer start, Integer end, Integer limit, String nextPageLink, String prevPageLink, String include, String edgeNameContains, String edgeNameNotContains, String linkNameContains, String linkNameNotContains, String typeIs, String typeIsNot, String typeIn, String typeNotIn, String stateIs, String stateIsNot, String stateIn, String stateNotIn, Float enterpriseAlertConfigurationIdIsNull, Float enterpriseAlertConfigurationIdIsNotNull, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/alerts"
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
        if (edgeNameContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[contains]", edgeNameContains));
        if (edgeNameNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeName[notContains]", edgeNameNotContains));
        if (linkNameContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("linkName[contains]", linkNameContains));
        if (linkNameNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("linkName[notContains]", linkNameNotContains));
        if (typeIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("type[is]", typeIs));
        if (typeIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("type[isNot]", typeIsNot));
        if (typeIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("type[in]", typeIn));
        if (typeNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("type[notIn]", typeNotIn));
        if (stateIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[is]", stateIs));
        if (stateIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[isNot]", stateIsNot));
        if (stateIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[in]", stateIn));
        if (stateNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[notIn]", stateNotIn));
        if (enterpriseAlertConfigurationIdIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseAlertConfigurationId[isNull]", enterpriseAlertConfigurationIdIsNull));
        if (enterpriseAlertConfigurationIdIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("enterpriseAlertConfigurationId[isNotNull]", enterpriseAlertConfigurationIdIsNotNull));

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
    private okhttp3.Call getEnterpriseAlertsValidateBeforeCall(String enterpriseLogicalId, Integer start, Integer end, Integer limit, String nextPageLink, String prevPageLink, String include, String edgeNameContains, String edgeNameNotContains, String linkNameContains, String linkNameNotContains, String typeIs, String typeIsNot, String typeIn, String typeNotIn, String stateIs, String stateIsNot, String stateIn, String stateNotIn, Float enterpriseAlertConfigurationIdIsNull, Float enterpriseAlertConfigurationIdIsNotNull, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseAlerts(Async)");
        }
        
        okhttp3.Call call = getEnterpriseAlertsCall(enterpriseLogicalId, start, end, limit, nextPageLink, prevPageLink, include, edgeNameContains, edgeNameNotContains, linkNameContains, linkNameNotContains, typeIs, typeIsNot, typeIn, typeNotIn, stateIs, stateIsNot, stateIn, stateNotIn, enterpriseAlertConfigurationIdIsNull, enterpriseAlertConfigurationIdIsNotNull, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch past triggered alerts for the specified enterprise &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param nextPageLink A nextPageLink value, as fetched via a past call to a list API method. When a nextPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param prevPageLink A prevPageLink value, as fetched via a past call to a list API method. When a prevPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param edgeNameContains Filter by edgeName[contains] (optional)
     * @param edgeNameNotContains Filter by edgeName[notContains] (optional)
     * @param linkNameContains Filter by linkName[contains] (optional)
     * @param linkNameNotContains Filter by linkName[notContains] (optional)
     * @param typeIs Filter by type[is] (optional)
     * @param typeIsNot Filter by type[isNot] (optional)
     * @param typeIn Filter by type[in] (optional)
     * @param typeNotIn Filter by type[notIn] (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @param enterpriseAlertConfigurationIdIsNull Filter by enterpriseAlertConfigurationId[isNull] (optional)
     * @param enterpriseAlertConfigurationIdIsNotNull Filter by enterpriseAlertConfigurationId[isNotNull] (optional)
     * @return EnterpriseAlertsSchema
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EnterpriseAlertsSchema getEnterpriseAlerts(String enterpriseLogicalId, Integer start, Integer end, Integer limit, String nextPageLink, String prevPageLink, String include, String edgeNameContains, String edgeNameNotContains, String linkNameContains, String linkNameNotContains, String typeIs, String typeIsNot, String typeIn, String typeNotIn, String stateIs, String stateIsNot, String stateIn, String stateNotIn, Float enterpriseAlertConfigurationIdIsNull, Float enterpriseAlertConfigurationIdIsNotNull) throws ApiException {
        ApiResponse<EnterpriseAlertsSchema> resp = getEnterpriseAlertsWithHttpInfo(enterpriseLogicalId, start, end, limit, nextPageLink, prevPageLink, include, edgeNameContains, edgeNameNotContains, linkNameContains, linkNameNotContains, typeIs, typeIsNot, typeIn, typeNotIn, stateIs, stateIsNot, stateIn, stateNotIn, enterpriseAlertConfigurationIdIsNull, enterpriseAlertConfigurationIdIsNotNull);
        return resp.getData();
    }

    /**
     * 
     * Fetch past triggered alerts for the specified enterprise &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param nextPageLink A nextPageLink value, as fetched via a past call to a list API method. When a nextPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param prevPageLink A prevPageLink value, as fetched via a past call to a list API method. When a prevPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param edgeNameContains Filter by edgeName[contains] (optional)
     * @param edgeNameNotContains Filter by edgeName[notContains] (optional)
     * @param linkNameContains Filter by linkName[contains] (optional)
     * @param linkNameNotContains Filter by linkName[notContains] (optional)
     * @param typeIs Filter by type[is] (optional)
     * @param typeIsNot Filter by type[isNot] (optional)
     * @param typeIn Filter by type[in] (optional)
     * @param typeNotIn Filter by type[notIn] (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @param enterpriseAlertConfigurationIdIsNull Filter by enterpriseAlertConfigurationId[isNull] (optional)
     * @param enterpriseAlertConfigurationIdIsNotNull Filter by enterpriseAlertConfigurationId[isNotNull] (optional)
     * @return ApiResponse&lt;EnterpriseAlertsSchema&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EnterpriseAlertsSchema> getEnterpriseAlertsWithHttpInfo(String enterpriseLogicalId, Integer start, Integer end, Integer limit, String nextPageLink, String prevPageLink, String include, String edgeNameContains, String edgeNameNotContains, String linkNameContains, String linkNameNotContains, String typeIs, String typeIsNot, String typeIn, String typeNotIn, String stateIs, String stateIsNot, String stateIn, String stateNotIn, Float enterpriseAlertConfigurationIdIsNull, Float enterpriseAlertConfigurationIdIsNotNull) throws ApiException {
        okhttp3.Call call = getEnterpriseAlertsValidateBeforeCall(enterpriseLogicalId, start, end, limit, nextPageLink, prevPageLink, include, edgeNameContains, edgeNameNotContains, linkNameContains, linkNameNotContains, typeIs, typeIsNot, typeIn, typeNotIn, stateIs, stateIsNot, stateIn, stateNotIn, enterpriseAlertConfigurationIdIsNull, enterpriseAlertConfigurationIdIsNotNull, null, null);
        Type localVarReturnType = new TypeToken<EnterpriseAlertsSchema>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch past triggered alerts for the specified enterprise &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param nextPageLink A nextPageLink value, as fetched via a past call to a list API method. When a nextPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param prevPageLink A prevPageLink value, as fetched via a past call to a list API method. When a prevPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored. (optional)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param edgeNameContains Filter by edgeName[contains] (optional)
     * @param edgeNameNotContains Filter by edgeName[notContains] (optional)
     * @param linkNameContains Filter by linkName[contains] (optional)
     * @param linkNameNotContains Filter by linkName[notContains] (optional)
     * @param typeIs Filter by type[is] (optional)
     * @param typeIsNot Filter by type[isNot] (optional)
     * @param typeIn Filter by type[in] (optional)
     * @param typeNotIn Filter by type[notIn] (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @param enterpriseAlertConfigurationIdIsNull Filter by enterpriseAlertConfigurationId[isNull] (optional)
     * @param enterpriseAlertConfigurationIdIsNotNull Filter by enterpriseAlertConfigurationId[isNotNull] (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseAlertsAsync(String enterpriseLogicalId, Integer start, Integer end, Integer limit, String nextPageLink, String prevPageLink, String include, String edgeNameContains, String edgeNameNotContains, String linkNameContains, String linkNameNotContains, String typeIs, String typeIsNot, String typeIn, String typeNotIn, String stateIs, String stateIsNot, String stateIn, String stateNotIn, Float enterpriseAlertConfigurationIdIsNull, Float enterpriseAlertConfigurationIdIsNotNull, final ApiCallback<EnterpriseAlertsSchema> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseAlertsValidateBeforeCall(enterpriseLogicalId, start, end, limit, nextPageLink, prevPageLink, include, edgeNameContains, edgeNameNotContains, linkNameContains, linkNameNotContains, typeIs, typeIsNot, typeIn, typeNotIn, stateIs, stateIsNot, stateIn, stateNotIn, enterpriseAlertConfigurationIdIsNull, enterpriseAlertConfigurationIdIsNotNull, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EnterpriseAlertsSchema>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
