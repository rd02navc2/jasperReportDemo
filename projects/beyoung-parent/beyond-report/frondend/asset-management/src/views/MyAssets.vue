<template>
  <div class="asset-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>資產盤點清單</span>
          <div class="header-actions">
            <!-- 修正 1：新增盤點日期篩選器，預設帶入日期 -->
            <el-date-picker
              v-model="searchDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="選擇盤點日期"
              size="small"
              style="width: 150px; margin-right: 10px;"
              @change="loadData"
            />
            <el-button type="success" size="small" @click="openAddDialog">
              新增/盤點資產
            </el-button>
            <el-button type="primary" size="small" @click="loadData">
              刷新資料
            </el-button>
          </div>
        </div>
      </template>

      <!-- 資產表格列表 -->
      <el-table 
        :data="assetList" 
        border 
        stripe 
        style="width: 100%" 
        v-loading="loading"
      >
        <el-table-column prop="inventory_date" label="盤點日期" width="110" sortable />
        <el-table-column prop="self_no" label="資產編號" width="130" sortable fixed />
        <el-table-column prop="model_no" label="型號" width="140" />
        <el-table-column prop="type" label="類別" width="90" align="center" />
        <el-table-column prop="prod_desc" label="資產描述" min-width="180" show-overflow-tooltip />
        
        <!-- 保管人資訊 -->
        <el-table-column label="保管人資訊" min-width="160">
          <template #default="{ row }">
            <div><strong>{{ row.owner_name }}</strong> <el-tag size="small">{{ row.owner_id }}</el-tag></div>
            <div class="sub-text">{{ row.title || '無職稱' }}</div>
          </template>
        </el-table-column>

        <el-table-column prop="dept_name" label="保管部門" width="130" />
        <el-table-column prop="confirm_id" label="確認人" width="100" />
        <el-table-column prop="confirm_date" label="確認時間" width="160" />
      </el-table>
    </el-card>

    <!-- POST 新增彈窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="新增/提交資產盤點資料"
      width="650px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="盤點日期" prop="inventory_date">
              <el-date-picker
                v-model="form.inventory_date"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="選擇日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="資產編號" prop="self_no">
              <el-input v-model="form.self_no" placeholder="例如: AST-2026-001" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="型號" prop="model_no">
              <el-input v-model="form.model_no" placeholder="例如: MacBook Pro 16" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="類別代碼" prop="type">
              <el-input v-model="form.type" placeholder="例如: T01" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="產品描述" prop="prod_desc">
          <el-input 
            v-model="form.prod_desc" 
            type="textarea" 
            :rows="2" 
            placeholder="例如: 開發用高效能筆電" 
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="保管人代號" prop="owner_id">
              <el-input v-model="form.owner_id" placeholder="例如: EMP1001" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保管人姓名" prop="owner_name">
              <el-input v-model="form.owner_name" placeholder="例如: 張小明" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="職稱" prop="title">
              <el-input v-model="form.title" placeholder="例如: 資深工程師" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部門名稱" prop="dept_name">
              <el-input v-model="form.dept_name" placeholder="例如: 資訊開發部" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="確認人代號" prop="confirm_id">
              <el-input v-model="form.confirm_id" placeholder="例如: MGR2001" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="確認時間" prop="confirm_date">
              <el-date-picker
                v-model="form.confirm_date"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="選擇日期時間"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="fillSampleData">帶入範例測試資料</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitting" @click="submitForm">
            提交 saveAsset
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, ref, reactive } from 'vue'
import { assetApi } from '@/api/asset'
import { ElMessage } from 'element-plus'

const assetList = ref([])
const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)

// 修正 2：定義預設查詢日期變數
const searchDate = ref('2026-07-25')

const defaultForm = {
  inventory_date: '2026-07-25',
  self_no: '',
  model_no: '',
  type: '',
  prod_desc: '',
  owner_id: '',
  owner_name: '',
  title: '',
  dept_name: '',
  confirm_id: '',
  confirm_date: ''
}

const form = reactive({ ...defaultForm })

const rules = {
  inventory_date: [{ required: true, message: '請選擇盤點日期', trigger: 'change' }],
  self_no: [{ required: true, message: '請輸入資產編號', trigger: 'blur' }]
}

// 1. 查詢清單
const loadData = async () => {
  loading.value = true
  try {
    // 修正 3：使用 searchDate.value 取代原先寫死的空字串 ''
    const res = await assetApi.getAssetInventory({
      inventory_date: searchDate.value || '',
      sidx: 'self_no',
      sord: 'ASC'
    })

    if (res.Success === 'Y' || res.rows) {
      assetList.value = res.rows || []
      ElMessage.success('資產資料載入成功！')
    } else {
      ElMessage.error(res.LoginMsg || res.ErrorMessage || '載入失敗')
    }
  } catch (error) {
    console.error('查詢失敗:', error)
    ElMessage.error('連線後端失敗')
  } finally {
    loading.value = false
  }
}

// 2. 打開新增視窗
const openAddDialog = () => {
  Object.assign(form, defaultForm)
  dialogVisible.value = true
}

// 3. 填寫測試範例資料
const fillSampleData = () => {
  Object.assign(form, {
    inventory_date: searchDate.value || '2026-07-25',
    self_no: 'AST-' + Date.now().toString().slice(-4), // 產生隨機編號避免重複
    model_no: 'MacBook Pro 16',
    type: 'T01',
    prod_desc: '開發用高效能筆電',
    owner_id: 'EMP1001',
    owner_name: '張小明',
    title: '資深工程師',
    dept_name: '資訊開發部',
    confirm_id: 'MGR2001',
    confirm_date: '2026-07-25 14:30:00'
  })
}

// 4. 呼叫 saveAsset 儲存資料
const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    try {
      const res = await assetApi.saveAsset(form.inventory_date, [form])

      if (res.Success === 'Y' || res.code === 200) {
        ElMessage.success('盤點資料新增成功！')
        dialogVisible.value = false
        // 修正 4：新增資料後自動切換至該新增資料的日期並刷新
        searchDate.value = form.inventory_date
        await loadData()
      } else {
        ElMessage.error(res.ErrorMessage || res.msg || '儲存失敗')
      }
    } catch (error) {
      console.error('儲存失敗:', error)
      ElMessage.error('提交失敗，請檢查 API 回傳內容')
    } finally {
      submitting.value = false
    }
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.asset-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.header-actions {
  display: flex;
  align-items: center;
}

.sub-text {
  font-size: 12px;
  color: #909399;
}
</style>