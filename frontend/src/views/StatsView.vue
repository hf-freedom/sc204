<template>
  <div class="view">
    <h2>统计报表</h2>
    
    <!-- 小区统计数据 -->
    <h3>小区统计数据</h3>
    <table>
      <thead>
        <tr>
          <th>小区ID</th>
          <th>订单量</th>
          <th>商品销量</th>
          <th>取消率</th>
          <th>履约完成率</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="stat in stats" :key="stat.communityId">
          <td>{{ stat.communityId }}</td>
          <td>{{ stat.orderCount }}</td>
          <td>{{ stat.productSales }}</td>
          <td>{{ stat.cancelRate.toFixed(2) }}%</td>
          <td>{{ stat.completionRate.toFixed(2) }}%</td>
        </tr>
      </tbody>
    </table>
    
    <!-- 消息提示 -->
    <div v-if="message" :class="messageType">{{ message }}</div>
  </div>
</template>

<script>
import { statsApi } from '../api/api';

export default {
  name: 'StatsView',
  data() {
    return {
      stats: [],
      message: '',
      messageType: ''
    };
  },
  mounted() {
    this.fetchStats();
    // 定时刷新统计数据，每30秒刷新一次
    this.refreshInterval = setInterval(() => {
      this.fetchStats();
    }, 30000);
  },
  beforeUnmount() {
    // 清除定时器
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval);
    }
  },
  methods: {
    async fetchStats() {
      try {
        this.stats = await statsApi.getAllCommunityStats();
      } catch (error) {
        console.error('获取统计数据失败:', error);
        this.showMessage('获取统计数据失败', 'error');
      }
    },
    showMessage(text, type) {
      this.message = text;
      this.messageType = type;
      setTimeout(() => {
        this.message = '';
      }, 3000);
    }
  }
};
</script>
