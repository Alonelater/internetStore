package store.dao.daoimp;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import store.dao.OrderDao;
import store.domain.Order;
import store.domain.OrderItem;
import store.domain.Product;
import store.domain.User;
import store.utils.JDBCUtils;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderDaoImp implements OrderDao {


    QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());

    @Override
    public void saveOrder(Connection conn, Order order) throws SQLException {
        String sql="insert into orders values(?,?,?,?,?,?,?,?)";
        QueryRunner queryRunner = new QueryRunner();
        Date date = order.getDatetime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = simpleDateFormat.format(date);
        Object [] params={order.getOid(),dateTime,order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid()};
        queryRunner.update(conn,sql,params);
    }

    @Override
    public void saveOrderItem(Connection conn, OrderItem item) throws SQLException {
        String sql="insert into orderitem values (?,?,?,?,?)";
        QueryRunner queryRunner = new QueryRunner();
        Object [] params={item.getItemid(),item.getQuantity(),item.getTotal(),item.getProduct().getPid(),item.getOrder().getOid()};
         queryRunner.update(conn, sql, params);
    }

    @Override
    public int findTotalRecords(User user) throws SQLException {
        String sql ="select count(*) from orders where uid=?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        Long totalRecords = queryRunner.query(sql,new ScalarHandler<>(),user.getUid());
        return totalRecords.intValue();
    }

    @Override
    public int findTotalRecords() throws SQLException {
        String sql = "select count(*) from orders";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        Long totalRecords = queryRunner.query(sql,new ScalarHandler<>());
        return totalRecords.intValue();
    }

    @Override
    public int findTotalRecords(int state) throws SQLException {
        String sql = "select count(*) from orders where state=?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        Long totalRecords = queryRunner.query(sql,new ScalarHandler<>(),state);
        return totalRecords.intValue();
    }

    @Override
    public List findMyOrdersWithPage(User user, int startIndex, int pageSize) throws SQLException, InvocationTargetException, IllegalAccessException {
        //select * from orders where uid=? limit ?,?  从第几条开始  需要显示的记录数有几条
        String sql ="select * from orders where uid =? limit ?,?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        List<Order> list = queryRunner.query(sql,new BeanListHandler<Order>(Order.class),user.getUid(),startIndex,pageSize);
        //写到这里 说明我们已经能够得到的就是该用户的所有的订单 但是我们要得到每一笔订单下面的每一个订单项
        //因为我们想要展示的是不仅是订单  还有每笔订单下的订单项
        for (Order order:list){
            //这里涉及到多表查询
            //首先我们要明白我们要找的是订单下的订单项  而订单项下面还要把具体信息查询出来
            // 所以我们就要从orderItem 和product这两张表里面进行多表查询 而两张表里面关联项就是我们的pid 我们要通过pid将我们所要的信息筛选出来
            String sql1 ="select * from orderitem o,product p where p.pid=o.pid AND oid=?";

            //执行玩这个语句我们能够过得的是若干条数据，数据里面包含若干项  那我们就要将我们筛选出来有用的信息给存起来
            // 由于我们得到的数据来自于多个表 所以我们采用MapListHandel 来装载 因为待会儿不止一个数据 由因为待会儿我们要将查询到的数据放在相应的订单项里面
            // 但是我们的数据是来自多个表的 也就是说有不同的实体类 我们就要用map里面的键通过BeanUtil帮我们注入信息到不同的实体类里面
            List<Map<String, Object>> query = queryRunner.query(sql1, new MapListHandler(), order.getOid());
            //上面我们得到的是一个list集合 集合里面是map 我们将其遍历出来打印出来看看是什么
            for (Map<String, Object> map:query){
                /*for (Map.Entry<String, Object> entry : map.entrySet()){
                    System.out.print(entry.getKey()+":  "+entry.getValue()+"  ");
                }
                System.out.println();*/
                //itemid                       quantity total   pid  oid                                pid   pname              market_price   shop_price pimage               pdate    is_hot pdesc                                                                                                                                pflag  cid
                //1853B0291A04426887D458508F35AF13	1	2599	10	E518B8F12B73413996BBC893F295E435	10	华为 Ascend Mate7	        2699	2599	products/1/c_0010.jpg	2015-11-02	1	华为 Ascend Mate7 月光银 移动4G手机 双卡双待双通6英寸高清大屏，纤薄机身，智能超八核，按压式指纹识别！!选择下方“移动老用户4G飞享合约”，无需换号，还有话费每月返还！	0	1
                // 30C11AD77A7A4206B281DE4897C22E0A	2	8998	34	E518B8F12B73413996BBC893F295E435	34	联想（Lenovo）小新V3000经典版	4599	4499	products/1/c_0034.jpg	2015-11-02	0	14英寸超薄笔记本电脑（i7-5500U 4G 500G+8G SSHD 2G独显 全高清屏）黑色满1000減100，狂减！火力全开，横扫3天！	                                        0	2
                //49F42EC67BB44D499EDD8544B2ACECCC	2	11998	42	E518B8F12B73413996BBC893F295E435	42	微星（MSI）GE62 2QC-264XCN	6199	5999	products/1/c_0042.jpg	2015-11-02	0	15.6英寸游戏笔记本电脑（i5-4210H 8G 1T GTX960MG DDR5 2G 背光键盘）黑色	                                                                        0	2

                //我们要明白我们是通过orderitem和product的多表查询的  所以得到的笛卡尔积里面的列还是属于这两张表的 我们还是可以将其注入到相应的实体类中的

                //我们通过上面的sql1语句 得到的就是这几行结果 现在我们就是将我们得到的结果注入到相应的实体类里面 所以我们创建实体类的对象
                OrderItem orderItem = new OrderItem();
                BeanUtils.populate(orderItem,map);
                /*将上面下面的信息分别通过工具类注入到其中 可能时间转换有问题 如果有我们再另外处理一下*/
                Product product = new Product();
                BeanUtils.populate(product,map);
                //现在已经通过工具类给两个对象赋好了值  接下来就是将商品信息关联到购物项里面了
                orderItem.setProduct(product);
                //将购物项关联到订单里面
                order.getList().add(orderItem);
                /*到这里我们已经将每笔订单查出来里面有多少订单项 将订单项里面的商品信息放进去了 看起来我们只是通过beanListHandler得到了很多订单 但是我们将每笔订单都做了详细的赋值操作
                * 这样我们在商品页面信息展示的时候就能得到我们要的订单信息了  最后返回order集合*/
            }
        }
        return list;
    }

    @Override
    public List findMyOrdersWithPage(int startIndex, int pageSize) throws SQLException, InvocationTargetException, IllegalAccessException {
        String sql ="select * from orders  limit ?,?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        List<Order> orders = queryRunner.query(sql, new BeanListHandler<Order>(Order.class),startIndex,pageSize);
        return orders;

    }

    @Override
    public List findMyOrdersWithPage(int state, int startIndex, int pageSize) throws SQLException, InvocationTargetException, IllegalAccessException {
        String sql ="select * from orders where state=?  limit ?,?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        List<Order> orders = queryRunner.query(sql, new BeanListHandler<Order>(Order.class),state,startIndex,pageSize);
        return orders;
    }

    @Override
    public Order findOrderByOid(String oid) throws SQLException, InvocationTargetException, IllegalAccessException {
        String sql = "select * from orders where oid=?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        Order order = queryRunner.query(sql, new BeanHandler<Order>(Order.class), oid);
        //现在我们已经得到了整个订单  我们不仅要得到整个订单 我们还要得到订单下面的订单项
        //先准备sql语句
        String sql1 ="select * from orderitem o,product p where p.pid=o.pid AND oid=?";
        List<Map<String, Object>> list = queryRunner.query(sql1, new MapListHandler(), oid);
        for (Map<String, Object> map : list){
            //我们要明白我们是通过orderitem和product的多表查询的  所以得到的笛卡尔积里面的列还是属于这两张表的 我们还是可以将其注入到相应的实体类中的
            /*到这我们得到的就是orderItem里面的一个个键值对 所以我们再将其注入会我们得到的product和orderitem*/
            OrderItem orderItem = new OrderItem();
            Product product = new Product();
            BeanUtils.populate(orderItem,map);
            BeanUtils.populate(product,map);
            orderItem.setProduct(product);
            order.getList().add(orderItem);
        }

        return order;
    }

    @Override
    public void updateOrder(Order order) throws SQLException {
        String sql = "update orders set ordertime=?,total=?,state=?,address=?,name=?,telephone=?where oid=?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        Object [] params={order.getDatetime(),order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getOid()};
        queryRunner.update(sql,params);

    }
}
