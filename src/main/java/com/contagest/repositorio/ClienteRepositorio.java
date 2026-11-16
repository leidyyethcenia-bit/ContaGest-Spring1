package com.contagest.repositorio;

import com.contagest.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de clientes con Spring Data JPA.
 *
 * <p>Al extender {@link JpaRepository}, Spring genera automaticamente la implementacion
 * de las operaciones CRUD (save, findById, findAll, deleteById, existsById, count...).
 * Los metodos adicionales se declaran solo con su nombre: Spring los convierte en
 * consultas SQL (por eso las palabras clave findBy, existsBy... van en ingles y los
 * nombres de los atributos coinciden con los de la entidad Cliente).
 */
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

    /** SELECT ... WHERE nit = ? */
    Optional<Cliente> findByNit(String nit);

    /** Indica si ya existe un cliente con ese NIT (para evitar duplicados al registrar). */
    boolean existsByNit(String nit);

    /** Igual que existsByNit, pero ignora al propio cliente (para evitar duplicados al editar). */
    boolean existsByNitAndIdNot(String nit, Long id);

    /** SELECT ... WHERE LOWER(razon_social) LIKE %texto% ORDER BY razon_social */
    List<Cliente> findByRazonSocialContainingIgnoreCaseOrderByRazonSocialAsc(String texto);
}
