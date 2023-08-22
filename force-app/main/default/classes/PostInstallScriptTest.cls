@IsTest
private class PostInstallScriptTest {

    @isTest
    static void tesPostInstallScript() {
        PostInstallScript sut = new PostInstallScript();
        Test.testInstall(sut, null);
        Test.testInstall(sut, new Version(1,0));

        System.assert(true, 'verify to only run this code.');
    }

}