<template>
  <div class="auth-wrapper">
    <div class="container auth-content">

      <!-- Imagem -->
      <div class="auth-image-side">
        <img src="/src/assets/cadastrapets.png" alt="Login Illustration" />
      </div>

      <!-- Formulário -->
      <div class="auth-form-side">
        <h1>LOGIN</h1>
        <form @submit.prevent="handleLogin" class="form-box">
          <InputField v-model="email" placeholder="Email" />
          <InputField v-model="password" placeholder="Senha" type="password" />

          <!-- Campo OTP -->
          <transition name="fade">
            <InputField
              v-if="step === 2"
              v-model="otp"
              placeholder="Código enviado por e-mail"
            />
          </transition>

          <Button type="submit">
            {{ step === 1 ? 'Enviar' : 'Confirmar Código' }}
          </Button>
        </form>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import InputField from '../components/InputField.vue'
import Button from '../components/Button.vue'
import { useAuthStore } from '../store/auth'
import api from '../services/api'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const email = ref('')
const password = ref('')
const otp = ref('')
const step = ref(1)
const teste2FA = ref('') // debug/dev

onMounted(() => {
  // Resetar formulário se houver query reset
  if (route.query.reset) {
    email.value = ''
    password.value = ''
    otp.value = ''
    step.value = 1
  }
})

const handleLogin = async () => {
  if (!email.value || !password.value) {
    alert('Preencha email e senha')
    return
  }

  try {
    let response

    if (step.value === 1) {
      // 🔹 Login inicial
      response = await api.post('/auth/login', {
        email: email.value,
        senha: password.value
      })

      // 🔹 Backend solicita 2FA
      if (response.data.requires2FA) {
        step.value = 2
        otp.value = ''
        teste2FA.value = response.data.codigoTeste || ''
        alert(response.data.mensagem || 'Código 2FA enviado para seu e-mail')
        return
      }

    } else if (step.value === 2) {
      // 🔹 Login com 2FA
      if (!otp.value.trim()) {
        alert('Digite o código enviado por e-mail')
        return
      }

      response = await api.post('/auth/login', {
        email: email.value,
        senha: password.value,
        codigo2FA: otp.value
      })
    }

    // 🔹 Login completo
    const { token, usuario } = response.data
    const usuarioObj = typeof usuario === 'string' ? { email: usuario } : usuario

    // Salva token e usuário no store
    auth.setUsuarioEtoken(usuarioObj, token)


    // Redireciona para dashboard
    router.push('/dashboard')

  } catch (error) {
    console.error('Erro login:', error)

    if (error.response) {
      // Mostra mensagem do backend
      alert(`Erro ${error.response.status}: ${JSON.stringify(error.response.data)}`)
    } else {
      // Falha de conexão
      alert('Erro ao conectar com o servidor.')
    }
  }
}
</script>



<style scoped>
.auth-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 50px 16px;
  width: 100%;
  box-sizing: border-box;
}

.container {
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.6);
  border-radius: 15px;
  padding: 40px 50px;
  box-shadow: 0 6px 25px rgba(0,0,0,0.12);
  width: 100%;
  max-width: 1100px;
  display: flex;
  gap: 35px;
  align-items: center;
}

.auth-content {
  display: flex;
  width: 100%;
  align-items: center;
  gap: 35px;
}

.auth-image-side {
  flex: 1.2;
  display: flex;
  justify-content: center;
}
.auth-image-side img {
  max-width: 420px;
  width: 100%;
}

.auth-form-side {
  flex: 1;
  display: flex;
  flex-direction: column;
  text-align: center;
  align-items: center;
}
h1 {
  color: #42b983;
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 18px;
}

.form-box {
  width: 100%;
  max-width: 380px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.fade-enter-active, .fade-leave-active {
  transition: all 0.4s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
.fade-enter-to, .fade-leave-from {
  opacity: 1;
  transform: translateY(0);
}
</style>
