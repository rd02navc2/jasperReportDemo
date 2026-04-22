<template>
  <div class="flex items-center justify-center min-h-screen">
    <div class="text-center">
      <h2>Processing login...</h2>
      <div class="loader mt-4"></div>

      <p v-if="error" class="text-red mt-3">
        {{ error }}
      </p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const error = ref('')

onMounted(async () => {
  try {
    // 從 backend redirect 帶來的 token
    const token = route.query.token

    if (!token) {
      throw new Error('Missing token from OAuth provider')
    }

    // 可選：驗證 token 或換 user info
    const { data } = await axios.get('/api/auth/me', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    // 存入 localStorage
    localStorage.setItem('token', token)
    localStorage.setItem('user', JSON.stringify(data))

    // redirect home
    router.replace('/')
  } catch (err) {
    error.value =
      err?.response?.data?.message ||
      err?.message ||
      'OAuth login failed'
  }
})
</script>