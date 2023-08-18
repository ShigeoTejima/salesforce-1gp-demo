package org.example.repository;

import org.example.Configuration;
import org.example.model.Demo;

public class DemoRepository extends GenericRepository implements Configuration {

    private final static String OBJECT_NAME = "demo_ahd__demo__c";

    public DemoRepository() {
        super();
    }

    public FindRecordsResult findRecords() {
        return findRecords(OBJECT_NAME);
    }

    public InsertRecordResult insert(Demo demo) {
        return insertRecord(OBJECT_NAME, demo);
    }

}
