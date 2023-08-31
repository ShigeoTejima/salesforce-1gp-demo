package org.example.step;

import com.thoughtworks.gauge.Step;
import org.example.gateway.DemoApiGateway;

public class DemoApiStep {

    private final DemoApiGateway gateway;

    public DemoApiStep() {
        this.gateway = new DemoApiGateway();
    }

    @Step("demo-api - setup mappings by <rootOfMapping> .")
    public void setupMappings(String rootOfMapping) {
        this.gateway.loadMappings(rootOfMapping);
    }

}
