package com.netcracker.ec.model.db;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NcAttribute extends NcEntity {
    private NcAttrTypeDef attrTypeDef;
//    private Integer attrSchemaId;

    public NcAttribute(Integer id, String name, NcAttrTypeDef attrTypeDef) {
        super(id, name);
//        this.attrSchemaId = attrSchemaId;
        this.attrTypeDef = attrTypeDef;
    }
}
