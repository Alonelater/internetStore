package store.dao;

import store.domain.Category;
import store.domain.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    List<Product> findNews() throws SQLException;

    List<Product> findHots() throws SQLException;

    Product findProductByPid(String pid) throws SQLException;

    int findTotalRecords(String cid) throws SQLException;

    int findTotalRecords() throws SQLException;


    List findProductsByCidWithPage(String cid, int startIndex, int pageSize) throws SQLException;
    List findProductsWithPage( int startIndex, int pageSize) throws SQLException;

    void updateProductsByCid(Connection conn, String cid, Category category) throws SQLException;

    void updateProductByPid(Product product) throws SQLException;

    void saveProduct(Product product) throws SQLException;

    int findPushDownProdeuctTotal() throws SQLException;

    List<Product> findPushDownProdeuctList(int startIndex, int pageSize) throws SQLException;
}
