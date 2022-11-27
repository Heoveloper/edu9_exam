package com.kh.product.web.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddForm {
    @NotBlank
    private String pname;
    @NotNull
    private Long count;
    @NotNull
    private Long price;
}
