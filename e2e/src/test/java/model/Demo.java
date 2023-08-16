package model;

import com.google.gson.annotations.SerializedName;

public record Demo(
    @SerializedName("Name") String name,
    @SerializedName("demo_ahd__description__c") String description
) {}
