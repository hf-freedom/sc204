package com.community.model;

import java.util.Date;
import java.util.List;

public class Order {
    private Long id;
    private String userId;
    private String communityId;
    private String leaderId;
    private List<OrderItem> items;
    private double totalAmount;
    private String status; // 待支付、已支付、团长确认、分拣中、已完成、已取消
    private Date createTime;
    private Date payTime;
    private Date confirmTime;
    private Date cancelTime;

    // 构造方法
    public Order() {
    }

    public Order(Long id, String userId, String communityId, String leaderId, List<OrderItem> items) {
        this.id = id;
        this.userId = userId;
        this.communityId = communityId;
        this.leaderId = leaderId;
        this.items = items;
        this.totalAmount = calculateTotal();
        this.status = "待支付";
        this.createTime = new Date();
    }

    // 计算总金额
    private double calculateTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    // getter 和 setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        this.totalAmount = calculateTotal();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }
}
