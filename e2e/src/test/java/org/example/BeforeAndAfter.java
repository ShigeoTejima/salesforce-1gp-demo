package org.example;

import com.codeborne.selenide.Selenide;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.BeforeSpec;
import com.thoughtworks.gauge.BeforeSuite;
import org.example.gateway.DemoApiGateway;
import org.example.gateway.DemoApiSettingGateway;
import org.example.gateway.DemoGateway;
import org.example.gateway.PermissionSetGateway;
import io.github.cdimascio.dotenv.Dotenv;
import org.testcontainers.containers.FixedHostPortGenericContainer;

import java.util.List;

public class BeforeAndAfter implements Configuration {

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

        // NOTE: start wiremock for mock of DemoApi
        new FixedHostPortGenericContainer(Depends.WireMock.dockerImageName())
                .withFixedExposedPort(MockDemoApi.port(), MockDemoApi.port())
                .withExposedPorts(MockDemoApi.port())
                .start();
    }

    @BeforeScenario
    public void beforeScenario() {
        new DemoApiGateway().resetAll();
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

    @BeforeSpec(tags = { "assignPermissionSetOfContract" })
    public void assignPermissionSetOfContract() {
        new PermissionSetGateway().assignToContract();
    }

    @BeforeSpec(tags = { "unAssignPermissionSetOfContract" })
    public void unAssignPermissionSetOfContract() {
        new PermissionSetGateway().unAssignFromContract();
    }

    @BeforeSpec(tags = { "assignPermissionSetOfSetting" })
    public void assignPermissionSetOfSetting() {
        new PermissionSetGateway().assignToSetting();
    }

    @BeforeSpec(tags = { "unAssignPermissionSetOfSetting" })
    public void unAssignPermissionSetOfSetting() {
        new PermissionSetGateway().unAssignFromSetting();
    }

    @BeforeScenario(tags = { "correctApiKey" })
    public void setCorrectApiKy() {
        new DemoApiSettingGateway().setApiKeyByAnonymousApex("correct-api-key");
    }

    @BeforeScenario(tags = { "wrongApiKey" })
    public void setWrongApiKy() {
        new DemoApiSettingGateway().setApiKeyByAnonymousApex("wrong-api-key");
    }

    @BeforeScenario(tags = { "unexpectedApiKey" })
    public void setUnexpectedApiKy() {
        new DemoApiSettingGateway().setApiKeyByAnonymousApex("unexpected-api-key");
    }

    @BeforeScenario(tags = { "removeApiKey" })
    public void removeApiKy() {
        new DemoApiSettingGateway().removeApiKeyByAnonymousApex();
    }

    private void loadSystemPropertiesFromDotenv(String filename) {
        Dotenv.configure()
                .filename(filename)
                .ignoreIfMissing()
                .systemProperties()
                .load();
    }

}
