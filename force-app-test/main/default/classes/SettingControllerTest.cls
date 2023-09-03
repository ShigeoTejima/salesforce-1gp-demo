@IsTest
private class SettingControllerTest {
  @IsTest
  static void testConnect_when_success() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'setting');

    System.runAs(u) {
      // given:
      MockDemoApi mockDemoApi = new MockDemoApi();
      mockDemoApi.getContract_apiKey_params_apiKey = 'api-key-for-test';
      mockDemoApi.getContract_apiKey_return = new DemoApi.Contract(
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
      MockDemoApi mockDemoApi = new MockDemoApi();
      mockDemoApi.getContract_apiKey_params_apiKey = 'api-key-for-test';
      mockDemoApi.getContract_apiKey_throws = new UnauthorizedException(
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
      MockDemoApiSettingRepository mockDemoApiSettingRepository = new MockDemoApiSettingRepository();
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
      MockDemoApiSettingRepository mockDemoApiSettingRepository = new MockDemoApiSettingRepository();
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

  class MockDemoApi extends DemoApi {
    Integer getContract_apiKey_times = 0;
    String getContract_apiKey_params_apiKey;
    Contract getContract_apiKey_return;
    Exception getContract_apiKey_throws;

    public override Contract getContract(String apiKey) {
      getContract_apiKey_times++;

      System.assertEquals(getContract_apiKey_params_apiKey, apiKey);

      if (getContract_apiKey_throws != null) {
        throw getContract_apiKey_throws;
      }

      return getContract_apiKey_return;
    }
  }

  class MockDemoApiSettingRepository extends DemoApiSettingRepository {
    Integer register_times = 0;
    DemoApiSetting register_params_entity;
    Exception register_throws;

    public override void register(DemoApiSetting entity) {
      register_times++;

      if (register_params_entity == null) {
        System.assertEquals(null, entity);
      } else {
        System.assertEquals(register_params_entity.apiKey, entity.apiKey);
      }

      if (register_throws != null) {
        throw register_throws;
      }
    }
  }
}
