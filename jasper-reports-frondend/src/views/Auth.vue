<template>
  <div class="auth-container">
    <div class="auth-card card">
      <h2 class="text-center mb-4">
        {{ isLogin ? 'Login' : 'Sign Up' }}
      </h2>

      <!-- Error -->
      <div v-if="errorMessage" class="error-box text-red mb-3">
        {{ errorMessage }}
      </div>

      <!-- Form -->
      <form @submit.prevent="handleSubmit">
        <!-- Username (signup only) -->
        <input
          v-if="!isLogin"
          v-model="form.username"
          type="text"
          placeholder="Username"
          class="mb-3"
        />

        <!-- Email -->
        <input
          v-model="form.email"
          type="email"
          placeholder="Email"
          class="mb-3"
          required
        />

        <!-- Password -->
        <input
          v-model="form.password"
          type="password"
          placeholder="Password"
          class="mb-3"
          required
        />

        <!-- Submit -->
        <button
          class="btn-primary w-full"
          :disabled="loading"
        >
          <span v-if="loading">Processing...</span>
          <span v-else>
            {{ isLogin ? 'Login' : 'Create Account' }}
          </span>
        </button>
      </form>

      <!-- Switch -->
      <p class="text-center mt-3 text-sm">
        <span v-if="isLogin">No account?</span>
        <span v-else>Already have account?</span>

        <a href="#" @click.prevent="toggleMode" class="text-blue">
          {{ isLogin ? 'Sign up' : 'Login' }}
        </a>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const isLogin = ref(true)
const loading = ref(false)
const errorMessage = ref('')

const form = ref({
  username: '',
  email: '',
  password: ''
})

const toggleMode = () => {
  isLogin.value = !isLogin.value
  errorMessage.value = ''
}

const handleSubmit = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const url = isLogin.value
      ? '/api/auth/login'
      : '/api/auth/signup'

    const payload = isLogin.value
      ? {
          email: form.value.email,
          password: form.value.password
        }
      : {
          username: form.value.username,
          email: form.value.email,
          password: form.value.password
        }

    const { data } = await axios.post(url, payload)

    // 假設 AuthResponse = { token, user }
    if (data?.token) {
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(data.user))

      router.push('/')
    } else {
      throw new Error('Invalid response from server')
    }

  } catch (err) {
    errorMessage.value =
      err?.response?.data?.message ||
      err?.message ||
      'Authentication failed'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.auth-card {
  width: 360px;
}

.error-box {
  background: #fee2e2;
  padding: 8px;
  border-radius: 6px;
}
.w-full {
  width: 100%;
}
.text-blue {
  color: #2563eb;
  cursor: pointer;
}
</style>