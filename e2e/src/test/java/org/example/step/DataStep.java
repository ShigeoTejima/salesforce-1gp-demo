package org.example.step;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import org.example.gateway.DemoGateway;
import org.example.model.Demo;

import java.util.List;
import java.util.stream.Collectors;

public class DataStep {

    private final DemoGateway demoGateway;

    public DataStep() {
        this.demoGateway = new DemoGateway();
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
}
