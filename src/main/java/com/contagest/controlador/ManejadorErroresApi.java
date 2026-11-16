package com.contagest.controlador;

import com.contagest.excepcion.ClienteNoEncontradoException;
import com.contagest.excepcion.NitDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Convierte las excepciones de la API REST en respuestas JSON con el codigo HTTP adecuado.
 * Solo aplica a {@link ClienteApiControlador}; las paginas web manejan sus propios errores.
 */
@RestControllerAdvice(assignableTypes = ClienteApiControlador.class)
public class ManejadorErroresApi {

    /** 404: el cliente no existe. */
    @ExceptionHandler(ClienteNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> manejarNoEncontrado(ClienteNoEncontradoException e) {
        return construirCuerpo(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /** 409: el NIT ya esta registrado. */
    @ExceptionHandler(NitDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> manejarNitDuplicado(NitDuplicadoException e) {
        return construirCuerpo(HttpStatus.CONFLICT, e.getMessage());
    }

    /** 400: los datos enviados no cumplen las validaciones; se listan los errores por campo. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> manejarValidacion(MethodArgumentNotValidException e) {
        Map<String, String> errores = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> cuerpo = construirCuerpo(HttpStatus.BAD_REQUEST,
                "Los datos enviados no son validos");
        cuerpo.put("errores", errores);
        return cuerpo;
    }

    private Map<String, Object> construirCuerpo(HttpStatus estado, String mensaje) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("estado", estado.value());
        cuerpo.put("mensaje", mensaje);
        return cuerpo;
    }
}
