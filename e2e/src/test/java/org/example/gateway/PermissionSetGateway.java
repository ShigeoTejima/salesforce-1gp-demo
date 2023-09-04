package org.example.gateway;

import org.example.Configuration;
import org.example.metadta.PermissionSet;
import org.example.model.PermissionSetAssignment;
import org.example.repository.*;
import org.example.repository.result.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PermissionSetGateway implements Configuration {

    private final PermissionSetRepository repository;

    public PermissionSetGateway() {
        this.repository = new PermissionSetRepository();
    }

    public void assignToDemo() {
        assignToPermissionSet(PermissionSet.Demo);
    }
    public void unAssignFromDemo() {
        unAssignFromPermissionSet(PermissionSet.Demo);
    }

    public void assignToContract() {
        assignToPermissionSet(PermissionSet.Contract);
    }
    public void unAssignFromContract() {
        unAssignFromPermissionSet(PermissionSet.Contract);
    }

    public void assignToSetting() {
        assignToPermissionSet(PermissionSet.Setting);
    }
    public void unAssignFromSetting() {
        unAssignFromPermissionSet(PermissionSet.Setting);
    }

    private void assignToPermissionSet(PermissionSet permissionSet) {
        String userId = StandardUser.userId();
        String namespacePrefix = namespacePrefix();
        String permissionSetName = permissionSet.getName();
        String permissionSetId = getPermissionSetId(namespacePrefix, permissionSetName)
                .orElseThrow(() -> new RuntimeException(String.format("not found permissionSet. namespacePrefix=%s , name=%s", namespacePrefix, permissionSetName)));

        Result<FindRecordsResult, ErrorsResult> findResult = this.repository.findPermissionSetAssignment(userId, permissionSetId);
        if (findResult instanceof Result.Failure) {
            throw new RuntimeException(((Result.Failure) findResult).value().toString());
        }

        Result.Success<FindRecordsResult> successResult = (Result.Success<FindRecordsResult>) findResult;
        if (successResult.value().totalSize() == 0) {
            Result<InsertRecordResult, ErrorsResult> insertResult = this.repository.insertPermissionSetAssignment(new PermissionSetAssignment(userId, permissionSetId));
            if (insertResult instanceof Result.Failure) {
                throw new RuntimeException(((Result.Failure) insertResult).value().toString());
            }
        }
    }
    private void unAssignFromPermissionSet(PermissionSet permissionSet) {
        String userId = StandardUser.userId();
        String namespacePrefix = namespacePrefix();
        String permissionSetName = permissionSet.getName();
        String permissionSetId = getPermissionSetId(namespacePrefix, permissionSetName)
                .orElseThrow(() -> new RuntimeException(String.format("not found permissionSet. namespacePrefix=%s , name=%s", namespacePrefix, permissionSetName)));

        Result<FindRecordsResult, ErrorsResult> findResult = this.repository.findPermissionSetAssignment(userId, permissionSetId);
        if (findResult instanceof Result.Failure) {
            throw new RuntimeException(((Result.Failure) findResult).value().toString());
        }

        Result.Success<FindRecordsResult> successResult = (Result.Success<FindRecordsResult>) findResult;
        if (successResult.value().totalSize() > 0) {
            List<String> recordIds = successResult.value().records().stream()
                    .map(record -> record.id())
                    .collect(Collectors.toList());

            Result<DeleteRecordsResult, ErrorsResult> deleteResult = this.repository.deleteRecords(recordIds);
            if (deleteResult instanceof Result.Failure) {
                throw new RuntimeException(((Result.Failure) deleteResult).value().toString());
            }
        }
    }

    private Optional<String> getPermissionSetId(String namespacePrefix, String name) {
        Result<FindRecordsResult, ErrorsResult> findResult = this.repository.findPermissionSet(namespacePrefix, name);
        if (findResult instanceof Result.Failure) {
            throw new RuntimeException(((Result.Failure) findResult).value().toString());
        }

        Result.Success<FindRecordsResult> successResult = (Result.Success<FindRecordsResult>) findResult;
        if (successResult.value().totalSize() > 1) {
            throw new IllegalStateException(String.format("found permissionSet does not one. detail=%s", successResult.toString()));
        }

        return successResult.value().records().stream()
                .map(FindRecordsResult.Record::id)
                .findFirst();
    }
}
