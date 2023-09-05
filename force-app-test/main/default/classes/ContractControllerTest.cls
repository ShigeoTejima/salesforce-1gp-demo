@IsTest
private class ContractControllerTest {
  @IsTest
  static void getContract() {
    Profile p = TestUtility.getStandardProfile();
    User u = TestUtility.createDemoUser(p);
    TestUtility.assignPermission(u, 'contract');

    System.runAs(u) {
      // given:
      Mocks.MockDemoApi mockDemoApi = new Mocks.MockDemoApi();
      mockDemoApi.getContract_return = new DemoApi.Contract(
        '123',
        'Demo Contract'
      );

      // check before set mock:
      System.assert(ContractController.demoApi != null);

      ContractController.demoApi = mockDemoApi;

      // when:
      ContractController.Contract actual = ContractController.getContract();

      // then:
      System.assertEquals('123', actual.id);
      System.assertEquals('Demo Contract', actual.name);
    }

  }
}
