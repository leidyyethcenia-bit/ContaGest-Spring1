package com.contagest.excepcion;

/**
 * Se lanza cuando se busca un cliente que no existe.
 */
public class ClienteNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ClienteNoEncontradoException(Long id) {
        super("No existe un cliente con id " + id);
    }
}
