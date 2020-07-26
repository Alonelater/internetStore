package store.dao;

import store.domain.Order;
import store.domain.OrderItem;
import store.domain.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderDao {
    void saveOrder(Connection conn, Order order) throws SQLException;

    void saveOrderItem(Connection conn, OrderItem item) throws SQLException;

    int findTotalRecords(User user) throws SQLException;
    int findTotalRecords() throws SQLException;
    int findTotalRecords(int state) throws SQLException;

    List findMyOrdersWithPage(User user, int startIndex, int pageSize) throws SQLException, InvocationTargetException, IllegalAccessException;
    List findMyOrdersWithPage( int startIndex, int pageSize) throws SQLException, InvocationTargetException, IllegalAccessException;
    List findMyOrdersWithPage( int state,int startIndex, int pageSize) throws SQLException, InvocationTargetException, IllegalAccessException;

    Order findOrderByOid(String oid) throws SQLException, InvocationTargetException, IllegalAccessException;

    void updateOrder(Order order) throws SQLException;
}
