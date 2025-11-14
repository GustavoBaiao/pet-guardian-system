<template>
  <button :disabled="disabled" @click="$emit('btn-click')" class="btn">
    <slot />
  </button>
</template>


<script setup>
const props = defineProps({
  disabled: { type: Boolean, default: false }
})
</script>

<style>
.btn {
  padding: 10px 20px;
  background: #42b983;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}
.btn:disabled {
  background: gray;
  cursor: not-allowed;
}
</style>
