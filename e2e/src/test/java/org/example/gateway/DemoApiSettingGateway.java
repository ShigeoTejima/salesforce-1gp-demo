package org.example.gateway;

import org.example.Configuration;
import org.example.model.DemoApiSetting;
import org.example.repository.*;
import org.example.repository.result.ErrorsResult;
import org.example.repository.result.FindRecordsResult;
import org.example.repository.result.InsertRecordResult;
import org.example.repository.Result;
import org.example.repository.result.RunAnonymousApexResult;

public class DemoApiSettingGateway implements Configuration {

    private final DemoApiSettingRepository repository;

    public DemoApiSettingGateway() {
        this.repository = new DemoApiSettingRepository();
    }

    // NOTE: カスタム設定が保護されている場合に、REST APIから参照できないため更新できなかった
    public void setApiKey(String apiKey) {
        String setupOwnerId = Organization.id();
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

    public void setApiKeyByAnonymousApex(String apiKey) {
        String snippetTemplate = """
                demo_aho.DemoApiSettingRepository sut = new demo_aho.DemoApiSettingRepository();
                demo_aho.DemoApiSettingRepository.DemoApiSetting setting = new demo_aho.DemoApiSettingRepository.DemoApiSetting('${apiKey}');
                sut.register(setting);
                """;

        String snippet = snippetTemplate.replaceAll("\\$\\{apiKey\\}", apiKey);
        Result<RunAnonymousApexResult, ErrorsResult> runResult = this.repository.runAnonymousApex(snippet);
        if (runResult instanceof Result.Failure) {
            throw new RuntimeException(((Result.Failure) runResult).value().toString());
        }

        Result.Success<RunAnonymousApexResult> result = (Result.Success<RunAnonymousApexResult>) runResult;
        if (!result.value().success()) {
            throw new RuntimeException(result.toString());
        }

    }
}
