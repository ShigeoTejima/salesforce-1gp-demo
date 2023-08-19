package org.example.repository;

import org.example.Configuration;
import org.example.model.PermissionSetAssignment;

import java.util.List;

public class PermissionSetRepository extends GenericRepository implements Configuration {

    public PermissionSetRepository() {
        super();
    }

    public Result<FindRecordsResult, List<ErrorResult>> findPermissionSetAssignment(String userId, String permissionSetId) {
        String query = String.format("SELECT Id FROM PermissionSetAssignment WHERE AssigneeId='%s' AND PermissionSetId='%s'", userId, permissionSetId);
        return findRecords("PermissionSetAssignment", query);
    }

    public Result<InsertRecordResult, List<ErrorResult>> insertPermissionSetAssignment(PermissionSetAssignment permissionSetAssignment) {
        return insertRecord("PermissionSetAssignment", permissionSetAssignment);
    }

}
