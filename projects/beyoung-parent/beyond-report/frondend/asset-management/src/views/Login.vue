<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2 class="login-title">資產管理系統</h2>
      </template>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        label-position="top"
        size="large"
        @keyup.enter="handleLogin"
      >
        <el-form-item label="帳號" prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="請輸入帳號"
            prefix-icon="User"
            clearable
          />
        </el-form-item>

        <el-form-item label="密碼" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="請輸入密碼"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            class="login-button"
            :loading="loading"
            @click="handleLogin"
          >
            登 入
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import axios from 'axios' // 或引用你封裝好的 axios 實例，如 @/utils/request

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

// 表單資料
const loginForm = reactive({
  username: '',
  password: ''
})

// 表單驗證規則
const loginRules = reactive({
  username: [{ required: true, message: '請輸入帳號', trigger: 'blur' }],
  password: [{ required: true, message: '請輸入密碼', trigger: 'blur' }]
})

// 執行登入 action
const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      const params = new URLSearchParams()
      params.append('username', loginForm.username)
      params.append('password', loginForm.password)

      const response = await axios.post('http://localhost:8095/login', params)
      
      console.log('API 回應內容：', response.data) // Debug 用

      // 關鍵修正：依照後端實際回傳欄位 (Success === 'Y' 或 success === true) 做判斷
      if (response.data.Success === 'Y' || response.data.success === true) {
        ElMessage.success('登入成功！')
        
        // 儲存登入狀態（可選）
        localStorage.setItem('user', loginForm.username)
        
        // 執行頁面跳轉
        router.push('/Dashboard')
      } else {
        ElMessage.error(response.data.LoginMsg || '登入失敗')
      }
    } catch (error) {
      console.error('Login error:', error)
      ElMessage.error('連線失敗，請檢查網路或 API 設定')
    } finally {
      loading.value = false
    }
  })
}

</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f2f5;
}

.login-card {
  width: 400px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.login-title {
  text-align: center;
  margin: 0;
  font-size: 20px;
  color: #303133;
}

.login-button {
  width: 100%;
  margin-top: 10px;
}
</style>