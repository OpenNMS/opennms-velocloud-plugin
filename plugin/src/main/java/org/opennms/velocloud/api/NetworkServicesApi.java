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


import org.opennms.velocloud.model.BgpSessionCollection;
import org.opennms.velocloud.model.EdgeNonSDWANTunnelStatus;
import org.opennms.velocloud.model.GatewayNonSdWanSiteSchema;
import org.opennms.velocloud.model.InternalServerError;
import org.opennms.velocloud.model.NonSdwanServiceStatus;
import org.opennms.velocloud.model.RateLimitExceededError;
import org.opennms.velocloud.model.ResourceNotFoundError;
import org.opennms.velocloud.model.UnAuthorized;
import org.opennms.velocloud.model.ValidationError;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkServicesApi {
    private ApiClient apiClient;

    public NetworkServicesApi() {
        this(Configuration.getDefaultApiClient());
    }

    public NetworkServicesApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getEdgeNonSdwanTunnelStatusViaEdge
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param state Filter by non-SD-WAN tunnel state (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEdgeNonSdwanTunnelStatusViaEdgeCall(String enterpriseLogicalId, String edgeLogicalId, String include, String sortBy, Integer start, Integer end, Object state, String stateIs, String stateIsNot, String stateIn, String stateNotIn, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/nonSdWanTunnelStatus"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));
        if (state != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state", state));
        if (stateIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[is]", stateIs));
        if (stateIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[isNot]", stateIsNot));
        if (stateIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[in]", stateIn));
        if (stateNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[notIn]", stateNotIn));

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
    private okhttp3.Call getEdgeNonSdwanTunnelStatusViaEdgeValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, String include, String sortBy, Integer start, Integer end, Object state, String stateIs, String stateIsNot, String stateIn, String stateNotIn, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEdgeNonSdwanTunnelStatusViaEdge(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEdgeNonSdwanTunnelStatusViaEdge(Async)");
        }
        
        okhttp3.Call call = getEdgeNonSdwanTunnelStatusViaEdgeCall(enterpriseLogicalId, edgeLogicalId, include, sortBy, start, end, state, stateIs, stateIsNot, stateIn, stateNotIn, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch Edge non-SD-WAN tunnel status &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param state Filter by non-SD-WAN tunnel state (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @return EdgeNonSDWANTunnelStatus
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EdgeNonSDWANTunnelStatus getEdgeNonSdwanTunnelStatusViaEdge(String enterpriseLogicalId, String edgeLogicalId, String include, String sortBy, Integer start, Integer end, Object state, String stateIs, String stateIsNot, String stateIn, String stateNotIn) throws ApiException {
        ApiResponse<EdgeNonSDWANTunnelStatus> resp = getEdgeNonSdwanTunnelStatusViaEdgeWithHttpInfo(enterpriseLogicalId, edgeLogicalId, include, sortBy, start, end, state, stateIs, stateIsNot, stateIn, stateNotIn);
        return resp.getData();
    }

    /**
     * 
     * Fetch Edge non-SD-WAN tunnel status &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param state Filter by non-SD-WAN tunnel state (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @return ApiResponse&lt;EdgeNonSDWANTunnelStatus&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EdgeNonSDWANTunnelStatus> getEdgeNonSdwanTunnelStatusViaEdgeWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, String include, String sortBy, Integer start, Integer end, Object state, String stateIs, String stateIsNot, String stateIn, String stateNotIn) throws ApiException {
        okhttp3.Call call = getEdgeNonSdwanTunnelStatusViaEdgeValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, include, sortBy, start, end, state, stateIs, stateIsNot, stateIn, stateNotIn, null, null);
        Type localVarReturnType = new TypeToken<EdgeNonSDWANTunnelStatus>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch Edge non-SD-WAN tunnel status &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param state Filter by non-SD-WAN tunnel state (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEdgeNonSdwanTunnelStatusViaEdgeAsync(String enterpriseLogicalId, String edgeLogicalId, String include, String sortBy, Integer start, Integer end, Object state, String stateIs, String stateIsNot, String stateIn, String stateNotIn, final ApiCallback<EdgeNonSDWANTunnelStatus> callback) throws ApiException {

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

        okhttp3.Call call = getEdgeNonSdwanTunnelStatusViaEdgeValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, include, sortBy, start, end, state, stateIs, stateIsNot, stateIn, stateNotIn, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EdgeNonSDWANTunnelStatus>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEnterpriseBgpSessions
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param peerType The BGP peer type (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @param neighborIpIs Filter by neighborIp[is] (optional)
     * @param neighborIpIsNot Filter by neighborIp[isNot] (optional)
     * @param neighborIpIsNull Filter by neighborIp[isNull] (optional)
     * @param neighborIpIsNotNull Filter by neighborIp[isNotNull] (optional)
     * @param neighborIpStartsWith Filter by neighborIp[startsWith] (optional)
     * @param neighborIpNotStartsWith Filter by neighborIp[notStartsWith] (optional)
     * @param neighborIpEndsWith Filter by neighborIp[endsWith] (optional)
     * @param neighborIpNotEndsWith Filter by neighborIp[notEndsWith] (optional)
     * @param neighborIpContains Filter by neighborIp[contains] (optional)
     * @param neighborIpNotContains Filter by neighborIp[notContains] (optional)
     * @param neighborIpIn Filter by neighborIp[in] (optional)
     * @param neighborIpNotIn Filter by neighborIp[notIn] (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseBgpSessionsCall(String enterpriseLogicalId, String include, String peerType, String stateIs, String stateIsNot, String stateIn, String stateNotIn, String neighborIpIs, String neighborIpIsNot, String neighborIpIsNull, String neighborIpIsNotNull, String neighborIpStartsWith, String neighborIpNotStartsWith, String neighborIpEndsWith, String neighborIpNotEndsWith, String neighborIpContains, String neighborIpNotContains, String neighborIpIn, String neighborIpNotIn, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/bgpSessions"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (peerType != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("peerType", peerType));
        if (stateIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[is]", stateIs));
        if (stateIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[isNot]", stateIsNot));
        if (stateIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[in]", stateIn));
        if (stateNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[notIn]", stateNotIn));
        if (neighborIpIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("neighborIp[is]", neighborIpIs));
        if (neighborIpIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("neighborIp[isNot]", neighborIpIsNot));
        if (neighborIpIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("neighborIp[isNull]", neighborIpIsNull));
        if (neighborIpIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("neighborIp[isNotNull]", neighborIpIsNotNull));
        if (neighborIpStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("neighborIp[startsWith]", neighborIpStartsWith));
        if (neighborIpNotStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("neighborIp[notStartsWith]", neighborIpNotStartsWith));
        if (neighborIpEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("neighborIp[endsWith]", neighborIpEndsWith));
        if (neighborIpNotEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("neighborIp[notEndsWith]", neighborIpNotEndsWith));
        if (neighborIpContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("neighborIp[contains]", neighborIpContains));
        if (neighborIpNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("neighborIp[notContains]", neighborIpNotContains));
        if (neighborIpIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("neighborIp[in]", neighborIpIn));
        if (neighborIpNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("neighborIp[notIn]", neighborIpNotIn));

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
    private okhttp3.Call getEnterpriseBgpSessionsValidateBeforeCall(String enterpriseLogicalId, String include, String peerType, String stateIs, String stateIsNot, String stateIn, String stateNotIn, String neighborIpIs, String neighborIpIsNot, String neighborIpIsNull, String neighborIpIsNotNull, String neighborIpStartsWith, String neighborIpNotStartsWith, String neighborIpEndsWith, String neighborIpNotEndsWith, String neighborIpContains, String neighborIpNotContains, String neighborIpIn, String neighborIpNotIn, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseBgpSessions(Async)");
        }
        
        okhttp3.Call call = getEnterpriseBgpSessionsCall(enterpriseLogicalId, include, peerType, stateIs, stateIsNot, stateIn, stateNotIn, neighborIpIs, neighborIpIsNot, neighborIpIsNull, neighborIpIsNotNull, neighborIpStartsWith, neighborIpNotStartsWith, neighborIpEndsWith, neighborIpNotEndsWith, neighborIpContains, neighborIpNotContains, neighborIpIn, neighborIpNotIn, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Get BGP peering session state &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param peerType The BGP peer type (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @param neighborIpIs Filter by neighborIp[is] (optional)
     * @param neighborIpIsNot Filter by neighborIp[isNot] (optional)
     * @param neighborIpIsNull Filter by neighborIp[isNull] (optional)
     * @param neighborIpIsNotNull Filter by neighborIp[isNotNull] (optional)
     * @param neighborIpStartsWith Filter by neighborIp[startsWith] (optional)
     * @param neighborIpNotStartsWith Filter by neighborIp[notStartsWith] (optional)
     * @param neighborIpEndsWith Filter by neighborIp[endsWith] (optional)
     * @param neighborIpNotEndsWith Filter by neighborIp[notEndsWith] (optional)
     * @param neighborIpContains Filter by neighborIp[contains] (optional)
     * @param neighborIpNotContains Filter by neighborIp[notContains] (optional)
     * @param neighborIpIn Filter by neighborIp[in] (optional)
     * @param neighborIpNotIn Filter by neighborIp[notIn] (optional)
     * @return BgpSessionCollection
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public BgpSessionCollection getEnterpriseBgpSessions(String enterpriseLogicalId, String include, String peerType, String stateIs, String stateIsNot, String stateIn, String stateNotIn, String neighborIpIs, String neighborIpIsNot, String neighborIpIsNull, String neighborIpIsNotNull, String neighborIpStartsWith, String neighborIpNotStartsWith, String neighborIpEndsWith, String neighborIpNotEndsWith, String neighborIpContains, String neighborIpNotContains, String neighborIpIn, String neighborIpNotIn) throws ApiException {
        ApiResponse<BgpSessionCollection> resp = getEnterpriseBgpSessionsWithHttpInfo(enterpriseLogicalId, include, peerType, stateIs, stateIsNot, stateIn, stateNotIn, neighborIpIs, neighborIpIsNot, neighborIpIsNull, neighborIpIsNotNull, neighborIpStartsWith, neighborIpNotStartsWith, neighborIpEndsWith, neighborIpNotEndsWith, neighborIpContains, neighborIpNotContains, neighborIpIn, neighborIpNotIn);
        return resp.getData();
    }

    /**
     * 
     * Get BGP peering session state &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param peerType The BGP peer type (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @param neighborIpIs Filter by neighborIp[is] (optional)
     * @param neighborIpIsNot Filter by neighborIp[isNot] (optional)
     * @param neighborIpIsNull Filter by neighborIp[isNull] (optional)
     * @param neighborIpIsNotNull Filter by neighborIp[isNotNull] (optional)
     * @param neighborIpStartsWith Filter by neighborIp[startsWith] (optional)
     * @param neighborIpNotStartsWith Filter by neighborIp[notStartsWith] (optional)
     * @param neighborIpEndsWith Filter by neighborIp[endsWith] (optional)
     * @param neighborIpNotEndsWith Filter by neighborIp[notEndsWith] (optional)
     * @param neighborIpContains Filter by neighborIp[contains] (optional)
     * @param neighborIpNotContains Filter by neighborIp[notContains] (optional)
     * @param neighborIpIn Filter by neighborIp[in] (optional)
     * @param neighborIpNotIn Filter by neighborIp[notIn] (optional)
     * @return ApiResponse&lt;BgpSessionCollection&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<BgpSessionCollection> getEnterpriseBgpSessionsWithHttpInfo(String enterpriseLogicalId, String include, String peerType, String stateIs, String stateIsNot, String stateIn, String stateNotIn, String neighborIpIs, String neighborIpIsNot, String neighborIpIsNull, String neighborIpIsNotNull, String neighborIpStartsWith, String neighborIpNotStartsWith, String neighborIpEndsWith, String neighborIpNotEndsWith, String neighborIpContains, String neighborIpNotContains, String neighborIpIn, String neighborIpNotIn) throws ApiException {
        okhttp3.Call call = getEnterpriseBgpSessionsValidateBeforeCall(enterpriseLogicalId, include, peerType, stateIs, stateIsNot, stateIn, stateNotIn, neighborIpIs, neighborIpIsNot, neighborIpIsNull, neighborIpIsNotNull, neighborIpStartsWith, neighborIpNotStartsWith, neighborIpEndsWith, neighborIpNotEndsWith, neighborIpContains, neighborIpNotContains, neighborIpIn, neighborIpNotIn, null, null);
        Type localVarReturnType = new TypeToken<BgpSessionCollection>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Get BGP peering session state &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param peerType The BGP peer type (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @param neighborIpIs Filter by neighborIp[is] (optional)
     * @param neighborIpIsNot Filter by neighborIp[isNot] (optional)
     * @param neighborIpIsNull Filter by neighborIp[isNull] (optional)
     * @param neighborIpIsNotNull Filter by neighborIp[isNotNull] (optional)
     * @param neighborIpStartsWith Filter by neighborIp[startsWith] (optional)
     * @param neighborIpNotStartsWith Filter by neighborIp[notStartsWith] (optional)
     * @param neighborIpEndsWith Filter by neighborIp[endsWith] (optional)
     * @param neighborIpNotEndsWith Filter by neighborIp[notEndsWith] (optional)
     * @param neighborIpContains Filter by neighborIp[contains] (optional)
     * @param neighborIpNotContains Filter by neighborIp[notContains] (optional)
     * @param neighborIpIn Filter by neighborIp[in] (optional)
     * @param neighborIpNotIn Filter by neighborIp[notIn] (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseBgpSessionsAsync(String enterpriseLogicalId, String include, String peerType, String stateIs, String stateIsNot, String stateIn, String stateNotIn, String neighborIpIs, String neighborIpIsNot, String neighborIpIsNull, String neighborIpIsNotNull, String neighborIpStartsWith, String neighborIpNotStartsWith, String neighborIpEndsWith, String neighborIpNotEndsWith, String neighborIpContains, String neighborIpNotContains, String neighborIpIn, String neighborIpNotIn, final ApiCallback<BgpSessionCollection> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseBgpSessionsValidateBeforeCall(enterpriseLogicalId, include, peerType, stateIs, stateIsNot, stateIn, stateNotIn, neighborIpIs, neighborIpIsNot, neighborIpIsNull, neighborIpIsNotNull, neighborIpStartsWith, neighborIpNotStartsWith, neighborIpEndsWith, neighborIpNotEndsWith, neighborIpContains, neighborIpNotContains, neighborIpIn, neighborIpNotIn, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<BgpSessionCollection>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEnterpriseNonSdwanTunnelStatusViaEdge
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param state Filter by non-SD-WAN tunnel state (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseNonSdwanTunnelStatusViaEdgeCall(String enterpriseLogicalId, String include, String sortBy, Object state, String stateIs, String stateIsNot, String stateIn, String stateNotIn, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edgeNonSdwanServices/status"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (state != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state", state));
        if (stateIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[is]", stateIs));
        if (stateIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[isNot]", stateIsNot));
        if (stateIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[in]", stateIn));
        if (stateNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("state[notIn]", stateNotIn));

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
    private okhttp3.Call getEnterpriseNonSdwanTunnelStatusViaEdgeValidateBeforeCall(String enterpriseLogicalId, String include, String sortBy, Object state, String stateIs, String stateIsNot, String stateIn, String stateNotIn, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseNonSdwanTunnelStatusViaEdge(Async)");
        }
        
        okhttp3.Call call = getEnterpriseNonSdwanTunnelStatusViaEdgeCall(enterpriseLogicalId, include, sortBy, state, stateIs, stateIsNot, stateIn, stateNotIn, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch nonSDWAN service status &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param state Filter by non-SD-WAN tunnel state (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @return NonSdwanServiceStatus
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public NonSdwanServiceStatus getEnterpriseNonSdwanTunnelStatusViaEdge(String enterpriseLogicalId, String include, String sortBy, Object state, String stateIs, String stateIsNot, String stateIn, String stateNotIn) throws ApiException {
        ApiResponse<NonSdwanServiceStatus> resp = getEnterpriseNonSdwanTunnelStatusViaEdgeWithHttpInfo(enterpriseLogicalId, include, sortBy, state, stateIs, stateIsNot, stateIn, stateNotIn);
        return resp.getData();
    }

    /**
     * 
     * Fetch nonSDWAN service status &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param state Filter by non-SD-WAN tunnel state (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @return ApiResponse&lt;NonSdwanServiceStatus&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<NonSdwanServiceStatus> getEnterpriseNonSdwanTunnelStatusViaEdgeWithHttpInfo(String enterpriseLogicalId, String include, String sortBy, Object state, String stateIs, String stateIsNot, String stateIn, String stateNotIn) throws ApiException {
        okhttp3.Call call = getEnterpriseNonSdwanTunnelStatusViaEdgeValidateBeforeCall(enterpriseLogicalId, include, sortBy, state, stateIs, stateIsNot, stateIn, stateNotIn, null, null);
        Type localVarReturnType = new TypeToken<NonSdwanServiceStatus>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch nonSDWAN service status &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param state Filter by non-SD-WAN tunnel state (optional)
     * @param stateIs Filter by state[is] (optional)
     * @param stateIsNot Filter by state[isNot] (optional)
     * @param stateIn Filter by state[in] (optional)
     * @param stateNotIn Filter by state[notIn] (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseNonSdwanTunnelStatusViaEdgeAsync(String enterpriseLogicalId, String include, String sortBy, Object state, String stateIs, String stateIsNot, String stateIn, String stateNotIn, final ApiCallback<NonSdwanServiceStatus> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseNonSdwanTunnelStatusViaEdgeValidateBeforeCall(enterpriseLogicalId, include, sortBy, state, stateIs, stateIsNot, stateIn, stateNotIn, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<NonSdwanServiceStatus>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getNonSdwanTunnelsViaGateway
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getNonSdwanTunnelsViaGatewayCall(String enterpriseLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/gatewayNonSdWanSites"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));

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
    private okhttp3.Call getNonSdwanTunnelsViaGatewayValidateBeforeCall(String enterpriseLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getNonSdwanTunnelsViaGateway(Async)");
        }
        
        okhttp3.Call call = getNonSdwanTunnelsViaGatewayCall(enterpriseLogicalId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch Non-SD-WAN Sites connected via a Gateway &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return GatewayNonSdWanSiteSchema
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public GatewayNonSdWanSiteSchema getNonSdwanTunnelsViaGateway(String enterpriseLogicalId, String include) throws ApiException {
        ApiResponse<GatewayNonSdWanSiteSchema> resp = getNonSdwanTunnelsViaGatewayWithHttpInfo(enterpriseLogicalId, include);
        return resp.getData();
    }

    /**
     * 
     * Fetch Non-SD-WAN Sites connected via a Gateway &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;GatewayNonSdWanSiteSchema&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<GatewayNonSdWanSiteSchema> getNonSdwanTunnelsViaGatewayWithHttpInfo(String enterpriseLogicalId, String include) throws ApiException {
        okhttp3.Call call = getNonSdwanTunnelsViaGatewayValidateBeforeCall(enterpriseLogicalId, include, null, null);
        Type localVarReturnType = new TypeToken<GatewayNonSdWanSiteSchema>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch Non-SD-WAN Sites connected via a Gateway &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getNonSdwanTunnelsViaGatewayAsync(String enterpriseLogicalId, String include, final ApiCallback<GatewayNonSdWanSiteSchema> callback) throws ApiException {

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

        okhttp3.Call call = getNonSdwanTunnelsViaGatewayValidateBeforeCall(enterpriseLogicalId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<GatewayNonSdWanSiteSchema>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
