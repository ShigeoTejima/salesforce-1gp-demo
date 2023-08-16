package repository;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SalesforceRepository {

    private static final String apiVersion = "58.0";

    private final String instanceUrl;
    private final String accessToken;
    private final HttpClient httpClient;

    public SalesforceRepository() {
        String instanceUrl = System.getProperty("test.instanceUrl");
        String accessToken = System.getProperty("test.accessToken");
        Objects.requireNonNull(instanceUrl, "required system property 'test.instanceUrl'");
        Objects.requireNonNull(accessToken, "required system property 'test.accessToken'");

        this.instanceUrl = instanceUrl;
        this.accessToken = accessToken;
        this.httpClient = HttpClient.newHttpClient();

    }

    public void deleteRecords(List<String> recordIds) {
        String paramIds = recordIds.stream().collect(Collectors.joining(","));
        String url = String.format("%s/services/data/v%s/composite/sobjects?ids=%s", instanceUrl, apiVersion, paramIds);
        System.out.println(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            /*
             * example:
             *   status: 200
             *   response.body: [{"id":"a005D000009uyaOQAQ","success":true,"errors":[]},{"id":"a005D000009uyaTQAQ","success":true,"errors":[]},{"id":"a005D000009uyaYQAQ","success":true,"errors":[]},{"id":"a005D000009uyadQAA","success":true,"errors":[]}]
             */
            if (response.statusCode() != 200) {
                throw new RuntimeException(String.format("failed to delete records. %s", recordIds));
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public FindRecordsResult findRecords(String objectName) {
        String query = String.format("SELECT Id FROM %s", objectName);
        String url = String.format("%s/services/data/v%s/query?q=%s", instanceUrl, apiVersion, URLEncoder.encode(query, StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();
        try {
            HttpResponse<FindRecordsResult> response = httpClient.send(request, responseInfo ->
                    HttpResponse.BodySubscribers.mapping(
                            HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                            (body) -> new Gson().fromJson(body, FindRecordsResult.class)
                    ));

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException(String.format("failed to get records of %s", objectName));
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public InsertRecordResult insertRecord(String objectName, String fieldsJson) {
        String url = String.format("%s/services/data/v%s/sobjects/%s/", instanceUrl, apiVersion, objectName);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(fieldsJson))
                .build();
        try {
            HttpResponse<InsertRecordResult> response = httpClient.send(request, responseInfo ->
                    HttpResponse.BodySubscribers.mapping(
                            HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                            (body) -> new Gson().fromJson(body, InsertRecordResult.class)
                    ));
            if (response.statusCode() == 201) {
                return response.body();
            } else {
                throw new RuntimeException(String.format("failed to insert into %s", objectName));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public FindRecordsResult findPermissionSetAssignment(String userId, String permissionSetId) {
        String query = String.format("SELECT Id FROM PermissionSetAssignment WHERE AssigneeId='%s' AND PermissionSetId='%s'", userId, permissionSetId);
        String url = String.format("%s/services/data/v%s/query?q=%s", instanceUrl, apiVersion, URLEncoder.encode(query, StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();
        try {
            HttpResponse<FindRecordsResult> response = httpClient.send(request, responseInfo ->
                    HttpResponse.BodySubscribers.mapping(
                            HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                            (body) -> new Gson().fromJson(body, FindRecordsResult.class)
                    ));

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException(String.format("failed to get records of PermissionSetAssignment. {userId: %s, permissionSetId: %s}", userId, permissionSetId));
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public InsertRecordResult insertPermissionSetAssignment(String userId, String permissionSetId) {
        String url = String.format("%s/services/data/v%s/sobjects/PermissionSetAssignment/", instanceUrl, apiVersion);
        Map<String, String> permissionSetAssignment = Map.of("AssigneeId", userId, "PermissionSetId", permissionSetId);
        String fieldsJson = new Gson().toJson(permissionSetAssignment);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(fieldsJson))
                .build();
        try {
            HttpResponse<InsertRecordResult> response = httpClient.send(request, responseInfo ->
                    HttpResponse.BodySubscribers.mapping(
                            HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                            (body) -> new Gson().fromJson(body, InsertRecordResult.class)
                    ));
            if (response.statusCode() == 201) {
                return response.body();
            } else {
                throw new RuntimeException(String.format("failed to insert into PermissionSetAssignment {userId: %s, permissionSetId: %s}", userId, permissionSetId));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
