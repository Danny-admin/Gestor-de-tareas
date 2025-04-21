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

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getTasksByUser(Long  id_user) {
        return taskRepository.findByUser_id(id_user);
    }

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
    private void transition(User user,Task task) {
        user.transition(task);
    }
}
