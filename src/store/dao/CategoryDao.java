package store.dao;

import store.domain.Category;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CategoryDao {
    List<Category> getAllCates() throws SQLException;

    void addCategory(Category c) throws SQLException;

    Category findCateByCid(String cid) throws SQLException;

    void editCategory(Category c) throws SQLException;

    void updateCategory(Connection con,Category category) throws SQLException;

    List<Category> getAllLowCates() throws SQLException;
}
