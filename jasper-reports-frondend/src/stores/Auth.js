import { defineStore } from 'pinia'
import axios from 'axios'

export const useAuth = defineStore('auth', {
  state: () => {
    // 取得原始字串
    const savedUser = localStorage.getItem('user');
    const userStr = localStorage.getItem('user')
    
    // 檢查：如果是 null 或是字串 "undefined"，就回傳 null
    const user = (userStr && userStr !== "undefined" && userStr !== "null")
      ? JSON.parse(savedUser) 
      : null;

    return {
      user: user,
      token: localStorage.getItem('token') || null,
      errorMessage: ''
    }
  },

  actions: {

    restoreAuth() {
      const token = localStorage.getItem('token');
      const savedUser = localStorage.getItem('user');

      if (token) {
        this.token = token;
      }

      // 同樣要在這裡做檢查，避免報錯
      if (savedUser && savedUser !== "undefined") {
        try {
          this.user = JSON.parse(savedUser);
        } catch (e) {
          console.error("解析 User JSON 失敗", e);
          this.user = null;
        }
      }

      this.initialized = true;
    },

    async login(email, password) {
      try {
        const { data } = await axios.post('/api/auth/login', { email, password });

        this.user = data.user || null;
        this.token = data.token || null;

        // 確保 data.user 存在才存入，否則存入空字串或移除
        if (data.user) {
          localStorage.setItem('user', JSON.stringify(data.user));
        }
        if (data.token) {
          localStorage.setItem('token', data.token);
        }

        return { success: true };
      } catch (err) {
        this.errorMessage =
          err?.response?.data?.message || 'Login failed'

        return { success: false }
      }
    },

    logout() {
      this.user = null
      this.token = null

      localStorage.removeItem('user')
      localStorage.removeItem('token')
    }
  }
})