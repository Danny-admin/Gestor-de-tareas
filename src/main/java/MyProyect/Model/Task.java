package MyProyect.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Task {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;
    @NotBlank
    private String description;

    @CreationTimestamp    //Ponle la fecha automatica al crear
    @Temporal(TemporalType.TIMESTAMP)    //Incluyele fecha y hora
    private Date createdAt;

    @UpdateTimestamp   //Ponle la fecha de la última actualizacion
    @Temporal(TemporalType.TIMESTAMP) //Incluyele fecha y hora
    private Date updatedAt;

    private boolean completed = false;

    @ManyToOne
    @JoinColumn(name = "User_id")
    @JsonIgnore
    private User user;
}
