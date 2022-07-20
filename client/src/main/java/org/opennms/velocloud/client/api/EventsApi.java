package org.opennms.velocloud.client.api;

import org.opennms.velocloud.client.handler.ApiClient;

import org.opennms.velocloud.client.model.EnterpriseEventsSchema;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-07-20T15:39:00.676358-04:00[America/Toronto]")@Component("org.opennms.velocloud.client.api.EventsApi")
public class EventsApi {
    private ApiClient apiClient;

    public EventsApi() {
        this(new ApiClient());
    }

    @Autowired
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
}
