package org.example.metadta;

public enum PermissionSet {
    Demo("demo"),
    Contract("contract"),
    Setting("setting");

    private final String name;
    PermissionSet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
