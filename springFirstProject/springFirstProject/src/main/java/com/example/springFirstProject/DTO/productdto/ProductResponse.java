package com.example.springFirstProject.DTO.productdto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private Integer productStockQuantity;
    private String productCategory;
    private String productURL;
    private Boolean active;
}
