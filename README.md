# pet-guardian-system

# 🐾 Pet Guardian System

![Status do Projeto](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.1.0-green)
![Vue 3](https://img.shields.io/badge/Vue-3.3.0-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)

O **Pet Guardian System** é um sistema para gerenciar informações de pets de forma simples, segura e organizada. Permite cadastro de usuários, pets e gerenciamento completo de dados, com autenticação via **JWT**.

---

## 🚀 Funcionalidades

- Cadastro e login de usuários
- Cadastro, edição e exclusão de pets
- Listagem de pets por usuário
- Recuperação de senha por e-mail
- Controle de acesso via JWT
- Integração com banco de dados MySQL
- Interface amigável com Vue.js

---

## 🛠 Tecnologias Utilizadas

**Backend:**  
Java 17, Spring Boot, Spring Security, MySQL, JWT

**Frontend:**  
Vue 3, Vite, Axios, Tailwind CSS

**Outros:**  
Git/GitHub, Postman

---

## 📦 Pré-requisitos

- Java 17 ou superior  
- Node.js e npm  
- MySQL  
- Git  

---

## 💻 Instalação

### Backend

```bash
git clone https://github.com/seu-usuario/pet-guardian-system.git
cd pet-guardian-system/backend

### Configure o arquivo application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/petguardian?useSSL=false&serverTimezone=UTC
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA

## 📝 Estrutura do Projeto
pet-guardian-system/
├── backend/       # Código do backend Spring Boot
├── frontend/      # Código do frontend Vue 3
├── README.md      # Documentação do projeto
└── .gitignore
