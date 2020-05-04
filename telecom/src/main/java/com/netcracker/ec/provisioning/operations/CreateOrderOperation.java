package com.netcracker.ec.provisioning.operations;

import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.services.console.Console;
import com.netcracker.ec.services.db.impl.*;
import com.netcracker.ec.services.db.DbWorker;
import com.netcracker.ec.view.Printer;
import com.netcracker.ec.model.domain.order.Order;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreateOrderOperation implements Operation {
    private final NcObjectTypeServiceLast ncObjectTypeServiceLast;
    private final NcAttrService ncAttributeService;
    private final NcObjectService ncObjectService;
    private final NcParamsService ncParamsService;

    private Console console = Console.getInstance();

    public CreateOrderOperation() {
        this.ncObjectTypeServiceLast = new NcObjectTypeServiceLast();
        this.ncAttributeService = new NcAttrService();
        this.ncObjectService = new NcObjectService();
        this.ncParamsService = new NcParamsService();
    }

    @Override
    public void execute() {
        Printer.print("Please Select Object Type.");

        Map<Integer, String> orderObjectTypeMap = ncObjectTypeServiceLast.getOrdersObjectTypeNameMap();
        console.printAvailableOperations(orderObjectTypeMap);


        Integer objectTypeId = console.nextAvailableOperation(orderObjectTypeMap.keySet());
        List<NcAttribute> attributeList = ncAttributeService.getAttributesByOrderType(objectTypeId);
      //  Set<NcAttribute> attributeList = new NcAttributeServiceImpl().getAttributesByObjectType(objectTypeId);

        NcObjectType selectedObjectType = new NcObjectTypeServiceImpl().getNcObjectTypeById(objectTypeId);

            Order order = new Order(selectedObjectType);
            int id = DbWorker.getInstance().generateId();
            order.setId(id);
            order.setName(getOrderName(selectedObjectType, id));
            attributeList.forEach(attr -> order.getParameters()
                    .put(attr, console.getAttributeValue(attr)));

            if (console.getSaveDialogueAnswer()) {
                addOrder(order);
                addOrderParams(order);
                console.printOrderInfo(order);
            }
    }

    private void addOrder(Order order) {
        new NcObjectServiceImpl().insert(order);
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
