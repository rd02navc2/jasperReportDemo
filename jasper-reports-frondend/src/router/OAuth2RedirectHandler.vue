<template>
  <div class="flex items-center justify-center min-h-screen">
    <div class="text-center">
      <!-- Loading -->
      <div v-if="state === 'loading'">
        <h2 class="text-lg font-semibold">Logging you in...</h2>
        <div class="loader mt-4"></div>
      </div>

      <!-- Error -->
      <div v-else-if="state === 'error'" class="text-red-500">
        <h2 class="text-lg font-semibold">Login Failed</h2>
        <p class="mt-2">{{ errorMessage }}</p>

        <button
          class="mt-4 px-4 py-2 bg-blue-600 text-white rounded"
          @click="goLogin"
        >
          Back to Login
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuth } from '../stores/auth'

const router = useRouter()
const route = useRoute()
const { loginWithToken } = useAuth()

const state = ref('loading') // loading | error
const errorMessage = ref('')
let handled = false

const getQueryString = (key) => {
  const val = route.query[key]
  return Array.isArray(val) ? val[0] : val
}

const goLogin = () => {
  router.replace({
    path: '/login',
    query: { error: errorMessage.value || 'Login failed' }
  })
}

onMounted(async () => {
  if (handled) return
  handled = true

  const token = getQueryString('token')
  const error = getQueryString('error')

  try {
    if (!token) {
      throw new Error(error || 'Missing authentication token')
    }

    const result = await loginWithToken(token)

    if (!result?.success) {
      throw new Error('Authentication failed')
    }

    router.replace('/')
  } catch (err) {
    state.value = 'error'
    errorMessage.value = err?.message || 'Unexpected error occurred'
  }
})
</script>