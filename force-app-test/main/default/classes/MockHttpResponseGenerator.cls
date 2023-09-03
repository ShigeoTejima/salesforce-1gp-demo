@IsTest
global class MockHttpResponseGenerator implements HttpCalloutMock {
  private String endpoint;
  private String requestMethod;
  private Map<String, String> requestHeaders;
  private Integer statusCode;
  private String responseContentType;
  private String responseBody;

  global MockHttpResponseGenerator(
    String endpoint,
    String requestMethod,
    Map<String, String> requestHeaders,
    Integer statusCode,
    String responseContentType,
    String responseBody
  ) {
    this.endpoint = endpoint;
    this.requestMethod = requestMethod;
    this.requestHeaders = requestHeaders;
    this.statusCode = statusCode;
    this.responseContentType = responseContentType;
    this.responseBody = responseBody;
  }

  global HTTPResponse respond(HTTPRequest request) {
    System.assertEquals(this.endpoint, request.getEndpoint());
    System.assertEquals(this.requestMethod, request.getMethod());

    if (this.requestHeaders != null) {
      for (String key : this.requestHeaders.keySet()) {
        System.assertEquals(
          this.requestHeaders.get(key),
          request.getHeader(key)
        );
      }
    }

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
