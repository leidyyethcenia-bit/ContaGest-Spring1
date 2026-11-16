package com.contagest.controlador;

import com.contagest.modelo.Cliente;
import com.contagest.servicio.ClienteServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API REST de clientes: devuelve y recibe datos en formato JSON.
 *
 * <p>Permite que otras aplicaciones (por ejemplo una app movil) usen el modulo de clientes.
 * Los errores se convierten en respuestas JSON en {@link ManejadorErroresApi}.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteApiControlador {

    private final ClienteServicio clienteServicio;

    public ClienteApiControlador(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    /** GET /api/clientes  o  GET /api/clientes?q=texto */
    @GetMapping
    public List<Cliente> listar(@RequestParam(name = "q", required = false) String busqueda) {
        return clienteServicio.listar(busqueda);
    }

    /** GET /api/clientes/{id} */
    @GetMapping("/{id}")
    public Cliente consultar(@PathVariable Long id) {
        return clienteServicio.consultarPorId(id);
    }

    /** POST /api/clientes : crea un cliente y responde 201 (Created). */
    @PostMapping
    public ResponseEntity<Cliente> crear(@Valid @RequestBody Cliente cliente) {
        Cliente creado = clienteServicio.registrar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** PUT /api/clientes/{id} : actualiza un cliente. */
    @PutMapping("/{id}")
    public Cliente actualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        return clienteServicio.actualizar(id, cliente);
    }

    /** DELETE /api/clientes/{id} : elimina un cliente y responde 204 (No Content). */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        clienteServicio.eliminar(id);
    }
}
