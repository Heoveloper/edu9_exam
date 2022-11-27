package com.kh.product.web;

import com.kh.product.domain.Product;
import com.kh.product.domain.svc.ProductSVC;
import com.kh.product.web.form.AddForm;
import com.kh.product.web.form.ProductForm;
import com.kh.product.web.form.UpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductSVC productSVC;

    //상품등록화면
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("form", new AddForm());

        return "addForm";
    }

    //상품등록처리
    @PostMapping("/add")
    public String add(
            @Valid @ModelAttribute("form") AddForm addForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        //검증로직
        if(bindingResult.hasErrors()) {
            return "addForm";
        }
        //필드검증
        //상품수량은 100개를 초과할 수 없다.
        //상품수량과 가격은 0, 음수를 입력 불가
        if(addForm.getCount().longValue() > 100) {
            bindingResult.rejectValue("count", "countError", "수량은 100개를 초과할 수 없습니다!");
            return "addForm";
        }

        if(addForm.getCount().longValue() <= 0) {
            bindingResult.rejectValue("count", "countError2", "수량은 1개 이상이어야 합니다!");
            return "addForm";
        }

        if(addForm.getPrice().longValue() <= 0) {
            bindingResult.rejectValue("price", "priceError", "가격은 1원 이상이어야 합니다!");
            return "addForm";
        }
        //오브젝트검증
        //총액(상품수량*가격)이 1,000만원을 초과할 수 없다.
        if((addForm.getCount() * addForm.getPrice()) > 10000000) {
            bindingResult.reject("sumError", "총액은 1,000만원을 초과할 수 없습니다!");
            return "addForm";
        }

        //상품등록
        Product product = new Product();
        product.setPname(addForm.getPname());
        product.setCount(addForm.getCount());
        product.setPrice(addForm.getPrice());
        Product addedProduct = productSVC.add(product);

        Long pid = addedProduct.getPid();
        redirectAttributes.addAttribute("pid", pid);
        return "redirect:/product/{pid}";
    }

    //상품조회화면
    @GetMapping("/{pid}")
    public String findById(
            @PathVariable("pid") Long pid,
            Model model
    ) {
        Product findedProduct = productSVC.findById(pid);

        ProductForm productForm = new ProductForm();
        productForm.setPid(findedProduct.getPid());
        productForm.setPname(findedProduct.getPname());
        productForm.setCount(findedProduct.getCount());
        productForm.setPrice(findedProduct.getPrice());

        model.addAttribute("form", productForm);
        return "productForm";
    }

    //상품수정화면
    @GetMapping("/{pid}/edit")
    public String updateForm(@PathVariable("pid") Long pid, Model model) {
        Product findedProduct = productSVC.findById(pid);

        UpdateForm updateForm = new UpdateForm();
        updateForm.setPid(findedProduct.getPid());
        updateForm.setPname(findedProduct.getPname());
        updateForm.setCount(findedProduct.getCount());
        updateForm.setPrice(findedProduct.getPrice());

        model.addAttribute("form", updateForm);
        return "updateForm";
    }

    //상품수정처리
    @PostMapping("/{pid}/edit")
    public String update(
            @PathVariable("pid") Long pid,
            @Valid @ModelAttribute("form") UpdateForm updateForm,
            BindingResult bindingResult
    ) {
        Product product = new Product();
        product.setPname(updateForm.getPname());
        product.setCount(updateForm.getCount());
        product.setPrice(updateForm.getPrice());

        int updatedRow = productSVC.update(pid, product);
        if(updatedRow == 0) {
            return "updateForm";
        }
        return "redirect:/product/{pid}";
    }

    //상품삭제처리
    @GetMapping("/{pid}/del")
    public String delete(@PathVariable("pid") Long pid) {
        int deletedRow = productSVC.delete(pid);
        if(deletedRow == 0) {
            return "redirect:/product/{pid}";
        }
        return "redirect:/product";
    }

    //전체상품목록화면
    @GetMapping
    public String allProducts(Model model) {

        List<Product> allProducts = productSVC.allProducts();
        model.addAttribute("allProducts", allProducts);
        return "allProductsForm";
    }
}
