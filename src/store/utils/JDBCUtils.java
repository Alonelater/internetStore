package store.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtils {
    private  static ComboPooledDataSource dataSource = new ComboPooledDataSource();

    private  static  ThreadLocal<Connection> threadLocal = new ThreadLocal<>();


    /**
     *从线程中获取连接
     *
     */
    public  static  Connection getConnection() throws SQLException {
        //从线程中获取Connection
        Connection conn =threadLocal.get();
        if (conn==null){
            //如果来获取连接 但是是空的  那我们就去连接c3p0连接池去取
            conn=dataSource.getConnection();
            //得到后将其绑定到线程中
            threadLocal.set(conn);
        }
        return conn;

    }

    // 获取数据源
    public  static DataSource getDataSource(){
        return dataSource;
    }

    // 释放资源
    public static  void closeResource(Statement statement, ResultSet resultSet){
        closeResultSet(resultSet);
        closeStatement(statement);
    }
    // 释放 statement
    public static void closeStatement(Statement statement) {

        //如果不为空就关闭 否则为空就不用关闭了
        if (statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            statement = null;
        }

    }
    // 释放 ResultSet
    public static void closeResultSet(ResultSet resultSet) {

        //如果不为空就关闭  关闭完后将resultSet 置为空 让垃圾回收器回收 否则为空就不用关闭了
        if (resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            resultSet = null;
        }

    }


    //释放资源
    public  static  void  closeResource(Connection conn,Statement st,ResultSet rs){
        closeResource(st,rs);
        closeConn(conn);

    }

    public static void closeConn(Connection conn) {
        if (conn!=null){
            //关闭连接
            try {
                conn.close();
                //和线程解绑
                threadLocal.remove();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }



    //   开启事务
    public  static  void  startTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }

    /*
     * 事务提交并且释放连接
     * */
    public static  void  commitAndClose(){
        Connection conn = null;
        try {
            //获得连接 因为这个连接时从线程里面获取的 提交完事务就要解除绑定
            conn = getConnection();
            //提交事务
            conn.commit();
            //释放连接
            conn.close();
            //解除绑定
            threadLocal.remove();
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }


    /*
     * 事务出现异常 进行回滚并且释放资源
     * */


    public  static void  rollbackAnfClose(){
        Connection conn = null;
        try {
            //获得连接 因为这个连接时从线程里面获取的 提交完事务就要解除绑定
            conn= getConnection();
            //事务回滚
            conn.rollback();
            //关闭连接
            conn.close();
            //解除绑定
            threadLocal.remove();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
