package com.example.alphasolutionseksamen.controller;

import jakarta.servlet.http.HttpSession;
import com.example.alphasolutionseksamen.model.Project;
import com.example.alphasolutionseksamen.model.Subproject;
import com.example.alphasolutionseksamen.model.Task;
import com.example.alphasolutionseksamen.model.User;
import com.example.alphasolutionseksamen.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path="project")
public class ProjectController {

    private User loggedInUser;

    private ProjectService projectService;
    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }


    @GetMapping("")
    public String getIndex(){
        return "index";
    }


    @GetMapping("/create")
    public String createProject(Model model){
    model.addAttribute("project", new Project());
    return "createproject";
}

    @PostMapping("/save")
    public String createProject(@ModelAttribute Project project){
        projectService.createProject(project);
        return "redirect:/project/overview";
    }

    @GetMapping("/{name}/create")
    public String createSubproject(@PathVariable String name, Model model){
        model.addAttribute("projectname", name);
        model.addAttribute("subproject", new Subproject());
        return "createsubproject";
    }

    @PostMapping("/{name}/save")
    public String createSubproject(@PathVariable String name, @ModelAttribute Subproject subproject){
        Project project = projectService.showProject(name);
        projectService.createSubproject(project, subproject);
        return "redirect:/project/{name}/subprojects";
    }

    @GetMapping("/{name}/{subproject}/create")
    public String createTask(@PathVariable String name, @PathVariable String subproject, Model model){
        model.addAttribute("projectname", name);
        model.addAttribute("subprojectname", subproject);
        model.addAttribute("task", new Task());
        return "createtask";
    }

    @PostMapping("/{name}/{subprojectname}/save")
    public String saveTask(@PathVariable String name, @PathVariable String subprojectname, @ModelAttribute Task task){
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(project, subprojectname);
        projectService.createTask(project, subproject, task);
        return "redirect:/project/{name}/{subprojectname}/tasks";
    }

    @GetMapping("/overview")
    public String showProjects(Model model){
        List<Project> projects = projectService.showProjects();
        model.addAttribute("projects", projects);
        model.addAttribute("hoursError", "Your used hours are higher than your estimated hours for this project");
        return "showprojects";
    }

    @GetMapping("/{name}/subprojects")
    public String showSubprojects(@PathVariable String name, Model model){
        Project project = projectService.showProject(name);
        List<Subproject>subprojects = project.getSubprojects();
        model.addAttribute("subprojects", subprojects);
        model.addAttribute("projectname", project.getName());
        model.addAttribute("hoursError", "Your used hours are higher than your estimated hours for this subproject");
        return "showsubprojects";
    }

    @GetMapping("/{name}/{subprojectname}/tasks")
    public String showTasks(@PathVariable String name, @PathVariable String subprojectname, Model model){
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(project, subprojectname);
        List<Task>tasks = subproject.getTasks();
        model.addAttribute("projectname", name);
        model.addAttribute("tasks", tasks);
        model.addAttribute("subprojectname", subproject.getName());
        model.addAttribute("hoursError", "Your used hours are higher than your estimated hours for this task");
        return "showtasks";
    }

    @GetMapping("/{name}/edit")
    public String updateProject(@PathVariable String name, Model model){
        Project project = projectService.showProject(name);
        model.addAttribute("project", project);
        return "updateproject";
    }

    @PostMapping("/{name}/update")
    public String updateProject(@PathVariable String name, Project project){
        projectService.updateProject(name, project);
        return "redirect:/project/overview";
    }


    @GetMapping("/{name}/{subprojectname}/edit")
    public String updateSubproject(@PathVariable String name, @PathVariable String subprojectname, Model model){
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(project, subprojectname);
        model.addAttribute("project", project);
        model.addAttribute("subproject", subproject);
        return "updatesubproject";
    }

    @PostMapping("/{name}/{subprojectname}/update")
    public String updateSubproject(@PathVariable String name, @PathVariable String subprojectname, Subproject subproject){
        Project project = projectService.showProject(name);
        projectService.updateSubproject(subprojectname, project, subproject);
        return "redirect:/project/{name}/subprojects";
    }


    @GetMapping("/{name}/{subprojectname}/{taskname}/edit")
    public String updateTask(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, Model model){
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(project, subprojectname);
        Task task = projectService.showTask(subproject, taskname);
        model.addAttribute("project", project);
        model.addAttribute("subproject", subproject);
        model.addAttribute("task", task);
        return "updatetask";
    }

    @PostMapping("/{name}/{subprojectname}/{taskname}/update")
    public String updateTask(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, Task task){
        Project project = projectService.showProject(name);
        Subproject subproject =projectService.showSubproject(project, subprojectname);
        projectService.updateTask(project, taskname, subproject, task);
        return "redirect:/project/{name}/{subprojectname}/tasks";
    }

    @GetMapping("/{name}/{subprojectname}/{taskname}/edithours")
    public String updateHours(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, Model model){
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(project, subprojectname);
        Task task = projectService.showTask(subproject, taskname);
        model.addAttribute("project", project);
        model.addAttribute("subproject", subproject);
        model.addAttribute("task", task);
        return "updatehours";
    }

    @PostMapping("/{name}/{subprojectname}/{taskname}/updatehours")
    public String updateHours(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, Task task){
        Project project = projectService.showProject(name);
        Subproject subproject =projectService.showSubproject(project, subprojectname);
        projectService.updateHours(project, taskname, subproject, task);
        return "redirect:/project/{name}/{subprojectname}/tasks";
    }

    @GetMapping("/user/create")
    public String createUser(Model model){
        model.addAttribute("user", new User());
        return "createuser";
    }

    @PostMapping("/user/save")
    public String createUser(@ModelAttribute User user, HttpSession session){
        projectService.createUser(user);
        loggedInUser = new User();
        session.setAttribute("key", user);
        return "redirect:/project/profile";
    }

    @GetMapping("/user/login")
    public String login(Model model){
        model.addAttribute("user", new User());
        return "loginpage";
    }

    @PostMapping("/user/postlogin")
    public String login(@ModelAttribute User user, Model model, HttpSession session) {
        if (!projectService.checkLogin(user)) {
            model.addAttribute("loginError", "Username or password is incorrect");
            return "loginpage";
        } else {


            User userToBeLoggedIn = projectService.showUser(user);
            loggedInUser = (User) session.getAttribute("key");
            if (loggedInUser == null) {
                loggedInUser = new User();
                session.setAttribute("key", userToBeLoggedIn);
            }
            return "redirect:/project/profile";
        }
    }

    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session){
        loggedInUser = (User)session.getAttribute("key");
        model.addAttribute("user", loggedInUser);
        return "profilepage";
    }



}
