package com.netcracker.ec.services.console;

import com.netcracker.ec.domain.enums.OperationType;
import com.netcracker.ec.model.db.NcAttrTypeDef;
import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.model.db.NcEntity;
import com.netcracker.ec.model.domain.enums.AttributeType;
import com.netcracker.ec.model.domain.order.Order;
import com.netcracker.ec.provisioning.operations.CreateOrderOperation;
import com.netcracker.ec.provisioning.operations.ExitOperation;
import com.netcracker.ec.provisioning.operations.Operation;
import com.netcracker.ec.provisioning.operations.ShowOrdersOperation;
import com.netcracker.ec.services.db.impl.NcListValueServiceImpl;
import com.netcracker.ec.services.db.impl.NcObjectServiceImpl;
import com.netcracker.ec.util.UserInput;
import com.netcracker.ec.view.Printer;

import java.util.Arrays;
import java.util.List;

public class Console {
    private static Console console = null;

    public static Console getInstance() {
        if (console == null) {
            console = new Console();
        }
        return console;
    }

    public static Operation getNextOperation() {
        Operation operation = null;
        printAvailableOperations();
        Printer.print("Enter operation: ");
        Integer operationId = UserInput.nextOperationId();
        OperationType operationType = OperationType.valueOf(operationId);

        if (operationType != null) {
            switch (operationType) {
                case CREATE_ORDER:
                    operation = new CreateOrderOperation();
                    break;
                case SHOW_ORDERS:
                    operation = new ShowOrdersOperation();
                    break;
                case EXIT:
                    operation = new ExitOperation();
                    break;
                default:
                    break;
            }
        } else {
            Printer.print("Enter right operation");
            operation = getNextOperation();
        }
        return operation;
    }

    private static void printAvailableOperations() {
        System.out.println("  Operations:");
        Arrays.stream(OperationType.values()).forEach(System.out::println);
    }

    public String getAttributeValue(NcAttribute attr) {
        Printer.print(attr.getName() + ": ");

        NcAttrTypeDef ncAttrTypeDef = attr.getAttrTypeDef();

        if (AttributeType.LIST == ncAttrTypeDef.getType()) {
            List<NcEntity> ncEntities =
                    new NcListValueServiceImpl().getNcListValuesAsEntitiesByNcAttrTypeDefId(ncAttrTypeDef.getId());
            printNcEntity(ncEntities);
        }
        if (AttributeType.REFERENCE == ncAttrTypeDef.getType()) {
            List<NcEntity> ncEntities =
                    new NcObjectServiceImpl().getNcObjectsAsEntitiesByObjectTypeId(10);
            printNcEntity(ncEntities);
        }
        return UserInput.inputString("");
    }

    private void printNcEntity(List<NcEntity> ncEntities){
        for (NcEntity ncEntity : ncEntities) {
            Printer.print(ncEntity.toFormattedOutput());
        }
    }

    public void printOrderInfo(Order order) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Order name: ").
                append(order.getName()).
                append("\n");
        order.getParams().forEach((key, value) ->
                stringBuilder.append("  ")
                        .append(key.getName())
                        .append(": ")
                        .append(value)
                        .append("\n"));
        Printer.print(stringBuilder.toString());
    }

    public boolean getSaveDialogueAnswer() {
        Printer.print("Save order?[Y/N]");
        return UserInput.getSaveDialogueAnswer();
    }

    public void close() {
        Printer.print("Exit in process...");
        UserInput.close();
    }
}