package com.netcracker.ec.util;

import com.netcracker.ec.services.db.DbWorker;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IdGenerator {

    private IdGenerator() {

    }

    public static int generateId() throws SQLException {
        String sqlQuery = "select getNcId();";

        ResultSet resultSet = DbWorker.getInstance().executeSelectQuery(sqlQuery);
        resultSet.next();
        int id = resultSet.getInt(1);
        resultSet.close();
        return id;
    }
}
