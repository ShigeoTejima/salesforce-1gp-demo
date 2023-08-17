import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class LoginPageStep {

    @Step("try login.")
    public void tryLogin() {
        String baseUrl = System.getProperty("selenide.baseUrl");
        String username = System.getProperty("test.username");
        String password = System.getProperty("test.password");
        String fullname = System.getProperty("test.fullname");

        open(baseUrl);

        SelenideElement title = $("title");

        System.out.println(String.format("tryLogin - title: %s", title.innerText()));

        // NOTE: ログインページが表示された
        if (title.innerText().startsWith("ログイン ")) {
            login(username, password);
            notRegisterPhoneWhenFirstLogin();

        // NOTE: ホームページが表示された
        } else if (title.innerText().startsWith("ホーム ")) {
            verifyUserProfile(fullname);

        // NOTE: わからないページが表示された
        } else {
            fail("unexpected display page.");

        }

    }

    private void login(String username, String password) {
        $("#username").val(username);
        $("#password").val(password);

        $("#Login").click();
    }

    private void notRegisterPhoneWhenFirstLogin() {
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
    }

    private void verifyUserProfile(String fullname) {
        $("button.branding-userProfile-button").click();

        // NOTE: Salesforce Platformのパフォーマンスによりデフォルトタイムアウトを超える場合あり、待機するタイムアウトを調整しても良さそう
        SelenideElement userProfileCard = $("div.oneUserProfileCard");
        userProfileCard.shouldBe(Condition.visible);

        userProfileCard.$(By.cssSelector("h1.profile-card-name"))
                .shouldHave(Condition.exactText(fullname));
    }

}
