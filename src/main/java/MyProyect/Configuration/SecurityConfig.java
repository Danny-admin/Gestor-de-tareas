package MyProyect.Configuration;

import MyProyect.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity  //habilita la seguridad de mi app
public class SecurityConfig {

    private final UserService userService;
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(UserService userService, JwtFilter jwtFilter) {
        this.userService = userService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //1. Desactivamos la proteccion contra formularios
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests ->
                        //2.La ruta "/auth/**" sera publica
                        authorizeRequests.requestMatchers("/auth/**").permitAll()
                                //cualquier otra ruta necesitará autenticacion
                                .anyRequest()
                                .authenticated()
                )
                //3. El gestor de sesiones será de politica "STATELESS" o in estado, NO SE GUARDARÁ SESIONES ANTERIORES
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //4. Agregamos antes de cada request nuestro filtro personalizado
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                //5. Agregamos el servicio que da informacion sobre el usuario autenticado
                .userDetailsService(userService)
                .build();

    }

    @Bean
    public PasswordEncoder encryptor() {
        return new BCryptPasswordEncoder(); //Quiero usar BCrypt como algoritmo de encriptación de contraseñas
    }
}
