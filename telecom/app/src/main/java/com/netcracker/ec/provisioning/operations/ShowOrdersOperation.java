package com.netcracker.ec.provisioning.operations;

import com.netcracker.ec.common.TelecomConstants;
import com.netcracker.ec.model.db.*;
import com.netcracker.ec.model.domain.enums.AttributeType;
import com.netcracker.ec.model.domain.order.NewOrder;
import com.netcracker.ec.model.domain.order.Order;
import com.netcracker.ec.services.console.Console;
import com.netcracker.ec.services.db.impl.*;
import com.netcracker.ec.util.UserInput;
import com.netcracker.ec.view.Printer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShowOrdersOperation implements Operation {

    private final Console console = Console.getInstance();

    public ShowOrdersOperation() {
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
        List<NcObject> objects = new NcObjectServiceImpl().getNcObjectsByParentId(TelecomConstants.ABSTRACT_ORDER_PARENT_ID);

        printOrders(objects);
    }

    private void showOrderOfASpecificObjectType() {
        Printer.print("Please Select Object Type.");

        List<NcObjectType> objectTypes = new NcObjectTypeServiceImpl().getObjectTypesByParentId(NewOrder.OBJECT_TYPE);
        objectTypes.forEach(objectType -> Printer.print(objectType.toFormattedOutput()));
        Integer objectTypeId = UserInput.nextOperationId();

        List<NcObject> objects = new NcObjectServiceImpl().getNcObjectsByObjectTypeId(objectTypeId);

        printOrders(objects);
    }

    private void printOrders(List<NcObject> objects){
        for (NcObject object :objects) {
            Printer.print(object.toFormattedOutput());
            Set<NcAttribute> attributes =
                    new NcAttributeServiceImpl().getAttributesByObjectType(object.getObjectType().getId());
            for (NcAttribute attribute : attributes) {
                Printer.print("\t" + attribute.getName() + ": " + getParams(attribute, object));
            }
            Printer.print("\n");
        }
    }

    private String getParams(NcAttribute attr, NcObject object) {
        if (AttributeType.LIST == attr.getAttrTypeDef().getType()) {
            Integer list_value_id = object.getListValueId(attr.getId());
            return new NcListValueServiceImpl().getNcListValueByNcListValueId(list_value_id);
        } else if (AttributeType.REFERENCE == attr.getAttrTypeDef().getType()) {
            Integer referenceId = object.getReferenceId(attr.getId());
            return new NcObjectServiceImpl().getNameByID(referenceId);
        } else {
            return object.getStringValue(attr.getId());
        }
    }
}