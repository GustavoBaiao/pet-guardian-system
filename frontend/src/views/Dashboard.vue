<template>
  <div class="container" v-if="auth.usuario">
    <h1>Bem-vindo, {{ auth.usuario.nome || auth.usuario.email }}</h1>
    <p>Email: {{ auth.usuario.email }}</p>

    <h2>Meus Pets</h2>

    <ul v-if="pets.length > 0">
      <li v-for="pet in pets" :key="pet.id">
        <span>{{ pet.nome }} - {{ pet.especie }} - {{ pet.raca }} - {{ pet.idade }} - {{ pet.vacinas }}</span>
        <Button class="delete-btn" @click="excluirPet(pet.id)">Excluir</Button>
      </li>
    </ul>

    <p v-else>Nenhum pet cadastrado ainda.</p>

    <div class="buttons">

      <Button @click="toggleFormulario">
        {{ mostrarFormulario ? 'Cancelar' : 'Adicionar Pet' }}
      </Button>

      <Button @click="excluirPets">
        Excluir todos os pets
      </Button>

       <!-- Novo botão -->
      <Button class="delete-btn" @click="excluirUsuarioComPets">
        Excluir meus Dados e Pets
      </Button>

      <Button @click="handleLogout">
        Sair
      </Button>

    </div>

    <!-- FORMULÁRIO DE ADIÇÃO DE PET -->
    <form
      v-if="mostrarFormulario"
      @submit.prevent="adicionarPet"
      class="pet-form"
    >
      <h3>Adicionar Novo Pet</h3>

      <InputField
        v-model="novoPet.nome"
        label="Nome do Pet"
        placeholder="Nome do Pet"
        required
      />

      <InputField
        v-model="novoPet.especie"
        label="Espécie"
        placeholder="Ex: Cachorro, Gato..."
        required
      />

      <InputField
        v-model="novoPet.raca"
        label="Raça"
        placeholder="Ex: Golden, Siamês..."
        required
      />

      <InputField
        v-model="novoPet.idade"
        type="number"
        label="Idade"
        placeholder="Idade do Pet"
        required
      />

      <InputField
        v-model="novoPet.vacinas"
        label="Vacinas"
        placeholder="Ex: Raiva, V8, Gripe Canina"
      />

      <Button type="submit">Salvar Pet</Button>
    </form>
  </div>

  <div v-else class="container">
    <p>Carregando usuário...</p>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'
import api from '../services/api'
import Button from '../components/Button.vue'
import InputField from '../components/InputField.vue'

const auth = useAuthStore()
const router = useRouter()

// Estado dos pets e do formulário
const pets = ref([])
const mostrarFormulario = ref(false)
const novoPet = ref({
  nome: '',
  especie: '',
  raca: '',
  idade:'',
  vacinas: ''
})


// 🔹 Alterna o formulário
const toggleFormulario = () => {
  mostrarFormulario.value = !mostrarFormulario.value
  console.log('📋 mostrarFormulario agora é:', mostrarFormulario.value)
}

// 🔹 Carrega pets do usuário autenticado
const loadPets = async () => {
  try {
    const response = await api.get('/pets/meus-pets')
    console.log('Pets carregados:', response.data)
    pets.value = response.data
  } catch (err) {
    console.error('Erro ao carregar pets:', err)
    alert('Erro ao carregar pets: ' + (err.response?.data || err.message))
  }
}

const adicionarPet = async () => {
  try {
    if (!novoPet.value.nome ||
        !novoPet.value.especie ||
        !novoPet.value.raca ||
        !novoPet.value.idade) {
      alert('Preencha todos os campos!');
      return;
    }

    const response = await api.post('/pets/criar-pets', novoPet.value);

    pets.value.push(response.data);

    novoPet.value = {
      nome: '',
      especie: '',
      raca: '',
      idade: '',
      vacinas: ''
    };

    mostrarFormulario.value = false;
    alert('Pet adicionado com sucesso!');
  } catch (err) {
    console.error('Erro ao adicionar pet:', err);
    alert('Erro ao adicionar pet: ' + (err.response?.data || err.message));
  }
}

// 🔹 Excluir usuário e todos os pets
const excluirUsuarioComPets = async () => {
  try {
    const confirmacao = confirm(
      'Deseja realmente excluir todos os seus dados e pets? Esta ação não pode ser desfeita!'
    )
    if (!confirmacao) return

    await api.delete('/pets/delete') // rota do backend
    alert('Todos os seus dados e pets foram excluídos com sucesso!')

    // Limpa store e redireciona para login
    auth.logout()
    router.push('/login')
  } catch (err) {
    console.error('Erro ao excluir usuário e pets:', err)
    alert('Erro ao excluir usuário e pets: ' + (err.response?.data || err.message))
  }
}

// 🔹 Excluir pet específico
const excluirPet = async (id) => {
  try {
    await api.delete(`/pets/${id}`)
    pets.value = pets.value.filter(p => p.id !== id)
    alert('Pet excluído com sucesso!')
  } catch (err) {
    console.error('Erro ao excluir pet:', err)
    alert('Erro ao excluir pet: ' + (err.response?.data || err.message))
  }
}

// 🔹 Excluir todos os pets
const excluirPets = async () => {
  try {
    if (!confirm('Deseja excluir todos os pets?')) return
    await api.delete('/pets/deletar-pets')
    pets.value = []
    alert('Pets excluídos com sucesso!')
  } catch (err) {
    console.error('Erro ao excluir pets:', err)
    alert('Erro ao excluir pets: ' + (err.response?.data || err.message))
  }
}

// 🔹 Logout
const handleLogout = () => {
  auth.logout()
  router.push('/login')
}

// 🔹 Montagem do componente
onMounted(async () => {
  auth.carregarUsuarioSalvo()
  if (!auth.estaAutenticado()) {
    router.push('/login')
    return
  }
  await loadPets()
})
</script>


<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
  text-align: center;
}

h1 {
  color: #42b983;
  margin-bottom: 8px;
}

h2 {
  margin-top: 24px;
}

ul {
  list-style: none;
  padding: 0;
  margin-top: 16px;
}

li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  border-bottom: 1px solid #eee;
}

.delete-btn {
  background: #ff6b6b;
  color: white;
  border-radius: 6px;
  padding: 6px 12px;
  font-size: 0.9rem;
}

.buttons {
  margin-top: 24px;
  display: flex;
  gap: 12px;
  justify-content: center;
}

.pet-form {
  margin-top: 24px;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 8px;
  text-align: left;
}
</style>
