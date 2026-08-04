<template>
  <div class="scrapped-assets">
    <el-card>
      <template #header><span>报废资产</span></template>
      <el-form :inline="true" @submit.prevent>
        <el-form-item label="起始日期">
          <el-date-picker v-model="filters.sale_s_date" type="date" placeholder="起始" value-format="yyyy-MM-dd" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="filters.sale_e_date" type="date" placeholder="结束" value-format="yyyy-MM-dd" />
        </el-form-item>
        <el-form-item label="资产编号">
          <el-input v-model="filters.self_no" placeholder="输入编号" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="scrappedList" border v-loading="loading">
        <el-table-column prop="self_no" label="资产编号" />
        <el-table-column prop="model_no" label="型号" />
        <el-table-column prop="type" label="类型" />
        <el-table-column prop="prod_desc" label="描述" />
        <el-table-column prop="owner_name" label="原持有人" />
        <el-table-column prop="scrapped_id" label="报废人" />
        <el-table-column prop="scrapped_date" label="报废日期" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { assetApi } from '@/api/asset'
import { ElMessage } from 'element-plus'

const scrappedList = ref([])
const loading = ref(false)
const filters = reactive({ sale_s_date: '', sale_e_date: '', self_no: '' })

const fetchData = async () => {
  loading.value = true
  try {
    const res = await assetApi.getAssetScrapped(filters)
    if (res.Success === 'Y') scrappedList.value = res.rows
    else ElMessage.error(res.ErrorMessage || '查询失败')
  } catch { ElMessage.error('请求失败') }
  finally { loading.value = false }
}
fetchData()
</script>
