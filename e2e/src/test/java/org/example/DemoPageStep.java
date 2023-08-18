package org.example;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.selector.ByShadow;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DemoPageStep {

    @Step("demo page - open page.")
    public void openDemoPage() {
        open("/lightning/n/demo_ahd__demo_page");

        $(ByShadow.cssSelector("article h2.slds-card__header-title",
                "demo_ahd-demo_page",
                "lightning-card"))
                .shouldBe(Condition.exactText("Demo"));
    }

    @Step("demo page - cannot open page.")
    public void cannotOpenDemoPage() {
        open("/lightning/n/demo_ahd__demo_page");

        $(ByShadow.cssSelector("article h2.slds-card__header-title",
                "demo_ahd-demo_page",
                "lightning-card"))
                .shouldBe(Condition.not(Condition.visible));
    }

    @Step("demo page - list of demo is empty.")
    public void verifyDemoRecordsEmpty() {
        SelenideElement datatable = getDemoDatatable();
        getDemoRows(datatable).shouldBe(CollectionCondition.empty);
    }

    @Step("demo page - list of demo has records for <records> .")
    public void verifyDemoRecordsIsExists(Table records) {
        SelenideElement datatable = getDemoDatatable();
        getDemoRows(datatable).shouldBe(CollectionCondition.size(records.getTableRows().size()));

        SelenideElement tbody = getDemoDatatableBody(datatable);
        List<String> expectedNames = records.getTableRows().stream()
                .map(tableRow -> tableRow.getCell("Name"))
                .collect(Collectors.toList());
        tbody.$$("tr th[data-label=Name]")
                .shouldBe(CollectionCondition.exactTexts(expectedNames));

        List<String> expectedDescription = records.getTableRows().stream()
                .map(tableRow -> tableRow.getCell("Description"))
                .collect(Collectors.toList());
        tbody.$$("tr td[data-label=Description]")
                .shouldBe(CollectionCondition.exactTexts(expectedDescription));
    }

    private SelenideElement getDemoDatatable() {
        return $(ByShadow.cssSelector("lightning-card lightning-datatable",
                "demo_ahd-demo_page"));
    }

    private SelenideElement getDemoDatatableBody(SelenideElement datatable) {
        return $(datatable.getShadowRoot()
                .findElement(By.cssSelector("tbody[lightning-datatable_table][data-rowgroup-body]")));
    }

    private ElementsCollection getDemoRows(SelenideElement datatable) {
        return getDemoDatatableBody(datatable).$$("tr");
    }
}
