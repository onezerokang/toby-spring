package kang.onezero.tobyspring.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class NUserDao extends UserDao{
    public Connection getConnection() throws SQLException {
        // N사의 connection 생성 코드(임시로 null을 반환)
        return null;
    }
}
