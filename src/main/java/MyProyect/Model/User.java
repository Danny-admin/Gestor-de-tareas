package MyProyect.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    @Email
    private String email;
    /*   ( \\d ) => indica un digito del 0-9
         ( {8} ) => indica 8 repeticiones  */
    @NotNull @Pattern(regexp = "\\d{8}" , message = "El numero de teléfono debe tener 8 digitos")
    private String phone;
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @JsonIgnore
    @OneToMany (mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> listTasks;

    public void transition(Task task) {
        this.listTasks.add(task);
    }
}
