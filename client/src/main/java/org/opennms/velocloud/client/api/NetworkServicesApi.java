package org.opennms.velocloud.client.api;

import org.opennms.velocloud.client.handler.ApiClient;

import org.opennms.velocloud.client.model.BgpSessionCollection;
import org.opennms.velocloud.client.model.EdgeNonSDWANTunnelStatus;
import org.opennms.velocloud.client.model.GatewayNonSdWanSiteSchema;
import org.opennms.velocloud.client.model.InternalServerError;
import org.opennms.velocloud.client.model.NonSdwanServiceStatus;
import org.opennms.velocloud.client.model.RateLimitExceededError;
import org.opennms.velocloud.client.model.ResourceNotFoundError;
import org.opennms.velocloud.client.model.UnAuthorized;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")@Component("org.opennms.velocloud.client.api.NetworkServicesApi")
public class NetworkServicesApi {
    private ApiClient apiClient;

    public NetworkServicesApi() {
        this(new ApiClient());
    }

    @Autowired
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
