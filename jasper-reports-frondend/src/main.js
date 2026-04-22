import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import './assets/main.css'

// 1️⃣ 建立 app
const app = createApp(App)

// 2️⃣ 建立 Pinia
const pinia = createPinia()

// 3️⃣ 掛載 Pinia（🔥 一定要最先）
app.use(pinia)

// 4️⃣ 掛載 Router
app.use(router)

// ❌ 不要在這裡 useAuth()
// ❌ 不要在這裡呼叫 store

// 5️⃣ mount
app.mount('#app')


