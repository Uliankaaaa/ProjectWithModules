package com.netcracker.ec.services.db.impl;

import com.netcracker.ec.model.db.NcAttrTypeDef;
import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.services.db.DbWorker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NcAttrService {
    private static final DbWorker dbWorker = DbWorker.getInstance();

    public NcAttrService () {
    }

    public List<NcAttribute> getAttributesByOrderType(Integer id) {
        List<NcAttribute> attributes = new ArrayList<>();

        try {
            String sqlQuery = String.format(
                    "select a.attr_id, ao.object_type_id," +
                            "a.name, a.attr_type_def_id, d.type " +
                            "from nc_attr_object_types ao " +
                            "inner join nc_attributes a " +
                            "on ao.attr_id = a.attr_id " +
                            "inner join nc_attr_type_defs d " +
                            "on a.attr_type_def_id = d.attr_type_def_id " +
                            "where ao.object_type_id = 2 or ao.object_type_id = %d;",
                            id);

            ResultSet resultSet = dbWorker.executeSelectQuery(sqlQuery);
            while (resultSet.next()) {
                attributes.add(
                        new NcAttribute(
                                resultSet.getInt("attr_id"),
                                resultSet.getString("name"),
                                new NcAttrTypeDef(
                                        resultSet.getInt("attr_type_def_id"),
                                        null,
                                        resultSet.getInt("type"))
                        ));
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attributes;
    }
}