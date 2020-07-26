package store.domain;

import java.awt.event.FocusEvent;
import java.util.*;

public class Cart {
    /**
     * 这个类就是我们的购物车了 购物车这个类就是有很多个购物项组成
     * 除此之外还有很多的自己的功能 也就是方法 比如删除某个购物项
     * 清除购物车啊 添加购物车啊
     * 所以我们先分析 共有两个属性  一个是购物项 一个是商品总金额 也就是总积分
     * 三个方法 添加购物车  删除某一个购物项 清空购物车
     *
     */
    //因为有n个购物项 所以我们采用什么集合来存储我们的集合好一点呢 按照往常我们都是用List集合


 /*
    //那我们先用这个试试
    private List<CartItem> list = new ArrayList<>();//集合
    private  double total;//总计/积分
    //添加购物车
    public void addCartItem(CartItem cartItem){
      //添加商品到购物车  我们先去遍历集合里面有没有这个商品 有的话直接增加数量  没有才要增加进集合里面
      //遍历list集合
        //设置状态值 不然不好写循环 false就表示没有 true表示有  假设集合里里面是没有的
        boolean flag =false;
        if (list!=null&&list.size()>0){
            for (CartItem c :list){
                 //如果传进来的购物项的pid和list集合里面的pid一样我们就认为购物车有该东西 只要叠加数量就行了
                if (cartItem.getProduct().getPid().equals(c.getProduct().getPid())){
                    flag = true;
                    int oldNum =c.getNum();
                    oldNum+=cartItem.getNum();
                    break;
                }
            }
            //遍历完没有就直接加入
            if(!flag){
                list.add(cartItem);
            }

        }else{//表示集合为空 那就是购物车为空 直接加入集合就是可
            list.add(cartItem);
        }
    }

    //移除购物车的某一项  我们删除购物项是根据请求携带的信息 也就是pid  我们根据那个进行删除
    public void removeCartItem(String pid){
        //因为不支持一边遍历一边删除 所以我们使用迭代 进行删除
        //首先我们要获得那个pid所在的那个购物项 然后在遍历

    }
    //清空购物车
    public  void clearCar(){
        list.clear();
    }
*/




 //根据上面我们明显感觉到这个list集合很麻烦  所以我们试试map集合  我们是将pid作为键整个购物项作为值
    private Map<String,CartItem> map = new HashMap<>();
    private  double total;//总计/积分

    //添加购物车
    public void addCartItemToCart(CartItem cartItem){
        //添加商品到购物车  我们先去遍历集合里面有没有这个商品 有的话直接增加数量  没有才要增加进map集合里面
        //遍历map集合
        //设置状态值 不然不好写循环 false就表示没有 true表示有  假设集合里里面是没有的
       String pid = cartItem.getProduct().getPid();
       if (map.containsKey(pid)){
           //表示存在这个键 存在就取出来
           CartItem oldItem = map.get(pid);
           oldItem.setNum(oldItem.getNum()+cartItem.getNum());
       }else {//表示没有这个
           map.put(pid,cartItem);
       }
    }
    /*获取map集合里面的购物项*//*
    public Collection getCartItems(){
        return map.values();
    }
    */

    public Collection<CartItem> getCartItems() {
        return map.values();
    }

    /*public void setCartItems(Collection<CartItem> cartItems) {
        this.cartItems = cartItems;
    }*/

    //移除购物车的某一项  我们删除购物项是根据请求携带的信息 也就是pid  我们根据那个进行删除
    public void removeCartItem(String pid){
        //因为不支持一边遍历一边删除 所以我们使用迭代 进行删除
        //首先我们要获得那个pid所在的那个购物项 然后在遍历
        map.remove(pid);

    }
    //清空购物车
    public  void clearCart(){
        map.clear();
    }


    public Map<String, CartItem> getMap() {
        return map;
    }

    public void setMap(Map<String, CartItem> map) {
        this.map = map;
    }
    //获取总计  就是购物车的结算金额
    public double getTotal() {
        total = 0;
        //循环遍历所有的购物项  得到所有的小计 就可以得到总金额了
        Collection<CartItem> values = map.values();
        for (CartItem cartItem :values){
            total = total+cartItem.getSubTotal();
        }
        //System.out.println(total);
        return total;
    }


    public void setTotal(double total) {
        this.total = total;

    }
}
