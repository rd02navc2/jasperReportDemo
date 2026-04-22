import { defineStore } from 'pinia'
import axios from 'axios'

export const useAuth = defineStore('auth', {
  state: () => ({
    user: JSON.parse(localStorage.getItem('user')) || null,
    token: localStorage.getItem('token') || null,
    errorMessage: ''
  }),

  actions: {

    restoreAuth() {
      const token = localStorage.getItem('token')
      const user = localStorage.getItem('user')

      if (token) {
        this.token = token
      }

      if (user) {
        this.user = JSON.parse(user)
      }

      this.initialized = true
    },

    async login(email, password) {
      try {
        const { data } = await axios.post('/api/auth/login', {
          email,
          password
        })

        this.user = data.user
        this.token = data.token

        localStorage.setItem('user', JSON.stringify(data.user))
        localStorage.setItem('token', data.token)

        return { success: true }
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