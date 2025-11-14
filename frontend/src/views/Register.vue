<template>
  <div class="auth-wrapper">
    <div class="auth-content">

      <!-- Imagem -->
      <div class="auth-image-side">
        <img src="/src/assets/cadastrapets.png" alt="Cadastro Pets" />
      </div>

      <!-- Formulário com scroll interno -->
      <div class="auth-form-side">
        <h1>CADASTRO DO TUTOR E PET</h1>

        <form @submit.prevent="handleRegister" class="form-scroll">

          <h2>TUTOR</h2>
          <InputField v-model="tutorName" placeholder="Nome do Tutor" label="Nome do Tutor:" />
          <InputField v-model="tutorEmail" placeholder="Seu Email" label="Email:" type="email" />
          <InputField v-model="tutorPassword" type="password" label="Senha:" placeholder="Sua senha" />
          <InputField v-model="confirmPassword" type="password" label="Confirmação de senha:" placeholder="Repita sua senha" />

          <!-- Regras da senha -->
          <ul class="password-rules">
            <li :class="{ valid: passwordRules.minLength }">No mínimo 8 caracteres.</li>
            <li :class="{ valid: passwordRules.number }">Pelo menos um número.</li>
            <li :class="{ valid: passwordRules.lowercase }">Pelo menos um caractere minúsculo.</li>
            <li :class="{ valid: passwordRules.uppercase }">Pelo menos um caractere maiúsculo.</li>
            <li :class="{ valid: passwordRules.special }">
              Pelo menos um caractere especial (!@#&()–[{}]:;',?/*~$^+=<>_`|%).
            </li>
            <li :class="{ valid: passwordsMatch }">As senhas coincidem.</li>
          </ul>

          <h2>PET</h2>
          <InputField v-model="petName" placeholder="Nome do Pet" label="Nome do Pet:" />
          <InputField v-model="petSpecies" placeholder="Nome da Espécie" label="Espécie:" />
          <InputField v-model="petBreed" placeholder="Nome da Raça" label="Raça:"/>
          <InputField v-model="petAge" placeholder="Coloque a Idade" label="Idade:" type="number" />
          <InputField v-model="petVaccines" placeholder="Ex: Raiva, V8, Gripe Canina" label="Vacinas que pet já tomou:"/>


          <!-- Termos -->
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

    </div>
  </div>
</template>



<script setup>
import { ref, computed } from 'vue'
import InputField from '../components/InputField.vue'
import Button from '../components/Button.vue'
import api from '../services/api'
import { useAuthStore } from '../store/auth'


const auth = useAuthStore()

const tutorName = ref('')
const tutorEmail = ref('')
const tutorPassword = ref('')
const confirmPassword = ref('')

const petName = ref('')
const petSpecies = ref('')
const petBreed = ref('')
const petAge = ref('')
const petVaccines = ref('')


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

const passwordsMatch = computed(() =>
  tutorPassword.value !== '' &&
  confirmPassword.value !== '' &&
  tutorPassword.value === confirmPassword.value
)


const handleRegister = async () => {
  if (!tutorName.value || !tutorEmail.value || !tutorPassword.value ||
      !petName.value || !petSpecies.value || !petBreed.value || !petAge.value || !petVaccines.value) {
    alert('Preencha todos os campos!')
    return
  }

  if (!passwordValid.value || !passwordsMatch.value) {
    alert('Senha inválida ou não coincide!')
    return
  }

  if (!acceptTerms.value) {
    alert('Aceite os termos antes de cadastrar!')
    return
  }

  try {
    // ✅ Payload atualizado
    const payload = {
      nome: tutorName.value,
      email: tutorEmail.value,
      senha: tutorPassword.value,
      aceitouTermos: acceptTerms.value,
      pets: [
        {
          nome: petName.value,
          especie: petSpecies.value,
          raca: petBreed.value,
          idade: parseInt(petAge.value),
          vacinas: petVaccines.value
        }
      ]
    }

    const response = await api.post('/auth/register', payload)

    // 🔹 Salvar token e usuário no store e localStorage
    auth.usuario = response.data.usuario
    auth.token = response.data.token
    localStorage.setItem('token', response.data.token)

    alert('Cadastro realizado com sucesso!')
    console.log('Usuário cadastrado:', auth.usuario)
    console.log('Token JWT:', auth.token)

  } catch (error) {
    console.error(error)
    alert('Erro ao cadastrar! Verifique os dados e tente novamente.')
  }
}


</script>


<style scoped>
.auth-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 0;
  width: 100%;
}

