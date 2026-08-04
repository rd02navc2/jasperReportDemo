<template>
  <div class="asset-inventory">
    <el-card>
      <template #header>
        <div class="header-actions">
          <span>盘点明细 - {{ inventoryDate || '未选择日期' }}</span>
          <div>
            <el-button type="success" @click="handleSave">保存</el-button>
            <el-button type="danger" @click="handleDeleteAll">删除全部</el-button>
            <el-button @click="showImportDialog = true">导入Excel</el-button>
            <el-button :loading="loading" @click="fetchData">刷新</el-button>
          </div>
        </div>
      </template>

      <el-table :data="assetList" border v-loading="loading">
        <el-table-column prop="self_no" label="资产编号" width="150" />
        <el-table-column prop="model_no" label="型号" width="150" />
        <el-table-column prop="type" label="类型" width="150" />
        <el-table-column prop="prod_desc" label="描述" />
        <el-table-column prop="owner_name" label="持有人" width="100" />
        <el-table-column prop="dept_name" label="部门" width="120" />
        <el-table-column label="确认人" width="100">
          <template #default="{ row }">
            <el-input v-model="row.confirm_id" size="small" placeholder="确认人" />
          </template>
        </el-table-column>
        <el-table-column label="确认日期" width="180">
          <template #default="{ row }">
            <el-date-picker 
              v-model="row.confirm_date" 
              type="datetime" 
              placeholder="确认日期"
              value-format="YYYY-MM-DD HH:mm:ss" 
              size="small" 
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
            <el-button size="small" type="warning" @click="handleScrapped(row)">报废</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 修正 1：傳遞當前盤點日期至彈窗，並接收匯入成功回傳的實際日期 -->
      <ImportDialog 
        v-model="showImportDialog" 
        :inventory-date="inventoryDate"
        @import-success="handleImportSuccess" 
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAssetStore } from '@/store/assetStore'
import { assetApi } from '@/api/asset'
import { ElMessage, ElMessageBox } from 'element-plus'
import ImportDialog from '@/views/ImportDialog.vue'

const route = useRoute()
const store = useAssetStore()

// 若 URL 無 query.date，給予預設值
const defaultDate = new Date().toISOString().split('T')[0]
const inventoryDate = ref(route.query.date || defaultDate)

const assetList = computed(() => store.assetList)
const loading = ref(false)
const showImportDialog = ref(false)

// 查詢/刷新資料
const fetchData = async () => {
  console.log('1. 開始刷新，當前查詢日期:', inventoryDate.value)
  if (!inventoryDate.value) {
    ElMessage.warning('请选择盘点日期后再进行查询')
    return
  }

  loading.value = true
  try {
    await store.fetchAssetInventory(inventoryDate.value)
    if (store.assetList.length > 0) {
    console.log('第一筆資料的真實 Key 名稱:', Object.keys(store.assetList[0]))
    console.log('第一筆資料內容:', store.assetList[0])
    }
    console.log('2. API 呼叫完成，Store 中的 assetList 為:', store.assetList)
  } catch (error) {
    console.error('刷新失败:', error)
    ElMessage.error('获取资产数据失败，请检查网络或后端服务')
  } finally {
    loading.value = false
  }
}

// 修正 2：處理匯入成功的 Hook
const handleImportSuccess = (importedDate) => {
  // 如果 Excel 內指定的盤點日期與當前不同，自動切換至新日期
  if (importedDate && importedDate !== inventoryDate.value) {
    inventoryDate.value = importedDate
  }
  ElMessage.success('导入成功，已刷新数据！')
  fetchData()
}

onMounted(fetchData)

const handleSave = async () => {
  try {
    const data = assetList.value.map(item => ({
      inventory_date: inventoryDate.value,
      self_no: item.self_no,
      confirm_id: item.confirm_id || '',
      confirm_date: item.confirm_date || ''
    }))
    const res = await assetApi.saveAsset(inventoryDate.value, data)
    if (res.Success === 'Y') {
      ElMessage.success('保存成功')
      fetchData()
    } else ElMessage.error(res.ErrorMessage || '保存失败')
  } catch { ElMessage.error('请求失败') }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该资产？', '提示', { type: 'warning' })
    const res = await assetApi.delete(inventoryDate.value, row.self_no)
    if (res.Success === 'Y') {
      ElMessage.success('删除成功')
      fetchData()
    } else ElMessage.error(res.ErrorMessage || '删除失败')
  } catch (error) { if (error !== 'cancel') console.error(error) }
}

const handleDeleteAll = async () => {
  try {
    await ElMessageBox.confirm('确认删除所有资产？', '警告', { type: 'error' })
    const res = await assetApi.deleteAll(inventoryDate.value)
    if (res.Success === 'Y') {
      ElMessage.success('全部删除成功')
      fetchData()
    } else ElMessage.error(res.ErrorMessage || '删除失败')
  } catch (error) { if (error !== 'cancel') console.error(error) }
}

const handleScrapped = async (row) => {
  try {
    await ElMessageBox.confirm('确认报废该资产？', '提示', { type: 'warning' })
    const res = await assetApi.scrapped(inventoryDate.value, row.self_no)
    if (res.Success === 'Y') {
      ElMessage.success('报废成功')
      fetchData()
    } else ElMessage.error(res.ErrorMessage || '报废失败')
  } catch (error) { if (error !== 'cancel') console.error(error) }
}
</script>

<style scoped>
.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>