package org.example.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Configuration;
import org.example.repository.result.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GenericRepository implements Configuration {

    private final String instanceUrl;
    private final String apiVersion;
    private final String accessToken;
    private final HttpClient httpClient;

    public GenericRepository() {
        String instanceUrl = getInstanceUrl();
        String accessToken = getAdminUserAccessToken();
        String apiVersion = getApiVersion();
        Objects.requireNonNull(instanceUrl, "required instanceUrl");
        Objects.requireNonNull(accessToken, "required accessToken");
        Objects.requireNonNull(apiVersion, "required apiVersion");

        this.instanceUrl = instanceUrl;
        this.apiVersion = apiVersion;
        this.accessToken = accessToken;
        this.httpClient = HttpClient.newHttpClient();

    }

    public Result<DeleteRecordsResult, ErrorsResult> deleteRecords(List<String> recordIds) {
        Objects.requireNonNull(recordIds, "recordIds must be non-null.");

        String paramIds = recordIds.stream().collect(Collectors.joining(","));
        String url = String.format("%s/services/data/v%s/composite/sobjects?ids=%s", instanceUrl, apiVersion, paramIds);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .DELETE()
                .build();
        try {
            Gson gson = new Gson();
            HttpResponse<Result<DeleteRecordsResult, ErrorsResult>> response = httpClient.send(request, responseInfo ->
                switch (responseInfo.statusCode()) {
                    case 200 ->
                        HttpResponse.BodySubscribers.mapping(
                            HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                            (body) -> new Result.Success<>(new DeleteRecordsResult(gson.fromJson(body, new TypeToken<List<DeleteRecordResult>>() {}.getType())))
                        );
                    default -> mappingErrorsResult();
                }
            );

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Result<FindRecordsResult, ErrorsResult> findRecords(String objectName) {
        String query = String.format("SELECT Id FROM %s", objectName);
        return findRecords(objectName, query);
    }
    public Result<FindRecordsResult, ErrorsResult> findRecords(String objectName, String query) {
        String url = String.format("%s/services/data/v%s/query?q=%s", instanceUrl, apiVersion, URLEncoder.encode(query, StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();
        try {
            Gson gson = new Gson();
            HttpResponse<Result<FindRecordsResult, ErrorsResult>> response = httpClient.send(request, responseInfo ->
                switch (responseInfo.statusCode()) {
                    case 200 ->
                        HttpResponse.BodySubscribers.mapping(
                            HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                            (body) -> new Result.Success<>(gson.fromJson(body, FindRecordsResult.class))
                        );

                    default -> mappingErrorsResult();
                });

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public <T> Result<InsertRecordResult, ErrorsResult> insertRecord(String objectName, T record) {
        String url = String.format("%s/services/data/v%s/sobjects/%s/", instanceUrl, apiVersion, objectName);
        String bodyJson = new Gson().toJson(record);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                .build();
        try {
            Gson gson = new Gson();
            HttpResponse<Result<InsertRecordResult, ErrorsResult>> response = httpClient.send(request, responseInfo ->
                switch (responseInfo.statusCode()) {
                    case 201 ->
                        HttpResponse.BodySubscribers.mapping(
                            HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                            (body) -> new Result.Success<>(gson.fromJson(body, InsertRecordResult.class))
                        );

                    default -> mappingErrorsResult();
                });

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public <T> Result<InsertRecordResult, ErrorsResult> updateRecord(String objectName, String recordId, T updateContent) {
        String url = String.format("%s/services/data/v%s/sobjects/%s/%s", instanceUrl, apiVersion, objectName, recordId);
        String bodyJson = new Gson().toJson(updateContent);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(bodyJson))
                .build();
        try {
            Gson gson = new Gson();
            HttpResponse<Result<InsertRecordResult, ErrorsResult>> response = httpClient.send(request, responseInfo ->
                switch (responseInfo.statusCode()) {
                    case 204 ->
                        HttpResponse.BodySubscribers.mapping(
                            HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                            (body) -> new Result.Success<>(gson.fromJson(body, InsertRecordResult.class))
                        );

                    default -> mappingErrorsResult();
                });

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private <T> HttpResponse.BodySubscriber<Result<T, ErrorsResult>> mappingErrorsResult() {
        return HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                (body) -> new Result.Failure<>(new ErrorsResult(new Gson().fromJson(body, new TypeToken<List<ErrorResult>>() {}.getType())))
        );
    }

}
