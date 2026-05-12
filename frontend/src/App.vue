<template>
  <div class="app">
    <header class="header">
      <h1>社区团购订单履约系统</h1>
      <nav class="nav">
        <ul>
          <li><a href="#" @click.prevent="activeView = 'products'">商品管理</a></li>
          <li><a href="#" @click.prevent="activeView = 'orders'">订单管理</a></li>
          <li><a href="#" @click.prevent="activeView = 'leader'">团长管理</a></li>
          <li><a href="#" @click.prevent="activeView = 'sorting'">分拣管理</a></li>
          <li><a href="#" @click.prevent="activeView = 'stats'">统计报表</a></li>
        </ul>
      </nav>
    </header>
    <main class="main">
      <ProductsView v-if="activeView === 'products'" />
      <OrdersView v-if="activeView === 'orders'" />
      <LeaderView v-if="activeView === 'leader'" />
      <SortingView v-if="activeView === 'sorting'" />
      <StatsView v-if="activeView === 'stats'" />
    </main>
  </div>
</template>

<script>
import ProductsView from './views/ProductsView.vue'
import OrdersView from './views/OrdersView.vue'
import LeaderView from './views/LeaderView.vue'
import SortingView from './views/SortingView.vue'
import StatsView from './views/StatsView.vue'

export default {
  name: 'App',
  components: {
    ProductsView,
    OrdersView,
    LeaderView,
    SortingView,
    StatsView
  },
  data() {
    return {
      activeView: 'products'
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.app {
  font-family: Arial, sans-serif;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background-color: #4CAF50;
  color: white;
  padding: 20px;
  text-align: center;
}

.nav ul {
  display: flex;
  justify-content: center;
  list-style: none;
  margin-top: 10px;
}

.nav li {
  margin: 0 10px;
}

.nav a {
  color: white;
  text-decoration: none;
  padding: 5px 10px;
  border-radius: 3px;
  transition: background-color 0.3s;
}

.nav a:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

.main {
  flex: 1;
  padding: 20px;
  background-color: #f5f5f5;
}

.view {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h2 {
  margin-bottom: 20px;
  color: #333;
}

form {
  margin-bottom: 20px;
}

input, select, button {
  padding: 8px;
  margin: 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  background-color: #4CAF50;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #45a049;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
}

tr:hover {
  background-color: #f5f5f5;
}

.success {
  color: green;
  margin: 10px 0;
}

.error {
  color: red;
  margin: 10px 0;
}
</style>
