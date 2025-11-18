<template>
  <nav class="navbar">
    <div class="logo">CADASTRAPETS</div>

    <!-- Botão para menu mobile -->
    <button class="menu-btn" @click="toggleMenu">
      ☰
    </button>

    <!-- Links da navbar -->
    <div :class="['nav-links', { active: menuOpen }]">
      <router-link to="/">Home</router-link>
      <router-link to="/login">Login</router-link>
      <router-link to="/register">Cadastro</router-link>
      <router-link to="/privacy-policy">Política de Privacidade</router-link>

      <!-- Botão de tema -->
      <button class="theme-btn" @click="toggleTheme">
        {{ isDark ? '☀️ Claro' : '🌙 Escuro' }}
      </button>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";

// Menu mobile
const menuOpen = ref(false);
const toggleMenu = () => menuOpen.value = !menuOpen.value;

// Tema claro/escuro
const isDark = ref(false);

const toggleTheme = () => {
  isDark.value = !isDark.value;
  document.documentElement.classList.toggle('dark', isDark.value);
}

// Mantém o tema escolhido ao recarregar
onMounted(() => {
  const savedTheme = localStorage.getItem('theme')
  if (savedTheme === 'dark') {
    isDark.value = true
    document.documentElement.classList.add('dark')
  }
})

watch(isDark, (val) => {
  localStorage.setItem('theme', val ? 'dark' : 'light')
})
</script>

<style>
/* Variáveis padrão (tema claro) */
:root {
  --bg-color: #42b983;
  --text-color: white;
  --link-hover: rgba(255, 255, 255, 0.8);
  --btn-bg: white;
  --btn-text: #42b983;
}

/* Tema escuro */
:root.dark {
  --bg-color: #222;
  --text-color: #f5f5f5;
  --link-hover: rgba(245, 245, 245, 0.8);
  --btn-bg: #f5f5f5;
  --btn-text: #222;
}

/* Navbar */
.navbar {
  background: var(--bg-color);
  color: var(--text-color);
  padding: 12px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: background 0.3s, color 0.3s;
}

/* Logo */
.logo {
  font-size: 1.2rem;
  font-weight: bold;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

/* Links container */
.nav-links {
  display: flex;
  gap: 15px;
  align-items: center;
}

/* Links */
.nav-links a {
  color: var(--text-color);
  text-decoration: none;
  font-weight: 500;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  transition: transform 0.2s ease, opacity 0.2s ease;
  padding: 5px 10px;
}

.nav-links a:hover {
  transform: scale(1.2);
  opacity: 0.9;
}

/* Botão de tema */
.theme-btn {
  cursor: pointer;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  background: var(--btn-bg);
  color: var(--btn-text);
  font-weight: 500;
  transition: background 0.3s, color 0.3s;
}

/* Botão mobile */
.menu-btn {
  display: none;
  font-size: 26px;
  background: none;
  border: none;
  color: var(--text-color);
  cursor: pointer;
}

/* Responsividade */
@media (max-width: 768px) {
  .menu-btn {
    display: block;
  }

  .nav-links {
    position: absolute;
    top: 60px;
    right: 0;
    background: var(--bg-color);
    flex-direction: column;
    width: 100%;
    text-align: center;
    padding: 15px 0;
    display: none;
  }

  .nav-links.active {
    display: flex;
  }

  .nav-links a {
    padding: 12px 0;
    width: 100%;
  }

  .theme-btn {
    margin-top: 10px;
    width: 80%;
    align-self: center;
  }
}
</style>
