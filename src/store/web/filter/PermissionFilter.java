package store.web.filter;

import jdk.nashorn.internal.ir.RuntimeNode;
import store.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class PermissionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        /*我们规定只要所有访问的内部页面都要进去这里进行判断  没有登录都不准访问*/

        /*首先进行强转  因为只有httpServletRequest里面才有getSession方法 */
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        User user = (User) httpServletRequest.getSession().getAttribute("userLogin");

        if (user!=null){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            servletRequest.setAttribute("msg","请登录后再访问相应页面");
            servletRequest.getRequestDispatcher("/jsp/info.jsp").forward(servletRequest,servletResponse);
        }

    }

    @Override
    public void destroy() {

    }
}
