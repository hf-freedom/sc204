package com.community.service;

import com.community.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProductService {
    private Map<Long, Product> products = new ConcurrentHashMap<>();
    private long nextId = 1;

    // 商品上架
    public Product addProduct(String name, double price, int stock) {
        Product product = new Product(nextId++, name, price, stock);
        products.put(product.getId(), product);
        return product;
    }

    // 获取所有商品
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    // 根据ID获取商品
    public Product getProductById(Long id) {
        return products.get(id);
    }

    // 更新商品库存
    public void updateStock(Long productId, int quantity, boolean isLock) {
        Product product = products.get(productId);
        if (product != null) {
            if (isLock) {
                product.setLockedStock(product.getLockedStock() + quantity);
            } else {
                product.setSoldStock(product.getSoldStock() + quantity);
                product.setLockedStock(product.getLockedStock() - quantity);
            }
        }
    }

    // 释放库存
    public void releaseStock(Long productId, int quantity) {
        Product product = products.get(productId);
        if (product != null) {
            product.setLockedStock(product.getLockedStock() - quantity);
        }
    }

    // 检查库存是否充足
    public boolean checkStock(Long productId, int quantity) {
        Product product = products.get(productId);
        return product != null && product.getAvailableStock() >= quantity && product.isOnSale();
    }
}
