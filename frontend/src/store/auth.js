import { defineStore } from 'pinia'
import api from '../services/api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    usuario: null,
    token: localStorage.getItem('token') || null,
  }),

  actions: {
    async login(email, senha) {
      try {
        // 🔹 Garante que está enviando apenas strings
        const payload = {
          email: typeof email === 'object' ? email.email : email,
          senha: senha
        }

        const response = await api.post('/auth/login', payload)
        const { token, usuario } = response.data

        this.token = token
        this.usuario = usuario

        // 🔹 Salva no localStorage
        localStorage.setItem('token', token)
        localStorage.setItem('usuario', JSON.stringify(usuario))

        // 🔹 Define token no cabeçalho global
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`

        return true
      } catch (error) {
        console.error('Erro no login:', error.response?.data || error.message)
        throw error
      }
    },

    setUsuarioEtoken(usuario, token) {
      this.usuario = usuario
      this.token = token
      localStorage.setItem('usuario', JSON.stringify(usuario))
      localStorage.setItem('token', token)
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`
    },


    logout() {
      this.token = null
      this.usuario = null
      localStorage.removeItem('token')
      localStorage.removeItem('usuario')
      delete api.defaults.headers.common['Authorization']
    },

    carregarUsuarioSalvo() {
      try {
        const usuarioSalvo = localStorage.getItem('usuario')
        const tokenSalvo = localStorage.getItem('token')

        if (usuarioSalvo && tokenSalvo && usuarioSalvo !== 'undefined') {
          this.usuario = JSON.parse(usuarioSalvo)
          this.token = tokenSalvo
          api.defaults.headers.common['Authorization'] = `Bearer ${this.token}`
        } else {
          this.usuario = null
          this.token = null
        }
      } catch (error) {
        console.error('Erro ao carregar dados do usuário:', error)
        this.usuario = null
        this.token = null
      }
    },

    estaAutenticado() {
      return !!this.token
    },
  },
})
