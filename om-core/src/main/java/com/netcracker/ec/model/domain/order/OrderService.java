package com.netcracker.ec.model.domain.order;

import java.util.List;

public interface OrderService {

    public List<Order> getOrdersByObjectId(Integer id);

    public List<Order> getOrders();
}
