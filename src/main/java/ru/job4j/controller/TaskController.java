package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.model.Task;
import ru.job4j.model.User;
import ru.job4j.service.TaskService;

import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/addTaskPage")
    public String getAddTaskPage() {
        return "addTask";
    }

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute Task task, RedirectAttributes redirectAttributes,
                          HttpSession session) {
        User user = (User) session.getAttribute("user");
        task.setUser(user);
        taskService.add(task);
        redirectAttributes.addFlashAttribute("addSuccess", true);
        return "redirect:/addTaskPage";
    }

    @GetMapping("/taskInfoPage/{taskId}")
    public String taskInfoPage(@PathVariable("taskId") int id, Model model) {
        model.addAttribute("task", taskService.findById(id));
        return "taskInfo";
    }

    @PostMapping("/madeTaskDone")
    public String madeTaskDone(@RequestParam("task_id") int id,
                               RedirectAttributes redirectAttributes) {
        taskService.setDone(id);
        redirectAttributes.addFlashAttribute("taskDone", true);
        return "redirect:/taskInfoPage/" + id;
    }

    @GetMapping("/editTaskPage/{taskId}")
    public String editTask(@PathVariable("taskId") int id, Model model) {
        model.addAttribute("current_task", taskService.findById(id));
        return "editTask";
    }

    @PostMapping("/editTask")
    public String editTask(@ModelAttribute Task task) {
        taskService.update(task);
        int id = task.getId();
        return "redirect:/taskInfoPage/" + id;
    }

    @PostMapping("/deleteTask")
    public String deleteTask(@RequestParam("task_id") int id) {
        taskService.delete(id);
        return "redirect:/index";
    }
}
