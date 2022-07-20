package org.opennms.velocloud.client.api;

import org.opennms.velocloud.client.handler.ApiClient;

import org.opennms.velocloud.client.model.ApplicationClasses;
import org.opennms.velocloud.client.model.ApplicationMapResource;
import org.opennms.velocloud.client.model.ApplicationResource;
import org.opennms.velocloud.client.model.Applications;
import org.opennms.velocloud.client.model.BgpSessionCollection;
import org.opennms.velocloud.client.model.ClientDeviceResource;
import org.opennms.velocloud.client.model.ClientDevices;
import org.opennms.velocloud.client.model.CreateEnterpriseSchema;
import org.opennms.velocloud.client.model.DataValidationError;
import org.opennms.velocloud.client.model.EdgeHealthStatsMetricsSchema;
import org.opennms.velocloud.client.model.EdgeHealthStatsSeriesSchema;
import org.opennms.velocloud.client.model.EdgeNonSDWANTunnelStatus;
import org.opennms.velocloud.client.model.EdgePathStatsMetrics;
import org.opennms.velocloud.client.model.EdgePathStatsSeries;
import org.opennms.velocloud.client.model.EdgeResource;
import org.opennms.velocloud.client.model.EdgeResourceCollection;
import org.opennms.velocloud.client.model.EnterpriseAlertsSchema;
import org.opennms.velocloud.client.model.EnterpriseEventsSchema;
import org.opennms.velocloud.client.model.EnterpriseHealthStatsSchema;
import org.opennms.velocloud.client.model.EnterpriseResource;
import org.opennms.velocloud.client.model.EnterpriseResourceCollection;
import org.opennms.velocloud.client.model.FlowStats;
import org.opennms.velocloud.client.model.FlowStatsSeries;
import org.opennms.velocloud.client.model.GatewayNonSdWanSiteSchema;
import org.opennms.velocloud.client.model.InternalServerError;
import org.opennms.velocloud.client.model.LinkQualityStatsResource;
import org.opennms.velocloud.client.model.LinkQualityStatsTimeSeriesResource;
import org.opennms.velocloud.client.model.LinkStats;
import org.opennms.velocloud.client.model.LinkStatsSeries;
import org.opennms.velocloud.client.model.NonSdwanServiceStatus;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")@Component("org.opennms.velocloud.client.api.MonitorApi")
public class MonitorApi {
    private ApiClient apiClient;

    public MonitorApi() {
        this(new ApiClient());
    }

