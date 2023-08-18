package org.example.gateway;

import org.example.Configuration;
import org.example.repository.FindRecordsResult;
import org.example.repository.PermissionSetRepository;

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

        FindRecordsResult permissionSetAssignment = this.repository.findPermissionSetAssignment(userId, permissionSetId);
        if (permissionSetAssignment.totalSize == 0) {
            this.repository.insertPermissionSetAssignment(userId, permissionSetId);
        }
    }

    public void unAssignFromDemo() {
        String userId = getUserId();
        String permissionSetId = getPermissionSetDemoId();

        FindRecordsResult permissionSetAssignment = this.repository.findPermissionSetAssignment(userId, permissionSetId);
        if (permissionSetAssignment.totalSize > 0) {
            List<String> recordIds = permissionSetAssignment.records.stream()
                    .map(record -> record.id)
                    .collect(Collectors.toList());

            this.repository.deleteRecords(recordIds);
        }
    }
}
