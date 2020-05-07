package com.netcracker.ec.provisioning.operations;

import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.model.domain.enums.AttributeType;
import com.netcracker.ec.services.console.Console;
import com.netcracker.ec.util.UserInput;
import com.netcracker.ec.view.Printer;
import com.netcracker.ec.services.db.impl.*;
import com.netcracker.ec.model.domain.order.Order;

import java.util.List;
import java.util.Map;

public class CreateOrderOperation implements Operation {

    private Console console = Console.getInstance();

    @Override
    public void execute() {
        Printer.print("Please Select Object Type.");

        List<NcObjectType> objectTypesList = new NcObjectTypeServiceImpl().getObjectTypesByParentId(2);
        objectTypesList.forEach(objectType -> Printer.print(objectType.toFormattedOutput()));
        Integer objectTypeId = UserInput.nextOperationId();

        List<NcAttribute> attributeList = new NcAttributeServiceImpl().getAttributesByOrderType(objectTypeId);

        NcObjectType selectedObjectType = new NcObjectTypeServiceImpl().getNcObjectTypeById(objectTypeId);

            Order order = new Order(selectedObjectType);
            order.setName(getOrderName(selectedObjectType, order.getId()));
            attributeList.forEach(attr -> order.getParams()
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
        insertParams(order.getParams(), order.getId());
    }

    private String getOrderName(NcObjectType objectType, int id) {
        String objectTypeName = objectType.getName();
        String name[] = objectTypeName.split(" ");
        return name[0] + " " + name[1] + " #" + id;
    }

    private void insertParams(Map<NcAttribute, String> attributesMap, Integer objectId) {
        attributesMap.forEach((attr, value) -> {
            if (attr.getAttrTypeDef().getType().getId().equals(AttributeType.LIST.getId())) {
                new NcParamsServiceImpl().mergeListValue(objectId, attr.getId(), Integer.parseInt(value));
            }
            else if (attr.getAttrTypeDef().getType().getId().equals(AttributeType.REFERENCE.getId())) {
                new NcReferencesServiceImpl().mergeReference(attr.getId(), objectId, objectId);
            } else {
                new NcParamsServiceImpl().mergeValue(objectId, attr.getId(), value);
            }
        });
    }
}
