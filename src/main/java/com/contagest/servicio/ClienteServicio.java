package com.contagest.servicio;

import com.contagest.excepcion.ClienteNoEncontradoException;
import com.contagest.excepcion.NitDuplicadoException;
import com.contagest.modelo.Cliente;
import com.contagest.repositorio.ClienteRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Logica de negocio del modulo de clientes.
 *
 * <p>Es la capa intermedia entre los controladores (web y API) y el repositorio.
 * Aqui se aplican las reglas del negocio, como no permitir NIT repetidos.
 */
@Service
public class ClienteServicio {

    private static final Logger REGISTRO = LoggerFactory.getLogger(ClienteServicio.class);

    private final ClienteRepositorio clienteRepositorio;

    /** Inyeccion de dependencias por constructor: Spring entrega el repositorio ya creado. */
    public ClienteServicio(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    /**
     * Lista los clientes; si se indica un texto, filtra por razon social (SELECT).
     */
    @Transactional(readOnly = true)
    public List<Cliente> listar(String busqueda) {
        if (busqueda == null || busqueda.isBlank()) {
            return clienteRepositorio.findAll(Sort.by("razonSocial"));
        }
        return clienteRepositorio.findByRazonSocialContainingIgnoreCaseOrderByRazonSocialAsc(
                busqueda.trim());
    }

    /** Consulta un cliente por id; lanza excepcion si no existe (SELECT). */
    @Transactional(readOnly = true)
    public Cliente consultarPorId(Long id) {
        return clienteRepositorio.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException(id));
    }

    /** Registra un cliente nuevo (INSERT). */
    @Transactional
    public Cliente registrar(Cliente cliente) {
        if (clienteRepositorio.existsByNit(cliente.getNit())) {
            throw new NitDuplicadoException(cliente.getNit());
        }
        cliente.setId(null); // con id nulo, save() siempre hace un INSERT
        normalizarTelefono(cliente);
        Cliente guardado = clienteRepositorio.save(cliente);
        REGISTRO.info("Cliente registrado con NIT {}", guardado.getNit());
        return guardado;
    }

    /** Actualiza los datos de un cliente existente (UPDATE). */
    @Transactional
    public Cliente actualizar(Long id, Cliente datos) {
        Cliente existente = consultarPorId(id);
        if (clienteRepositorio.existsByNitAndIdNot(datos.getNit(), id)) {
            throw new NitDuplicadoException(datos.getNit());
        }
        // Se copian solo los campos editables; la fecha de registro no cambia
        existente.setNit(datos.getNit());
        existente.setRazonSocial(datos.getRazonSocial());
        existente.setTipoPersona(datos.getTipoPersona());
        existente.setCorreo(datos.getCorreo());
        existente.setTelefono(datos.getTelefono());
        existente.setActivo(datos.isActivo());
        normalizarTelefono(existente);
        Cliente guardado = clienteRepositorio.save(existente);
        REGISTRO.info("Cliente actualizado con id {}", id);
        return guardado;
    }

    /** Elimina un cliente (DELETE). */
    @Transactional
    public void eliminar(Long id) {
        if (!clienteRepositorio.existsById(id)) {
            throw new ClienteNoEncontradoException(id);
        }
        clienteRepositorio.deleteById(id);
        REGISTRO.info("Cliente eliminado con id {}", id);
    }

    /** Guarda el telefono vacio como null para no almacenar cadenas en blanco. */
    private void normalizarTelefono(Cliente cliente) {
        if (cliente.getTelefono() != null && cliente.getTelefono().isBlank()) {
            cliente.setTelefono(null);
        }
    }
}
