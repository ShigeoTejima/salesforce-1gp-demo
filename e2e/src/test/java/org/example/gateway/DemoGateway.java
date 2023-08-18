package org.example.gateway;

import com.google.gson.Gson;
import org.example.Configuration;
import org.example.model.Demo;
import org.example.repository.FindRecordsResult;
import org.example.repository.GenericRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DemoGateway implements Configuration {

    private final static String OBJECT_NAME_DEMO = "demo_ahd__demo__c";

    private final GenericRepository repository;

    public DemoGateway() {
        this.repository = new GenericRepository();
    }

    public void truncate() {
        boolean running = true;
        while (running) {
            FindRecordsResult findRecordsDto = this.repository.findRecords(OBJECT_NAME_DEMO);
            if (findRecordsDto.totalSize > 0) {
                List<String> recordIds = findRecordsDto.records.stream()
                        .map(record -> record.id)
                        .collect(Collectors.toList());

                this.repository.deleteRecords(recordIds);
            } else {
                running = false;
            }
        }
    }

    public void add(List<Demo> demos) {
        Gson gson = new Gson();
        demos.stream()
             .forEach(demo -> this.repository.insertRecord(OBJECT_NAME_DEMO, gson.toJson(demo)));
    }

}
