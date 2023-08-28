@IsTest
private class ContractControllerTest {
  @IsTest
  static void getContract() {
    Profile p = [
      SELECT Id, Name
      FROM Profile
      WHERE Name IN ('Standard User', '標準ユーザ')
    ];

    User u = demoUser(p);
    insert u;

    PermissionSet ps = [SELECT Id FROM PermissionSet WHERE Name = 'demo'];
    insert new PermissionSetAssignment(
      AssigneeId = u.Id,
      PermissionSetId = ps.Id
    );

    System.runAs(u) {
      Test.setMock(
        HttpCalloutMock.class,
        new MockHttpResponseGenerator(
          'callout:demo_aho__demo_api/contract',
          'GET',
          200,
          'application/json',
          '{"id": "123", "name": "Demo contract"}'
        )
      );

      ContractController.Contract actual = ContractController.getContract();

      System.assertEquals('123', actual.id);
      System.assertEquals('Demo contract', actual.name);
    }

  }

  private static User demoUser(Profile p) {
    String baseUsername = UserInfo.getUserName();
    List<String> partsOfUsername = baseUsername.split('@');
    String demoUsername = String.format('{0}-demo@{1}', partsOfUsername);

    return new User(
      UserName = demoUsername,
      Email = demoUsername,
      LastName = 'demo',
      Alias = 'demo',
      TimeZoneSidKey = 'asia/tokyo',
      LocaleSidKey = 'ja_JP',
      EmailEncodingKey = 'UTF-8',
      LanguageLocaleKey = 'ja',
      ProfileId = p.Id
    );
  }
}
