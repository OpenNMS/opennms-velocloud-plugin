package org.opennms.velocloud.shell;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.opennms.velocloud.client.api.VelocloudApiClientProvider;

@Command(scope = "opennms-velocloud", name = "list-msp-customers", description = "List MSP Customers", detailedDescription = "List Enterprises for MSP")
@Service
public class VelocloudListCustomersCommand implements Action {

    @Reference
    private VelocloudApiClientProvider clientProvider;

    @Override
    public Object execute() throws Exception {
        //TODO: figure a way to get credentials in Karaf
        clientProvider.connect("", "").getEnterprises().forEach(e -> System.out.println(e));

        return null;
    }
}
