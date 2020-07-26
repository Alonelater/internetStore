package store.web.servlet;

import store.domain.Product;
import store.services.ProductService;
import store.services.serviceimp.ProductServiceImp;
import store.web.base.BaseServlet;


import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class IndexServlet extends BaseServlet {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {


      /*
         //我们要差创建一个List集合 将返回的结果存在里面将信息 转发给request  不要存在session里面 会给服务器带来压力
        //  只要你访问我  我就去数据库里面读取信息 将这个信息发送给你
        //
        //调用业务层获取全部分类数据
        CategoryService categoryService = new CategoryServiceImp();
        try {
            List<Category> list = categoryService.getAllCates();
            //将全部分类信息放入request
            req.setAttribute("allCategory",list);
            //转发到真实的首页/jsp/index.jsp
            return "/jsp/index.jsp";
        }catch (Exception e){
            req.setAttribute("msg","网络延迟问题，请重新刷新加载");
            return "/jsp/info.jsp";

        }*/

        //上面这些代码我们已经用了更好的方式去完成了 那就是ajax
        // 下面我们现在要在这里完成最新最热的商品展示

        ProductService productService = new ProductServiceImp();
        List<Product> list =  productService.findNews();
        List<Product> list1 = productService.findHots();

        req.setAttribute("newProduct",list);
        req.setAttribute("hotProduct",list1);
        return "/jsp/index.jsp";
    }
}