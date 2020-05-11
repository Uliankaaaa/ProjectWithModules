package com.netcracker.ec.services.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySqlConnection {
    private String url;

    public MySqlConnection() {
        this.url = "jdbc:mysql://localhost:3306/opf?useUnicode=true&serverTimezone=UTC&useSSL=false";
    }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, "root", "uliana");
    }
}
