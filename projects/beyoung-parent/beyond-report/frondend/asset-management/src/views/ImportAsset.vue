<template>
  <div class="p-4">
    <h2 class="text-2xl mb-4">匯入資產資料 (Excel)</h2>
    
    <div class="border p-6 rounded bg-gray-50">
      <input type="file" @change="onFileChange" accept=".xlsx, .xls" class="mb-4" />
      <button 
        @click="handleUpload" 
        :disabled="!selectedFile"
        class="bg-indigo-600 text-white px-4 py-2 rounded disabled:bg-gray-400"
      >
        上傳並解析
      </button>
    </div>

    <!-- 預覽解析後的資料 -->
    <div v-if="previewData.length > 0" class="mt-8">
      <h3 class="text-xl mb-2">預覽資料 (共 {{ previewData.length }} 筆)</h3>
      <button @click="handleSave" class="bg-green-600 text-white px-4 py-2 rounded mb-4">確認儲存至資料庫</button>
      
      <table class="w-full border-collapse border border-gray-300 text-sm">
        <thead>
          <tr class="bg-gray-100">
            <th class="border p-2">自編號</th>
            <th class="border p-2">型號</th>
            <th class="border p-2">使用人</th>
            <th class="border p-2">部門</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, index) in previewData" :key="index">
            <td class="border p-2">{{ row.self_no }}</td>
            <td class="border p-2">{{ row.model_no }}</td>
            <td class="border p-2">{{ row.owner_name }}</td>
            <td class="border p-2">{{ row.dept_name }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import assetApi from '../api/asset'
import { ElMessage } from 'element-plus'

const selectedFile = ref(null)
const previewData = ref([])

const onFileChange = (e) => {
  selectedFile.value = e.target.files[0]
}

const handleUpload = async () => {
  if (!selectedFile.value) return

  const formData = new FormData()
  formData.append('file', selectedFile.value)

  try {
    const res = await assetApi.uploadExcel(formData)
    if (res.data.Success === 'Y') {
      previewData.value = res.data.rows // 後端回傳解析後的 rows
      ElMessage.success('檔案上傳並解析成功')
    } else {
      ElMessage.error(res.data.ErrorMessage)
    }
  } catch (error) {
    ElMessage.error('上傳失敗')
  }
}

const handleSave = async () => {
  try {
    // 後端 saveImport 需要 { asset_inventory_data: [...] }
    const payload = { asset_inventory_data: previewData.value }
    const res = await assetApi.saveImport(payload)
    
    if (res.data.Success === 'Y') {
      ElMessage.success('資料已儲存')
      previewData.value = []
    } else {
      ElMessage.error(res.data.ErrorMessage)
    }
  } catch (error) {
    ElMessage.error('儲存失敗')
  }
}
</script>