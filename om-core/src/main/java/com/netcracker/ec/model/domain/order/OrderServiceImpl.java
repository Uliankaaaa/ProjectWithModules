package com.netcracker.ec.model.domain.order;

import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.services.db.DbWorker;
import com.netcracker.ec.services.db.Queries;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private static final DbWorker DB_WORKER = DbWorker.getInstance();

    @SneakyThrows
    @Override
    public List<Order> getOrdersByObjectId(Integer id) {
        String query = Queries.getQuery("get_orders_by_object_type_id");
        ResultSet resultSet = DB_WORKER.executeSelectQuery(query, id);
        List<Order> orders = new ArrayList<>();
        while (resultSet.next()) {
            orders.add(createOrderByObjectTypeId(resultSet));
        }
        resultSet.close();
        return orders;
    }


    @SneakyThrows
    @Override
    public List<Order> getOrders() {
        String query = Queries.getQuery("get_orders");
        ResultSet resultSet = DB_WORKER.executeSelectQuery(query);
        List<Order> orders = new ArrayList<>();
        while (resultSet.next()) {
            orders.add(createOrderByObjectTypeId(resultSet));
        }
        resultSet.close();
        return orders;
    }

    @SneakyThrows
    private Order createOrderByObjectTypeId(ResultSet resultSet) {
        return new Order(
                resultSet.getInt("object_id"),
                resultSet.getString("name"),
                new NcObjectType(
                        resultSet.getInt("object_type_id"),
                        null,
                        null
                )
        ); */
    }

}