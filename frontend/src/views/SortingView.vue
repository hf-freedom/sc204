<template>
  <div class="view">
    <h2>分拣管理</h2>
    
    <!-- 按商品维度聚合订单 -->
    <div>
      <h3>按商品维度聚合订单</h3>
      <div>
        <label>小区ID：</label>
        <input type="text" v-model="communityId">
        <button @click="aggregateByProduct">聚合</button>
      </div>
    </div>
    
    <!-- 聚合结果 -->
    <h3>分拣清单</h3>
    <table v-if="aggregatedProducts.length > 0">
      <thead>
        <tr>
          <th>商品ID</th>
          <th>商品名称</th>
          <th>总数量</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in aggregatedProducts" :key="item.productId">
          <td>{{ item.productId }}</td>
          <td>{{ item.productName }}</td>
          <td>{{ item.quantity }}</td>
        </tr>
      </tbody>
    </table>
    <div v-else class="empty">暂无分拣数据</div>
    
    <!-- 消息提示 -->
    <div v-if="message" :class="messageType">{{ message }}</div>
  </div>
</template>

<script>
import { sortingApi, productApi } from '../api/api';

export default {
  name: 'SortingView',
  data() {
    return {
      communityId: '',
      products: [],
      aggregatedProducts: [],
      message: '',
      messageType: ''
    };
  },
  mounted() {
    this.fetchProducts();
    // 定时刷新商品列表，每30秒刷新一次
    this.refreshInterval = setInterval(() => {
      this.fetchProducts();
      if (this.communityId) {
        this.aggregateByProduct();
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
    async fetchProducts() {
      try {
        this.products = await productApi.getAllProducts();
      } catch (error) {
        console.error('获取商品列表失败:', error);
        this.showMessage('获取商品列表失败', 'error');
      }
    },
    async aggregateByProduct() {
      if (!this.communityId) {
        this.showMessage('请输入小区ID', 'error');
        return;
      }
      
      try {
        const aggregated = await sortingApi.aggregateByProduct(this.communityId);
        // 转换为包含商品名称的数组
        this.aggregatedProducts = Object.entries(aggregated).map(([productId, quantity]) => {
          const product = this.products.find(p => p.id == productId);
          return {
            productId: productId,
            productName: product ? product.name : '未知商品',
            quantity: quantity
          };
        });
        
        if (this.aggregatedProducts.length === 0) {
          this.showMessage('该小区暂无需要分拣的订单', 'info');
        }
      } catch (error) {
        console.error('聚合订单失败:', error);
        this.showMessage('聚合订单失败', 'error');
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

<style scoped>
.empty {
  margin: 20px 0;
  color: #999;
  text-align: center;
}
</style>
