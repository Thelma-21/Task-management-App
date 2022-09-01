package com.example.week8_task_manager.service;

import com.example.week8_task_manager.dto.TaskDto;
import com.example.week8_task_manager.dto.UserDto;
import com.example.week8_task_manager.model.Task;
import com.example.week8_task_manager.model.User;

import java.util.List;

public interface UserService {

    User registerUser(UserDto userDto);

    String loginUser(String email, String password);

    Task createTask(TaskDto taskDto);

    Task updateTitleAndDescription(TaskDto taskDto , Long id);

    Task getTaskById(Long id);

    List<Task> viewAllTasks();

    boolean updateTaskStatus(String status, Long id);

    List<Task> viewAllTaskByStatus(String status);

    boolean deleteById(Long id);
    User getUserByEmail(String email);


    String moveForward(Long id);

    String moveBackward(Long id);
}
