package store.domain;


/**
 * 这个类是用来描述购物车里面得购物项的 我们将其看成是一个对象 所以我们将一些公共的属性抽取出来
 */
public class CartItem {

    //根据图例 我们知道里里面有图片 商品名称 价格 但是这些是Product 里面的属性
    //我们不会再将其单独抽出来作为属性了 而是把整个实体类Product作为购物项的属性
    //所以除此之外我们还有数量 和小计
    //所以共有三个属性 一个Product类属性 一个购买数量 一个金钱小计
    private Product product;
    private int num ;
    private  double subTotal;

    public CartItem() {
    }

    public CartItem(Product product, int num, double subTotal) {
        this.product = product;
        this.num = num;
        this.subTotal = subTotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getSubTotal() {
        return product.getShop_price() * num;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal =subTotal;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product +
                ", num=" + num +
                ", subTotal=" + subTotal +
                '}';
    }
}
