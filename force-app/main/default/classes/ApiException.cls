public class ApiException extends Exception {
  public Integer statusCode { get; private set; }
  public String responseBody { get; private set; }

  public ApiException(String message, Integer statusCode, String responseBody) {
    this(message);
    this.statusCode = statusCode;
    this.responseBody = responseBody;
  }
}
