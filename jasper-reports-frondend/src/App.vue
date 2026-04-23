<template>
  <div id="app" class="app-wrapper">
    <nav v-if="isLoggedIn" class="nav-bar">
      <div class="nav-container">
        <span class="brand">系統管理</span>
        <div class="nav-links">
          <button @click="goToReport" class="nav-btn">進入報表下載</button>
          <button @click="handleLogout" class="logout-btn">登出</button>
        </div>
      </div>
    </nav>

    <main :class="['main-content', { 'center-content': !isLoggedIn }]">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'
import { computed, onMounted } from 'vue'
import { useAuth } from './stores/auth'

const auth = useAuth()
const router = useRouter()
const route = useRoute()

// 確保重新整理時能讀取到 token
onMounted(() => {
  auth.restoreAuth()
})

/**
 * 判斷是否登入：
 * 1. Store 裡必須有 token
 * 2. 排除 login 和 auth 頁面，避免導覽列出現在註冊/登入頁
 */
const isLoggedIn = computed(() => {
  const hiddenPages = ['/login', '/auth']
  return !!auth.token && !hiddenPages.includes(route.path)
})

const goToReport = () => {
  router.push('/report')
}

const handleLogout = () => {
  auth.logout() // 統一由 store 處理狀態與緩存清理
  router.push('/login')
}
</script>

<style>
/* 全域基礎設定 */
html, body, #app {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: sans-serif;
}

.app-wrapper {
  display: flex;
  flex-direction: column;
  min-h-screen: 100vh; /* 確保佔滿全螢幕高度 */
}

/* 導覽列樣式 */
.nav-bar {
  background-color: #333;
  color: white;
  padding: 0 20px;
  height: 60px; /* 固定導覽列高度 */
  display: flex;
  align-items: center;
  z-index: 100;
}

.nav-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

/* 內容區塊設定 */
.main-content {
  flex: 1; /* 自動填滿剩餘高度 */
  display: flex;
  flex-direction: column;
}

/* 當未登入時，讓 router-view 的內容置中 */
.center-content {
  justify-content: center;
  align-items: center;
  background-color: #f3f4f6; /* 登入頁背景色 */
}

.nav-btn {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 10px;
}

.logout-btn {
  background: transparent;
  color: #ccc;
  border: 1px solid #555;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.brand {
  font-weight: bold;
  font-size: 1.2rem;
}
</style>