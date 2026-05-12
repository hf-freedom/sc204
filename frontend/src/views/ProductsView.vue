<template>
  <div class="view">
    <h2>商品管理</h2>
    
    <!-- 商品上架表单 -->
    <form @submit.prevent="addProduct">
      <h3>商品上架</h3>
      <div>
        <label>商品名称：</label>
        <input type="text" v-model="newProduct.name" required>
      </div>
      <div>
        <label>价格：</label>
        <input type="number" v-model.number="newProduct.price" required step="0.01">
      </div>
      <div>
        <label>库存：</label>
        <input type="number" v-model.number="newProduct.stock" required min="1">
      </div>
      <button type="submit">上架商品</button>
    </form>
    
    <!-- 商品列表 -->
    <h3>商品列表</h3>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>商品名称</th>
          <th>价格</th>
          <th>总库存</th>
          <th>可售库存</th>
          <th>锁定库存</th>
          <th>已售库存</th>
          <th>状态</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="product in products" :key="product.id">
          <td>{{ product.id }}</td>
          <td>{{ product.name }}</td>
          <td>{{ product.price.toFixed(2) }}</td>
          <td>{{ product.totalStock }}</td>
          <td>{{ product.availableStock }}</td>
          <td>{{ product.lockedStock }}</td>
          <td>{{ product.soldStock }}</td>
          <td>{{ product.isOnSale ? '上架' : '下架' }}</td>
        </tr>
      </tbody>
    </table>
    
    <!-- 消息提示 -->
    <div v-if="message" :class="messageType">{{ message }}</div>
  </div>
</template>

<script>
import { productApi } from '../api/api';

export default {
  name: 'ProductsView',
  data() {
    return {
      products: [],
      newProduct: {
        name: '',
        price: 0,
        stock: 0
      },
      message: '',
      messageType: ''
    };
  },
  mounted() {
    this.fetchProducts();
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
    async addProduct() {
      try {
        await productApi.addProduct(this.newProduct.name, this.newProduct.price, this.newProduct.stock);
        this.showMessage('商品上架成功', 'success');
        this.newProduct = { name: '', price: 0, stock: 0 };
        this.fetchProducts();
      } catch (error) {
        console.error('商品上架失败:', error);
        this.showMessage('商品上架失败', 'error');
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
