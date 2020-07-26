package store.web.servlet;
import net.sf.json.JSONArray;
import store.domain.Order;
import store.domain.PageModel;
import store.services.OrderService;
import store.services.serviceimp.OrderServiceImp;
import store.web.base.BaseServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class AdminOrderServlet extends BaseServlet {
    OrderService orderService = new OrderServiceImp();
    // 默认方法
    public String findAllOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException, InvocationTargetException, IllegalAccessException {
        int num = Integer.parseInt(req.getParameter("num"));
        int  state = Integer.parseInt(req.getParameter("state"));

        PageModel pageModel =null;
        if (state!=1&&state!=2&&state!=3&&state!=4){
            pageModel = orderService.findAllOrders(num);
        }else {
             pageModel = orderService.findAllOrders(num,state);
        }
        req.setAttribute("page",pageModel);
        return "/admin/order/list.jsp";
    }




    public String findOrderByIdWithAjax(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException, InvocationTargetException, IllegalAccessException {

        //获取服务端的传来的订单oid
        String oid = req.getParameter("oid");
        //根据订单编号找到相应的订单下的订单项
        Order orderByOid = orderService.findOrderByOid(oid);
        //调用想用的api得到相应的json数据的字符串
        JSONArray jsonStr = JSONArray.fromObject(orderByOid.getList().toArray());
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(jsonStr);
        return null;
    }


}