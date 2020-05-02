package com.netcracker.ec.model.domain.enums;

public enum OperationType {
    CREATE_ORDER("1", "Create Order"),
    SHOW_ORDERS("2", "Show Orders"),
    EXIT("0", "Exit");

    private String id;
    private String name;

    OperationType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }

    public static OperationType getOperationById(String id) {
        for (OperationType value : values()) {
            if (value.id.equals(id)) {
                return value;
            }
        }
        return null;
    }
}