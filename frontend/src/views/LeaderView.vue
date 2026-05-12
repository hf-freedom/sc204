<template>
  <div class="view">
    <h2>团长管理</h2>
    
    <!-- 批量确认订单 -->
    <form @submit.prevent="batchConfirmOrders">
      <h3>批量确认订单</h3>
      <div>
        <label>小区ID：</label>
        <input type="text" v-model="communityId" required>
      </div>
      <button type="submit">批量确认</button>
    </form>
    
    <!-- 小区订单列表 -->
    <h3>小区订单列表</h3>
    <div>
      <label>小区ID：</label>
      <input type="text" v-model="searchCommunityId">
      <button @click="fetchCommunityOrders">查询</button>
    </div>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>用户ID</th>
          <th>小区ID</th>
          <th>团长ID</th>
          <th>总金额</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in communityOrders" :key="order.id">
          <td>{{ order.id }}</td>
          <td>{{ order.userId }}</td>
          <td>{{ order.communityId }}</td>
          <td>{{ order.leaderId }}</td>
          <td>{{ order.totalAmount.toFixed(2) }}</td>
          <td>{{ order.status }}</td>
          <td>
            <button v-if="order.status === '已支付'" @click="confirmOrder(order.id)">确认</button>
          </td>
        </tr>
      </tbody>
    </table>
    
    <!-- 消息提示 -->
    <div v-if="message" :class="messageType">{{ message }}</div>
  </div>
</template>

<script>
import { orderApi } from '../api/api';

export default {
  name: 'LeaderView',
  data() {
    return {
      communityId: '',
      searchCommunityId: '',
      communityOrders: [],
      message: '',
      messageType: ''
    };
  },
  mounted() {
    // 定时刷新订单列表，每30秒刷新一次
    this.refreshInterval = setInterval(() => {
      if (this.searchCommunityId) {
        this.fetchCommunityOrders();
      }
    }, 30000);
  },
  beforeUnmount() {
    // 清除定时器
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval);
    }
  },
  methods: {
    async batchConfirmOrders() {
      try {
        await orderApi.batchConfirmOrders(this.communityId);
        this.showMessage('批量确认订单成功', 'success');
        if (this.searchCommunityId === this.communityId) {
          this.fetchCommunityOrders();
        }
      } catch (error) {
        console.error('批量确认订单失败:', error);
        this.showMessage('批量确认订单失败', 'error');
      }
    },
    async fetchCommunityOrders() {
      if (!this.searchCommunityId) {
        this.showMessage('请输入小区ID', 'error');
        return;
      }
      
      try {
        this.communityOrders = await orderApi.getOrdersByCommunity(this.searchCommunityId);
      } catch (error) {
        console.error('获取小区订单失败:', error);
        this.showMessage('获取小区订单失败', 'error');
      }
    },
    async confirmOrder(orderId) {
      try {
        await orderApi.confirmOrder(orderId);
        this.showMessage('订单确认成功', 'success');
        this.fetchCommunityOrders();
      } catch (error) {
        console.error('订单确认失败:', error);
        this.showMessage('订单确认失败', 'error');
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
