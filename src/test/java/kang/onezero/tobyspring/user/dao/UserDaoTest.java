package kang.onezero.tobyspring.user.dao;

import kang.onezero.tobyspring.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    @Test
    public void addAndGet() throws SQLException {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);
        User user1 = new User("faker", "이상혁", "1234");
        User user2 = new User("gumayusi", "이민형", "5678");

        dao.deleteAll();
        assertEquals(dao.getCount(), 0); // getCount 테스트 1

        dao.add(user1);
        dao.add(user2);
        assertEquals(dao.getCount(), 2); // getCount 테스트2

        User userget1 = dao.get(user1.getId());
        assertEquals(userget1.getName(), user1.getName());
        assertEquals(userget1.getPassword(), user1.getPassword());

        User userget2 = dao.get(user2.getId());
        assertEquals(userget2.getName(), user2.getName());
        assertEquals(userget2.getPassword(), user2.getPassword());
    }

    @Test
    public void getUserFailure() throws SQLException {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);
        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            dao.get("unknown_id");
        });
    }


    @Test
    public void count() throws SQLException {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);
        User user1 = new User("faker", "이상혁", "1234");
        User user2 = new User("zeus", "최우제", "5678");
        User user3 = new User("keria", "류민석", "9012");

        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        dao.add(user1);
        assertEquals(dao.getCount(), 1);

        dao.add(user2);
        assertEquals(dao.getCount(), 2);

        dao.add(user3);
        assertEquals(dao.getCount(), 3);
    }
}