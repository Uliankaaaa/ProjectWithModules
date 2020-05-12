package com.netcracker.ec.provisioning.operations;

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
        List<NcEntity> entities = new NcObjectServiceImpl().getNcObjectsAsEntities();

        printOrders(entities);
    }

    private void showOrderOfASpecificObjectType() {
        Printer.print("Please Select Object Type.");

        List<NcObjectType> objectTypes = new NcObjectTypeServiceImpl().getObjectTypesByParentId(NewOrder.OBJECT_TYPE);
        objectTypes.forEach(objectType -> Printer.print(objectType.toFormattedOutput()));
        Integer objectTypeId = UserInput.nextOperationId();

        List<NcEntity> entities = new NcObjectServiceImpl().getNcObjectsAsEntitiesByObjectTypeId(objectTypeId);

        printOrders(entities);

    }

    private void printOrders(List<NcEntity> entities){
        for (NcEntity entity : entities) {
            Printer.print(entity.toFormattedOutput());
            Set<NcAttribute> attributes =
                    new NcAttributeServiceImpl().getAttributesByObjectTypeParentIdAndObjectId(NewOrder.OBJECT_TYPE, entity.getId());

            for (NcAttribute attribute : attributes) {
                Printer.print("\t" + attribute.getName() + ": " + getParams(attribute, entity.getId()));
            }
            Printer.print("\n");
        }
    }

    private String getParams(NcAttribute attr, Integer objectId) {
        if (AttributeType.LIST == attr.getAttrTypeDef().getType()) {
            Integer list_value_id = new NcParamsServiceImpl().selectListValueId(objectId, attr.getId());
            return new NcListValueServiceImpl().getNcListValueByNcListValueId(list_value_id);
        } else if (AttributeType.REFERENCE == attr.getAttrTypeDef().getType()) {
             return "" + new NcReferencesServiceImpl().selectReference(objectId, attr.getId());
        } else {
            return new NcParamsServiceImpl().selectStringValue(objectId, attr.getId());
        }
    }
}