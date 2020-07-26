package store.web.servlet;

import net.sf.json.JSONArray;
import store.domain.Category;
import store.services.CategoryService;
import store.services.serviceimp.CategoryServiceImp;
import store.web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CategoryServlet extends BaseServlet {

    public String findAllCats(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        //1.调用serviceImp  里面的方法获取数据库里面的category 这张表里面的数据
        //这个方法我们写过不用再重新写 只要调用就行了

        CategoryService categoryService = new CategoryServiceImp();
        List<Category> list = categoryService.getAllCates();
        //现在、我们就是引入第三方的jar文件 将我们的集合变成我们要的json格式的类型 jar文件已经准备好了 就在常用jar文件里面
        //我们将其jar导入到项目里面  调用里面的方法将其变成我们要的字符串
        String jsonStr = JSONArray.fromObject(list).toString();
        //System.out.println(jsonStr);
        /*将我们的分类信息响应会客户端 但是此时我们是json格式 的 所以我们要改变一下响应头 这样浏览器在接收的时候就知道这是json数据
        * 就知道用什么格式去接收*/
        resp.setContentType("application/json;charset=utf-8");
        /*我们在这里已经通过浏览器里面的调试工具看到了我们想要的数据  现在我们只要将其在header.jsp里面进行接收就行了*/
        //一定要发过去 不然客户端接受不了信息
        resp.getWriter().print(jsonStr);
        return null;

    }

}
