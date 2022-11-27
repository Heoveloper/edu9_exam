package com.kh.product.domain.svc;

import com.kh.product.domain.Product;
import com.kh.product.domain.dao.ProductDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSVCImpl implements ProductSVC {
    private final ProductDAO productDAO;

    /**
     * 상품등록
     *
     * @param product
     * @return
     */
    @Override
    public Product add(Product product) {
        Long generatePid = productDAO.generatePid();
        product.setPid(generatePid);
        productDAO.add(product);
        return productDAO.findById(generatePid);
    }

    /**
     * 상품조회
     *
     * @param pid
     * @return
     */
    @Override
    public Product findById(Long pid) {
        return productDAO.findById(pid);
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
        int cnt = productDAO.update(pid, product);
        return cnt;
    }

    /**
     * 상품삭제
     *
     * @param pid
     * @return
     */
    @Override
    public int delete(Long pid) {
        int cnt = productDAO.delete(pid);
        return cnt;
    }

    /**
     * 전체상품조회
     *
     * @return
     */
    @Override
    public List<Product> allProducts() {
        return productDAO.allProducts();
    }
}
