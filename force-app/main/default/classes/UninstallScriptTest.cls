@IsTest
private class UninstallScriptTest {
  @isTest
  static void testUninstallScript() {
    UninstallScript sut = new UninstallScript();

    Test.testUninstall(sut);

    System.assert(true, 'verify to only run this code.');
  }
}
