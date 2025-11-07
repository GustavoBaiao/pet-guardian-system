<template>
  <div class="auth-wrapper">
    <div class="container auth-content"> 
      
      <div class="auth-image-side">
        <img src="/src/assets/cadastrapets.png" alt="Login Illustration" />
      </div>

      <div class="auth-form-side">
        <h1>LOGIN</h1>
        <form @submit.prevent="handleLogin" class="form-box">
          <InputField v-model="email" placeholder="Email" />
          <InputField v-model="password" placeholder="Senha" type="password" />
          <InputField v-model="otp" placeholder="Código 2FA" />
          <Button type="submit">Entrar</Button>
        </form>
      </div>

    </div>
  </div>
</template>


<script setup>
import { ref } from 'vue'
import InputField from '../components/InputField.vue'
import Button from '../components/Button.vue'
import { useAuthStore } from '../store/auth'

const email = ref('')
const password = ref('')
const otp = ref('')
const auth = useAuthStore()

const handleLogin = () => {
  if (!email.value || !password.value || !otp.value) {
    alert('Preencha todos os campos')
    return
  }
  auth.login({ name: 'Usuário', email: email.value })
  alert('Login realizado com sucesso!')
}
</script>

<style scoped>
/* Centralizar o container como nas outras páginas */
.auth-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 50px 16px;
  width: 100%;
  box-sizing: border-box;
}

/* Mantendo o MESMO container que você já usa */
.container {
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.6); /* transparente */
  border-radius: 15px;
  padding: 40px 50px;
  box-shadow: 0 6px 25px rgba(0,0,0,0.12);
  width: 100%;
  max-width: 1100px;
  display: flex;
  gap: 35px;
  align-items: center;
}

/* Layout imagem + form */
.auth-content {
  display: flex;
  width: 100%;
  align-items: center;
  gap: 35px;
}

/* Lado da imagem */
.auth-image-side {
  flex: 1.2;
  display: flex;
  justify-content: center;
}
.auth-image-side img {
  max-width: 420px;
  width: 100%;
}

/* Lado do formulário */
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

@keyframes fadeSlideUp {
  0% {
    opacity: 0;
    transform: translateY(25px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

/* aplica animação no container */
.container {
  animation: fadeSlideUp 0.6s ease-out;
}

.form-box > * {
  animation: fadeSlideUp 0.6s ease backwards;
}

.form-box > *:nth-child(1) { animation-delay: 0.1s; }
.form-box > *:nth-child(2) { animation-delay: 0.2s; }
.form-box > *:nth-child(3) { animation-delay: 0.3s; }
.form-box > *:nth-child(4) { animation-delay: 0.4s; }


/* Box dos campos */
.form-box {
  width: 100%;
  max-width: 380px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

/* Responsividade */
@media (max-width: 900px) {
  .container {
    flex-direction: column;
    padding: 30px;
  }
  .auth-content {
    flex-direction: column;
  }
  .auth-image-side img {
    max-width: 300px;
  }
}
</style>




