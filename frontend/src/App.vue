<template>
  <Navbar />

  <router-view v-slot="{ Component }">
    <transition name="fade" mode="out-in">
      <component :is="Component" />
    </transition>
  </router-view>
</template>

<script setup>
import Navbar from './components/Navbar.vue'
</script>

<style>
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  background: url('/src/assets/bg.png') no-repeat center center fixed;
  background-size: cover;
}

/* animação aplicada ao componente da página */
.page-view.card-enter-active,
.page-view.card-leave-active {
  transition: all 0.35s ease;
  position: relative;
}

.page-view.card-enter-from {
  opacity: 0;
  transform: scale(0.95) translateY(15px);
  filter: blur(4px);
}

.page-view.card-leave-to {
  opacity: 0;
  transform: scale(1.03) translateY(-10px);
  filter: blur(6px);
}
</style>