    @Autowired
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
     * 
     * Create a Customer &lt;br /&gt; &lt;br /&gt; 
     * <p><b>201</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>415</b> - UnsupportedMediaTypeError
     * <p><b>422</b> - DataValidationError
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param body Enterprise creation schema
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return EnterpriseResource
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EnterpriseResource createEnterprise(CreateEnterpriseSchema body, String include) throws RestClientException {
        Object postBody = body;
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/").build().toUriString();
        
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

        ParameterizedTypeReference<EnterpriseResource> returnType = new ParameterizedTypeReference<EnterpriseResource>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Delete a Customer &lt;br /&gt; &lt;br /&gt; 
     * <p><b>204</b> - Resource deleted successfully
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void deleteEnterprise(String enterpriseLogicalId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling deleteEnterprise");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        apiClient.invokeAPI(path, HttpMethod.DELETE, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * [OPERATOR ONLY] Fetch application map resource &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return ApplicationMapResource
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ApplicationMapResource getApplicationMap(String logicalId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'logicalId' is set
        if (logicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'logicalId' when calling getApplicationMap");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("logicalId", logicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/applicationMaps/{logicalId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<ApplicationMapResource> returnType = new ParameterizedTypeReference<ApplicationMapResource>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * [OPERATOR ONLY] Fetch an application definition for the target application map &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource
     * @param appId The &#x60;appId&#x60; for the target application
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return ApplicationResource
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ApplicationResource getApplicationMapApplication(String logicalId, Integer appId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'logicalId' is set
        if (logicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'logicalId' when calling getApplicationMapApplication");
        }
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'appId' when calling getApplicationMapApplication");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("logicalId", logicalId);
        uriVariables.put("appId", appId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/applicationMaps/{logicalId}/applications/{appId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<ApplicationResource> returnType = new ParameterizedTypeReference<ApplicationResource>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch application map classes &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return ApplicationClasses
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ApplicationClasses getApplicationMapApplicationClasses(String logicalId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'logicalId' is set
        if (logicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'logicalId' when calling getApplicationMapApplicationClasses");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("logicalId", logicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/applicationMaps/{logicalId}/applicationClasses").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<ApplicationClasses> returnType = new ParameterizedTypeReference<ApplicationClasses>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * [OPERATOR ONLY] List applications associated with an application map &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return Applications
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Applications getApplicationMapApplications(String logicalId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'logicalId' is set
        if (logicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'logicalId' when calling getApplicationMapApplications");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("logicalId", logicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/applicationMaps/{logicalId}/applications").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<Applications> returnType = new ParameterizedTypeReference<Applications>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch edge applications &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param appId The &#x60;appId&#x60; for the target application
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return Applications
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Applications getEdgeApplication(String enterpriseLogicalId, String edgeLogicalId, Integer appId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgeApplication");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgeApplication");
        }
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'appId' when calling getEdgeApplication");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        uriVariables.put("appId", appId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/applications/{appId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<Applications> returnType = new ParameterizedTypeReference<Applications>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch edge applications &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return Applications
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Applications getEdgeApplications(String enterpriseLogicalId, String edgeLogicalId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgeApplications");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgeApplications");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/applications").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<Applications> returnType = new ParameterizedTypeReference<Applications>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch Edge flow stats &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported.
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt;
     * @param metrics metrics supported for querying flowStats
     * @return FlowStats
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public FlowStats getEdgeFlowMetrics(String enterpriseLogicalId, String edgeLogicalId, String groupBy, Integer start, String include, Integer end, String sortBy, String metrics) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgeFlowMetrics");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgeFlowMetrics");
        }
        // verify the required parameter 'groupBy' is set
        if (groupBy == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupBy' when calling getEdgeFlowMetrics");
        }
        // verify the required parameter 'start' is set
        if (start == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'start' when calling getEdgeFlowMetrics");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/flowStats").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "groupBy", groupBy));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sortBy", sortBy));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "metrics", metrics));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<FlowStats> returnType = new ParameterizedTypeReference<FlowStats>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch edge flow stats series &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported.
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param metrics metrics supported for querying flowStats
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt;
     * @return FlowStatsSeries
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public FlowStatsSeries getEdgeFlowSeries(String enterpriseLogicalId, String edgeLogicalId, String groupBy, Integer start, String metrics, String include, Integer end, String sortBy) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgeFlowSeries");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgeFlowSeries");
        }
        // verify the required parameter 'groupBy' is set
        if (groupBy == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupBy' when calling getEdgeFlowSeries");
        }
        // verify the required parameter 'start' is set
        if (start == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'start' when calling getEdgeFlowSeries");
        }
        // verify the required parameter 'metrics' is set
        if (metrics == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'metrics' when calling getEdgeFlowSeries");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/flowStats/timeSeries").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "groupBy", groupBy));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sortBy", sortBy));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "metrics", metrics));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<FlowStatsSeries> returnType = new ParameterizedTypeReference<FlowStatsSeries>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch edge system resource metrics &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param metrics The metrics parameter
     * @return EdgeHealthStatsMetricsSchema
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EdgeHealthStatsMetricsSchema getEdgeHealthMetrics(String enterpriseLogicalId, String edgeLogicalId, String include, Integer start, Integer end, String metrics) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgeHealthMetrics");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgeHealthMetrics");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/healthStats").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "metrics", metrics));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EdgeHealthStatsMetricsSchema> returnType = new ParameterizedTypeReference<EdgeHealthStatsMetricsSchema>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch system resource metric time series data &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param metrics The metrics parameter
     * @return EdgeHealthStatsSeriesSchema
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EdgeHealthStatsSeriesSchema getEdgeHealthSeries(String enterpriseLogicalId, String edgeLogicalId, String include, Integer start, Integer end, String metrics) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgeHealthSeries");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgeHealthSeries");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/healthStats/timeSeries").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "metrics", metrics));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EdgeHealthStatsSeriesSchema> returnType = new ParameterizedTypeReference<EdgeHealthStatsSeriesSchema>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Edge WAN link transport metrics &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param metrics metrics supported for querying linkStats
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt;
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @return LinkStats
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public LinkStats getEdgeLinkMetrics(String enterpriseLogicalId, String edgeLogicalId, String include, String metrics, String sortBy, Integer start, Integer end) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgeLinkMetrics");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgeLinkMetrics");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/linkStats").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "metrics", metrics));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sortBy", sortBy));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<LinkStats> returnType = new ParameterizedTypeReference<LinkStats>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch link quality scores for an Edge &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @return LinkQualityStatsResource
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public LinkQualityStatsResource getEdgeLinkQualityEvents(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, Integer end) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgeLinkQualityEvents");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgeLinkQualityEvents");
        }
        // verify the required parameter 'start' is set
        if (start == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'start' when calling getEdgeLinkQualityEvents");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/linkQualityStats").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<LinkQualityStatsResource> returnType = new ParameterizedTypeReference<LinkQualityStatsResource>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch link quality timeseries for an Edge &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param minutesPerSample An integer value specifying time slice value for link quality events time series data
     * @return LinkQualityStatsTimeSeriesResource
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public LinkQualityStatsTimeSeriesResource getEdgeLinkQualitySeries(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, Integer end, Integer minutesPerSample) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgeLinkQualitySeries");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgeLinkQualitySeries");
        }
        // verify the required parameter 'start' is set
        if (start == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'start' when calling getEdgeLinkQualitySeries");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/linkQualityStats/timeSeries").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "minutesPerSample", minutesPerSample));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<LinkQualityStatsTimeSeriesResource> returnType = new ParameterizedTypeReference<LinkQualityStatsTimeSeriesResource>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch edge linkStats time series &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param metrics metrics supported for querying linkStats
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt;
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @return LinkStatsSeries
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public LinkStatsSeries getEdgeLinkSeries(String enterpriseLogicalId, String edgeLogicalId, Integer start, String include, String metrics, String sortBy, Integer end) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgeLinkSeries");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgeLinkSeries");
        }
        // verify the required parameter 'start' is set
        if (start == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'start' when calling getEdgeLinkSeries");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/linkStats/timeSeries").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "metrics", metrics));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sortBy", sortBy));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<LinkStatsSeries> returnType = new ParameterizedTypeReference<LinkStatsSeries>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch Edge non-SD-WAN tunnel status &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt;
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param state Filter by non-SD-WAN tunnel state
     * @param stateIs Filter by state[is]
     * @param stateIsNot Filter by state[isNot]
     * @param stateIn Filter by state[in]
     * @param stateNotIn Filter by state[notIn]
     * @return EdgeNonSDWANTunnelStatus
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EdgeNonSDWANTunnelStatus getEdgeNonSdwanTunnelStatusViaEdge(String enterpriseLogicalId, String edgeLogicalId, String include, String sortBy, Integer start, Integer end, Object state, String stateIs, String stateIsNot, String stateIn, String stateNotIn) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgeNonSdwanTunnelStatusViaEdge");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgeNonSdwanTunnelStatusViaEdge");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/nonSdWanTunnelStatus").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sortBy", sortBy));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state", state));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[is]", stateIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[isNot]", stateIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[in]", stateIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[notIn]", stateNotIn));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EdgeNonSDWANTunnelStatus> returnType = new ParameterizedTypeReference<EdgeNonSDWANTunnelStatus>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch path stats metrics from an edge &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param peerLogicalId The &#x60;logicalId&#x60; GUID for the path stats peer
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param metrics metrics supported for querying pathStats
     * @param limit Limits the maximum size of the result set.
     * @return EdgePathStatsMetrics
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EdgePathStatsMetrics getEdgePathMetrics(String enterpriseLogicalId, String edgeLogicalId, String peerLogicalId, String include, Integer start, Integer end, String metrics, Integer limit) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgePathMetrics");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgePathMetrics");
        }
        // verify the required parameter 'peerLogicalId' is set
        if (peerLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'peerLogicalId' when calling getEdgePathMetrics");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/pathStats").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "metrics", metrics));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "peerLogicalId", peerLogicalId));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EdgePathStatsMetrics> returnType = new ParameterizedTypeReference<EdgePathStatsMetrics>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch path stats time series from an edge &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param peerLogicalId The &#x60;logicalId&#x60; GUID for the path stats peer
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param maxSamples An integer value specifying the max samples for path stats
     * @param metrics metrics supported for querying pathStats
     * @param limit Limits the maximum size of the result set.
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported.
     * @return EdgePathStatsSeries
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EdgePathStatsSeries getEdgePathSeries(String enterpriseLogicalId, String edgeLogicalId, String peerLogicalId, String include, Integer start, Integer end, Integer maxSamples, String metrics, Integer limit, String groupBy) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEdgePathSeries");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEdgePathSeries");
        }
        // verify the required parameter 'peerLogicalId' is set
        if (peerLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'peerLogicalId' when calling getEdgePathSeries");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("edgeLogicalId", edgeLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/{edgeLogicalId}/pathStats/timeSeries").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "peerLogicalId", peerLogicalId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "maxSamples", maxSamples));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "metrics", metrics));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "groupBy", groupBy));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EdgePathStatsSeries> returnType = new ParameterizedTypeReference<EdgePathStatsSeries>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch a customer &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return EnterpriseResource
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EnterpriseResource getEnterprise(String enterpriseLogicalId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterprise");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<EnterpriseResource> returnType = new ParameterizedTypeReference<EnterpriseResource>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch aggregate Edge health metrics for all Edges belonging to the target customer &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param metrics The metrics parameter
     * @return EnterpriseHealthStatsSchema
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EnterpriseHealthStatsSchema getEnterpriseAggregateHealthStats(String enterpriseLogicalId, String include, String metrics) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseAggregateHealthStats");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/healthStats").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "metrics", metrics));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EnterpriseHealthStatsSchema> returnType = new ParameterizedTypeReference<EnterpriseHealthStatsSchema>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch past triggered alerts for the specified enterprise &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param limit Limits the maximum size of the result set.
     * @param nextPageLink A nextPageLink value, as fetched via a past call to a list API method. When a nextPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored.
     * @param prevPageLink A prevPageLink value, as fetched via a past call to a list API method. When a prevPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored.
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param edgeNameContains Filter by edgeName[contains]
     * @param edgeNameNotContains Filter by edgeName[notContains]
     * @param linkNameContains Filter by linkName[contains]
     * @param linkNameNotContains Filter by linkName[notContains]
     * @param typeIs Filter by type[is]
     * @param typeIsNot Filter by type[isNot]
     * @param typeIn Filter by type[in]
     * @param typeNotIn Filter by type[notIn]
     * @param stateIs Filter by state[is]
     * @param stateIsNot Filter by state[isNot]
     * @param stateIn Filter by state[in]
     * @param stateNotIn Filter by state[notIn]
     * @param enterpriseAlertConfigurationIdIsNull Filter by enterpriseAlertConfigurationId[isNull]
     * @param enterpriseAlertConfigurationIdIsNotNull Filter by enterpriseAlertConfigurationId[isNotNull]
     * @return EnterpriseAlertsSchema
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EnterpriseAlertsSchema getEnterpriseAlerts(String enterpriseLogicalId, Integer start, Integer end, Integer limit, String nextPageLink, String prevPageLink, String include, String edgeNameContains, String edgeNameNotContains, String linkNameContains, String linkNameNotContains, String typeIs, String typeIsNot, String typeIn, String typeNotIn, String stateIs, String stateIsNot, String stateIn, String stateNotIn, Float enterpriseAlertConfigurationIdIsNull, Float enterpriseAlertConfigurationIdIsNotNull) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseAlerts");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/alerts").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "nextPageLink", nextPageLink));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "prevPageLink", prevPageLink));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[contains]", edgeNameContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[notContains]", edgeNameNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "linkName[contains]", linkNameContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "linkName[notContains]", linkNameNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "type[is]", typeIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "type[isNot]", typeIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "type[in]", typeIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "type[notIn]", typeNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[is]", stateIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[isNot]", stateIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[in]", stateIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[notIn]", stateNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseAlertConfigurationId[isNull]", enterpriseAlertConfigurationIdIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseAlertConfigurationId[isNotNull]", enterpriseAlertConfigurationIdIsNotNull));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EnterpriseAlertsSchema> returnType = new ParameterizedTypeReference<EnterpriseAlertsSchema>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch enterprise applications &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param appId The &#x60;appId&#x60; for the target application
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return Applications
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Applications getEnterpriseApplicationById(String enterpriseLogicalId, Integer appId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseApplicationById");
        }
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'appId' when calling getEnterpriseApplicationById");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("appId", appId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/applications/{appId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<Applications> returnType = new ParameterizedTypeReference<Applications>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch enterprise applications &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return Applications
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Applications getEnterpriseApplications(String enterpriseLogicalId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseApplications");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/applications").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<Applications> returnType = new ParameterizedTypeReference<Applications>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Get BGP peering session state &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param peerType The BGP peer type
     * @param stateIs Filter by state[is]
     * @param stateIsNot Filter by state[isNot]
     * @param stateIn Filter by state[in]
     * @param stateNotIn Filter by state[notIn]
     * @param neighborIpIs Filter by neighborIp[is]
     * @param neighborIpIsNot Filter by neighborIp[isNot]
     * @param neighborIpIsNull Filter by neighborIp[isNull]
     * @param neighborIpIsNotNull Filter by neighborIp[isNotNull]
     * @param neighborIpStartsWith Filter by neighborIp[startsWith]
     * @param neighborIpNotStartsWith Filter by neighborIp[notStartsWith]
     * @param neighborIpEndsWith Filter by neighborIp[endsWith]
     * @param neighborIpNotEndsWith Filter by neighborIp[notEndsWith]
     * @param neighborIpContains Filter by neighborIp[contains]
     * @param neighborIpNotContains Filter by neighborIp[notContains]
     * @param neighborIpIn Filter by neighborIp[in]
     * @param neighborIpNotIn Filter by neighborIp[notIn]
     * @return BgpSessionCollection
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public BgpSessionCollection getEnterpriseBgpSessions(String enterpriseLogicalId, String include, String peerType, String stateIs, String stateIsNot, String stateIn, String stateNotIn, String neighborIpIs, String neighborIpIsNot, String neighborIpIsNull, String neighborIpIsNotNull, String neighborIpStartsWith, String neighborIpNotStartsWith, String neighborIpEndsWith, String neighborIpNotEndsWith, String neighborIpContains, String neighborIpNotContains, String neighborIpIn, String neighborIpNotIn) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseBgpSessions");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/bgpSessions").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "peerType", peerType));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[is]", stateIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[isNot]", stateIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[in]", stateIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[notIn]", stateNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "neighborIp[is]", neighborIpIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "neighborIp[isNot]", neighborIpIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "neighborIp[isNull]", neighborIpIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "neighborIp[isNotNull]", neighborIpIsNotNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "neighborIp[startsWith]", neighborIpStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "neighborIp[notStartsWith]", neighborIpNotStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "neighborIp[endsWith]", neighborIpEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "neighborIp[notEndsWith]", neighborIpNotEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "neighborIp[contains]", neighborIpContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "neighborIp[notContains]", neighborIpNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "neighborIp[in]", neighborIpIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "neighborIp[notIn]", neighborIpNotIn));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<BgpSessionCollection> returnType = new ParameterizedTypeReference<BgpSessionCollection>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch a client device &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param logicalId The &#x60;logicalId&#x60; GUID for the target resource
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return ClientDeviceResource
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ClientDeviceResource getEnterpriseClientDevice(String enterpriseLogicalId, String logicalId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseClientDevice");
        }
        // verify the required parameter 'logicalId' is set
        if (logicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'logicalId' when calling getEnterpriseClientDevice");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        uriVariables.put("logicalId", logicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/clientDevices/{logicalId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<ClientDeviceResource> returnType = new ParameterizedTypeReference<ClientDeviceResource>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch all client devices &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return ClientDevices
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ClientDevices getEnterpriseClientDevices(String enterpriseLogicalId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseClientDevices");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/clientDevices").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<ClientDevices> returnType = new ParameterizedTypeReference<ClientDevices>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Get an Edge &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param edgeLogicalId The &#x60;logicalId&#x60; GUID for the target edge
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return EdgeResource
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EdgeResource getEnterpriseEdge(String enterpriseLogicalId, String edgeLogicalId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseEdge");
        }
        // verify the required parameter 'edgeLogicalId' is set
        if (edgeLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'edgeLogicalId' when calling getEnterpriseEdge");
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
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EdgeResource> returnType = new ParameterizedTypeReference<EdgeResource>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * List Customer Edges &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param nameIs Filter by name[is]
     * @param nameIsNot Filter by name[isNot]
     * @param nameIsNull Filter by name[isNull]
     * @param nameIsNotNull Filter by name[isNotNull]
     * @param nameStartsWith Filter by name[startsWith]
     * @param nameNotStartsWith Filter by name[notStartsWith]
     * @param nameEndsWith Filter by name[endsWith]
     * @param nameNotEndsWith Filter by name[notEndsWith]
     * @param nameContains Filter by name[contains]
     * @param nameNotContains Filter by name[notContains]
     * @param nameIn Filter by name[in]
     * @param nameNotIn Filter by name[notIn]
     * @param createdIs Filter by created[is]
     * @param createdIsNot Filter by created[isNot]
     * @param createdIsNull Filter by created[isNull]
     * @param createdIsNotNull Filter by created[isNotNull]
     * @param createdGreaterOrEquals Filter by created[greaterOrEquals]
     * @param createdLesserOrEquals Filter by created[lesserOrEquals]
     * @param createdIn Filter by created[in]
     * @param createdNotIn Filter by created[notIn]
     * @param activationStateIs Filter by activationState[is]
     * @param activationStateIsNot Filter by activationState[isNot]
     * @param activationStateIn Filter by activationState[in]
     * @param activationStateNotIn Filter by activationState[notIn]
     * @param modelNumberIs Filter by modelNumber[is]
     * @param modelNumberIsNot Filter by modelNumber[isNot]
     * @param modelNumberIn Filter by modelNumber[in]
     * @param modelNumberNotIn Filter by modelNumber[notIn]
     * @param edgeStateIs Filter by edgeState[is]
     * @param edgeStateIsNot Filter by edgeState[isNot]
     * @param edgeStateIn Filter by edgeState[in]
     * @param edgeStateNotIn Filter by edgeState[notIn]
     * @param buildNumberIs Filter by buildNumber[is]
     * @param buildNumberIsNot Filter by buildNumber[isNot]
     * @param buildNumberIsNull Filter by buildNumber[isNull]
     * @param buildNumberIsNotNull Filter by buildNumber[isNotNull]
     * @param buildNumberStartsWith Filter by buildNumber[startsWith]
     * @param buildNumberNotStartsWith Filter by buildNumber[notStartsWith]
     * @param buildNumberEndsWith Filter by buildNumber[endsWith]
     * @param buildNumberNotEndsWith Filter by buildNumber[notEndsWith]
     * @param buildNumberContains Filter by buildNumber[contains]
     * @param buildNumberNotContains Filter by buildNumber[notContains]
     * @param buildNumberIn Filter by buildNumber[in]
     * @param buildNumberNotIn Filter by buildNumber[notIn]
     * @param softwareVersionIs Filter by softwareVersion[is]
     * @param softwareVersionIsNot Filter by softwareVersion[isNot]
     * @param softwareVersionIsNull Filter by softwareVersion[isNull]
     * @param softwareVersionIsNotNull Filter by softwareVersion[isNotNull]
     * @param softwareVersionStartsWith Filter by softwareVersion[startsWith]
     * @param softwareVersionNotStartsWith Filter by softwareVersion[notStartsWith]
     * @param softwareVersionEndsWith Filter by softwareVersion[endsWith]
     * @param softwareVersionNotEndsWith Filter by softwareVersion[notEndsWith]
     * @param softwareVersionContains Filter by softwareVersion[contains]
     * @param softwareVersionNotContains Filter by softwareVersion[notContains]
     * @param softwareVersionIn Filter by softwareVersion[in]
     * @param softwareVersionNotIn Filter by softwareVersion[notIn]
     * @param serialNumberIs Filter by serialNumber[is]
     * @param serialNumberIsNot Filter by serialNumber[isNot]
     * @param serialNumberIsNull Filter by serialNumber[isNull]
     * @param serialNumberIsNotNull Filter by serialNumber[isNotNull]
     * @param serialNumberStartsWith Filter by serialNumber[startsWith]
     * @param serialNumberNotStartsWith Filter by serialNumber[notStartsWith]
     * @param serialNumberEndsWith Filter by serialNumber[endsWith]
     * @param serialNumberNotEndsWith Filter by serialNumber[notEndsWith]
     * @param serialNumberContains Filter by serialNumber[contains]
     * @param serialNumberNotContains Filter by serialNumber[notContains]
     * @param serialNumberIn Filter by serialNumber[in]
     * @param serialNumberNotIn Filter by serialNumber[notIn]
     * @param softwareUpdatedIs Filter by softwareUpdated[is]
     * @param softwareUpdatedIsNot Filter by softwareUpdated[isNot]
     * @param softwareUpdatedIsNull Filter by softwareUpdated[isNull]
     * @param softwareUpdatedIsNotNull Filter by softwareUpdated[isNotNull]
     * @param softwareUpdatedGreaterOrEquals Filter by softwareUpdated[greaterOrEquals]
     * @param softwareUpdatedLesserOrEquals Filter by softwareUpdated[lesserOrEquals]
     * @param softwareUpdatedIn Filter by softwareUpdated[in]
     * @param softwareUpdatedNotIn Filter by softwareUpdated[notIn]
     * @param customInfoIs Filter by customInfo[is]
     * @param customInfoIsNot Filter by customInfo[isNot]
     * @param customInfoIsNull Filter by customInfo[isNull]
     * @param customInfoIsNotNull Filter by customInfo[isNotNull]
     * @param customInfoStartsWith Filter by customInfo[startsWith]
     * @param customInfoNotStartsWith Filter by customInfo[notStartsWith]
     * @param customInfoEndsWith Filter by customInfo[endsWith]
     * @param customInfoNotEndsWith Filter by customInfo[notEndsWith]
     * @param customInfoContains Filter by customInfo[contains]
     * @param customInfoNotContains Filter by customInfo[notContains]
     * @param customInfoIn Filter by customInfo[in]
     * @param customInfoNotIn Filter by customInfo[notIn]
     * @return EdgeResourceCollection
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EdgeResourceCollection getEnterpriseEdges(String enterpriseLogicalId, String include, String nameIs, String nameIsNot, String nameIsNull, String nameIsNotNull, String nameStartsWith, String nameNotStartsWith, String nameEndsWith, String nameNotEndsWith, String nameContains, String nameNotContains, String nameIn, String nameNotIn, Integer createdIs, Integer createdIsNot, Integer createdIsNull, Integer createdIsNotNull, Integer createdGreaterOrEquals, Integer createdLesserOrEquals, Integer createdIn, Integer createdNotIn, String activationStateIs, String activationStateIsNot, String activationStateIn, String activationStateNotIn, String modelNumberIs, String modelNumberIsNot, String modelNumberIn, String modelNumberNotIn, String edgeStateIs, String edgeStateIsNot, String edgeStateIn, String edgeStateNotIn, String buildNumberIs, String buildNumberIsNot, String buildNumberIsNull, String buildNumberIsNotNull, String buildNumberStartsWith, String buildNumberNotStartsWith, String buildNumberEndsWith, String buildNumberNotEndsWith, String buildNumberContains, String buildNumberNotContains, String buildNumberIn, String buildNumberNotIn, String softwareVersionIs, String softwareVersionIsNot, String softwareVersionIsNull, String softwareVersionIsNotNull, String softwareVersionStartsWith, String softwareVersionNotStartsWith, String softwareVersionEndsWith, String softwareVersionNotEndsWith, String softwareVersionContains, String softwareVersionNotContains, String softwareVersionIn, String softwareVersionNotIn, String serialNumberIs, String serialNumberIsNot, String serialNumberIsNull, String serialNumberIsNotNull, String serialNumberStartsWith, String serialNumberNotStartsWith, String serialNumberEndsWith, String serialNumberNotEndsWith, String serialNumberContains, String serialNumberNotContains, String serialNumberIn, String serialNumberNotIn, Integer softwareUpdatedIs, Integer softwareUpdatedIsNot, Integer softwareUpdatedIsNull, Integer softwareUpdatedIsNotNull, Integer softwareUpdatedGreaterOrEquals, Integer softwareUpdatedLesserOrEquals, Integer softwareUpdatedIn, Integer softwareUpdatedNotIn, String customInfoIs, String customInfoIsNot, String customInfoIsNull, String customInfoIsNotNull, String customInfoStartsWith, String customInfoNotStartsWith, String customInfoEndsWith, String customInfoNotEndsWith, String customInfoContains, String customInfoNotContains, String customInfoIn, String customInfoNotIn) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseEdges");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edges/").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[is]", nameIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[isNot]", nameIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[isNull]", nameIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[isNotNull]", nameIsNotNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[startsWith]", nameStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[notStartsWith]", nameNotStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[endsWith]", nameEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[notEndsWith]", nameNotEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[contains]", nameContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[notContains]", nameNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[in]", nameIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[notIn]", nameNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "created[is]", createdIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "created[isNot]", createdIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "created[isNull]", createdIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "created[isNotNull]", createdIsNotNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "created[greaterOrEquals]", createdGreaterOrEquals));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "created[lesserOrEquals]", createdLesserOrEquals));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "created[in]", createdIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "created[notIn]", createdNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "activationState[is]", activationStateIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "activationState[isNot]", activationStateIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "activationState[in]", activationStateIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "activationState[notIn]", activationStateNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "modelNumber[is]", modelNumberIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "modelNumber[isNot]", modelNumberIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "modelNumber[in]", modelNumberIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "modelNumber[notIn]", modelNumberNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeState[is]", edgeStateIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeState[isNot]", edgeStateIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeState[in]", edgeStateIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeState[notIn]", edgeStateNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "buildNumber[is]", buildNumberIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "buildNumber[isNot]", buildNumberIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "buildNumber[isNull]", buildNumberIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "buildNumber[isNotNull]", buildNumberIsNotNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "buildNumber[startsWith]", buildNumberStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "buildNumber[notStartsWith]", buildNumberNotStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "buildNumber[endsWith]", buildNumberEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "buildNumber[notEndsWith]", buildNumberNotEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "buildNumber[contains]", buildNumberContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "buildNumber[notContains]", buildNumberNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "buildNumber[in]", buildNumberIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "buildNumber[notIn]", buildNumberNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareVersion[is]", softwareVersionIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareVersion[isNot]", softwareVersionIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareVersion[isNull]", softwareVersionIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareVersion[isNotNull]", softwareVersionIsNotNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareVersion[startsWith]", softwareVersionStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareVersion[notStartsWith]", softwareVersionNotStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareVersion[endsWith]", softwareVersionEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareVersion[notEndsWith]", softwareVersionNotEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareVersion[contains]", softwareVersionContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareVersion[notContains]", softwareVersionNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareVersion[in]", softwareVersionIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareVersion[notIn]", softwareVersionNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "serialNumber[is]", serialNumberIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "serialNumber[isNot]", serialNumberIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "serialNumber[isNull]", serialNumberIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "serialNumber[isNotNull]", serialNumberIsNotNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "serialNumber[startsWith]", serialNumberStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "serialNumber[notStartsWith]", serialNumberNotStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "serialNumber[endsWith]", serialNumberEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "serialNumber[notEndsWith]", serialNumberNotEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "serialNumber[contains]", serialNumberContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "serialNumber[notContains]", serialNumberNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "serialNumber[in]", serialNumberIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "serialNumber[notIn]", serialNumberNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareUpdated[is]", softwareUpdatedIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareUpdated[isNot]", softwareUpdatedIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareUpdated[isNull]", softwareUpdatedIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareUpdated[isNotNull]", softwareUpdatedIsNotNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareUpdated[greaterOrEquals]", softwareUpdatedGreaterOrEquals));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareUpdated[lesserOrEquals]", softwareUpdatedLesserOrEquals));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareUpdated[in]", softwareUpdatedIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "softwareUpdated[notIn]", softwareUpdatedNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customInfo[is]", customInfoIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customInfo[isNot]", customInfoIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customInfo[isNull]", customInfoIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customInfo[isNotNull]", customInfoIsNotNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customInfo[startsWith]", customInfoStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customInfo[notStartsWith]", customInfoNotStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customInfo[endsWith]", customInfoEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customInfo[notEndsWith]", customInfoNotEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customInfo[contains]", customInfoContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customInfo[notContains]", customInfoNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customInfo[in]", customInfoIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customInfo[notIn]", customInfoNotIn));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EdgeResourceCollection> returnType = new ParameterizedTypeReference<EdgeResourceCollection>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch time-ordered list of events &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param limit Limits the maximum size of the result set.
     * @param nextPageLink A nextPageLink value, as fetched via a past call to a list API method. When a nextPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored.
     * @param prevPageLink A prevPageLink value, as fetched via a past call to a list API method. When a prevPageLink value is specified, any other query parameters that may ordinarily be used to sort or filter the result set are ignored.
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param eventIs Filter by event[is]
     * @param eventIsNot Filter by event[isNot]
     * @param eventIn Filter by event[in]
     * @param eventNotIn Filter by event[notIn]
     * @param severityIs Filter by severity[is]
     * @param severityIsNot Filter by severity[isNot]
     * @param severityIn Filter by severity[in]
     * @param severityNotIn Filter by severity[notIn]
     * @param enterpriseUsernameIs Filter by enterpriseUsername[is]
     * @param enterpriseUsernameIsNot Filter by enterpriseUsername[isNot]
     * @param enterpriseUsernameIsNull Filter by enterpriseUsername[isNull]
     * @param enterpriseUsernameIsNotNull Filter by enterpriseUsername[isNotNull]
     * @param enterpriseUsernameStartsWith Filter by enterpriseUsername[startsWith]
     * @param enterpriseUsernameNotStartsWith Filter by enterpriseUsername[notStartsWith]
     * @param enterpriseUsernameEndsWith Filter by enterpriseUsername[endsWith]
     * @param enterpriseUsernameNotEndsWith Filter by enterpriseUsername[notEndsWith]
     * @param enterpriseUsernameContains Filter by enterpriseUsername[contains]
     * @param enterpriseUsernameNotContains Filter by enterpriseUsername[notContains]
     * @param enterpriseUsernameIn Filter by enterpriseUsername[in]
     * @param enterpriseUsernameNotIn Filter by enterpriseUsername[notIn]
     * @param detailIs Filter by detail[is]
     * @param detailIsNot Filter by detail[isNot]
     * @param detailIsNull Filter by detail[isNull]
     * @param detailIsNotNull Filter by detail[isNotNull]
     * @param detailStartsWith Filter by detail[startsWith]
     * @param detailNotStartsWith Filter by detail[notStartsWith]
     * @param detailEndsWith Filter by detail[endsWith]
     * @param detailNotEndsWith Filter by detail[notEndsWith]
     * @param detailContains Filter by detail[contains]
     * @param detailNotContains Filter by detail[notContains]
     * @param detailIn Filter by detail[in]
     * @param detailNotIn Filter by detail[notIn]
     * @param segmentNameIs Filter by segmentName[is]
     * @param segmentNameIsNot Filter by segmentName[isNot]
     * @param segmentNameIsNull Filter by segmentName[isNull]
     * @param segmentNameIsNotNull Filter by segmentName[isNotNull]
     * @param segmentNameStartsWith Filter by segmentName[startsWith]
     * @param segmentNameNotStartsWith Filter by segmentName[notStartsWith]
     * @param segmentNameEndsWith Filter by segmentName[endsWith]
     * @param segmentNameNotEndsWith Filter by segmentName[notEndsWith]
     * @param segmentNameContains Filter by segmentName[contains]
     * @param segmentNameNotContains Filter by segmentName[notContains]
     * @param segmentNameIn Filter by segmentName[in]
     * @param segmentNameNotIn Filter by segmentName[notIn]
     * @param messageIs Filter by message[is]
     * @param messageIsNot Filter by message[isNot]
     * @param messageIsNull Filter by message[isNull]
     * @param messageIsNotNull Filter by message[isNotNull]
     * @param messageStartsWith Filter by message[startsWith]
     * @param messageNotStartsWith Filter by message[notStartsWith]
     * @param messageEndsWith Filter by message[endsWith]
     * @param messageNotEndsWith Filter by message[notEndsWith]
     * @param messageContains Filter by message[contains]
     * @param messageNotContains Filter by message[notContains]
     * @param messageIn Filter by message[in]
     * @param messageNotIn Filter by message[notIn]
     * @param edgeNameIs Filter by edgeName[is]
     * @param edgeNameIsNot Filter by edgeName[isNot]
     * @param edgeNameIsNull Filter by edgeName[isNull]
     * @param edgeNameIsNotNull Filter by edgeName[isNotNull]
     * @param edgeNameStartsWith Filter by edgeName[startsWith]
     * @param edgeNameNotStartsWith Filter by edgeName[notStartsWith]
     * @param edgeNameEndsWith Filter by edgeName[endsWith]
     * @param edgeNameNotEndsWith Filter by edgeName[notEndsWith]
     * @param edgeNameContains Filter by edgeName[contains]
     * @param edgeNameNotContains Filter by edgeName[notContains]
     * @param edgeNameIn Filter by edgeName[in]
     * @param edgeNameNotIn Filter by edgeName[notIn]
     * @return EnterpriseEventsSchema
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EnterpriseEventsSchema getEnterpriseEvents(String enterpriseLogicalId, Integer start, Integer end, Integer limit, String nextPageLink, String prevPageLink, String include, String eventIs, String eventIsNot, String eventIn, String eventNotIn, String severityIs, String severityIsNot, String severityIn, String severityNotIn, String enterpriseUsernameIs, String enterpriseUsernameIsNot, String enterpriseUsernameIsNull, String enterpriseUsernameIsNotNull, String enterpriseUsernameStartsWith, String enterpriseUsernameNotStartsWith, String enterpriseUsernameEndsWith, String enterpriseUsernameNotEndsWith, String enterpriseUsernameContains, String enterpriseUsernameNotContains, String enterpriseUsernameIn, String enterpriseUsernameNotIn, String detailIs, String detailIsNot, String detailIsNull, String detailIsNotNull, String detailStartsWith, String detailNotStartsWith, String detailEndsWith, String detailNotEndsWith, String detailContains, String detailNotContains, String detailIn, String detailNotIn, String segmentNameIs, String segmentNameIsNot, String segmentNameIsNull, String segmentNameIsNotNull, String segmentNameStartsWith, String segmentNameNotStartsWith, String segmentNameEndsWith, String segmentNameNotEndsWith, String segmentNameContains, String segmentNameNotContains, String segmentNameIn, String segmentNameNotIn, String messageIs, String messageIsNot, String messageIsNull, String messageIsNotNull, String messageStartsWith, String messageNotStartsWith, String messageEndsWith, String messageNotEndsWith, String messageContains, String messageNotContains, String messageIn, String messageNotIn, String edgeNameIs, String edgeNameIsNot, String edgeNameIsNull, String edgeNameIsNotNull, String edgeNameStartsWith, String edgeNameNotStartsWith, String edgeNameEndsWith, String edgeNameNotEndsWith, String edgeNameContains, String edgeNameNotContains, String edgeNameIn, String edgeNameNotIn) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseEvents");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/events").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "nextPageLink", nextPageLink));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "prevPageLink", prevPageLink));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "event[is]", eventIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "event[isNot]", eventIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "event[in]", eventIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "event[notIn]", eventNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "severity[is]", severityIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "severity[isNot]", severityIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "severity[in]", severityIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "severity[notIn]", severityNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseUsername[is]", enterpriseUsernameIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseUsername[isNot]", enterpriseUsernameIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseUsername[isNull]", enterpriseUsernameIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseUsername[isNotNull]", enterpriseUsernameIsNotNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseUsername[startsWith]", enterpriseUsernameStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseUsername[notStartsWith]", enterpriseUsernameNotStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseUsername[endsWith]", enterpriseUsernameEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseUsername[notEndsWith]", enterpriseUsernameNotEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseUsername[contains]", enterpriseUsernameContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseUsername[notContains]", enterpriseUsernameNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseUsername[in]", enterpriseUsernameIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "enterpriseUsername[notIn]", enterpriseUsernameNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "detail[is]", detailIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "detail[isNot]", detailIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "detail[isNull]", detailIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "detail[isNotNull]", detailIsNotNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "detail[startsWith]", detailStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "detail[notStartsWith]", detailNotStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "detail[endsWith]", detailEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "detail[notEndsWith]", detailNotEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "detail[contains]", detailContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "detail[notContains]", detailNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "detail[in]", detailIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "detail[notIn]", detailNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "segmentName[is]", segmentNameIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "segmentName[isNot]", segmentNameIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "segmentName[isNull]", segmentNameIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "segmentName[isNotNull]", segmentNameIsNotNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "segmentName[startsWith]", segmentNameStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "segmentName[notStartsWith]", segmentNameNotStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "segmentName[endsWith]", segmentNameEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "segmentName[notEndsWith]", segmentNameNotEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "segmentName[contains]", segmentNameContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "segmentName[notContains]", segmentNameNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "segmentName[in]", segmentNameIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "segmentName[notIn]", segmentNameNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "message[is]", messageIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "message[isNot]", messageIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "message[isNull]", messageIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "message[isNotNull]", messageIsNotNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "message[startsWith]", messageStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "message[notStartsWith]", messageNotStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "message[endsWith]", messageEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "message[notEndsWith]", messageNotEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "message[contains]", messageContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "message[notContains]", messageNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "message[in]", messageIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "message[notIn]", messageNotIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[is]", edgeNameIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[isNot]", edgeNameIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[isNull]", edgeNameIsNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[isNotNull]", edgeNameIsNotNull));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[startsWith]", edgeNameStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[notStartsWith]", edgeNameNotStartsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[endsWith]", edgeNameEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[notEndsWith]", edgeNameNotEndsWith));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[contains]", edgeNameContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[notContains]", edgeNameNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[in]", edgeNameIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "edgeName[notIn]", edgeNameNotIn));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EnterpriseEventsSchema> returnType = new ParameterizedTypeReference<EnterpriseEventsSchema>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch Customer-global network flow summary statistics &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param groupBy groupBy criteria according to which the result set is grouped. For example, a groupBy value of link produces an API response wherein each element corresponds to a distinct link record for which aggregate values are reported.
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt;
     * @param metrics metrics supported for querying flowStats
     * @return FlowStats
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public FlowStats getEnterpriseFlowMetrics(String enterpriseLogicalId, String groupBy, Integer start, String include, Integer end, String sortBy, String metrics) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseFlowMetrics");
        }
        // verify the required parameter 'groupBy' is set
        if (groupBy == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupBy' when calling getEnterpriseFlowMetrics");
        }
        // verify the required parameter 'start' is set
        if (start == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'start' when calling getEnterpriseFlowMetrics");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/flowStats").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "groupBy", groupBy));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sortBy", sortBy));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "metrics", metrics));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<FlowStats> returnType = new ParameterizedTypeReference<FlowStats>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch aggregate WAN link transport metrics for all Customer links &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param metrics metrics supported for querying linkStats
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt;
     * @param start Query interval start time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @param end Query interval end time represented as a 13-digit, millisecond-precision epoch timestamp.
     * @return LinkStats
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public LinkStats getEnterpriseLinkMetrics(String enterpriseLogicalId, String include, String metrics, String sortBy, Integer start, Integer end) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseLinkMetrics");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/linkStats").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "metrics", metrics));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sortBy", sortBy));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<LinkStats> returnType = new ParameterizedTypeReference<LinkStats>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch nonSDWAN service status &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param sortBy sortBy query param to sort the resultset. Format of sortBy is &lt;attribute&gt;&lt;ASC | DESC&gt;
     * @param state Filter by non-SD-WAN tunnel state
     * @param stateIs Filter by state[is]
     * @param stateIsNot Filter by state[isNot]
     * @param stateIn Filter by state[in]
     * @param stateNotIn Filter by state[notIn]
     * @return NonSdwanServiceStatus
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public NonSdwanServiceStatus getEnterpriseNonSdwanTunnelStatusViaEdge(String enterpriseLogicalId, String include, String sortBy, Object state, String stateIs, String stateIsNot, String stateIn, String stateNotIn) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getEnterpriseNonSdwanTunnelStatusViaEdge");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/edgeNonSdwanServices/status").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "sortBy", sortBy));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state", state));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[is]", stateIs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[isNot]", stateIsNot));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[in]", stateIn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state[notIn]", stateNotIn));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<NonSdwanServiceStatus> returnType = new ParameterizedTypeReference<NonSdwanServiceStatus>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * List Customers &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>400</b> - ValidationError
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @param nameContains Filter by name[contains]
     * @param nameNotContains Filter by name[notContains]
     * @param accountNumberContains Filter by accountNumber[contains]
     * @param accountNumberNotContains Filter by accountNumber[notContains]
     * @param domainContains Filter by domain[contains]
     * @param domainNotContains Filter by domain[notContains]
     * @return EnterpriseResourceCollection
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public EnterpriseResourceCollection getEnterprises(String include, String nameContains, String nameNotContains, String accountNumberContains, String accountNumberNotContains, String domainContains, String domainNotContains) throws RestClientException {
        Object postBody = null;
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "include", include));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[contains]", nameContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "name[notContains]", nameNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "accountNumber[contains]", accountNumberContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "accountNumber[notContains]", accountNumberNotContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "domain[contains]", domainContains));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "domain[notContains]", domainNotContains));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<EnterpriseResourceCollection> returnType = new ParameterizedTypeReference<EnterpriseResourceCollection>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Fetch Non-SD-WAN Sites connected via a Gateway &lt;br /&gt; &lt;br /&gt; 
     * <p><b>200</b> - Request successfully processed
     * <p><b>401</b> - Unauthorized
     * <p><b>404</b> - Resource not found
     * <p><b>429</b> - Rate Limit Exceeded
     * <p><b>500</b> - Internal server error
     * @param enterpriseLogicalId The &#x60;logicalId&#x60; GUID for the target enterprise
     * @param include A comma-separated list of field names corresponding to linked resources. Where supported, the server will resolve resource attributes for the specified resources.
     * @return GatewayNonSdWanSiteSchema
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public GatewayNonSdWanSiteSchema getNonSdwanTunnelsViaGateway(String enterpriseLogicalId, String include) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'enterpriseLogicalId' is set
        if (enterpriseLogicalId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'enterpriseLogicalId' when calling getNonSdwanTunnelsViaGateway");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("enterpriseLogicalId", enterpriseLogicalId);
        String path = UriComponentsBuilder.fromPath("/api/sdwan/v2/enterprises/{enterpriseLogicalId}/gatewayNonSdWanSites").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<GatewayNonSdWanSiteSchema> returnType = new ParameterizedTypeReference<GatewayNonSdWanSiteSchema>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
