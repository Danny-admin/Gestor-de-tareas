package MyProyect.Exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

public class Jwt_Exceptions extends RuntimeException {

    public Jwt_Exceptions(String message){
        super(message);
    }

    public static String AnalizerException(JwtException ex){
        switch (ex) {
            //ignore = indica que esa excepcion casteada sera ignorada
            case ExpiredJwtException ignored -> {
                return "ERROR -ExpiredJwtException- : EL TOKEN ACTUAL ESTA VENCIDO, SOLICITE UN NUEVO TOKEN..." ;
            }
            case UnsupportedJwtException ignore -> {
                return "ERROR -UnsupportedJwtException- : EL TOKEN INGRESADO NO ES COMPATIBLE CON NUESTRO SERVICIO...";
            }
            case SignatureException ignore ->{
                return "ERROR -SignatureException- : TOKEN MODIFICADO NO VALIDO...";
            }
            case MalformedJwtException ignore ->{
                return "ERROR -MalformedJwtException- : TOKEN ROTO O MODIFICADO POR EXTERNOS...";
            }
            default -> { return "ERROR -JwtException- : EXCEPCION DESCONOCIDA..."; }
        }
    }
}
