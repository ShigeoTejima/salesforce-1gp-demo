package gateway;

import com.google.gson.Gson;
import model.Demo;
import repository.FindRecordsResult;
import repository.SalesforceRepository;

import java.util.List;
import java.util.stream.Collectors;

public class SalesforceGateway {

    private final static String OBJECT_NAME_DEMO = "demo_ahd__demo__c";

    private final SalesforceRepository repository;

    public SalesforceGateway() {
        this.repository = new SalesforceRepository();
    }

    public void truncateDemo() {
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

    public void insertDemos(List<Demo> demos) {
        Gson gson = new Gson();
        demos.stream()
             .forEach(demo -> this.repository.insertRecord(OBJECT_NAME_DEMO, gson.toJson(demo)));
    }
}