package com.netcracker.ec.provisioning.operations;

import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.model.db.NcListValue;
import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.services.console.Console;
import com.netcracker.ec.services.db.impl.NcAttrService;
import com.netcracker.ec.services.db.impl.NcObjectService;
import com.netcracker.ec.services.db.impl.NcObjectTypeService;
import com.netcracker.ec.services.db.impl.NcParamsService;
import com.netcracker.ec.util.IdGenerator;
import com.netcracker.ec.view.Printer;
import com.netcracker.ec.model.domain.order.Order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CreateOrderOperation implements Operation {
    private final NcObjectTypeService ncObjectTypeService;
    private final NcAttrService ncAttributeService;
    private final NcObjectService ncObjectService;
    private final NcParamsService ncParamsService;

    private Console console = Console.getInstance();

    public CreateOrderOperation() {
        this.ncObjectTypeService = new NcObjectTypeService();
        this.ncAttributeService = new NcAttrService();
        this.ncObjectService = new NcObjectService();
        this.ncParamsService = new NcParamsService();
    }

    @Override
    public void execute() {
        Printer.print("Please Select Object Type.");

        Map<Integer, String> orderObjectTypeMap = ncObjectTypeService.getOrdersObjectTypeNameMap();
        console.printAvailableOperations(orderObjectTypeMap);


        Integer objectTypeId = console.nextAvailableOperation(orderObjectTypeMap.keySet());
        List<NcAttribute> attributeList = ncAttributeService.getAttributesByOrderType(objectTypeId);

        NcObjectType selectedObjectType = ncObjectTypeService.getObjectTypeById(objectTypeId);

        try {
            Order order = new Order(selectedObjectType);
            int id = IdGenerator.generateId();
            order.setId(id);
            order.setName(getOrderName(selectedObjectType, id));
            attributeList.forEach(attr -> order.getParameters()
                    .put(attr, console.getAttributeValue(attr)));

            if (console.getSaveDialogueAnswer()) {
                addOrder(order);
                addOrderParams(order);
                console.printOrderInfo(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addOrder(Order order) {
        ncObjectService.insertOrder(order);
    }

    private void addOrderParams(Order order) {
        ncParamsService.insertParams(order.getParameters(), order.getId());
    }

    private String getOrderName(NcObjectType objectType, int id) {
        String objectTypeName = objectType.getName();
        String name[] = objectTypeName.split(" ");
        return name[0] + " " + name[1] + " #" + id;
    }
}
