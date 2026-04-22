// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import ReportView from '../views/ReportView.vue'
import LoginView from '../views/Login.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    // 對應前端顯示頁面
    path: '/api/report/:reportName',
    name: 'ReportDownload',
    component: ReportView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router