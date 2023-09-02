global with sharing class DemoApiSettingRepository {
  global void register(DemoApiSetting entity) {
    demo_api_setting__c target = demo_api_setting__c.getOrgDefaults();
    target.api_key__c = entity.apiKey;
    upsert target;
  }

  global with sharing class DemoApiSetting {
    global String apiKey { get; private set; }

    global DemoApiSetting(String apiKey) {
      this.apiKey = apiKey;
    }
  }
}
