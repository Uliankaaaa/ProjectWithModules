package com.netcracker.ec.provisioning.operations;

import com.netcracker.ec.util.UserInput;
import com.netcracker.ec.view.Printer;

public class ShowOrdersOperation implements Operation {

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

    }

    private void showOrderOfASpecificObjectType() {

    }


}