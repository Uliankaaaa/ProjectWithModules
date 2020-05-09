package com.netcracker.ec.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OperationType {
    EXIT(0, "Exit"),
    CREATE_ORDER(1, "Create Order"),
    SHOW_ORDERS(2, "Show Orders");

    private Integer id;
    private String name;

    @Override
    public String toString() {
        return id + " - " + name;
    }

    public static OperationType valueOf(Integer id) {
        return Arrays.stream(values())
                .filter(value -> value.getId().equals(id))
                .findFirst().orElse(null);
    }
}