// auth.cjs
const readline = require("readline");
const { spawn } = require("child_process");
const path = require("path");
const fs = require("fs");

const PASSWORD = process.env.DEV_PASSWORD || "felipe123";
const promptText = "Digite a senha para iniciar o ambiente DEV: ";

// Função que lê senha oculta (mask)
function askHidden(question, callback) {
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
    terminal: true
  });

  const stdin = process.stdin;
  let password = "";

  // Muda para modo raw para capturar teclas
  if (stdin.isTTY) stdin.setRawMode(true);
  readline.emitKeypressEvents(stdin, rl);

  process.stdout.write(question);

  function onKeypress(str, key) {
    if (key.name === "return") {
      // Enter
      process.stdout.write("\n");
      if (stdin.isTTY) stdin.setRawMode(false);
      stdin.removeListener("keypress", onKeypress);
      rl.close();
      callback(password);
    } else if (key.name === "backspace") {
      // Backspace
      if (password.length > 0) {
        password = password.slice(0, -1);
        // Remove last * from terminal
        process.stdout.clearLine();
        process.stdout.cursorTo(0);
        process.stdout.write(question + "*".repeat(password.length));
      }
    } else if (key.ctrl && key.name === "c") {
      // Ctrl+C
      process.stdout.write("\n");
      if (stdin.isTTY) stdin.setRawMode(false);
      process.exit(1);
    } else {
      // append char and show asterisk
      password += str;
      process.stdout.write("*");
    }
  }

  stdin.on("keypress", onKeypress);
}

// Tenta iniciar Vite: primeiro via npx, senão tenta node_modules/.bin/vite
function startVite() {
  console.log("✅ Senha correta — iniciando Vite...\n");

  // comando via npx (funciona quando npx está disponível)
  const npxCmd = process.platform === "win32" ? "npx.cmd" : "npx";

  // caminho direto para node_modules vite (fallback)
  const viteLocalWin = path.join("node_modules", ".bin", "vite.cmd");
  const viteLocalUnix = path.join("node_modules", ".bin", "vite");

  // Função para spawn com fallback
  function spawnCmd(cmd, args) {
    const child = spawn(cmd, args, { stdio: "inherit", shell: true });
    child.on("exit", (code) => process.exit(code));
    child.on("error", (err) => {
      console.error(`Erro ao executar ${cmd}:`, err);
      process.exit(1);
    });
  }

  // Tenta npx primeiro
  try {
    spawnCmd(npxCmd, ["vite"]);
  } catch (err) {
    // se npx falhar de forma síncrona (raro), tenta caminho local
    const vitePath = process.platform === "win32" ? viteLocalWin : viteLocalUnix;
    if (fs.existsSync(vitePath)) {
      spawnCmd(vitePath, []);
    } else {
      console.error("Não foi possível iniciar o Vite (npx falhou e vite local não encontrado).");
      console.error("Tente instalar dependências: npm install");
      process.exit(1);
    }
  }
}

// Execução: perguntar senha e comparar
askHidden(promptText, (input) => {
  const provided = (input || "").trim();

  // DEBUG (remover se quiser)
  // console.log("Você digitou:", `"${provided}"`);
  // console.log("Senha esperada:", `"${PASSWORD}"`);

  if (provided === PASSWORD) {
    startVite();
  } else {
    console.log("❌ Senha incorreta. Saindo...");
    process.exit(1);
  }
});
