@IsTest
private class DemoApiSettingRepositoryTest {
  @IsTest
  static void registerWhenAtFirstNotFound() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      // begin:
      demo_api_setting__c current = demo_api_setting__c.getOrgDefaults();
      if (current != null && current.Id != null) {
        delete current;
      }

      Test.startTest();

      DemoApiSettingRepository sut = new DemoApiSettingRepository();

      // when:
      sut.register(
        new DemoApiSettingRepository.DemoApiSetting('api-key-for-test')
      );

      // then:
      demo_api_setting__c actual = demo_api_setting__c.getOrgDefaults();
      System.assertEquals('api-key-for-test', actual.api_key__c);

      Test.stopTest();
    }

  }

  @IsTest
  static void registerWhenApiKeyAlreadySet() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      // begin:
      demo_api_setting__c current = demo_api_setting__c.getOrgDefaults();
      current.api_key__c = 'previous-api-key';
      upsert current;

      Test.startTest();

      DemoApiSettingRepository sut = new DemoApiSettingRepository();

      // when:
      sut.register(
        new DemoApiSettingRepository.DemoApiSetting('api-key-for-test')
      );

      // then:
      demo_api_setting__c actual = demo_api_setting__c.getOrgDefaults();
      System.assertEquals('api-key-for-test', actual.api_key__c);

      Test.stopTest();
    }

  }

  @IsTest
  static void find_when_set() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      // begin:
      demo_api_setting__c current = demo_api_setting__c.getOrgDefaults();
      current.api_key__c = 'api-key-for-test';
      upsert current;

      Test.startTest();

      DemoApiSettingRepository sut = new DemoApiSettingRepository();

      // when:
      DemoApiSettingRepository.DemoApiSetting actual = sut.find();

      // then:
      System.assertEquals('api-key-for-test', actual.apiKey);

      Test.stopTest();
    }

  }

  @IsTest
  static void find_when_notset() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      // begin:
      demo_api_setting__c current = demo_api_setting__c.getOrgDefaults();
      if (current != null && current.Id != null) {
        delete current;
      }

      Test.startTest();

      DemoApiSettingRepository sut = new DemoApiSettingRepository();

      // when:
      DemoApiSettingRepository.DemoApiSetting actual = sut.find();

      // then:
      System.assertEquals(null, actual.apiKey);

      Test.stopTest();
    }

  }
}
