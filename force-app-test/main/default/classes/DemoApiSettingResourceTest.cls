@IsTest
private class DemoApiSettingResourceTest {
  @IsTest
  static void getCurrent_when_set() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'setting');

    System.runAs(u) {
      // begin:
      Mocks.MockDemoApiSettingRepository mockRepository = new Mocks.MockDemoApiSettingRepository();
      mockRepository.find_return = new DemoApiSettingRepository.DemoApiSetting(
        'api-key-for-test'
      );

      // check before set mock:
      System.assert(DemoApiSettingResource.demoApiSettingRepository != null);

      DemoApiSettingResource.demoApiSettingRepository = mockRepository;

      // when:
      DemoApiSettingResource.GetCurrentResponse actual = DemoApiSettingResource.getCurrent();

      // then:
      System.assertEquals('api-key-for-test', actual.apiKey);
    }

  }

  @IsTest
  static void getCurrent_when_notset() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'setting');

    System.runAs(u) {
      // begin:
      Mocks.MockDemoApiSettingRepository mockRepository = new Mocks.MockDemoApiSettingRepository();
      mockRepository.find_return = new DemoApiSettingRepository.DemoApiSetting(
        null
      );

      // check before set mock:
      System.assert(DemoApiSettingResource.demoApiSettingRepository != null);

      DemoApiSettingResource.demoApiSettingRepository = mockRepository;

      // when:
      DemoApiSettingResource.GetCurrentResponse actual = DemoApiSettingResource.getCurrent();

      // then:
      System.assertEquals(null, actual.apiKey);
    }

  }
}
