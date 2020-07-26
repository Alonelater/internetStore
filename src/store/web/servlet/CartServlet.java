package store.web.servlet;

import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Product;
import store.services.ProductService;
import store.services.serviceimp.ProductServiceImp;
import store.web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class CartServlet extends BaseServlet {


    public String addCartItemToCart(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        //1.首先我们上来就先去取session里面的购物车 如果有的话就不用new一个cart对象了
        //如果没有就new一个
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            req.getSession().setAttribute("cart", cart);
        }
        //获取product_info.jsp传来的的input参数  一个是pid 一个是数量
        String pid = req.getParameter("pid");
        int num = Integer.parseInt(req.getParameter("quantity"));
        //通过pid找到那个商品
        ProductService productService = new ProductServiceImp();
        Product product = productService.findProductByPid(pid);
        //设置了购物项的参数
        CartItem cartItem = new CartItem();
        cartItem.setNum(num);
        cartItem.setProduct(product);
        //将得到的购物项加入购物车
        cart.addCartItemToCart(cartItem);
        //然后重定向到cart.jsp里面
        resp.sendRedirect(req.getContextPath() + "/jsp/cart.jsp");
        return null;

    }


    public String removeCartItem(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        //System.out.println("aaaaaaaaaaaa");
        String pid = req.getParameter("pid");
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        cart.removeCartItem(pid);
        resp.sendRedirect(req.getContextPath() + "/jsp/cart.jsp");
        return null;
    }

    public String clearCart(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        //获取购物车
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        cart.clearCart();
        resp.sendRedirect(req.getContextPath() + "/jsp/cart.jsp");
        return null;
    }
}