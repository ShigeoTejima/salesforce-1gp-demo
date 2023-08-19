package org.example.repository;

import org.example.Configuration;
import org.example.model.Demo;

import java.util.List;

public class DemoRepository extends GenericRepository implements Configuration {

    private final static String OBJECT_NAME = "demo_ahd__demo__c";

    public DemoRepository() {
        super();
    }

    public Result<FindRecordsResult, List<ErrorResult>> findRecords() {
        return findRecords(OBJECT_NAME);
    }

    public Result<InsertRecordResult, List<ErrorResult>> insert(Demo demo) {
        return insertRecord(OBJECT_NAME, demo);
    }
}
