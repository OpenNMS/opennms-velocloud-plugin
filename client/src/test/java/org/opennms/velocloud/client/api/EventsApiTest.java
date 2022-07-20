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

import org.opennms.velocloud.client.model.EnterpriseEventsSchema;
import org.opennms.velocloud.client.model.InternalServerError;
import org.opennms.velocloud.client.model.RateLimitExceededError;
import org.opennms.velocloud.client.model.ResourceNotFoundError;
import org.opennms.velocloud.client.model.UnAuthorized;
import org.opennms.velocloud.client.model.ValidationError;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for EventsApi
 */
@Ignore
public class EventsApiTest {

    private final EventsApi api = new EventsApi();

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
}
