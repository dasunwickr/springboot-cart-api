package com.dasunhq.application.request;

import com.dasunhq.application.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductsRequest {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;

    private Category category;
}
