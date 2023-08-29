@IsTest
global class MockHttpResponseGenerator implements HttpCalloutMock {
  private String endpoint;
  private String requestMethod;
  private Integer statusCode;
  private String responseContentType;
  private String responseBody;

  global MockHttpResponseGenerator(
    String endpoint,
    String requestMethod,
    Integer statusCode,
    String responseContentType,
    String responseBody
  ) {
    this.endpoint = endpoint;
    this.requestMethod = requestMethod;
    this.statusCode = statusCode;
    this.responseContentType = responseContentType;
    this.responseBody = responseBody;
  }

  global HTTPResponse respond(HTTPRequest request) {
    System.assertEquals(this.endpoint, request.getEndpoint());
    System.assertEquals(this.requestMethod, request.getMethod());

    HttpResponse response = new HttpResponse();
    response.setStatusCode(this.statusCode);
    if (this.responseContentType != null) {
      response.setHeader('Content-Type', this.responseContentType);
    }
    if (this.responseBody != null) {
      response.setBody(this.responseBody);
    }
    return response;
  }
}
