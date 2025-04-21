package MyProyect.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component  //indica que esta clase será administrado por spring listo para inyectar en la app
public class JwtUtil {
    //indica que se inyectara auto. "${} formato de application.properties" con el nombre de "jwt.secret"
    @Value("${jwt.secret}")
    private String KeySystem;

    private Key getSecretKey(){ //Extraemos una llave
        byte [] binaryKey = Base64.getDecoder().decode(KeySystem); //decodificamos el formato Base64 a Keybyts
        return Keys.hmacShaKeyFor(binaryKey); //Transformamos la KeyBits a una llave generica con el algoritmo "HMAC"
    }

    //CREAMOS UNA VARIABLE PARA VERIFICAR EL TIEMPO DE EXPIRACION DEL TOKEN (12 horas)
    private final long EXPIRATION_TOKEN = 43200000;

    // ==========  1. CREAR: ESTE METODO CREAR EL TOKEN
    public String generarToken(String username, Long id) {
        return Jwts.builder()
                .setSubject(username)
                .claim("user_id",id)
                .setIssuedAt(new Date())  // EMITIDA O CREADA EN?
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TOKEN))
                .signWith(SignatureAlgorithm.HS512,getSecretKey())   //firmar con?
                .compact();
    }
    //=========== 2. EXTRAER: ESTE METODO ME PERMITE EXTRAER "LOS CLAIMS" O DATOS DEL TOKEN
    public Claims extraerDatos_Token(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())  //Dame la llave secreta de la firma
                .parseClaimsJws(token)   // quiero analizar los Claims del formato(JWS) del token(JWT)
                .getBody();
    }
    //================ 3. EXTRAER USERNAME = PERMITE EXTRAER EL USERNAME DEL TOKEN
    public String extraerUsername(String token){
        return extraerDatos_Token(token)
                .getSubject();  //Muéstrame el usuario
    }
    //=============== 4. VALIDACION = PERMITE EXTRAER LOS DATOS DE EXPIRACION DEL TOKEN Y COMPARARLOS CON LA FECHA ACTUAL
    public boolean tokenExpirado(String token){
        return extraerDatos_Token(token)
                .getExpiration()
                .before(new Date());  //la fecha de expiracion fue antes que hoy???

    }
        // ========== 5. VALIDACION = PERMITE VERIFICAR LA VALIDEZ DEL TOKEN TOMANDO EN CUENTA (EL USUARIO INGRESADO,FECHA DE EXPIRACION)
    public boolean tokenValido(String username,String token){
        return extraerUsername(token).equals(username) && !tokenExpirado(token);
    }
}