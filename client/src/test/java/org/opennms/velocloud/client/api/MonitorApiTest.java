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

package org.opennms.velocloud.client.api;

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
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for MonitorApi
 */
@Ignore
public class MonitorApiTest {

    private final MonitorApi api = new MonitorApi();

    /**
     * 
     *
     * Create a Customer &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createEnterpriseTest() {
        CreateEnterpriseSchema body = null;
        String include = null;
        EnterpriseResource response = api.createEnterprise(body, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * Delete a Customer &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteEnterpriseTest() {
        String enterpriseLogicalId = null;
        String include = null;
        api.deleteEnterprise(enterpriseLogicalId, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * [OPERATOR ONLY] Fetch application map resource &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getApplicationMapTest() {
        String logicalId = null;
        String include = null;
        ApplicationMapResource response = api.getApplicationMap(logicalId, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * [OPERATOR ONLY] Fetch an application definition for the target application map &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getApplicationMapApplicationTest() {
        String logicalId = null;
        Integer appId = null;
        String include = null;
        ApplicationResource response = api.getApplicationMapApplication(logicalId, appId, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch application map classes &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getApplicationMapApplicationClassesTest() {
        String logicalId = null;
        String include = null;
        ApplicationClasses response = api.getApplicationMapApplicationClasses(logicalId, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * [OPERATOR ONLY] List applications associated with an application map &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getApplicationMapApplicationsTest() {
        String logicalId = null;
        String include = null;
        Applications response = api.getApplicationMapApplications(logicalId, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch edge applications &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEdgeApplicationTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        Integer appId = null;
        String include = null;
        Applications response = api.getEdgeApplication(enterpriseLogicalId, edgeLogicalId, appId, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch edge applications &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEdgeApplicationsTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        String include = null;
        Applications response = api.getEdgeApplications(enterpriseLogicalId, edgeLogicalId, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch Edge flow stats &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEdgeFlowMetricsTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        String groupBy = null;
        Integer start = null;
        String include = null;
        Integer end = null;
        String sortBy = null;
        String metrics = null;
        FlowStats response = api.getEdgeFlowMetrics(enterpriseLogicalId, edgeLogicalId, groupBy, start, include, end, sortBy, metrics);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch edge flow stats series &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEdgeFlowSeriesTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        String groupBy = null;
        Integer start = null;
        String metrics = null;
        String include = null;
        Integer end = null;
        String sortBy = null;
        FlowStatsSeries response = api.getEdgeFlowSeries(enterpriseLogicalId, edgeLogicalId, groupBy, start, metrics, include, end, sortBy);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch edge system resource metrics &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEdgeHealthMetricsTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        String include = null;
        Integer start = null;
        Integer end = null;
        String metrics = null;
        EdgeHealthStatsMetricsSchema response = api.getEdgeHealthMetrics(enterpriseLogicalId, edgeLogicalId, include, start, end, metrics);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch system resource metric time series data &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEdgeHealthSeriesTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        String include = null;
        Integer start = null;
        Integer end = null;
        String metrics = null;
        EdgeHealthStatsSeriesSchema response = api.getEdgeHealthSeries(enterpriseLogicalId, edgeLogicalId, include, start, end, metrics);

        // TODO: test validations
    }
    /**
     * 
     *
     * Edge WAN link transport metrics &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEdgeLinkMetricsTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        String include = null;
        String metrics = null;
        String sortBy = null;
        Integer start = null;
        Integer end = null;
        LinkStats response = api.getEdgeLinkMetrics(enterpriseLogicalId, edgeLogicalId, include, metrics, sortBy, start, end);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch link quality scores for an Edge &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEdgeLinkQualityEventsTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        Integer start = null;
        String include = null;
        Integer end = null;
        LinkQualityStatsResource response = api.getEdgeLinkQualityEvents(enterpriseLogicalId, edgeLogicalId, start, include, end);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch link quality timeseries for an Edge &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEdgeLinkQualitySeriesTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        Integer start = null;
        String include = null;
        Integer end = null;
        Integer minutesPerSample = null;
        LinkQualityStatsTimeSeriesResource response = api.getEdgeLinkQualitySeries(enterpriseLogicalId, edgeLogicalId, start, include, end, minutesPerSample);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch edge linkStats time series &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEdgeLinkSeriesTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        Integer start = null;
        String include = null;
        String metrics = null;
        String sortBy = null;
        Integer end = null;
        LinkStatsSeries response = api.getEdgeLinkSeries(enterpriseLogicalId, edgeLogicalId, start, include, metrics, sortBy, end);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch Edge non-SD-WAN tunnel status &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEdgeNonSdwanTunnelStatusViaEdgeTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        String include = null;
        String sortBy = null;
        Integer start = null;
        Integer end = null;
        Object state = null;
        String stateIs = null;
        String stateIsNot = null;
        String stateIn = null;
        String stateNotIn = null;
        EdgeNonSDWANTunnelStatus response = api.getEdgeNonSdwanTunnelStatusViaEdge(enterpriseLogicalId, edgeLogicalId, include, sortBy, start, end, state, stateIs, stateIsNot, stateIn, stateNotIn);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch path stats metrics from an edge &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEdgePathMetricsTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        String peerLogicalId = null;
        String include = null;
        Integer start = null;
        Integer end = null;
        String metrics = null;
        Integer limit = null;
        EdgePathStatsMetrics response = api.getEdgePathMetrics(enterpriseLogicalId, edgeLogicalId, peerLogicalId, include, start, end, metrics, limit);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch path stats time series from an edge &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEdgePathSeriesTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        String peerLogicalId = null;
        String include = null;
        Integer start = null;
        Integer end = null;
        Integer maxSamples = null;
        String metrics = null;
        Integer limit = null;
        String groupBy = null;
        EdgePathStatsSeries response = api.getEdgePathSeries(enterpriseLogicalId, edgeLogicalId, peerLogicalId, include, start, end, maxSamples, metrics, limit, groupBy);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch a customer &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseTest() {
        String enterpriseLogicalId = null;
        String include = null;
        EnterpriseResource response = api.getEnterprise(enterpriseLogicalId, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch aggregate Edge health metrics for all Edges belonging to the target customer &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseAggregateHealthStatsTest() {
        String enterpriseLogicalId = null;
        String include = null;
        String metrics = null;
        EnterpriseHealthStatsSchema response = api.getEnterpriseAggregateHealthStats(enterpriseLogicalId, include, metrics);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch past triggered alerts for the specified enterprise &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseAlertsTest() {
        String enterpriseLogicalId = null;
        Integer start = null;
        Integer end = null;
        Integer limit = null;
        String nextPageLink = null;
        String prevPageLink = null;
        String include = null;
        String edgeNameContains = null;
        String edgeNameNotContains = null;
        String linkNameContains = null;
        String linkNameNotContains = null;
        String typeIs = null;
        String typeIsNot = null;
        String typeIn = null;
        String typeNotIn = null;
        String stateIs = null;
        String stateIsNot = null;
        String stateIn = null;
        String stateNotIn = null;
        Float enterpriseAlertConfigurationIdIsNull = null;
        Float enterpriseAlertConfigurationIdIsNotNull = null;
        EnterpriseAlertsSchema response = api.getEnterpriseAlerts(enterpriseLogicalId, start, end, limit, nextPageLink, prevPageLink, include, edgeNameContains, edgeNameNotContains, linkNameContains, linkNameNotContains, typeIs, typeIsNot, typeIn, typeNotIn, stateIs, stateIsNot, stateIn, stateNotIn, enterpriseAlertConfigurationIdIsNull, enterpriseAlertConfigurationIdIsNotNull);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch enterprise applications &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseApplicationByIdTest() {
        String enterpriseLogicalId = null;
        Integer appId = null;
        String include = null;
        Applications response = api.getEnterpriseApplicationById(enterpriseLogicalId, appId, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch enterprise applications &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseApplicationsTest() {
        String enterpriseLogicalId = null;
        String include = null;
        Applications response = api.getEnterpriseApplications(enterpriseLogicalId, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * Get BGP peering session state &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseBgpSessionsTest() {
        String enterpriseLogicalId = null;
        String include = null;
        String peerType = null;
        String stateIs = null;
        String stateIsNot = null;
        String stateIn = null;
        String stateNotIn = null;
        String neighborIpIs = null;
        String neighborIpIsNot = null;
        String neighborIpIsNull = null;
        String neighborIpIsNotNull = null;
        String neighborIpStartsWith = null;
        String neighborIpNotStartsWith = null;
        String neighborIpEndsWith = null;
        String neighborIpNotEndsWith = null;
        String neighborIpContains = null;
        String neighborIpNotContains = null;
        String neighborIpIn = null;
        String neighborIpNotIn = null;
        BgpSessionCollection response = api.getEnterpriseBgpSessions(enterpriseLogicalId, include, peerType, stateIs, stateIsNot, stateIn, stateNotIn, neighborIpIs, neighborIpIsNot, neighborIpIsNull, neighborIpIsNotNull, neighborIpStartsWith, neighborIpNotStartsWith, neighborIpEndsWith, neighborIpNotEndsWith, neighborIpContains, neighborIpNotContains, neighborIpIn, neighborIpNotIn);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch a client device &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseClientDeviceTest() {
        String enterpriseLogicalId = null;
        String logicalId = null;
        String include = null;
        ClientDeviceResource response = api.getEnterpriseClientDevice(enterpriseLogicalId, logicalId, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch all client devices &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseClientDevicesTest() {
        String enterpriseLogicalId = null;
        String include = null;
        ClientDevices response = api.getEnterpriseClientDevices(enterpriseLogicalId, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * Get an Edge &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseEdgeTest() {
        String enterpriseLogicalId = null;
        String edgeLogicalId = null;
        String include = null;
        EdgeResource response = api.getEnterpriseEdge(enterpriseLogicalId, edgeLogicalId, include);

        // TODO: test validations
    }
    /**
     * 
     *
     * List Customer Edges &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseEdgesTest() {
        String enterpriseLogicalId = null;
        String include = null;
        String nameIs = null;
        String nameIsNot = null;
        String nameIsNull = null;
        String nameIsNotNull = null;
        String nameStartsWith = null;
        String nameNotStartsWith = null;
        String nameEndsWith = null;
        String nameNotEndsWith = null;
        String nameContains = null;
        String nameNotContains = null;
        String nameIn = null;
        String nameNotIn = null;
        Integer createdIs = null;
        Integer createdIsNot = null;
        Integer createdIsNull = null;
        Integer createdIsNotNull = null;
        Integer createdGreaterOrEquals = null;
        Integer createdLesserOrEquals = null;
        Integer createdIn = null;
        Integer createdNotIn = null;
        String activationStateIs = null;
        String activationStateIsNot = null;
        String activationStateIn = null;
        String activationStateNotIn = null;
        String modelNumberIs = null;
        String modelNumberIsNot = null;
        String modelNumberIn = null;
        String modelNumberNotIn = null;
        String edgeStateIs = null;
        String edgeStateIsNot = null;
        String edgeStateIn = null;
        String edgeStateNotIn = null;
        String buildNumberIs = null;
        String buildNumberIsNot = null;
        String buildNumberIsNull = null;
        String buildNumberIsNotNull = null;
        String buildNumberStartsWith = null;
        String buildNumberNotStartsWith = null;
        String buildNumberEndsWith = null;
        String buildNumberNotEndsWith = null;
        String buildNumberContains = null;
        String buildNumberNotContains = null;
        String buildNumberIn = null;
        String buildNumberNotIn = null;
        String softwareVersionIs = null;
        String softwareVersionIsNot = null;
        String softwareVersionIsNull = null;
        String softwareVersionIsNotNull = null;
        String softwareVersionStartsWith = null;
        String softwareVersionNotStartsWith = null;
        String softwareVersionEndsWith = null;
        String softwareVersionNotEndsWith = null;
        String softwareVersionContains = null;
        String softwareVersionNotContains = null;
        String softwareVersionIn = null;
        String softwareVersionNotIn = null;
        String serialNumberIs = null;
        String serialNumberIsNot = null;
        String serialNumberIsNull = null;
        String serialNumberIsNotNull = null;
        String serialNumberStartsWith = null;
        String serialNumberNotStartsWith = null;
        String serialNumberEndsWith = null;
        String serialNumberNotEndsWith = null;
        String serialNumberContains = null;
        String serialNumberNotContains = null;
        String serialNumberIn = null;
        String serialNumberNotIn = null;
        Integer softwareUpdatedIs = null;
        Integer softwareUpdatedIsNot = null;
        Integer softwareUpdatedIsNull = null;
        Integer softwareUpdatedIsNotNull = null;
        Integer softwareUpdatedGreaterOrEquals = null;
        Integer softwareUpdatedLesserOrEquals = null;
        Integer softwareUpdatedIn = null;
        Integer softwareUpdatedNotIn = null;
        String customInfoIs = null;
        String customInfoIsNot = null;
        String customInfoIsNull = null;
        String customInfoIsNotNull = null;
        String customInfoStartsWith = null;
        String customInfoNotStartsWith = null;
        String customInfoEndsWith = null;
        String customInfoNotEndsWith = null;
        String customInfoContains = null;
        String customInfoNotContains = null;
        String customInfoIn = null;
        String customInfoNotIn = null;
        EdgeResourceCollection response = api.getEnterpriseEdges(enterpriseLogicalId, include, nameIs, nameIsNot, nameIsNull, nameIsNotNull, nameStartsWith, nameNotStartsWith, nameEndsWith, nameNotEndsWith, nameContains, nameNotContains, nameIn, nameNotIn, createdIs, createdIsNot, createdIsNull, createdIsNotNull, createdGreaterOrEquals, createdLesserOrEquals, createdIn, createdNotIn, activationStateIs, activationStateIsNot, activationStateIn, activationStateNotIn, modelNumberIs, modelNumberIsNot, modelNumberIn, modelNumberNotIn, edgeStateIs, edgeStateIsNot, edgeStateIn, edgeStateNotIn, buildNumberIs, buildNumberIsNot, buildNumberIsNull, buildNumberIsNotNull, buildNumberStartsWith, buildNumberNotStartsWith, buildNumberEndsWith, buildNumberNotEndsWith, buildNumberContains, buildNumberNotContains, buildNumberIn, buildNumberNotIn, softwareVersionIs, softwareVersionIsNot, softwareVersionIsNull, softwareVersionIsNotNull, softwareVersionStartsWith, softwareVersionNotStartsWith, softwareVersionEndsWith, softwareVersionNotEndsWith, softwareVersionContains, softwareVersionNotContains, softwareVersionIn, softwareVersionNotIn, serialNumberIs, serialNumberIsNot, serialNumberIsNull, serialNumberIsNotNull, serialNumberStartsWith, serialNumberNotStartsWith, serialNumberEndsWith, serialNumberNotEndsWith, serialNumberContains, serialNumberNotContains, serialNumberIn, serialNumberNotIn, softwareUpdatedIs, softwareUpdatedIsNot, softwareUpdatedIsNull, softwareUpdatedIsNotNull, softwareUpdatedGreaterOrEquals, softwareUpdatedLesserOrEquals, softwareUpdatedIn, softwareUpdatedNotIn, customInfoIs, customInfoIsNot, customInfoIsNull, customInfoIsNotNull, customInfoStartsWith, customInfoNotStartsWith, customInfoEndsWith, customInfoNotEndsWith, customInfoContains, customInfoNotContains, customInfoIn, customInfoNotIn);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch time-ordered list of events &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseEventsTest() {
        String enterpriseLogicalId = null;
        Integer start = null;
        Integer end = null;
        Integer limit = null;
        String nextPageLink = null;
        String prevPageLink = null;
        String include = null;
        String eventIs = null;
        String eventIsNot = null;
        String eventIn = null;
        String eventNotIn = null;
        String severityIs = null;
        String severityIsNot = null;
        String severityIn = null;
        String severityNotIn = null;
        String enterpriseUsernameIs = null;
        String enterpriseUsernameIsNot = null;
        String enterpriseUsernameIsNull = null;
        String enterpriseUsernameIsNotNull = null;
        String enterpriseUsernameStartsWith = null;
        String enterpriseUsernameNotStartsWith = null;
        String enterpriseUsernameEndsWith = null;
        String enterpriseUsernameNotEndsWith = null;
        String enterpriseUsernameContains = null;
        String enterpriseUsernameNotContains = null;
        String enterpriseUsernameIn = null;
        String enterpriseUsernameNotIn = null;
        String detailIs = null;
        String detailIsNot = null;
        String detailIsNull = null;
        String detailIsNotNull = null;
        String detailStartsWith = null;
        String detailNotStartsWith = null;
        String detailEndsWith = null;
        String detailNotEndsWith = null;
        String detailContains = null;
        String detailNotContains = null;
        String detailIn = null;
        String detailNotIn = null;
        String segmentNameIs = null;
        String segmentNameIsNot = null;
        String segmentNameIsNull = null;
        String segmentNameIsNotNull = null;
        String segmentNameStartsWith = null;
        String segmentNameNotStartsWith = null;
        String segmentNameEndsWith = null;
        String segmentNameNotEndsWith = null;
        String segmentNameContains = null;
        String segmentNameNotContains = null;
        String segmentNameIn = null;
        String segmentNameNotIn = null;
        String messageIs = null;
        String messageIsNot = null;
        String messageIsNull = null;
        String messageIsNotNull = null;
        String messageStartsWith = null;
        String messageNotStartsWith = null;
        String messageEndsWith = null;
        String messageNotEndsWith = null;
        String messageContains = null;
        String messageNotContains = null;
        String messageIn = null;
        String messageNotIn = null;
        String edgeNameIs = null;
        String edgeNameIsNot = null;
        String edgeNameIsNull = null;
        String edgeNameIsNotNull = null;
        String edgeNameStartsWith = null;
        String edgeNameNotStartsWith = null;
        String edgeNameEndsWith = null;
        String edgeNameNotEndsWith = null;
        String edgeNameContains = null;
        String edgeNameNotContains = null;
        String edgeNameIn = null;
        String edgeNameNotIn = null;
        EnterpriseEventsSchema response = api.getEnterpriseEvents(enterpriseLogicalId, start, end, limit, nextPageLink, prevPageLink, include, eventIs, eventIsNot, eventIn, eventNotIn, severityIs, severityIsNot, severityIn, severityNotIn, enterpriseUsernameIs, enterpriseUsernameIsNot, enterpriseUsernameIsNull, enterpriseUsernameIsNotNull, enterpriseUsernameStartsWith, enterpriseUsernameNotStartsWith, enterpriseUsernameEndsWith, enterpriseUsernameNotEndsWith, enterpriseUsernameContains, enterpriseUsernameNotContains, enterpriseUsernameIn, enterpriseUsernameNotIn, detailIs, detailIsNot, detailIsNull, detailIsNotNull, detailStartsWith, detailNotStartsWith, detailEndsWith, detailNotEndsWith, detailContains, detailNotContains, detailIn, detailNotIn, segmentNameIs, segmentNameIsNot, segmentNameIsNull, segmentNameIsNotNull, segmentNameStartsWith, segmentNameNotStartsWith, segmentNameEndsWith, segmentNameNotEndsWith, segmentNameContains, segmentNameNotContains, segmentNameIn, segmentNameNotIn, messageIs, messageIsNot, messageIsNull, messageIsNotNull, messageStartsWith, messageNotStartsWith, messageEndsWith, messageNotEndsWith, messageContains, messageNotContains, messageIn, messageNotIn, edgeNameIs, edgeNameIsNot, edgeNameIsNull, edgeNameIsNotNull, edgeNameStartsWith, edgeNameNotStartsWith, edgeNameEndsWith, edgeNameNotEndsWith, edgeNameContains, edgeNameNotContains, edgeNameIn, edgeNameNotIn);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch Customer-global network flow summary statistics &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseFlowMetricsTest() {
        String enterpriseLogicalId = null;
        String groupBy = null;
        Integer start = null;
        String include = null;
        Integer end = null;
        String sortBy = null;
        String metrics = null;
        FlowStats response = api.getEnterpriseFlowMetrics(enterpriseLogicalId, groupBy, start, include, end, sortBy, metrics);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch aggregate WAN link transport metrics for all Customer links &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseLinkMetricsTest() {
        String enterpriseLogicalId = null;
        String include = null;
        String metrics = null;
        String sortBy = null;
        Integer start = null;
        Integer end = null;
        LinkStats response = api.getEnterpriseLinkMetrics(enterpriseLogicalId, include, metrics, sortBy, start, end);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch nonSDWAN service status &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterpriseNonSdwanTunnelStatusViaEdgeTest() {
        String enterpriseLogicalId = null;
        String include = null;
        String sortBy = null;
        Object state = null;
        String stateIs = null;
        String stateIsNot = null;
        String stateIn = null;
        String stateNotIn = null;
        NonSdwanServiceStatus response = api.getEnterpriseNonSdwanTunnelStatusViaEdge(enterpriseLogicalId, include, sortBy, state, stateIs, stateIsNot, stateIn, stateNotIn);

        // TODO: test validations
    }
    /**
     * 
     *
     * List Customers &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEnterprisesTest() {
        String include = null;
        String nameContains = null;
        String nameNotContains = null;
        String accountNumberContains = null;
        String accountNumberNotContains = null;
        String domainContains = null;
        String domainNotContains = null;
        EnterpriseResourceCollection response = api.getEnterprises(include, nameContains, nameNotContains, accountNumberContains, accountNumberNotContains, domainContains, domainNotContains);

        // TODO: test validations
    }
    /**
     * 
     *
     * Fetch Non-SD-WAN Sites connected via a Gateway &lt;br /&gt; &lt;br /&gt; 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getNonSdwanTunnelsViaGatewayTest() {
        String enterpriseLogicalId = null;
        String include = null;
        GatewayNonSdWanSiteSchema response = api.getNonSdwanTunnelsViaGateway(enterpriseLogicalId, include);

        // TODO: test validations
    }
}
