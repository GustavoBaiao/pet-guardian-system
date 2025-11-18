import api from './api';

export const register = async (userData) => {
  const response = await api.post('/auth/register', userData);
  return response.data;
};

export const login = async (credentials) => {
  const response = await api.post('/auth/login', credentials);
  // Armazena o token JWT
  localStorage.setItem('token', response.data.token);
  return response.data;
};

export const getUserInfo = async () => {
  const token = localStorage.getItem('token');
  const response = await api.get('/usuarios/me', {
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.data;
};
