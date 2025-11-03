<template>
  <div class="input-wrapper">
    <label v-if="label">{{ label }}</label>

    <div class="input-container">
      <input
        :type="showPassword && type === 'password' ? 'text' : type"
        :placeholder="placeholder"
        v-model="inputValue"
      />

      <button 
  v-if="type === 'password'"
  class="toggle-btn"
  type="button"
  @click="showPassword = !showPassword"
>
  <img
    :src="showPassword ? '/src/assets/eye-open.svg' : '/src/assets/eye-closed.svg'"
    alt="Mostrar senha"
    class="eye-icon"
  />
</button>

    </div>
  </div>
</template>


<script setup>
import { ref, watch } from "vue";

const props = defineProps({
  modelValue: String,
  placeholder: String,
  type: {
    type: String,
    default: "text"
  },
  label: String
});

const emit = defineEmits(["update:modelValue"]);
const showPassword = ref(false);

// Valor interno do input
const inputValue = ref(props.modelValue);

// Sempre que a prop mudar (vindo de fora), atualiza o campo
watch(() => props.modelValue, (val) => {
  inputValue.value = val;
});

// Sempre que o usuário digitar, emitimos o valor novo
watch(inputValue, (val) => {
  emit("update:modelValue", val);
});
</script>


<style scoped>
.input-wrapper {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.input-container {
  position: relative;
  width: 100%;
}

input {
  width: 100%;
  padding: 10px 40px 10px 10px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 5px;
}


.toggle-btn {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
}

.eye-icon {
  width: 20px;
  height: 20px;
}

</style>
