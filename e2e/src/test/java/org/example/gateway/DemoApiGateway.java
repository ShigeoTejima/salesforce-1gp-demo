package org.example.gateway;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.example.Configuration;

public class DemoApiGateway implements Configuration {

    private final WireMock wireMock;

    public DemoApiGateway() {
        this.wireMock = new WireMock(MockDemoApi.port());
    }

    public void resetAll() {
        this.wireMock.resetScenarios();
        this.wireMock.resetRequests();
        this.wireMock.resetMappings();
    }

    public void loadMappings(String rootDirOfMappings) {
        this.wireMock.loadMappingsFrom(rootDirOfMappings);
    }
}
