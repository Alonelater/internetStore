package store.services.serviceimp;

import store.dao.OrderDao;
import store.dao.daoimp.OrderDaoImp;
import store.domain.Order;
import store.domain.OrderItem;
import store.domain.PageModel;
import store.domain.User;
import store.services.OrderService;
import store.utils.JDBCUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderServiceImp implements OrderService {


    OrderDao orderDao = new OrderDaoImp();

    @Override
    public void saveOrder(Order order) throws SQLException {

        //步骤
        //1.开启事务 首先就得获取连接
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            //2.保存订单
            orderDao.saveOrder(conn,order);
            //3.保存订单项  就要遍历所有的订单项  将其每一个订单项下面的完整信息保存在订单项这张表里面
            for(OrderItem item:order.getList()){
                orderDao.saveOrderItem(conn,item);
            }
            //4.提交事务
            conn.commit();
        } catch (SQLException e) {
            //5.失败就回滚
            conn.rollback();
        }

    }

    /*将我的订单查询出来并且以分页的形式返回*/
    @Override
    public PageModel findMyOrdersWithPage(User user, int currentNum) throws SQLException, InvocationTargetException, IllegalAccessException {
        //首先我们是先创建一个PageModel  pageModel里面有几个属性需要设置  一个是代表当前页的 一个是当前页的展示数量 一个是需要展示的的数目
        // 目的 三个参数设置进去就是通过里面的参数帮我们计算分页数目 还有一些其他参数待会儿一一设置进去
        //1.属性关联  我们要要获取到我们要查找的用户的总订单数
        int totalRecords = orderDao.findTotalRecords(user);
        PageModel pm = new PageModel(currentNum,5,totalRecords);
        //2.关联集合  就是我们通过条件获得的一片数据
        //select * from orders where uid=? limit ?,?  从第几条开始  需要显示的记录数有几条
        List list = orderDao.findMyOrdersWithPage(user,pm.getStartIndex(),pm.getPageSize());
        pm.setList(list);
        //3.路径关联
        pm.setUrl("orderServlet?method=findMyOrdersWithPage");
        return pm;
    }

    @Override
    public Order findOrderByOid(String oid) throws IllegalAccessException, SQLException, InvocationTargetException {

        return orderDao.findOrderByOid(oid);
    }

    @Override
    public void updateOrder(Order order) throws SQLException {

        orderDao.updateOrder(order);
    }

    @Override
    public PageModel findAllOrders(int num) throws SQLException, InvocationTargetException, IllegalAccessException {
        //首先我们是先创建一个PageModel  pageModel里面有几个属性需要设置  一个是代表当前页的 一个是当前页的展示数量 一个是需要展示的的数目
        // 目的 三个参数设置进去就是通过里面的参数帮我们计算分页数目 还有一些其他参数待会儿一一设置进去

        //1.属性关联  我们要要获取到我们要查找的用户的总订单数
        int totalRecords = orderDao.findTotalRecords();
        PageModel pageModel = new PageModel(num,9,totalRecords);
        List<Order> list = orderDao.findMyOrdersWithPage(pageModel.getStartIndex(),pageModel.getPageSize());
        pageModel.setList(list);
        pageModel.setUrl("adminOrderServlet?method=findAllOrder");
        return pageModel;


    }

    @Override
    public PageModel findAllOrders(int num, int state) throws SQLException, InvocationTargetException, IllegalAccessException {
        //首先我们是先创建一个PageModel  pageModel里面有几个属性需要设置  一个是代表当前页的 一个是当前页的展示数量 一个是需要展示的的数目
        // 目的 三个参数设置进去就是通过里面的参数帮我们计算分页数目 还有一些其他参数待会儿一一设置进去

        //1.属性关联  我们要要获取到我们要查找的用户的总订单数
        int totalRecords = orderDao.findTotalRecords(state);
        PageModel pageModel = new PageModel(num,9,totalRecords);
        List<Order> list = orderDao.findMyOrdersWithPage(state,pageModel.getStartIndex(),pageModel.getPageSize());
        pageModel.setList(list);
        pageModel.setUrl("adminOrderServlet?method=findAllOrder");
        return pageModel;
    }


}
