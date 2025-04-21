package MyProyect.Configuration;

import MyProyect.Dto.CustomUserDetails_Dto;
import MyProyect.Exceptions.Jwt_Exceptions;
import MyProyect.Services.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
//OncePerRequestFilter = Se utiliza para crear filtros personalizados
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;  //para extraer el username,validez y expiracion del Token
    private final UserService userService;  //para obtener datos del usuario

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Este es el corazón del filtro y el más importante. Spring lo ejecuta automáticamente en cada petición HTTP que entra.
     * Es un metodo obligatorio de la clase "OncePerRequestFilter"
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //1.EXCLUIMOS RUTAS PUBLICAS
        String path = request.getRequestURI();
        //Me aseguro de que el request sea de tipo "POST" y no de otro tipo
        if(esPublico(path,request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        //LOS TOKEN SUELEN MANDAR ESTO: "Authorization: Bearer eyJhbGciOiJIUzI1..."
        String verifyAuthToken = request.getHeader("Authorization"); //verificamos la Autorizacion del token
        String token;
        String username;

         //* 2. METODO PARA HACER VERIFICACIONES COMO "AUTORIZACION, BEARER Y FIRMA DE TOKEN"
        if(verifyAuthToken != null && verifyAuthToken.startsWith("Bearer ")) {
            token = verifyAuthToken.substring(7); //extraemos solo el token encriptado
            try{
                username = jwtUtil.extraerUsername(token);
                //3. Verificamos el usuario presente y si no hay nadie autenticado dentro del servidor, evitando doble autenticacion
                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    CustomUserDetails_Dto userDetails = userService.loadUserByUsername(username);
                    //Realizamos verificacion extra de validacion de Token
                    if(jwtUtil.tokenValido(username, token)){
                        UsernamePasswordAuthenticationToken userAuth =
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        //4. Hacemos saber a SpringSecurity que este usuario ha sido autenticado
                        SecurityContextHolder.getContext().setAuthentication(userAuth);
                    }else{
                        throw new Jwt_Exceptions("ERROR -ExpiredJwtException- : EL TOKEN ACTUAL ESTA VENCIDO, SOLICITE UN NUEVO TOKEN...");
                    }
                }
            }catch(JwtException ex){
                throw new Jwt_Exceptions( Jwt_Exceptions.AnalizerException(ex)); //Mandamos a analizar el tipo de Exceptions y retornamos el mensaje
            }
        }
        //5. Seguimos que siga su curso si todo esta bien
        filterChain.doFilter(request, response);
    }
    //metodo de verificacion de rutas públicas
    private boolean esPublico(String path,String method){
        return path.startsWith("/agenda/api/v1/auth") && method.equals("POST");
    }
}
