public with sharing class SettingController {
  @TestVisible
  private static DemoApi demoApi;

  @TestVisible
  private static DemoApiSettingRepository demoApiSettingRepository;

  static {
    demoApi = new DemoApi();
    demoApiSettingRepository = new DemoApiSettingRepository();
  }

  @AuraEnabled
  public static TestConnectResponse testConnect(String apiKey) {
    try {
      DemoApi.Contract contract = demoApi.getContract(apiKey);
      return new TestConnectResponse(Result.SUCCESS);
    } catch (Exception e) {
      return new TestConnectResponse(Result.FAILURE);
    }
  }

  @AuraEnabled
  public static SaveApiKeyResponse saveApiKey(String apiKey) {
    try {
      demoApiSettingRepository.register(
        new DemoApiSettingRepository.DemoApiSetting(apiKey)
      );
      return new SaveApiKeyResponse(Result.SUCCESS);
    } catch (Exception e) {
      return new SaveApiKeyResponse(Result.FAILURE);
    }
  }

  @AuraEnabled
  public static GetApiKeyResponse getApiKey() {
    DemoApiSettingRepository.DemoApiSetting current = demoApiSettingRepository.find();
    return new GetApiKeyResponse(current.apiKey);
  }

  public class TestConnectResponse {
    @AuraEnabled
    public String result { get; private set; }

    public TestConnectResponse(Result result) {
      this.result = result.name();
    }
  }

  public class SaveApiKeyResponse {
    @AuraEnabled
    public String result { get; private set; }

    public SaveApiKeyResponse(Result result) {
      this.result = result.name();
    }
  }

  public class GetApiKeyResponse {
    @AuraEnabled
    public String value { get; private set; }

    public GetApiKeyResponse(String value) {
      this.value = value;
    }
  }

  public enum Result {
    SUCCESS,
    FAILURE
  }
}
