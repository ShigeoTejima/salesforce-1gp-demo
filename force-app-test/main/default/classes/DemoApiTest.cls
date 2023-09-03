@IsTest
private class DemoApiTest {
  @IsTest
  static void getContract() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      demo_api_setting__c demoApiSetting = demo_api_setting__c.getOrgDefaults();
      demoApiSetting.api_key__c = 'test-api-key';
      upsert demoApiSetting;

      Test.startTest();

      String expectedEndpoint = 'https://demo-api.example.com/contract';
      Map<String, String> expectedRequestHeaders = new Map<String, String>{
        'Accept' => 'application/json',
        'api-key' => 'test-api-key',
        'organization-id' => UserInfo.getOrganizationId()
      };

      Test.setMock(
        HttpCalloutMock.class,
        new MockHttpResponseGenerator(
          expectedEndpoint,
          'GET',
          expectedRequestHeaders,
          200,
          'application/json',
          '{"id": "123", "name": "Demo contract"}'
        )
      );

      DemoApi sut = new DemoApi();
      DemoApi.Contract actual = sut.getContract();

      System.assertEquals('123', actual.id);
      System.assertEquals('Demo contract', actual.name);

      Test.stopTest();
    }

  }

  @IsTest
  static void getContractWhenUnauthorizedError() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      demo_api_setting__c demoApiSetting = demo_api_setting__c.getOrgDefaults();
      demoApiSetting.api_key__c = 'test-api-key';
      upsert demoApiSetting;

      Test.startTest();

      String expectedEndpoint = 'https://demo-api.example.com/contract';
      Map<String, String> expectedRequestHeaders = new Map<String, String>{
        'Accept' => 'application/json',
        'api-key' => 'test-api-key',
        'organization-id' => UserInfo.getOrganizationId()
      };

      Test.setMock(
        HttpCalloutMock.class,
        new MockHttpResponseGenerator(
          expectedEndpoint,
          'GET',
          expectedRequestHeaders,
          401,
          null,
          null
        )
      );

      try {
        DemoApi sut = new DemoApi();
        sut.getContract();

        System.Assert.fail('Failure due to lack of success');
      } catch (UnauthorizedException e) {
        System.Assert.isTrue(true);
        System.assertEquals('', e.getMessage());
      }

      Test.stopTest();
    }

  }

  @IsTest
  static void getContractWhenUnexpectedError() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      demo_api_setting__c demoApiSetting = demo_api_setting__c.getOrgDefaults();
      demoApiSetting.api_key__c = 'test-api-key';
      upsert demoApiSetting;

      Test.startTest();

      String expectedEndpoint = 'https://demo-api.example.com/contract';
      Map<String, String> expectedRequestHeaders = new Map<String, String>{
        'Accept' => 'application/json',
        'api-key' => 'test-api-key',
        'organization-id' => UserInfo.getOrganizationId()
      };

      Test.setMock(
        HttpCalloutMock.class,
        new MockHttpResponseGenerator(
          expectedEndpoint,
          'GET',
          expectedRequestHeaders,
          500,
          null,
          null
        )
      );

      try {
        DemoApi sut = new DemoApi();
        sut.getContract();

        System.Assert.fail('Failure due to lack of success');
      } catch (ApiException e) {
        System.Assert.isTrue(true);
        System.assertEquals('unexpected server error', e.getMessage());
        System.assertEquals(500, e.statusCode);
        System.assertEquals('', e.responseBody);
      }

      Test.stopTest();
    }

  }

  @IsTest
  static void getContractWhenApiKeyNotRegistered() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      Test.startTest();

      String expectedEndpoint = 'https://demo-api.example.com/contract';
      Map<String, String> expectedRequestHeaders = new Map<String, String>{
        'Accept' => 'application/json',
        'api-key' => 'test-api-key',
        'organization-id' => UserInfo.getOrganizationId()
      };

      Test.setMock(
        HttpCalloutMock.class,
        new MockHttpResponseGenerator(
          expectedEndpoint,
          'GET',
          expectedRequestHeaders,
          500,
          null,
          null
        )
      );

      try {
        DemoApi sut = new DemoApi();
        sut.getContract();

        System.Assert.fail('Failure due to lack of success');
      } catch (UnauthorizedException.NotSetApiKeyException e) {
        System.debug(e);
        System.Assert.isTrue(true);
      }

      Test.stopTest();
    }
  }

  @IsTest
  static void getContract_with_apiKey() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      Test.startTest();

      String expectedEndpoint = 'https://demo-api.example.com/contract';
      Map<String, String> expectedRequestHeaders = new Map<String, String>{
        'Accept' => 'application/json',
        'api-key' => 'test-api-key',
        'organization-id' => UserInfo.getOrganizationId()
      };

      Test.setMock(
        HttpCalloutMock.class,
        new MockHttpResponseGenerator(
          expectedEndpoint,
          'GET',
          expectedRequestHeaders,
          200,
          'application/json',
          '{"id": "123", "name": "Demo contract"}'
        )
      );

      DemoApi sut = new DemoApi();
      DemoApi.Contract actual = sut.getContract('test-api-key');

      System.assertEquals('123', actual.id);
      System.assertEquals('Demo contract', actual.name);

      Test.stopTest();
    }

  }

  @IsTest
  static void getContract_with_apiKey_WhenUnauthorizedError() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      Test.startTest();

      String expectedEndpoint = 'https://demo-api.example.com/contract';
      Map<String, String> expectedRequestHeaders = new Map<String, String>{
        'Accept' => 'application/json',
        'api-key' => 'test-api-key',
        'organization-id' => UserInfo.getOrganizationId()
      };

      Test.setMock(
        HttpCalloutMock.class,
        new MockHttpResponseGenerator(
          expectedEndpoint,
          'GET',
          expectedRequestHeaders,
          401,
          null,
          null
        )
      );

      try {
        DemoApi sut = new DemoApi();
        sut.getContract('test-api-key');

        System.Assert.fail('Failure due to lack of success');
      } catch (UnauthorizedException e) {
        System.Assert.isTrue(true);
        System.assertEquals('', e.getMessage());
      }

      Test.stopTest();
    }

  }

  @IsTest
  static void getContract_with_apiKey_WhenUnexpectedError() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      Test.startTest();

      String expectedEndpoint = 'https://demo-api.example.com/contract';
      Map<String, String> expectedRequestHeaders = new Map<String, String>{
        'Accept' => 'application/json',
        'api-key' => 'test-api-key',
        'organization-id' => UserInfo.getOrganizationId()
      };

      Test.setMock(
        HttpCalloutMock.class,
        new MockHttpResponseGenerator(
          expectedEndpoint,
          'GET',
          expectedRequestHeaders,
          500,
          null,
          null
        )
      );

      try {
        DemoApi sut = new DemoApi();
        sut.getContract('test-api-key');

        System.Assert.fail('Failure due to lack of success');
      } catch (ApiException e) {
        System.Assert.isTrue(true);
        System.assertEquals('unexpected server error', e.getMessage());
        System.assertEquals(500, e.statusCode);
        System.assertEquals('', e.responseBody);
      }

      Test.stopTest();
    }

  }
}
