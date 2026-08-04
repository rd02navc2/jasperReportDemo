import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'   // 需要引入 path 模块

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')   // 将 @ 指向 src 目录
    }
  },
  server: {
    proxy: {
      '/Report': {
        target: 'http://localhost:8095',   // 你的后端地址
        changeOrigin: true,
      }
    }
  }
})