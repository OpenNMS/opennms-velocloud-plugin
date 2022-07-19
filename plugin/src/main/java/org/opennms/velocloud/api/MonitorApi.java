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


import org.opennms.velocloud.model.ApplicationClasses;
import org.opennms.velocloud.model.ApplicationMapResource;
import org.opennms.velocloud.model.ApplicationResource;
import org.opennms.velocloud.model.Applications;
import org.opennms.velocloud.model.BgpSessionCollection;
import org.opennms.velocloud.model.ClientDeviceResource;
import org.opennms.velocloud.model.ClientDevices;
import org.opennms.velocloud.model.CreateEnterpriseSchema;
import org.opennms.velocloud.model.DataValidationError;
import org.opennms.velocloud.model.EdgeHealthStatsMetricsSchema;
import org.opennms.velocloud.model.EdgeHealthStatsSeriesSchema;
import org.opennms.velocloud.model.EdgeNonSDWANTunnelStatus;
import org.opennms.velocloud.model.EdgePathStatsMetrics;
import org.opennms.velocloud.model.EdgePathStatsSeries;
import org.opennms.velocloud.model.EdgeResource;
import org.opennms.velocloud.model.EdgeResourceCollection;
import org.opennms.velocloud.model.EnterpriseAlertsSchema;
import org.opennms.velocloud.model.EnterpriseEventsSchema;
import org.opennms.velocloud.model.EnterpriseHealthStatsSchema;
import org.opennms.velocloud.model.EnterpriseResource;
import org.opennms.velocloud.model.EnterpriseResourceCollection;
import org.opennms.velocloud.model.FlowStats;
import org.opennms.velocloud.model.FlowStatsSeries;
import org.opennms.velocloud.model.GatewayNonSdWanSiteSchema;
import org.opennms.velocloud.model.InternalServerError;
import org.opennms.velocloud.model.LinkQualityStatsResource;
import org.opennms.velocloud.model.LinkQualityStatsTimeSeriesResource;
import org.opennms.velocloud.model.LinkStats;
import org.opennms.velocloud.model.LinkStatsSeries;
import org.opennms.velocloud.model.NonSdwanServiceStatus;
import org.opennms.velocloud.model.RateLimitExceededError;
import org.opennms.velocloud.model.ResourceNotFoundError;
import org.opennms.velocloud.model.UnAuthorized;
import org.opennms.velocloud.model.UnsupportedMediaTypeError;
import org.opennms.velocloud.model.ValidationError;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitorApi {
    private ApiClient apiClient;

    public MonitorApi() {
        this(Configuration.getDefaultApiClient());
    }

    public MonitorApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for createEnterprise
     * @param body Enterprise creation schema (optional)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call createEnterpriseCall(CreateEnterpriseSchema body, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/";

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
            "application/json"
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
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private okhttp3.Call createEnterpriseValidateBeforeCall(CreateEnterpriseSchema body, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        okhttp3.Call call = createEnterpriseCall(body, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Create a Customer &lt;br /&gt; &lt;br /&gt; 
     * @param body Enterprise creation schema (optional)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return EnterpriseResource
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EnterpriseResource createEnterprise(CreateEnterpriseSchema body, String include) throws ApiException {
        ApiResponse<EnterpriseResource> resp = createEnterpriseWithHttpInfo(body, include);
        return resp.getData();
    }

    /**
     * 
     * Create a Customer &lt;br /&gt; &lt;br /&gt; 
     * @param body Enterprise creation schema (optional)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;EnterpriseResource&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EnterpriseResource> createEnterpriseWithHttpInfo(CreateEnterpriseSchema body, String include) throws ApiException {
        okhttp3.Call call = createEnterpriseValidateBeforeCall(body, include, null, null);
        Type localVarReturnType = new TypeToken<EnterpriseResource>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Create a Customer &lt;br /&gt; &lt;br /&gt; 
     * @param body Enterprise creation schema (optional)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call createEnterpriseAsync(CreateEnterpriseSchema body, String include, final ApiCallback<EnterpriseResource> callback) throws ApiException {

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

        okhttp3.Call call = createEnterpriseValidateBeforeCall(body, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EnterpriseResource>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deleteEnterprise
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call deleteEnterpriseCall(String enterpriseLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/"
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
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private okhttp3.Call deleteEnterpriseValidateBeforeCall(String enterpriseLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling deleteEnterprise(Async)");
        }
        
        okhttp3.Call call = deleteEnterpriseCall(enterpriseLogicalId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Delete a Customer &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public void deleteEnterprise(String enterpriseLogicalId, String include) throws ApiException {
        deleteEnterpriseWithHttpInfo(enterpriseLogicalId, include);
    }

    /**
     * 
     * Delete a Customer &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Void> deleteEnterpriseWithHttpInfo(String enterpriseLogicalId, String include) throws ApiException {
        okhttp3.Call call = deleteEnterpriseValidateBeforeCall(enterpriseLogicalId, include, null, null);
        return apiClient.execute(call);
    }

    /**
     *  (asynchronously)
     * Delete a Customer &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call deleteEnterpriseAsync(String enterpriseLogicalId, String include, final ApiCallback<Void> callback) throws ApiException {

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

        okhttp3.Call call = deleteEnterpriseValidateBeforeCall(enterpriseLogicalId, include, progressListener, progressRequestListener);
        apiClient.executeAsync(call, callback);
        return call;
    }
    /**
     * Build call for getApplicationMap
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getApplicationMapCall(String logicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/applicationMaps/{logicalId}"
            .replaceAll("\\{" + "logicalId" + "\\}", apiClient.escapeString(logicalId.toString()));

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
    private okhttp3.Call getApplicationMapValidateBeforeCall(String logicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'logicalId' is set
        if (logicalId == null) {
            throw new ApiException("Missing the required parameter 'logicalId' when calling getApplicationMap(Async)");
        }
        
        okhttp3.Call call = getApplicationMapCall(logicalId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * [OPERATOR ONLY] Fetch application map resource &lt;br /&gt; &lt;br /&gt; 
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApplicationMapResource
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApplicationMapResource getApplicationMap(String logicalId, String include) throws ApiException {
        ApiResponse<ApplicationMapResource> resp = getApplicationMapWithHttpInfo(logicalId, include);
        return resp.getData();
    }

    /**
     * 
     * [OPERATOR ONLY] Fetch application map resource &lt;br /&gt; &lt;br /&gt; 
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;ApplicationMapResource&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ApplicationMapResource> getApplicationMapWithHttpInfo(String logicalId, String include) throws ApiException {
        okhttp3.Call call = getApplicationMapValidateBeforeCall(logicalId, include, null, null);
        Type localVarReturnType = new TypeToken<ApplicationMapResource>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * [OPERATOR ONLY] Fetch application map resource &lt;br /&gt; &lt;br /&gt; 
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getApplicationMapAsync(String logicalId, String include, final ApiCallback<ApplicationMapResource> callback) throws ApiException {

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

        okhttp3.Call call = getApplicationMapValidateBeforeCall(logicalId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ApplicationMapResource>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getApplicationMapApplication
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param appId The &#x60;appId&#x60; for the target application (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getApplicationMapApplicationCall(String logicalId, Integer appId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/applicationMaps/{logicalId}/applications/{appId}"
            .replaceAll("\\{" + "logicalId" + "\\}", apiClient.escapeString(logicalId.toString()))
            .replaceAll("\\{" + "appId" + "\\}", apiClient.escapeString(appId.toString()));

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
    private okhttp3.Call getApplicationMapApplicationValidateBeforeCall(String logicalId, Integer appId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'logicalId' is set
        if (logicalId == null) {
            throw new ApiException("Missing the required parameter 'logicalId' when calling getApplicationMapApplication(Async)");
        }
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling getApplicationMapApplication(Async)");
        }
        
        okhttp3.Call call = getApplicationMapApplicationCall(logicalId, appId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * [OPERATOR ONLY] Fetch an application definition for the target application map &lt;br /&gt; &lt;br /&gt; 
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param appId The &#x60;appId&#x60; for the target application (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApplicationResource
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApplicationResource getApplicationMapApplication(String logicalId, Integer appId, String include) throws ApiException {
        ApiResponse<ApplicationResource> resp = getApplicationMapApplicationWithHttpInfo(logicalId, appId, include);
        return resp.getData();
    }

    /**
     * 
     * [OPERATOR ONLY] Fetch an application definition for the target application map &lt;br /&gt; &lt;br /&gt; 
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param appId The &#x60;appId&#x60; for the target application (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;ApplicationResource&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ApplicationResource> getApplicationMapApplicationWithHttpInfo(String logicalId, Integer appId, String include) throws ApiException {
        okhttp3.Call call = getApplicationMapApplicationValidateBeforeCall(logicalId, appId, include, null, null);
        Type localVarReturnType = new TypeToken<ApplicationResource>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * [OPERATOR ONLY] Fetch an application definition for the target application map &lt;br /&gt; &lt;br /&gt; 
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param appId The &#x60;appId&#x60; for the target application (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getApplicationMapApplicationAsync(String logicalId, Integer appId, String include, final ApiCallback<ApplicationResource> callback) throws ApiException {

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

        okhttp3.Call call = getApplicationMapApplicationValidateBeforeCall(logicalId, appId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ApplicationResource>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getApplicationMapApplicationClasses
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getApplicationMapApplicationClassesCall(String logicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/applicationMaps/{logicalId}/applicationClasses"
            .replaceAll("\\{" + "logicalId" + "\\}", apiClient.escapeString(logicalId.toString()));

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
    private okhttp3.Call getApplicationMapApplicationClassesValidateBeforeCall(String logicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'logicalId' is set
        if (logicalId == null) {
            throw new ApiException("Missing the required parameter 'logicalId' when calling getApplicationMapApplicationClasses(Async)");
        }
        
        okhttp3.Call call = getApplicationMapApplicationClassesCall(logicalId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch application map classes &lt;br /&gt; &lt;br /&gt; 
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApplicationClasses
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApplicationClasses getApplicationMapApplicationClasses(String logicalId, String include) throws ApiException {
        ApiResponse<ApplicationClasses> resp = getApplicationMapApplicationClassesWithHttpInfo(logicalId, include);
        return resp.getData();
    }

    /**
     * 
     * Fetch application map classes &lt;br /&gt; &lt;br /&gt; 
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;ApplicationClasses&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ApplicationClasses> getApplicationMapApplicationClassesWithHttpInfo(String logicalId, String include) throws ApiException {
        okhttp3.Call call = getApplicationMapApplicationClassesValidateBeforeCall(logicalId, include, null, null);
        Type localVarReturnType = new TypeToken<ApplicationClasses>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch application map classes &lt;br /&gt; &lt;br /&gt; 
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getApplicationMapApplicationClassesAsync(String logicalId, String include, final ApiCallback<ApplicationClasses> callback) throws ApiException {

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

        okhttp3.Call call = getApplicationMapApplicationClassesValidateBeforeCall(logicalId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ApplicationClasses>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getApplicationMapApplications
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getApplicationMapApplicationsCall(String logicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/applicationMaps/{logicalId}/applications"
            .replaceAll("\\{" + "logicalId" + "\\}", apiClient.escapeString(logicalId.toString()));

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
    private okhttp3.Call getApplicationMapApplicationsValidateBeforeCall(String logicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'logicalId' is set
        if (logicalId == null) {
            throw new ApiException("Missing the required parameter 'logicalId' when calling getApplicationMapApplications(Async)");
        }
        
        okhttp3.Call call = getApplicationMapApplicationsCall(logicalId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * [OPERATOR ONLY] List applications associated with an application map &lt;br /&gt; &lt;br /&gt; 
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return Applications
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Applications getApplicationMapApplications(String logicalId, String include) throws ApiException {
        ApiResponse<Applications> resp = getApplicationMapApplicationsWithHttpInfo(logicalId, include);
        return resp.getData();
    }

    /**
     * 
     * [OPERATOR ONLY] List applications associated with an application map &lt;br /&gt; &lt;br /&gt; 
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;Applications&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Applications> getApplicationMapApplicationsWithHttpInfo(String logicalId, String include) throws ApiException {
        okhttp3.Call call = getApplicationMapApplicationsValidateBeforeCall(logicalId, include, null, null);
        Type localVarReturnType = new TypeToken<Applications>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * [OPERATOR ONLY] List applications associated with an application map &lt;br /&gt; &lt;br /&gt; 
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getApplicationMapApplicationsAsync(String logicalId, String include, final ApiCallback<Applications> callback) throws ApiException {

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

        okhttp3.Call call = getApplicationMapApplicationsValidateBeforeCall(logicalId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Applications>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEdgeApplication
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param appId The &#x60;appId&#x60; for the target application (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEdgeApplicationCall(String enterpriseLogicalId, String edgeLogicalId, Integer appId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/applications/{appId}"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()))
            .replaceAll("\\{" + "appId" + "\\}", apiClient.escapeString(appId.toString()));

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
    private okhttp3.Call getEdgeApplicationValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, Integer appId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEdgeApplication(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEdgeApplication(Async)");
        }
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling getEdgeApplication(Async)");
        }
        
        okhttp3.Call call = getEdgeApplicationCall(enterpriseLogicalId, edgeLogicalId, appId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch edge applications &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param appId The &#x60;appId&#x60; for the target application (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return Applications
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Applications getEdgeApplication(String enterpriseLogicalId, String edgeLogicalId, Integer appId, String include) throws ApiException {
        ApiResponse<Applications> resp = getEdgeApplicationWithHttpInfo(enterpriseLogicalId, edgeLogicalId, appId, include);
        return resp.getData();
    }

    /**
     * 
     * Fetch edge applications &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param appId The &#x60;appId&#x60; for the target application (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;Applications&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Applications> getEdgeApplicationWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, Integer appId, String include) throws ApiException {
        okhttp3.Call call = getEdgeApplicationValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, appId, include, null, null);
        Type localVarReturnType = new TypeToken<Applications>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch edge applications &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param appId The &#x60;appId&#x60; for the target application (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEdgeApplicationAsync(String enterpriseLogicalId, String edgeLogicalId, Integer appId, String include, final ApiCallback<Applications> callback) throws ApiException {

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

        okhttp3.Call call = getEdgeApplicationValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, appId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Applications>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEdgeApplications
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEdgeApplicationsCall(String enterpriseLogicalId, String edgeLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/applications"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()));

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
    private okhttp3.Call getEdgeApplicationsValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEdgeApplications(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEdgeApplications(Async)");
        }
        
        okhttp3.Call call = getEdgeApplicationsCall(enterpriseLogicalId, edgeLogicalId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch edge applications &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return Applications
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Applications getEdgeApplications(String enterpriseLogicalId, String edgeLogicalId, String include) throws ApiException {
        ApiResponse<Applications> resp = getEdgeApplicationsWithHttpInfo(enterpriseLogicalId, edgeLogicalId, include);
        return resp.getData();
    }

    /**
     * 
     * Fetch edge applications &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;Applications&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Applications> getEdgeApplicationsWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, String include) throws ApiException {
        okhttp3.Call call = getEdgeApplicationsValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, include, null, null);
        Type localVarReturnType = new TypeToken<Applications>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch edge applications &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEdgeApplicationsAsync(String enterpriseLogicalId, String edgeLogicalId, String include, final ApiCallback<Applications> callback) throws ApiException {

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

        okhttp3.Call call = getEdgeApplicationsValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Applications>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEdgeFlowMetrics
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param metrics metrics supported for querying flowStats (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEdgeFlowMetricsCall(String enterpriseLogicalId, String edgeLogicalId, String groupBy, Integer start, String include, Integer end, String sortBy, String metrics, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/flowStats"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (groupBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("groupBy", groupBy));
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (metrics != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("metrics", metrics));

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
    private okhttp3.Call getEdgeFlowMetricsValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, String groupBy, Integer start, String include, Integer end, String sortBy, String metrics, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEdgeFlowMetrics(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEdgeFlowMetrics(Async)");
        }
        // verify the required parameter 'groupBy' is set
        if (groupBy == null) {
            throw new ApiException("Missing the required parameter 'groupBy' when calling getEdgeFlowMetrics(Async)");
        }
        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getEdgeFlowMetrics(Async)");
        }
        
        okhttp3.Call call = getEdgeFlowMetricsCall(enterpriseLogicalId, edgeLogicalId, groupBy, start, include, end, sortBy, metrics, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch Edge flow stats &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param metrics metrics supported for querying flowStats (optional)
     * @return FlowStats
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FlowStats getEdgeFlowMetrics(String enterpriseLogicalId, String edgeLogicalId, String groupBy, Integer start, String include, Integer end, String sortBy, String metrics) throws ApiException {
        ApiResponse<FlowStats> resp = getEdgeFlowMetricsWithHttpInfo(enterpriseLogicalId, edgeLogicalId, groupBy, start, include, end, sortBy, metrics);
        return resp.getData();
    }

    /**
     * 
     * Fetch Edge flow stats &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param metrics metrics supported for querying flowStats (optional)
     * @return ApiResponse&lt;FlowStats&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FlowStats> getEdgeFlowMetricsWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, String groupBy, Integer start, String include, Integer end, String sortBy, String metrics) throws ApiException {
        okhttp3.Call call = getEdgeFlowMetricsValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, groupBy, start, include, end, sortBy, metrics, null, null);
        Type localVarReturnType = new TypeToken<FlowStats>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch Edge flow stats &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param metrics metrics supported for querying flowStats (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEdgeFlowMetricsAsync(String enterpriseLogicalId, String edgeLogicalId, String groupBy, Integer start, String include, Integer end, String sortBy, String metrics, final ApiCallback<FlowStats> callback) throws ApiException {

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

        okhttp3.Call call = getEdgeFlowMetricsValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, groupBy, start, include, end, sortBy, metrics, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FlowStats>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEdgeFlowSeries
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param metrics metrics supported for querying flowStats (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEdgeFlowSeriesCall(String enterpriseLogicalId, String edgeLogicalId, String groupBy, Integer start, String metrics, String include, Integer end, String sortBy, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/flowStats/timeSeries"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (groupBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("groupBy", groupBy));
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (metrics != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("metrics", metrics));

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
    private okhttp3.Call getEdgeFlowSeriesValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, String groupBy, Integer start, String metrics, String include, Integer end, String sortBy, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEdgeFlowSeries(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEdgeFlowSeries(Async)");
        }
        // verify the required parameter 'groupBy' is set
        if (groupBy == null) {
            throw new ApiException("Missing the required parameter 'groupBy' when calling getEdgeFlowSeries(Async)");
        }
        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getEdgeFlowSeries(Async)");
        }
        // verify the required parameter 'metrics' is set
        if (metrics == null) {
            throw new ApiException("Missing the required parameter 'metrics' when calling getEdgeFlowSeries(Async)");
        }
        
        okhttp3.Call call = getEdgeFlowSeriesCall(enterpriseLogicalId, edgeLogicalId, groupBy, start, metrics, include, end, sortBy, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch edge flow stats series &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param metrics metrics supported for querying flowStats (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @return FlowStatsSeries
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FlowStatsSeries getEdgeFlowSeries(String enterpriseLogicalId, String edgeLogicalId, String groupBy, Integer start, String metrics, String include, Integer end, String sortBy) throws ApiException {
        ApiResponse<FlowStatsSeries> resp = getEdgeFlowSeriesWithHttpInfo(enterpriseLogicalId, edgeLogicalId, groupBy, start, metrics, include, end, sortBy);
        return resp.getData();
    }

    /**
     * 
     * Fetch edge flow stats series &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param metrics metrics supported for querying flowStats (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @return ApiResponse&lt;FlowStatsSeries&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FlowStatsSeries> getEdgeFlowSeriesWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, String groupBy, Integer start, String metrics, String include, Integer end, String sortBy) throws ApiException {
        okhttp3.Call call = getEdgeFlowSeriesValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, groupBy, start, metrics, include, end, sortBy, null, null);
        Type localVarReturnType = new TypeToken<FlowStatsSeries>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch edge flow stats series &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param metrics metrics supported for querying flowStats (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEdgeFlowSeriesAsync(String enterpriseLogicalId, String edgeLogicalId, String groupBy, Integer start, String metrics, String include, Integer end, String sortBy, final ApiCallback<FlowStatsSeries> callback) throws ApiException {

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

        okhttp3.Call call = getEdgeFlowSeriesValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, groupBy, start, metrics, include, end, sortBy, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FlowStatsSeries>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEdgeHealthMetrics
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param metrics  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEdgeHealthMetricsCall(String enterpriseLogicalId, String edgeLogicalId, String include, Integer start, Integer end, String metrics, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/healthStats"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));
        if (metrics != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("metrics", metrics));

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
    private okhttp3.Call getEdgeHealthMetricsValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, String include, Integer start, Integer end, String metrics, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEdgeHealthMetrics(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEdgeHealthMetrics(Async)");
        }
        
        okhttp3.Call call = getEdgeHealthMetricsCall(enterpriseLogicalId, edgeLogicalId, include, start, end, metrics, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch edge system resource metrics &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param metrics  (optional)
     * @return EdgeHealthStatsMetricsSchema
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EdgeHealthStatsMetricsSchema getEdgeHealthMetrics(String enterpriseLogicalId, String edgeLogicalId, String include, Integer start, Integer end, String metrics) throws ApiException {
        ApiResponse<EdgeHealthStatsMetricsSchema> resp = getEdgeHealthMetricsWithHttpInfo(enterpriseLogicalId, edgeLogicalId, include, start, end, metrics);
        return resp.getData();
    }

    /**
     * 
     * Fetch edge system resource metrics &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param metrics  (optional)
     * @return ApiResponse&lt;EdgeHealthStatsMetricsSchema&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EdgeHealthStatsMetricsSchema> getEdgeHealthMetricsWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, String include, Integer start, Integer end, String metrics) throws ApiException {
        okhttp3.Call call = getEdgeHealthMetricsValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, include, start, end, metrics, null, null);
        Type localVarReturnType = new TypeToken<EdgeHealthStatsMetricsSchema>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch edge system resource metrics &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param metrics  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEdgeHealthMetricsAsync(String enterpriseLogicalId, String edgeLogicalId, String include, Integer start, Integer end, String metrics, final ApiCallback<EdgeHealthStatsMetricsSchema> callback) throws ApiException {

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

        okhttp3.Call call = getEdgeHealthMetricsValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, include, start, end, metrics, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EdgeHealthStatsMetricsSchema>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEdgeHealthSeries
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param metrics  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEdgeHealthSeriesCall(String enterpriseLogicalId, String edgeLogicalId, String include, Integer start, Integer end, String metrics, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/healthStats/timeSeries"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));
        if (metrics != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("metrics", metrics));

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
    private okhttp3.Call getEdgeHealthSeriesValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, String include, Integer start, Integer end, String metrics, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEdgeHealthSeries(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEdgeHealthSeries(Async)");
        }
        
        okhttp3.Call call = getEdgeHealthSeriesCall(enterpriseLogicalId, edgeLogicalId, include, start, end, metrics, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch system resource metric time series data &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param metrics  (optional)
     * @return EdgeHealthStatsSeriesSchema
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EdgeHealthStatsSeriesSchema getEdgeHealthSeries(String enterpriseLogicalId, String edgeLogicalId, String include, Integer start, Integer end, String metrics) throws ApiException {
        ApiResponse<EdgeHealthStatsSeriesSchema> resp = getEdgeHealthSeriesWithHttpInfo(enterpriseLogicalId, edgeLogicalId, include, start, end, metrics);
        return resp.getData();
    }

    /**
     * 
     * Fetch system resource metric time series data &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param metrics  (optional)
     * @return ApiResponse&lt;EdgeHealthStatsSeriesSchema&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EdgeHealthStatsSeriesSchema> getEdgeHealthSeriesWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, String include, Integer start, Integer end, String metrics) throws ApiException {
        okhttp3.Call call = getEdgeHealthSeriesValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, include, start, end, metrics, null, null);
        Type localVarReturnType = new TypeToken<EdgeHealthStatsSeriesSchema>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch system resource metric time series data &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param metrics  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEdgeHealthSeriesAsync(String enterpriseLogicalId, String edgeLogicalId, String include, Integer start, Integer end, String metrics, final ApiCallback<EdgeHealthStatsSeriesSchema> callback) throws ApiException {

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

        okhttp3.Call call = getEdgeHealthSeriesValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, include, start, end, metrics, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EdgeHealthStatsSeriesSchema>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEdgeLinkMetrics
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics metrics supported for querying linkStats (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEdgeLinkMetricsCall(String enterpriseLogicalId, String edgeLogicalId, String include, String metrics, String sortBy, Integer start, Integer end, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/linkStats"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (metrics != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("metrics", metrics));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));

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
    private okhttp3.Call getEdgeLinkMetricsValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, String include, String metrics, String sortBy, Integer start, Integer end, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEdgeLinkMetrics(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEdgeLinkMetrics(Async)");
        }
        
        okhttp3.Call call = getEdgeLinkMetricsCall(enterpriseLogicalId, edgeLogicalId, include, metrics, sortBy, start, end, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Edge WAN link transport metrics &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics metrics supported for querying linkStats (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @return LinkStats
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public LinkStats getEdgeLinkMetrics(String enterpriseLogicalId, String edgeLogicalId, String include, String metrics, String sortBy, Integer start, Integer end) throws ApiException {
        ApiResponse<LinkStats> resp = getEdgeLinkMetricsWithHttpInfo(enterpriseLogicalId, edgeLogicalId, include, metrics, sortBy, start, end);
        return resp.getData();
    }

    /**
     * 
     * Edge WAN link transport metrics &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics metrics supported for querying linkStats (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @return ApiResponse&lt;LinkStats&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<LinkStats> getEdgeLinkMetricsWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, String include, String metrics, String sortBy, Integer start, Integer end) throws ApiException {
        okhttp3.Call call = getEdgeLinkMetricsValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, include, metrics, sortBy, start, end, null, null);
        Type localVarReturnType = new TypeToken<LinkStats>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Edge WAN link transport metrics &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics metrics supported for querying linkStats (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEdgeLinkMetricsAsync(String enterpriseLogicalId, String edgeLogicalId, String include, String metrics, String sortBy, Integer start, Integer end, final ApiCallback<LinkStats> callback) throws ApiException {

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

        okhttp3.Call call = getEdgeLinkMetricsValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, include, metrics, sortBy, start, end, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<LinkStats>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEdgeLinkQualityEvents
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEdgeLinkQualityEventsCall(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, Integer end, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/linkQualityStats"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));

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
    private okhttp3.Call getEdgeLinkQualityEventsValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, Integer end, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEdgeLinkQualityEvents(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEdgeLinkQualityEvents(Async)");
        }
        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getEdgeLinkQualityEvents(Async)");
        }
        
        okhttp3.Call call = getEdgeLinkQualityEventsCall(enterpriseLogicalId, edgeLogicalId, start, include, end, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch link quality scores for an Edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @return LinkQualityStatsResource
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public LinkQualityStatsResource getEdgeLinkQualityEvents(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, Integer end) throws ApiException {
        ApiResponse<LinkQualityStatsResource> resp = getEdgeLinkQualityEventsWithHttpInfo(enterpriseLogicalId, edgeLogicalId, start, include, end);
        return resp.getData();
    }

    /**
     * 
     * Fetch link quality scores for an Edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @return ApiResponse&lt;LinkQualityStatsResource&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<LinkQualityStatsResource> getEdgeLinkQualityEventsWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, Integer end) throws ApiException {
        okhttp3.Call call = getEdgeLinkQualityEventsValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, start, include, end, null, null);
        Type localVarReturnType = new TypeToken<LinkQualityStatsResource>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch link quality scores for an Edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEdgeLinkQualityEventsAsync(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, Integer end, final ApiCallback<LinkQualityStatsResource> callback) throws ApiException {

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

        okhttp3.Call call = getEdgeLinkQualityEventsValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, start, include, end, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<LinkQualityStatsResource>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEdgeLinkQualitySeries
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param minutesPerSample An integer value specifying time slice value for link quality events time series data (optional, default to 5)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEdgeLinkQualitySeriesCall(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, Integer end, Integer minutesPerSample, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/linkQualityStats/timeSeries"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));
        if (minutesPerSample != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("minutesPerSample", minutesPerSample));

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
    private okhttp3.Call getEdgeLinkQualitySeriesValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, Integer end, Integer minutesPerSample, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEdgeLinkQualitySeries(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEdgeLinkQualitySeries(Async)");
        }
        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getEdgeLinkQualitySeries(Async)");
        }
        
        okhttp3.Call call = getEdgeLinkQualitySeriesCall(enterpriseLogicalId, edgeLogicalId, start, include, end, minutesPerSample, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch link quality timeseries for an Edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param minutesPerSample An integer value specifying time slice value for link quality events time series data (optional, default to 5)
     * @return LinkQualityStatsTimeSeriesResource
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public LinkQualityStatsTimeSeriesResource getEdgeLinkQualitySeries(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, Integer end, Integer minutesPerSample) throws ApiException {
        ApiResponse<LinkQualityStatsTimeSeriesResource> resp = getEdgeLinkQualitySeriesWithHttpInfo(enterpriseLogicalId, edgeLogicalId, start, include, end, minutesPerSample);
        return resp.getData();
    }

    /**
     * 
     * Fetch link quality timeseries for an Edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param minutesPerSample An integer value specifying time slice value for link quality events time series data (optional, default to 5)
     * @return ApiResponse&lt;LinkQualityStatsTimeSeriesResource&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<LinkQualityStatsTimeSeriesResource> getEdgeLinkQualitySeriesWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, Integer end, Integer minutesPerSample) throws ApiException {
        okhttp3.Call call = getEdgeLinkQualitySeriesValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, start, include, end, minutesPerSample, null, null);
        Type localVarReturnType = new TypeToken<LinkQualityStatsTimeSeriesResource>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch link quality timeseries for an Edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param minutesPerSample An integer value specifying time slice value for link quality events time series data (optional, default to 5)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEdgeLinkQualitySeriesAsync(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, Integer end, Integer minutesPerSample, final ApiCallback<LinkQualityStatsTimeSeriesResource> callback) throws ApiException {

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

        okhttp3.Call call = getEdgeLinkQualitySeriesValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, start, include, end, minutesPerSample, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<LinkQualityStatsTimeSeriesResource>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEdgeLinkSeries
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics metrics supported for querying linkStats (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEdgeLinkSeriesCall(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, String metrics, String sortBy, Integer end, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/linkStats/timeSeries"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (metrics != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("metrics", metrics));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));

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
    private okhttp3.Call getEdgeLinkSeriesValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, String metrics, String sortBy, Integer end, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEdgeLinkSeries(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEdgeLinkSeries(Async)");
        }
        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getEdgeLinkSeries(Async)");
        }
        
        okhttp3.Call call = getEdgeLinkSeriesCall(enterpriseLogicalId, edgeLogicalId, start, include, metrics, sortBy, end, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch edge linkStats time series &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics metrics supported for querying linkStats (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @return LinkStatsSeries
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public LinkStatsSeries getEdgeLinkSeries(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, String metrics, String sortBy, Integer end) throws ApiException {
        ApiResponse<LinkStatsSeries> resp = getEdgeLinkSeriesWithHttpInfo(enterpriseLogicalId, edgeLogicalId, start, include, metrics, sortBy, end);
        return resp.getData();
    }

    /**
     * 
     * Fetch edge linkStats time series &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics metrics supported for querying linkStats (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @return ApiResponse&lt;LinkStatsSeries&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<LinkStatsSeries> getEdgeLinkSeriesWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, String metrics, String sortBy, Integer end) throws ApiException {
        okhttp3.Call call = getEdgeLinkSeriesValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, start, include, metrics, sortBy, end, null, null);
        Type localVarReturnType = new TypeToken<LinkStatsSeries>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch edge linkStats time series &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics metrics supported for querying linkStats (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEdgeLinkSeriesAsync(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, String metrics, String sortBy, Integer end, final ApiCallback<LinkStatsSeries> callback) throws ApiException {

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

        okhttp3.Call call = getEdgeLinkSeriesValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, start, include, metrics, sortBy, end, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<LinkStatsSeries>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
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
     * Build call for getEdgePathMetrics
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param peerLogicalId The &#x60;logicalId&#x60; GUID for the path stats peer (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param metrics metrics supported for querying pathStats (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEdgePathMetricsCall(String enterpriseLogicalId, String edgeLogicalId, String peerLogicalId, String include, Integer start, Integer end, String metrics, Integer limit, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/pathStats"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));
        if (metrics != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("metrics", metrics));
        if (limit != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("limit", limit));
        if (peerLogicalId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("peerLogicalId", peerLogicalId));

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
    private okhttp3.Call getEdgePathMetricsValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, String peerLogicalId, String include, Integer start, Integer end, String metrics, Integer limit, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEdgePathMetrics(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEdgePathMetrics(Async)");
        }
        // verify the required parameter 'peerLogicalId' is set
        if (peerLogicalId == null) {
            throw new ApiException("Missing the required parameter 'peerLogicalId' when calling getEdgePathMetrics(Async)");
        }
        
        okhttp3.Call call = getEdgePathMetricsCall(enterpriseLogicalId, edgeLogicalId, peerLogicalId, include, start, end, metrics, limit, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch path stats metrics from an edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param peerLogicalId The &#x60;logicalId&#x60; GUID for the path stats peer (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param metrics metrics supported for querying pathStats (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @return EdgePathStatsMetrics
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EdgePathStatsMetrics getEdgePathMetrics(String enterpriseLogicalId, String edgeLogicalId, String peerLogicalId, String include, Integer start, Integer end, String metrics, Integer limit) throws ApiException {
        ApiResponse<EdgePathStatsMetrics> resp = getEdgePathMetricsWithHttpInfo(enterpriseLogicalId, edgeLogicalId, peerLogicalId, include, start, end, metrics, limit);
        return resp.getData();
    }

    /**
     * 
     * Fetch path stats metrics from an edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param peerLogicalId The &#x60;logicalId&#x60; GUID for the path stats peer (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param metrics metrics supported for querying pathStats (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @return ApiResponse&lt;EdgePathStatsMetrics&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EdgePathStatsMetrics> getEdgePathMetricsWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, String peerLogicalId, String include, Integer start, Integer end, String metrics, Integer limit) throws ApiException {
        okhttp3.Call call = getEdgePathMetricsValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, peerLogicalId, include, start, end, metrics, limit, null, null);
        Type localVarReturnType = new TypeToken<EdgePathStatsMetrics>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch path stats metrics from an edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param peerLogicalId The &#x60;logicalId&#x60; GUID for the path stats peer (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param metrics metrics supported for querying pathStats (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEdgePathMetricsAsync(String enterpriseLogicalId, String edgeLogicalId, String peerLogicalId, String include, Integer start, Integer end, String metrics, Integer limit, final ApiCallback<EdgePathStatsMetrics> callback) throws ApiException {

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

        okhttp3.Call call = getEdgePathMetricsValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, peerLogicalId, include, start, end, metrics, limit, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EdgePathStatsMetrics>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEdgePathSeries
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param peerLogicalId The &#x60;logicalId&#x60; GUID for the path stats peer (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param maxSamples An integer value specifying the max samples for path stats (optional, default to 1024)
     * @param metrics metrics supported for querying pathStats (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (optional, default to path)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEdgePathSeriesCall(String enterpriseLogicalId, String edgeLogicalId, String peerLogicalId, String include, Integer start, Integer end, Integer maxSamples, String metrics, Integer limit, String groupBy, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/pathStats/timeSeries"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (peerLogicalId != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("peerLogicalId", peerLogicalId));
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));
        if (maxSamples != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("maxSamples", maxSamples));
        if (metrics != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("metrics", metrics));
        if (limit != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("limit", limit));
        if (groupBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("groupBy", groupBy));

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
    private okhttp3.Call getEdgePathSeriesValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, String peerLogicalId, String include, Integer start, Integer end, Integer maxSamples, String metrics, Integer limit, String groupBy, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEdgePathSeries(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEdgePathSeries(Async)");
        }
        // verify the required parameter 'peerLogicalId' is set
        if (peerLogicalId == null) {
            throw new ApiException("Missing the required parameter 'peerLogicalId' when calling getEdgePathSeries(Async)");
        }
        
        okhttp3.Call call = getEdgePathSeriesCall(enterpriseLogicalId, edgeLogicalId, peerLogicalId, include, start, end, maxSamples, metrics, limit, groupBy, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch path stats time series from an edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param peerLogicalId The &#x60;logicalId&#x60; GUID for the path stats peer (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param maxSamples An integer value specifying the max samples for path stats (optional, default to 1024)
     * @param metrics metrics supported for querying pathStats (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (optional, default to path)
     * @return EdgePathStatsSeries
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EdgePathStatsSeries getEdgePathSeries(String enterpriseLogicalId, String edgeLogicalId, String peerLogicalId, String include, Integer start, Integer end, Integer maxSamples, String metrics, Integer limit, String groupBy) throws ApiException {
        ApiResponse<EdgePathStatsSeries> resp = getEdgePathSeriesWithHttpInfo(enterpriseLogicalId, edgeLogicalId, peerLogicalId, include, start, end, maxSamples, metrics, limit, groupBy);
        return resp.getData();
    }

    /**
     * 
     * Fetch path stats time series from an edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param peerLogicalId The &#x60;logicalId&#x60; GUID for the path stats peer (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param maxSamples An integer value specifying the max samples for path stats (optional, default to 1024)
     * @param metrics metrics supported for querying pathStats (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (optional, default to path)
     * @return ApiResponse&lt;EdgePathStatsSeries&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EdgePathStatsSeries> getEdgePathSeriesWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, String peerLogicalId, String include, Integer start, Integer end, Integer maxSamples, String metrics, Integer limit, String groupBy) throws ApiException {
        okhttp3.Call call = getEdgePathSeriesValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, peerLogicalId, include, start, end, maxSamples, metrics, limit, groupBy, null, null);
        Type localVarReturnType = new TypeToken<EdgePathStatsSeries>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch path stats time series from an edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param peerLogicalId The &#x60;logicalId&#x60; GUID for the path stats peer (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param maxSamples An integer value specifying the max samples for path stats (optional, default to 1024)
     * @param metrics metrics supported for querying pathStats (optional)
     * @param limit Limits the maximum size of the result set. (optional)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (optional, default to path)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEdgePathSeriesAsync(String enterpriseLogicalId, String edgeLogicalId, String peerLogicalId, String include, Integer start, Integer end, Integer maxSamples, String metrics, Integer limit, String groupBy, final ApiCallback<EdgePathStatsSeries> callback) throws ApiException {

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

        okhttp3.Call call = getEdgePathSeriesValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, peerLogicalId, include, start, end, maxSamples, metrics, limit, groupBy, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EdgePathStatsSeries>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEnterprise
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseCall(String enterpriseLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/"
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
    private okhttp3.Call getEnterpriseValidateBeforeCall(String enterpriseLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterprise(Async)");
        }
        
        okhttp3.Call call = getEnterpriseCall(enterpriseLogicalId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch a customer &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return EnterpriseResource
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EnterpriseResource getEnterprise(String enterpriseLogicalId, String include) throws ApiException {
        ApiResponse<EnterpriseResource> resp = getEnterpriseWithHttpInfo(enterpriseLogicalId, include);
        return resp.getData();
    }

    /**
     * 
     * Fetch a customer &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;EnterpriseResource&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EnterpriseResource> getEnterpriseWithHttpInfo(String enterpriseLogicalId, String include) throws ApiException {
        okhttp3.Call call = getEnterpriseValidateBeforeCall(enterpriseLogicalId, include, null, null);
        Type localVarReturnType = new TypeToken<EnterpriseResource>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch a customer &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseAsync(String enterpriseLogicalId, String include, final ApiCallback<EnterpriseResource> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseValidateBeforeCall(enterpriseLogicalId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EnterpriseResource>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEnterpriseAggregateHealthStats
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseAggregateHealthStatsCall(String enterpriseLogicalId, String include, String metrics, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/healthStats"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (metrics != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("metrics", metrics));

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
    private okhttp3.Call getEnterpriseAggregateHealthStatsValidateBeforeCall(String enterpriseLogicalId, String include, String metrics, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseAggregateHealthStats(Async)");
        }
        
        okhttp3.Call call = getEnterpriseAggregateHealthStatsCall(enterpriseLogicalId, include, metrics, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch aggregate Edge health metrics for all Edges belonging to the target customer &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics  (optional)
     * @return EnterpriseHealthStatsSchema
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EnterpriseHealthStatsSchema getEnterpriseAggregateHealthStats(String enterpriseLogicalId, String include, String metrics) throws ApiException {
        ApiResponse<EnterpriseHealthStatsSchema> resp = getEnterpriseAggregateHealthStatsWithHttpInfo(enterpriseLogicalId, include, metrics);
        return resp.getData();
    }

    /**
     * 
     * Fetch aggregate Edge health metrics for all Edges belonging to the target customer &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics  (optional)
     * @return ApiResponse&lt;EnterpriseHealthStatsSchema&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EnterpriseHealthStatsSchema> getEnterpriseAggregateHealthStatsWithHttpInfo(String enterpriseLogicalId, String include, String metrics) throws ApiException {
        okhttp3.Call call = getEnterpriseAggregateHealthStatsValidateBeforeCall(enterpriseLogicalId, include, metrics, null, null);
        Type localVarReturnType = new TypeToken<EnterpriseHealthStatsSchema>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch aggregate Edge health metrics for all Edges belonging to the target customer &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseAggregateHealthStatsAsync(String enterpriseLogicalId, String include, String metrics, final ApiCallback<EnterpriseHealthStatsSchema> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseAggregateHealthStatsValidateBeforeCall(enterpriseLogicalId, include, metrics, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EnterpriseHealthStatsSchema>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
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
    /**
     * Build call for getEnterpriseApplicationById
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param appId The &#x60;appId&#x60; for the target application (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseApplicationByIdCall(String enterpriseLogicalId, Integer appId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/applications/{appId}"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "appId" + "\\}", apiClient.escapeString(appId.toString()));

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
    private okhttp3.Call getEnterpriseApplicationByIdValidateBeforeCall(String enterpriseLogicalId, Integer appId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseApplicationById(Async)");
        }
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling getEnterpriseApplicationById(Async)");
        }
        
        okhttp3.Call call = getEnterpriseApplicationByIdCall(enterpriseLogicalId, appId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch enterprise applications &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param appId The &#x60;appId&#x60; for the target application (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return Applications
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Applications getEnterpriseApplicationById(String enterpriseLogicalId, Integer appId, String include) throws ApiException {
        ApiResponse<Applications> resp = getEnterpriseApplicationByIdWithHttpInfo(enterpriseLogicalId, appId, include);
        return resp.getData();
    }

    /**
     * 
     * Fetch enterprise applications &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param appId The &#x60;appId&#x60; for the target application (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;Applications&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Applications> getEnterpriseApplicationByIdWithHttpInfo(String enterpriseLogicalId, Integer appId, String include) throws ApiException {
        okhttp3.Call call = getEnterpriseApplicationByIdValidateBeforeCall(enterpriseLogicalId, appId, include, null, null);
        Type localVarReturnType = new TypeToken<Applications>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch enterprise applications &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param appId The &#x60;appId&#x60; for the target application (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseApplicationByIdAsync(String enterpriseLogicalId, Integer appId, String include, final ApiCallback<Applications> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseApplicationByIdValidateBeforeCall(enterpriseLogicalId, appId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Applications>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEnterpriseApplications
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseApplicationsCall(String enterpriseLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/applications"
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
    private okhttp3.Call getEnterpriseApplicationsValidateBeforeCall(String enterpriseLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseApplications(Async)");
        }
        
        okhttp3.Call call = getEnterpriseApplicationsCall(enterpriseLogicalId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch enterprise applications &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return Applications
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Applications getEnterpriseApplications(String enterpriseLogicalId, String include) throws ApiException {
        ApiResponse<Applications> resp = getEnterpriseApplicationsWithHttpInfo(enterpriseLogicalId, include);
        return resp.getData();
    }

    /**
     * 
     * Fetch enterprise applications &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;Applications&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Applications> getEnterpriseApplicationsWithHttpInfo(String enterpriseLogicalId, String include) throws ApiException {
        okhttp3.Call call = getEnterpriseApplicationsValidateBeforeCall(enterpriseLogicalId, include, null, null);
        Type localVarReturnType = new TypeToken<Applications>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch enterprise applications &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseApplicationsAsync(String enterpriseLogicalId, String include, final ApiCallback<Applications> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseApplicationsValidateBeforeCall(enterpriseLogicalId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<Applications>(){}.getType();
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
     * Build call for getEnterpriseClientDevice
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseClientDeviceCall(String enterpriseLogicalId, String logicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/clientDevices/{logicalId}"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "logicalId" + "\\}", apiClient.escapeString(logicalId.toString()));

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
    private okhttp3.Call getEnterpriseClientDeviceValidateBeforeCall(String enterpriseLogicalId, String logicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseClientDevice(Async)");
        }
        // verify the required parameter 'logicalId' is set
        if (logicalId == null) {
            throw new ApiException("Missing the required parameter 'logicalId' when calling getEnterpriseClientDevice(Async)");
        }
        
        okhttp3.Call call = getEnterpriseClientDeviceCall(enterpriseLogicalId, logicalId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch a client device &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ClientDeviceResource
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ClientDeviceResource getEnterpriseClientDevice(String enterpriseLogicalId, String logicalId, String include) throws ApiException {
        ApiResponse<ClientDeviceResource> resp = getEnterpriseClientDeviceWithHttpInfo(enterpriseLogicalId, logicalId, include);
        return resp.getData();
    }

    /**
     * 
     * Fetch a client device &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;ClientDeviceResource&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ClientDeviceResource> getEnterpriseClientDeviceWithHttpInfo(String enterpriseLogicalId, String logicalId, String include) throws ApiException {
        okhttp3.Call call = getEnterpriseClientDeviceValidateBeforeCall(enterpriseLogicalId, logicalId, include, null, null);
        Type localVarReturnType = new TypeToken<ClientDeviceResource>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch a client device &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseClientDeviceAsync(String enterpriseLogicalId, String logicalId, String include, final ApiCallback<ClientDeviceResource> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseClientDeviceValidateBeforeCall(enterpriseLogicalId, logicalId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ClientDeviceResource>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEnterpriseClientDevices
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseClientDevicesCall(String enterpriseLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/clientDevices"
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
    private okhttp3.Call getEnterpriseClientDevicesValidateBeforeCall(String enterpriseLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseClientDevices(Async)");
        }
        
        okhttp3.Call call = getEnterpriseClientDevicesCall(enterpriseLogicalId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch all client devices &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ClientDevices
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ClientDevices getEnterpriseClientDevices(String enterpriseLogicalId, String include) throws ApiException {
        ApiResponse<ClientDevices> resp = getEnterpriseClientDevicesWithHttpInfo(enterpriseLogicalId, include);
        return resp.getData();
    }

    /**
     * 
     * Fetch all client devices &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;ClientDevices&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ClientDevices> getEnterpriseClientDevicesWithHttpInfo(String enterpriseLogicalId, String include) throws ApiException {
        okhttp3.Call call = getEnterpriseClientDevicesValidateBeforeCall(enterpriseLogicalId, include, null, null);
        Type localVarReturnType = new TypeToken<ClientDevices>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch all client devices &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseClientDevicesAsync(String enterpriseLogicalId, String include, final ApiCallback<ClientDevices> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseClientDevicesValidateBeforeCall(enterpriseLogicalId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<ClientDevices>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEnterpriseEdge
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseEdgeCall(String enterpriseLogicalId, String edgeLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "edgeLogicalId" + "\\}", apiClient.escapeString(edgeLogicalId.toString()));

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
    private okhttp3.Call getEnterpriseEdgeValidateBeforeCall(String enterpriseLogicalId, String edgeLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseEdge(Async)");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new ApiException("Missing the required parameter 'edgeLogicalId' when calling getEnterpriseEdge(Async)");
        }
        
        okhttp3.Call call = getEnterpriseEdgeCall(enterpriseLogicalId, edgeLogicalId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Get an Edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return EdgeResource
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EdgeResource getEnterpriseEdge(String enterpriseLogicalId, String edgeLogicalId, String include) throws ApiException {
        ApiResponse<EdgeResource> resp = getEnterpriseEdgeWithHttpInfo(enterpriseLogicalId, edgeLogicalId, include);
        return resp.getData();
    }

    /**
     * 
     * Get an Edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;EdgeResource&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EdgeResource> getEnterpriseEdgeWithHttpInfo(String enterpriseLogicalId, String edgeLogicalId, String include) throws ApiException {
        okhttp3.Call call = getEnterpriseEdgeValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, include, null, null);
        Type localVarReturnType = new TypeToken<EdgeResource>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Get an Edge &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseEdgeAsync(String enterpriseLogicalId, String edgeLogicalId, String include, final ApiCallback<EdgeResource> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseEdgeValidateBeforeCall(enterpriseLogicalId, edgeLogicalId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EdgeResource>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEnterpriseEdges
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param nameIs Filter by name[is] (optional)
     * @param nameIsNot Filter by name[isNot] (optional)
     * @param nameIsNull Filter by name[isNull] (optional)
     * @param nameIsNotNull Filter by name[isNotNull] (optional)
     * @param nameStartsWith Filter by name[startsWith] (optional)
     * @param nameNotStartsWith Filter by name[notStartsWith] (optional)
     * @param nameEndsWith Filter by name[endsWith] (optional)
     * @param nameNotEndsWith Filter by name[notEndsWith] (optional)
     * @param nameContains Filter by name[contains] (optional)
     * @param nameNotContains Filter by name[notContains] (optional)
     * @param nameIn Filter by name[in] (optional)
     * @param nameNotIn Filter by name[notIn] (optional)
     * @param createdIs Filter by created[is] (optional)
     * @param createdIsNot Filter by created[isNot] (optional)
     * @param createdIsNull Filter by created[isNull] (optional)
     * @param createdIsNotNull Filter by created[isNotNull] (optional)
     * @param createdGreaterOrEquals Filter by created[greaterOrEquals] (optional)
     * @param createdLesserOrEquals Filter by created[lesserOrEquals] (optional)
     * @param createdIn Filter by created[in] (optional)
     * @param createdNotIn Filter by created[notIn] (optional)
     * @param activationStateIs Filter by activationState[is] (optional)
     * @param activationStateIsNot Filter by activationState[isNot] (optional)
     * @param activationStateIn Filter by activationState[in] (optional)
     * @param activationStateNotIn Filter by activationState[notIn] (optional)
     * @param modelNumberIs Filter by modelNumber[is] (optional)
     * @param modelNumberIsNot Filter by modelNumber[isNot] (optional)
     * @param modelNumberIn Filter by modelNumber[in] (optional)
     * @param modelNumberNotIn Filter by modelNumber[notIn] (optional)
     * @param edgeStateIs Filter by edgeState[is] (optional)
     * @param edgeStateIsNot Filter by edgeState[isNot] (optional)
     * @param edgeStateIn Filter by edgeState[in] (optional)
     * @param edgeStateNotIn Filter by edgeState[notIn] (optional)
     * @param buildNumberIs Filter by buildNumber[is] (optional)
     * @param buildNumberIsNot Filter by buildNumber[isNot] (optional)
     * @param buildNumberIsNull Filter by buildNumber[isNull] (optional)
     * @param buildNumberIsNotNull Filter by buildNumber[isNotNull] (optional)
     * @param buildNumberStartsWith Filter by buildNumber[startsWith] (optional)
     * @param buildNumberNotStartsWith Filter by buildNumber[notStartsWith] (optional)
     * @param buildNumberEndsWith Filter by buildNumber[endsWith] (optional)
     * @param buildNumberNotEndsWith Filter by buildNumber[notEndsWith] (optional)
     * @param buildNumberContains Filter by buildNumber[contains] (optional)
     * @param buildNumberNotContains Filter by buildNumber[notContains] (optional)
     * @param buildNumberIn Filter by buildNumber[in] (optional)
     * @param buildNumberNotIn Filter by buildNumber[notIn] (optional)
     * @param softwareVersionIs Filter by softwareVersion[is] (optional)
     * @param softwareVersionIsNot Filter by softwareVersion[isNot] (optional)
     * @param softwareVersionIsNull Filter by softwareVersion[isNull] (optional)
     * @param softwareVersionIsNotNull Filter by softwareVersion[isNotNull] (optional)
     * @param softwareVersionStartsWith Filter by softwareVersion[startsWith] (optional)
     * @param softwareVersionNotStartsWith Filter by softwareVersion[notStartsWith] (optional)
     * @param softwareVersionEndsWith Filter by softwareVersion[endsWith] (optional)
     * @param softwareVersionNotEndsWith Filter by softwareVersion[notEndsWith] (optional)
     * @param softwareVersionContains Filter by softwareVersion[contains] (optional)
     * @param softwareVersionNotContains Filter by softwareVersion[notContains] (optional)
     * @param softwareVersionIn Filter by softwareVersion[in] (optional)
     * @param softwareVersionNotIn Filter by softwareVersion[notIn] (optional)
     * @param serialNumberIs Filter by serialNumber[is] (optional)
     * @param serialNumberIsNot Filter by serialNumber[isNot] (optional)
     * @param serialNumberIsNull Filter by serialNumber[isNull] (optional)
     * @param serialNumberIsNotNull Filter by serialNumber[isNotNull] (optional)
     * @param serialNumberStartsWith Filter by serialNumber[startsWith] (optional)
     * @param serialNumberNotStartsWith Filter by serialNumber[notStartsWith] (optional)
     * @param serialNumberEndsWith Filter by serialNumber[endsWith] (optional)
     * @param serialNumberNotEndsWith Filter by serialNumber[notEndsWith] (optional)
     * @param serialNumberContains Filter by serialNumber[contains] (optional)
     * @param serialNumberNotContains Filter by serialNumber[notContains] (optional)
     * @param serialNumberIn Filter by serialNumber[in] (optional)
     * @param serialNumberNotIn Filter by serialNumber[notIn] (optional)
     * @param softwareUpdatedIs Filter by softwareUpdated[is] (optional)
     * @param softwareUpdatedIsNot Filter by softwareUpdated[isNot] (optional)
     * @param softwareUpdatedIsNull Filter by softwareUpdated[isNull] (optional)
     * @param softwareUpdatedIsNotNull Filter by softwareUpdated[isNotNull] (optional)
     * @param softwareUpdatedGreaterOrEquals Filter by softwareUpdated[greaterOrEquals] (optional)
     * @param softwareUpdatedLesserOrEquals Filter by softwareUpdated[lesserOrEquals] (optional)
     * @param softwareUpdatedIn Filter by softwareUpdated[in] (optional)
     * @param softwareUpdatedNotIn Filter by softwareUpdated[notIn] (optional)
     * @param customInfoIs Filter by customInfo[is] (optional)
     * @param customInfoIsNot Filter by customInfo[isNot] (optional)
     * @param customInfoIsNull Filter by customInfo[isNull] (optional)
     * @param customInfoIsNotNull Filter by customInfo[isNotNull] (optional)
     * @param customInfoStartsWith Filter by customInfo[startsWith] (optional)
     * @param customInfoNotStartsWith Filter by customInfo[notStartsWith] (optional)
     * @param customInfoEndsWith Filter by customInfo[endsWith] (optional)
     * @param customInfoNotEndsWith Filter by customInfo[notEndsWith] (optional)
     * @param customInfoContains Filter by customInfo[contains] (optional)
     * @param customInfoNotContains Filter by customInfo[notContains] (optional)
     * @param customInfoIn Filter by customInfo[in] (optional)
     * @param customInfoNotIn Filter by customInfo[notIn] (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseEdgesCall(String enterpriseLogicalId, String include, String nameIs, String nameIsNot, String nameIsNull, String nameIsNotNull, String nameStartsWith, String nameNotStartsWith, String nameEndsWith, String nameNotEndsWith, String nameContains, String nameNotContains, String nameIn, String nameNotIn, Integer createdIs, Integer createdIsNot, Integer createdIsNull, Integer createdIsNotNull, Integer createdGreaterOrEquals, Integer createdLesserOrEquals, Integer createdIn, Integer createdNotIn, String activationStateIs, String activationStateIsNot, String activationStateIn, String activationStateNotIn, String modelNumberIs, String modelNumberIsNot, String modelNumberIn, String modelNumberNotIn, String edgeStateIs, String edgeStateIsNot, String edgeStateIn, String edgeStateNotIn, String buildNumberIs, String buildNumberIsNot, String buildNumberIsNull, String buildNumberIsNotNull, String buildNumberStartsWith, String buildNumberNotStartsWith, String buildNumberEndsWith, String buildNumberNotEndsWith, String buildNumberContains, String buildNumberNotContains, String buildNumberIn, String buildNumberNotIn, String softwareVersionIs, String softwareVersionIsNot, String softwareVersionIsNull, String softwareVersionIsNotNull, String softwareVersionStartsWith, String softwareVersionNotStartsWith, String softwareVersionEndsWith, String softwareVersionNotEndsWith, String softwareVersionContains, String softwareVersionNotContains, String softwareVersionIn, String softwareVersionNotIn, String serialNumberIs, String serialNumberIsNot, String serialNumberIsNull, String serialNumberIsNotNull, String serialNumberStartsWith, String serialNumberNotStartsWith, String serialNumberEndsWith, String serialNumberNotEndsWith, String serialNumberContains, String serialNumberNotContains, String serialNumberIn, String serialNumberNotIn, Integer softwareUpdatedIs, Integer softwareUpdatedIsNot, Integer softwareUpdatedIsNull, Integer softwareUpdatedIsNotNull, Integer softwareUpdatedGreaterOrEquals, Integer softwareUpdatedLesserOrEquals, Integer softwareUpdatedIn, Integer softwareUpdatedNotIn, String customInfoIs, String customInfoIsNot, String customInfoIsNull, String customInfoIsNotNull, String customInfoStartsWith, String customInfoNotStartsWith, String customInfoEndsWith, String customInfoNotEndsWith, String customInfoContains, String customInfoNotContains, String customInfoIn, String customInfoNotIn, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (nameIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[is]", nameIs));
        if (nameIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[isNot]", nameIsNot));
        if (nameIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[isNull]", nameIsNull));
        if (nameIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[isNotNull]", nameIsNotNull));
        if (nameStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[startsWith]", nameStartsWith));
        if (nameNotStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[notStartsWith]", nameNotStartsWith));
        if (nameEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[endsWith]", nameEndsWith));
        if (nameNotEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[notEndsWith]", nameNotEndsWith));
        if (nameContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[contains]", nameContains));
        if (nameNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[notContains]", nameNotContains));
        if (nameIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[in]", nameIn));
        if (nameNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[notIn]", nameNotIn));
        if (createdIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("created[is]", createdIs));
        if (createdIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("created[isNot]", createdIsNot));
        if (createdIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("created[isNull]", createdIsNull));
        if (createdIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("created[isNotNull]", createdIsNotNull));
        if (createdGreaterOrEquals != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("created[greaterOrEquals]", createdGreaterOrEquals));
        if (createdLesserOrEquals != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("created[lesserOrEquals]", createdLesserOrEquals));
        if (createdIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("created[in]", createdIn));
        if (createdNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("created[notIn]", createdNotIn));
        if (activationStateIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("activationState[is]", activationStateIs));
        if (activationStateIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("activationState[isNot]", activationStateIsNot));
        if (activationStateIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("activationState[in]", activationStateIn));
        if (activationStateNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("activationState[notIn]", activationStateNotIn));
        if (modelNumberIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("modelNumber[is]", modelNumberIs));
        if (modelNumberIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("modelNumber[isNot]", modelNumberIsNot));
        if (modelNumberIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("modelNumber[in]", modelNumberIn));
        if (modelNumberNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("modelNumber[notIn]", modelNumberNotIn));
        if (edgeStateIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeState[is]", edgeStateIs));
        if (edgeStateIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeState[isNot]", edgeStateIsNot));
        if (edgeStateIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeState[in]", edgeStateIn));
        if (edgeStateNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("edgeState[notIn]", edgeStateNotIn));
        if (buildNumberIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("buildNumber[is]", buildNumberIs));
        if (buildNumberIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("buildNumber[isNot]", buildNumberIsNot));
        if (buildNumberIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("buildNumber[isNull]", buildNumberIsNull));
        if (buildNumberIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("buildNumber[isNotNull]", buildNumberIsNotNull));
        if (buildNumberStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("buildNumber[startsWith]", buildNumberStartsWith));
        if (buildNumberNotStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("buildNumber[notStartsWith]", buildNumberNotStartsWith));
        if (buildNumberEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("buildNumber[endsWith]", buildNumberEndsWith));
        if (buildNumberNotEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("buildNumber[notEndsWith]", buildNumberNotEndsWith));
        if (buildNumberContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("buildNumber[contains]", buildNumberContains));
        if (buildNumberNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("buildNumber[notContains]", buildNumberNotContains));
        if (buildNumberIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("buildNumber[in]", buildNumberIn));
        if (buildNumberNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("buildNumber[notIn]", buildNumberNotIn));
        if (softwareVersionIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareVersion[is]", softwareVersionIs));
        if (softwareVersionIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareVersion[isNot]", softwareVersionIsNot));
        if (softwareVersionIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareVersion[isNull]", softwareVersionIsNull));
        if (softwareVersionIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareVersion[isNotNull]", softwareVersionIsNotNull));
        if (softwareVersionStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareVersion[startsWith]", softwareVersionStartsWith));
        if (softwareVersionNotStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareVersion[notStartsWith]", softwareVersionNotStartsWith));
        if (softwareVersionEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareVersion[endsWith]", softwareVersionEndsWith));
        if (softwareVersionNotEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareVersion[notEndsWith]", softwareVersionNotEndsWith));
        if (softwareVersionContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareVersion[contains]", softwareVersionContains));
        if (softwareVersionNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareVersion[notContains]", softwareVersionNotContains));
        if (softwareVersionIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareVersion[in]", softwareVersionIn));
        if (softwareVersionNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareVersion[notIn]", softwareVersionNotIn));
        if (serialNumberIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("serialNumber[is]", serialNumberIs));
        if (serialNumberIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("serialNumber[isNot]", serialNumberIsNot));
        if (serialNumberIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("serialNumber[isNull]", serialNumberIsNull));
        if (serialNumberIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("serialNumber[isNotNull]", serialNumberIsNotNull));
        if (serialNumberStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("serialNumber[startsWith]", serialNumberStartsWith));
        if (serialNumberNotStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("serialNumber[notStartsWith]", serialNumberNotStartsWith));
        if (serialNumberEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("serialNumber[endsWith]", serialNumberEndsWith));
        if (serialNumberNotEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("serialNumber[notEndsWith]", serialNumberNotEndsWith));
        if (serialNumberContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("serialNumber[contains]", serialNumberContains));
        if (serialNumberNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("serialNumber[notContains]", serialNumberNotContains));
        if (serialNumberIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("serialNumber[in]", serialNumberIn));
        if (serialNumberNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("serialNumber[notIn]", serialNumberNotIn));
        if (softwareUpdatedIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareUpdated[is]", softwareUpdatedIs));
        if (softwareUpdatedIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareUpdated[isNot]", softwareUpdatedIsNot));
        if (softwareUpdatedIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareUpdated[isNull]", softwareUpdatedIsNull));
        if (softwareUpdatedIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareUpdated[isNotNull]", softwareUpdatedIsNotNull));
        if (softwareUpdatedGreaterOrEquals != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareUpdated[greaterOrEquals]", softwareUpdatedGreaterOrEquals));
        if (softwareUpdatedLesserOrEquals != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareUpdated[lesserOrEquals]", softwareUpdatedLesserOrEquals));
        if (softwareUpdatedIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareUpdated[in]", softwareUpdatedIn));
        if (softwareUpdatedNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("softwareUpdated[notIn]", softwareUpdatedNotIn));
        if (customInfoIs != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("customInfo[is]", customInfoIs));
        if (customInfoIsNot != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("customInfo[isNot]", customInfoIsNot));
        if (customInfoIsNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("customInfo[isNull]", customInfoIsNull));
        if (customInfoIsNotNull != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("customInfo[isNotNull]", customInfoIsNotNull));
        if (customInfoStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("customInfo[startsWith]", customInfoStartsWith));
        if (customInfoNotStartsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("customInfo[notStartsWith]", customInfoNotStartsWith));
        if (customInfoEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("customInfo[endsWith]", customInfoEndsWith));
        if (customInfoNotEndsWith != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("customInfo[notEndsWith]", customInfoNotEndsWith));
        if (customInfoContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("customInfo[contains]", customInfoContains));
        if (customInfoNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("customInfo[notContains]", customInfoNotContains));
        if (customInfoIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("customInfo[in]", customInfoIn));
        if (customInfoNotIn != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("customInfo[notIn]", customInfoNotIn));

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
    private okhttp3.Call getEnterpriseEdgesValidateBeforeCall(String enterpriseLogicalId, String include, String nameIs, String nameIsNot, String nameIsNull, String nameIsNotNull, String nameStartsWith, String nameNotStartsWith, String nameEndsWith, String nameNotEndsWith, String nameContains, String nameNotContains, String nameIn, String nameNotIn, Integer createdIs, Integer createdIsNot, Integer createdIsNull, Integer createdIsNotNull, Integer createdGreaterOrEquals, Integer createdLesserOrEquals, Integer createdIn, Integer createdNotIn, String activationStateIs, String activationStateIsNot, String activationStateIn, String activationStateNotIn, String modelNumberIs, String modelNumberIsNot, String modelNumberIn, String modelNumberNotIn, String edgeStateIs, String edgeStateIsNot, String edgeStateIn, String edgeStateNotIn, String buildNumberIs, String buildNumberIsNot, String buildNumberIsNull, String buildNumberIsNotNull, String buildNumberStartsWith, String buildNumberNotStartsWith, String buildNumberEndsWith, String buildNumberNotEndsWith, String buildNumberContains, String buildNumberNotContains, String buildNumberIn, String buildNumberNotIn, String softwareVersionIs, String softwareVersionIsNot, String softwareVersionIsNull, String softwareVersionIsNotNull, String softwareVersionStartsWith, String softwareVersionNotStartsWith, String softwareVersionEndsWith, String softwareVersionNotEndsWith, String softwareVersionContains, String softwareVersionNotContains, String softwareVersionIn, String softwareVersionNotIn, String serialNumberIs, String serialNumberIsNot, String serialNumberIsNull, String serialNumberIsNotNull, String serialNumberStartsWith, String serialNumberNotStartsWith, String serialNumberEndsWith, String serialNumberNotEndsWith, String serialNumberContains, String serialNumberNotContains, String serialNumberIn, String serialNumberNotIn, Integer softwareUpdatedIs, Integer softwareUpdatedIsNot, Integer softwareUpdatedIsNull, Integer softwareUpdatedIsNotNull, Integer softwareUpdatedGreaterOrEquals, Integer softwareUpdatedLesserOrEquals, Integer softwareUpdatedIn, Integer softwareUpdatedNotIn, String customInfoIs, String customInfoIsNot, String customInfoIsNull, String customInfoIsNotNull, String customInfoStartsWith, String customInfoNotStartsWith, String customInfoEndsWith, String customInfoNotEndsWith, String customInfoContains, String customInfoNotContains, String customInfoIn, String customInfoNotIn, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseEdges(Async)");
        }
        
        okhttp3.Call call = getEnterpriseEdgesCall(enterpriseLogicalId, include, nameIs, nameIsNot, nameIsNull, nameIsNotNull, nameStartsWith, nameNotStartsWith, nameEndsWith, nameNotEndsWith, nameContains, nameNotContains, nameIn, nameNotIn, createdIs, createdIsNot, createdIsNull, createdIsNotNull, createdGreaterOrEquals, createdLesserOrEquals, createdIn, createdNotIn, activationStateIs, activationStateIsNot, activationStateIn, activationStateNotIn, modelNumberIs, modelNumberIsNot, modelNumberIn, modelNumberNotIn, edgeStateIs, edgeStateIsNot, edgeStateIn, edgeStateNotIn, buildNumberIs, buildNumberIsNot, buildNumberIsNull, buildNumberIsNotNull, buildNumberStartsWith, buildNumberNotStartsWith, buildNumberEndsWith, buildNumberNotEndsWith, buildNumberContains, buildNumberNotContains, buildNumberIn, buildNumberNotIn, softwareVersionIs, softwareVersionIsNot, softwareVersionIsNull, softwareVersionIsNotNull, softwareVersionStartsWith, softwareVersionNotStartsWith, softwareVersionEndsWith, softwareVersionNotEndsWith, softwareVersionContains, softwareVersionNotContains, softwareVersionIn, softwareVersionNotIn, serialNumberIs, serialNumberIsNot, serialNumberIsNull, serialNumberIsNotNull, serialNumberStartsWith, serialNumberNotStartsWith, serialNumberEndsWith, serialNumberNotEndsWith, serialNumberContains, serialNumberNotContains, serialNumberIn, serialNumberNotIn, softwareUpdatedIs, softwareUpdatedIsNot, softwareUpdatedIsNull, softwareUpdatedIsNotNull, softwareUpdatedGreaterOrEquals, softwareUpdatedLesserOrEquals, softwareUpdatedIn, softwareUpdatedNotIn, customInfoIs, customInfoIsNot, customInfoIsNull, customInfoIsNotNull, customInfoStartsWith, customInfoNotStartsWith, customInfoEndsWith, customInfoNotEndsWith, customInfoContains, customInfoNotContains, customInfoIn, customInfoNotIn, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * List Customer Edges &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param nameIs Filter by name[is] (optional)
     * @param nameIsNot Filter by name[isNot] (optional)
     * @param nameIsNull Filter by name[isNull] (optional)
     * @param nameIsNotNull Filter by name[isNotNull] (optional)
     * @param nameStartsWith Filter by name[startsWith] (optional)
     * @param nameNotStartsWith Filter by name[notStartsWith] (optional)
     * @param nameEndsWith Filter by name[endsWith] (optional)
     * @param nameNotEndsWith Filter by name[notEndsWith] (optional)
     * @param nameContains Filter by name[contains] (optional)
     * @param nameNotContains Filter by name[notContains] (optional)
     * @param nameIn Filter by name[in] (optional)
     * @param nameNotIn Filter by name[notIn] (optional)
     * @param createdIs Filter by created[is] (optional)
     * @param createdIsNot Filter by created[isNot] (optional)
     * @param createdIsNull Filter by created[isNull] (optional)
     * @param createdIsNotNull Filter by created[isNotNull] (optional)
     * @param createdGreaterOrEquals Filter by created[greaterOrEquals] (optional)
     * @param createdLesserOrEquals Filter by created[lesserOrEquals] (optional)
     * @param createdIn Filter by created[in] (optional)
     * @param createdNotIn Filter by created[notIn] (optional)
     * @param activationStateIs Filter by activationState[is] (optional)
     * @param activationStateIsNot Filter by activationState[isNot] (optional)
     * @param activationStateIn Filter by activationState[in] (optional)
     * @param activationStateNotIn Filter by activationState[notIn] (optional)
     * @param modelNumberIs Filter by modelNumber[is] (optional)
     * @param modelNumberIsNot Filter by modelNumber[isNot] (optional)
     * @param modelNumberIn Filter by modelNumber[in] (optional)
     * @param modelNumberNotIn Filter by modelNumber[notIn] (optional)
     * @param edgeStateIs Filter by edgeState[is] (optional)
     * @param edgeStateIsNot Filter by edgeState[isNot] (optional)
     * @param edgeStateIn Filter by edgeState[in] (optional)
     * @param edgeStateNotIn Filter by edgeState[notIn] (optional)
     * @param buildNumberIs Filter by buildNumber[is] (optional)
     * @param buildNumberIsNot Filter by buildNumber[isNot] (optional)
     * @param buildNumberIsNull Filter by buildNumber[isNull] (optional)
     * @param buildNumberIsNotNull Filter by buildNumber[isNotNull] (optional)
     * @param buildNumberStartsWith Filter by buildNumber[startsWith] (optional)
     * @param buildNumberNotStartsWith Filter by buildNumber[notStartsWith] (optional)
     * @param buildNumberEndsWith Filter by buildNumber[endsWith] (optional)
     * @param buildNumberNotEndsWith Filter by buildNumber[notEndsWith] (optional)
     * @param buildNumberContains Filter by buildNumber[contains] (optional)
     * @param buildNumberNotContains Filter by buildNumber[notContains] (optional)
     * @param buildNumberIn Filter by buildNumber[in] (optional)
     * @param buildNumberNotIn Filter by buildNumber[notIn] (optional)
     * @param softwareVersionIs Filter by softwareVersion[is] (optional)
     * @param softwareVersionIsNot Filter by softwareVersion[isNot] (optional)
     * @param softwareVersionIsNull Filter by softwareVersion[isNull] (optional)
     * @param softwareVersionIsNotNull Filter by softwareVersion[isNotNull] (optional)
     * @param softwareVersionStartsWith Filter by softwareVersion[startsWith] (optional)
     * @param softwareVersionNotStartsWith Filter by softwareVersion[notStartsWith] (optional)
     * @param softwareVersionEndsWith Filter by softwareVersion[endsWith] (optional)
     * @param softwareVersionNotEndsWith Filter by softwareVersion[notEndsWith] (optional)
     * @param softwareVersionContains Filter by softwareVersion[contains] (optional)
     * @param softwareVersionNotContains Filter by softwareVersion[notContains] (optional)
     * @param softwareVersionIn Filter by softwareVersion[in] (optional)
     * @param softwareVersionNotIn Filter by softwareVersion[notIn] (optional)
     * @param serialNumberIs Filter by serialNumber[is] (optional)
     * @param serialNumberIsNot Filter by serialNumber[isNot] (optional)
     * @param serialNumberIsNull Filter by serialNumber[isNull] (optional)
     * @param serialNumberIsNotNull Filter by serialNumber[isNotNull] (optional)
     * @param serialNumberStartsWith Filter by serialNumber[startsWith] (optional)
     * @param serialNumberNotStartsWith Filter by serialNumber[notStartsWith] (optional)
     * @param serialNumberEndsWith Filter by serialNumber[endsWith] (optional)
     * @param serialNumberNotEndsWith Filter by serialNumber[notEndsWith] (optional)
     * @param serialNumberContains Filter by serialNumber[contains] (optional)
     * @param serialNumberNotContains Filter by serialNumber[notContains] (optional)
     * @param serialNumberIn Filter by serialNumber[in] (optional)
     * @param serialNumberNotIn Filter by serialNumber[notIn] (optional)
     * @param softwareUpdatedIs Filter by softwareUpdated[is] (optional)
     * @param softwareUpdatedIsNot Filter by softwareUpdated[isNot] (optional)
     * @param softwareUpdatedIsNull Filter by softwareUpdated[isNull] (optional)
     * @param softwareUpdatedIsNotNull Filter by softwareUpdated[isNotNull] (optional)
     * @param softwareUpdatedGreaterOrEquals Filter by softwareUpdated[greaterOrEquals] (optional)
     * @param softwareUpdatedLesserOrEquals Filter by softwareUpdated[lesserOrEquals] (optional)
     * @param softwareUpdatedIn Filter by softwareUpdated[in] (optional)
     * @param softwareUpdatedNotIn Filter by softwareUpdated[notIn] (optional)
     * @param customInfoIs Filter by customInfo[is] (optional)
     * @param customInfoIsNot Filter by customInfo[isNot] (optional)
     * @param customInfoIsNull Filter by customInfo[isNull] (optional)
     * @param customInfoIsNotNull Filter by customInfo[isNotNull] (optional)
     * @param customInfoStartsWith Filter by customInfo[startsWith] (optional)
     * @param customInfoNotStartsWith Filter by customInfo[notStartsWith] (optional)
     * @param customInfoEndsWith Filter by customInfo[endsWith] (optional)
     * @param customInfoNotEndsWith Filter by customInfo[notEndsWith] (optional)
     * @param customInfoContains Filter by customInfo[contains] (optional)
     * @param customInfoNotContains Filter by customInfo[notContains] (optional)
     * @param customInfoIn Filter by customInfo[in] (optional)
     * @param customInfoNotIn Filter by customInfo[notIn] (optional)
     * @return EdgeResourceCollection
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EdgeResourceCollection getEnterpriseEdges(String enterpriseLogicalId, String include, String nameIs, String nameIsNot, String nameIsNull, String nameIsNotNull, String nameStartsWith, String nameNotStartsWith, String nameEndsWith, String nameNotEndsWith, String nameContains, String nameNotContains, String nameIn, String nameNotIn, Integer createdIs, Integer createdIsNot, Integer createdIsNull, Integer createdIsNotNull, Integer createdGreaterOrEquals, Integer createdLesserOrEquals, Integer createdIn, Integer createdNotIn, String activationStateIs, String activationStateIsNot, String activationStateIn, String activationStateNotIn, String modelNumberIs, String modelNumberIsNot, String modelNumberIn, String modelNumberNotIn, String edgeStateIs, String edgeStateIsNot, String edgeStateIn, String edgeStateNotIn, String buildNumberIs, String buildNumberIsNot, String buildNumberIsNull, String buildNumberIsNotNull, String buildNumberStartsWith, String buildNumberNotStartsWith, String buildNumberEndsWith, String buildNumberNotEndsWith, String buildNumberContains, String buildNumberNotContains, String buildNumberIn, String buildNumberNotIn, String softwareVersionIs, String softwareVersionIsNot, String softwareVersionIsNull, String softwareVersionIsNotNull, String softwareVersionStartsWith, String softwareVersionNotStartsWith, String softwareVersionEndsWith, String softwareVersionNotEndsWith, String softwareVersionContains, String softwareVersionNotContains, String softwareVersionIn, String softwareVersionNotIn, String serialNumberIs, String serialNumberIsNot, String serialNumberIsNull, String serialNumberIsNotNull, String serialNumberStartsWith, String serialNumberNotStartsWith, String serialNumberEndsWith, String serialNumberNotEndsWith, String serialNumberContains, String serialNumberNotContains, String serialNumberIn, String serialNumberNotIn, Integer softwareUpdatedIs, Integer softwareUpdatedIsNot, Integer softwareUpdatedIsNull, Integer softwareUpdatedIsNotNull, Integer softwareUpdatedGreaterOrEquals, Integer softwareUpdatedLesserOrEquals, Integer softwareUpdatedIn, Integer softwareUpdatedNotIn, String customInfoIs, String customInfoIsNot, String customInfoIsNull, String customInfoIsNotNull, String customInfoStartsWith, String customInfoNotStartsWith, String customInfoEndsWith, String customInfoNotEndsWith, String customInfoContains, String customInfoNotContains, String customInfoIn, String customInfoNotIn) throws ApiException {
        ApiResponse<EdgeResourceCollection> resp = getEnterpriseEdgesWithHttpInfo(enterpriseLogicalId, include, nameIs, nameIsNot, nameIsNull, nameIsNotNull, nameStartsWith, nameNotStartsWith, nameEndsWith, nameNotEndsWith, nameContains, nameNotContains, nameIn, nameNotIn, createdIs, createdIsNot, createdIsNull, createdIsNotNull, createdGreaterOrEquals, createdLesserOrEquals, createdIn, createdNotIn, activationStateIs, activationStateIsNot, activationStateIn, activationStateNotIn, modelNumberIs, modelNumberIsNot, modelNumberIn, modelNumberNotIn, edgeStateIs, edgeStateIsNot, edgeStateIn, edgeStateNotIn, buildNumberIs, buildNumberIsNot, buildNumberIsNull, buildNumberIsNotNull, buildNumberStartsWith, buildNumberNotStartsWith, buildNumberEndsWith, buildNumberNotEndsWith, buildNumberContains, buildNumberNotContains, buildNumberIn, buildNumberNotIn, softwareVersionIs, softwareVersionIsNot, softwareVersionIsNull, softwareVersionIsNotNull, softwareVersionStartsWith, softwareVersionNotStartsWith, softwareVersionEndsWith, softwareVersionNotEndsWith, softwareVersionContains, softwareVersionNotContains, softwareVersionIn, softwareVersionNotIn, serialNumberIs, serialNumberIsNot, serialNumberIsNull, serialNumberIsNotNull, serialNumberStartsWith, serialNumberNotStartsWith, serialNumberEndsWith, serialNumberNotEndsWith, serialNumberContains, serialNumberNotContains, serialNumberIn, serialNumberNotIn, softwareUpdatedIs, softwareUpdatedIsNot, softwareUpdatedIsNull, softwareUpdatedIsNotNull, softwareUpdatedGreaterOrEquals, softwareUpdatedLesserOrEquals, softwareUpdatedIn, softwareUpdatedNotIn, customInfoIs, customInfoIsNot, customInfoIsNull, customInfoIsNotNull, customInfoStartsWith, customInfoNotStartsWith, customInfoEndsWith, customInfoNotEndsWith, customInfoContains, customInfoNotContains, customInfoIn, customInfoNotIn);
        return resp.getData();
    }

    /**
     * 
     * List Customer Edges &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param nameIs Filter by name[is] (optional)
     * @param nameIsNot Filter by name[isNot] (optional)
     * @param nameIsNull Filter by name[isNull] (optional)
     * @param nameIsNotNull Filter by name[isNotNull] (optional)
     * @param nameStartsWith Filter by name[startsWith] (optional)
     * @param nameNotStartsWith Filter by name[notStartsWith] (optional)
     * @param nameEndsWith Filter by name[endsWith] (optional)
     * @param nameNotEndsWith Filter by name[notEndsWith] (optional)
     * @param nameContains Filter by name[contains] (optional)
     * @param nameNotContains Filter by name[notContains] (optional)
     * @param nameIn Filter by name[in] (optional)
     * @param nameNotIn Filter by name[notIn] (optional)
     * @param createdIs Filter by created[is] (optional)
     * @param createdIsNot Filter by created[isNot] (optional)
     * @param createdIsNull Filter by created[isNull] (optional)
     * @param createdIsNotNull Filter by created[isNotNull] (optional)
     * @param createdGreaterOrEquals Filter by created[greaterOrEquals] (optional)
     * @param createdLesserOrEquals Filter by created[lesserOrEquals] (optional)
     * @param createdIn Filter by created[in] (optional)
     * @param createdNotIn Filter by created[notIn] (optional)
     * @param activationStateIs Filter by activationState[is] (optional)
     * @param activationStateIsNot Filter by activationState[isNot] (optional)
     * @param activationStateIn Filter by activationState[in] (optional)
     * @param activationStateNotIn Filter by activationState[notIn] (optional)
     * @param modelNumberIs Filter by modelNumber[is] (optional)
     * @param modelNumberIsNot Filter by modelNumber[isNot] (optional)
     * @param modelNumberIn Filter by modelNumber[in] (optional)
     * @param modelNumberNotIn Filter by modelNumber[notIn] (optional)
     * @param edgeStateIs Filter by edgeState[is] (optional)
     * @param edgeStateIsNot Filter by edgeState[isNot] (optional)
     * @param edgeStateIn Filter by edgeState[in] (optional)
     * @param edgeStateNotIn Filter by edgeState[notIn] (optional)
     * @param buildNumberIs Filter by buildNumber[is] (optional)
     * @param buildNumberIsNot Filter by buildNumber[isNot] (optional)
     * @param buildNumberIsNull Filter by buildNumber[isNull] (optional)
     * @param buildNumberIsNotNull Filter by buildNumber[isNotNull] (optional)
     * @param buildNumberStartsWith Filter by buildNumber[startsWith] (optional)
     * @param buildNumberNotStartsWith Filter by buildNumber[notStartsWith] (optional)
     * @param buildNumberEndsWith Filter by buildNumber[endsWith] (optional)
     * @param buildNumberNotEndsWith Filter by buildNumber[notEndsWith] (optional)
     * @param buildNumberContains Filter by buildNumber[contains] (optional)
     * @param buildNumberNotContains Filter by buildNumber[notContains] (optional)
     * @param buildNumberIn Filter by buildNumber[in] (optional)
     * @param buildNumberNotIn Filter by buildNumber[notIn] (optional)
     * @param softwareVersionIs Filter by softwareVersion[is] (optional)
     * @param softwareVersionIsNot Filter by softwareVersion[isNot] (optional)
     * @param softwareVersionIsNull Filter by softwareVersion[isNull] (optional)
     * @param softwareVersionIsNotNull Filter by softwareVersion[isNotNull] (optional)
     * @param softwareVersionStartsWith Filter by softwareVersion[startsWith] (optional)
     * @param softwareVersionNotStartsWith Filter by softwareVersion[notStartsWith] (optional)
     * @param softwareVersionEndsWith Filter by softwareVersion[endsWith] (optional)
     * @param softwareVersionNotEndsWith Filter by softwareVersion[notEndsWith] (optional)
     * @param softwareVersionContains Filter by softwareVersion[contains] (optional)
     * @param softwareVersionNotContains Filter by softwareVersion[notContains] (optional)
     * @param softwareVersionIn Filter by softwareVersion[in] (optional)
     * @param softwareVersionNotIn Filter by softwareVersion[notIn] (optional)
     * @param serialNumberIs Filter by serialNumber[is] (optional)
     * @param serialNumberIsNot Filter by serialNumber[isNot] (optional)
     * @param serialNumberIsNull Filter by serialNumber[isNull] (optional)
     * @param serialNumberIsNotNull Filter by serialNumber[isNotNull] (optional)
     * @param serialNumberStartsWith Filter by serialNumber[startsWith] (optional)
     * @param serialNumberNotStartsWith Filter by serialNumber[notStartsWith] (optional)
     * @param serialNumberEndsWith Filter by serialNumber[endsWith] (optional)
     * @param serialNumberNotEndsWith Filter by serialNumber[notEndsWith] (optional)
     * @param serialNumberContains Filter by serialNumber[contains] (optional)
     * @param serialNumberNotContains Filter by serialNumber[notContains] (optional)
     * @param serialNumberIn Filter by serialNumber[in] (optional)
     * @param serialNumberNotIn Filter by serialNumber[notIn] (optional)
     * @param softwareUpdatedIs Filter by softwareUpdated[is] (optional)
     * @param softwareUpdatedIsNot Filter by softwareUpdated[isNot] (optional)
     * @param softwareUpdatedIsNull Filter by softwareUpdated[isNull] (optional)
     * @param softwareUpdatedIsNotNull Filter by softwareUpdated[isNotNull] (optional)
     * @param softwareUpdatedGreaterOrEquals Filter by softwareUpdated[greaterOrEquals] (optional)
     * @param softwareUpdatedLesserOrEquals Filter by softwareUpdated[lesserOrEquals] (optional)
     * @param softwareUpdatedIn Filter by softwareUpdated[in] (optional)
     * @param softwareUpdatedNotIn Filter by softwareUpdated[notIn] (optional)
     * @param customInfoIs Filter by customInfo[is] (optional)
     * @param customInfoIsNot Filter by customInfo[isNot] (optional)
     * @param customInfoIsNull Filter by customInfo[isNull] (optional)
     * @param customInfoIsNotNull Filter by customInfo[isNotNull] (optional)
     * @param customInfoStartsWith Filter by customInfo[startsWith] (optional)
     * @param customInfoNotStartsWith Filter by customInfo[notStartsWith] (optional)
     * @param customInfoEndsWith Filter by customInfo[endsWith] (optional)
     * @param customInfoNotEndsWith Filter by customInfo[notEndsWith] (optional)
     * @param customInfoContains Filter by customInfo[contains] (optional)
     * @param customInfoNotContains Filter by customInfo[notContains] (optional)
     * @param customInfoIn Filter by customInfo[in] (optional)
     * @param customInfoNotIn Filter by customInfo[notIn] (optional)
     * @return ApiResponse&lt;EdgeResourceCollection&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EdgeResourceCollection> getEnterpriseEdgesWithHttpInfo(String enterpriseLogicalId, String include, String nameIs, String nameIsNot, String nameIsNull, String nameIsNotNull, String nameStartsWith, String nameNotStartsWith, String nameEndsWith, String nameNotEndsWith, String nameContains, String nameNotContains, String nameIn, String nameNotIn, Integer createdIs, Integer createdIsNot, Integer createdIsNull, Integer createdIsNotNull, Integer createdGreaterOrEquals, Integer createdLesserOrEquals, Integer createdIn, Integer createdNotIn, String activationStateIs, String activationStateIsNot, String activationStateIn, String activationStateNotIn, String modelNumberIs, String modelNumberIsNot, String modelNumberIn, String modelNumberNotIn, String edgeStateIs, String edgeStateIsNot, String edgeStateIn, String edgeStateNotIn, String buildNumberIs, String buildNumberIsNot, String buildNumberIsNull, String buildNumberIsNotNull, String buildNumberStartsWith, String buildNumberNotStartsWith, String buildNumberEndsWith, String buildNumberNotEndsWith, String buildNumberContains, String buildNumberNotContains, String buildNumberIn, String buildNumberNotIn, String softwareVersionIs, String softwareVersionIsNot, String softwareVersionIsNull, String softwareVersionIsNotNull, String softwareVersionStartsWith, String softwareVersionNotStartsWith, String softwareVersionEndsWith, String softwareVersionNotEndsWith, String softwareVersionContains, String softwareVersionNotContains, String softwareVersionIn, String softwareVersionNotIn, String serialNumberIs, String serialNumberIsNot, String serialNumberIsNull, String serialNumberIsNotNull, String serialNumberStartsWith, String serialNumberNotStartsWith, String serialNumberEndsWith, String serialNumberNotEndsWith, String serialNumberContains, String serialNumberNotContains, String serialNumberIn, String serialNumberNotIn, Integer softwareUpdatedIs, Integer softwareUpdatedIsNot, Integer softwareUpdatedIsNull, Integer softwareUpdatedIsNotNull, Integer softwareUpdatedGreaterOrEquals, Integer softwareUpdatedLesserOrEquals, Integer softwareUpdatedIn, Integer softwareUpdatedNotIn, String customInfoIs, String customInfoIsNot, String customInfoIsNull, String customInfoIsNotNull, String customInfoStartsWith, String customInfoNotStartsWith, String customInfoEndsWith, String customInfoNotEndsWith, String customInfoContains, String customInfoNotContains, String customInfoIn, String customInfoNotIn) throws ApiException {
        okhttp3.Call call = getEnterpriseEdgesValidateBeforeCall(enterpriseLogicalId, include, nameIs, nameIsNot, nameIsNull, nameIsNotNull, nameStartsWith, nameNotStartsWith, nameEndsWith, nameNotEndsWith, nameContains, nameNotContains, nameIn, nameNotIn, createdIs, createdIsNot, createdIsNull, createdIsNotNull, createdGreaterOrEquals, createdLesserOrEquals, createdIn, createdNotIn, activationStateIs, activationStateIsNot, activationStateIn, activationStateNotIn, modelNumberIs, modelNumberIsNot, modelNumberIn, modelNumberNotIn, edgeStateIs, edgeStateIsNot, edgeStateIn, edgeStateNotIn, buildNumberIs, buildNumberIsNot, buildNumberIsNull, buildNumberIsNotNull, buildNumberStartsWith, buildNumberNotStartsWith, buildNumberEndsWith, buildNumberNotEndsWith, buildNumberContains, buildNumberNotContains, buildNumberIn, buildNumberNotIn, softwareVersionIs, softwareVersionIsNot, softwareVersionIsNull, softwareVersionIsNotNull, softwareVersionStartsWith, softwareVersionNotStartsWith, softwareVersionEndsWith, softwareVersionNotEndsWith, softwareVersionContains, softwareVersionNotContains, softwareVersionIn, softwareVersionNotIn, serialNumberIs, serialNumberIsNot, serialNumberIsNull, serialNumberIsNotNull, serialNumberStartsWith, serialNumberNotStartsWith, serialNumberEndsWith, serialNumberNotEndsWith, serialNumberContains, serialNumberNotContains, serialNumberIn, serialNumberNotIn, softwareUpdatedIs, softwareUpdatedIsNot, softwareUpdatedIsNull, softwareUpdatedIsNotNull, softwareUpdatedGreaterOrEquals, softwareUpdatedLesserOrEquals, softwareUpdatedIn, softwareUpdatedNotIn, customInfoIs, customInfoIsNot, customInfoIsNull, customInfoIsNotNull, customInfoStartsWith, customInfoNotStartsWith, customInfoEndsWith, customInfoNotEndsWith, customInfoContains, customInfoNotContains, customInfoIn, customInfoNotIn, null, null);
        Type localVarReturnType = new TypeToken<EdgeResourceCollection>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * List Customer Edges &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param nameIs Filter by name[is] (optional)
     * @param nameIsNot Filter by name[isNot] (optional)
     * @param nameIsNull Filter by name[isNull] (optional)
     * @param nameIsNotNull Filter by name[isNotNull] (optional)
     * @param nameStartsWith Filter by name[startsWith] (optional)
     * @param nameNotStartsWith Filter by name[notStartsWith] (optional)
     * @param nameEndsWith Filter by name[endsWith] (optional)
     * @param nameNotEndsWith Filter by name[notEndsWith] (optional)
     * @param nameContains Filter by name[contains] (optional)
     * @param nameNotContains Filter by name[notContains] (optional)
     * @param nameIn Filter by name[in] (optional)
     * @param nameNotIn Filter by name[notIn] (optional)
     * @param createdIs Filter by created[is] (optional)
     * @param createdIsNot Filter by created[isNot] (optional)
     * @param createdIsNull Filter by created[isNull] (optional)
     * @param createdIsNotNull Filter by created[isNotNull] (optional)
     * @param createdGreaterOrEquals Filter by created[greaterOrEquals] (optional)
     * @param createdLesserOrEquals Filter by created[lesserOrEquals] (optional)
     * @param createdIn Filter by created[in] (optional)
     * @param createdNotIn Filter by created[notIn] (optional)
     * @param activationStateIs Filter by activationState[is] (optional)
     * @param activationStateIsNot Filter by activationState[isNot] (optional)
     * @param activationStateIn Filter by activationState[in] (optional)
     * @param activationStateNotIn Filter by activationState[notIn] (optional)
     * @param modelNumberIs Filter by modelNumber[is] (optional)
     * @param modelNumberIsNot Filter by modelNumber[isNot] (optional)
     * @param modelNumberIn Filter by modelNumber[in] (optional)
     * @param modelNumberNotIn Filter by modelNumber[notIn] (optional)
     * @param edgeStateIs Filter by edgeState[is] (optional)
     * @param edgeStateIsNot Filter by edgeState[isNot] (optional)
     * @param edgeStateIn Filter by edgeState[in] (optional)
     * @param edgeStateNotIn Filter by edgeState[notIn] (optional)
     * @param buildNumberIs Filter by buildNumber[is] (optional)
     * @param buildNumberIsNot Filter by buildNumber[isNot] (optional)
     * @param buildNumberIsNull Filter by buildNumber[isNull] (optional)
     * @param buildNumberIsNotNull Filter by buildNumber[isNotNull] (optional)
     * @param buildNumberStartsWith Filter by buildNumber[startsWith] (optional)
     * @param buildNumberNotStartsWith Filter by buildNumber[notStartsWith] (optional)
     * @param buildNumberEndsWith Filter by buildNumber[endsWith] (optional)
     * @param buildNumberNotEndsWith Filter by buildNumber[notEndsWith] (optional)
     * @param buildNumberContains Filter by buildNumber[contains] (optional)
     * @param buildNumberNotContains Filter by buildNumber[notContains] (optional)
     * @param buildNumberIn Filter by buildNumber[in] (optional)
     * @param buildNumberNotIn Filter by buildNumber[notIn] (optional)
     * @param softwareVersionIs Filter by softwareVersion[is] (optional)
     * @param softwareVersionIsNot Filter by softwareVersion[isNot] (optional)
     * @param softwareVersionIsNull Filter by softwareVersion[isNull] (optional)
     * @param softwareVersionIsNotNull Filter by softwareVersion[isNotNull] (optional)
     * @param softwareVersionStartsWith Filter by softwareVersion[startsWith] (optional)
     * @param softwareVersionNotStartsWith Filter by softwareVersion[notStartsWith] (optional)
     * @param softwareVersionEndsWith Filter by softwareVersion[endsWith] (optional)
     * @param softwareVersionNotEndsWith Filter by softwareVersion[notEndsWith] (optional)
     * @param softwareVersionContains Filter by softwareVersion[contains] (optional)
     * @param softwareVersionNotContains Filter by softwareVersion[notContains] (optional)
     * @param softwareVersionIn Filter by softwareVersion[in] (optional)
     * @param softwareVersionNotIn Filter by softwareVersion[notIn] (optional)
     * @param serialNumberIs Filter by serialNumber[is] (optional)
     * @param serialNumberIsNot Filter by serialNumber[isNot] (optional)
     * @param serialNumberIsNull Filter by serialNumber[isNull] (optional)
     * @param serialNumberIsNotNull Filter by serialNumber[isNotNull] (optional)
     * @param serialNumberStartsWith Filter by serialNumber[startsWith] (optional)
     * @param serialNumberNotStartsWith Filter by serialNumber[notStartsWith] (optional)
     * @param serialNumberEndsWith Filter by serialNumber[endsWith] (optional)
     * @param serialNumberNotEndsWith Filter by serialNumber[notEndsWith] (optional)
     * @param serialNumberContains Filter by serialNumber[contains] (optional)
     * @param serialNumberNotContains Filter by serialNumber[notContains] (optional)
     * @param serialNumberIn Filter by serialNumber[in] (optional)
     * @param serialNumberNotIn Filter by serialNumber[notIn] (optional)
     * @param softwareUpdatedIs Filter by softwareUpdated[is] (optional)
     * @param softwareUpdatedIsNot Filter by softwareUpdated[isNot] (optional)
     * @param softwareUpdatedIsNull Filter by softwareUpdated[isNull] (optional)
     * @param softwareUpdatedIsNotNull Filter by softwareUpdated[isNotNull] (optional)
     * @param softwareUpdatedGreaterOrEquals Filter by softwareUpdated[greaterOrEquals] (optional)
     * @param softwareUpdatedLesserOrEquals Filter by softwareUpdated[lesserOrEquals] (optional)
     * @param softwareUpdatedIn Filter by softwareUpdated[in] (optional)
     * @param softwareUpdatedNotIn Filter by softwareUpdated[notIn] (optional)
     * @param customInfoIs Filter by customInfo[is] (optional)
     * @param customInfoIsNot Filter by customInfo[isNot] (optional)
     * @param customInfoIsNull Filter by customInfo[isNull] (optional)
     * @param customInfoIsNotNull Filter by customInfo[isNotNull] (optional)
     * @param customInfoStartsWith Filter by customInfo[startsWith] (optional)
     * @param customInfoNotStartsWith Filter by customInfo[notStartsWith] (optional)
     * @param customInfoEndsWith Filter by customInfo[endsWith] (optional)
     * @param customInfoNotEndsWith Filter by customInfo[notEndsWith] (optional)
     * @param customInfoContains Filter by customInfo[contains] (optional)
     * @param customInfoNotContains Filter by customInfo[notContains] (optional)
     * @param customInfoIn Filter by customInfo[in] (optional)
     * @param customInfoNotIn Filter by customInfo[notIn] (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseEdgesAsync(String enterpriseLogicalId, String include, String nameIs, String nameIsNot, String nameIsNull, String nameIsNotNull, String nameStartsWith, String nameNotStartsWith, String nameEndsWith, String nameNotEndsWith, String nameContains, String nameNotContains, String nameIn, String nameNotIn, Integer createdIs, Integer createdIsNot, Integer createdIsNull, Integer createdIsNotNull, Integer createdGreaterOrEquals, Integer createdLesserOrEquals, Integer createdIn, Integer createdNotIn, String activationStateIs, String activationStateIsNot, String activationStateIn, String activationStateNotIn, String modelNumberIs, String modelNumberIsNot, String modelNumberIn, String modelNumberNotIn, String edgeStateIs, String edgeStateIsNot, String edgeStateIn, String edgeStateNotIn, String buildNumberIs, String buildNumberIsNot, String buildNumberIsNull, String buildNumberIsNotNull, String buildNumberStartsWith, String buildNumberNotStartsWith, String buildNumberEndsWith, String buildNumberNotEndsWith, String buildNumberContains, String buildNumberNotContains, String buildNumberIn, String buildNumberNotIn, String softwareVersionIs, String softwareVersionIsNot, String softwareVersionIsNull, String softwareVersionIsNotNull, String softwareVersionStartsWith, String softwareVersionNotStartsWith, String softwareVersionEndsWith, String softwareVersionNotEndsWith, String softwareVersionContains, String softwareVersionNotContains, String softwareVersionIn, String softwareVersionNotIn, String serialNumberIs, String serialNumberIsNot, String serialNumberIsNull, String serialNumberIsNotNull, String serialNumberStartsWith, String serialNumberNotStartsWith, String serialNumberEndsWith, String serialNumberNotEndsWith, String serialNumberContains, String serialNumberNotContains, String serialNumberIn, String serialNumberNotIn, Integer softwareUpdatedIs, Integer softwareUpdatedIsNot, Integer softwareUpdatedIsNull, Integer softwareUpdatedIsNotNull, Integer softwareUpdatedGreaterOrEquals, Integer softwareUpdatedLesserOrEquals, Integer softwareUpdatedIn, Integer softwareUpdatedNotIn, String customInfoIs, String customInfoIsNot, String customInfoIsNull, String customInfoIsNotNull, String customInfoStartsWith, String customInfoNotStartsWith, String customInfoEndsWith, String customInfoNotEndsWith, String customInfoContains, String customInfoNotContains, String customInfoIn, String customInfoNotIn, final ApiCallback<EdgeResourceCollection> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseEdgesValidateBeforeCall(enterpriseLogicalId, include, nameIs, nameIsNot, nameIsNull, nameIsNotNull, nameStartsWith, nameNotStartsWith, nameEndsWith, nameNotEndsWith, nameContains, nameNotContains, nameIn, nameNotIn, createdIs, createdIsNot, createdIsNull, createdIsNotNull, createdGreaterOrEquals, createdLesserOrEquals, createdIn, createdNotIn, activationStateIs, activationStateIsNot, activationStateIn, activationStateNotIn, modelNumberIs, modelNumberIsNot, modelNumberIn, modelNumberNotIn, edgeStateIs, edgeStateIsNot, edgeStateIn, edgeStateNotIn, buildNumberIs, buildNumberIsNot, buildNumberIsNull, buildNumberIsNotNull, buildNumberStartsWith, buildNumberNotStartsWith, buildNumberEndsWith, buildNumberNotEndsWith, buildNumberContains, buildNumberNotContains, buildNumberIn, buildNumberNotIn, softwareVersionIs, softwareVersionIsNot, softwareVersionIsNull, softwareVersionIsNotNull, softwareVersionStartsWith, softwareVersionNotStartsWith, softwareVersionEndsWith, softwareVersionNotEndsWith, softwareVersionContains, softwareVersionNotContains, softwareVersionIn, softwareVersionNotIn, serialNumberIs, serialNumberIsNot, serialNumberIsNull, serialNumberIsNotNull, serialNumberStartsWith, serialNumberNotStartsWith, serialNumberEndsWith, serialNumberNotEndsWith, serialNumberContains, serialNumberNotContains, serialNumberIn, serialNumberNotIn, softwareUpdatedIs, softwareUpdatedIsNot, softwareUpdatedIsNull, softwareUpdatedIsNotNull, softwareUpdatedGreaterOrEquals, softwareUpdatedLesserOrEquals, softwareUpdatedIn, softwareUpdatedNotIn, customInfoIs, customInfoIsNot, customInfoIsNull, customInfoIsNotNull, customInfoStartsWith, customInfoNotStartsWith, customInfoEndsWith, customInfoNotEndsWith, customInfoContains, customInfoNotContains, customInfoIn, customInfoNotIn, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EdgeResourceCollection>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
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
    /**
     * Build call for getEnterpriseFlowMetrics
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param metrics metrics supported for querying flowStats (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseFlowMetricsCall(String enterpriseLogicalId, String groupBy, Integer start, String include, Integer end, String sortBy, String metrics, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/flowStats"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (groupBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("groupBy", groupBy));
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (metrics != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("metrics", metrics));

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
    private okhttp3.Call getEnterpriseFlowMetricsValidateBeforeCall(String enterpriseLogicalId, String groupBy, Integer start, String include, Integer end, String sortBy, String metrics, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseFlowMetrics(Async)");
        }
        // verify the required parameter 'groupBy' is set
        if (groupBy == null) {
            throw new ApiException("Missing the required parameter 'groupBy' when calling getEnterpriseFlowMetrics(Async)");
        }
        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getEnterpriseFlowMetrics(Async)");
        }
        
        okhttp3.Call call = getEnterpriseFlowMetricsCall(enterpriseLogicalId, groupBy, start, include, end, sortBy, metrics, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch Customer-global network flow summary statistics &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param metrics metrics supported for querying flowStats (optional)
     * @return FlowStats
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FlowStats getEnterpriseFlowMetrics(String enterpriseLogicalId, String groupBy, Integer start, String include, Integer end, String sortBy, String metrics) throws ApiException {
        ApiResponse<FlowStats> resp = getEnterpriseFlowMetricsWithHttpInfo(enterpriseLogicalId, groupBy, start, include, end, sortBy, metrics);
        return resp.getData();
    }

    /**
     * 
     * Fetch Customer-global network flow summary statistics &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param metrics metrics supported for querying flowStats (optional)
     * @return ApiResponse&lt;FlowStats&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FlowStats> getEnterpriseFlowMetricsWithHttpInfo(String enterpriseLogicalId, String groupBy, Integer start, String include, Integer end, String sortBy, String metrics) throws ApiException {
        okhttp3.Call call = getEnterpriseFlowMetricsValidateBeforeCall(enterpriseLogicalId, groupBy, start, include, end, sortBy, metrics, null, null);
        Type localVarReturnType = new TypeToken<FlowStats>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch Customer-global network flow summary statistics &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported. (required)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param metrics metrics supported for querying flowStats (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseFlowMetricsAsync(String enterpriseLogicalId, String groupBy, Integer start, String include, Integer end, String sortBy, String metrics, final ApiCallback<FlowStats> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseFlowMetricsValidateBeforeCall(enterpriseLogicalId, groupBy, start, include, end, sortBy, metrics, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FlowStats>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getEnterpriseLinkMetrics
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics metrics supported for querying linkStats (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseLinkMetricsCall(String enterpriseLogicalId, String include, String metrics, String sortBy, Integer start, Integer end, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/linkStats"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (metrics != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("metrics", metrics));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("sortBy", sortBy));
        if (start != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("start", start));
        if (end != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("end", end));

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
    private okhttp3.Call getEnterpriseLinkMetricsValidateBeforeCall(String enterpriseLogicalId, String include, String metrics, String sortBy, Integer start, Integer end, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseLinkMetrics(Async)");
        }
        
        okhttp3.Call call = getEnterpriseLinkMetricsCall(enterpriseLogicalId, include, metrics, sortBy, start, end, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Fetch aggregate WAN link transport metrics for all Customer links &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics metrics supported for querying linkStats (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @return LinkStats
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public LinkStats getEnterpriseLinkMetrics(String enterpriseLogicalId, String include, String metrics, String sortBy, Integer start, Integer end) throws ApiException {
        ApiResponse<LinkStats> resp = getEnterpriseLinkMetricsWithHttpInfo(enterpriseLogicalId, include, metrics, sortBy, start, end);
        return resp.getData();
    }

    /**
     * 
     * Fetch aggregate WAN link transport metrics for all Customer links &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics metrics supported for querying linkStats (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @return ApiResponse&lt;LinkStats&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<LinkStats> getEnterpriseLinkMetricsWithHttpInfo(String enterpriseLogicalId, String include, String metrics, String sortBy, Integer start, Integer end) throws ApiException {
        okhttp3.Call call = getEnterpriseLinkMetricsValidateBeforeCall(enterpriseLogicalId, include, metrics, sortBy, start, end, null, null);
        Type localVarReturnType = new TypeToken<LinkStats>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch aggregate WAN link transport metrics for all Customer links &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param metrics metrics supported for querying linkStats (optional)
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt; (optional)
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseLinkMetricsAsync(String enterpriseLogicalId, String include, String metrics, String sortBy, Integer start, Integer end, final ApiCallback<LinkStats> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseLinkMetricsValidateBeforeCall(enterpriseLogicalId, include, metrics, sortBy, start, end, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<LinkStats>(){}.getType();
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
     * Build call for getEnterprises
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param nameContains Filter by name[contains] (optional)
     * @param nameNotContains Filter by name[notContains] (optional)
     * @param accountNumberContains Filter by accountNumber[contains] (optional)
     * @param accountNumberNotContains Filter by accountNumber[notContains] (optional)
     * @param domainContains Filter by domain[contains] (optional)
     * @param domainNotContains Filter by domain[notContains] (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterprisesCall(String include, String nameContains, String nameNotContains, String accountNumberContains, String accountNumberNotContains, String domainContains, String domainNotContains, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (include != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include", include));
        if (nameContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[contains]", nameContains));
        if (nameNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("name[notContains]", nameNotContains));
        if (accountNumberContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("accountNumber[contains]", accountNumberContains));
        if (accountNumberNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("accountNumber[notContains]", accountNumberNotContains));
        if (domainContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("domain[contains]", domainContains));
        if (domainNotContains != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("domain[notContains]", domainNotContains));

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
    private okhttp3.Call getEnterprisesValidateBeforeCall(String include, String nameContains, String nameNotContains, String accountNumberContains, String accountNumberNotContains, String domainContains, String domainNotContains, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        okhttp3.Call call = getEnterprisesCall(include, nameContains, nameNotContains, accountNumberContains, accountNumberNotContains, domainContains, domainNotContains, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * List Customers &lt;br /&gt; &lt;br /&gt; 
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param nameContains Filter by name[contains] (optional)
     * @param nameNotContains Filter by name[notContains] (optional)
     * @param accountNumberContains Filter by accountNumber[contains] (optional)
     * @param accountNumberNotContains Filter by accountNumber[notContains] (optional)
     * @param domainContains Filter by domain[contains] (optional)
     * @param domainNotContains Filter by domain[notContains] (optional)
     * @return EnterpriseResourceCollection
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EnterpriseResourceCollection getEnterprises(String include, String nameContains, String nameNotContains, String accountNumberContains, String accountNumberNotContains, String domainContains, String domainNotContains) throws ApiException {
        ApiResponse<EnterpriseResourceCollection> resp = getEnterprisesWithHttpInfo(include, nameContains, nameNotContains, accountNumberContains, accountNumberNotContains, domainContains, domainNotContains);
        return resp.getData();
    }

    /**
     * 
     * List Customers &lt;br /&gt; &lt;br /&gt; 
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param nameContains Filter by name[contains] (optional)
     * @param nameNotContains Filter by name[notContains] (optional)
     * @param accountNumberContains Filter by accountNumber[contains] (optional)
     * @param accountNumberNotContains Filter by accountNumber[notContains] (optional)
     * @param domainContains Filter by domain[contains] (optional)
     * @param domainNotContains Filter by domain[notContains] (optional)
     * @return ApiResponse&lt;EnterpriseResourceCollection&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EnterpriseResourceCollection> getEnterprisesWithHttpInfo(String include, String nameContains, String nameNotContains, String accountNumberContains, String accountNumberNotContains, String domainContains, String domainNotContains) throws ApiException {
        okhttp3.Call call = getEnterprisesValidateBeforeCall(include, nameContains, nameNotContains, accountNumberContains, accountNumberNotContains, domainContains, domainNotContains, null, null);
        Type localVarReturnType = new TypeToken<EnterpriseResourceCollection>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * List Customers &lt;br /&gt; &lt;br /&gt; 
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param nameContains Filter by name[contains] (optional)
     * @param nameNotContains Filter by name[notContains] (optional)
     * @param accountNumberContains Filter by accountNumber[contains] (optional)
     * @param accountNumberNotContains Filter by accountNumber[notContains] (optional)
     * @param domainContains Filter by domain[contains] (optional)
     * @param domainNotContains Filter by domain[notContains] (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterprisesAsync(String include, String nameContains, String nameNotContains, String accountNumberContains, String accountNumberNotContains, String domainContains, String domainNotContains, final ApiCallback<EnterpriseResourceCollection> callback) throws ApiException {

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

        okhttp3.Call call = getEnterprisesValidateBeforeCall(include, nameContains, nameNotContains, accountNumberContains, accountNumberNotContains, domainContains, domainNotContains, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EnterpriseResourceCollection>(){}.getType();
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
