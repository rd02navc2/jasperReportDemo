import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '@/views/Dashboard.vue'
import AssetInventory from '@/views/AssetInventory.vue'
import MyAssets from '@/views/MyAssets.vue'
import ScrappedAssets from '@/views/ScrappedAssets.vue'
// 1. 引用你的 Login 組件（請確認檔名與路徑是否為 @/views/Login.vue）
import Login from '@/views/Login.vue' 

const routes = [
  { path: '/', redirect: '/dashboard' },
  // 2. 補上 /login 路由
  { path: '/login', component: Login, meta: { title: '登录' } }, 
  { path: '/dashboard', component: Dashboard, meta: { title: '盘点总览' } },
  { path: '/inventory', component: AssetInventory, meta: { title: '资产盘点明细' } },
  { path: '/my-assets', component: MyAssets, meta: { title: '我的资产' } },
  { path: '/scrapped', component: ScrappedAssets, meta: { title: '报废资产' } },
  // 3. (可選) 建議加上 404 通配符，避免使用者輸入未知網址時直接崩潰
  { path: '/:pathMatch(.*)*', redirect: '/login' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 4. (可選) 自動動態修改瀏覽器頁面標題 (Title)
// router/index.js
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = `${to.meta.title}`
  }

  // 檢查是否有 token 或 user 登入資訊
  const token = localStorage.getItem('token')
  const user = localStorage.getItem('user')
  const isAuthenticated = token || user

  // 如果目標頁面不是 /login 且未登入，強制重定向到 /login
  if (to.path !== '/login' && !isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})

export default router