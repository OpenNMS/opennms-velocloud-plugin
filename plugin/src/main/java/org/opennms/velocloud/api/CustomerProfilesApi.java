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


import org.opennms.velocloud.model.EnterpriseDeviceSettings;
import org.opennms.velocloud.model.InternalServerError;
import org.opennms.velocloud.model.ProfileDeviceSettingsRequestSpec;
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

public class CustomerProfilesApi {
    private ApiClient apiClient;

    public CustomerProfilesApi() {
        this(Configuration.getDefaultApiClient());
    }

    public CustomerProfilesApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getEnterpriseProfileDeviceSettings
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call getEnterpriseProfileDeviceSettingsCall(String enterpriseLogicalId, String profileLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/profiles/{profileLogicalId}/deviceSettings"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "profileLogicalId" + "\\}", apiClient.escapeString(profileLogicalId.toString()));

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
    private okhttp3.Call getEnterpriseProfileDeviceSettingsValidateBeforeCall(String enterpriseLogicalId, String profileLogicalId, String include, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseProfileDeviceSettings(Async)");
        }
        // verify the required parameter 'profileLogicalId' is set
        if (profileLogicalId == null) {
            throw new ApiException("Missing the required parameter 'profileLogicalId' when calling getEnterpriseProfileDeviceSettings(Async)");
        }
        
