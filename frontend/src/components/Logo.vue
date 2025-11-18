<template>
  <div class="logo-wrapper">
    <picture>
      <source srcset="/cadastrapets.webp" type="image/webp" />
      <img src="/cadastrapets.png" alt="CadastraPets Logo" class="logo" />
    </picture>
  </div>
</template>

<style scoped>
.logo-wrapper {
  width: 100%;
  display: flex;
  justify-content: center;
  padding: 20px;
}

/* Responsividade e animação */
.logo {
  width: 90%;
  max-width: 350px;
  height: auto;
  transition: transform 0.3s ease, filter 0.3s ease;
  filter: drop-shadow(0px 4px 6px rgba(0, 0, 0, 0.2));
}

.logo:hover {
  transform: scale(1.05);
  filter: drop-shadow(0px 6px 10px rgba(0, 0, 0, 0.3));
}

/* Telas maiores */
@media (min-width: 768px) {
  .logo {
    max-width: 550px;
  }
}
</style>
