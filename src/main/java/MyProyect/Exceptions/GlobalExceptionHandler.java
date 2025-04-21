package MyProyect.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice //le dice a Spring "esta clase maneja errores de toda la app"
public class GlobalExceptionHandler {

    /**
     * Ah, encontré un método que maneja esa excepción específica… voy a ejecutar "handleResourceNotFound".
     */
    @ExceptionHandler(RequestExceptions.class) //se encarga de atrapar esa excepción si se lanza en cualquier parte del código.
    public ResponseEntity<String> ResourceNotFound(RequestExceptions ex) {
        if(ex.getType().equals(RequestExceptions.errorType.NOT_FOUND)){
            // Retornamos el mensaje con un codigo (404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage()); //codigo (409)
    }
}
