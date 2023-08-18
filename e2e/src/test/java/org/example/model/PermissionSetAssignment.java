package org.example.model;

import com.google.gson.annotations.SerializedName;

public record PermissionSetAssignment(
    @SerializedName("AssigneeId") String assigneeId,
    @SerializedName("PermissionSetId") String permissionSetId) {
}
