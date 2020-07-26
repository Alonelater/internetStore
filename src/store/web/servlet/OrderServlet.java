package store.web.servlet;

import com.sun.org.apache.xpath.internal.operations.Or;
import store.domain.*;
import store.services.OrderService;
import store.services.serviceimp.OrderServiceImp;
import store.utils.PaymentUtil;
import store.utils.UUIDUtils;
import store.web.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Date;

public class OrderServlet extends BaseServlet {

    OrderService orderService = new OrderServiceImp();

    /*保存订单 将订单信息保存到数据库里面的订单表和订单项表中*/
    public String saveOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        //1)确认用户登录状态  没有登录的话就跳转到提示界面
        User user = (User) req.getSession().getAttribute("userLogin");
        if (user == null) {
            req.setAttribute("msg", "您未登录，请您登录后再下单");
            return "/jsp/info.jsp";
        }
        //2)创建订单 为订单赋值 里面包含订单的id 下单时间 订单总金额 订单状态 地址 姓名 电话

        Cart cart = (Cart) req.getSession().getAttribute("cart");
        Order order = new Order();
        order.setOid(UUIDUtils.getId());
        Date date = new Date();
        order.setDatetime(date);
        order.setTotal(cart.getTotal());//订单总金额
        order.setState(1);
        /*这个是最重要的 我们之前就判断过 如果走到这里表示已经登录了该用户 所以我们创建的就是该用户的订单 所以将该用户传进去就行了*/
        order.setUser(user);

        //3)创建订单项  我们要一边遍历购物项一边创建订单项  并且将订单项赋值 将其放在相应的订单下面
        for (CartItem item : cart.getCartItems()) {
            //创建购物项
            OrderItem orderItem = new OrderItem();
            //商品
            orderItem.setItemid(UUIDUtils.getId());
            orderItem.setQuantity(item.getNum());
            orderItem.setTotal(item.getSubTotal());//订单项下面的小计
            orderItem.setProduct(item.getProduct());
            orderItem.setOrder(order);
            //将购物项加入到订单中
            order.getList().add(orderItem);

        }
        //4)调用业务层的功能  将订单保存

