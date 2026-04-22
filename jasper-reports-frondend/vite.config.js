import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path' // 需確認是否有路徑需求，可選

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      // 設定 @ 符號指向 src 目錄，方便 import 檔案
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    proxy: {
      // 當你的前端程式碼呼叫 /api 開頭的路徑時
      '/api': {
        target: 'http://localhost:8080', // 你的後端 Spring Boot 執行位址
        changeOrigin: true,             // 允許跨域
        secure: false,                  // 如果是 http 則設為 false
        // rewrite: (path) => path.replace(/^\/api/, '') 
        // ^ 如果後端 API 本身「沒有」/api 前綴，請取消上面這行的註解
      }
    }
  }
})