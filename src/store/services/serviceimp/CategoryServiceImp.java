package store.services.serviceimp;

import store.dao.CategoryDao;
import store.dao.daoimp.CategoryDaoImp;
import store.domain.Category;
import store.services.CategoryService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImp implements CategoryService {


    CategoryDao categoryDao = new CategoryDaoImp();
    @Override
    public List<Category> getAllCates() throws SQLException {

        List <Category> list = categoryDao.getAllCates();
        return list;
    }

    @Override
    public void addCategory(Category c) throws SQLException {

        categoryDao.addCategory(c);
    }

    @Override
    public Category findCateByCid(String cid) throws SQLException {

        return categoryDao.findCateByCid(cid);

    }

    @Override
    public void editCategory(Category c) throws SQLException {
        CategoryDao categoryDao = new CategoryDaoImp();
        categoryDao.editCategory(c);
    }

    @Override
    public void updateCategory(Connection conn,Category category) throws SQLException {

        categoryDao.updateCategory(conn,category);
    }

    /*得到所有下架的商品分类信息*/
    @Override
    public List<Category> getAllLowCates() throws SQLException {

        return  categoryDao.getAllLowCates();
    }


}
