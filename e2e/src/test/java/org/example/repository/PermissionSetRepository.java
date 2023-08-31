package org.example.repository;

import org.example.Configuration;
import org.example.model.PermissionSetAssignment;
import org.example.repository.result.ErrorsResult;
import org.example.repository.result.FindRecordsResult;
import org.example.repository.result.InsertRecordResult;

public class PermissionSetRepository extends GenericRepository implements Configuration {

    private static final String PERMISSION_SET_OBJECT_NAME = "PermissionSet";
    private static final String PERMISSION_SET_ASSIGNMENT_OBJECT_NAME = "PermissionSetAssignment";

    public PermissionSetRepository() {
        super();
    }

    public Result<FindRecordsResult, ErrorsResult> findPermissionSet(String namespacePrefix, String name) {
        String query = String.format("SELECT Id FROM %s WHERE NamespacePrefix='%s' AND Name='%s'", PERMISSION_SET_OBJECT_NAME, namespacePrefix, name);
        return findRecords(PERMISSION_SET_OBJECT_NAME, query);
    }

    public Result<FindRecordsResult, ErrorsResult> findPermissionSetAssignment(String userId, String permissionSetId) {
        String query = String.format("SELECT Id FROM %s WHERE AssigneeId='%s' AND PermissionSetId='%s'", PERMISSION_SET_ASSIGNMENT_OBJECT_NAME, userId, permissionSetId);
        return findRecords(PERMISSION_SET_ASSIGNMENT_OBJECT_NAME, query);
    }

    public Result<InsertRecordResult, ErrorsResult> insertPermissionSetAssignment(PermissionSetAssignment permissionSetAssignment) {
        return insertRecord(PERMISSION_SET_ASSIGNMENT_OBJECT_NAME, permissionSetAssignment);
    }

}
