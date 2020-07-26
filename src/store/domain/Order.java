package store.domain;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private  String oid;//订单的id
    private  Date datetime;//订单时间
    private  double total;//订单总计
    private  int state;//订单状态  订单状态：1=未付款;2=已付款,未发货;3=已发货,没收货;4=收货,订单结束
    private  String address;//下单地址
    private  String name;//下单地址
    private  String telephone;//下单地址
    /*这里最重要的是就是这个用户的uid  那我们就不用单独使用一个uid直接用一个user对象*/
    private  User user;
    /*程序中体现订单对象和订单项对象的关系 这是为了我们之后的需求 比如查询订单的时候将该订单下的所有订单项查询出来*/
    private List<OrderItem> list = new ArrayList<>();

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public double getTotal() {
        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order() {
    }

    public List<OrderItem> getList() {
        return list;
    }

    public void setList(List<OrderItem> list) {
        this.list = list;
    }


}
