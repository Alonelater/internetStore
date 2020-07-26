package store.services;

import store.domain.Order;
import store.domain.PageModel;
import store.domain.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface OrderService {
    void saveOrder(Order order) throws SQLException;

    PageModel findMyOrdersWithPage(User user, int currentNum) throws SQLException, InvocationTargetException, IllegalAccessException;

    Order findOrderByOid(String oid) throws IllegalAccessException, SQLException, InvocationTargetException;

    void updateOrder(Order order) throws SQLException;

    PageModel findAllOrders(int num) throws SQLException, InvocationTargetException, IllegalAccessException;
    PageModel findAllOrders(int num,int state) throws SQLException, InvocationTargetException, IllegalAccessException;
}
