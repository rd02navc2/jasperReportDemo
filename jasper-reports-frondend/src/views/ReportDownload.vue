<template>
  <div class="wrapper">

    <h2 class="title">報表下載中心</h2>
    <p class="subtitle">選擇格式下載庫存報表</p>

    <div class="card">

      <div class="info">
        <div class="tag">即時資料</div>
        <h3>庫存品項明細報表</h3>
        <p class="desc">包含品項、庫存量、儲位&emsp;&emsp;</p>
        <small>ItemRepository</small>
      </div>

      <div class="actions">

        <!-- PDF -->
        <button
          @click="downloadReport('pdf')"
          :disabled="loadingFormat === 'pdf'"
          class="btn pdf"
        >
          <span v-if="loadingFormat === 'pdf'">下載中...</span>
          <span v-else>PDF</span>
        </button>

        <!-- Excel -->
        <button
          @click="downloadReport('excel')"
          :disabled="loadingFormat === 'excel'"
          class="btn excel"
        >
          <span v-if="loadingFormat === 'excel'">下載中...</span>
          <span v-else>Excel</span>
        </button>

      </div>

    </div>

    <!-- error -->
    <p v-if="errorMsg" class="error">
      {{ errorMsg }}
    </p>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const loadingFormat = ref(null)
const errorMsg = ref('')

const downloadReport = async (format) => {
  try {
    loadingFormat.value = format
    errorMsg.value = ''

    const token = localStorage.getItem('token')

    const res = await axios.get('/api/reports/item-report', {
      params: { format },
      responseType: 'blob',
      headers: token ? {
        Authorization: `Bearer ${token}`
      } : {}
    })
    
    // MIME mapping
    const mimeMap = {
      pdf: 'application/pdf',
      excel: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    }

    const fileMap = {
      pdf: 'item-report.pdf',
      excel: 'item-report.xlsx'
    }

    const blob = new Blob([res.data], {
      type: mimeMap[format]
    })

    const url = window.URL.createObjectURL(blob)

    const a = document.createElement('a')
    a.href = url
    a.download = fileMap[format]
    a.click()

    window.URL.revokeObjectURL(url)


  } catch (err) {
    console.error('下載錯誤:', err.response || err)

    if (err.response?.status === 403) {
      errorMsg.value = '未授權（請重新登入）'
    } else {
      errorMsg.value = '下載失敗'
    }
  } finally {
    loadingFormat.value = null
  }
}

</script>

<style scoped>
.wrapper {
  max-width: 720px;
  margin: 60px auto;
  font-family: Arial, sans-serif;
  color: #111827;
}

.title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 6px;
}

.subtitle {
  color: #6b7280;
  margin-bottom: 20px;
}

.card {
  border: 1px solid #e5e7eb;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tag {
  font-size: 12px;
  color: #059669;
  margin-bottom: 6px;
}

h3 {
  margin: 0;
  font-size: 18px;
}

.desc {
  font-size: 14px;
  color: #6b7280;
}

small {
  color: #9ca3af;
}

.actions {
  display: flex;
  gap: 10px;
}

.btn {
  padding: 8px 14px;
  border: none;
  cursor: pointer;
  font-size: 14px;
  color: white;
  min-width: 90px;
}

.pdf {
  background: #ef4444;
}

.excel {
  background: #3b82f6;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.error {
  margin-top: 12px;
  color: #dc2626;
  font-size: 14px;
}

.desc {
    white-space: pre-wrap; /* 保留空格與 Tab，且支援自動換行 */
}

</style>