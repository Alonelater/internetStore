package store.dao.daoimp;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import store.dao.UserDao;
import store.domain.User;
import store.utils.JDBCUtils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class UserDaoImp implements UserDao {

    QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());


    @Override
    public void userRegist(User user) throws SQLException {
        //在这里我们进行将前台传过来的User对象数据的插入
        String sql = "INSERT INTO `user` VALUES(?,?,?,?,?,?,?,?,?,?)";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthday = simpleDateFormat.format(user.getBirthday());
        Object[] params = {user.getUid(), user.getUsername(), user.getPassword(), user.getName(), user.getEmail(), user.getTelephone(), birthday, user.getSex(), user.getState(), user.getCode()};
        queryRunner.update(sql, params);

    }

    @Override
    public User userActive(String code) throws SQLException {
        String sql = "select * from user where code = ?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        User user = queryRunner.query(sql, new BeanHandler<User>(User.class), code);
        return user;
    }

    @Override
    public void updateUser(User user) throws SQLException {
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "update user set username=?,password=?,name=?,email=?,telephone=?,birthday=?,sex=?,state=?,code=? where uid=?";
        Object[] params = {user.getUsername(), user.getPassword(), user.getName(), user.getEmail(), user.getTelephone(), user.getBirthday(), user.getSex(), user.getState(), user.getCode(), user.getUid()};
        queryRunner.update(sql, params);
    }

    @Override
    public User userLogin(User user) throws SQLException {
        String sql = "select * from user where username=? and password=?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        User user1 = null;
        user1 = queryRunner.query(sql, new BeanHandler<User>(User.class), user.getUsername(), user.getPassword());
        return user1;
    }

    @Override
    public User fiindUserByUid(String uid) throws SQLException {
        String sql = "select * from user where uid=? ";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        User user1 = null;
        user1 = queryRunner.query(sql, new BeanHandler<User>(User.class),uid);
        return user1;

    }

    @Override
    public boolean checkUsername(String username) throws SQLException {
        String sql = "select count(*) from user where username=? ";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        Long result = queryRunner.query(sql, new ScalarHandler<>(), username);
        return result>0;
    }
}
