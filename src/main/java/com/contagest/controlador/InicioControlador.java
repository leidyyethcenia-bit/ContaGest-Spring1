package com.contagest.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de la pagina de inicio.
 */
@Controller
public class InicioControlador {

    /** GET / : muestra la plantilla templates/inicio.html. */
    @GetMapping("/")
    public String mostrarInicio() {
        return "inicio";
    }
}
