@IsTest
private class PostInstallScriptTest {

    @isTest
    static void installed() {
        Test.setMock(HttpCalloutMock.class, new MockHttpResponseGenerator('callout:demo_aho__demo_api/package/installed'));

        PostInstallScript sut = new PostInstallScript();
        Test.testInstall(sut, null);

        System.assert(true, 'verify to only run this code.');
        System.assertEquals(200, sut.responseStatusCode);
        System.assertEquals('accepted', sut.responseBody);
    }

    @isTest
    static void upgrated() {
        Test.setMock(HttpCalloutMock.class, new MockHttpResponseGenerator('callout:demo_aho__demo_api/package/installed'));

        PostInstallScript sut = new PostInstallScript();
        Test.testInstall(sut, new Version(1,0));

        System.assert(true, 'verify to only run this code.');
        System.assertEquals(200, sut.responseStatusCode);
        System.assertEquals('accepted', sut.responseBody);
    }

}