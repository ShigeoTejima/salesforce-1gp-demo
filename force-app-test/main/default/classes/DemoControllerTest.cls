@IsTest
private class DemoControllerTest {
  @IsTest
  static void getDemos() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'demo');

    System.runAs(u) {
      List<Demo__c> records = new List<Demo__c>{
        new Demo__c(Name = 'Foo'),
        new Demo__c(Name = 'Bar', description__c = 'Bar Description')
      };
      insert records;

      Test.startTest();

      List<DemoController.Demo> actual = DemoController.getDemos();

      System.assertEquals(2, actual.size());

      DemoController.Demo actualThat = actual.get(0);
      System.assertEquals(true, actualThat.id != null);
      System.assertEquals('Bar', actualThat.name);
      System.assertEquals('Bar Description', actualThat.description);

      actualThat = actual.get(1);
      System.assertEquals(true, actualThat.id != null);
      System.assertEquals('Foo', actualThat.name);
      System.assertEquals(null, actualThat.description);

      Test.stopTest();
    }

  }
}
