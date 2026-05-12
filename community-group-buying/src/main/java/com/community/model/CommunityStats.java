package com.community.model;

public class CommunityStats {
    private String communityId;
    private int orderCount;
    private int productSales;
    private int cancelCount;
    private int completedCount;

    // 构造方法
    public CommunityStats() {
    }

    public CommunityStats(String communityId) {
        this.communityId = communityId;
        this.orderCount = 0;
        this.productSales = 0;
        this.cancelCount = 0;
        this.completedCount = 0;
    }

    // getter 和 setter 方法
    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public void incrementOrderCount() {
        this.orderCount++;
    }

    public int getProductSales() {
        return productSales;
    }

    public void setProductSales(int productSales) {
        this.productSales = productSales;
    }

    public void addProductSales(int quantity) {
        this.productSales += quantity;
    }

    public int getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(int cancelCount) {
        this.cancelCount = cancelCount;
    }

    public void incrementCancelCount() {
        this.cancelCount++;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public void incrementCompletedCount() {
        this.completedCount++;
    }

    // 计算取消率
    public double getCancelRate() {
        if (orderCount == 0) return 0;
        return (double) cancelCount / orderCount * 100;
    }

    // 计算履约完成率
    public double getCompletionRate() {
        if (orderCount == 0) return 0;
        return (double) completedCount / orderCount * 100;
    }
}
