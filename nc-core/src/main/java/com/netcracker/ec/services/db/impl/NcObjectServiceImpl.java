package com.netcracker.ec.services.db.impl;

import com.netcracker.ec.model.db.NcEntity;
import com.netcracker.ec.model.db.NcObject;
import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.services.db.DbWorker;
import com.netcracker.ec.services.db.NcObjectService;
import com.netcracker.ec.services.db.Queries;
import lombok.SneakyThrows;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class NcObjectServiceImpl extends NcEntityServiceImpl implements NcObjectService {
    private static final DbWorker DB_WORKER = DbWorker.getInstance();

    public NcObjectServiceImpl() {

    }

    @Override
    public List<Integer> getObjectIdsByByObjectTypeId(Integer otId) {
        String query = Queries.getQuery("get_object_ids_by_parent_ot_desc");
        return DB_WORKER.getIdsByQuery(query, otId);
    }

    @Override
    public List<NcEntity> getNcObjectsAsEntitiesByObjectType(NcObjectType objectType) {
        return getNcObjectsAsEntitiesByObjectTypeId(objectType.getId());
    }

    @SneakyThrows
    @Override
    public NcObject getNcObjectById(Integer objectId) {
        throw new NotImplementedException();
    }

    @Override
    public List<NcEntity> getNcObjectsAsEntitiesByObjectTypeId(Integer objectTypeId) {
        String query = Queries.getQuery("get_objects_by_ot");
        return getNcEntitiesByResultSet(DB_WORKER.executeSelectQuery(query, objectTypeId));
    }

    @Override
    public List<NcEntity> getNcObjectsAsEntities() {
        String query = Queries.getQuery("get_objects");
        return getNcEntitiesByResultSet(DB_WORKER.executeSelectQuery(query));
    }

    @Override
    public void insert(NcObject object) {
        String query = Queries.getQuery("insert_object");
        DB_WORKER.executeQuery(query, object.getId(), object.getName(), object.getObjectType().getId(), object.getDescription());
    }
}
