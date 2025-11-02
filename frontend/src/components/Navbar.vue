<template>
  <nav class="navbar">
    <router-link to="/">Home</router-link> |
    <router-link to="/login">Login</router-link> |
    <router-link to="/register">Cadastro</router-link> |
    <router-link to="/privacy-policy">Política de Privacidade</router-link>
  </nav>
</template>

<script setup></script>

<style>
.navbar {
  background: #42b983;
  color: white;
  padding: 10px;
}
.navbar a {
  color: white;
  margin-right: 10px;
  text-decoration: none;
}
.navbar a:hover { text-decoration: underline; }
</style>
