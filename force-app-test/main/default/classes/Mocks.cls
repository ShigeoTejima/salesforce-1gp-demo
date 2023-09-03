@IsTest
public class Mocks {
  public class MockDemoApi extends DemoApi {
    // getContract
    public Integer getContract_times = 0;
    public Contract getContract_return;

    public override Contract getContract() {
      getContract_times++;

      return getContract_return;
    }

    // getContract with apiKey
    public Integer getContract_with_apiKey_times = 0;
    public String getContract_with_apiKey_params_apiKey;
    public Contract getContract_with_apiKey_return;
    public Exception getContract_with_apiKey_throws;

    public override Contract getContract(String apiKey) {
      getContract_with_apiKey_times++;

      System.assertEquals(getContract_with_apiKey_params_apiKey, apiKey);

      if (getContract_with_apiKey_throws != null) {
        throw getContract_with_apiKey_throws;
      }

      return getContract_with_apiKey_return;
    }
  }

  public class MockDemoApiSettingRepository extends DemoApiSettingRepository {
    public Integer register_times = 0;
    public DemoApiSetting register_params_entity;
    public Exception register_throws;

    public override void register(DemoApiSetting entity) {
      register_times++;

      if (register_params_entity == null) {
        System.assertEquals(null, entity);
      } else {
        System.assertEquals(register_params_entity.apiKey, entity.apiKey);
      }

      if (register_throws != null) {
        throw register_throws;
      }
    }
  }
}
