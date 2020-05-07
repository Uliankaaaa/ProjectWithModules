/*package com.netcracker.ec.provisioning.operations;

import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.model.domain.oder.Order;
import com.netcracker.Console;
import com.netcracker.NcAttrService;
import com.netcracker.NcObjectService;
import com.netcracker.NcObjectTypeServiceLast;
import com.netcracker.NcParamsService;
import com.netcracker.UserInput;
import com.netcracker.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ShowOrdersOperation implements Operation {
    private final NcObjectTypeServiceLast ncObjectTypeServiceLast;
    private final NcAttrService ncAttributeService;
    private final NcObjectService ncObjectService;
    private final NcParamsService ncParamsService;

    public ShowOrdersOperation() {
        this.ncObjectTypeServiceLast = new NcObjectTypeServiceLast();
        this.ncAttributeService = new NcAttrService();
        this.ncObjectService = new NcObjectService();
        this.ncParamsService = new NcParamsService();
    }

    @Override
    public void execute() {
        Printer.print("\nAll Orders: ");

        Map<Integer, NcObjectType> orderObjectTypeMap = ncObjectTypeServiceLast.getOrderObjectTypes();
        orderObjectTypeMap.forEach((key, value) -> Printer.print(key + " - " + value.getName()));

        Integer objectTypeId = UserInput.getOrderTypeId(orderObjectTypeMap.keySet());
        NcObjectType selectedObjectType = orderObjectTypeMap.get(objectTypeId);

        ResultSet order = selectOrders();

        try {
            int count = 1;
            while (order.next()) {
                String orderName = order.getString("name");
                if (substringInString(orderName, generateOrderName(selectedObjectType))){
                    Printer.print("Order" + count + " Name: " + orderName);
                    ResultSet resultSet = selectSetOrderParams(selectedObjectType.getId());
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        int attrTypeDefId = resultSet.getInt("attr_type_def_id");
                        Printer.print("\t" + name + ": " + attrTypeDefId);
                    }
                count++;}
            }
        } catch (SQLException ex) {
            Printer.print(ex.toString());
        }
    }

    private ResultSet selectOrders() {
        return ncObjectService.selectOrdersObject();
    }

    private ResultSet selectSetOrderParams(int id) {
        return ncObjectService.selectOrder(id);
    }

    private String generateOrderName(NcObjectType objectType) {
        return String.join(" ", UserInput.scan(objectType).next());
    }

    private boolean substringInString(String name, String substring) {
        int index = name.indexOf(substring);
        return !(index == -1);
    }

}*/


package com.netcracker.ec.provisioning.operations;

import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.model.domain.enums.AttributeType;
import com.netcracker.ec.model.domain.order.OrderServiceImpl;
import com.netcracker.ec.services.db.impl.NcObjectTypeServiceImpl;
import com.netcracker.ec.services.db.impl.NcParamsService;
import com.netcracker.ec.services.console.Console;
import com.netcracker.ec.model.domain.order.Order;
import com.netcracker.ec.services.db.impl.NcParamsServiceImpl;
import com.netcracker.ec.services.db.impl.NcReferencesServiceImpl;
import com.netcracker.ec.util.UserInput;
import com.netcracker.ec.view.Printer;

import java.util.List;
import java.util.Map;

public class ShowOrdersOperation implements Operation {
    private final NcParamsService ncParamsService;

    private final Console console = Console.getInstance();

    public ShowOrdersOperation() {
        this.ncParamsService = new NcParamsService();
    }

    @Override
    public void execute() {
        System.out.println("Please Select Operation.");

        Printer.print("1 - Show All Orders\n" +
                "2 - Show Orders Of A Specific Object Type");
        Integer operationModification = UserInput.nextOperationId();

        switch (operationModification) {
            case 1:
                showAllOrders();
                break;
            case 2:
                showOrderOfASpecificObjectType();
                break;
            default:
                break;
        }
    }

    private void showAllOrders() {
        List<Order> orders = new OrderServiceImpl().getOrders();

        orders.forEach(order ->
               // order.setParameters(
                getParams(
               ncParamsService.getParamsByObjectId(order.getId()), order.getId()
               // getParams(order.getParams(), order.getId()));
        ));

        initOrdersParameters(orders);
        printOrders(orders);
    }

    private void showOrderOfASpecificObjectType() {
        List<NcObjectType> objectTypesList = new NcObjectTypeServiceImpl().getObjectTypesByParentId(2);
        objectTypesList.forEach(objectType -> Printer.print(objectType.toFormattedOutput()));

        Integer objectTypeId = UserInput.nextOperationId();
        List<Order> orders = new OrderServiceImpl().getOrdersByObjectId(objectTypeId);

        initOrdersParameters(orders);

        printOrders(orders);
    }

    private void initOrdersParameters(List<Order> orders) {
        orders.forEach(order -> getParams(order.getParams(), order.getId()));
    }

    private void printOrders(List<Order> orders) {
        orders.forEach(order -> console.printOrderInfo(order));
    }

    private void getParams(Map<NcAttribute, String> attributesMap, Integer objectId) {
        attributesMap.forEach((attr, value) -> {
            if (attr.getAttrTypeDef().getType().getId().equals(AttributeType.LIST.getId())) {
                new NcParamsServiceImpl().selectStringValue(objectId, attr.getId());
            }
            else if (attr.getAttrTypeDef().getType().getId().equals(AttributeType.REFERENCE.getId())) {
                new NcReferencesServiceImpl().selectReference(objectId, attr.getId());
            } else {
                new NcParamsServiceImpl().selectStringValue(objectId, attr.getId());
            }
        });
    }
}