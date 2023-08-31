package org.example.gateway;

import org.example.Configuration;
import org.example.model.Demo;
import org.example.repository.*;
import org.example.repository.result.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DemoGateway implements Configuration {

    private final DemoRepository repository;

    public DemoGateway() {
        this.repository = new DemoRepository();
    }

    public void truncate() {
        boolean running = true;
        while (running) {
            Result<FindRecordsResult, ErrorsResult> findResult = this.repository.findRecords();

            // NOTE: Change to switch and pattern matching in the future
            if (findResult instanceof Result.Failure) {
                throw new RuntimeException(((Result.Failure<?>) findResult).value().toString());
            }

            Result.Success<FindRecordsResult> successResult = (Result.Success<FindRecordsResult>) findResult;
            if (successResult.value().totalSize() > 0) {
                List<String> recordIds = successResult.value().records().stream()
                    .map(FindRecordsResult.Record::id)
                    .collect(Collectors.toList());

                Result<DeleteRecordsResult, ErrorsResult> deleteResult = this.repository.deleteRecords(recordIds);
                if (deleteResult instanceof Result.Failure) {
                    throw new RuntimeException(((Result.Failure<?>) deleteResult).value().toString());
                }
            } else {
                running = false;
            }
        }
    }

    public void add(List<Demo> demos) {
        Objects.requireNonNull(demos, "demos must be non-null.");

        demos.forEach(demo -> {
            Result<InsertRecordResult, ErrorsResult> insertResult = this.repository.insert(demo);
            if (insertResult instanceof Result.Failure) {
                throw new RuntimeException(((Result.Failure<?>) insertResult).value().toString());
            }
        });
    }

}
