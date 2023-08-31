package org.example.step;

import com.thoughtworks.gauge.Step;
import org.example.gateway.DemoApiGateway;

public class SalesforceDemoApiStep {

    private final DemoApiGateway gateway;

    public SalesforceDemoApiStep() {
        this.gateway = new DemoApiGateway();
    }

    @Step("salesforce-demo-api - setup mappings by <rootOfMapping> .")
    public void setupMappings(String rootOfMapping) {
        this.gateway.loadMappings(rootOfMapping);
    }

}
