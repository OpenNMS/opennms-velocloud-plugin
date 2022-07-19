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


import org.opennms.velocloud.model.Applications;
import org.opennms.velocloud.model.CreateEnterpriseSchema;
import org.opennms.velocloud.model.DataValidationError;
import org.opennms.velocloud.model.EnterpriseHealthStatsSchema;
import org.opennms.velocloud.model.EnterpriseResource;
import org.opennms.velocloud.model.EnterpriseResourceCollection;
import org.opennms.velocloud.model.FlowStats;
import org.opennms.velocloud.model.InternalServerError;
import org.opennms.velocloud.model.LinkStats;
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

public class CustomersApi {
    private ApiClient apiClient;

    public CustomersApi() {
        this(Configuration.getDefaultApiClient());
    }

    public CustomersApi(ApiClient apiClient) {
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
}
