<template>
  <div class="container flex items-center justify-center min-h-screen">
    <div class="card" style="width: 360px;">
      <h2 class="text-center mb-4">Login</h2>

      <form @submit.prevent="handleLogin">
        <input
          v-model="email"
          type="email"
          placeholder="Email"
          class="mb-3"
        />

        <input
          v-model="password"
          type="password"
          placeholder="Password"
          class="mb-3"
        />

        <button class="btn-primary w-full">
          Login
        </button>
      </form>

      <p class="text-center mt-3 text-sm">
        No account?
        <router-link to="/auth" class="text-blue">
          Sign up
        </router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const email = ref('')
const password = ref('')

const handleLogin = async () => {
  try {
    const { data } = await axios.post('/api/auth/login', {
      email: email.value,
      password: password.value
    })

    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify(data.user))

    router.push('/')
  } catch (err) {
    alert(err?.response?.data?.message || 'Login failed')
  }
}
</script>