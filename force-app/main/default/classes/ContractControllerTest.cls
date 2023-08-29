@IsTest
private class ContractControllerTest {
  @IsTest
  static void getContract() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

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

  @IsTest
  static void getContractWhenUnauthorizedError() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      Test.setMock(
        HttpCalloutMock.class,
        new MockHttpResponseGenerator(
          'callout:demo_aho__demo_api/contract',
          'GET',
          401,
          null,
          null
        )
      );

      try {
        ContractController.getContract();

        System.Assert.fail('Failure due to lack of success');
      } catch (UnauthorizedException e) {
        System.Assert.isTrue(true);
      }
    }

  }

  @IsTest
  static void getContractWhenUnexpectedError() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      Test.setMock(
        HttpCalloutMock.class,
        new MockHttpResponseGenerator(
          'callout:demo_aho__demo_api/contract',
          'GET',
          500,
          null,
          null
        )
      );

      try {
        ContractController.getContract();

        System.Assert.fail('Failure due to lack of success');
      } catch (ExternalService.demo_aho.DemoApi.getContractByAuth_ResponseException e) {
        System.Assert.isTrue(true);
      }
    }

  }
}
