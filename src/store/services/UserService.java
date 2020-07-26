package store.services;

import store.domain.User;

import java.sql.SQLException;

public interface UserService {
    public void userRegist(User user) throws SQLException;

    public  boolean userActive(String code) throws SQLException;

    User userLogin(User user) throws SQLException;

    User findUserByUid(String uid) throws SQLException;

    boolean checkUsername(String username) throws SQLException;
}
