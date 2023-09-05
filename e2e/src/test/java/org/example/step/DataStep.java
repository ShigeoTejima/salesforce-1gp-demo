package org.example.step;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import org.example.gateway.DemoApiSettingGateway;
import org.example.gateway.DemoGateway;
import org.example.model.Demo;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class DataStep {

    private final DemoGateway demoGateway;
    private final DemoApiSettingGateway demoApiSettingGateway;

    public DataStep() {
        this.demoGateway = new DemoGateway();
        this.demoApiSettingGateway = new DemoApiSettingGateway();
    }

    @Step("salesforce data - add demos for <records> .")
    public void addDemos(Table records) {
        List<Demo> demos = records.getTableRows().stream()
                .map(tableRow -> new Demo(
                    tableRow.getCell("Name"),
                    tableRow.getCell("demo_aho__description__c")
                ))
                .collect(Collectors.toList());

        if (!demos.isEmpty()) {
            demoGateway.add(demos);
        }
    }

    @Step("salesforce data - custom setting 'DemoApiSetting' api-key is <apiKey>.")
    public void verifyApiKeyOfDemoApiSetting(String apiKey) {
        String currentApiKey = this.demoApiSettingGateway.getCurrentApiKey();

        assertEquals(apiKey, currentApiKey);
    }
}
