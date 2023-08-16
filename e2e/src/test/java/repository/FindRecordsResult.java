package repository;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FindRecordsResult {
    public int totalSize;
    public boolean done;
    public List<Record> records;

    @Override
    public String toString() {
        return "FindRecordsDto{" +
                "totalSize=" + totalSize +
                ", done=" + done +
                ", records=" + records +
                '}';
    }

    public static class Record {
        public Attributes attributes;

        @SerializedName("Id")
        public String id;

        @Override
        public String toString() {
            return "Record{" +
                    "attributes=" + attributes +
                    ", id='" + id + '\'' +
                    '}';
        }
    }

    public static class Attributes {
        public String type;
        public String url;

        @Override
        public String toString() {
            return "Attributes{" +
                    "type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
