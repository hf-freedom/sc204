package com.community.service;

import com.community.model.CommunityStats;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StatsService {
    private Map<String, CommunityStats> communityStatsMap = new ConcurrentHashMap<>();

    // 增加订单数
    public void incrementOrderCount(String communityId) {
        CommunityStats stats = communityStatsMap.computeIfAbsent(communityId, CommunityStats::new);
        stats.incrementOrderCount();
    }

    // 增加商品销量
    public void addProductSales(String communityId, int quantity) {
        CommunityStats stats = communityStatsMap.computeIfAbsent(communityId, CommunityStats::new);
        stats.addProductSales(quantity);
    }

    // 增加取消订单数
    public void incrementCancelCount(String communityId) {
        CommunityStats stats = communityStatsMap.computeIfAbsent(communityId, CommunityStats::new);
        stats.incrementCancelCount();
    }

    // 增加完成订单数
    public void incrementCompletedCount(String communityId) {
        CommunityStats stats = communityStatsMap.computeIfAbsent(communityId, CommunityStats::new);
        stats.incrementCompletedCount();
    }

    // 获取所有小区的统计数据
    public List<CommunityStats> getAllCommunityStats() {
        return new ArrayList<>(communityStatsMap.values());
    }

    // 获取指定小区的统计数据
    public CommunityStats getCommunityStats(String communityId) {
        return communityStatsMap.get(communityId);
    }
}
