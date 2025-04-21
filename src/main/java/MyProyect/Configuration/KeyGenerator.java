package MyProyect.Configuration;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

//Este metodo me permitira mostrar la llave generada yy convertida a texto plano en pantalla
public class KeyGenerator {
    public static void main(String []args){

        //Generamos la llave [-27, 45, 100, 98, 12, -92, 111, 89, ...] ← esto es lo que genera
        SecretKey GeneratedKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        //Dame la llave codificada y lo convertire en "Texto" con el formato Base64
        String translated_key = Base64.getEncoder().encodeToString(GeneratedKey.getEncoded());

        System.out.println(translated_key);
    }
}
