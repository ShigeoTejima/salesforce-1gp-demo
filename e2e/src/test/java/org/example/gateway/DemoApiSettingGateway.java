package org.example.gateway;

import org.example.Configuration;
import org.example.model.DemoApiSetting;
import org.example.repository.*;
import org.example.repository.result.ErrorsResult;
import org.example.repository.result.FindRecordsResult;
import org.example.repository.result.InsertRecordResult;
import org.example.repository.Result;

public class DemoApiSettingGateway implements Configuration {

    private final DemoApiSettingRepository repository;

    public DemoApiSettingGateway() {
        this.repository = new DemoApiSettingRepository();
    }

    public void setApiKey(String apiKey) {
        String setupOwnerId = getOrganizationId();
        Result<FindRecordsResult, ErrorsResult> findResult = this.repository.find(setupOwnerId);
        if (findResult instanceof Result.Failure) {
            throw new RuntimeException(((Result.Failure) findResult).value().toString());
        }

        Result.Success<FindRecordsResult> successResult = (Result.Success<FindRecordsResult>) findResult;
        if (successResult.value().totalSize() > 1) {
            throw new RuntimeException("Unexpectedly, DemoApiSetting size is over than 1.");
        }

        if (successResult.value().totalSize() == 0) {
            Result<InsertRecordResult, ErrorsResult> insertResult = this.repository.insert(new DemoApiSetting(setupOwnerId, apiKey));
            if (insertResult instanceof Result.Failure) {
                throw new RuntimeException(((Result.Failure) insertResult).value().toString());
            }

        } else {
            String recordId = successResult.value().records()
                    .stream()
                    .findFirst()
                    .map(FindRecordsResult.Record::id)
                    .get();

            Result<InsertRecordResult, ErrorsResult> updateResult = this.repository.update(recordId, new DemoApiSetting.ApiKeyContent(apiKey));
            if (updateResult instanceof Result.Failure) {
                throw new RuntimeException(((Result.Failure) updateResult).value().toString());
            }
        }
    }
}
