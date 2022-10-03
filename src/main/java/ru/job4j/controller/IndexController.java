package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.job4j.model.User;
import ru.job4j.service.TaskService;

import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class IndexController {
    private final TaskService taskService;

    @GetMapping("/index")
    public String index(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("tasks", taskService.findAll(user));
        return "index";
    }

    @GetMapping("/indexWithDoneTasks")
    public String indexWithDoneTasks(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("tasks", taskService.findAllDone(user));
        return "index";
    }

    @GetMapping("/indexWithUndoneTasks")
    public String indexWithUndoneTasks(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("tasks", taskService.findAllUndone(user));
        return "index";
    }

    @ModelAttribute("userName")
    public String getUserName(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user.getName();
    }
}
