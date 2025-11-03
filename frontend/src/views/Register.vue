<template>
  <div class="container">
    <h1>CADASTRO DO TUTOR E PET</h1>
    <form @submit.prevent="handleRegister">

      <!-- Dados do Tutor -->
      <h2>TUTOR</h2>
      <InputField v-model="tutorName" placeholder="Nome do Tutor" />
      <InputField v-model="tutorEmail" placeholder="Email" type="email" />
      <InputField v-model="tutorPassword" placeholder="Senha" type="password" />

      <!-- Confirmar senha -->
      <InputField v-model="confirmPassword" placeholder="Confirmar Senha" type="password" />

      <!-- Regras da senha -->
      <ul class="password-rules">
        <li :class="{ valid: passwordRules.minLength }">No mínimo 8 caracteres.</li>
        <li :class="{ valid: passwordRules.number }">Pelo menos um número.</li>
        <li :class="{ valid: passwordRules.lowercase }">Pelo menos um caractere minúsculo.</li>
        <li :class="{ valid: passwordRules.uppercase }">Pelo menos um caractere maiúsculo.</li>
        <li :class="{ valid: passwordRules.special }">
          Pelo menos um caractere especial (!@#&()–[{}]:;',?/*~$^+=<>_`|%).
        </li>
      </ul>

      <!-- Dados do Pet -->
      <h2>PET</h2>
      <InputField v-model="petName" placeholder="Nome do Pet" />
      <InputField v-model="petSpecies" placeholder="Espécie" />
      <InputField v-model="petBreed" placeholder="Raça" />
      <InputField v-model="petAge" placeholder="Idade" type="number" />

      <!-- Aceite da política -->
      <label class="terms-label">
        <input type="checkbox" v-model="acceptTerms" />
        Aceito a Política de Privacidade
        <router-link to="/privacy-policy" class="terms-link">
          (Leia os termos)
        </router-link>
      </label>

      <Button :disabled="!acceptTerms" type="submit">Cadastrar</Button>
    </form>
  </div>
</template>


<script setup>
import { ref, computed } from 'vue'
import InputField from '../components/InputField.vue'
import Button from '../components/Button.vue'

const tutorName = ref('')
const tutorEmail = ref('')
const tutorPassword = ref('')
const confirmPassword = ref('')

const petName = ref('')
const petSpecies = ref('')
const petBreed = ref('')
const petAge = ref('')

const acceptTerms = ref(false)

// Regras da senha
const passwordRules = computed(() => ({
  minLength: tutorPassword.value.length >= 8,
  number: /\d/.test(tutorPassword.value),
  lowercase: /[a-z]/.test(tutorPassword.value),
  uppercase: /[A-Z]/.test(tutorPassword.value),
  special: /[!@#&()–[{}\]:;',?/*~$^+=<>_`|%]/.test(tutorPassword.value)
}))

const passwordValid = computed(() =>
  Object.values(passwordRules.value).every(Boolean)
)

const handleRegister = () => {
  if (!tutorName.value || !tutorEmail.value || !tutorPassword.value ||
      !petName.value || !petSpecies.value || !petBreed.value || !petAge.value) {
    alert('Preencha todos os campos!')
    return
  }

  if (!passwordValid.value) {
    alert('A senha não atende aos requisitos de segurança!')
    return
  }

  if (tutorPassword.value !== confirmPassword.value) {
    alert('As senhas não coincidem!')
    return
  }

  if (!acceptTerms.value) {
    alert('Aceite os termos antes de cadastrar!')
    return
  }

  alert(`Cadastro realizado com sucesso!\nTutor: ${tutorName.value}\nPet: ${petName.value}`)

  tutorName.value = ''
  tutorEmail.value = ''
  tutorPassword.value = ''
  confirmPassword.value = ''
  petName.value = ''
  petSpecies.value = ''
  petBreed.value = ''
  petAge.value = ''
  acceptTerms.value = false
}
</script>


<style>
/* Container centralizado */
.container {
  max-width: 500px;
  margin: 50px auto;
  padding: 40px;
  background-color: #f7f7f7;
  border-radius: 10px;
  box-shadow: 0 5px 20px rgba(0,0,0,0.1);
  display: flex;
  flex-direction: column;
  align-items: stretch; /* importante */
  gap: 20px;
}

.password-rules {
  font-size: 13px;
  margin-bottom: 10px;
  color: #777;
}

.password-rules li.valid {
  color: green;
  font-weight: 600;
}

/* Títulos */
h1, h2 {
  text-align: center;
  color: #42b983;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  margin-bottom: 15px;
}

/* Form */
form {
  display: flex;
  flex-direction: column;
  width: 100%;
  gap: 12px;
}

.form-group {
  display: flex;
  flex-direction: column; /* label em cima do input */
  align-items: flex-start; /* alinha à esquerda */
}

/* Inputs */
.InputField {
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
}

input[type="text"],
input[type="email"],
input[type="password"],
input[type="number"],
textarea {
  width: 100%;
  padding: 10px;
  box-sizing: border-box; /* garante que padding não quebre a largura */
  border: 1px solid #ccc;
  border-radius: 5px;
}

/* Checkbox de termos */
.terms-label {
  display: flex;
  align-items: center;
  gap: 10px;              /* Espaço entre checkbox e texto */
  width: 100%;
  padding: 0 10px;        /* Mesma margem interna dos inputs */
  box-sizing: border-box;
  margin: 0;
  font-size: 14px;
}

.terms-link {
  color: #42b983;
  text-decoration: underline;
  margin-left: 0;      /* Remove qualquer margem lateral */
}

.terms-label input[type="checkbox"] {
  margin: 0; /* Remove margens extras do checkbox */
}

/* Botão */
button {
  padding: 10px;
  border-radius: 6px;
  border: none;
  background-color: #42b983;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s;
}

button:disabled {
  background-color: #a0d6b8;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
  background-color: #369a6f;
}

/* Responsividade */
@media (max-width: 600px) {
  .container {
    margin: 20px;
    padding: 15px;
  }

  form {
    gap: 10px;
  }

  h1 {
    font-size: 1.6rem;
  }

  h2 {
    font-size: 1.2rem;
  }

  .terms-label {
    flex-direction: column;
    align-items: flex-start;
    padding: 0;        /* Remove padding para mobile */
  }

  .terms-link {
    margin-left: 0;
  }
}
</style>
