package store.dao.daoimp;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import store.dao.CategoryDao;
import store.domain.Category;
import store.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryDaoImp implements CategoryDao {

    QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());



    @Override
    public List<Category> getAllCates() throws SQLException {
        String sql = "select * from category where cstatus=0";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        return queryRunner.query(sql, new BeanListHandler<Category>(Category.class));

    }

    @Override
    public void addCategory(Category c) throws SQLException {
        String sql = "insert into  category(cid,cname,cstatus) values(?,?,?)";
         queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        queryRunner.update(sql, c.getCid(), c.getCname(), 1);
    }

    @Override
    public Category findCateByCid(String cid) throws SQLException {
        String sql = "select * from category where cid=?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        return queryRunner.query(sql, new BeanHandler<Category>(Category.class), cid);
    }

    @Override
    public void editCategory(Category c) throws SQLException {
        String sql = "update category set cname=? where cid=?";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        queryRunner.update(sql, c.getCname(), c.getCid());
    }

    @Override
    public void updateCategory(Connection conn, Category category) throws SQLException {
        String sql = "update category set cstatus=? where cid=?";
        QueryRunner queryRunner = new QueryRunner();
        queryRunner.update(conn, sql, category.getCstatus(), category.getCid());
    }

    @Override
    public List<Category> getAllLowCates() throws SQLException {
        String sql = "select * from category where cstatus=1";
        queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        return queryRunner.query(sql, new BeanListHandler<Category>(Category.class));
    }
}
