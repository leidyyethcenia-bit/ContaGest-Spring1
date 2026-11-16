package com.contagest.modelo;

/**
 * Tipo de persona ante la DIAN.
 */
public enum TipoPersona {

    NATURAL("Natural"),
    JURIDICA("Juridica");

    private final String descripcion;

    TipoPersona(String descripcion) {
        this.descripcion = descripcion;
    }

    /** Texto que se muestra al usuario en las paginas. */
    public String getDescripcion() {
        return descripcion;
    }
}
