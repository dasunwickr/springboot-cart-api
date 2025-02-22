package com.dasunhq.application.service.product;

import com.dasunhq.application.dto.CategoryDto;
import com.dasunhq.application.dto.ImageDto;
import com.dasunhq.application.dto.ProductDto;
import com.dasunhq.application.exceptions.ProductNotFoundException;
import com.dasunhq.application.exceptions.ResourceNotFoundException;
import com.dasunhq.application.model.Category;
import com.dasunhq.application.model.Image;
import com.dasunhq.application.model.Product;
import com.dasunhq.application.repository.CategoryRepository;
import com.dasunhq.application.repository.ImageRepository;
import com.dasunhq.application.repository.ProductRepository;
import com.dasunhq.application.request.AddProductRequest;
import com.dasunhq.application.request.UpdateProductsRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
       Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
               .orElseGet(()-> {
                   Category newCategory = new Category(request.getCategory().getName());
                   return categoryRepository.save(newCategory);
               });
       request.setCategory(category);
       System.out.println(request);
       return productRepository.save(createProduct(request,category));
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product not found!"));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        ()-> {throw new ResourceNotFoundException("Product not found!");
                });
    }

    @Override
    public Product updateProduct(UpdateProductsRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductsRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return productRepository.save(existingProduct);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String categoryName, String brand) {
        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found!");
        }
        return productRepository.findByCategoryAndBrand(category, brand);
    }


    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product){

        ProductDto productDto = modelMapper.map(product, ProductDto.class);

        CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
        productDto.setCategory(categoryDto);

        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .collect(Collectors.toList());

        productDto.setImages(imageDtos);

        return productDto;
    }
}
