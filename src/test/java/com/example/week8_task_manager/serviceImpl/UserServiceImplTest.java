package com.example.week8_task_manager.serviceImpl;

import com.example.week8_task_manager.dto.TaskDto;
import com.example.week8_task_manager.dto.UserDto;
import com.example.week8_task_manager.model.Task;
import com.example.week8_task_manager.model.User;
import com.example.week8_task_manager.repository.TaskRepository;
import com.example.week8_task_manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.Month.AUGUST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    TaskRepository taskRepository;
    @InjectMocks
    UserServiceImpl userService;

    private Task task;
    private TaskDto taskDto;
    private User user;
    private UserDto userDto;
    private LocalDateTime time;
    List<Task> taskList;


    @BeforeEach
    void setUp() {

        time = LocalDateTime.of(2022, AUGUST, 4, 5, 45, 30, 30000);
        taskList = new ArrayList<>();
        taskList.add(task);
        this.user = new User(1L, "Thelma", "temzy@gmail.com", "samKay", taskList);
        this.task = new Task(1L, "Practice algorithm","practice with codeWars for 5 hours", "pending", time, time, time, user);
        this.taskDto = new TaskDto("Practice algorithm", "practice with codeWars for 5 hours");
        this.userDto = new UserDto("Thelma", "temzy@gmail.com", "samKay");
        when(userRepository.save(user)).thenReturn(user);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskRepository.findAll()).thenReturn(taskList);
        when(taskRepository.listOfTasksByStatus("pending")).thenReturn(taskList);
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(task));
        when(userRepository.findUserByEmail("temzy@gmail.com")).thenReturn(Optional.of(user));
        when(taskRepository.updateTaskByIdAndStatus("In progress", 1L)).thenReturn(true);

    }

    @Test
    void registerUser() {
        when(userService.registerUser(userDto)).thenReturn(user);
        var actual = userService.registerUser(userDto);
        var expected = user;
        assertEquals(expected, actual);
    }

    @Test
    void shouldCheckIfLoginUserIsSuccessful() {
        String message = "Successful";
        assertEquals(message, userService.loginUser("temzy@gmail.com", "samKay"));
    }

    @Test
    void shouldCheckIfLoginUserIsUnsuccessful(){
        String message = "Incorrect password";
        assertEquals(message, userService.loginUser("temzy@gmail.com", "samkay"));
    }

    @Test
    void createTask() {
        when(userService.createTask(taskDto)).thenReturn(task);
        assertEquals(task, userService.createTask(taskDto));
    }

    @Test
    void updateTitleAndDescription() {
        assertEquals(task, userService.updateTitleAndDescription(taskDto, 1L));
    }

    @Test
    void getTaskById() {
        assertEquals(task, userService.getTaskById(1L));
    }

    @Test
    void viewAllTasks() {
        assertEquals(1, userService.viewAllTasks().size());
    }

    @Test
    void updateTaskStatus() {
        assertTrue(userService.updateTaskStatus("In progress", 1L));
    }

    @Test
    void viewAllTaskByStatus() {
        assertEquals(taskList, userService.viewAllTaskByStatus("pending"));
    }

    @Test
    void deleteById() {
        userService.deleteById(1L);
        verify(taskRepository).deleteById(any());
    }

    @Test
    void getUserByEmail() {
        assertEquals(user, userService.getUserByEmail("temzy@gmail.com"));
    }
}