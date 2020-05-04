package com.netcracker.ec.services.db.impl;

import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.services.db.DbWorker;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NcObjectTypeServiceLast {
    private static final DbWorker dbWorker = DbWorker.getInstance();

    public NcObjectTypeServiceLast() {
    }

    public Map<Integer, String> getOrdersObjectTypeNameMap() {
        List<NcObjectType> objectTypesList = new NcObjectTypeServiceImpl().getObjectTypesByParentId(2);
        Map<Integer, String> objectTypesMap = new HashMap<>();
        objectTypesList.forEach(objectType -> objectTypesMap
                .put(objectType.getId(), objectType.getName()));
        return objectTypesMap;
    }
}