package org.example.repository;

import org.example.model.DemoApiSetting;

public class DemoApiSettingRepository extends GenericRepository {

    public DemoApiSettingRepository() {
        super();
    }

    public Result<FindRecordsResult, ErrorsResult> find(String setupOwnerId) {
        String query = String.format("SELECT Id FROM demo_aho__demo_api_setting__c WHERE SetupOwnerId='%s'", setupOwnerId);
        return findRecords("demo_aho__demo_api_setting__c", query);
    }

    public Result<InsertRecordResult, ErrorsResult> insert(DemoApiSetting demoApiSetting) {
        return insertRecord("demo_aho__demo_api_setting__c", demoApiSetting);
    }

    public <T> Result<InsertRecordResult, ErrorsResult> update(String recordId, T updateContent) {
        return updateRecord("demo_aho__demo_api_setting__c", recordId, updateContent);
    }

}
