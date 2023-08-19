package org.example.repository;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record FindRecordsResult(int totalSize, boolean done, List<Record> records) {
    public record Record(Attributes attributes, @SerializedName("Id") String id) {}
    public record Attributes(String type, String url) {}
}
