package org.example.gateway;

import org.example.Configuration;
import org.example.model.PermissionSetAssignment;
import org.example.repository.*;

import java.util.List;
import java.util.stream.Collectors;

public class PermissionSetGateway implements Configuration {

    private final PermissionSetRepository repository;

    public PermissionSetGateway() {
        this.repository = new PermissionSetRepository();
    }

    public void assignToDemo() {
        String userId = getUserId();
        String permissionSetId = getPermissionSetDemoId();

        Result<FindRecordsResult, List<ErrorResult>> findResult = this.repository.findPermissionSetAssignment(userId, permissionSetId);
        if (findResult instanceof Result.Failure) {
            throw new RuntimeException(((Result.Failure) findResult).value().toString());
        }

        Result.Success<FindRecordsResult> successResult = (Result.Success<FindRecordsResult>) findResult;
        if (successResult.value().totalSize == 0) {
            Result<InsertRecordResult, List<ErrorResult>> insertResult = this.repository.insertPermissionSetAssignment(new PermissionSetAssignment(userId, permissionSetId));
            if (insertResult instanceof Result.Failure) {
                throw new RuntimeException(((Result.Failure) insertResult).value().toString());
            }
        }

    }

    public void unAssignFromDemo() {
        String userId = getUserId();
        String permissionSetId = getPermissionSetDemoId();

        Result<FindRecordsResult, List<ErrorResult>> findResult = this.repository.findPermissionSetAssignment(userId, permissionSetId);
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
        }

    }
}
