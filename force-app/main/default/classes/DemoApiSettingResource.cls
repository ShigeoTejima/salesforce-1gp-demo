@RestResource(urlMapping='/DemoApiSetting/*')
global with sharing class DemoApiSettingResource {
  @TestVisible
  private static DemoApiSettingRepository demoApiSettingRepository;

  static {
    demoApiSettingRepository = new DemoApiSettingRepository();
  }

  @HttpGet
  global static GetCurrentResponse getCurrent() {
    DemoApiSettingRepository.DemoApiSetting current = demoApiSettingRepository.find();
    return new GetCurrentResponse(current.apiKey);
  }

  global with sharing class GetCurrentResponse {
    public String apiKey { get; private set; }

    global GetCurrentResponse(String apiKey) {
      this.apiKey = apiKey;
    }
  }
}
