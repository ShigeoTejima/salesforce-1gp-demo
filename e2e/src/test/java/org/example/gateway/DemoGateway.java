package org.example.gateway;

import org.example.Configuration;
import org.example.model.Demo;
import org.example.repository.DemoRepository;
import org.example.repository.FindRecordsResult;

import java.util.List;
import java.util.stream.Collectors;

public class DemoGateway implements Configuration {

    private final DemoRepository repository;

    public DemoGateway() {
        this.repository = new DemoRepository();
    }

    public void truncate() {
        boolean running = true;
        while (running) {
            FindRecordsResult findRecordsDto = this.repository.findRecords();
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
        demos.stream().forEach(demo -> this.repository.insert(demo));
    }

}
