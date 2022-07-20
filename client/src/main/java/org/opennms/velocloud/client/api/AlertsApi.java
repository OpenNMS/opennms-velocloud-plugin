package org.opennms.velocloud.client.api;

import org.opennms.velocloud.client.handler.ApiClient;

import org.opennms.velocloud.client.model.EnterpriseAlertsSchema;
import org.opennms.velocloud.client.model.InternalServerError;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")@Component("org.opennms.velocloud.client.api.AlertsApi")
public class AlertsApi {
    private ApiClient apiClient;

    public AlertsApi() {
        this(new ApiClient());
    }

    @Autowired
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
}
