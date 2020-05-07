package com.netcracker.ec.services.db.impl;

import com.netcracker.ec.model.db.NcAttrTypeDef;
import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.services.db.DbWorker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NcParamsService {
    private static final DbWorker dbWorker = DbWorker.getInstance();

    public Map<NcAttribute, String> getParamsByObjectId(Integer id) {
        Map<NcAttribute, String> params = new HashMap<>();

        try {
            String sqlQuery = String.format("select a.name, p.attr_id, p.object_id, p.list_value_id, p.value, " +
                            "a.attr_schema_id, a.attr_type_def_id " +
                            "from nc_params p " +
                            "inner join nc_attributes a " +
                            "on a.attr_id = p.attr_id " +
                            "where object_id = %d;",
                    id);
            ResultSet resultSet = dbWorker.executeSelectQuery(sqlQuery);
            while (resultSet.next()) {
                params.put(
                        new NcAttribute(
                                resultSet.getInt("attr_id"),
                                resultSet.getString("name"),
                                resultSet.getInt("attr_schema_id"),
                                new NcAttrTypeDefServiceImpl().getNcAttrTypeDefById(resultSet.getInt("attr_type_def_id"))),
                        resultSet.getString("value"));
            }
                resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return params;
    }
}