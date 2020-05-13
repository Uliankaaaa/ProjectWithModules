package com.netcracker.ec.services.db.impl;

import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.model.db.NcEntity;
import com.netcracker.ec.model.db.NcObject;
import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.services.db.DbWorker;
import com.netcracker.ec.services.db.NcObjectService;
import com.netcracker.ec.services.db.Queries;
import lombok.SneakyThrows;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public List<NcObject> getNcObjectsByParentId(Integer parentId){
        String query = Queries.getQuery("get_objects_by_parent_id");
        return getNcObjectsByResultSet(DB_WORKER.executeSelectQuery(query, parentId));
    }

    @Override
    public List<NcObject> getNcObjectsByObjectTypeId(Integer objectTypeId){
        String query = Queries.getQuery("get_objects_by_object_type_id");
        return getNcObjectsByResultSet(DB_WORKER.executeSelectQuery(query, objectTypeId));
    }

    @Override
    public String getObjectNameByID(Integer id){
        String query = Queries.getQuery("get_object_name_by_id");
        return DB_WORKER.getStringValueByQuery(query, id);
    }

    @SneakyThrows
    private List<NcObject> getNcObjectsByResultSet(ResultSet resultSet) {
        List<NcObject> attributes = new ArrayList<>();
        while (resultSet.next()) {
            attributes.add(createNcObjectByResultSet(resultSet));
        }
        resultSet.close();
        return attributes;
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

    @SneakyThrows
    private NcObject createNcObjectByResultSet(ResultSet resultSet) throws SQLException {
        return new NcObject(resultSet.getInt(1),
                resultSet.getString(2),
                new NcObjectType(resultSet.getInt(5),
                        resultSet.getString(6),
                        resultSet.getInt(7)));
    }
}
