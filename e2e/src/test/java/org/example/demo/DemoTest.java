package org.example.demo;

import com.github.jknack.handlebars.internal.Files;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.client.WireMockBuilder;
import com.github.tomakehurst.wiremock.stubbing.StubImportBuilder;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.checkerframework.framework.qual.StubFiles;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class DemoTest {

    @Test
    void y() throws IOException, InterruptedException {
//        WireMock wireMock = new WireMock(8080);
        URI wireMockUri = URI.create("http://localhost:8080");
        WireMock wireMock = new WireMock(wireMockUri.getScheme(), wireMockUri.getHost(), wireMockUri.getPort());
        wireMock.resetMappings();
        wireMock.resetRequests();
        wireMock.resetScenarios();

        /*
        String read = Files.read(Path.of("fixtures", "cdc", "correct-contract.json").toFile(), StandardCharsets.UTF_8);
        System.out.println(read);
        wireMock.register(StubMapping.buildFrom(read));
         */
        wireMock.loadMappingsFrom(Path.of("fixtures", "cdc").toFile());

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/hello"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        System.out.println(response.statusCode());
        System.out.println(response.body());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/bye"))
                .GET()
                .build();
        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        System.out.println(response2.statusCode());
        System.out.println(response2.body());

    }

    @Test
    void z() throws IOException, InterruptedException {
        URI wireMockUri = URI.create("http://localhost:8080");
        WireMock wireMock = new WireMock(wireMockUri.getScheme(), wireMockUri.getHost(), wireMockUri.getPort());
        wireMock.resetMappings();
        wireMock.resetRequests();
        wireMock.resetScenarios();

        wireMock.loadMappingsFrom(Path.of("fixtures", "cdc", "salesforce-demo-api", "contract").toFile());

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/contract"))
                .GET()
                .header("accept", "application/json")
                .header("api-key", "correct-api-key")
                .header("organization-id", "foo-bar")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        System.out.println(response.statusCode());
        System.out.println(response.body());


        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/contract"))
                .GET()
                .header("accept", "application/json")
                .header("api-key", "wrong-api-key")
                .header("organization-id", "foo-bar")
                .build();
        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        System.out.println(response2.statusCode());
        System.out.println(response2.body());
    }
}
