
package com.sgv.controller;

import com.sgv.model.Usuario;
import com.sgv.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        // Asignar el rol manualmente, ya que no se muestra en el formulario
        usuario.setRol("ADMIN");

        usuarioRepository.save(usuario);
        return "redirect:/login";
    }

}
