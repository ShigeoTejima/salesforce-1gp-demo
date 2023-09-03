@IsTest
private class SettingControllerTest {
  @IsTest
  static void testConnect_when_success() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'setting');

    System.runAs(u) {
      // given:
      Mocks.MockDemoApi mockDemoApi = new Mocks.MockDemoApi();
      mockDemoApi.getContract_with_apiKey_params_apiKey = 'api-key-for-test';
      mockDemoApi.getContract_with_apiKey_return = new DemoApi.Contract(
        '123',
        'Demo Contract'
      );

      // check before set mock:
      System.assert(SettingController.demoApi != null);

      SettingController.demoApi = mockDemoApi;

      // when:
      SettingController.TestConnectResponse actual = SettingController.testConnect(
        'api-key-for-test'
      );

      // then:
      System.assertEquals('SUCCESS', actual.result);
    }

  }

  @IsTest
  static void testConnect_when_failure() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'setting');

    System.runAs(u) {
      // given:
      Mocks.MockDemoApi mockDemoApi = new Mocks.MockDemoApi();
      mockDemoApi.getContract_with_apiKey_params_apiKey = 'api-key-for-test';
      mockDemoApi.getContract_with_apiKey_throws = new UnauthorizedException(
        'error for test'
      );

      // check before set mock:
      System.assert(SettingController.demoApi != null);

      SettingController.demoApi = mockDemoApi;

      // when:
      SettingController.TestConnectResponse actual = SettingController.testConnect(
        'api-key-for-test'
      );

      // then:
      System.assertEquals('FAILURE', actual.result);
    }

  }

  @IsTest
  static void saveApiKey_when_success() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'setting');

    System.runAs(u) {
      // given:
      Mocks.MockDemoApiSettingRepository mockDemoApiSettingRepository = new Mocks.MockDemoApiSettingRepository();
      mockDemoApiSettingRepository.register_params_entity = new DemoApiSettingRepository.DemoApiSetting(
        'api-key-for-test'
      );

      // check before set mock:
      System.assert(SettingController.demoApiSettingRepository != null);

      SettingController.demoApiSettingRepository = mockDemoApiSettingRepository;

      // when:
      SettingController.SaveApiKeyResponse actual = SettingController.saveApiKey(
        'api-key-for-test'
      );

      System.assertEquals('SUCCESS', actual.result);
      System.assertEquals(1, mockDemoApiSettingRepository.register_times);
    }

  }

  @IsTest
  static void saveApiKey_when_failure() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'setting');

    System.runAs(u) {
      // given:
      Mocks.MockDemoApiSettingRepository mockDemoApiSettingRepository = new Mocks.MockDemoApiSettingRepository();
      mockDemoApiSettingRepository.register_params_entity = new DemoApiSettingRepository.DemoApiSetting(
        'api-key-for-test'
      );
      mockDemoApiSettingRepository.register_throws = new IllegalArgumentException(
        'error for test'
      );

      // check before set mock:
      System.assert(SettingController.demoApiSettingRepository != null);

      SettingController.demoApiSettingRepository = mockDemoApiSettingRepository;

      // when:
      SettingController.SaveApiKeyResponse actual = SettingController.saveApiKey(
        'api-key-for-test'
      );

      System.assertEquals('FAILURE', actual.result);
      System.assertEquals(1, mockDemoApiSettingRepository.register_times);
    }

  }

  @IsTest
  static void getApiKey() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'setting');

    System.runAs(u) {
      // given:
      Mocks.MockDemoApiSettingRepository mockDemoApiSettingRepository = new Mocks.MockDemoApiSettingRepository();
      mockDemoApiSettingRepository.find_return = new DemoApiSettingRepository.DemoApiSetting(
        'api-key-for-test'
      );

      // check before set mock:
      System.assert(SettingController.demoApiSettingRepository != null);

      SettingController.demoApiSettingRepository = mockDemoApiSettingRepository;

      // when:
      SettingController.GetApiKeyResponse actual = SettingController.getApiKey();

      // then:
      System.assertEquals('api-key-for-test', actual.value);
    }

  }

  @IsTest
  static void getApiKey_when_notfound() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'setting');

    System.runAs(u) {
      // given:
      Mocks.MockDemoApiSettingRepository mockDemoApiSettingRepository = new Mocks.MockDemoApiSettingRepository();
      mockDemoApiSettingRepository.find_return = new DemoApiSettingRepository.DemoApiSetting(
        null
      );

      // check before set mock:
      System.assert(SettingController.demoApiSettingRepository != null);

      SettingController.demoApiSettingRepository = mockDemoApiSettingRepository;

      // when:
      SettingController.GetApiKeyResponse actual = SettingController.getApiKey();

      // then:
      System.assertEquals(null, actual.value);
    }

  }
}
