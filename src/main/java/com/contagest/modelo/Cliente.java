package com.contagest.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Persona natural o juridica atendida por el despacho contable.
 *
 * <p>Es una entidad JPA: Hibernate la asocia con la tabla {@code clientes} y crea las
 * columnas a partir de sus atributos. Las anotaciones de validacion (@NotBlank, @Pattern...)
 * se comprueban tanto en los formularios web como en la API REST.
 */
@Entity
@Table(name = "clientes")
public class Cliente {

    /** Identificador generado automaticamente por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Numero de Identificacion Tributaria, con digito de verificacion opcional. */
    @NotBlank(message = "El NIT es obligatorio")
    @Pattern(regexp = "^\\d{5,12}(-\\d)?$",
            message = "El NIT debe tener entre 5 y 12 digitos, con digito de verificacion opcional (ej: 900123456-7)")
    @Column(nullable = false, unique = true, length = 15)
    private String nit;

    @NotBlank(message = "La razon social es obligatoria")
    @Size(max = 150, message = "La razon social no puede superar 150 caracteres")
    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;

    @NotNull(message = "Seleccione el tipo de persona")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_persona", nullable = false, length = 10)
    private TipoPersona tipoPersona;

    @NotBlank(message = "El correo electronico es obligatorio")
    @Email(message = "El correo electronico no es valido")
    @Size(max = 100, message = "El correo no puede superar 100 caracteres")
    @Column(nullable = false, length = 100)
    private String correo;

    /** Telefono opcional: vacio o entre 7 y 10 digitos. */
    @Pattern(regexp = "^$|^\\d{7,10}$", message = "El telefono debe tener entre 7 y 10 digitos")
    @Column(length = 15)
    private String telefono;

    @Column(name = "es_activo", nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDate fechaRegistro;

    /** Constructor vacio exigido por JPA y por el enlace de formularios. */
    public Cliente() {
    }

    public Cliente(String nit, String razonSocial, TipoPersona tipoPersona,
                   String correo, String telefono) {
        this.nit = nit;
        this.razonSocial = razonSocial;
        this.tipoPersona = tipoPersona;
        this.correo = correo;
        this.telefono = telefono;
    }

    /** Se ejecuta justo antes del INSERT: asigna la fecha de registro si no tiene. */
    @PrePersist
    void asignarFechaRegistro() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDate.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
