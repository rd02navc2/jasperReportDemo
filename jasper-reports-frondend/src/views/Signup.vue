<template>
  <div class="container flex items-center justify-center min-h-screen">
    <div class="card" style="width: 360px;">
      <h2 class="text-center mb-4">Sign Up</h2>

      <form @submit.prevent="handleSignup">
        <input
          v-model="username"
          type="text"
          placeholder="Username"
          class="mb-3"
        />

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
          Create Account
        </button>
      </form>

      <p class="text-center mt-3 text-sm">
        Already have account?
        <router-link to="/login" class="text-blue">
          Login
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

const username = ref('')
const email = ref('')
const password = ref('')

const handleSignup = async () => {
  try {
    await axios.post('/api/auth/signup', {
      username: username.value,
      email: email.value,
      password: password.value
    })

    router.push('/login')
  } catch (err) {
    alert(err?.response?.data?.message || 'Signup failed')
  }
}
</script>