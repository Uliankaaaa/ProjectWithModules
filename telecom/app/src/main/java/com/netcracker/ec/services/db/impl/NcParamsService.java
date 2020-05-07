package com.netcracker.ec.services.db.impl;

import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.services.db.DbWorker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class NcParamsService {
    private static final DbWorker dbWorker = DbWorker.getInstance();

    public NcParamsService() {
    }

    public Set<NcAttribute> getParamsByObjectId(Integer id) {
        Set<NcAttribute> params = new HashSet<>();

        try {
            String sqlQuery = String.format("select a.name, p.attr_id, p.object_id, p.list_value_id, p.value " +
                            "from nc_params p " +
                            "inner join nc_attributes a " +
                            "on a.attr_id = p.attr_id " +
                            "where object_id = %d;",
                    id);
            ResultSet resultSet = dbWorker.executeSelectQuery(sqlQuery);
            while (resultSet.next()) {
                params.add(
                        new NcAttribute(
                                resultSet.getInt("attr_id"),
                                resultSet.getString("name"),
                                resultSet.getInt("attr_schema_id"),
                                new NcAttrTypeDefServiceImpl().getNcAttrTypeDefById(resultSet.getInt("attr_type_def_id"))));
            }
                resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return params;
    }
}