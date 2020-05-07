package com.netcracker.ec.provisioning.operations;

import com.netcracker.ec.services.db.DbWorker;
import com.netcracker.ec.Application;
import com.netcracker.ec.services.console.Console;

public class ExitOperation implements Operation {
    @Override
    public void execute() {
        Console.getInstance().close();
        DbWorker.getInstance().close();
        Application.setIsApplicationRunning(false);
    }

}
