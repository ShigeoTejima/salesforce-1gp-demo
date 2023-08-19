package org.example.gateway;

import org.example.Configuration;
import org.example.model.Demo;
import org.example.repository.*;

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
            Result<FindRecordsResult, List<ErrorResult>> findResult = this.repository.findRecords();

            // NOTE: Change to switch and pattern matching in the future
            if (findResult instanceof Result.Failure) {
                throw new RuntimeException(((Result.Failure) findResult).value().toString());
            }

            Result.Success<FindRecordsResult> successResult = (Result.Success<FindRecordsResult>) findResult;
            if (successResult.value().totalSize > 0) {
                List<String> recordIds = successResult.value().records.stream()
                    .map(record -> record.id)
                    .collect(Collectors.toList());

                Result<List<DeleteRecordResult>, List<ErrorResult>> deleteResult = this.repository.deleteRecords(recordIds);
                if (deleteResult instanceof Result.Failure) {
                    throw new RuntimeException(((Result.Failure) deleteResult).value().toString());
                }
            } else {
                running = false;
            }
        }
    }

    public void add(List<Demo> demos) {
        demos.stream().forEach(demo -> {
            Result<InsertRecordResult, List<ErrorResult>> insertResult = this.repository.insert(demo);
            if (insertResult instanceof Result.Failure) {
                throw new RuntimeException(((Result.Failure) insertResult).value().toString());
            }
        });
    }

}
