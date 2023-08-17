import com.codeborne.selenide.Configuration;
import com.thoughtworks.gauge.BeforeSpec;
import com.thoughtworks.gauge.BeforeSuite;
import gateway.SalesforceGateway;
import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.Optional;

public class BeforeAndAfter {

    @BeforeSuite
    public void beforeSuite() {
        // NOTE: デフォルトの.env-defaultをロードして、システムプロパティに設定する
        loadSystemPropertiesFromDotenv(".env-default");

        // NOTE: ローカルでの実行用の.env-local-defaultをロードして、システムプロパティに設定する. 設定があればデフォルトの設定値を上書きする
        loadSystemPropertiesFromDotenv(".env-local-default");

        // NOTE: ローカルでの実行用の.env-localをロードして、システムプロパティに設定する. 設定があればデフォルトの設定値を上書きする
        loadSystemPropertiesFromDotenv(".env-local");

        // NOTE: Chromeのみで検証しているため、Chromeに対する設定があれば設定している
        Optional.ofNullable(System.getProperty("chromeOptions.addArguments")).ifPresent(arguments -> {
            ChromeOptions chromeOptions = new ChromeOptions();
            List.of(arguments.split(" ")).stream().forEach(argument -> chromeOptions.addArguments(argument));
            Configuration.browserCapabilities = chromeOptions;
        });

    }

    private void loadSystemPropertiesFromDotenv(String filename) {
        Dotenv.configure()
                .filename(filename)
                .ignoreIfMissing()
                .systemProperties()
                .load();
    }

    @BeforeSpec(tags = { "cleanDemo" })
    public void cleanDemo() {
        new SalesforceGateway().truncateDemo();
    }

    @BeforeSpec(tags = { "assignPermissionSetOfDemo" })
    public void assignPermissionSetOfDemo() {
        new SalesforceGateway().assignPermissionSetOfDemo();
    }

    @BeforeSpec(tags = { "unAssignPermissionSetOfDemo" })
    public void unAssignPermissionSetOfDemo() {
        new SalesforceGateway().unAssignPermissionSetOfDemo();
    }
}
