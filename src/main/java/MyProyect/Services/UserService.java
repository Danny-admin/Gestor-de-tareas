package MyProyect.Services;

import MyProyect.Dto.CustomUserDetails_Dto;
import MyProyect.Dto.UserClone_Dto;
import MyProyect.Exceptions.RequestExceptions;
import MyProyect.Model.User;
import MyProyect.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
//UserDetailsService = utilizamos sus servicios para obtener info del usuario con mayor seguridad
public class UserService implements UserDetailsService {

    private final PasswordEncoder encryptor;  //este es un encriptador de contraseñas
    private final UserRepository userRepository;

    @Autowired
    public UserService(@Lazy PasswordEncoder encryptor,UserRepository userRepository) {
        this.userRepository = userRepository;
        this.encryptor = encryptor;
    }
    //=============== MOSTRAR USUARIO POR SU USERNAME (objeto Spring) ==============
    //UserDetails = es el formato oficial para manejar usuarios por Spring
    @Override
    public CustomUserDetails_Dto loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new CustomUserDetails_Dto(
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    Collections.emptyList()
                        )
                )
                .orElseThrow(() -> new UsernameNotFoundException("ERROR: EL USUARIO '"+username+"' NO HA SIDO ENCONTRADO "));
    }
    //============= VERIFICADOR DE CREDENCIALES =============
    public Optional<User> credentialsVerifier(String username, String password) {
            // .matches(password ingresado,password encriptado de BD) Permite comparar "Password" sin volver a encriptar
            return userRepository.findByUsername(username)
                    .filter(user -> encryptor.matches(password, user.getPassword()));
    }

    //========= REGISTRAR USUARIOS NUEVOS ======================
    public UserClone_Dto register(User user) {
        if(userRepository.existsByUsername( user.getUsername() )) {     //Si existe el usuario
            throw new RequestExceptions(RequestExceptions.errorType.CONFLICT,
                    "EL USUARIO '" + user.getUsername() + "' YA EXISTE EN EL SISTEMA...");
        }
        //encriptamos la contraseña con".encode"
        user.setPassword(encryptor.encode(user.getPassword()));

        User userConfirmated = userRepository.save(user);
        return new UserClone_Dto(
                userConfirmated.getId(),
                userConfirmated.getName(),
                userConfirmated.getEmail(),
                userConfirmated.getPhone());
    }
}
