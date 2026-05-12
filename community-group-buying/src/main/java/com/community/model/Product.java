package com.community.model;

public class Product {
    private Long id;
    private String name;
    private double price;
    private int totalStock; // 总库存
    private int availableStock; // 可售库存
    private int lockedStock; // 锁定库存
    private int soldStock; // 已售库存
    private boolean isOnSale; // 是否上架

    // 构造方法
    public Product() {
    }

    public Product(Long id, String name, double price, int totalStock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.totalStock = totalStock;
        this.availableStock = totalStock;
        this.lockedStock = 0;
        this.soldStock = 0;
        this.isOnSale = true;
    }

    // getter 和 setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public int getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(int totalStock) {
        this.totalStock = totalStock;
        this.availableStock = totalStock - this.lockedStock - this.soldStock;
        // 检查是否需要自动上架
        if (this.availableStock > 0 && !this.isOnSale) {
            this.isOnSale = true;
        }
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public int getLockedStock() {
        return lockedStock;
    }

    public void setLockedStock(int lockedStock) {
        this.lockedStock = lockedStock;
        this.availableStock = this.totalStock - this.lockedStock - this.soldStock;
    }

    public int getSoldStock() {
        return soldStock;
    }

    public void setSoldStock(int soldStock) {
        this.soldStock = soldStock;
        this.availableStock = this.totalStock - this.lockedStock - this.soldStock;
        // 检查是否需要自动下架
        if (this.availableStock <= 0 && this.isOnSale) {
            this.isOnSale = false;
        }
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public void setOnSale(boolean onSale) {
        isOnSale = onSale;
    }
}
