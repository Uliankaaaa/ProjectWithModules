package com.netcracker.ec.services.console;

import com.netcracker.ec.model.db.*;
import com.netcracker.ec.model.domain.enums.AttributeType;
import com.netcracker.ec.model.domain.enums.OperationType;
import com.netcracker.ec.provisioning.operations.ExitOperation;
import com.netcracker.ec.provisioning.operations.Operation;
import com.netcracker.ec.provisioning.operations.ShowOrdersOperation;
import com.netcracker.ec.services.db.impl.NcListValueServiceImpl;
import com.netcracker.ec.services.db.impl.NcObjectService;
import com.netcracker.ec.services.db.impl.NcObjectTypeService;
import com.netcracker.ec.services.db.impl.NcReferencesServiceImpl;
import com.netcracker.ec.util.UserInput;
import com.netcracker.ec.view.Printer;
import com.netcracker.ec.provisioning.operations.CreateOrderOperation;
import com.netcracker.ec.model.domain.order.Order;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        String operationId = UserInput.inputString("Enter operation: ");

        OperationType operationType = OperationType.getOperationById(operationId);

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

    public static void printAvailableOperations() {
        System.out.println("  Operations:");
        Arrays.stream(OperationType.values()).forEach(System.out::println);
    }

    public String getAttributeValue(NcAttribute attr) {
        Printer.print(attr.getName() + ": ");
        if(attr.getName().equals("Order Status")){
            Printer.print("Entering");
            return "Entering";
        }
        if(attr.getName().equals("Order Aim")){
            Printer.print("New");
            return "New";
        }
        if(attr.getAttrTypeDef().getType().equals(AttributeType.LIST.getId())){
            List<NcEntity> ncEntities = new NcListValueServiceImpl().getNcListValuesAsEntitiesByNcAttrTypeDefId(attr.getAttrTypeDef().getId());
            for (NcEntity ncEntity: ncEntities) {
               Printer.print(ncEntity.getName() + ": " + ncEntity.getId());
            }
        }
    /*    if (attr.getAttrTypeDef().getType().equals(AttributeType.REFERENCE.getId())){
            Integer reference = attr.getReferenceId(attr.getId());
            Printer.print(reference.toString());
        }*/
        return UserInput.inputString("");
    }

    public void printOrderInfo(Order order) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Order name: ").
                append(order.getName()).
                append("\n");
        order.getParameters().forEach((key, value) ->
                stringBuilder.append("  ")
                        .append(key.getName())
                        .append(": ")
                        .append(value)
                        .append("\n"));
        Printer.print(stringBuilder.toString());
    }

    public void printAvailableOperations(Map<Integer, String> operationsMap) {
        operationsMap.forEach((key, value) -> System.out.println(key + " - " + value));
    }


    public boolean getSaveDialogueAnswer() {
        Printer.print("Save order?[Y/N]");
        return UserInput.getSaveDialogueAnswer();
    }

    public void close() {
        Printer.print("Exit in process...");
        UserInput.close();
    }

    public Integer nextAvailableOperation(Set<Integer> availableOperationsSet) {
        Integer id = 0;
        while (!availableOperationsSet.contains(id)){
            id = UserInput.nextOperationId();
        }
        return id;
    }
}