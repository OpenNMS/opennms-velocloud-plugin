package org.opennms.velocloud.client;

import org.opennms.velocloud.client.api.AlertsApi;
import org.opennms.velocloud.client.api.ApplicationMapsApi;
import org.opennms.velocloud.client.api.ClientsApi;
import org.opennms.velocloud.client.api.ConfigureApi;
import org.opennms.velocloud.client.api.CustomerProfilesApi;
import org.opennms.velocloud.client.api.CustomersApi;
import org.opennms.velocloud.client.api.EdgeSpecificProfilesApi;
import org.opennms.velocloud.client.api.EdgesApi;
import org.opennms.velocloud.client.api.EventsApi;
import org.opennms.velocloud.client.api.MonitorApi;
import org.opennms.velocloud.client.api.NetworkServicesApi;
import org.opennms.velocloud.client.handler.ApiClient;
import org.opennms.velocloud.client.handler.Configuration;


public class VelocloudApiClient extends ApiClient {

    /**
     * Authentication parameter for ApiKeyAuth heather parameter name/key
     * @see org.opennms.velocloud.client.handler.auth.ApiKeyAuth
     */
    public static final String AUTH_HEADER_NAME = "Authorization";
    /**
     * Authentication parameter for ApiKeyAuth header location
     * @see org.opennms.velocloud.client.handler.auth.ApiKeyAuth
     */
    public static final String AUTH_HEADER_LOCATION = "header";
    /**
     * Authentication parameter for ApiKeyAuth used in header parameter value
     * @see org.opennms.velocloud.client.handler.auth.ApiKeyAuth
     */
    public static final String AUTH_HEADER_PREFIX = "Token";

    private AlertsApi alertsApi;
    private ApplicationMapsApi applicationMapsApi;
    private ClientsApi clientsApi;
    private ConfigureApi configureApi;
    private CustomerProfilesApi customerProfilesApi;
    private CustomersApi customersApi;
    private EdgesApi edgesApi;
    private EdgeSpecificProfilesApi edgeSpecificProfilesApi;
    private EventsApi eventsApi;
    private MonitorApi monitorApi;
    private NetworkServicesApi networkServicesApi;

    public VelocloudApiClient(final String url, final String apiKey) {
        super();
        setBasePath(url);
        setApiKeyPrefix(AUTH_HEADER_PREFIX);
        setApiKey(apiKey);
        Configuration.setDefaultApiClient(this);
    }

    public AlertsApi getAlertsApi(){
        if(alertsApi == null){
            alertsApi = new AlertsApi(this);
        }
        return alertsApi;
    }

    public ApplicationMapsApi getApplicationMapsApi() {
        if(applicationMapsApi == null){
            applicationMapsApi = new ApplicationMapsApi(this);
        }
        return applicationMapsApi;
    }

    public ClientsApi getClientsApi() {
        if(clientsApi == null){
            clientsApi = new ClientsApi(this);
        }
        return clientsApi;
    }

    public ConfigureApi getConfigureApi() {
        if(configureApi == null ){
            configureApi = new ConfigureApi(this);
        }
        return configureApi;
    }

    public CustomerProfilesApi getCustomerProfilesApi() {
        if(customerProfilesApi == null){
            customerProfilesApi = new CustomerProfilesApi(this);
        }
        return customerProfilesApi;
    }

    public CustomersApi getCustomersApi() {
        if(customersApi == null){
            customersApi = new CustomersApi(this);
        }
        return customersApi;
    }

    public EdgesApi getEdgesApi() {
        if(edgesApi == null){
            edgesApi = new EdgesApi(this);
        }
        return edgesApi;
    }

    public EdgeSpecificProfilesApi getEdgeSpecificProfilesApi() {
        if(edgeSpecificProfilesApi == null){
            edgeSpecificProfilesApi = new EdgeSpecificProfilesApi(this);
        }
        return edgeSpecificProfilesApi;
    }

    public EventsApi getEventsApi() {
        if(eventsApi == null){
            eventsApi = new EventsApi(this);
        }
        return eventsApi;
    }

    public MonitorApi getMonitorApi() {
        if(monitorApi == null ){
            monitorApi = new MonitorApi(this);
        }
        return monitorApi;
    }

    public NetworkServicesApi getNetworkServicesApi() {
        if(networkServicesApi == null){
            networkServicesApi = new NetworkServicesApi(this);
        }
        return networkServicesApi;
    }

}

