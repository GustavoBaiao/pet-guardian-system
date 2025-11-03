<template>
  <Navbar />
  <router-view />
</template>

<script setup>
import Navbar from './components/Navbar.vue'

</script>

<style>
body { font-family: Arial, sans-serif; margin: 0; padding: 0; 
  background: url('/src/assets/bg.png') no-repeat center center fixed;
  background-size: cover;
}
</style>
