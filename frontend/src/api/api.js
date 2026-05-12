const API_BASE_URL = 'http://localhost:8005/api';

// 通用请求方法
async function request(url, options = {}) {
  const response = await fetch(`${API_BASE_URL}${url}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options.headers
    }
  });
  
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }
  
  return response.json();
}

// 商品相关 API
export const productApi = {
  // 获取所有商品
  getAllProducts: () => request('/products'),
  
  // 商品上架
  addProduct: (name, price, stock) => request('/products', {
    method: 'POST',
    body: JSON.stringify({ name, price, stock })
  })
};

// 订单相关 API
export const orderApi = {
  // 创建订单
  createOrder: (userId, communityId, leaderId, items) => request('/orders', {
    method: 'POST',
    body: JSON.stringify({ userId, communityId, leaderId, items })
  }),
  
  // 支付订单
  payOrder: (orderId) => request(`/orders/${orderId}/pay`, {
    method: 'POST'
  }),
  
  // 取消订单
  cancelOrder: (orderId) => request(`/orders/${orderId}/cancel`, {
    method: 'POST'
  }),
  
  // 团长确认订单
  confirmOrder: (orderId) => request(`/orders/${orderId}/confirm`, {
    method: 'POST'
  }),
  
  // 批量确认订单
  batchConfirmOrders: (communityId) => request('/orders/batch-confirm', {
    method: 'POST',
    body: JSON.stringify({ communityId })
  }),
  
  // 开始分拣
  startSorting: (orderId) => request(`/orders/${orderId}/sort`, {
    method: 'POST'
  }),
  
  // 完成订单
  completeOrder: (orderId) => request(`/orders/${orderId}/complete`, {
    method: 'POST'
  }),
  
  // 获取所有订单
  getAllOrders: () => request('/orders'),
  
  // 按小区获取订单
  getOrdersByCommunity: (communityId) => request(`/orders/community/${communityId}`)
};

// 统计相关 API
export const statsApi = {
  // 获取所有小区的统计数据
  getAllCommunityStats: () => request('/stats'),
  
  // 获取指定小区的统计数据
  getCommunityStats: (communityId) => request(`/stats/${communityId}`)
};

// 分拣相关 API
export const sortingApi = {
  // 按商品维度聚合订单数量
  aggregateByProduct: (communityId) => request(`/sorting/aggregate/${communityId}`)
};
