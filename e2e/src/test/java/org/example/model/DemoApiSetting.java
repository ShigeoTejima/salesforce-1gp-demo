package org.example.model;

import com.google.gson.annotations.SerializedName;

public record DemoApiSetting(
    @SerializedName("SetupOwnerId") String setupOwnerId,
    @SerializedName("demo_aho__api_key__c") String apiKey
) {
    public record ApiKeyContent(
        @SerializedName("demo_aho__api_key__c") String apiKey
    ) {}
}
