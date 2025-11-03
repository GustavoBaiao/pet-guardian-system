<template>
  <div class="container">
    <h1>LOGIN</h1>
    <form @submit.prevent="handleLogin">
      <InputField v-model="email" placeholder="Email" />
      <InputField v-model="password" placeholder="Senha" type="password" />
      <InputField v-model="otp" placeholder="Código 2FA" />
      <Button type="submit">Entrar</Button>
    </form>
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

<style>
.container { max-width: 400px; margin: 20px auto; }

h1 {
  text-align: center;
  color: #42b983;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  margin-bottom: 15px;
}

</style>
