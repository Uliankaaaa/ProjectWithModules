package com.netcracker.ec.provisioning.operations;

import com.netcracker.ec.common.TelecomConstants;
import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.model.domain.order.NewOrder;
import com.netcracker.ec.model.domain.order.Order;
import com.netcracker.ec.services.console.Console;
import com.netcracker.ec.services.db.impl.NcAttributeServiceImpl;
import com.netcracker.ec.services.db.impl.NcObjectTypeServiceImpl;
import com.netcracker.ec.util.UserInput;
import com.netcracker.ec.view.Printer;

import java.util.List;
import java.util.Set;

public class CreateOrderOperation implements Operation {
    private Console console = Console.getInstance();

    @Override
    public void execute() {
        Printer.print("Please Select Object Type.");

        List<NcObjectType> objectTypes = new NcObjectTypeServiceImpl().getObjectTypesByParentId(NewOrder.OBJECT_TYPE);
        objectTypes.forEach(objectType -> Printer.print(objectType.toFormattedOutput()));
        Integer objectTypeId = UserInput.nextOperationId();
        NcObjectType ncObjectType = getNcObjectType(objectTypes, objectTypeId);

        if (ncObjectType != null) {
            Order order = new NewOrder(ncObjectType);

            getAttributesByObjectTypeId(objectTypeId)
                    .forEach(attr -> order.setParam(attr, console.getAttributeValue(attr)));

            if (console.getSaveDialogueAnswer()) {
                order.save();
                console.printOrderInfo(order);
            }
        }
    }

    private NcObjectType getNcObjectType(List<NcObjectType> objectTypes, Integer objectTypeId) {
        return objectTypes.stream()
                .filter(objectType -> objectType.getId().equals(objectTypeId))
                .findFirst()
                .orElse(null);
    }

    private Set<NcAttribute> getAttributesByObjectTypeId(Integer objectTypeId) {
        return new NcAttributeServiceImpl().getAttributesByObjectTypeAndAttrSchema(objectTypeId,
                TelecomConstants.TELECOM_OM_SHEMA_ID);
    }
}
