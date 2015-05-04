package eu.cloudopting.events.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Aceasta exceptie este aruncata cand se incearca instantierea unei clase utilitare.
 * @author mihait
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ClassInstantiationException extends RuntimeException {

    private static final long serialVersionUID = 7463175396911522278L;

    /**
     * Constructor default.
     */
    public ClassInstantiationException() {
        super("Aceasta clasa nu poate fi instantiata!");
    }

    /**
     * Constructor cu parametru - tipul clasei
     * @param clazz - clasa din care este aruncata exceptia
     */
    public ClassInstantiationException(Class clazz) {
        super("Clasa " + clazz.getSimpleName() + " nu poate fi instantiata!");
    }

    /**
     * Constructor cu parametru - tipul clasei + cauza exceptiei
     * @param clazz - clasa din care este aruncata exceptia
     * @param cause - cauza
     */
    public ClassInstantiationException(Class clazz, final Throwable cause) {
        super("Clasa " + clazz.getSimpleName() + " nu poate fi instantiata!", cause);
    }
}
