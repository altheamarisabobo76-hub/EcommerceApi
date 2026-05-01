package com.ws101.abobo.eccommerceapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.ws101.abobo.eccommerceapi.exception.ProductNotFoundException;
import com.ws101.abobo.eccommerceapi.model.Product;

public class ProductService {
      private final List<Product> productList = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public ProductService() {
        // Sample data
        for (int i = 1; i <= 10; i++) {
            productList.add(new Product(
                    counter.incrementAndGet(),
                    "Product " + i,
                    "Description " + i,
                    100.0 * i,
                    i % 2 == 0 ? "Electronics" : "Clothing",
                    10 * i,
                    ""
            ));
        }
    }

    public List<Product> getAllProducts() {
        return productList;
    }

    public Product getProductById(Long id) {
        return productList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public Product createProduct(Product product) {
        product.setId(counter.incrementAndGet());
        productList.add(product);
        return product;
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = getProductById(id);

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCategory(updatedProduct.getCategory());
        existing.setStockQuantity(updatedProduct.getStockQuantity());
        existing.setImageUrl(updatedProduct.getImageUrl());

        return existing;
    }

    public Product patchProduct(Long id, Map<String, Object> updates) {
        Product product = getProductById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> product.setName((String) value);
                case "description" -> product.setDescription((String) value);
                case "price" -> product.setPrice((Double) value);
                case "category" -> product.setCategory((String) value);
                case "stockQuantity" -> product.setStockQuantity((Integer) value);
                case "imageUrl" -> product.setImageUrl((String) value);
            }
        });
        return product;
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productList.remove(product);
    }

    public List<Product> filterProducts(String type, String value) {
        return switch (type.toLowerCase()) {
            case "category" -> productList.stream()
                    .filter(p -> p.getCategory().equalsIgnoreCase(value))
                    .collect(Collectors.toList());

            case "name" -> productList.stream()
                    .filter(p -> p.getName().toLowerCase().contains(value.toLowerCase()))
                    .collect(Collectors.toList());

            case "price" -> {
                double price = Double.parseDouble(value);
                yield productList.stream()
                        .filter(p -> p.getPrice() <= price)
                        .collect(Collectors.toList());
            }

            default -> new ArrayList<>();
        };
    }
    
}
