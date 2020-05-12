package com.netcracker.ec.services.db.impl;

import com.netcracker.ec.model.db.NcEntity;
import com.netcracker.ec.services.db.DbWorker;
import com.netcracker.ec.services.db.NcListValueService;
import com.netcracker.ec.services.db.Queries;

import java.util.List;

public class NcListValueServiceImpl extends NcEntityServiceImpl implements NcListValueService {
    private static final DbWorker DB_WORKER = DbWorker.getInstance();

    public NcListValueServiceImpl() {

    }

    @Override
    public List<NcEntity> getNcListValuesAsEntitiesByNcAttrTypeDefId(Integer attrTypeDefId) {
        String query = Queries.getQuery("get_list_values_by_attr_type_def");
        return getNcEntitiesByResultSet(DB_WORKER.executeSelectQuery(query, attrTypeDefId));
    }

    @Override
    public String getNcListValueByNcListValueId(Integer ncListValueId){
        String query = Queries.getQuery("get_list_value_by_list_value_id");
        return DB_WORKER.getStringValueByQuery(query, ncListValueId);
    }
}
