package store.dao.daoimp;

import jdk.nashorn.internal.scripts.JD;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import store.dao.ProductDao;
import store.domain.Category;
import store.domain.Product;
import store.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ProductDaoImp implements ProductDao {


    QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());




    /**
     * 查询在货架上并且最新的按照时间顺序排序  找出共九件商品封装成商品实体类存储在集合里面
     *
     * @return 返回一个集合
     * @throws SQLException
     */
    @Override
    public List<Product> findNews() throws SQLException {

        String sql ="SELECT * from product WHERE pflag=0 and cid_status=0 ORDER BY pdate DESC LIMIT 0,9";

        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        List<Product> list = queryRunner.query(sql,new BeanListHandler<Product>(Product.class));
        return list;
    }

    /**
     * 查询在货架上并且最热门的按照时间顺序排序  找出九件商品封装成商品实体类存储在集合里面
     *
     * @return 返回一个集合
     * @throws SQLException
     */
    @Override
    public List<Product> findHots() throws SQLException {

        String sql ="SELECT * from product WHERE pflag=0 and is_hot=1 and cid_status=0 ORDER BY pdate DESC LIMIT 0,9";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        List<Product> list1 = queryRunner.query(sql,new BeanListHandler<Product>(Product.class));
        return list1;
    }

    /**
     * 查询商品的编号为pid 的详细信息  通过工具类封装成商品实体类传回上面
     *
     * @param pid 商品的编号
     * @return 商品实体类
     * @throws SQLException
     */

    @Override
    public Product findProductByPid(String pid) throws SQLException {
        String sql ="select * from product where pid= ?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        Product product =queryRunner.query(sql,new BeanHandler<Product>(Product.class),pid);
        return product;
    }

    /**
     *
     * @param cid  查询模块为cid的在数据库里面共有几条记录
     * @return 记录总数
     * @throws SQLException
     */
    @Override
    public int findTotalRecords(String cid) throws SQLException {
        String sql ="select count(*) from product where cid =? ";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        //这里要注意 得到的是一个long类型的  我们要强转
        Long totalRecords =queryRunner.query(sql,new ScalarHandler<>(),cid);
        return totalRecords.intValue();
    }



    /*不要条件查出所有的商品数量*/
    @Override
    public int findTotalRecords() throws SQLException {
        String sql ="select count(*) from product";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        //这里要注意 得到的是一个long类型的  我们要强转
        Long totalRecords =queryRunner.query(sql,new ScalarHandler<>());
        return totalRecords.intValue();
    }

    @Override
    public List<Product> findProductsByCidWithPage(String cid, int startIndex, int pageSize) throws SQLException {

        String sql ="select * from product where cid=? limit ?,?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class), cid, startIndex, pageSize);
        return list;

    }

    @Override
    public List findProductsWithPage(int startIndex, int pageSize) throws SQLException {

        String sql ="select * from product where pflag='0' order by pdate desc limit ?,?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class),startIndex, pageSize);
        return list;

    }

    @Override
    public void updateProductsByCid(Connection conn, String cid, Category category) throws SQLException {
        String sql ="update product set cid_status = ? where cid =? ";
        QueryRunner queryRunner = new QueryRunner();
        queryRunner.update(conn,sql,category.getCstatus(),cid);
    }

    @Override
    public void updateProductByPid(Product product) throws SQLException {
        String sql = "update product set pname=?,market_price=?,shop_price=?,pimage=?,pdate=?,is_hot=?,pdesc=?,pflag=?,cid=?,cid_status=? where pid=?";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String  pdate = simpleDateFormat.format(product.getPdate());
        Object [] params = {product.getPname(),product.getMarket_price(),product.getShop_price(),product.getPimage(),pdate,product.getIs_hot(),product.getPdesc(),product.getPflag(),product.getCid(),product.getCid_status(),product.getPid()};
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        queryRunner.update(sql,params);


    }

    @Override
    public void saveProduct(Product product) throws SQLException {
        String sql = "insert into  product values(?,?,?,?,?,?,?,?,?,?,?)";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String  pdate = simpleDateFormat.format(product.getPdate());
        Object [] params = {product.getPid(),product.getPname(),product.getMarket_price(),product.getShop_price(),product.getPimage(),pdate,product.getIs_hot(),product.getPdesc(),product.getPflag(),product.getCid(),product.getCid_status()};
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        queryRunner.update(sql,params);

    }


    /*返回下架商品的总数量*/
    @Override
    public int findPushDownProdeuctTotal() throws SQLException {
        String sql = "select count(*) from product where pflag=?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        //这里要注意 得到的是一个long类型的  我们要强转
        Long totalRecords =queryRunner.query(sql,new ScalarHandler<>(),1);
        return totalRecords.intValue();
    }
    /*返回下架商品的集合*/
    @Override
    public List<Product> findPushDownProdeuctList(int startIndex, int pageSize) throws SQLException {
        String sql = "select * from product where pflag = '1' order by pdate desc limit ?,? ";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class),startIndex, pageSize);
        return list;
    }
}
