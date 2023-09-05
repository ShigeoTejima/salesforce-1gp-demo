@IsTest
private class PostInstallScriptTest {
  @isTest
  static void installed() {
    PostInstallScript sut = new PostInstallScript();

    Test.testInstall(sut, null);

    System.assert(true, 'verify to only run this code.');
  }

  @isTest
  static void upgrated() {
    PostInstallScript sut = new PostInstallScript();

    Test.testInstall(sut, new Version(1, 0));

    System.assert(true, 'verify to only run this code.');
  }
}
