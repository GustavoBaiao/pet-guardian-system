<template>
  <input
    :type="type"
    :placeholder="placeholder"
    :value="modelValue"
    @input="$emit('update:modelValue', $event.target.value)"
    class="input-field"
  />
</template>

<script setup>
const props = defineProps({
  modelValue: String,
  placeholder: String,
  type: { type: String, default: 'text' }
})
</script>

<style>
.input-field {
  padding: 8px;
  margin: 5px 0;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>
