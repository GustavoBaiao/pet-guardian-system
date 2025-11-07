// auth.js
const readline = require("readline");
const { spawn } = require("child_process");

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

// Recomendação: use uma variável de ambiente em vez de hardcode.
// Ex: set DEV_PASSWORD=minhaSenha  (Windows) ou export DEV_PASSWORD=...
const PASSWORD = process.env.DEV_PASSWORD || "felipe123"; // troque aqui ou use env

rl.question("Digite a senha para iniciar o ambiente DEV: ", (input) => {
  rl.close();
  const answer = (input || "").toString().trim();

  if (answer === PASSWORD) {
    console.log("✅ Senha correta — iniciando Vite...\n");

    // Inicia Vite via npx (usa node_modules/.bin se houver), em shell para compatibilidade.
    const cmd = process.platform === "win32" ? "npx.cmd" : "npx";
    const child = spawn(cmd, ["vite"], { stdio: "inherit", shell: false });

    child.on("exit", (code) => {
      process.exit(code);
    });

    child.on("error", (err) => {
      console.error("Erro ao tentar iniciar o Vite:", err);
      process.exit(1);
    });

  } else {
    console.log("❌ Senha incorreta. Saindo...");
    process.exit(1);
  }
});