        okhttp3.Call call = getEnterpriseProfileDeviceSettingsCall(enterpriseLogicalId, profileLogicalId, include, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Profile deviceSettings module response spec &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return EnterpriseDeviceSettings
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EnterpriseDeviceSettings getEnterpriseProfileDeviceSettings(String enterpriseLogicalId, String profileLogicalId, String include) throws ApiException {
        ApiResponse<EnterpriseDeviceSettings> resp = getEnterpriseProfileDeviceSettingsWithHttpInfo(enterpriseLogicalId, profileLogicalId, include);
        return resp.getData();
    }

    /**
     * 
     * Profile deviceSettings module response spec &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @return ApiResponse&lt;EnterpriseDeviceSettings&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EnterpriseDeviceSettings> getEnterpriseProfileDeviceSettingsWithHttpInfo(String enterpriseLogicalId, String profileLogicalId, String include) throws ApiException {
        okhttp3.Call call = getEnterpriseProfileDeviceSettingsValidateBeforeCall(enterpriseLogicalId, profileLogicalId, include, null, null);
        Type localVarReturnType = new TypeToken<EnterpriseDeviceSettings>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Profile deviceSettings module response spec &lt;br /&gt; &lt;br /&gt; 
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile (required)
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources. (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call getEnterpriseProfileDeviceSettingsAsync(String enterpriseLogicalId, String profileLogicalId, String include, final ApiCallback<EnterpriseDeviceSettings> callback) throws ApiException {

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

        okhttp3.Call call = getEnterpriseProfileDeviceSettingsValidateBeforeCall(enterpriseLogicalId, profileLogicalId, include, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EnterpriseDeviceSettings>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for replaceEnterpriseProfileDeviceSettings
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile (required)
     * @param body Replace profile deviceSettings module request schema (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call replaceEnterpriseProfileDeviceSettingsCall(String enterpriseLogicalId, String profileLogicalId, ProfileDeviceSettingsRequestSpec body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/profiles/{profileLogicalId}/deviceSettings"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "profileLogicalId" + "\\}", apiClient.escapeString(profileLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private okhttp3.Call replaceEnterpriseProfileDeviceSettingsValidateBeforeCall(String enterpriseLogicalId, String profileLogicalId, ProfileDeviceSettingsRequestSpec body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling replaceEnterpriseProfileDeviceSettings(Async)");
        }
        // verify the required parameter 'profileLogicalId' is set
        if (profileLogicalId == null) {
            throw new ApiException("Missing the required parameter 'profileLogicalId' when calling replaceEnterpriseProfileDeviceSettings(Async)");
        }
        
        okhttp3.Call call = replaceEnterpriseProfileDeviceSettingsCall(enterpriseLogicalId, profileLogicalId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Profile deviceSettings module replace spec &lt;br /&gt; &lt;br /&gt; &lt;strong&gt;Note:&lt;/strong&gt; By default this API operation executes asynchronously, meaning successful requests to this method will return an HTTP 202 response including tracking resource information (incl. an HTTP Location header with a value like &#x60;/api/sdwan/v2/asyncOperations/4f951593-aff8-4f3c-9855-10fa5d32a419&#x60;). Due to a limitation of our documentation automation/tooling, we can only describe non-async API behavior, we are working on this and will update the expected behavior soon
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile (required)
     * @param body Replace profile deviceSettings module request schema (optional)
     * @return EnterpriseDeviceSettings
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EnterpriseDeviceSettings replaceEnterpriseProfileDeviceSettings(String enterpriseLogicalId, String profileLogicalId, ProfileDeviceSettingsRequestSpec body) throws ApiException {
        ApiResponse<EnterpriseDeviceSettings> resp = replaceEnterpriseProfileDeviceSettingsWithHttpInfo(enterpriseLogicalId, profileLogicalId, body);
        return resp.getData();
    }

    /**
     * 
     * Profile deviceSettings module replace spec &lt;br /&gt; &lt;br /&gt; &lt;strong&gt;Note:&lt;/strong&gt; By default this API operation executes asynchronously, meaning successful requests to this method will return an HTTP 202 response including tracking resource information (incl. an HTTP Location header with a value like &#x60;/api/sdwan/v2/asyncOperations/4f951593-aff8-4f3c-9855-10fa5d32a419&#x60;). Due to a limitation of our documentation automation/tooling, we can only describe non-async API behavior, we are working on this and will update the expected behavior soon
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile (required)
     * @param body Replace profile deviceSettings module request schema (optional)
     * @return ApiResponse&lt;EnterpriseDeviceSettings&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EnterpriseDeviceSettings> replaceEnterpriseProfileDeviceSettingsWithHttpInfo(String enterpriseLogicalId, String profileLogicalId, ProfileDeviceSettingsRequestSpec body) throws ApiException {
        okhttp3.Call call = replaceEnterpriseProfileDeviceSettingsValidateBeforeCall(enterpriseLogicalId, profileLogicalId, body, null, null);
        Type localVarReturnType = new TypeToken<EnterpriseDeviceSettings>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Profile deviceSettings module replace spec &lt;br /&gt; &lt;br /&gt; &lt;strong&gt;Note:&lt;/strong&gt; By default this API operation executes asynchronously, meaning successful requests to this method will return an HTTP 202 response including tracking resource information (incl. an HTTP Location header with a value like &#x60;/api/sdwan/v2/asyncOperations/4f951593-aff8-4f3c-9855-10fa5d32a419&#x60;). Due to a limitation of our documentation automation/tooling, we can only describe non-async API behavior, we are working on this and will update the expected behavior soon
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile (required)
     * @param body Replace profile deviceSettings module request schema (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call replaceEnterpriseProfileDeviceSettingsAsync(String enterpriseLogicalId, String profileLogicalId, ProfileDeviceSettingsRequestSpec body, final ApiCallback<EnterpriseDeviceSettings> callback) throws ApiException {

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

        okhttp3.Call call = replaceEnterpriseProfileDeviceSettingsValidateBeforeCall(enterpriseLogicalId, profileLogicalId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EnterpriseDeviceSettings>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateEnterpriseProfileDeviceSettings
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile (required)
     * @param body Update Profile request schema (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call updateEnterpriseProfileDeviceSettingsCall(String enterpriseLogicalId, String profileLogicalId, List<Object> body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/api/sdwan/v2/enterprises/{enterpriseLogicalId}/profiles/{profileLogicalId}/deviceSettings"
            .replaceAll("\\{" + "enterpriseLogicalId" + "\\}", apiClient.escapeString(enterpriseLogicalId.toString()))
            .replaceAll("\\{" + "profileLogicalId" + "\\}", apiClient.escapeString(profileLogicalId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

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
        return apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private okhttp3.Call updateEnterpriseProfileDeviceSettingsValidateBeforeCall(String enterpriseLogicalId, String profileLogicalId, List<Object> body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new ApiException("Missing the required parameter 'enterpriseLogicalId' when calling updateEnterpriseProfileDeviceSettings(Async)");
        }
        // verify the required parameter 'profileLogicalId' is set
        if (profileLogicalId == null) {
            throw new ApiException("Missing the required parameter 'profileLogicalId' when calling updateEnterpriseProfileDeviceSettings(Async)");
        }
        
        okhttp3.Call call = updateEnterpriseProfileDeviceSettingsCall(enterpriseLogicalId, profileLogicalId, body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Profile deviceSettings module update spec &lt;br /&gt; &lt;br /&gt; &lt;strong&gt;Note:&lt;/strong&gt; By default this API operation executes asynchronously, meaning successful requests to this method will return an HTTP 202 response including tracking resource information (incl. an HTTP Location header with a value like &#x60;/api/sdwan/v2/asyncOperations/4f951593-aff8-4f3c-9855-10fa5d32a419&#x60;). Due to a limitation of our documentation automation/tooling, we can only describe non-async API behavior, we are working on this and will update the expected behavior soon
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile (required)
     * @param body Update Profile request schema (optional)
     * @return EnterpriseDeviceSettings
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public EnterpriseDeviceSettings updateEnterpriseProfileDeviceSettings(String enterpriseLogicalId, String profileLogicalId, List<Object> body) throws ApiException {
        ApiResponse<EnterpriseDeviceSettings> resp = updateEnterpriseProfileDeviceSettingsWithHttpInfo(enterpriseLogicalId, profileLogicalId, body);
        return resp.getData();
    }

    /**
     * 
     * Profile deviceSettings module update spec &lt;br /&gt; &lt;br /&gt; &lt;strong&gt;Note:&lt;/strong&gt; By default this API operation executes asynchronously, meaning successful requests to this method will return an HTTP 202 response including tracking resource information (incl. an HTTP Location header with a value like &#x60;/api/sdwan/v2/asyncOperations/4f951593-aff8-4f3c-9855-10fa5d32a419&#x60;). Due to a limitation of our documentation automation/tooling, we can only describe non-async API behavior, we are working on this and will update the expected behavior soon
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile (required)
     * @param body Update Profile request schema (optional)
     * @return ApiResponse&lt;EnterpriseDeviceSettings&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<EnterpriseDeviceSettings> updateEnterpriseProfileDeviceSettingsWithHttpInfo(String enterpriseLogicalId, String profileLogicalId, List<Object> body) throws ApiException {
        okhttp3.Call call = updateEnterpriseProfileDeviceSettingsValidateBeforeCall(enterpriseLogicalId, profileLogicalId, body, null, null);
        Type localVarReturnType = new TypeToken<EnterpriseDeviceSettings>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Profile deviceSettings module update spec &lt;br /&gt; &lt;br /&gt; &lt;strong&gt;Note:&lt;/strong&gt; By default this API operation executes asynchronously, meaning successful requests to this method will return an HTTP 202 response including tracking resource information (incl. an HTTP Location header with a value like &#x60;/api/sdwan/v2/asyncOperations/4f951593-aff8-4f3c-9855-10fa5d32a419&#x60;). Due to a limitation of our documentation automation/tooling, we can only describe non-async API behavior, we are working on this and will update the expected behavior soon
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise (required)
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile (required)
     * @param body Update Profile request schema (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call updateEnterpriseProfileDeviceSettingsAsync(String enterpriseLogicalId, String profileLogicalId, List<Object> body, final ApiCallback<EnterpriseDeviceSettings> callback) throws ApiException {

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

        okhttp3.Call call = updateEnterpriseProfileDeviceSettingsValidateBeforeCall(enterpriseLogicalId, profileLogicalId, body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<EnterpriseDeviceSettings>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
