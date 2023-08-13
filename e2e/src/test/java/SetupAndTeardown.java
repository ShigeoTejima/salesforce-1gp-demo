import com.codeborne.selenide.Configuration;
import com.thoughtworks.gauge.BeforeSuite;
import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Optional;

public class SetupAndTeardown {

    @BeforeSuite
    public void beforeSuite() {
        // NOTE: デフォルトの.env-defaultをロードして、システムプロパティに設定する
        Dotenv.configure()
                .filename(".env-default")
                .systemProperties()
                .load();

        // NOTE: ローカルでの実行用の.env-localをロードして、システムプロパティに設定する. 設定があればデフォルトの設定値を上書きする
        Dotenv.configure()
                .filename(".env-local")
                .ignoreIfMissing()
                .systemProperties()
                .load();

        // NOTE: Chromeのみで検証しているため、Chromeに対する設定があれば設定している
        Optional.ofNullable(System.getProperty("chromeOptions.addArguments")).ifPresent(arguments -> {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments(arguments);
            Configuration.browserCapabilities = chromeOptions;
        });
    }
}
