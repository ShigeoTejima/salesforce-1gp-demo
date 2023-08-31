package org.example.repository;

import org.example.Configuration;
import org.example.model.Demo;
import org.example.repository.result.ErrorsResult;
import org.example.repository.result.FindRecordsResult;
import org.example.repository.result.InsertRecordResult;

public class DemoRepository extends GenericRepository implements Configuration {

    public DemoRepository() {
        super();
    }

    public Result<FindRecordsResult, ErrorsResult> findRecords() {
        return findRecords(getObjectName());
    }

    public Result<InsertRecordResult, ErrorsResult> insert(Demo demo) {
        return insertRecord(getObjectName(), demo);
    }

    private String getObjectName() {
        return String.format("%s__demo__c", namespacePrefix());
    }
}
