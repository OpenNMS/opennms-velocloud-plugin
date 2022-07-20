package org.opennms.velocloud.client.api;

import org.opennms.velocloud.client.handler.ApiClient;

import org.opennms.velocloud.client.model.DataValidationError;
import org.opennms.velocloud.client.model.EdgeDeviceSettings;
import org.opennms.velocloud.client.model.EdgePostSchema;
import org.opennms.velocloud.client.model.EdgeProfileDeviceSettingsRequestSpec;
import org.opennms.velocloud.client.model.EdgeResource;
import org.opennms.velocloud.client.model.EdgeResponseSchema;
import org.opennms.velocloud.client.model.EnterpriseDeviceSettings;
import org.opennms.velocloud.client.model.InternalServerError;
import org.opennms.velocloud.client.model.ProfileDeviceSettingsRequestSpec;
import org.opennms.velocloud.client.model.RateLimitExceededError;
import org.opennms.velocloud.client.model.ResourceNotFoundError;
import org.opennms.velocloud.client.model.UnAuthorized;
import org.opennms.velocloud.client.model.UnsupportedMediaTypeError;
import org.opennms.velocloud.client.model.ValidationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")@Component("org.opennms.velocloud.client.api.ConfigureApi")
public class ConfigureApi {
    private ApiClient apiClient;

    public ConfigureApi() {
        this(new ApiClient());
    }

