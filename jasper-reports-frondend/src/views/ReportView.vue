<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-50">
    <div class="text-center p-8 bg-white shadow-lg rounded-lg max-w-lg w-full">
      <div class="report-container">
        <h2 class="text-2xl font-bold mb-2 text-gray-800">Jasper Report 下載中心</h2>
        <p class="text-gray-500 mb-6">
          當前報表：<span class="font-mono text-blue-600">{{ reportName }}</span>
        </p>

        <div class="button-group">
          <button @click="download('pdf')" class="btn btn-pdf">
            <span class="icon">📄</span> Download PDF
          </button>
          <button @click="download('excel')" class="btn btn-excel">
            <span class="icon">📊</span> Download Excel
          </button>
          <button @click="download('csv')" class="btn btn-csv">
            <span class="icon">📑</span> Download CSV
          </button>
        </div>

        <div class="mt-8">
          <button @click="$router.push('/login')" class="text-sm text-gray-400 hover:text-gray-600 underline">
            登出或返回登入
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

// 取得路徑中的報表名稱
const reportName = computed(() => route.params.reportName || 'default_report')

const download = (format) => {
  // 1. 基於你的後端 @RequestMapping("/api/reports")
  const baseUrl = '/api/reports' 
  
  // 2. 取得 Query 參數 (如 itemId)
  const itemId = route.query.itemId || ''
  const queryParams = itemId ? `?itemId=${itemId}` : ''

  // 3. 組合出正確的後端 API 路徑
  // 最終：/api/reports/item-report/pdf?itemId=xxx
  const downloadUrl = `${baseUrl}/item-report/${format}${queryParams}`

  console.log(`正在請求 API: ${downloadUrl}`)
  
  // 4. 使用 a 標籤觸發下載 (最穩定)
  const link = document.createElement('a')
  link.href = downloadUrl
  link.setAttribute('download', `report_${reportName.value}.${format}`)
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}
</script>

<style scoped>
/* 這裡保留你上傳的 ReportView.vue 樣式即可 */
.report-container { padding: 10px; }
.button-group { display: flex; flex-direction: column; gap: 12px; }
@media (min-width: 480px) { .button-group { flex-direction: row; justify-content: center; } }
.btn {
  display: flex; align-items: center; justify-content: center;
  padding: 12px 20px; border-radius: 8px; border: 1px solid #e2e8f0;
  background: white; cursor: pointer; transition: 0.2s;
}
.btn:hover { transform: translateY(-2px); box-shadow: 0 4px 6px rgba(0,0,0,0.1); border-color: #3b82f6; }
.icon { margin-right: 8px; }
</style>