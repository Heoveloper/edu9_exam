package com.kh.demo.dao;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ProductDAOImplTest {

    @Autowired
    private ProductDAO productDAO;

    @Test
    @DisplayName("상품등록")
    void save() {

        Product product = new Product();
        product.setPname("에어컨");
        product.setQuantity(5L);
        product.setPrice(500000L);

        //등록
        Long savedProduct = productDAO.save(product);

        log.info("savedProduct = {}", savedProduct);
        Assertions.assertThat(savedProduct).isEqualTo(382);

    }

    @Test
    @DisplayName("상품목록")
    void findAll() {

        //전체 조회
        List<Product> allProducts = productDAO.findAll();

        Stream<Product> products = allProducts.stream();
        products.forEach(product -> log.info(product.toString()));

    }

    @Test
    @DisplayName("상품조회")
    void findByProductId() {

        //조회
        Optional<Product> findedProduct = productDAO.findByProductId(382L);

        log.info("findedProduct = {}", findedProduct);

    }

    @Test
    @DisplayName("상품수정")
    void update() {

        Product product = new Product();
        product.setPname("강풍에어컨");
        product.setQuantity(7L);
        product.setPrice(700000L);

        //수정
        int updatedRow = productDAO.update(382L, product);

        log.info("updatedRow = {}", updatedRow);
        Assertions.assertThat(updatedRow).isEqualTo(1);

    }

    @Test
    @DisplayName("상품삭제")
    void deleteByProductId() {

        //삭제
        int deletedRow = productDAO.deleteByProductId(382L);

        log.info("deletedRow = {}", deletedRow);
        Assertions.assertThat(deletedRow).isEqualTo(1);

    }
}