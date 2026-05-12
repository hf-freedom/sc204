package com.community.controller;

import com.community.model.Product;
import com.community.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3005")
public class ProductController {
    @Autowired
    private ProductService productService;

    // 商品上架
    @PostMapping
    public Product addProduct(@RequestBody ProductRequest request) {
        return productService.addProduct(request.getName(), request.getPrice(), request.getStock());
    }
    
    // 商品上架请求参数
    static class ProductRequest {
        private String name;
        private double price;
        private int stock;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public double getPrice() {
            return price;
        }
        
        public void setPrice(double price) {
            this.price = price;
        }
        
        public int getStock() {
            return stock;
        }
        
        public void setStock(int stock) {
            this.stock = stock;
        }
    }

    // 获取所有商品
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 根据ID获取商品
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}
