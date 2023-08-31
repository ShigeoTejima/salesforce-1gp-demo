public with sharing virtual class DemoApi {
  public virtual Contract getContract() {
    api_config__mdt apiConfig = api_config__mdt.getInstance('demo_api');
    String endpoint = apiConfig.host__c + '/contract';

    demo_api_setting__c demoApiSetting = demo_api_setting__c.getOrgDefaults();
    if (demoApiSetting.api_key__c == null) {
      throw new UnauthorizedException.NotSetApiKeyException();
    }

    Http http = new Http();

    HttpRequest request = new HttpRequest();
    request.setEndpoint(endpoint);
    request.setMethod('GET');
    request.setHeader('Accept', 'application/json');
    request.setHeader('api-key', demoApiSetting.api_key__c);
    request.setHeader('organization-id', UserInfo.getOrganizationId());

    HttpResponse response = http.send(request);
    switch on response.getStatusCode() {
      when 200 {
        return (Contract) JSON.deserialize(response.getBody(), Contract.class);
      }
      when 401 {
        throw new UnauthorizedException(response.getBody());
      }
      when else {
        throw new ApiException(
          'unexpected server error',
          response.getStatusCode(),
          response.getBody()
        );
      }
    }
  }

  public class Contract {
    public String id { get; private set; }
    public String name { get; private set; }

    public Contract(String id, String name) {
      this.id = id;
      this.name = name;
    }
  }
}
