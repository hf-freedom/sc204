<template>
  <div class="view">
    <h2>订单管理</h2>
    
    <!-- 创建订单表单 -->
    <form @submit.prevent="createOrder">
      <h3>创建订单</h3>
      <div>
        <label>用户ID：</label>
        <input type="text" v-model="newOrder.userId" required>
      </div>
      <div>
        <label>小区ID：</label>
        <input type="text" v-model="newOrder.communityId" required>
      </div>
      <div>
        <label>团长ID：</label>
        <input type="text" v-model="newOrder.leaderId" required>
      </div>
      
      <!-- 订单商品 -->
      <div>
        <h4>商品列表</h4>
        <div v-for="(item, index) in newOrder.items" :key="index">
          <select v-model="item.productId" @change="updateProductInfo(item)">
            <option value="">选择商品</option>
            <option v-for="product in products" :key="product.id" :value="product.id">
              {{ product.name }} - ¥{{ product.price.toFixed(2) }}
            </option>
          </select>
          <input type="number" v-model.number="item.quantity" min="1" required>
          <button type="button" @click="removeItem(index)">删除</button>
        </div>
        <button type="button" @click="addItem">添加商品</button>
      </div>
      <button type="submit">创建订单</button>
    </form>
    
    <!-- 订单列表 -->
    <h3>订单列表</h3>
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
        <tr v-for="order in orders" :key="order.id">
          <td>{{ order.id }}</td>
          <td>{{ order.userId }}</td>
          <td>{{ order.communityId }}</td>
          <td>{{ order.leaderId }}</td>
          <td>{{ order.totalAmount.toFixed(2) }}</td>
          <td>{{ order.status }}</td>
          <td>
            <button v-if="order.status === '待支付'" @click="payOrder(order.id)">支付</button>
            <button v-if="order.status === '待支付'" @click="cancelOrder(order.id)">取消</button>
            <button v-if="order.status === '已支付'" @click="confirmOrder(order.id)">确认</button>
            <button v-if="order.status === '团长确认'" @click="startSorting(order.id)">开始分拣</button>
            <button v-if="order.status === '分拣中'" @click="completeOrder(order.id)">完成</button>
          </td>
        </tr>
      </tbody>
    </table>
    
    <!-- 消息提示 -->
    <div v-if="message" :class="messageType">{{ message }}</div>
  </div>
</template>

<script>
import { orderApi, productApi } from '../api/api';

export default {
  name: 'OrdersView',
  data() {
    return {
      orders: [],
      products: [],
      newOrder: {
        userId: '',
        communityId: '',
        leaderId: '',
        items: [{ productId: '', quantity: 1, productName: '', price: 0 }]
      },
      message: '',
      messageType: ''
    };
  },
  mounted() {
    this.fetchOrders();
    this.fetchProducts();
    // 定时刷新订单列表，每30秒刷新一次
    this.refreshInterval = setInterval(() => {
      this.fetchOrders();
    }, 30000);
  },
  beforeUnmount() {
    // 清除定时器
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval);
    }
  },
  methods: {
    async fetchOrders() {
      try {
        this.orders = await orderApi.getAllOrders();
      } catch (error) {
        console.error('获取订单列表失败:', error);
        this.showMessage('获取订单列表失败', 'error');
      }
    },
    async fetchProducts() {
      try {
        this.products = await productApi.getAllProducts();
      } catch (error) {
        console.error('获取商品列表失败:', error);
        this.showMessage('获取商品列表失败', 'error');
      }
    },
    addItem() {
      this.newOrder.items.push({ productId: '', quantity: 1, productName: '', price: 0 });
    },
    removeItem(index) {
      this.newOrder.items.splice(index, 1);
    },
    updateProductInfo(item) {
      const product = this.products.find(p => p.id === item.productId);
      if (product) {
        item.productName = product.name;
        item.price = product.price;
      }
    },
    async createOrder() {
      try {
        // 过滤掉未选择商品的项
        const validItems = this.newOrder.items.filter(item => item.productId);
        if (validItems.length === 0) {
          this.showMessage('请至少添加一个商品', 'error');
          return;
        }
        
        await orderApi.createOrder(
          this.newOrder.userId,
          this.newOrder.communityId,
          this.newOrder.leaderId,
          validItems
        );
        
        this.showMessage('订单创建成功', 'success');
        this.newOrder = {
          userId: '',
          communityId: '',
          leaderId: '',
          items: [{ productId: '', quantity: 1, productName: '', price: 0 }]
        };
        this.fetchOrders();
      } catch (error) {
        console.error('创建订单失败:', error);
        this.showMessage('创建订单失败', 'error');
      }
    },
    async payOrder(orderId) {
      try {
        await orderApi.payOrder(orderId);
        this.showMessage('订单支付成功', 'success');
        this.fetchOrders();
      } catch (error) {
        console.error('订单支付失败:', error);
        this.showMessage('订单支付失败', 'error');
      }
    },
    async cancelOrder(orderId) {
      try {
        await orderApi.cancelOrder(orderId);
        this.showMessage('订单取消成功', 'success');
        this.fetchOrders();
      } catch (error) {
        console.error('订单取消失败:', error);
        this.showMessage('订单取消失败', 'error');
      }
    },
    async confirmOrder(orderId) {
      try {
        await orderApi.confirmOrder(orderId);
        this.showMessage('订单确认成功', 'success');
        this.fetchOrders();
      } catch (error) {
        console.error('订单确认失败:', error);
        this.showMessage('订单确认失败', 'error');
      }
    },
    async startSorting(orderId) {
      try {
        await orderApi.startSorting(orderId);
        this.showMessage('开始分拣成功', 'success');
        this.fetchOrders();
      } catch (error) {
        console.error('开始分拣失败:', error);
        this.showMessage('开始分拣失败', 'error');
      }
    },
    async completeOrder(orderId) {
      try {
        await orderApi.completeOrder(orderId);
        this.showMessage('订单完成成功', 'success');
        this.fetchOrders();
      } catch (error) {
        console.error('订单完成失败:', error);
        this.showMessage('订单完成失败', 'error');
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
