import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { useAuthStore } from './store/auth'
import api from './services/api'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

// 🔹 Garante que o usuário logado é carregado antes de renderizar a aplicação
const auth = useAuthStore()
auth.carregarUsuarioSalvo()

// 🔹 Define o token no axios se existir
if (auth.token) {
  api.defaults.headers.common['Authorization'] = `Bearer ${auth.token}`
}

// Monta o app
app.mount('#app')
