package com.example.week8_task_manager.controller;

import com.example.week8_task_manager.dto.TaskDto;
import com.example.week8_task_manager.dto.UserDto;
import com.example.week8_task_manager.model.Task;
import com.example.week8_task_manager.model.User;
import com.example.week8_task_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
//@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;


    @GetMapping(value = "/index")
    public String UserIndex(Model model){
        List<Task> allTasks = userService.viewAllTasks();
        model.addAttribute("listTasks", allTasks);
        return "index";
    }


    @GetMapping(value = "/dashboard")
    public String UserDashboard(Model model){
        List<Task> allTasks = userService.viewAllTasks();
        model.addAttribute("listTasks", allTasks);
        return "dashboard";
    }

    @GetMapping(value = "/login")
    public String displayLoginPage(Model model){
        model.addAttribute("user", new UserDto());
        return "login";
    }


    @PostMapping(value = "/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session, Model model){
        String message = userService.loginUser(email, password);
        if(message.equals("Successful")){
            User user = userService.getUserByEmail(email);
            System.out.println(user);
            session.setAttribute("email", user.getEmail());
            session.setAttribute("id", user.getId());
            session.setAttribute("name", user.getName());
            return "redirect:/dashboard";
        }else {
            model.addAttribute("errorMessage", message);
            return "redirect:/login";
        }

    }

    @GetMapping(value = "/register_user")
    public String showRegistrationForm(Model model){
        model.addAttribute("userRegistration", new UserDto());
        return "register_user";
    }

    @PostMapping("/register_user")
    public String registration(@ModelAttribute UserDto userDto){
        User user = userService.registerUser(userDto);
        if(user != null){
            return "redirect:/login";
        }else {
            return "redirect:/register_user";
        }
    }

    @GetMapping(value = "/task/{status}")
    public String taskByStatus(@PathVariable(name = "status") String status , Model model){
        List<Task> listOfTaskByStatus = userService.viewAllTaskByStatus(status);
        model.addAttribute("tasksByStatus" , listOfTaskByStatus);
        return "task-by-status";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable(name = "id") Long id){
        userService.deleteById(id);
        return "redirect:/dashboard";
    }

    @GetMapping(value = "/task-edit/{id}")
    public String getEditPage(@PathVariable(name = "id") Long id, Model model){
        Task task = userService.getTaskById(id);
        model.addAttribute("singleTask", task);
        model.addAttribute("taskBody", new TaskDto());
        return "task-edit";
    }

    @PostMapping(value = "/task-edit/{id}")
    public String editTask(@PathVariable(name = "id") Long id, @ModelAttribute TaskDto taskDto){
        userService.updateTitleAndDescription(taskDto, id);
        return "redirect:/dashboard";
    }

    @GetMapping(value = "/task-new")
    public String addNewTask(Model model){
        model.addAttribute("newTask", new TaskDto());
        return "task_new";
    }

    @PostMapping(value = "/task_new")
    public String createNewTask(@ModelAttribute TaskDto taskDto){
        userService.createTask(taskDto);
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping(value = "/arrow-right/{id}")
    public String moveStatusForward(@PathVariable(name = "id") Long id){
       userService.moveForward(id);
        return "redirect:/dashboard";
    }

    @GetMapping("/arrow-left/{id}")
    public String moveStatusBackward(@PathVariable(name = "id") Long id){
        userService.moveBackward(id);
        return "redirect:/dashboard";
    }

    @GetMapping("/viewTask/{id}")
    public String viewSingleTask(@PathVariable(name = "id") Long id){
        userService.getTaskById(id);
        return "viewSingleTask";
    }
}
