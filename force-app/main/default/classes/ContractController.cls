public with sharing class ContractController {
  @AuraEnabled(cacheable=true)
  public static Contract getContract() {
    ExternalService.demo_aho.DemoApi api = new ExternalService.demo_aho.DemoApi();

    ExternalService.demo_aho.DemoApi.getContractByAuth_Response response = api.getContractByAuth();
    ExternalService.demo_aho.DemoApi_Contract demoApiContract = response.Code200;

    return new Contract(demoApiContract.id, demoApiContract.name);
  }

  public class Contract {
    @AuraEnabled
    public String id { get; private set; }

    @AuraEnabled
    public String name { get; private set; }

    public Contract(String id, String name) {
      this.id = id;
      this.name = name;
    }
  }
}
