package org.example;

public interface Configuration {

    default String baseUrl() {
        return System.getProperty("selenide.baseUrl");
    }
    default String namespacePrefix() {
        return "demo_aho";
    }

    interface Organization {
        static String instanceUrl() {
            return System.getProperty("test.instanceUrl");
        }
        static String apiVersion() {
            return System.getProperty("test.apiVersion");
        }
        static String id() {
            return System.getProperty("test.organizationId");
        }
    }
    interface AdminUser {
        static String accessToken() {
            return System.getProperty("test.admin-user.accessToken");
        }
    }
    interface StandardUser {
        static String username() {
            return System.getProperty("test.standard-user.username");
        }

        static String password() {
            return System.getProperty("test.standard-user.password");
        }

        static String fullname() {
            return System.getProperty("test.standard-user.fullname");
        }

        static String userId() {
            return System.getProperty("test.standard-user.userId");
        }
    }

    interface MockDemoApi {
        static String host() {
            return System.getProperty("test.mock.demo-api.host");
        }
    }
}
