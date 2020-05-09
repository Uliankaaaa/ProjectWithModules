package com.netcracker.ec.services.db;

import com.netcracker.ec.model.db.NcEntity;
import com.netcracker.ec.model.db.NcObject;
import com.netcracker.ec.model.db.NcObjectType;

import java.util.List;

public interface NcObjectService extends NcEntityService {
    List<Integer> getObjectIdsByByObjectTypeId(Integer otId);

    List<NcEntity> getNcObjectsAsEntitiesByObjectType(NcObjectType objectType);

<<<<<<< HEAD
    List<NcEntity> getNcObjectsAsEntitiesByObjectTypeId(Integer objectTypeId);
=======
    NcObject getNcObjectById(Integer objectId);
>>>>>>> master

    void insert(NcObject object);
}
