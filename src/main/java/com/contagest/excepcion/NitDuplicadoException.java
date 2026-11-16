package com.contagest.excepcion;

/**
 * Se lanza cuando se intenta guardar un cliente con un NIT que ya esta registrado.
 */
public class NitDuplicadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NitDuplicadoException(String nit) {
        super("Ya existe un cliente con el NIT " + nit);
    }
}
