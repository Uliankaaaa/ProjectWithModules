package com.netcracker.ec.provisioning.operations;

import com.netcracker.ec.common.TelecomConstants;
import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.model.db.NcObject;
import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.model.domain.enums.AttributeType;
import com.netcracker.ec.model.domain.order.DisconnectOrder;
import com.netcracker.ec.model.domain.order.NewOrder;
import com.netcracker.ec.model.domain.order.Order;
import com.netcracker.ec.services.db.impl.NcAttributeServiceImpl;
import com.netcracker.ec.services.db.impl.NcListValueServiceImpl;
import com.netcracker.ec.services.db.impl.NcObjectServiceImpl;
import com.netcracker.ec.services.db.impl.NcObjectTypeServiceImpl;
import com.netcracker.ec.util.UserInput;
import com.netcracker.ec.view.Printer;

import java.util.List;
import java.util.Set;

public class ShowOrdersOperation implements Operation {
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
        List<NcObject> objects = new NcObjectServiceImpl().getNcObjectsByParentId(Order.OBJECT_TYPE);

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
        AttributeType attrTypeDef = attr.getAttrTypeDef().getType();

        switch (attrTypeDef) {
            case LIST:
                Integer listValueId = object.getListValueId(attr.getId());
                return new NcListValueServiceImpl().getListValueByListValueId(listValueId);
            case REFERENCE:
                Integer referenceId = object.getReferenceId(attr.getId());
                return new NcObjectServiceImpl().getObjectNameByID(referenceId);
            default:
                return object.getStringValue(attr.getId());
        }
    }
}