package com.netcracker.ec.model.db;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NcObjectType extends NcEntity {
    private Integer parentId;

    public NcObjectType() {
    }

    public NcObjectType(Integer id, String name, Integer parentId) {
        super(id, name);
        this.parentId = parentId;
    }
}
