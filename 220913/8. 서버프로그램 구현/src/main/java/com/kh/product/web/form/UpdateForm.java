package com.kh.product.web.form;

import lombok.Data;

@Data
public class UpdateForm {
    private Long pid;
    private String pname;
    private Long count;
    private Long price;
}
