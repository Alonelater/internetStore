package store.services.serviceimp;

import jdk.management.resource.internal.TotalResourceContext;
import store.dao.ProductDao;
import store.dao.daoimp.ProductDaoImp;
import store.domain.Category;
import store.domain.PageModel;
import store.domain.Product;
import store.services.ProductService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductServiceImp implements ProductService {


    ProductDao productDao =new ProductDaoImp();

    /*
    这是service里面调用dao层查找最新数据库的九件商品  排序通过时间来排
     */
    @Override
    public List<Product> findNews() throws SQLException {
        List<Product> list = productDao.findNews();
        return list;
    }
    /*
        这是service里面调用dao层查找最新数据库在货架上的最热门的九件商品
         */
    @Override
    public List<Product> findHots() throws SQLException {
        List<Product> list1 =productDao.findHots();
        return list1;
    }

    /*
    通过点击一件商品  通过商品的id找到该商品的详细参数
     */
    @Override
    public Product findProductByPid(String pid) throws SQLException {
        Product product = productDao.findProductByPid(pid);
        return product;
    }

    /*
    这个就比较复杂了 我么你要返回一个pageModel的实体类 这个实体类封装了很多方法 里面的方法计算了我们点击的
    模块所需要的的详细数据  但是我们仅仅靠前面提供的信息还不足以得到一个PageModel对象  我们还需要数据库里面的当前模块的
    总数据 这样我们才能在创建PageModel的时候通过写在其构造方法里面的计算很多东西
     */
    @Override
    public PageModel findProductsByCidWithPage(String cid, int num) throws SQLException {

        //1.创建PageModel  目的就是为了计算分页参数
        //既然要创建一个PageModel对象 那我们就要提供一些创建的参数 一个是当前页数 我们在没有点击的任何下面的页数时默认为1
        //第二个就是 当前页需要显示几个 那我们根据美工设计的参考设计为12个  还有最后一个参数是当前模块数据库的总产品数量
        //这个是需要查询的  所以我们提供一个方法专门查询总数

        int totalRecords = productDao.findTotalRecords(cid);
        //System.out.println(totalRecords);
        //将得到的记录总数传进去  得到一个PageModel 对象  该对象只要你传进去值他就会帮你算好当前页数 总页数 页数的下面栏目
        PageModel pm  = new PageModel(num,12,totalRecords);

       // System.out.println("--------");
       // System.out.print(pm.getStartIndex()+"     "+pm.getPageSize());

        //2.关联集合
        //因为我们要执行的是这个语句 select * from product where cid =? limit ?,?; 所以得到的就是一个List集合
        //实际上这个集合的类型是我们的Product 类型 我们先写上去 如果错了再删除好了
        List list = productDao.findProductsByCidWithPage(cid,pm.getStartIndex(),pm.getPageSize());
        pm.setList(list);
        //3.关联url地址
        pm.setUrl("productServlet?method=findProductsByCidWithPage&cid="+cid);
        return  pm;

    }

    @Override
    public void updateProductsByCid(Connection conn, String cid, Category category) throws SQLException {
        productDao.updateProductsByCid(conn, cid,category);
    }

    @Override
    public PageModel findAllProduct(int num) throws SQLException {
      //我们还需要一些参数 不如总记录帮我们计算要分多少页
        int total = productDao.findTotalRecords();
        //将上面传来的num  我们自己设置的每页展示多少条记录数  还有总数量 这样pageModel的构造方法就会帮我们算好我们的分页详细参数
        PageModel pageModel = new PageModel(num,9,total);
        //下面关联集合
        List list = productDao.findProductsWithPage(pageModel.getStartIndex(), pageModel.getPageSize());
        pageModel.setList(list);
        //最后关联路径 然后返回pageModel对象就行
        pageModel.setUrl("adminProductServlet?method=getAllProductWithPage");
        return pageModel;
    }

    @Override
    public void updateProductByPid(Product product) throws SQLException {
        productDao.updateProductByPid(product);
    }

    @Override
    public void saveProduct(Product product) throws SQLException {
        productDao.saveProduct(product);

    }

    @Override
    public PageModel findAllPushDownProducts(int num) throws SQLException {
        int total = productDao.findPushDownProdeuctTotal();
        PageModel pageModel = new PageModel(num,9, total);
        //关联集合
        List<Product> list = productDao.findPushDownProdeuctList(pageModel.getStartIndex(),pageModel.getPageSize());
        pageModel.setList(list);
        //关联路径
        pageModel.setUrl("adminProductServlet?method=getAllPushDownWithPage");
        return  pageModel;
    }
}
