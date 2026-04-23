import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import ReportDownload from '../views/ReportDownload.vue' // 確保有這個組件
import { useAuth } from '../stores/auth' // 1. 引入你的 Store

const routes = [
  { path: '/', redirect: '/login' },
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
  
  // 檢查登入狀態 (加上防呆，確保 token 不是字串 "undefined")
  const token = localStorage.getItem('token')
  const isAuthenticated = !!auth.token || (token && token !== 'undefined')

  // 🚀 關鍵：判斷目標頁面是否在「白名單」中
  // 這裡的名稱必須跟下面 routes 裡的 name: 'Auth' 一模一樣
  const isPublicPage = ['Login', 'Auth'].includes(to.name)

  if (!isPublicPage && !isAuthenticated) {
    // 情況 A：要去受保護頁面（如 Report）但沒登入 -> 踢回登入頁
    next({ name: 'Login' })
  } else if (isPublicPage && isAuthenticated) {
    // 情況 B：已經登入卻想去登入/註冊頁 -> 直接送去報表頁
    next({ name: 'Report' })
  } else {
    // 情況 C：要去註冊頁且沒登入，或是正常的頁面跳轉 -> 放行
    next()
  }
})

export default router