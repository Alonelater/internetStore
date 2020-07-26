package store.services;

import store.domain.Category;
import store.domain.PageModel;
import store.domain.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProductService {
    List<Product> findNews() throws SQLException;

    List<Product> findHots() throws SQLException;

    Product findProductByPid(String pid) throws SQLException;

    PageModel findProductsByCidWithPage(String cid, int num) throws SQLException;

    void updateProductsByCid(Connection conn, String cid, Category c) throws SQLException;

    PageModel findAllProduct(int num) throws SQLException;

    void updateProductByPid(Product product) throws SQLException;

    void saveProduct(Product product) throws SQLException;

    PageModel findAllPushDownProducts(int num) throws SQLException;
}
