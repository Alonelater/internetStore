package store.services.serviceimp;

import store.dao.UserDao;
import store.dao.daoimp.UserDaoImp;
import store.domain.User;
import store.services.UserService;

import java.sql.SQLException;

public class UserServiceImp implements UserService {


    UserDao userDao = new UserDaoImp();

    @Override
    public void userRegist(User user) throws SQLException {

        userDao.userRegist(user);
    }

    @Override
    public boolean userActive(String code) throws SQLException {

        //相当于select * from user where code = ?
        User user = userDao.userActive(code);
        if (user!=null){
            //重新设置状态码  将激活码置空 更新数据库
            user.setState(1);
            user.setCode("null");
            //调用userDao里面的updateUser方法更新底层的数据库数据
            userDao.updateUser(user);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public User userLogin(User user) throws SQLException {

        //在这里我们也要理清楚逻辑问题 我们传递下去的参数是一个只有用户名和密码的User 对象  但是在数据库里面却是
        //将所有的查询的信息封装成一个对象传递给我们 所有为空代表没有查到 但是不代表用户状态已激活  所以我们要在通过传递上
        //来的User1 去判断是否激活码是激活的 没有的话抛出异常 如果正确密码登录并且激活就返回这个User1这个拥有完整信息的对象
        // 这样该User1对象就能向上传递 给上面接收放在session里面了
        User user1 = new User();
        user1=userDao.userLogin(user);
        if (user1==null){
            throw  new RuntimeException("用户名或密码错误");
        }else if (user1.getState()==0){
            throw  new RuntimeException("您未激活,请前往注册时的"+user1.getEmail()+"邮箱激活");
        }else {

            return user1;
        }
    }

    @Override
    public User findUserByUid(String uid) throws SQLException {
        return userDao.fiindUserByUid(uid);
    }

    @Override
    public boolean checkUsername(String username) throws SQLException {
        return userDao.checkUsername(username);
    }
}
