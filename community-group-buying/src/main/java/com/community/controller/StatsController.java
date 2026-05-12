package com.community.controller;

import com.community.model.CommunityStats;
import com.community.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "http://localhost:3005")
public class StatsController {
    @Autowired
    private StatsService statsService;

    // 获取所有小区的统计数据
    @GetMapping
    public List<CommunityStats> getAllCommunityStats() {
        return statsService.getAllCommunityStats();
    }

    // 获取指定小区的统计数据
    @GetMapping("/{communityId}")
    public CommunityStats getCommunityStats(@PathVariable String communityId) {
        return statsService.getCommunityStats(communityId);
    }
}
