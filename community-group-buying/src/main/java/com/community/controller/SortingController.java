package com.community.controller;

import com.community.service.SortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sorting")
@CrossOrigin(origins = "http://localhost:3005")
public class SortingController {
    @Autowired
    private SortingService sortingService;

    // 按商品维度聚合订单数量
    @GetMapping("/aggregate/{communityId}")
    public Map<Long, Integer> aggregateByProduct(@PathVariable String communityId) {
        return sortingService.aggregateByProduct(communityId);
    }
}
