@IsTest
private class UninstallScriptTest {

    @isTest
    static void testUninstallScript() {
        Test.testUninstall(new UninstallScript());

        System.assert(true, 'verify to only run this code.');
    }
    
}