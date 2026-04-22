<template>
  <nav class="nav-bar">
    <button @click="goToReport" class="nav-btn">進入報表下載</button>
  </nav>
  
  <router-view />
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToReport = () => {
  // 使用名稱或路徑跳轉，並可選擇性帶入 Query 參數 (如 itemId)
  router.push({ 
    name: 'ReportDownload', 
    params: { reportName: 'default_report' },
    query: { itemId: '123' }
  })
}
</script>

<style>
body {
  margin: 0;
  padding: 0;
  font-family: sans-serif;
  background-color: #f9fafb;
}

.nav-bar {
  padding: 1rem;
  background: white;
  border-bottom: 1px solid #ddd;
  text-align: center;
}

.nav-btn {
  padding: 8px 16px;
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.nav-btn:hover {
  background-color: #2563eb;
}
</style>