        /*这里是最巧妙的地方 订单里面有订单项 用户 还有自己的属性 比如总金额什么的 我们在下面操作数据库的时候就可以通过order里面的
         * 用户 订单项 到Dao层里面用不同的对象执行底层操作  这里提交订单 和订单项保存到到数据库 应该成功就全部成功 失败就全部失败
         * 不能只成功一部分的  所以我们设计其中的事务回滚 所以要开启事务就是在服务层的时候做的操作*/
        orderService.saveOrder(order);
        //5)清空购物车
        cart.clearCart();
        //6)将订单放在request 里面转发至相应的order_info.jsp
        req.setAttribute("order", order);
        return "/jsp/order_info.jsp";
    }


    /*点击我的订单 将信息以分页的形式展示出来*/
    public String findMyOrdersWithPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException, InvocationTargetException, IllegalAccessException {

        //获取用户信息
        ///因为我们已经出现了我的订单 证明该用户已经登录过了所以就不用判断用户是否登录;
        User user = (User) req.getSession().getAttribute("userLogin");
        //获取当前页 这个是我们传来的数据  就是num=1
        int currentNum = Integer.parseInt(req.getParameter("num"));
        //调用业务层功能:查询当前用户订单信息,返回PageModel

        //我们在底层实现的时候肯定是查询一个用户所有订单信息 所有我们就要将用户给传下去 用来数据库的查询语句 将当前页也传下去
        //select * from orders where uid=?  因为返回值是一个PageModel  所以我们
        PageModel pm = orderService.findMyOrdersWithPage(user, currentNum);
        //将PageModel放入request
        req.setAttribute("page", pm);
        //转发到/jsp/order_list.jsp
        return "/jsp/order_list.jsp";


    }

    /*通过id查找一个具体的订单  里面有具体的若干的订单项封装在订单里面*/
    public String findOrderByOid(HttpServletRequest req, HttpServletResponse resp) throws IllegalAccessException, SQLException, InvocationTargetException {

        //1)准备工作  修改链接将order_list.jsp 里面的连接修改成我们要的链接
        //2)获取相应的参数
        String oid = req.getParameter("oid");
        //3)创建项目的结构

        Order order = orderService.findOrderByOid(oid);
        //4)将order信息存到request域中
        req.setAttribute("order",order);
        //5)转发至order_info.jsp
        return "/jsp/order_info.jsp";
    }

    /*提交支付订单*/
    public String payOrder(HttpServletRequest req, HttpServletResponse resp) throws IllegalAccessException, SQLException, InvocationTargetException, IOException {

        //获取客户端传来的表单信息 姓名 电话 地址 oid 银行
        String name = req.getParameter("name");
        String telephone = req.getParameter("telephone");
        String address = req.getParameter("address");
        String oid = req.getParameter("oid");
        String pd_FrpId = req.getParameter("pd_FrpId");
        //更新数据库里面的订单里面的相关信息  将传来的信息更新进去数据库

        //通过订单号找到我们订单 只不过它里面没有我们的姓名电话地址什么的 我们就将他设置上去
        Order order = orderService.findOrderByOid(oid);
        order.setName(name);
        order.setAddress(address);
        order.setTelephone(telephone);
        orderService.updateOrder(order);
        //然后向支付接口发送参数

        // 把付款所需要的参数准备好:
        String p0_Cmd = "Buy";
        //商户编号
        String p1_MerId = "10001126856";
        //订单编号
        String p2_Order = oid;
        //金额
        String p3_Amt = "0.01";
        String p4_Cur = "CNY";
        String p5_Pid = "";
        String p6_Pcat = "";
        String p7_Pdesc = "";
        //接受响应参数的Servlet
        String p8_Url = "http://localhost:8080/"+req.getContextPath()+"/CallBackServlet?method=callBack";
        String p9_SAF = "";
        String pa_MP = "";
        String pr_NeedResponse = "1";
        //公司的秘钥
        String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";

        //调用易宝的加密算法,对所有数据进行加密,返回电子签名
        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);

        StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
        sb.append("p0_Cmd=").append(p0_Cmd).append("&");
        sb.append("p1_MerId=").append(p1_MerId).append("&");
        sb.append("p2_Order=").append(p2_Order).append("&");
        sb.append("p3_Amt=").append(p3_Amt).append("&");
        sb.append("p4_Cur=").append(p4_Cur).append("&");
        sb.append("p5_Pid=").append(p5_Pid).append("&");
        sb.append("p6_Pcat=").append(p6_Pcat).append("&");
        sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
        sb.append("p8_Url=").append(p8_Url).append("&");
        sb.append("p9_SAF=").append(p9_SAF).append("&");
        sb.append("pa_MP=").append(pa_MP).append("&");
        sb.append("pd_FrpId=").append(pd_FrpId).append("&");
        sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
        sb.append("hmac=").append(hmac);

        //System.out.println(sb.toString());
        // 使用重定向：
        resp.sendRedirect(sb.toString());
        return null;
    }


    public String callBack(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, SQLException, InvocationTargetException, IOException {
        //接收支付接口传回来的参数 经过计算进行比对 如果数据正确就更新订单的支付状态
        //接收易付宝的数据

        // 阅读支付结果参数说明
        // System.out.println("==============================================");
        String p1_MerId = request.getParameter("p1_MerId");
        String r0_Cmd = request.getParameter("r0_Cmd");
        String r1_Code = request.getParameter("r1_Code");
        String r2_TrxId = request.getParameter("r2_TrxId");
        String r3_Amt = request.getParameter("r3_Amt");
        String r4_Cur = request.getParameter("r4_Cur");
        String r5_Pid = request.getParameter("r5_Pid");
        String r6_Order = request.getParameter("r6_Order");
        String r7_Uid = request.getParameter("r7_Uid");
        String r8_MP = request.getParameter("r8_MP");
        String r9_BType = request.getParameter("r9_BType");
        String rb_BankId = request.getParameter("rb_BankId");
        String ro_BankOrderId = request.getParameter("ro_BankOrderId");
        String rp_PayDate = request.getParameter("rp_PayDate");
        String rq_CardNo = request.getParameter("rq_CardNo");
        String ru_Trxtime = request.getParameter("ru_Trxtime");

        // hmac
        String hmac = request.getParameter("hmac");
        // 利用本地密钥和加密算法 加密数据
        String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";

        // 验证请求来源和数据有效性 保证数据的合法性
        boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
                r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
                r8_MP, r9_BType, keyValue);
        if (isValid) {
            // 有效
            // 根据支付情况决定是否更新订单的支付状态
            if (r9_BType.equals("1")) {//表示支付成功 那就更新订单的状态
                // 浏览器重定向

                Order order = orderService.findOrderByOid(r6_Order);
                //将订单设置为已付款
                order.setState(2);
                orderService.updateOrder(order);
                //转发信息 并且将相关信息放入request域中 转发至jsp/info.jsp下面
                request.setAttribute("msg","支付成功！订单号：" + r6_Order + "金额：" + r3_Amt);
                return "jsp/info.jsp";
            } else if (r9_BType.equals("2")) {
                // 修改订单状态:
                // 服务器点对点，来自于易宝的通知
                System.out.println("收到易宝通知，修改订单状态！");//
                // 回复给易宝success，如果不回复，易宝会一直通知
                response.getWriter().print("success");
            }

        } else {
            throw new RuntimeException("数据被篡改！");
        }

       return null;

    }


}