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

    default String getAdminUserAccessToken() {
        return System.getProperty("test.admin-user.accessToken");
    }

    default String getStandardUserUsername() {
        return System.getProperty("test.standard-user.username");
    }

    default String getStandardUserPassword() {
        return System.getProperty("test.standard-user.password");
    }

    default String getStandardUserFullname() {
        return System.getProperty("test.standard-user.fullname");
    }

    default String getStandardUserUserId() {
        return System.getProperty("test.standard-user.userId");
    }

    default String getPermissionSetDemoId() {
        return System.getProperty("test.permissionSet.demo_ahd_demo.id");
    }

}
