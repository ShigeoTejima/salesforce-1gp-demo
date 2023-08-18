package org.example;

import com.codeborne.selenide.Selenide;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeSpec;
import com.thoughtworks.gauge.BeforeSuite;
import org.example.gateway.DemoGateway;
import org.example.gateway.PermissionSetGateway;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public class BeforeAndAfter {

    @BeforeSuite
    public void beforeSuite() {
        /*
         * NOTE: 以下の順番にロードして、システムプロパティに設定する.
         *       後続に同じプロパティ名の設定があれば上書きする.
         */
        List.of(
            ".env-default",
            ".env-local-default",
            ".env-local"
        ).stream().forEach(env -> loadSystemPropertiesFromDotenv(env));

    }

    @AfterScenario
    public void afterScenario() {
        Selenide.clearBrowserLocalStorage();
        Selenide.clearBrowserCookies();
    }

    @BeforeSpec(tags = { "cleanDemo" })
    public void cleanDemo() {
        new DemoGateway().truncate();
    }

    @BeforeSpec(tags = { "assignPermissionSetOfDemo" })
    public void assignPermissionSetOfDemo() {
        new PermissionSetGateway().assignToDemo();
    }

    @BeforeSpec(tags = { "unAssignPermissionSetOfDemo" })
    public void unAssignPermissionSetOfDemo() {
        new PermissionSetGateway().unAssignFromDemo();
    }

    private void loadSystemPropertiesFromDotenv(String filename) {
        Dotenv.configure()
                .filename(filename)
                .ignoreIfMissing()
                .systemProperties()
                .load();
    }

}
