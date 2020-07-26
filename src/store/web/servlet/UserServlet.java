package store.web.servlet;


import store.domain.User;
import store.services.UserService;
import store.services.serviceimp.UserServiceImp;
import store.utils.MailUtils;
import store.utils.MyBeanUtils;
import store.utils.UUIDUtils;
import store.web.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import java.sql.SQLException;
import java.util.Map;


public class UserServlet extends BaseServlet {


    UserService userService = new UserServiceImp();

    /**
     * 注册页面地址的切换  不能让客户端点击注册 直接跳转到register.jsp  需要通过userServlet控制转发 方便管理
     */
    public String registerUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //当我们点击的时候注册的时候就会进入BaseServlet这个类去调用他的service里面的方法 然后在方法结束后返回的地址将会由BaseServlet里面统一转发
        // 并不是这样就能发到了/jsp/register.jsp这下面的注册界面了
        return "/jsp/register.jsp";
    }

    /**
     * 登录页面地址的切换  不能让客户端点击登录 直接跳转到login.jsp  需要通过userServlet控制转发 方便管理
     */
    public String loginUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        //当我们点击的时候注册的时候就会进入BaseServlet这个类去调用他的service里面的方法 然后在方法结束后返回的地址将会由BaseServlet里面统一转发
        // 并不是这样就能发到了/jsp/register.jsp这下面的注册界面了
        Cookie[] cookies = req.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("autoLogin")) {
                    String uid = cookie.getValue();
                    User user1 = userService.findUserByUid(uid);
                    req.getSession().setAttribute("userLogin", user1);
                    //但是我们一定要通过web下面的去重定向 因为那样可以帮我们获取最新最热  不然就会有bug了
                    resp.sendRedirect(req.getContextPath() + "/index.jsp");
                    return null;
                }
                if (cookie.getName().equals("rememberName")) {
                    String username = cookie.getValue();
                    req.setAttribute("username", username);
                }
            }

            return "/jsp/login.jsp";
        } else {
            return "/jsp/login.jsp";
        }
    }

    /*
     * 实现用户注册
     * */
    public String userRegist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, InvocationTargetException, IllegalAccessException {
        //由于表单项太多 我们不可能去每一个都用getParameter(String name)来获取 而且也不利于封装数据 难道还要将一个个得到的值用setUserName放进去
        //我们有一个工具类叫beanUtils 就是通过反射  找到map里面的集合所有的键名 然后匹配我们的实体类  获得实体类里面所有的set方法 如果名字键名是和实体类
        //里面的属性名一样 就将map里面的值封装进去
        Map<String, String[]> map = req.getParameterMap();
        //现在我们要将获得的数据将其封装进User对象 就要借助工具类beanUtils 里面的populate这个方法
        User user = new User();
        //BeanUtils.populate(user,map);
        //System.out.println(user);
        //程序写到这里就会出现错误 因为我们在注册界面里面有一个是是日期格式的时间  如果不处理的话beanUtils无法注入信息到User对象里面去
        //下面我们自己写了一个时间转换器的工具类 这个工具类也是beanUtils里面提供的转换器接口  详见utils包下的MyBeanUtils  所以我们将上面得代码注释了
        MyBeanUtils.populate(user, map);
        //System.out.println(user);
        user.setUid(UUIDUtils.getId());
        user.setState(0);
        user.setCode(UUIDUtils.getCode());
        //System.out.println(user);

        //new一个接口的实现类 返回值类型用接口去接收


        try {
            //注册成功我们将发送邮件给客户 并且返回注册成功信息到info.jsp页面
            userService.userRegist(user);
            //将注册界面的用户填写的邮箱传给工具类里面  和UUID里面的工具类获得的激活码传给发送邮件的工具类 并且通过邮件发送给用户
            MailUtils.sendMail(user.getEmail(), user.getCode());
            req.setAttribute("msg", "恭喜注册成功，请前往激活");
//            经过测试  上述功能已经全部实现 下面就登录界面的展示

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("msg", "注册失败，请重新注册");

        }
        return "/jsp/info.jsp";


        //现在复习 通过迭代器和其他方式获得map集合里面的内容  下面三种方式都要牢记于心  明白了获得是什么就能依次将其遍历出来
        //1.第一种方式
        /*for (String str :map.keySet()){
            System.out.println(str);
            for (String value : map.get(str))
            System.out.println(value);
        }*/


       /* //2.第二种方式
        for (Map.Entry<String,String[]> entry:map.entrySet()){
            System.out.println(entry.getKey());
            for (String str :entry.getValue()){
                System.out.println(str);
            }
        }
*/
    /*
       //第三种方式 采用迭代器的方式

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            //获取键
            String next = iterator.next();
            System.out.println(next);
            String[] str = map.get(next);
            for (String value : str){
                System.out.println(value);
            }
        }
        */


    }

    /*
     *
     * 实现激活码的激活*/
    public String active(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        //1.如果我们已经来到了这个方法表示我们的method已经被BaseServlet 通过反射得到了 我们只要获取参数code就行了
        String code = req.getParameter("code");
        //2.需要一个UserServiceImp对象帮我们做事

        boolean flag = userService.userActive(code);
        if (flag) {//表示激活成功  将激活成功的的那我们重定向到login.jsp页面
            req.getSession().setAttribute("msg", "激活成功，请登录");
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp");
            return null;
        } else {//表示没激活
            req.setAttribute("msg", "激活失败，请重新激活");
            return "/jsp/info.jsp";
        }
    }

    /*实现用户登录*/
    public String userLogin(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        //创建一个user对象 方便们将获取的表单数据封装到改User对象里面
        User user = new User();
        Map<String, String[]> map = req.getParameterMap();
        //使用工具类将我们的数据封装成对象
        MyBeanUtils.populate(user, map);
        //System.out.println(user);

        String autoLogin = req.getParameter("autoLogin");
        String rememberName = req.getParameter("rememberName");
        //再创建一个空的User对象  用来接收登录信息的用户值
        User user1 = null;
        try {
            user1 = userService.userLogin(user);
            //如果来到了这里  说明下面的没有异常 证明有该用户 并且状态码是激活的  我们就将信息存到session域中
            //如果他选择了自动登录 那我们就为它设置cookie并且响应回客户端
            if (autoLogin != null) {
                Cookie cookie = new Cookie("autoLogin", user1.getUid());
                cookie.setMaxAge(Integer.MAX_VALUE);
                resp.addCookie(cookie);
            }
            if (rememberName != null) {
                Cookie cookie1 = new Cookie("rememberName", user1.getUsername());
                cookie1.setMaxAge(Integer.MAX_VALUE);
                resp.addCookie(cookie1);
            }
            req.getSession().setAttribute("userLogin", user1);
            //但是我们一定要通过web下面的去重定向 因为那样可以帮我们获取最新最热  不然就会有bug了
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return null;

        } catch (Exception e) {

            //来到了这里说明登录有异常 由于我们在底层通过if else语句判断了到底是什么原因导致登录失败
            //我们采用的是通过抛出异常来传递数据 所以我们要在这里接收异常信息 将其设置在request里面
            //最后将其转发回当前的login页面
            String msg = e.getMessage();
            req.setAttribute("msg", msg);
            return "/jsp/login.jsp";

        }

    }

    /*实现用户退出登录*/
    public String loginOut(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        //让session失效
        User user = (User) req.getSession().getAttribute("userLogin");
        req.getSession().invalidate();
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("autoLogin")) {
                    Cookie cookie1 = new Cookie("autoLogin", user.getUid());
                    cookie1.setMaxAge(-1);
                    resp.addCookie(cookie1);
                }
            }
        }

        //重定向到首页  但是我们一定要通过web下面的去重定向 因为那样可以帮我们获取最新最热  不然就会有bug了
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
        return null;
    }


    /*实现检验用户名是否被使用*/
    public String ajax(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        String username = req.getParameter("username");
        boolean result =userService.checkUsername(username);
        resp.setContentType("utf-8");

        if (result){
            resp.getWriter().print(1);   //表示用户名已经被注册
        }else {
            resp.getWriter().print(2);   //表示用户名还可以被注册

        }
        return null;


    }

}
