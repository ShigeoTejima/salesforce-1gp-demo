public with sharing class ContractController {
  @TestVisible
  private static DemoApi demoApi;

  static {
    demoApi = new DemoApi();
  }

  @AuraEnabled(cacheable=true)
  public static Contract getContract() {
    DemoApi.Contract apiResult = demoApi.getContract();

    return new Contract(apiResult.id, apiResult.name);
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
