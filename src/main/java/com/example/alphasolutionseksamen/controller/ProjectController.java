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
    public String getIndex(HttpSession session){

        User user = new User("Bobby", "Bobby", "Bobsen", "bobby555", "bobbyersej@gmail.com", "Bobbyvej", "66", 2200, "København", 12345678, "Denmark");
        User userToBeLoggedIn = projectService.showUser(user);
        loggedInUser = (User) session.getAttribute("key");
        if (loggedInUser == null) {
            loggedInUser = new User();
            session.setAttribute("key", userToBeLoggedIn);
            return "frontpage";
        }
        return "frontpage";
    }

    @GetMapping("/frontpage")
    public String getFrontpage(){
        return "frontpage";
    }


    @GetMapping("/create")
    public String createProject(Model model){
    model.addAttribute("project", new Project());
    return "createproject";
}

    @PostMapping("/save")
    public String createProject(@ModelAttribute Project project, HttpSession session){
        loggedInUser = (User) session.getAttribute("key");
        project.setUsername(loggedInUser.getUsername());
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
    public String showProjects(Model model, HttpSession session){
        List<Project> projects = projectService.showProjects();
        loggedInUser = (User) session.getAttribute("key");
        model.addAttribute("projects", projects);
        for (Project p : projects){
        if (p.getUsername().equals(loggedInUser.getUsername())){
            model.addAttribute("manager", "manager");
        }}
        model.addAttribute("hoursError", "Your used hours are higher than your estimated hours for this project");
        return "showprojects";
    }

    @GetMapping("/{name}/subprojects")
    public String showSubprojects(@PathVariable String name, Model model, HttpSession session){
        loggedInUser = (User) session.getAttribute("key");
        Project project = projectService.showProject(name);
        List<Subproject>subprojects = project.getSubprojects();
        model.addAttribute("subprojects", subprojects);
        model.addAttribute("projectname", project.getName());
        model.addAttribute("hoursError", "Your used hours are higher than your estimated hours for this subproject");
        if (project.getUsername().equals(loggedInUser.getUsername())){
            model.addAttribute("manager", "manager");
        }
        return "showsubprojects";
    }

    @GetMapping("/{name}/{subprojectname}/tasks")
    public String showTasks(@PathVariable String name, @PathVariable String subprojectname, Model model, HttpSession session){
        loggedInUser = (User) session.getAttribute("key");
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(project, subprojectname);
        List<Task>tasks = subproject.getTasks();
        model.addAttribute("projectname", name);
        model.addAttribute("tasks", tasks);
        model.addAttribute("subprojectname", subproject.getName());
        model.addAttribute("hoursError", "Your used hours are higher than your estimated hours for this task");
        if (project.getUsername().equals(loggedInUser.getUsername())){
            model.addAttribute("manager", "manager");
        }
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
    public String createUser(@ModelAttribute User user, Model model, HttpSession session){
        if (!projectService.checkMail(user))
        {
            model.addAttribute("mailError", "This is not a valid mail");
            return "createuser";
        }
        if (user.getFirstName().length()<1){
            model.addAttribute("firstNameError", "You need to have a first name");
            return "createuser";
        }
        if (user.getLastName().length()<1) {
            model.addAttribute("lastNameError", "You need to have a last name");
            return "createuser";
        }
        if (!projectService.checkNumber(user)){
            model.addAttribute("numberError", "This is not a valid phone number");
            return "createuser";
        }
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
        List<Project> assignedProjects =projectService.showProjectsForUser(loggedInUser);
        model.addAttribute("user", loggedInUser);
        model.addAttribute("projects", assignedProjects);
        return "profilepage";
    }

    @GetMapping("/{name}/{subprojectname}/{taskname}/assign")
    public String assignUser(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, Model model){
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(project, subprojectname);
        Task task = projectService.showTask(subproject, taskname);
        model.addAttribute("project", project);
        model.addAttribute("subproject", subproject);
        model.addAttribute("task", task);
        model.addAttribute("user", new User());
        return "assignment";
    }

    @PostMapping("/{name}/{subprojectname}/{taskname}/assignpost")
    public String assignUser(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, User user, Model model){
        Project project = projectService.showProject(name);
        Subproject subproject =projectService.showSubproject(project, subprojectname);
        Task task = projectService.showTask(subproject, taskname);
        if (!projectService.checkUser(user.getUsername())){
            model.addAttribute("userError", "No user with this username exists");
            return "assignment";
        }
        User userToAdd = projectService.showUser(user.getUsername());
        projectService.addUser(project, subproject, task, userToAdd);
        return "redirect:/project/{name}/{subprojectname}/tasks";
    }

    @GetMapping("/{name}/{subprojectname}/{taskname}/assignments")
    public String showAssigned(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, Model model){
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(project, subprojectname);
        Task task = projectService.showTask(subproject, taskname);
        List<User>assignedUsers = task.getAssignedUsers();
        if (assignedUsers.size()>0){
            model.addAttribute("usersassigned", "usersassignet");
        }
        model.addAttribute("project", project);
        model.addAttribute("task", task);
        model.addAttribute("subproject", subproject);
        model.addAttribute("users", assignedUsers);
        return "assignments";
    }

    @PostMapping("/user/logout")
    public String logout(HttpSession session){
        session.removeAttribute("key");
        return "redirect:/project";
    }



}
