package com.contagest.config;

import com.contagest.modelo.Cliente;
import com.contagest.modelo.TipoPersona;
import com.contagest.repositorio.ClienteRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Carga algunos clientes de ejemplo la primera vez que se ejecuta la aplicacion,
 * para que la base de datos no aparezca vacia. Si ya hay datos, no hace nada.
 */
@Component
public class CargadorDatosIniciales implements CommandLineRunner {

    private final ClienteRepositorio clienteRepositorio;

    public CargadorDatosIniciales(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    /** Spring Boot ejecuta este metodo automaticamente al terminar de iniciar. */
    @Override
    public void run(String... args) {
        if (clienteRepositorio.count() > 0) {
            return; // ya hay clientes registrados
        }
        clienteRepositorio.save(new Cliente("900123456-7", "Ferreteria El Tornillo S.A.S.",
                TipoPersona.JURIDICA, "contacto@eltornillo.com", "3101234567"));
        clienteRepositorio.save(new Cliente("1032456789", "Maria Fernanda Gomez",
                TipoPersona.NATURAL, "mfgomez@correo.com", null));
        clienteRepositorio.save(new Cliente("800200300-1", "Panaderia La Pinata Ltda.",
                TipoPersona.JURIDICA, "info@lapinata.co", "6012345678"));
    }
}
