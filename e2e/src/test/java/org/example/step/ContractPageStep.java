package org.example.step;

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

public class ContractPageStep {

    private static final String PATH_PAGE = "/lightning/n/demo_aho__contract_page";

    @Step("contract page - open page.")
    public void openPage() {
        open(PATH_PAGE);

        getTitle().shouldBe(Condition.exactText("Contract"));
    }

    @Step("contract page - cannot open page.")
    public void cannotOpenPage() {
        open(PATH_PAGE);

        getTitle().shouldBe(Condition.not(Condition.visible));
    }

    @Step("contract page - list of contract is empty.")
    public void verifyDemoRecordsEmpty() {
        SelenideElement datatable = getContractDatatable();
        getContractRows(datatable).shouldBe(CollectionCondition.empty);
    }

    @Step("contract page - list of contract has record for <records> .")
    public void verifyDemoRecordsIsExists(Table records) {
        SelenideElement datatable = getContractDatatable();
        getContractRows(datatable).shouldBe(CollectionCondition.size(records.getTableRows().size()));

        SelenideElement tbody = getContractDatatableBody(datatable);
        List<String> expectedIds = records.getTableRows().stream()
                .map(tableRow -> tableRow.getCell("Id"))
                .collect(Collectors.toList());
        tbody.$$("tr th[data-label=Id]")
                .shouldBe(CollectionCondition.exactTexts(expectedIds));

        List<String> expectedNames = records.getTableRows().stream()
                .map(tableRow -> tableRow.getCell("Name"))
                .collect(Collectors.toList());
        tbody.$$("tr td[data-label=Name]")
                .shouldBe(CollectionCondition.exactTexts(expectedNames));
    }

    private SelenideElement getTitle() {
        return $(ByShadow.cssSelector("article h2.slds-card__header-title",
                "demo_aho-contract_page",
                "lightning-card"));
    }

    private SelenideElement getContractDatatable() {
        return $(ByShadow.cssSelector("lightning-card lightning-datatable",
                "demo_aho-Contract_page"));
    }

    private SelenideElement getContractDatatableBody(SelenideElement datatable) {
        return $(datatable.getShadowRoot()
                .findElement(By.cssSelector("table tbody[data-rowgroup-body]")));
    }

    private ElementsCollection getContractRows(SelenideElement datatable) {
        return getContractDatatableBody(datatable).$$("tr");
    }

}
