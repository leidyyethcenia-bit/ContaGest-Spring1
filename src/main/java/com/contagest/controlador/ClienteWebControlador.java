package com.contagest.controlador;

import com.contagest.excepcion.ClienteNoEncontradoException;
import com.contagest.excepcion.NitDuplicadoException;
import com.contagest.modelo.Cliente;
import com.contagest.modelo.TipoPersona;
import com.contagest.servicio.ClienteServicio;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador de las paginas web de clientes (Spring MVC + Thymeleaf).
 *
 * <p>Cada metodo atiende una URL. Los metodos GET consultan y muestran paginas; los
 * metodos POST reciben los formularios y modifican datos. Devuelven el nombre de la
 * plantilla a mostrar o una redireccion ("redirect:...").
 */
@Controller
@RequestMapping("/clientes")
public class ClienteWebControlador {

    private static final String VISTA_LISTA = "clientes/lista";
    private static final String VISTA_FORMULARIO = "clientes/formulario";
    private static final String VISTA_DETALLE = "clientes/detalle";
    private static final String REDIRIGIR_A_LISTA = "redirect:/clientes";

    private final ClienteServicio clienteServicio;

    public ClienteWebControlador(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    /** GET /clientes  o  GET /clientes?q=texto : lista y busca clientes. */
    @GetMapping
    public String listar(@RequestParam(name = "q", required = false) String busqueda,
                         Model modelo) {
        modelo.addAttribute("clientes", clienteServicio.listar(busqueda));
        modelo.addAttribute("busqueda", busqueda);
        return VISTA_LISTA;
    }

    /** GET /clientes/nuevo : formulario vacio para registrar un cliente. */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model modelo) {
        modelo.addAttribute("cliente", new Cliente());
        modelo.addAttribute("tipos", TipoPersona.values());
        return VISTA_FORMULARIO;
    }

    /** GET /clientes/{id}/editar : formulario cargado con los datos del cliente. */
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model modelo,
                                          RedirectAttributes mensajes) {
        try {
            modelo.addAttribute("cliente", clienteServicio.consultarPorId(id));
            modelo.addAttribute("tipos", TipoPersona.values());
            return VISTA_FORMULARIO;
        } catch (ClienteNoEncontradoException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
            return REDIRIGIR_A_LISTA;
        }
    }

    /** GET /clientes/{id} : detalle de un cliente. */
    @GetMapping("/{id}")
    public String mostrarDetalle(@PathVariable Long id, Model modelo,
                                 RedirectAttributes mensajes) {
        try {
            modelo.addAttribute("cliente", clienteServicio.consultarPorId(id));
            return VISTA_DETALLE;
        } catch (ClienteNoEncontradoException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
            return REDIRIGIR_A_LISTA;
        }
    }

    /**
     * POST /clientes/guardar : recibe el formulario y registra o actualiza el cliente.
     * La anotacion @Valid ejecuta las validaciones de la entidad; los errores quedan
     * en el objeto BindingResult y se muestran junto a cada campo.
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cliente") Cliente cliente,
                          BindingResult resultado, Model modelo,
                          RedirectAttributes mensajes) {
        if (resultado.hasErrors()) {
            modelo.addAttribute("tipos", TipoPersona.values());
            return VISTA_FORMULARIO; // vuelve al formulario mostrando los errores
        }
        try {
            if (cliente.getId() == null) {
                clienteServicio.registrar(cliente);
                mensajes.addFlashAttribute("exito", "Cliente registrado correctamente.");
            } else {
                clienteServicio.actualizar(cliente.getId(), cliente);
                mensajes.addFlashAttribute("exito", "Cliente actualizado correctamente.");
            }
            // Post-Redirect-Get: evita que el formulario se reenvie al recargar la pagina
            return REDIRIGIR_A_LISTA;
        } catch (NitDuplicadoException e) {
            resultado.rejectValue("nit", "nit.duplicado", e.getMessage());
            modelo.addAttribute("tipos", TipoPersona.values());
            return VISTA_FORMULARIO;
        } catch (ClienteNoEncontradoException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
            return REDIRIGIR_A_LISTA;
        }
    }

    /** POST /clientes/{id}/eliminar : elimina un cliente. */
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes mensajes) {
        try {
            clienteServicio.eliminar(id);
            mensajes.addFlashAttribute("exito", "Cliente eliminado correctamente.");
        } catch (ClienteNoEncontradoException e) {
            mensajes.addFlashAttribute("error", e.getMessage());
        }
        return REDIRIGIR_A_LISTA;
    }
}
