package com.contagest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de ContaGest (modulo de clientes).
 *
 * <p>La anotacion {@code @SpringBootApplication} activa la configuracion automatica de
 * Spring Boot: servidor web embebido, Spring MVC, Thymeleaf y acceso a datos con JPA.
 * Tambien busca de forma automatica los componentes del paquete {@code com.contagest}
 * y de sus subpaquetes (controladores, servicios y repositorios).
 */
@SpringBootApplication
public class ContaGestApplication {

    public static void main(String[] args) {
        // Inicia el contexto de Spring y el servidor Tomcat embebido
        SpringApplication.run(ContaGestApplication.class, args);
    }
}
