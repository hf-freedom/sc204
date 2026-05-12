package com.community.service;

import com.community.model.Order;
import com.community.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SortingService {
    @Autowired
    private OrderService orderService;

    // 按商品维度聚合订单数量
    public Map<Long, Integer> aggregateByProduct(String communityId) {
        List<Order> orders = orderService.getOrdersByCommunity(communityId);
        Map<Long, Integer> productQuantityMap = new HashMap<>();

        for (Order order : orders) {
            if ("团长确认".equals(order.getStatus()) || "分拣中".equals(order.getStatus())) {
                for (OrderItem item : order.getItems()) {
                    productQuantityMap.put(item.getProductId(), 
                        productQuantityMap.getOrDefault(item.getProductId(), 0) + item.getQuantity());
                }
            }
        }

        return productQuantityMap;
    }
}
