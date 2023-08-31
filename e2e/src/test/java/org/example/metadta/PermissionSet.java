package org.example.metadta;

public enum PermissionSet {
    Demo("demo"),
    Contract("contract");

    private final String name;
    PermissionSet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
