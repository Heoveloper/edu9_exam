package com.kh.product.domain.dao;

import com.kh.product.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductDAOImpl implements ProductDAO {
    private final JdbcTemplate jt;

    /**
     * 상품등록
     *
     * @param product
     * @return
     */
    @Override
    public int add(Product product) {
        int add = 0;
        StringBuffer sql = new StringBuffer();
        sql.append("insert into product (pid, pname, count, price) ");
        sql.append("values (?, ?, ?, ?) ");

        add = jt.update(sql.toString(), product.getPid(), product.getPname(), product.getCount(), product.getPrice());

        return add;
    }

    /**
     * 상품조회
     *
     * @param pid
     * @return
     */
    @Override
    public Product findById(Long pid) {
        StringBuffer sql = new StringBuffer();
        sql.append("select pid, pname, count, price ");
        sql.append("from product ");
        sql.append("where pid = ? ");

        Product product = null;
        product = jt.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(Product.class), pid);
        return product;
    }

    /**
     * 상품수정
     *
     * @param pid
     * @param product
     * @return
     */
    @Override
    public int update(Long pid, Product product) {
        int update = 0;
        StringBuffer sql = new StringBuffer();
        sql.append("update product ");
        sql.append("   set pname = ?, ");
        sql.append("       count = ?, ");
        sql.append("       price = ? ");
        sql.append(" where pid = ? ");

        update = jt.update(sql.toString(), product.getPname(), product.getCount(), product.getPrice(), pid);
        return update;
    }

    /**
     * 상품삭제
     *
     * @param pid
     * @return
     */
    @Override
    public int delete(Long pid) {
        int delete = 0;
        String sql = "delete from product where pid = ? ";

        delete = jt.update(sql, pid);
        return delete;
    }

    /**
     * 전체상품조회
     *
     * @return
     */
    @Override
    public List<Product> allProducts() {
        StringBuffer sql = new StringBuffer();
        sql.append("select pid, pname, count, price ");
        sql.append("  from product ");

        return jt.query(sql.toString(), new BeanPropertyRowMapper<>(Product.class));
    }

    /**
     * 상품번호생성
     *
     * @return
     */
    @Override
    public Long generatePid() {
        String sql = "select product_pid_seq.nextval from dual";
        Long newPid = jt.queryForObject(sql, Long.class);
        return newPid;
    }
}
