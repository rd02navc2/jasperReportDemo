<template>
  <el-dialog
    v-model="visible"
    title="匯入 Excel 資產資料"
    width="600px"
    :before-close="handleClose"
  >
    <el-upload
      drag
      action="#"
      :auto-upload="false"
      :show-file-list="false"
      :on-change="handleFileChange"
      accept=".xlsx, .xls"
    >
      <div class="el-upload__text">
        將 Excel 檔案拖曳至此，或 <em>點擊上傳</em>
      </div>
    </el-upload>

    <!-- 預覽資料區塊 -->
    <div v-if="previewData.length > 0" style="margin-top: 15px;">
      <p style="margin-bottom: 8px; font-weight: bold;">
        解析成功，共 {{ previewData.length }} 筆資料（預覽前 5 筆）：
      </p>
      <el-table :data="previewData.slice(0, 5)" border size="small">
        <el-table-column prop="self_no" label="資產編號" />
        <el-table-column prop="owner_name" label="持有人" />
        <el-table-column prop="dept_name" label="部門" />
      </el-table>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button
          type="primary"
          :disabled="previewData.length === 0"
          :loading="importLoading"
          @click="handleImport"
        >
          確認匯入
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { assetApi } from '@/api/asset'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  inventoryDate: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue', 'import-success'])

const visible = ref(false)
const previewData = ref([])
const importLoading = ref(false)

// 監聽外部開關
watch(() => props.modelValue, (val) => {
  visible.value = val
})

watch(visible, (val) => {
  emit('update:modelValue', val)
  if (!val) reset()
})

const reset = () => {
  previewData.value = []
}

const handleClose = (done) => {
  reset()
  done()
}

// 欄位 Key 轉換對照表 (確保 Excel 欄位能對應到 SQL 欄位)
const keyMap = {
  '資產編號': 'self_no', '资产编号': 'self_no', 'self_no': 'self_no', 'SelfNo': 'self_no',
  '型號': 'model_no', '型号': 'model_no', 'model_no': 'model_no', 'ModelNo': 'model_no',
  '類型': 'type', '类型': 'type', 'type': 'type',
  '描述': 'prod_desc', 'prod_desc': 'prod_desc',
  '持有人ID': 'owner_id', 'owner_id': 'owner_id',
  '持有人姓名': 'owner_name', '持有人': 'owner_name', 'owner_name': 'owner_name',
  '職稱': 'title', '职称': 'title', 'title': 'title',
  '部門': 'dept_name', '部门': 'dept_name', 'dept_name': 'dept_name'
}

const handleFileChange = async (file) => {
  try {
    const res = await assetApi.uploadExcel(file.raw)
    if (res.Success === 'Y') {
      const rawRows = res.rows || res.data || []
      
      // 自動轉 key，防止寫入空資料
      previewData.value = rawRows.map(row => {
        const formattedRow = {}
        Object.keys(row).forEach(key => {
          const standardKey = keyMap[key.trim()] || key
          formattedRow[standardKey] = row[key]
        })
        return formattedRow
      })

      ElMessage.success(`成功解析 ${previewData.value.length} 筆資料`)
    } else {
      ElMessage.error(res.ErrorMessage || '解析失敗')
    }
  } catch (error) {
    console.error('上傳解析錯誤:', error)
    ElMessage.error('Excel 上傳解析失敗')
  }
}

const handleImport = async () => {
  if (previewData.value.length === 0) return
  
  importLoading.value = true
  try {
    const importData = previewData.value.map(item => ({
      inventory_date: props.inventoryDate,
      self_no: item.self_no || '',
      model_no: item.model_no || '',
      type: item.type || '',
      prod_desc: item.prod_desc || '',
      owner_id: item.owner_id || '',
      owner_name: item.owner_name || '',
      title: item.title || '',
      dept_name: item.dept_name || '',
      confirm_id: item.confirm_id || '',
      confirm_date: item.confirm_date || ''
    }))

    const res = await assetApi.saveImport(importData)
    if (res.Success === 'Y') {
      ElMessage.success('匯入成功！')
      emit('import-success', props.inventoryDate)
      visible.value = false
      reset()
    } else {
      ElMessage.error(res.ErrorMessage || '儲存匯入失敗')
    }
  } catch (error) {
    console.error('儲存匯入錯誤:', error)
    ElMessage.error('匯入請求失敗')
  } finally {
    importLoading.value = false
  }
}
</script>