/* Card central */
/* Card central */
.auth-content {
  display: flex;
  gap: 30px;
  width: 90%;
  max-width: 1100px;
  height: 80vh; /* altura fixa */
  background: rgba(255, 255, 255, 0.88);
  backdrop-filter: blur(8px);
  border-radius: 15px;
  padding: 40px 50px;
  box-shadow: 0px 4px 25px rgba(0,0,0,0.12);
  align-items: center;
  overflow: hidden; /* impede scroll fora do form */
}


/* Imagem */
.auth-image-side {
  flex: 1.2;
  display: flex;
  justify-content: center;
}
.auth-image-side img {
  width: 100%;
  max-width: 450px;
}

/* Form */
.auth-form-side {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* Scroll interno */
.form-scroll {
  width: 100%;
  max-width: 380px;
  max-height: 70vh; /* limite para criar scroll */
  overflow-y: auto; /* scroll no form */
  padding-right: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}


.form-scroll input,
.form-scroll select {
  width: 85%; /* menor */
  padding: 8px 10px;
  font-size: 14px;
  border-radius: 8px;
  border: 1px solid rgba(0,0,0,0.2);
  outline: none;
  background: rgba(255,255,255,0.8);
  backdrop-filter: blur(6px);
  transition: 0.2s ease;
}

.form-scroll .input-wrapper {
  width: 100%;
  text-align: left;
}



.form-scroll input:focus,
.form-scroll select:focus {
  border-color: #6c63ff;
  transform: scale(1.02);
}

button {
  width: 85%;
  padding: 10px;
  font-size: 15px;
  border-radius: 8px;
  margin-top: 5px;
}

/* Regras senha */
.password-rules {
  font-size: 13px;
  color: #777;
}
.password-rules li.valid {
  color: green;
  font-weight: bold;
}

/* Títulos */
h1 {
  text-align: center;
  color: #42b983;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  margin-bottom: 8px;
  font-size: 28px;
  font-weight: 700;
}
h2 {
  color: #42b983;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  font-size: 18px;
  font-weight: bold;
  margin: 15px 0 5px;
}


/* Container da linha termos */
.terms-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  text-align: left;
  width: 100%;
  justify-content: center;
  margin-top: 10px;
}

/* Checkbox */
.terms-row input[type="checkbox"] {
  width: 16px;
  height: 16px;
  cursor: pointer;
}

.terms-row label {
  cursor: pointer;
  line-height: 1.3;
}

.terms-row a {
  color: #cce6ff;
  text-decoration: underline;
}
/* Mobile */
@media (max-width: 900px) {
  .auth-content {
    flex-direction: column;
    padding: 25px;
  }
  .auth-image-side img { max-width: 300px; }
  .form-scroll { max-height: 60vh; }
}

.auth-form-side,
.auth-form-side h1,
.auth-form-side h2,
.form-scroll {
  text-align: center;
}

.password-rules {
  text-align: center;
  list-style: none;
  padding: 0;
}
.password-rules li {
  margin-bottom: 4px;
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

/* aplica animação no card do cadastro */
.auth-content {
  animation: fadeSlideUp 0.6s ease-out;
}

.form-scroll > * {
  animation: fadeSlideUp 0.6s ease backwards;
}
.form-scroll > *:nth-child(1) { animation-delay: 0.1s; }
.form-scroll > *:nth-child(2) { animation-delay: 0.2s; }
.form-scroll > *:nth-child(3) { animation-delay: 0.3s; }
.form-scroll > *:nth-child(4) { animation-delay: 0.4s; }
.form-scroll > *:nth-child(5) { animation-delay: 0.5s; }
.form-scroll > *:nth-child(6) { animation-delay: 0.6s; }
.form-scroll > *:nth-child(7) { animation-delay: 0.7s; }
.form-scroll > *:nth-child(8) { animation-delay: 0.8s; }
.form-scroll > *:nth-child(9) { animation-delay: 0.9s; }
.form-scroll > *:nth-child(10) { animation-delay: 1s; }


</style>

