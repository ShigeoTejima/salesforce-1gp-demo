@IsTest
global class MockHttpResponseGenerator implements HttpCalloutMock {

    private String endpoint;

    global MockHttpResponseGenerator(String endpoint) {
        this.endpoint = endpoint;
    }

    global HTTPResponse respond(HTTPRequest request) {
        System.debug('MockHttpResponseGenerator');
        System.debug(request.getEndpoint());
        System.debug(request.getMethod());

        System.assertEquals(this.endpoint, request.getEndpoint());
        System.assertEquals('POST', request.getMethod());

        HttpResponse response = new HttpResponse();
        response.setStatusCode(200);
        response.setHeader('Content-Type', 'text/plain');
        response.setBody('accepted');
        return response;
    }

}