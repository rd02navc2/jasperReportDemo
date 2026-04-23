<template>
  <div class="container flex items-center justify-center min-h-screen">
    <div class="card" style="width: 360px;">
      <h2 class="text-center mb-4">Login</h2>

      <form @submit.prevent="handleLogin">
        <input v-model="email" type="email" placeholder="Email" class="mb-3 w-full p-2 border" required />
        <input v-model="password" type="password" placeholder="Password" class="mb-3 w-full p-2 border" required />
        <button class="btn-primary w-full bg-blue-500 text-white p-2">Login</button>
      </form>

      <p class="text-center mt-3 text-sm">
        No account? <router-link to="/auth" class="text-blue">Sign up</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../stores/auth'
import axios from 'axios'

const router = useRouter()
const email = ref('')
const password = ref('')

// Login.vue
const auth = useAuth() // 引用 Store

const handleLogin = async () => {
  // 使用 Store 定義好的 login action
  const result = await auth.login(email.value, password.value)
  
  if (result.success) {
    // 登入成功後，Store 內的 this.token 已經被更新
    // App.vue 的 isLoggedIn 會自動偵測到變化並顯示導覽列
    router.push('/') 
  } else {
    alert(auth.errorMessage)
  }
}

</script>