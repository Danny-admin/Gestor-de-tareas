package MyProyect.Exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
//encargado de las excepciones de solicitudes http
public class RequestExceptions extends RuntimeException {

    private errorType type;

    public enum errorType{
        NOT_FOUND, CONFLICT
    }
    public RequestExceptions(errorType type, String message) {
        super(message);
        if(errorType.NOT_FOUND.equals(type)){
            this.type=type;
        }
        if(errorType.CONFLICT.equals(type)){
            this.type=type;
        }

    }
}
