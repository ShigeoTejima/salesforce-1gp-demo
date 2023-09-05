package org.example.step;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.selector.ByShadow;
import com.thoughtworks.gauge.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class SettingPageStep {

    private static final String PATH_PAGE = "/lightning/n/demo_aho__setting_page";

    @Step("setting page - open page.")
    public void openPage() {
        open(PATH_PAGE);

        getTitle().shouldBe(Condition.exactText("Setting"));
    }

    @Step("setting page - cannot open page.")
    public void cannotOpenPage() {
        open(PATH_PAGE);

        getTitle().shouldBe(Condition.not(Condition.visible));
    }

    @Step("setting page - field 'api-key' is empty.")
    public void verifyApiKeyIsEmpty() {
        verifyApiKeyIs("");
    }

    @Step("setting page - field 'api-key' is <value>.")
    public void verifyApiKeyIs(String value) {
        getApiKey().shouldBe(Condition.exactValue(value));
    }

    @Step("setting page - field 'api-key' is enable.")
    public void verifyApiKeyIsEnable() {
        getApiKey().shouldBe(Condition.enabled);
    }

    @Step("setting page - field 'api-key' is disable.")
    public void verifyApiKeyIsDisable() {
        getApiKey().shouldBe(Condition.disabled);
    }

    @Step("setting page - field 'api-key' set <value>.")
    public void setValueApiKey(String value) {
        getApiKey().val(value);
    }

    @Step("setting page - button 'Test connect' is disable.")
    public void verifyTestConnectIsDisable() {
        getTestConnect().shouldBe(Condition.disabled);
    }

    @Step("setting page - button 'Test connect' is enable.")
    public void verifyTestConnectIsEnable() {
        getTestConnect().shouldBe(Condition.enabled);
    }

    @Step("setting page - button 'Test connect' click.")
    public void clickTestConnect() {
        getTestConnect().click();
    }

    @Step("setting page - button 'Save' is disable.")
    public void verifySaveIsDisable() {
        getSave().shouldBe(Condition.disabled);
    }

    @Step("setting page - button 'Save' is enable.")
    public void verifySaveIsEnable() {
        getSave().shouldBe(Condition.enabled);
    }

    @Step("setting page - button 'Save' is not visible.")
    public void verifySaveIsNotVisible() {
        getSave().shouldNotBe(Condition.visible);
    }

    @Step("setting page - button 'Save' click.")
    public void clickSave() {
        getSave().click();
    }

    @Step("setting page - button 'Edit' is enable.")
    public void verifyEditIsEnable() {
        getEdit().shouldBe(Condition.enabled);
    }

    @Step("setting page - button 'Edit' is not visible.")
    public void verifyEditIsNotVisible() {
        getEdit().shouldNotBe(Condition.visible);
    }

    @Step("setting page - button 'Edit' click.")
    public void clickEdit() {
        getEdit().click();
    }

    @Step("setting page - button 'Cancel' is enable.")
    public void verifyCancelIsEnable() {
        getCancel().shouldBe(Condition.enabled);
    }

    @Step("setting page - button 'Cancel' is not visible.")
    public void verifyCancelIsNotVisible() {
        getCancel().shouldNotBe(Condition.visible);
    }

    @Step("setting page - button 'Cancel' click.")
    public void clickCancel() {
        getCancel().click();
    }

    @Step("setting page - toast displayed. variant: <variant>, title: <title>, message: <message>")
    public void verifyToastDisplayed(String variant, String title, String message) {
        SelenideElement toast = $(".forceToastMessage");
        toast.shouldBe(Condition.visible)
            .shouldBe(Condition.attribute("data-key", variant));

        toast.$(".toastContent .toastTitle").shouldBe(Condition.exactText(title));

        toast.$(".toastContent .toastMessage").shouldBe(Condition.exactText(message));
    }

    private SelenideElement getTitle() {
        return $(ByShadow.cssSelector("article h2.slds-card__header-title",
                "demo_aho-setting_page",
                "lightning-card"));
    }

    private SelenideElement getApiKey() {
        return $(ByShadow.cssSelector("input",
                "demo_aho-setting_page",
                ".api-key"));
    }

    private SelenideElement getTestConnect() {
        return $(ByShadow.cssSelector("button",
                "demo_aho-setting_page",
                ".test-connect"));
    }

    private SelenideElement getEdit() {
        return $(ByShadow.cssSelector("button",
                "demo_aho-setting_page",
                ".edit"));
    }

    private SelenideElement getSave() {
        return $(ByShadow.cssSelector("button",
                "demo_aho-setting_page",
                ".save"));
    }

    private SelenideElement getCancel() {
        return $(ByShadow.cssSelector("button",
                "demo_aho-setting_page",
                ".edit-cancel"));
    }
}
