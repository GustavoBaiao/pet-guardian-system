<template>
  <div class="home-container">
    <img src="/cadastrapets.png" alt="Pets" class="home-image" />
  </div>
</template>

<script setup>
</script>

<style>
.home-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh; /* altura da tela */
}

.home-image {
  max-width: 80%;  /* ajusta para caber na tela */
  max-height: 80%;
  border-radius: 10px;
  object-fit: cover;
}
</style>

