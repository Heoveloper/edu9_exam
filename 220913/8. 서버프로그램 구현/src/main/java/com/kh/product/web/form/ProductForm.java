package com.kh.product.web.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductForm {
    private Long pid;
    private String pname;
    private Long count;
    private Long price;
}
