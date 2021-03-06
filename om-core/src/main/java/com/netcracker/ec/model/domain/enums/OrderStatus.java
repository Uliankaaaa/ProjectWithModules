package com.netcracker.ec.model.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    NA(405),
    ENTERING(406),
    READY_FOR_PROCESSING(407),
    PROCESSING(408),
    COMPLETED(409),
    CANCELED(410),
    SUPERSEDED(411);

    private final Integer lvId;

    public static OrderStatus valueOf(Integer id) {
        return Arrays.stream(values())
                .filter(sts -> sts.lvId.equals(id))
                .findFirst().orElse(null);
    }
}
