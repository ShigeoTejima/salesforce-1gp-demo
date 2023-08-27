@IsTest
private class UninstallScriptTest {

    @isTest
    static void testUninstallScript() {
        Test.setMock(HttpCalloutMock.class, new MockHttpResponseGenerator('callout:demo_aho__demo_api/package/uninstalled'));

        UninstallScript sut = new UninstallScript();
        Test.testUninstall(sut);

        System.assert(true, 'verify to only run this code.');
        System.assertEquals(200, sut.responseStatusCode);
        System.assertEquals('accepted', sut.responseBody);
    }
    
}