package com.dasunhq.application.service.product;

import com.dasunhq.application.dto.ProductDto;
import com.dasunhq.application.model.Category;
import com.dasunhq.application.model.Product;
import com.dasunhq.application.request.AddProductRequest;
import com.dasunhq.application.request.UpdateProductsRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(UpdateProductsRequest product, Long productId );
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);

    ProductDto convertToDto(Product product);
}
