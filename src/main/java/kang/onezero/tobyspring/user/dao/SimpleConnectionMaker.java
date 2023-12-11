package kang.onezero.tobyspring.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {

    public Connection makeNewConnection() throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/toby_spring", "root", "1234");
        return c;
    }
}
