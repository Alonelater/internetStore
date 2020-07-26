package store.web.servlet;

import jdk.nashorn.internal.ir.RuntimeNode;
import net.sf.json.JSONArray;
import store.domain.Category;
import store.services.CategoryService;
import store.services.ProductService;
import store.services.serviceimp.CategoryServiceImp;
import store.services.serviceimp.ProductServiceImp;
import store.utils.JDBCUtils;
import store.utils.UUIDUtils;
import store.web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AdminCategoryServlet extends BaseServlet {

    CategoryService categoryService = new CategoryServiceImp();

    public String getAllCats(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        //获取全部未下架的信息

        List<Category> allCats = categoryService.getAllCates();
        req.setAttribute("allCats",allCats);
        //放在request域中 然后转发到/admin/category/list.jsp
        return "/admin/category/list.jsp";
    }


    /*获得已经下架的商品分类信息*/
    public String getAllLowCats(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        //获取全部信息
        List<Category> allLowCats = categoryService.getAllLowCates();
        req.setAttribute("allCats",allLowCats);
        //放在request域中 然后转发到/admin/category/list.jsp
        return "/admin/category/lowlist.jsp";
    }

    /*恢复已经下架的商品分类信息*/
    public String restoreCategory(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        String cid = req.getParameter("cid");
        Category category = categoryService.findCateByCid(cid);
        category.setCstatus(0);
        ProductService productService = new ProductServiceImp();
        //增加事务
        Connection conn = JDBCUtils.getConnection();
        try {
            conn.setAutoCommit(false);
            categoryService.updateCategory(conn,category);
            productService.updateProductsByCid(conn,cid,category);
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
            conn.rollback();
        }
        resp.sendRedirect(req.getContextPath()+"/adminCategoryServlet?method=getAllCats");
        return null;
    }



    /*对list页面进行相应相应的跳转*/
    public String addCategoryUI(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        /*做个链接的跳转*/
        return  "admin/category/add.jsp";
    }



    public String addCategory(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        //获取添加的栏目名称
        String cname = req.getParameter("cname");
        String cid = UUIDUtils.getId();
        Category c = new Category(cid,cname);
        //调用方法添将整个商品添加到数据库加到数据库
        categoryService.addCategory(c);
        /*添加完商品后直接重定向或查询的adminCategoryServlet?method=getAllCats  然后重新查询分类信息*/
        resp.sendRedirect(req.getContextPath()+"/adminCategoryServlet?method=getAllCats");
        return null;
    }


    /*对编辑页面进行相应相应的回显*/
    public String editCategoryUI(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        String cid = req.getParameter("cid");

        Category category = categoryService.findCateByCid(cid);
        /*将获取的分类对象放进request域中 方便存取*/
        req.setAttribute("category",category);
        /*做个链接的跳转*/
        return  "/admin/category/edit.jsp";
    }
    /*对编辑页面进行修改后再次重定向到list界面*/
    public String editCategory(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        //获取修改的名称还有隐藏域的cid  封装成一个对象
        String cname = req.getParameter("cname");
        String cid = req.getParameter("cid");
        Category c = new Category(cid,cname);

        categoryService.editCategory(c);
        /*添加完商品后直接重定向或查询的adminCategoryServlet?method=getAllCats  然后重新查询分类信息*/
        resp.sendRedirect(req.getContextPath()+"/adminCategoryServlet?method=getAllCats");
        return null;
    }


    //deleteCategoryUI
    public String deleteCategory(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        String cid = req.getParameter("cid");

        ProductService productService = new ProductServiceImp();
        /*通过cid得到对应的商品分类 然后修改状态栏*/
        Category category = categoryService.findCateByCid(cid);
        category.setCstatus(1);
        //增加事务
        Connection conn = JDBCUtils.getConnection();
        try {
            conn.setAutoCommit(false);
            categoryService.updateCategory(conn,category);
            productService.updateProductsByCid(conn,cid,category);
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
            conn.rollback();
        }

        req.setAttribute("category",category);
        /*做个链接的跳转*/
        resp.sendRedirect(req.getContextPath()+"/adminCategoryServlet?method=getAllCats");
        return null;
    }

}
