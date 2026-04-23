import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import ReportDownload from '../views/ReportDownload.vue' // 確保有這個組件
import { useAuth } from '../stores/auth' // 1. 引入你的 Store

const routes = [
  {
    path: '/', 
    name: 'Home', 
    component: () => import('../views/Home.vue'), // 這裡是你的管理首頁
    meta: { requiresAuth: true }
  },
  { 
    path: '/login', 
    name: 'Login', 
    component: Login 
  },
  { 
    path: '/auth', 
    name: 'Auth', 
    component: () => import('../views/Auth.vue') 
  },
  { 
    path: '/report', 
    name: 'Report', 
    component: () => import('../views/ReportDownload.vue'),
    meta: { requiresAuth: true }
  }
]

// ✨ 關鍵修正：必須先定義 router 實例，才能在下面使用 router.beforeEach
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const auth = useAuth()
  const token = localStorage.getItem('token')
  const isAuthenticated = !!auth.token || (token && token !== 'undefined')
  const isPublicPage = ['Login', 'Auth'].includes(to.name)

  if (!isPublicPage && !isAuthenticated) {
    // 沒登入：去哪都踢回登入頁
    next({ name: 'Login' })
  } else if (isPublicPage && isAuthenticated) {
    // ✨ 修正：已登入時，如果去登入頁，應該導向「首頁」而不是直接衝向「報表」
    // 這樣使用者才有機會看到「系統管理」的首頁
    next({ name: 'Home' }) 
  } else {
    // 正常放行
    next()
  }
})

export default router