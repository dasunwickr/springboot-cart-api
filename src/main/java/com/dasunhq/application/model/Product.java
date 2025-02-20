package com.dasunhq.application.model;

import java.math.BigDecimal;
import java.util.List;

public class Product {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;

    private Category category;

    private List<Image> images;
}
