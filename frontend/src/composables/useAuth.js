import { ref } from 'vue';

export const useAuth = () => {
  const usuario = ref(JSON.parse(localStorage.getItem('usuario')) || null);

  const setUsuario = (dados) => {
    usuario.value = dados;
    localStorage.setItem('usuario', JSON.stringify(dados));
  };

  const logout = () => {
    usuario.value = null;
    localStorage.removeItem('usuario');
  };

  return { usuario, setUsuario, logout };
};
