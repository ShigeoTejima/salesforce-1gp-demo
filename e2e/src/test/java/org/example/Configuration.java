package org.example;

public interface Configuration {

    default String getBaseUrl() {
        return System.getProperty("selenide.baseUrl");
    }
    default String getInstanceUrl() {
        return System.getProperty("test.instanceUrl");
    }
    default String getApiVersion() {
        return System.getProperty("test.apiVersion");
    }

    default String getAccessToken() {
        return System.getProperty("test.accessToken");
    }

    default String getUsername() {
        return System.getProperty("test.username");
    }

    default String getPassword() {
        return System.getProperty("test.password");
    }

    default String getFullname() {
        return System.getProperty("test.fullname");
    }

    default String getUserId() {
        return System.getProperty("test.userId");
    }

    default String getPermissionSetDemoId() {
        return System.getProperty("test.permissionSet.demo_ahd_demo.id");
    }

}
