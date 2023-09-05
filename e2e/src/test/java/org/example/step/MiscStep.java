package org.example.step;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.thoughtworks.gauge.Step;

import static com.codeborne.selenide.Selenide.$;

public class MiscStep {

    @Step("salesforce ui - toast displayed. variant: <variant>, title: <title>, message: <message>")
    public void verifyToastDisplayed(String variant, String title, String message) {
        SelenideElement toast = $(".forceToastMessage");
        toast.shouldBe(Condition.visible)
                .shouldBe(Condition.attribute("data-key", variant));

        toast.$(".toastContent .toastTitle").shouldBe(Condition.exactText(title));

        toast.$(".toastContent .toastMessage").shouldBe(Condition.exactText(message));
    }

}
