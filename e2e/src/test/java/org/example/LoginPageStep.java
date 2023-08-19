package org.example;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPageStep implements Configuration {

    @Step("try login.")
    public void tryLogin() {
        String baseUrl = getBaseUrl();
        String username = getStandardUserUsername();
        String password = getStandardUserPassword();
        String fullname = getStandardUserFullname();

        open(baseUrl);

        // NOTE: ログインページが表示された
        if (isLoginPage()) {
            login(username, password);
            notRegisterPhoneWhenFirstLogin();
            cancelChangePasswordWhenForcedPasswordChange();
            verifyUserProfile(fullname);

        } else {
            // NOTE: ホームページが表示された場合、ユーザープロファイルを見ることができる
            verifyUserProfile(fullname);

        }

    }

    private SelenideElement getLoginElement() {
        return $("#Login");
    }

    private boolean isLoginPage() {
        // NOTE: ログインページが表示された場合、ログインボタンが表示されている
        return getLoginElement().isDisplayed();
    }

    private void login(String username, String password) {
        $("#username").val(username);
        $("#password").val(password);

        getLoginElement().click();
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

    private void cancelChangePasswordWhenForcedPasswordChange() {
        SelenideElement changePassword = $("div.setup.change-password");
        // NOTE: 強制パスワード変更画面が表示された場合、キャンセルしてホーム画面に遷移させる
        if (changePassword.isDisplayed()) {
            $("#cancel-button").click();
        }
    }

    private void verifyUserProfile(String fullname) {
        $("button.branding-userProfile-button").click();

        /*
         * NOTE: Salesforce Platformのパフォーマンスによりデフォルトタイムアウトを超える場合あり、待機するタイムアウトを調整しても良さそう
         *       => 6 sec に設定
         */
        SelenideElement userProfileCard = $("div.oneUserProfileCard");
        userProfileCard.shouldBe(Condition.visible);

        userProfileCard.$(By.cssSelector("h1.profile-card-name"))
                .shouldHave(Condition.exactText(fullname));
    }

}
