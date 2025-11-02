import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    isLoggedIn: false,
    sessionTimer: null,
    loginAttempts: 0
  }),
  actions: {
    login(userData) {
      this.user = userData
      this.isLoggedIn = true
      this.startSessionTimer()
      this.loginAttempts = 0
    },
    logout() {
      this.user = null
      this.isLoggedIn = false
      clearTimeout(this.sessionTimer)
    },
    startSessionTimer() {
      clearTimeout(this.sessionTimer)
      this.sessionTimer = setTimeout(() => {
        this.logout()
        alert('Sessão expirada por inatividade')
      }, 15 * 60 * 1000)
    },
    failedLoginAttempt() {
      this.loginAttempts++
      if (this.loginAttempts >= 5) {
        alert('Conta temporariamente bloqueada após 5 tentativas')
      }
    }
  }
})
