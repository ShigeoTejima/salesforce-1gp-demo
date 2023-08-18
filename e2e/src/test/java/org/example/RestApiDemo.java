package org.example;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;
import org.example.repository.InsertRecordResult;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/*
 * NOTE: Salesforce APIの動作を確認するためのテストクラス.
 */
public class RestApiDemo {

    @Test
    public void getDemos() throws IOException, InterruptedException {
        Dotenv dotenv = Dotenv.configure()
                .filename(".env-local")
                .load();
        String instanceUrl = dotenv.get("test.instanceUrl");
        String accessToken = dotenv.get("test.accessToken");

        System.out.println(instanceUrl);
        System.out.println(accessToken);

        HttpClient httpClient = HttpClient.newHttpClient();

        /*
        curl https://MyDomainName.my.salesforce.com/services/data/v58.0/query?q=SELECT+name+from+Account -H "Authorization: Bearer access_token"
         */
        String query = "SELECT Id, Name, demo_ahd__description__c FROM demo_ahd__demo__c ORDER BY Name ASC";
        String getUrl = String.format("%s/services/data/v58.0/query?q=%s", instanceUrl, URLEncoder.encode(query, StandardCharsets.UTF_8));
        System.out.println(getUrl);

        HttpRequest request = HttpRequest.newBuilder(URI.create(getUrl))
                .header("Authorization", "Bearer " + accessToken)
                .build();
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<DemoResourcesDto> response = httpClient.send(request, responseInfo ->
                HttpResponse.BodySubscribers.mapping(
                        HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                        (body) -> new Gson().fromJson(body, DemoResourcesDto.class)
                ));

        System.out.println(response);
        System.out.println(response.body());
    }

    @Test
    public void postDemo() throws IOException, InterruptedException {
        Dotenv dotenv = Dotenv.configure()
                .filename(".env-local")
                .load();
        String instanceUrl = dotenv.get("test.instanceUrl");
        String accessToken = dotenv.get("test.accessToken");

        System.out.println(instanceUrl);
        System.out.println(accessToken);

        HttpClient httpClient = HttpClient.newHttpClient();

        /*
        curl https://MyDomainName.my.salesforce.com/services/data/v58.0/sobjects/Account/ -H "Authorization: Bearer token" -H "Content-Type: application/json" -d "@newaccount.json"
         */
        String postUrl = String.format("%s/services/data/v58.0/sobjects/demo_ahd__demo__c/", instanceUrl);
        PostDemoResourceDto postDemoResourceDto = new PostDemoResourceDto();
        postDemoResourceDto.name = "Qux";
//        postDemoResourceDto.name = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        postDemoResourceDto.description = "QuxQuxQux";
        String requestBody = new Gson().toJson(postDemoResourceDto);
        System.out.println(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(postUrl))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<InsertRecordResult> response = httpClient.send(request, responseInfo ->
                HttpResponse.BodySubscribers.mapping(
                        HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                        (body) -> new Gson().fromJson(body, InsertRecordResult.class)
                ));
        System.out.println(response);
        System.out.println(response.body().id);
        System.out.println(response.body().success);
    }

    @Test
    public void deleteDemo() throws IOException, InterruptedException {
        Dotenv dotenv = Dotenv.configure()
                .filename(".env-local")
                .load();
        String instanceUrl = dotenv.get("test.instanceUrl");
        String accessToken = dotenv.get("test.accessToken");

        System.out.println(instanceUrl);
        System.out.println(accessToken);

        HttpClient httpClient = HttpClient.newHttpClient();

        /*
        curl https://MyDomainName.my.salesforce.com/services/data/v58.0/sobjects/Account/001D000000INjVe -H "Authorization: Bearer token" -X DELETE
         */
        /*
        /services/data/vXX.X/composite/sobjects?ids=recordId,recordId
         */
        List<String> deleteIds = List.of("a005D000009uw70QAA", "a005D000009uw71QAA", "a005D000009uw6zQAA");
        String deleteIdParam = deleteIds.stream().collect(Collectors.joining(","));
//        String deleteUrl = String.format("%s/services/data/v58.0/sobjects/demo_ahd__demo__c/%s", instanceUrl, deleteIdParam);
        String deleteUrl = String.format("%s/services/data/v58.0/composite/sobjects?ids=%s", instanceUrl, deleteIdParam);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(deleteUrl))
                .header("Authorization", "Bearer " + accessToken)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
        System.out.println(response.body());
    }

    public static class DemoResourcesDto {
        public int totalSize;
        public boolean done;
        public List<Record> records;

        @Override
        public String toString() {
            return "DemoResourcesDto{" +
                    "totalSize=" + totalSize +
                    ", done=" + done +
                    ", records=" + records +
                    '}';
        }

        public static class Record {
            public Attributes attributes;

            @SerializedName("Id")
            public String id;

            @SerializedName("Name")
            public String name;

            public String demo_ahd__description__c;

            @Override
            public String toString() {
                return "Record{" +
                        "attributes=" + attributes +
                        ", id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", demo_ahd__description__c='" + demo_ahd__description__c + '\'' +
                        '}';
            }
        }

        public static class Attributes {
            public String type;
            public String url;

            @Override
            public String toString() {
                return "Attributes{" +
                        "type='" + type + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }
        }
    }

    public static class PostDemoResourceDto {
        @SerializedName("Name")
        public String name;

        @SerializedName("demo_ahd__description__c")
        public String description;
    }
}
