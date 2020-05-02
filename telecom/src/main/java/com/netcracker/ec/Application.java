package com.netcracker.ec;

import com.netcracker.ec.services.console.Console;

public class Application {
    private static boolean isApplicationRunning = false;

        void run() {

        if (!isApplicationRunning) {
            isApplicationRunning = true;
        }

        while (isApplicationRunning) {
            Console.getNextOperation().execute();
        }
    }

    public static void setIsApplicationRunning(boolean isRunning) {
        isApplicationRunning = isRunning;
    }
}
