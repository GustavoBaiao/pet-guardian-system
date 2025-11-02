<template>
  <div class="container">
    <h1>Cadastro do Tutor e do Pet</h1>
    <form @submit.prevent="handleRegister">
      
      <!-- Dados do Tutor -->
      <h2>Tutor</h2>
      <InputField v-model="tutorName" placeholder="Nome do Tutor" />
      <InputField v-model="tutorEmail" placeholder="Email" type="email" />
      <InputField v-model="tutorPassword" placeholder="Senha" type="password" />

      <!-- Dados do Pet -->
      <h2>Pet</h2>
      <InputField v-model="petName" placeholder="Nome do Pet" />
      <InputField v-model="petSpecies" placeholder="Espécie" />
      <InputField v-model="petBreed" placeholder="Raça" />
      <InputField v-model="petAge" placeholder="Idade" type="number" />

      <!-- Aceite da política de privacidade -->
      <label style="display: flex; align-items: center; gap: 5px; margin: 10px 0;">
        <input type="checkbox" v-model="acceptTerms" />
            Aceito a Política de Privacidade
        <router-link to="/privacy-policy" style="margin-left: 5px; color: #42b983; text-decoration: underline;">
            (Leia os termos)
         </router-link>
    </label>


      <Button :disabled="!acceptTerms" type="submit">Cadastrar</Button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import InputField from '../components/InputField.vue'
import Button from '../components/Button.vue'

const tutorName = ref('')
const tutorEmail = ref('')
const tutorPassword = ref('')

const petName = ref('')
const petSpecies = ref('')
const petBreed = ref('')
const petAge = ref('')

const acceptTerms = ref(false)

const handleRegister = () => {
  // Validação simples
  if (!tutorName.value || !tutorEmail.value || !tutorPassword.value ||
      !petName.value || !petSpecies.value || !petBreed.value || !petAge.value) {
    alert('Preencha todos os campos!')
    return
  }

  if (!acceptTerms.value) {
    alert('Aceite os termos antes de cadastrar!')
    return
  }

  // Aqui você poderia enviar os dados para um backend
  alert(`Cadastro realizado com sucesso!\nTutor: ${tutorName.value}\nPet: ${petName.value}`)
  
  // Resetar formulário
  tutorName.value = ''
  tutorEmail.value = ''
  tutorPassword.value = ''
  petName.value = ''
  petSpecies.value = ''
  petBreed.value = ''
  petAge.value = ''
  acceptTerms.value = false
}
</script>

<style>
.container {
  max-width: 500px;
  margin: 20px auto;
  padding: 20px;
  background-color: #f7f7f7;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}

h1, h2 {
  text-align: center;
  color: #42b983;
}

form {
  display: flex;
  flex-direction: column;
}

label {
  margin: 10px 0;
  font-size: 14px;
}
</style>
