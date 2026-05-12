package com.community.controller;

import com.community.model.Order;
import com.community.model.OrderItem;
import com.community.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3005")
public class OrderController {
    @Autowired
    private OrderService orderService;

    // 创建订单
    @PostMapping
    public Order createOrder(@RequestBody OrderRequest request) {
        return orderService.createOrder(request.getUserId(), request.getCommunityId(), request.getLeaderId(), request.getItems());
    }
    
    // 订单请求参数
    static class OrderRequest {
        private String userId;
        private String communityId;
        private String leaderId;
        private List<OrderItem> items;
        
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
        }
    }

    // 支付订单
    @PostMapping("/{id}/pay")
    public Order payOrder(@PathVariable Long id) {
        return orderService.payOrder(id);
    }

    // 取消订单
    @PostMapping("/{id}/cancel")
    public Order cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    // 团长确认订单
    @PostMapping("/{id}/confirm")
    public Order confirmOrder(@PathVariable Long id) {
        return orderService.confirmOrder(id);
    }

    // 批量确认订单
    @PostMapping("/batch-confirm")
    public List<Order> batchConfirmOrders(@RequestParam String communityId) {
        return orderService.batchConfirmOrders(communityId);
    }

    // 开始分拣
    @PostMapping("/{id}/sort")
    public Order startSorting(@PathVariable Long id) {
        return orderService.startSorting(id);
    }

    // 完成订单
    @PostMapping("/{id}/complete")
    public Order completeOrder(@PathVariable Long id) {
        return orderService.completeOrder(id);
    }

    // 获取订单
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // 获取所有订单
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // 按小区获取订单
    @GetMapping("/community/{communityId}")
    public List<Order> getOrdersByCommunity(@PathVariable String communityId) {
        return orderService.getOrdersByCommunity(communityId);
    }
}
