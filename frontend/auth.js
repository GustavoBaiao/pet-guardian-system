// auth.js - exemplo simples Pinia
import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: null
  }),
  actions: {
    login(usuario, token) {
      this.user = usuario
      this.token = token
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify(usuario))
    },
    loadFromStorage() {
      const token = localStorage.getItem('token')
      const user = localStorage.getItem('user')
      if (token && user) {
        this.token = token
        this.user = JSON.parse(user)
      }
    },
    logout() {
      this.user = null
      this.token = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})

z
