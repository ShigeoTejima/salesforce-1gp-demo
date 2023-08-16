import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import gateway.SalesforceGateway;
import model.Demo;

import java.util.List;
import java.util.stream.Collectors;

public class SalesforceDataStep {

    private final SalesforceGateway salesforceGateway;

    public SalesforceDataStep() {
        this.salesforceGateway = new SalesforceGateway();
    }

    @Step("salesforce data - insert demos for <records> .")
    public void insertDemos(Table records) {
        List<Demo> demos = records.getTableRows().stream()
                .map(tableRow -> new Demo(
                    tableRow.getCell("Name"),
                    tableRow.getCell("demo_ahd__description__c")
                ))
                .collect(Collectors.toList());

        if (!demos.isEmpty()) {
            salesforceGateway.insertDemos(demos);
        }
    }
}
