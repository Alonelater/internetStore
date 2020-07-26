package store.domain;

public class OrderItem {

    private String itemid;//商品项的id
    private int quantity;//单个购物项的数量
    private double total;//这个total是小计
    /*将实体类作为属性 携带更多的信息  比如pid oid*/
    private Product product;//购买商品的id
    private Order order;//订单项所在订单id

    public OrderItem() {
    }

    public OrderItem(String itemid, int quantity, double total, Product product, Order order) {
        this.itemid = itemid;
        this.quantity = quantity;
        this.total = total;
        this.product = product;
        this.order = order;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "itemid='" + itemid + '\'' +
                ", quantity=" + quantity +
                ", total=" + total +
                ", product=" + product +
                ", order=" + order +
                '}';
    }
}
