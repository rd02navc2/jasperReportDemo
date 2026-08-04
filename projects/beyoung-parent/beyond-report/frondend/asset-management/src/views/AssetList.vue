<template>
  <div class="p-4">
    <h2 class="text-2xl mb-4">資產盤點管理</h2>
    
    <!-- 篩選條件 -->
    <div class="flex gap-2 mb-4">
      <input v-model="filters.inventory_date" type="date" class="border p-2 rounded" placeholder="盤點日期" />
      <button @click="fetchData" class="bg-blue-500 text-white px-4 py-2 rounded">查詢</button>
      <button @click="submitConfirm" class="bg-green-500 text-white px-4 py-2 rounded">提交確認</button>
    </div>

    <!-- 資產列表 -->
    <table class="w-full border-collapse border border-gray-300">
      <thead>
        <tr class="bg-gray-100">
          <th class="border p-2">自編號</th>
          <th class="border p-2">型號</th>
          <th class="border p-2">資產名稱</th>
          <th class="border p-2">使用人</th>
          <th class="border p-2">確認日期</th>
          <th class="border p-2">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="row in rows" :key="row.self_no">
          <td class="border p-2">{{ row.self_no }}</td>
          <td class="border p-2">{{ row.model_no }}</td>
          <td class="border p-2">{{ row.prod_desc }}</td>
          <td class="border p-2">{{ row.owner_name }}</td>
          <td class="border p-2">{{ row.confirm_date }}</td>
          <td class="border p-2 text-center">
            <button @click="handleScrap(row)" class="text-red-500 mr-2">報廢</button>
            <button @click="handleDelete(row)" class="text-gray-500">刪除</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import assetApi from '../api/asset'
import { ElMessage, ElMessageBox } from 'element-plus' // 假設使用 Element Plus 做提示

const rows = ref([])
const filters = ref({
  inventory_date: '',
  sidx: 'self_no',
  sord: 'ASC'
})

// 取得資料
const fetchData = async () => {
  try {
    const res = await assetApi.getAssetInventory(filters.value)
    if (res.data.Success === 'Y') {
      rows.value = res.data.rows
    } else {
      ElMessage.error(res.data.ErrorMessage || '查詢失敗')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('系統錯誤')
  }
}

// 報廢資產
const handleScrap = (row) => {
  ElMessageBox.confirm(`確定要報廢資產 ${row.self_no} 嗎?`, '警告', { type: 'warning' })
    .then(async () => {
      const payload = {
        inventory_date: row.inventory_date,
        self_no: row.self_no
      }
      const res = await assetApi.scrappedAsset(payload)
      if (res.data.Success === 'Y') {
        ElMessage.success('報廢成功')
        fetchData()
      } else {
        ElMessage.error(res.data.ErrorMessage)
      }
    })
}

// 刪除資產
const handleDelete = (row) => {
  ElMessageBox.confirm(`確定要刪除 ${row.self_no}?`, '確認刪除', { type: 'info' })
    .then(async () => {
      const payload = {
        inventory_date: row.inventory_date,
        self_no: row.self_no
      }
      const res = await assetApi.deleteAsset(payload)
      if (res.data.Success === 'Y') {
        ElMessage.success('刪除成功')
        fetchData()
      } else {
        ElMessage.error(res.data.ErrorMessage)
      }
    })
}

// 提交確認 (對應後端 /submit)
const submitConfirm = async () => {
    // 這裡需要呼叫後端的 submit 接口，但後端代碼中 submit 似乎不需要參數，只取 Session
    // 假設有一個 API 方法叫 submitConfirm
    try {
        const res = await assetApi.submitConfirm() 
        if(res.data.Success === 'Y') {
            ElMessage.success('提交成功')
        }
    } catch(e) { console.error(e) }
}

onMounted(() => {
  fetchData()
})
</script>