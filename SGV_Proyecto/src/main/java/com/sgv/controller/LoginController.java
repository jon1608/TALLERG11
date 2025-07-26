package com.sgv.controller;

import com.sgv.model.Usuario;
import com.sgv.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                @RequestParam String contrasena,
                                HttpSession session,
                                Model model) {
        var usuario = usuarioService.autenticar(correo, contrasena);
        if (usuario.isPresent()) {
            session.setAttribute("usuario", usuario.get());
            String rol = usuario.get().getRol();
            switch (rol) {
                case "ADMIN" -> {
                    return "redirect:/dashboard";
                }
                case "MECANICO" -> {
                    return "redirect:/mecanico";
                }
                case "CLIENTE" -> {
                    return "redirect:/cliente";
                }
            }
        }
        model.addAttribute("error", "Credenciales incorrectas");
        return "login";
    }

    @GetMapping("/")
    public String redirigirRaiz() {
        return "redirect:/login";
    }
    
    @GetMapping("/dashboard")
    public String mostrarDashboard() {
        return "dashboard";
    }

}
