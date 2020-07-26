package store.web.servlet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import store.domain.Category;
import store.domain.PageModel;
import store.domain.Product;
import store.services.CategoryService;
import store.services.ProductService;
import store.services.serviceimp.CategoryServiceImp;
import store.services.serviceimp.ProductServiceImp;
import store.utils.UUIDUtils;
import store.web.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;

public class AdminProductServlet extends BaseServlet {

    ProductService productService = new ProductServiceImp();

    public String addProductUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        //获取全部信息
        CategoryService categoryService = new CategoryServiceImp();
        List<Category> allCats = categoryService.getAllCates();
        req.setAttribute("allCats",allCats);
        return "admin/product/add.jsp";
    }
    public String addProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {



        /*下面我们要写文件域的上传  这跟传统的不一样  不能通过req.getParameter(String name)得到键值 因为我们改了enctype="multipart/form-data"
         * 所以现在我们写复习文件上传的九大步骤
         *
         *1. 创建文件上传的目录和临时目录
         *2. 创建工厂
         * */
        //创建文件上传的目录
        String savePath =getServletContext().getRealPath("/products/3");
        System.out.println(savePath);
        //创建文件上传过大临时存放文件的临时目录  注意这就是个字符串  表示文件夹的路径  真正文件夹的创建在下面一行代码
        String tempPath = req.getServletContext().getRealPath("/WEB-INF/temp");

        /*看临时文件夹是否存在 不存在就创建*/
        File file = new File(tempPath);
        if (!file.exists()){
            //该方法会将子文件夹一起创建出来
            file.mkdirs();
        }

        //2.创建工厂
        // 创建这个对象 可以提供两个参数  一个是sizeThreshold  这个参数是设置默认值  就是文件的
        //大小如果超出了第一个参数的限制就存放在临时文件夹 否则就一次行加载到内存 第二个repository是指定临时文件夹的路径 所以就是上面创建出来的文件夹
        DiskFileItemFactory  diskFileItemFactory = new DiskFileItemFactory(100 * 1024, file);

        //3.创建解析器
        //这个是为了创建一个解析请求的解析器 该解析器需要一个工厂类作为参数 该解析器也能限制文件的大小
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        //该解析器也能、对文件做一些相应的限制
        //设置字符集
        servletFileUpload.setHeaderEncoding("UTF-8");
        //这个是对单个文件进行大小限制  还有的是对批量文件设置总共的大小
        servletFileUpload.setFileSizeMax(300*1024);
        //servletFileUpload.setSizeMax(40 * 1024 * 1024);

        //4.现在判断一下这个表单是否是用二进制流文件上传的  如果不是 就没必要进行了下面的操作了  直接return
        if(!servletFileUpload.isMultipartContent(req)){
            return "";
        }
        //5.利用解析器解析request请求  进行解析request得到的是一个list<FileItem>集合
        InputStream in =null;
        OutputStream out = null;
        Map<String,String> map = new HashMap();
        try {
            //这个FileItem  就是封装了form里面的一项项的表单项  我们将其分成普通表单项和其他表单项 普通就是文本，密码 checkbox等等
            List<FileItem> list = servletFileUpload.parseRequest(req);
            for (FileItem fileItem : list){
                //表示传统的表单项
                if (fileItem.isFormField()){
                    //我们要事先准备好一个集合 待会儿使用工具类将我们读取到的值帮我们转化为实体类Product
                    //不是用req.getParameter("")得到键值对 而是循环遍历得到所有的名字 并且得到值
                    //得到input的name
                    String name = fileItem.getFieldName();
                    System.out.println("普通表单项，名字是" + name);
                    //得到name的value值
                    String value = fileItem.getString("UTF-8");
                    System.out.println("普通表单项，值是" + value);
                    map.put(name,value);
                }else {//走到这里就是我们的文件上传的内容了
                    //因为不同的浏览器给我们的文件名字不一样 有些是带有盘符的  所以我们要先处理一下  并且我们还要获取的文件名字最后的后缀名
                    String fileName = fileItem.getName();//拿到文件的名字 针对不同浏览器的获得的信息有所不同
                    if (fileName == null || "".equals(fileName.trim())) {
                        continue;
                    }
                    //得到什么xxxx.jpg
                    fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
                    //得到后缀名
                    String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
                    if (!"jpg".equals(suffix) && !"jpeg".equals(suffix) && !"png".equals(suffix) && !"bmp".equals(suffix) && !"gif".equals(suffix)) {
                        throw new RuntimeException("只能上传带有jpg,png,jpeg,bmp,gif的后缀名的照片");
                    }

                    //现在我们就要将文件通过浏览器的请求将文件上传到/WEB-INF下面得product文件夹下面了
                    //我们定义一个方法保存我们最终的文件名 这个名字我们想让其变得唯一  通过下面两个方法我们已经知道了
                    String realFileName = make(fileName);
                    //System.out.println(realFileName);
                    String realSavePath = makePath(realFileName,savePath);
                    //System.out.println(realSavePath);



                    //创建一个输出流  输出流的路径就是我们的最终的得到存放路径
                    out = new FileOutputStream(realSavePath+"\\"+realFileName);
                    byte[] buffer =  new  byte[1024];
                    int len  =0;
                    //最关键的就是拿到输入流了  那就是通过我们的FileItem得到输入流
                    in= fileItem.getInputStream();
                    while ((len = in.read(buffer))!=-1){
                        out.write(buffer,0,len);
                    }
                    /*得到products/xxx文件夹  这个参数我们要存在数据库里面的  所以要保留下来 */
                    String pimage = realSavePath.substring(realSavePath.indexOf("products"));
                    pimage=pimage.replace("\\","/");
                    map.put("pimage",pimage+"/"+realFileName);
                }
            }

            //遍历完了之后  我们就将我们传来的内容更新进去数据库了我们看看还少了什么字段
            Product product = new Product();
            product.setPid(UUIDUtils.getId());
            product.setPdate(new Date());
            product.setCid_status(0);
            product.setPflag(0);
            BeanUtils.populate(product,map);
            ProductService productService = new ProductServiceImp();
            productService.saveProduct(product);
            resp.sendRedirect(req.getContextPath()+"/adminProductServlet?method=getAllProductWithPage&num=1");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if(in!=null){
                in.close();
            }
            if(out!=null){
                out.close();
            }
        }

        return null;



    }

    //getAllProductWithPage  已分页的形式获得所有的商品信息
    public String getAllProductWithPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {

        /*由于我们要构成一个pageModel对象 需要实现是所有他需要的参数 那我们点击订单管理的时候就应该获得数字第1页的内容
         * 所以我们在我们的left.jsp里面改造一下
         * */
        /*获得1这个数字  因为这是创建pageModel必不可少的一个参数*/
        int num = Integer.parseInt(req.getParameter("num"));

        //首先获取我们的所有商品记录  放在我们的pageModel对象里面
        PageModel pm = productService.findAllProduct(num);
        //将得到的分页对象放到request域中
        req.setAttribute("page", pm);
        //转发至相应的界面admin/product/list.jsp
        return "/admin/product/list.jsp";
    }

    public String editProductByPid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {

        String pid = req.getParameter("pid");

        Product product = productService.findProductByPid(pid);
        CategoryService categoryService = new CategoryServiceImp();
        List<Category> allCats = categoryService.getAllCates();
        req.setAttribute("allCats",allCats);
        req.setAttribute("p", product);
        return "/admin/product/edit.jsp";
    }

    public String saveEditProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {

        /*下面我们要写文件域的上传  这跟传统的不一样  不能通过req.getParameter(String name)得到键值 因为我们改了enctype="multipart/form-data"
        * 所以现在我们写复习文件上传的九大步骤
        *
        *1. 创建文件上传的目录和临时目录
        *2. 创建工厂
        * */
        //创建文件上传的目录
        String savePath =getServletContext().getRealPath("/products/3");
        System.out.println(savePath);
        //创建文件上传过大临时存放文件的临时目录  注意这就是个字符串  表示文件夹的路径  真正文件夹的创建在下面一行代码
        String tempPath = req.getServletContext().getRealPath("/WEB-INF/temp");

        /*看临时文件夹是否存在 不存在就创建*/
        File file = new File(tempPath);
        if (!file.exists()){
            //该方法会将子文件夹一起创建出来
            file.mkdirs();
        }

        //2.创建工厂
        // 创建这个对象 可以提供两个参数  一个是sizeThreshold  这个参数是设置默认值  就是文件的
        //大小如果超出了第一个参数的限制就存放在临时文件夹 否则就一次行加载到内存 第二个repository是指定临时文件夹的路径 所以就是上面创建出来的文件夹
        DiskFileItemFactory  diskFileItemFactory = new DiskFileItemFactory(100 * 1024, file);

        //3.创建解析器
        //这个是为了创建一个解析请求的解析器 该解析器需要一个工厂类作为参数 该解析器也能限制文件的大小
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        //该解析器也能、对文件做一些相应的限制
        //设置字符集
        servletFileUpload.setHeaderEncoding("UTF-8");
        //这个是对单个文件进行大小限制  还有的是对批量文件设置总共的大小
        servletFileUpload.setFileSizeMax(300*1024);
        //servletFileUpload.setSizeMax(40 * 1024 * 1024);

        //4.现在判断一下这个表单是否是用二进制流文件上传的  如果不是 就没必要进行了下面的操作了  直接return
        if(!servletFileUpload.isMultipartContent(req)){
            return "";
        }
        //5.利用解析器解析request请求  进行解析request得到的是一个list<FileItem>集合
        InputStream in =null;
        OutputStream out = null;
        Map<String,String> map = new HashMap();
        String  pid =null;
        try {
            //这个FileItem  就是封装了form里面的一项项的表单项  我们将其分成普通表单项和其他表单项 普通就是文本，密码 checkbox等等
            List<FileItem> list = servletFileUpload.parseRequest(req);
            for (FileItem fileItem : list){
                //表示传统的表单项
                if (fileItem.isFormField()){
                    //我们要事先准备好一个集合 待会儿使用工具类将我们读取到的值帮我们转化为实体类Product
                   //不是用req.getParameter("")得到键值对 而是循环遍历得到所有的名字 并且得到值
                    //得到input的name
                    String name = fileItem.getFieldName();

                    //System.out.println("普通表单项，名字是" + name);
                    //得到name的value值
                    String value = fileItem.getString("UTF-8");
                    //System.out.println("普通表单项，值是" + value);
                    if ("pid".equals(name)){
                        pid=value;
                    }
                    map.put(name,value);
                }else {//走到这里就是我们的文件上传的内容了
                    //因为不同的浏览器给我们的文件名字不一样 有些是带有盘符的  所以我们要先处理一下  并且我们还要获取的文件名字最后的后缀名
                    String fileName = fileItem.getName();//拿到文件的名字 针对不同浏览器的获得的信息有所不同
                    if (fileName == null || "".equals(fileName.trim())) {
                        ProductService productService = new ProductServiceImp();
                        Product productByPid = productService.findProductByPid(pid);
                        map.put("pimage",productByPid.getPimage());
                        continue;
                    }
                    //得到什么xxxx.jpg
                    fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
                    //得到后缀名
                    String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
                    if (!"jpg".equals(suffix) && !"jpeg".equals(suffix) && !"png".equals(suffix) && !"bmp".equals(suffix) && !"gif".equals(suffix)) {
                        throw new RuntimeException("只能上传带有jpg,png,jpeg,bmp,gif的后缀名的照片");
                    }

                    //现在我们就要将文件通过浏览器的请求将文件上传到/WEB-INF下面得product文件夹下面了
                    //我们定义一个方法保存我们最终的文件名 这个名字我们想让其变得唯一  通过下面两个方法我们已经知道了
                    String realFileName = make(fileName);
                    System.out.println(realFileName);
                    String realSavePath = makePath(realFileName,savePath);
                    System.out.println(realSavePath);



                    //创建一个输出流  输出流的路径就是我们的最终的得到存放路径
                    out = new FileOutputStream(realSavePath+"\\"+realFileName);
                    byte[] buffer =  new  byte[1024];
                    int len  =0;
                    //最关键的就是拿到输入流了  那就是通过我们的FileItem得到输入流
                    in= fileItem.getInputStream();
                    while ((len = in.read(buffer))!=-1){
                        out.write(buffer,0,len);
                    }
                    /*得到products/xxx文件夹  这个参数我们要存在数据库里面的  所以要保留下来 */
                    String pimage = realSavePath.substring(realSavePath.indexOf("products"));
                    pimage=pimage.replace("\\","/");
                    map.put("pimage",pimage+"/"+realFileName);
                }
            }

            //遍历完了之后  我们就将我们传来的内容更新进去数据库了我们看看还少了什么字段
            Product product = new Product();
            product.setPdate(new Date());
            product.setCid_status(0);
            product.setPflag(0);
            BeanUtils.populate(product,map);
            ProductService productService = new ProductServiceImp();
            productService.updateProductByPid(product);
            resp.sendRedirect(req.getContextPath()+"/adminProductServlet?method=getAllProductWithPage&num=1");
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }finally {
            if(in!=null){
                in.close();
            }
            if(out!=null){
                out.close();
            }
        }


        return null;
    }


    //将商品下架
    public String pushDownProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {

        String pid = req.getParameter("pid");

        Product product = productService.findProductByPid(pid);
        product.setPflag(1);
        productService.updateProductByPid(product);
        resp.sendRedirect(req.getContextPath()+"/adminProductServlet?method=getAllProductWithPage&num=1");
        return null;
    }
    //得到下架商品的详细信息getAllPushDownWithPage&num=1
    public String getAllPushDownWithPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {

        int num = Integer.parseInt(req.getParameter("num"));

        PageModel pageModel = productService.findAllPushDownProducts(num);
        //将得到的分页对象放到request域中
        req.setAttribute("page", pageModel);
        //转发至相应的界面admin/product/list.jsp
        return "/admin/product/pushDown_list.jsp";

    }

    public String restorePushDownProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String pid = req.getParameter("pid");

        Product product = productService.findProductByPid(pid);
        product.setPflag(0);
        productService.updateProductByPid(product);
        resp.sendRedirect(req.getContextPath()+"/adminProductServlet?method=getAllPushDownWithPage&num=1");
        return null;

    }



    private String makePath(String realFileName, String savePath) {
        int dir1= realFileName.hashCode()&0x0f;//得到一个0-15的整数
        String dir = savePath+"\\"+dir1;
        File file = new File(dir);
        if (!file.exists()){
            file.mkdirs();
        }
        //最后return  dir这个最终的存放文件的目录
        return dir;
    }

    private String make(String fileName) {
        //通过uuid得到一个永远不会相同的文件名
        String uuid = UUID.randomUUID().toString();
        uuid= uuid.replace("-","");
        return uuid+"_"+fileName;
        //return UUID.randomUUID().toString()+"_"+fileName;
    }


}