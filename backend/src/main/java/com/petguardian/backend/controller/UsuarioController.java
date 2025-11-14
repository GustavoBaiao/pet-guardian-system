package com.petguardian.backend.controller;

import com.petguardian.backend.model.Usuario;
import com.petguardian.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Registro do usuário
    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@RequestParam String email, @RequestParam String senha) {
        Usuario usuario = usuarioService.registrarUsuario(email, senha);

        // TODO: enviar e-mail com link de confirmação
        String linkConfirmacao = "http://localhost:8080/usuarios/confirmar?email="
                + usuario.getEmail() + "&token=" + usuario.getTokenEmail();
        System.out.println("Link para confirmação de e-mail: " + linkConfirmacao);

        return ResponseEntity.ok("Usuário registrado. Confira seu e-mail para confirmar.");
    }

    // Confirmação do e-mail
    @GetMapping("/confirmar")
    public ResponseEntity<String> confirmarEmail(@RequestParam String email, @RequestParam String token) {
        boolean sucesso = usuarioService.confirmarEmail(email, token);
        if (sucesso) {
            return ResponseEntity.ok("E-mail confirmado com sucesso!");
        } else {
            return ResponseEntity.status(403).body("Token inválido ou expirado.");
        }
    }
}
