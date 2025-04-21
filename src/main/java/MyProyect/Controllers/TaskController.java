package MyProyect.Controllers;

import MyProyect.Dto.CustomUserDetails_Dto;
import MyProyect.Model.Task;
import MyProyect.Services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {

    private final TaskService service;
    @Autowired
    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/getTasks")
    //Authentication = es le usuario autenticado "SecurityContextHolder.getContext().getAuthentication();"
    public ResponseEntity<?> getTasks(Authentication user) {
        //Hacemos saber explicitamente que "auth" es de tipo "CustomUserDetails_Dto" para que haga relacion
        List<Task> listTask = service.getTasksByUser( ( (CustomUserDetails_Dto) user.getPrincipal()).getId() );
        return listTask.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(listTask);
    }

    @PostMapping("/postTask")
    public ResponseEntity<?> postTask(@Valid @RequestBody Task task, Authentication user) {
        return service.postTask(task,user.getName());
    }
}
