import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginStep {

    @Step("login.")
    public void loginStep() {
        String username = System.getProperty("test.username");
        String password = System.getProperty("test.password");
        String lastname = System.getProperty("test.lastname");
        String firstname = System.getProperty("test.firstname");
        String baseUrl = System.getProperty("selenide.baseUrl");

        // TODO: baseUrlにすると既にログイン済みの場合は、ログインページが表示されないので後続の処理が失敗する. 暫定でtest.salesforce.comでログインし直すようにする
//        open(baseUrl);
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

}
