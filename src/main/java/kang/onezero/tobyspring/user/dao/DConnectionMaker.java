package kang.onezero.tobyspring.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {
    @Override
    public Connection makeConnection() throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/toby_spring", "root", "1234");
        return c;
    }
}
