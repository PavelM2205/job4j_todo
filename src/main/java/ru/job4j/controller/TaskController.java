package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.exception.NoSpecifyCategory;
import ru.job4j.model.Priority;
import ru.job4j.model.Task;
import ru.job4j.model.User;
import ru.job4j.service.CategoryService;
import ru.job4j.service.PriorityService;
import ru.job4j.service.TaskService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final CategoryService categoryService;
    private final PriorityService priorityService;

    @GetMapping("/addTaskPage")
    public String getAddTaskPage(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("priorities", priorityService.findAll());
        return "addTask";
    }

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute Task task, RedirectAttributes redirectAttributes,
                          HttpSession session,
                          @RequestParam(name = "categoryId", required = false)
                              List<Integer> categoriesId,
                          @RequestParam(name = "priorityId") Integer id) {
        if (categoriesId == null) {
            throw new NoSpecifyCategory("Category is not specified");
        }
        Priority priority = priorityService.findById(id);
        User user = (User) session.getAttribute("user");
        task.setUser(user);
        task.setCategories(categoryService.findSeveralCategoriesById(categoriesId));
        task.setPriority(priority);
        taskService.add(task);
        redirectAttributes.addFlashAttribute("addSuccess", true);
        return "redirect:/addTaskPage";
    }

    @GetMapping("/taskInfoPage/{taskId}")
    public String taskInfoPage(@PathVariable("taskId") int id, Model model,
                               HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("task", taskService.findById(id, user));
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
    public String editTask(@PathVariable("taskId") int id, Model model,
                           HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("current_task", taskService.findById(id, user));
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
