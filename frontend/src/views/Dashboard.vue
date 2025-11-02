<template>
  <div class="container">
    <h1>Bem-vindo, {{ auth.user.name }}</h1>
    <p>Área restrita da clínica veterinária.</p>
    <Button @click="auth.logout">Sair</Button>
  </div>
</template>

<script setup>
import { useAuthStore } from '../store/auth'
import Button from '../components/Button.vue'
const auth = useAuthStore()
</script>

<style>
.container { max-width: 600px; margin: 20px auto; }
</style>
