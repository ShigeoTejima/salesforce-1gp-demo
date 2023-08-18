package org.example.repository;

import org.example.Configuration;
import org.example.model.PermissionSetAssignment;

public class PermissionSetRepository extends GenericRepository implements Configuration {

    public PermissionSetRepository() {
        super();
    }

    public FindRecordsResult findPermissionSetAssignment(String userId, String permissionSetId) {
        String query = String.format("SELECT Id FROM PermissionSetAssignment WHERE AssigneeId='%s' AND PermissionSetId='%s'", userId, permissionSetId);
        return findRecords("PermissionSetAssignment", query);
    }

    public InsertRecordResult insertPermissionSetAssignment(PermissionSetAssignment permissionSetAssignment) {
        return insertRecord("PermissionSetAssignment", permissionSetAssignment);
    }

}
