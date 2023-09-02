public with sharing class DemoApiSettingRepository {
  public void register(DemoApiSetting entity) {
    demo_api_setting__c target = demo_api_setting__c.getOrgDefaults();
    target.api_key__c = entity.apiKey;
    upsert target;
  }

  public with sharing class DemoApiSetting {
    public String apiKey { get; private set; }

    public DemoApiSetting(String apiKey) {
      this.apiKey = apiKey;
    }
  }
}
