package MyProyect.Services;

import MyProyect.Exceptions.RequestExceptions;
import MyProyect.Model.Task;
import MyProyect.Model.User;
import MyProyect.Repository.TaskRepository;
import MyProyect.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    //======= Busca todas las tareas de un usuario ========
    public List<Task> getTasksByUser(Long  id_user) {
        return taskRepository.findByUser_id(id_user);
    }
    //======= Crea una nueva tarea ========
    public ResponseEntity<?> postTask(Task task, String  username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    task.setUser(user);
                    transition(user, task);
                    return ResponseEntity.status(HttpStatus.CREATED).body(taskRepository.save(task));
                })
                .orElseThrow(()-> new RequestExceptions(RequestExceptions.errorType.NOT_FOUND,
                        "ERROR 404: USUARIO '"+ username +"' NO HA SIDO ENCONTRADO..."));
    }
    //======= Mostrar tareas completas o incompletas ==========
    public List<Task> getAllCompletedTasks(Long id_user) {
        return getTasksByUser(id_user)
                .stream().filter(Task::isCompleted)
                .collect(Collectors.toList());
    }
    public List<Task> getAllIncompletedTask(Long id_user) {
        return getTasksByUser(id_user)
                .stream().filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
    }
    //====== MODIFICAR TAREAS ======
    public ResponseEntity<Task> saveTask(Task task) throws IllegalArgumentException {
            return taskRepository.findById(task.getId())
                    .map(taskFound -> {
                        taskFound.setTitle(task.getTitle());
                        taskFound.setDescription(task.getDescription());
                        taskFound.setCompleted(task.isCompleted());
                        return ResponseEntity.ok(taskRepository.save(taskFound));
                    })
                    .orElseThrow(() -> new RequestExceptions(RequestExceptions.errorType.NOT_FOUND,
                            "ERROR 404: LA TAREA CON LA ID '"+ task.getId() +"', NO HA SIDO ENCONTRADO..."));
    }
    //===== Eliminar tareas =======
    public ResponseEntity<?> deleteTask(Long id_task) {
        return taskRepository.findById(id_task)
                .map(task -> {
                    taskRepository.delete(task);
                    return ResponseEntity.status(HttpStatus.GONE).build();  //codigo (410) eliminacion permanente
                })
                .orElseThrow(() -> new RequestExceptions(RequestExceptions.errorType.NOT_FOUND,
                        "ERROR 404: LA TAREA CON LA ID '"+ id_task +"', NO HA SIDO ENCONTRADO..."));
    }

    //Transicion bidireccional
    private void transition(User user,Task task) {
        user.transition(task);
    }
}
