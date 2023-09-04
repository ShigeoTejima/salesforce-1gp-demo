global with sharing virtual class DemoApiSettingRepository {
  global virtual void register(DemoApiSetting entity) {
    demo_api_setting__c target = demo_api_setting__c.getOrgDefaults();
    target.api_key__c = entity.apiKey;
    upsert target;
  }

  global virtual DemoApiSetting find() {
    demo_api_setting__c current = demo_api_setting__c.getOrgDefaults();
    return new DemoApiSetting(current.api_key__c);
  }

  global void remove() {
    demo_api_setting__c current = demo_api_setting__c.getOrgDefaults();
    if (current != null && current.Id != null) {
      delete current;
    }
  }

  global with sharing class DemoApiSetting {
    global String apiKey { get; private set; }

    global DemoApiSetting(String apiKey) {
      this.apiKey = apiKey;
    }
  }
}
