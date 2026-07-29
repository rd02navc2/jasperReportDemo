import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000,
    proxy: {
      '/api-8080': {
        target: 'http://192.168.5.92:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api-8080/, '')
      },
      '/api-8085': {
        target: 'http://192.168.5.92:8085',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api-8085/, '')
      },
      '/api-8095': {
        target: 'http://192.168.5.92:8095',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api-8095/, '')
      }
    }
  }
})
