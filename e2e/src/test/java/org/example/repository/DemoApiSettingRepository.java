package org.example.repository;

import org.example.Configuration;
import org.example.model.DemoApiSetting;
import org.example.repository.result.ErrorsResult;
import org.example.repository.result.FindRecordsResult;
import org.example.repository.result.InsertRecordResult;

public class DemoApiSettingRepository extends GenericRepository implements Configuration {

    public DemoApiSettingRepository() {
        super();
    }

    public Result<FindRecordsResult, ErrorsResult> find(String setupOwnerId) {
        String objectName = getObjectName();
        String query = String.format("SELECT Id FROM %s WHERE SetupOwnerId='%s'", objectName, setupOwnerId);
        return findRecords(objectName, query);
    }

    public Result<InsertRecordResult, ErrorsResult> insert(DemoApiSetting demoApiSetting) {
        return insertRecord(getObjectName(), demoApiSetting);
    }

    public <T> Result<InsertRecordResult, ErrorsResult> update(String recordId, T updateContent) {
        return updateRecord(getObjectName(), recordId, updateContent);
    }

    private String getObjectName() {
        return String.format("%s__demo_api_setting__c", getNamespacePrefix());
    }
}
