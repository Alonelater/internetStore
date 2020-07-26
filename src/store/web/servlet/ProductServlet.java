package store.web.servlet;

import store.domain.PageModel;
import store.domain.Product;
import store.services.ProductService;
import store.services.serviceimp.ProductServiceImp;
import store.web.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ProductServlet extends BaseServlet {

    ProductService productService = new ProductServiceImp();

    /*查询所有商品的详细信息  就是当你点击某件热门或者最新的商品的时候 我们就获取了你点击的商品里面携带的信息 将其调用底层的工具类 将其找到的信息封装成实体类传递上来*/
    public String findProductByPid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String pid = req.getParameter("pid");

        Product product = productService.findProductByPid(pid);
        req.setAttribute("product",product);
        return "/jsp/product_info.jsp";
    }

    /*查询所有分类模块里面的分页的详细信息  就是当你点击某个模块的的时候 我们就获取了你点击的模块栏目里面携带的信息  里面有方法名  还有你点击的模块属于什么类别 此时
    * 我们就通过操作数据库得到所有的记录 并且按照指定格式封装成集合 将其存到request域中  循环遍历就行了*/
    public String findProductsByCidWithPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {

        //我们在这里要做的几件事
        //获取cid num
        String cid = req.getParameter("cid");
        //因为我们需要数字类型的数据用来表示我们的分页下标页数 所以我们将字符串转成数字
       int num = Integer.parseInt(req.getParameter("num"));
        //调用业务层的功能  以分页的形式将当前类别的下的信息
        //返回pageModel对象

        PageModel pm = productService.findProductsByCidWithPage(cid,num);
        //将获取得pageModel对象里面的信息存放至request
        req.setAttribute("page",pm);
        //最后转发至
        return "/jsp/product_list.jsp";
    }
}
