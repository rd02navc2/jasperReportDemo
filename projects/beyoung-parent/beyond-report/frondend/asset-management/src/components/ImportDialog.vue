<template>
  <el-dialog v-model="visible" title="导入资产" width="80%" @close="reset">
    <el-upload ref="uploadRef" drag :auto-upload="false" :on-change="handleFileChange"
      :file-list="fileList" accept=".xlsx,.xls">
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">拖拽或点击选择 Excel 文件</div>
    </el-upload>

    <el-table :data="previewData" border v-loading="previewLoading" style="margin-top:20px;">
      <el-table-column prop="self_no" label="资产编号" />
      <el-table-column prop="model_no" label="型号" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="prod_desc" label="描述" />
      <el-table-column prop="owner_id" label="持有人ID" />
      <el-table-column prop="owner_name" label="持有人姓名" />
      <el-table-column prop="title" label="职称" />
      <el-table-column prop="dept_name" label="部门" />
    </el-table>

    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="handleImport" :disabled="!previewData.length">导入</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { assetApi } from '@/api/asset'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'

const props = defineProps({ modelValue: Boolean })
const emit = defineEmits(['update:modelValue', 'import-success'])

const visible = ref(false)
const fileList = ref([])
const previewData = ref([])
const previewLoading = ref(false)

watch(() => props.modelValue, val => visible.value = val)
watch(visible, val => emit('update:modelValue', val))

const reset = () => { fileList.value = []; previewData.value = [] }

const handleFileChange = async (file) => {
  previewLoading.value = true
  try {
    const res = await assetApi.uploadExcel(file.raw)
    if (res.Success === 'Y') {
      previewData.value = res.rows || []
      ElMessage.success(`解析成功，共 ${res.total} 条记录`)
    } else ElMessage.error(res.ErrorMessage || '解析失败')
  } catch { ElMessage.error('上传解析失败') }
  finally { previewLoading.value = false }
}

const handleImport = async () => {
  try {
    const res = await assetApi.saveImport(previewData.value)
    if (res.Success === 'Y') {
      ElMessage.success('导入成功')
      emit('import-success')
      visible.value = false
      reset()
    } else ElMessage.error(res.ErrorMessage || '导入失败')
  } catch { ElMessage.error('导入请求失败') }
}
</script>