    @Autowired
    public ConfigureApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * 
     * Provision a new Edge, or invoke an action on a set of existing Edges. &lt;br /&gt; &lt;br /&gt; 
     * <p><b>201</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>415</b> - UnsupportedMediaTypeError
     * <p><b>422</b> - DataValidationError
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param body Edge provision request schema
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param action An action to be applied to the list of specified &#x60;edges&#x60;.
     * @return EdgeResponseSchema
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EdgeResponseSchema createEdge(String enterpriseLogicalId, EdgePostSchema body, String include, String action) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling createEdge");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "action", action));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EdgeResponseSchema> returnType = new ParameterizedTypeReference<EdgeResponseSchema>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Delete an Edge device &lt;br /&gt; &lt;br /&gt; 
     * <p><b>204</b> - Resource deleted successfully
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteEdge(String enterpriseLogicalId, String edgeLogicalId) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling deleteEdge");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling deleteEdge");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        apiClient.invokeAPI(path, HttpMethod.DELETE, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Edge-specific device settings module response spec &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return EdgeDeviceSettings
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EdgeDeviceSettings getEdgeProfileDeviceSettings(String enterpriseLogicalId, String edgeLogicalId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgeProfileDeviceSettings");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgeProfileDeviceSettings");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/deviceSettings").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EdgeDeviceSettings> returnType = new ParameterizedTypeReference<EdgeDeviceSettings>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Profile deviceSettings module response spec &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return EnterpriseDeviceSettings
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EnterpriseDeviceSettings getEnterpriseProfileDeviceSettings(String enterpriseLogicalId, String profileLogicalId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseProfileDeviceSettings");
        }
        // verify the required parameter 'profileLogicalId' is set
        if (profileLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'profileLogicalId' when calling getEnterpriseProfileDeviceSettings");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("profileLogicalId", profileLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/profiles/{profileLogicalId}/deviceSettings").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EnterpriseDeviceSettings> returnType = new ParameterizedTypeReference<EnterpriseDeviceSettings>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Edge-specific device settings module replace spec &lt;br /&gt; &lt;br /&gt; &lt;strong&gt;Note:&lt;/strong&gt; By default this API operation executes asynchronously, meaning successful requests to this method will return an HTTP 202 response including tracking resource information (incl. an HTTP Location header with a value like &#x60;/api/sdwan/v2/asyncOperations/4f951593-aff8-4f3c-9855-10fa5d32a419&#x60;). Due to a limitation of our documentation automation/tooling, we can only describe non-async API behavior, we are working on this and will update the expected behavior soon
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>415</b> - UnsupportedMediaTypeError
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param body Replace Edge-specific device settings module request schema
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return EdgeDeviceSettings
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EdgeDeviceSettings replaceEdgeProfileDeviceSettings(String enterpriseLogicalId, String edgeLogicalId, EdgeProfileDeviceSettingsRequestSpec body, String include) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling replaceEdgeProfileDeviceSettings");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling replaceEdgeProfileDeviceSettings");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/deviceSettings").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EdgeDeviceSettings> returnType = new ParameterizedTypeReference<EdgeDeviceSettings>() {};
        return apiClient.invokeAPI(path, HttpMethod.PUT, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Profile deviceSettings module replace spec &lt;br /&gt; &lt;br /&gt; &lt;strong&gt;Note:&lt;/strong&gt; By default this API operation executes asynchronously, meaning successful requests to this method will return an HTTP 202 response including tracking resource information (incl. an HTTP Location header with a value like &#x60;/api/sdwan/v2/asyncOperations/4f951593-aff8-4f3c-9855-10fa5d32a419&#x60;). Due to a limitation of our documentation automation/tooling, we can only describe non-async API behavior, we are working on this and will update the expected behavior soon
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>415</b> - UnsupportedMediaTypeError
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile
     * @param body Replace profile deviceSettings module request schema
     * @return EnterpriseDeviceSettings
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EnterpriseDeviceSettings replaceEnterpriseProfileDeviceSettings(String enterpriseLogicalId, String profileLogicalId, ProfileDeviceSettingsRequestSpec body) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling replaceEnterpriseProfileDeviceSettings");
        }
        // verify the required parameter 'profileLogicalId' is set
        if (profileLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'profileLogicalId' when calling replaceEnterpriseProfileDeviceSettings");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("profileLogicalId", profileLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/profiles/{profileLogicalId}/deviceSettings").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EnterpriseDeviceSettings> returnType = new ParameterizedTypeReference<EnterpriseDeviceSettings>() {};
        return apiClient.invokeAPI(path, HttpMethod.PUT, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Update an Edge &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>415</b> - UnsupportedMediaTypeError
     * <p><b>422</b> - DataValidationError
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param body Update Edge request schema
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return EdgeResource
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EdgeResource updateEdge(String enterpriseLogicalId, String edgeLogicalId, List<Object> body, String include) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling updateEdge");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling updateEdge");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EdgeResource> returnType = new ParameterizedTypeReference<EdgeResource>() {};
        return apiClient.invokeAPI(path, HttpMethod.PATCH, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Edge-specific device settings module update spec &lt;br /&gt; &lt;br /&gt; &lt;strong&gt;Note:&lt;/strong&gt; By default this API operation executes asynchronously, meaning successful requests to this method will return an HTTP 202 response including tracking resource information (incl. an HTTP Location header with a value like &#x60;/api/sdwan/v2/asyncOperations/4f951593-aff8-4f3c-9855-10fa5d32a419&#x60;). Due to a limitation of our documentation automation/tooling, we can only describe non-async API behavior, we are working on this and will update the expected behavior soon
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>415</b> - UnsupportedMediaTypeError
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param body PATCH request schema
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return EdgeDeviceSettings
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EdgeDeviceSettings updateEdgeProfileDeviceSettings(String enterpriseLogicalId, String edgeLogicalId, List<Object> body, String include) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling updateEdgeProfileDeviceSettings");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling updateEdgeProfileDeviceSettings");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/deviceSettings").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EdgeDeviceSettings> returnType = new ParameterizedTypeReference<EdgeDeviceSettings>() {};
        return apiClient.invokeAPI(path, HttpMethod.PATCH, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Profile deviceSettings module update spec &lt;br /&gt; &lt;br /&gt; &lt;strong&gt;Note:&lt;/strong&gt; By default this API operation executes asynchronously, meaning successful requests to this method will return an HTTP 202 response including tracking resource information (incl. an HTTP Location header with a value like &#x60;/api/sdwan/v2/asyncOperations/4f951593-aff8-4f3c-9855-10fa5d32a419&#x60;). Due to a limitation of our documentation automation/tooling, we can only describe non-async API behavior, we are working on this and will update the expected behavior soon
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>415</b> - UnsupportedMediaTypeError
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param profileLogicalId The &#x60;logicalId&#x60; GUID for the target profile
     * @param body Update Profile request schema
     * @return EnterpriseDeviceSettings
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EnterpriseDeviceSettings updateEnterpriseProfileDeviceSettings(String enterpriseLogicalId, String profileLogicalId, List<Object> body) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling updateEnterpriseProfileDeviceSettings");
        }
        // verify the required parameter 'profileLogicalId' is set
        if (profileLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'profileLogicalId' when calling updateEnterpriseProfileDeviceSettings");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("profileLogicalId", profileLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/profiles/{profileLogicalId}/deviceSettings").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EnterpriseDeviceSettings> returnType = new ParameterizedTypeReference<EnterpriseDeviceSettings>() {};
        return apiClient.invokeAPI(path, HttpMethod.PATCH, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
