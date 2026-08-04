<template>
  <div class="dashboard">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>盤點日期列表</span>
          <div>
            <el-button type="primary" @click="handleNewAsset">發起新盤點</el-button>
            <el-button type="warning" @click="handleInform">發送通知</el-button>
          </div>
        </div>
      </template>

      <!-- 綁定 Pinia Store 中的盤點日期列表 -->
      <el-table :data="store.inventoryDates" border style="width: 100%">
        <el-table-column prop="inventory_date" label="盤點日期" />
        <el-table-column prop="generate_type" label="生成方式" />
        <el-table-column prop="close_date" label="關閉日期" />
        <el-table-column prop="remark" label="備註" />
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button size="small" @click="goToInventory(row.inventory_date)">查看明細</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAssetStore } from '@/store/assetStore'
import { assetApi } from '@/api/asset'
import { ElMessage } from 'element-plus'

const store = useAssetStore()
const router = useRouter()

// 1. 初始化載入資料（加上 try...catch 捕捉 401 錯誤）
const initData = async () => {
  try {
    await store.fetchInventoryDates()
  } catch (error) {
    console.error('載入盤點日期失敗:', error)
    if (error.response?.status === 401) {
      ElMessage.warning('尚未登入或登入逾時，請先進行登入')
      // router.push('/login') // 💡 若有登入頁，可解除註解自動跳轉
    } else {
      ElMessage.error('載入盤點資料失敗，請確認後端服務狀態')
    }
  }
}

onMounted(() => {
  initData()
})

// 2. 查看明細跳轉
const goToInventory = (date) => {
  router.push({ path: '/inventory', query: { date } })
}

// 3. 發起新盤點
const handleNewAsset = async () => {
  try {
    const res = await assetApi.newAsset()
    if (res.Success === 'Y') {
      ElMessage.success('盤點發起成功')
      initData() // 重新載入列表
    } else {
      ElMessage.error(res.ErrorMessage || '發起失敗')
    }
  } catch (error) {
    console.error('發起失敗:', error)
    ElMessage.error('連線後端失敗')
  }
}

// 4. 發送通知
const handleInform = async () => {
  try {
    const res = await assetApi.inform()
    if (res.Success === 'Y') {
      ElMessage.success('通知已發送')
    } else {
      ElMessage.error(res.ErrorMessage || '發送失敗')
    }
  } catch (error) {
    console.error('發送失敗:', error)
    ElMessage.error('連線後端失敗')
  }
}
</script>

<style scoped>
.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>