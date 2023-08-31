package org.example.gateway;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.example.Configuration;

import java.net.URI;

public class DemoApiGateway implements Configuration {

    private final WireMock wireMock;

    public DemoApiGateway() {
        URI wireMockUri = URI.create(getMockDemoApiEndpoint());
        this.wireMock = new WireMock(wireMockUri.getScheme(), wireMockUri.getHost(), wireMockUri.getPort());
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
