import com.codeborne.selenide.*;
import com.codeborne.selenide.selector.ByShadow;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

// TODO: 特定のクラスに分割する
public class ExampleStep {

    @Step("open login page.")
    public void openLoginPageStep() {
        String username = System.getProperty("test.username");
        String password = System.getProperty("test.password");
        String lastname = System.getProperty("test.lastname");
        String firstname = System.getProperty("test.firstname");

        open("https://test.salesforce.com");

        $("#username").val(username);
        $("#password").val(password);

        $("#Login").click();

        // NOTE: 初回ログイン時に`携帯電話を登録`を確認されるので、`電話を登録しません`を選択する
        //       2回目ログイン以降はホーム画面に遷移する
        // NOTE: ここの判定が不安定. => セットアップ用のstepに切り出した方がよいかもしれない、と思ったが今のところ大丈夫なので様子見
        SelenideElement title = $("title");
        if (title.innerText().contains("携帯電話を登録")) {
            SelenideElement linkUnregisterPhone = $(new By.ByLinkText("電話を登録しません"));
            if (linkUnregisterPhone.exists()) {
                linkUnregisterPhone.click();
            }
        }

        // NOTE: ユーザープロファイルに氏名が表示されたことで、ログインできたことを検証するとした
        verifyUserProfile(lastname, firstname);
    }

    @Step("open demo page.")
    public void openDemoPageStep() {
        open("/lightning/n/demo_ahd__demo_page");

        SelenideElement header = $(ByShadow.cssSelector("article h2.slds-card__header-title",
                "demo_ahd-demo_page",
                "lightning-card"));
        assertEquals("Demo", header.innerText());
    }

    @Step("Demo records is empty.")
    public void verifyDemoRecordsEmptyStep() {
        SelenideElement datatable = getDemoDatatable();
        getDemoRows(datatable).shouldBe(CollectionCondition.empty);
    }

    @Step("Demo records has records for <records> .")
    public void verifyDemoRecordsIsExistsStep(Table records) {
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

    public void verifyUserProfile(String lastname, String firstname) {
        $("button.branding-userProfile-button").click();

        // NOTE: Salesforce Platformのパフォーマンスによりデフォルトタイムアウトを超える場合あり、待機するタイムアウトを調整しても良さそう
        SelenideElement userProfileCard = $("div.oneUserProfileCard");
        userProfileCard.shouldBe(Condition.visible);

        // NOTE: locale=jaの場合、姓名の並び
        final String expectedProfileName = lastname + " " + firstname;
        userProfileCard.$(By.cssSelector("h1.profile-card-name"))
                .shouldHave(Condition.exactText(expectedProfileName));
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
