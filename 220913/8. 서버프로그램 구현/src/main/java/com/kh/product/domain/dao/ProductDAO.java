package com.kh.product.domain.dao;

import com.kh.product.domain.Product;

import java.util.List;

public interface ProductDAO {
    /**
     * 상품등록
     * @param product
     * @return
     */
    int add(Product product);

    /**
     * 상품조회
     * @param pid
     * @return
     */
    Product findById(Long pid);

    /**
     * 상품수정
     * @param pid
     * @param product
     * @return
     */
    int update(Long pid, Product product);

    /**
     * 상품삭제
     * @param pid
     * @return
     */
    int delete(Long pid);

    /**
     * 전체상품조회
     * @return
     */
    List<Product> allProducts();

    /**
     * 상품번호생성
     * @return
     */
    Long generatePid();
}
