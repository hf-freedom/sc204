package com.community.service;

import com.community.model.Order;
import com.community.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {
    private Map<Long, Order> orders = new ConcurrentHashMap<>();
    private long nextId = 1;

    @Autowired
    private ProductService productService;

    @Autowired
    private StatsService statsService;

    // 创建订单
    public Order createOrder(String userId, String communityId, String leaderId, List<OrderItem> items) {
        // 检查库存
        for (OrderItem item : items) {
            if (!productService.checkStock(item.getProductId(), item.getQuantity())) {
                throw new RuntimeException("商品库存不足");
            }
        }

        // 锁定库存
        for (OrderItem item : items) {
            productService.updateStock(item.getProductId(), item.getQuantity(), true);
        }

        // 创建订单
        Order order = new Order(nextId++, userId, communityId, leaderId, items);
        orders.put(order.getId(), order);

        // 更新统计
        statsService.incrementOrderCount(communityId);

        return order;
    }

    // 支付订单
    public Order payOrder(Long orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!"待支付".equals(order.getStatus())) {
            throw new RuntimeException("订单状态错误");
        }

        // 更新订单状态
        order.setStatus("已支付");
        order.setPayTime(new Date());

        // 确认库存
        for (OrderItem item : order.getItems()) {
            productService.updateStock(item.getProductId(), item.getQuantity(), false);
            statsService.addProductSales(order.getCommunityId(), item.getQuantity());
        }

        return order;
    }

    // 取消订单
    public Order cancelOrder(Long orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if ("已取消".equals(order.getStatus())) {
            throw new RuntimeException("订单已取消");
        }

        if ("分拣中".equals(order.getStatus()) || "已完成".equals(order.getStatus())) {
            throw new RuntimeException("订单已分拣，无法取消");
        }

        // 更新订单状态
        order.setStatus("已取消");
        order.setCancelTime(new Date());

        // 释放库存
        for (OrderItem item : order.getItems()) {
            productService.releaseStock(item.getProductId(), item.getQuantity());
        }

        // 更新统计
        statsService.incrementCancelCount(order.getCommunityId());

        return order;
    }

    // 团长确认订单
    public Order confirmOrder(Long orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!"已支付".equals(order.getStatus())) {
            throw new RuntimeException("订单状态错误");
        }

        // 更新订单状态
        order.setStatus("团长确认");
        order.setConfirmTime(new Date());

        return order;
    }

    // 批量确认订单
    public List<Order> batchConfirmOrders(String communityId) {
        List<Order> confirmedOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (communityId.equals(order.getCommunityId()) && "已支付".equals(order.getStatus())) {
                confirmOrder(order.getId());
                confirmedOrders.add(order);
            }
        }
        return confirmedOrders;
    }

    // 开始分拣
    public Order startSorting(Long orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!"团长确认".equals(order.getStatus())) {
            throw new RuntimeException("订单状态错误");
        }

        // 更新订单状态
        order.setStatus("分拣中");

        return order;
    }

    // 完成订单
    public Order completeOrder(Long orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!"分拣中".equals(order.getStatus())) {
            throw new RuntimeException("订单状态错误");
        }

        // 更新订单状态
        order.setStatus("已完成");

        // 更新统计
        statsService.incrementCompletedCount(order.getCommunityId());

        return order;
    }

    // 获取订单
    public Order getOrderById(Long id) {
        return orders.get(id);
    }

    // 获取所有订单
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    // 按小区获取订单
    public List<Order> getOrdersByCommunity(String communityId) {
        List<Order> communityOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (communityId.equals(order.getCommunityId())) {
                communityOrders.add(order);
            }
        }
        return communityOrders;
    }

    // 定时任务：取消超时未支付订单
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void cancelTimeoutOrders() {
        long timeout = 30 * 60 * 1000; // 30分钟超时
        long now = System.currentTimeMillis();

        for (Order order : orders.values()) {
            if ("待支付".equals(order.getStatus()) && now - order.getCreateTime().getTime() > timeout) {
                cancelOrder(order.getId());
            }
        }
    }
}
