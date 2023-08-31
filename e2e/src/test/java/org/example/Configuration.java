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
    default String getOrganizationId() {
        return System.getProperty("test.organizationId");
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

    default String getNamespacePrefix() {
        return "demo_aho";
    }

    default String getMockDemoApiEndpoint() {
        return System.getProperty("test.mock.demo-api.host");
    }

}
