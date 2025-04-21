package MyProyect.Controllers;

import MyProyect.Configuration.JwtUtil;
import MyProyect.Dto.Credentials;
import MyProyect.Dto.SendToken_Dto;
import MyProyect.Model.User;
import MyProyect.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MyProyect.Dto.UserClone_Dto;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService service;

    @Autowired
    public AuthController(JwtUtil jwtUtil, UserService service) {
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Credentials credentials) {
        Optional<User> userVerified = service.credentialsVerifier(credentials.getUsername(), credentials.getPassword());
        return userVerified
                .map(user -> {
                    String token = jwtUtil.generarToken(user.getUsername(), user.getId());
                    return ResponseEntity.ok(new SendToken_Dto(token));
                })
                //.orElseGet = me permite crear o retornar un objeto si un "Optional<T> ESTA VACIO"
                .orElseGet( () -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new SendToken_Dto("ERROR 401: USUARIO O CONTRASEÑA INCORRECTA"))
                );
    }

    @PostMapping("/register")
    public ResponseEntity<UserClone_Dto> register(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(user));  //codigo (201)
    }
}