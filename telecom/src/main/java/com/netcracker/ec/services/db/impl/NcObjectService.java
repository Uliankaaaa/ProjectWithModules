package com.netcracker.ec.services.db.impl;

import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.model.domain.order.Order;
import com.netcracker.ec.services.db.DbWorker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NcObjectService {
    private static final DbWorker dbWorker = DbWorker.getInstance();

    public NcObjectService() {
    }

    public Order insertOrder(Order order) {
        String sqlQuery = String.format("insert into nc_objects values(%d, '%s', %d, null);",
                order.getId(),
                order.getName(),
                order.getObjectType().getId());
        dbWorker.executeInsert(sqlQuery);

        return order;
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();

        try {
            String sqlQuery = "select o.object_id, o.name, o.object_type_id, t.parent_id " +
                    "from nc_objects o " +
                    "inner join nc_object_types t " +
                    "on o.object_type_id = t.object_type_id " +
                    "where t.parent_id = 2;";
            ResultSet resultSet = dbWorker.executeSelect(sqlQuery);
            while (resultSet.next()) {
                orders.add(
                        new Order(
                                resultSet.getInt("object_id"),
                                resultSet.getString("name"),
                                new NcObjectType(
                                        resultSet.getInt("object_type_id"),
                                        null,
                                        null
                                )
                        )
                );
            }
                resultSet.close();

            } catch(SQLException e){
                e.printStackTrace();
            }
            return orders;
        }

        public List<Order> getOrdersByObjectTypeId (Integer id){
            List<Order> orders = new ArrayList<>();

            try {
                String sqlQuery = String.format("select o.object_id, o.name, o.object_type_id, t.parent_id " +
                        "from nc_objects o " +
                        "inner join nc_object_types t " +
                        "on o.object_type_id = t.object_type_id " +
                        "where t.parent_id = 2 " +
                        "and o.object_type_id = %d;", id);

                ResultSet resultSet = dbWorker.executeSelect(sqlQuery);
                while (resultSet.next()) {
                    orders.add(
                            new Order(
                                    resultSet.getInt("object_id"),
                                    resultSet.getString("name"),
                                    new NcObjectType(
                                            resultSet.getInt("object_type_id"),
                                            null,
                                            null
                                    )
                            )
                    );
                }
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return orders;
        }

        public ResultSet selectOrdersObject () {
            String sqlQuery = "select * from nc_objects o, nc_object_types ot\n" +
                    " where o.object_type_id = ot.object_type_id and ot.parent_id = 2";

            return dbWorker.executeSelect(sqlQuery);
        }

        public ResultSet selectOrder ( int id){
            String sqlQuery = "select a.attr_id, ao.object_type_id," +
                    "a.name, a.attr_type_def_id, d.type " +
                    "from nc_attr_object_types ao " +
                    "inner join nc_attributes a " +
                    "on ao.attr_id = a.attr_id " +
                    "inner join nc_attr_type_defs d " +
                    "on a.attr_type_def_id = d.attr_type_def_id " +
                    "where ao.object_type_id in (2, " + id + ");";

            return dbWorker.executeSelect(sqlQuery);
        }
    }