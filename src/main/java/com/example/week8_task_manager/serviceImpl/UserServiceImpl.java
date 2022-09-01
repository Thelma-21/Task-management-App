package com.example.week8_task_manager.serviceImpl;

import com.example.week8_task_manager.dto.TaskDto;
import com.example.week8_task_manager.dto.UserDto;
import com.example.week8_task_manager.exception.TaskNotFoundException;
import com.example.week8_task_manager.exception.UserNotFoundException;
import com.example.week8_task_manager.model.Status;
import com.example.week8_task_manager.model.Task;
import com.example.week8_task_manager.model.User;
import com.example.week8_task_manager.repository.TaskRepository;
import com.example.week8_task_manager.repository.UserRepository;
import com.example.week8_task_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final TaskRepository taskRepository;




    @Override
    public User registerUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return  userRepository.save(user);
    }

    @Override
    public String loginUser(String email, String password) {
        String message = "";
        User user = getUserByEmail(email);
        if (user.getPassword().equals(password)){
            message = "Successful";
        }else {
            message = "Incorrect password";
        }
        return message;
    }

    @Override
    public Task createTask(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setStatus(Status.PENDING);
        task.setDescription(taskDto.getDescription());
        return taskRepository.save(task);
    }

    @Override
    public Task updateTitleAndDescription(TaskDto taskDto, Long id) {
        Task task = getTaskById(id);
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException( "Task not found"));
    }

    @Override
    public List<Task> viewAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public boolean updateTaskStatus(String status, Long id) {
        return taskRepository.updateTaskByIdAndStatus(status, id);
    }

    @Override
    public List<Task> viewAllTaskByStatus(String status) {
        return taskRepository.listOfTasksByStatus(status);
    }

    @Override
    public boolean deleteById(Long id) {
         taskRepository.deleteById(id);
        return true;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email + "not found!"));
    }

    @Override
    public String moveForward(Long id){
        String message ="";
        Task task = taskRepository.findById(id).get();
        if(task.getStatus() == Status.PENDING){
            task.setStatus(Status.IN_PROGRESS);
            taskRepository.save(task);
            message="moved from pending to in-progress";
        }else if (task.getStatus() == Status.IN_PROGRESS){
            task.setStatus(Status.COMPLETED);
            task.setCompletedAt(LocalDateTime.now());
            taskRepository.save(task);
            message = "moved from in-progress to completed";
        }else {
            message ="unauthorized moved";
        }
        return message;

    }

    @Override
    public String moveBackward(Long id){
        String message ="";
        Task task = taskRepository.findById(id).get();
        if(task.getStatus() == Status.IN_PROGRESS){
            task.setStatus(Status.PENDING);
            taskRepository.save(task);
            message ="moved back to pending";
        }else{
            message = " Unauthorized";
        }
        return message;
    }
}
