package com.example.alphasolutionseksamen.controller;

import com.example.alphasolutionseksamen.TaskComparator;
import com.example.alphasolutionseksamen.repository.ProjectRepository;
import com.example.alphasolutionseksamen.repository.ProjectRepositoryDB;
import jakarta.servlet.http.HttpSession;
import com.example.alphasolutionseksamen.model.Project;
import com.example.alphasolutionseksamen.model.Subproject;
import com.example.alphasolutionseksamen.model.Task;
import com.example.alphasolutionseksamen.model.User;
import com.example.alphasolutionseksamen.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path="project")
public class ProjectController {

    private User loggedInUser;
    private ProjectRepository rp = new ProjectRepository();
    private ProjectService projectService;

    private ProjectRepositoryDB projectRepositoryDB;

    public ProjectController(ProjectService projectService){
        this.projectService =projectService;
    }
    /*public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }*/


    @GetMapping("")
    public String getIndex(HttpSession session, Model model){
        /*User user = new User("Bobby", "Bobby", "Bobsen", "bobby555", "bobbyersej@gmail.com", "Bobbyvej", "66", 2200, "København", 12345678, "Denmark");

        User userToBeLoggedIn = projectService.showUser(user.getUsername());
        loggedInUser = (User) session.getAttribute("key");
        if (loggedInUser == null) {
            loggedInUser = new User();
            session.setAttribute("key", userToBeLoggedIn);
        return "frontpage";
    }*/
        model.addAttribute("tal", "width:100%");
        return "index";}

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
        //projectService.createProject(project);
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
        //projectService.createSubproject(project, subproject);
        return "redirect:/project/{name}/subprojects";
    }

    @GetMapping("/{name}/{subproject}/create")
    public String createTask(@PathVariable String name, @PathVariable String subproject, Model model){
        model.addAttribute("projectname", name);
        model.addAttribute("subprojectname", subproject);
        model.addAttribute("task", new Task());
        List <String> statusPriorities = projectService.showStatus();
        model.addAttribute("statusPriorities", statusPriorities);
        model.addAttribute("skills", projectService.showSkills());
        return "createtask";
    }

    @PostMapping("/{name}/{subprojectname}/save")
    public String saveTask(@PathVariable String name, @PathVariable String subprojectname, @ModelAttribute Task task){
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(name, subprojectname);
        projectService.createTask(project, subproject, task);
        //projectService.createTask(project, subproject, task);
        return "redirect:/project/{name}/{subprojectname}/tasks";
    }

    @GetMapping("/overview")
    public String showProjects(Model model, HttpSession session){
        List<Project> allProjects = projectService.showProjects();
        Project project = new Project();
        List<Project> projects = projectService.showProjects();
        List<Project> managedProjects = new ArrayList<>();
        List<Project> doneProjects = projectService.showDoneProjects();

        List<Project>projectsWithTasks = new ArrayList<>();
        for (Project p : doneProjects){
            for (Project po : projects){
                if(p.getName().equals(po.getName())){
                    project=po;
                }
            }
            projects.remove(project);
            }

        loggedInUser = (User) session.getAttribute("key");
        model.addAttribute("projects", projects);
        for (Project p : projects){
        if (p.getUsername().equals(loggedInUser.getUsername())){
            managedProjects.add(p);
        }
            if (projectService.showSubprojects(p.getName()).size()>0) {
                projectsWithTasks.add(p);}

        model.addAttribute("managedprojects", managedProjects);
        model.addAttribute("projectswithtasks", projectsWithTasks);

        }
        model.addAttribute("doneprojects", doneProjects);

        model.addAttribute("hoursError", "Antallet af brugte timer overgår antallet af forventede timer for dette projekt!");
        return "showprojects";
    }

    @GetMapping("/archive")
    public String showDoneProjects(Model model){
        List<Project> doneProjects = projectService.showDoneProjects();
        model.addAttribute("doneprojects", doneProjects);
        return "showarchive";
    }

    @GetMapping("/yourarchive")
    public String showYourDoneProjects(Model model, HttpSession session){
        loggedInUser = (User) session.getAttribute("key");
        Project project = new Project();
        List<Project> projects = new ArrayList<>();
        List<Project> doneProjects = projectService.showDoneProjects();
        for (Project p : doneProjects) {
            if (p.getUsername().equals(loggedInUser.getUsername())) {
                projects.add(p);
            }
        }

        model.addAttribute("projects", projects);
        return "showyourarchive";

    }


    @GetMapping("/{name}/subprojects")
    public String showSubprojects(@PathVariable String name, Model model, HttpSession session){
        loggedInUser = (User) session.getAttribute("key");
        Project project = projectService.showProject(name);
        List<Subproject>subprojects = projectService.showSubprojects(name);
        List<Subproject>doneSubprojects = new ArrayList<>();
        List<Subproject>subprojectsWithTasks = new ArrayList<>();
        projectService.checkStatusSubproject(name, subprojects);

        for (Subproject s : subprojects){
            if (s.isDone()) {
                doneSubprojects.add(s);
            }
            model.addAttribute("donesubprojects", doneSubprojects);
            if (projectService.showTasks(name, s.getName()).size()>0) {
                subprojectsWithTasks.add(s);
            }
            model.addAttribute("subprojectswithtasks", subprojectsWithTasks);
        }

        model.addAttribute("subprojects", subprojects);
        model.addAttribute("projectname", name);
        model.addAttribute("hoursError", "Antallet af brugte timer overgår antallet af forventede timer for dette subprojekt!");
        if (project.getUsername().equals(loggedInUser.getUsername())){
            model.addAttribute("manager", "manager");
        }
        return "showsubprojects";
    }

    @GetMapping("/{name}/{subprojectname}/tasks")
    public String showTasks(@PathVariable String name, @PathVariable String subprojectname, Model model, HttpSession session){
        loggedInUser = (User) session.getAttribute("key");
        Project project = projectService.showProject(name);
       // Subproject subproject = projectService.showSubproject(project, subprojectname);
        List<Task> assignedTasks = new ArrayList<>();
        List<Task>tasks = projectService.showTasks(name, subprojectname);
        tasks.sort(new TaskComparator());
        HashMap<String, Double> workloads = new HashMap<>();



        List<Task>doneTasks = projectService.showTasks(name, subprojectname);

        for (Task t : tasks){
            for (String s : projectService.showAssignedUsers(name, subprojectname, t.getName())){
                if (s.equals(loggedInUser.getUsername())) {
                    assignedTasks.add(t);
                    if (projectService.calculateUserWorkload(name, subprojectname, t) >= 0) {
                        workloads.put(t.getName(), projectService.calculateUserWorkload(name, subprojectname, t));
                    }
                }
            }
            model.addAttribute("assignedtasks", assignedTasks);
        }
        for (Task t : tasks){
            if (t.isDone()) {
               doneTasks.add(t);
            }
            model.addAttribute("donetasks", doneTasks);
        }
        model.addAttribute("workloads", workloads);

        model.addAttribute("projectname", name);
        model.addAttribute("tasks", tasks);
        model.addAttribute("subprojectname", subprojectname);
        model.addAttribute("hoursError", "Antallet af brugte timer overgår antallet af forventede timer for denne task!");
        if (project.getUsername().equals(loggedInUser.getUsername())){
            model.addAttribute("manager", "manager");
        }
        return "showtasks";
    }

    @GetMapping("/{name}/edit")
    public String updateProject(@PathVariable String name, Model model){
        //Project project = projectService.showProject(name);
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
        Subproject subproject = projectService.showSubproject(name, subprojectname);
        List<Subproject>subprojects = projectService.showSubprojects(name);
        model.addAttribute("project", project);
        model.addAttribute("subproject", subproject);
        model.addAttribute("subprojects", subprojects);
        return "updatesubproject";
    }

    @PostMapping("/{name}/{subprojectname}/update")
    public String updateSubproject(@PathVariable String name, @PathVariable String subprojectname, Subproject subproject){
        projectService.updateSubproject(name, subprojectname, subproject);
        return "redirect:/project/{name}/subprojects";
    }


    @GetMapping("/{name}/{subprojectname}/{taskname}/edit")
    public String updateTask(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, Model model){
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(name, subprojectname);
        Task task = projectService.showTask(name, subprojectname, taskname);
        for (String s : task.getSkills()){
            System.out.println(s);
        }
        List <String> statusPriorities = projectService.showStatus();
        model.addAttribute("project", project);
        model.addAttribute("subproject", subproject);
        model.addAttribute("task", task);
        model.addAttribute("statusPriorities", statusPriorities);
        model.addAttribute("skills", projectService.showSkills());

        return "updatetask";
    }

    @PostMapping("/{name}/{subprojectname}/{taskname}/update")
    public String updateTask(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, Task task){
        //Project project = projectRepositoryDB.showProject(name);
       // Subproject subproject =projectRepositoryDB.showSubproject(project, subprojectname);
        projectService.updateTask(name, subprojectname, task, taskname);
        return "redirect:/project/{name}/{subprojectname}/tasks";
    }

    @GetMapping("/profile/edit")
    public String updateUser(Model model, HttpSession session){
        loggedInUser = (User)session.getAttribute("key");
        List<String>skills = projectService.showSkills();
        model.addAttribute("user", loggedInUser);
        model.addAttribute("skills", skills);
        return "updateuser";
    }

    @PostMapping("/profile/update")
    public String updateUser(User user, HttpSession session){
        System.out.println(user.getStreetNumber());
        session.setAttribute("key", user);
        projectService.updateUser(user);
        return "redirect:/project/profile";
    }

    @GetMapping("/{name}/{subprojectname}/{taskname}/edithours")
    public String updateHours(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, Model model, HttpSession session){
        loggedInUser = (User) session.getAttribute("key");
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(name, subprojectname);
        Task task = projectService.showTask(name, subprojectname, taskname);
        model.addAttribute("project", project);
        model.addAttribute("subproject", subproject);
        model.addAttribute("task", task);
        if (!task.isDone()){
            model.addAttribute("notdone", "notDone");
        }
        if (task.isDone() && project.getUsername().equals(loggedInUser.getUsername())){
            model.addAttribute("manager", "manager");
        }
        return "updatehours";
    }

    @PostMapping("/{name}/{subprojectname}/{taskname}/updatehours")
    public String updateHours(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, Task task){
        projectService.updateHours(name, subprojectname, taskname, task);
        return "redirect:/project/{name}/{subprojectname}/tasks";
    }

    @GetMapping("/user/create")
    public String createUser(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("skills", projectService.showSkills());
        return "createuser";
    }

    @PostMapping("/user/save")
    public String createUser(@ModelAttribute User user, Model model, HttpSession session){
        if (user.getUsername().length()<1){
            model.addAttribute("userNameError", "Du skal have et brugernavn");
            return "createuser";
        }
        List<String> usedUserNames = projectService.showUserNames();
        for (String s : usedUserNames){
            if (s.equalsIgnoreCase(user.getUsername())){
                model.addAttribute("usedUserNameError", "Dette brugernavn er allerede brugt");
                return "createuser";
            }
        }
        if (user.getPassword().length()<1){
            model.addAttribute("passwordError", "Du skal have et kodeord");
            return "createuser";
        }
        if (user.getFirstName().length()<1){
            model.addAttribute("firstNameError", "Du skal have et fornavn");
            return "createuser";
        }
        if (user.getLastName().length()<1) {
            model.addAttribute("lastNameError", "Du skal have et efternavn");
            return "createuser";
        }
        if (user.getStreetName().length()<1) {
            model.addAttribute("streetNameError", "Du skal indtaste et vejnavn");
            return "createuser";
        }
        if (user.getStreetNumber().length()<1) {
            model.addAttribute("streetNumberError", "Du skal indtaste et vejnummer");
            return "createuser";
        }
        if (user.getPostNumber()<1) {
            model.addAttribute("postNumberError", "Du skal indtaste et postnummer");
            return "createuser";
        }
        if (user.getCity().length()<1) {
            model.addAttribute("cityError", "Du skal indtaste en by");
            return "createuser";
        }
        if (user.getCountry().length()<1) {
            model.addAttribute("countryError", "Du skal indtaste et land");
            return "createuser";
        }
        if (!projectService.checkMail(user))
        {
            model.addAttribute("mailError", "Dette er ikke en gyldig emailadresse");
            return "createuser";
        }
        if (!projectService.checkNumber(user)){
            model.addAttribute("numberError", "Dette er ikke et gyldigt telefonnummer");
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
            model.addAttribute("loginError", "Brugernavnet eller kodeordet er forkert");
            return "loginpage";
        } else {

            User userToBeLoggedIn = projectService.showUser(user.getUsername());
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
        List<Project> assignedProjects =projectService.showsProjectsForUser(loggedInUser);
        List<Project> managedProjects = projectService.showProjectsForManagers(loggedInUser);
        List<Project> doneProjects = projectService.showDoneProjects();
        Project project = new Project();
        for (Project p : doneProjects){
            for (Project po : assignedProjects){
                if(p.getName().equals(po.getName())){
                    project = po;
                }
            }
        assignedProjects.remove(project);
            for (Project po : managedProjects){
                if(p.getName().equals(po.getName())){
                    project = po;
                }
            }
            managedProjects.remove(project);}

        model.addAttribute("user", loggedInUser);
        model.addAttribute("managedprojects", managedProjects);
        model.addAttribute("projects", assignedProjects);
        return "profilepage";
    }

    @GetMapping("/{name}/{subprojectname}/{taskname}/assign")
    public String assignUser(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, Model model){
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(name, subprojectname);
        Task task = projectService.showTask(name, subprojectname, taskname);
        model.addAttribute("project", project);
        model.addAttribute("subproject", subproject);
        model.addAttribute("task", task);
        model.addAttribute("user", new User());
        model.addAttribute("skills", projectService.showSkills());
        return "assignment";
    }

    @GetMapping("/{name}/{subprojectname}/{taskname}/{skill}")
    public String assignUserBySkills(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, @PathVariable String skill, Model model){
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(name, subprojectname);
        Task task = projectService.showTask(name, subprojectname, taskname);
        List<User> users = projectService.showUsersWithSkill(skill);

        model.addAttribute("users", users);
        model.addAttribute("project", project);
        model.addAttribute("subproject", subproject);
        model.addAttribute("task", task);
        model.addAttribute("user", new User());
        model.addAttribute("skills", projectService.showSkills());
        return "assignmentskills";
    }

    @PostMapping("/{name}/{subprojectname}/{taskname}/assignpost")
    public String assignUser(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, User user, Model model){
        Project project = projectService.showProject(name);
        Subproject subproject =projectService.showSubproject(name, subprojectname);
        Task task = projectService.showTask(name, subprojectname, taskname);
        List<String>assignedUsers = projectService.showAssignedUsers(name, subprojectname, taskname);
        for (String s : assignedUsers){
            if (s.equalsIgnoreCase(user.getUsername())){
                model.addAttribute("assignmentError", "Denne bruger er allerede tildelt denne task");
                model.addAttribute("project", project);
                model.addAttribute("subproject", subproject);
                model.addAttribute("task", task);
                model.addAttribute("user", new User());
                return "assignment";

            }
        }
        if (!projectService.checkUsers(user.getUsername())){
            model.addAttribute("userError", "Der kunne ikke findes nogle brugere med dette brugernavn.");
            model.addAttribute("project", project);
            model.addAttribute("subproject", subproject);
            model.addAttribute("task", task);
            model.addAttribute("user", new User());
            return "assignment";
        }
        User userToAdd = projectService.showUser(user.getUsername());
        projectService.addUser(name, subprojectname, taskname, userToAdd);
        return "redirect:/project/{name}/{subprojectname}/tasks";
    }

    @GetMapping("/{name}/{subprojectname}/{taskname}/{username}/unassign")
    public String unassignUser(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, @PathVariable String username){
        projectService.unassignUser(name, subprojectname, taskname, username);
        return "redirect:/project/{name}/{subprojectname}/tasks";
    }

    @GetMapping("/{name}/{subprojectname}/{taskname}/assignments")
    public String showAssigned(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname, Model model){
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(name, subprojectname);
        Task task = projectService.showTask(name, subprojectname, taskname);
        List<String>assignedUsers = projectService.showAssignedUsers(name, subprojectname, taskname);

        if (assignedUsers.size()>0){
            model.addAttribute("usersassigned", "usersassignet");
        }
        if (project.getUsername().equals(loggedInUser.getUsername())){
            model.addAttribute("manager", "manager");
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

    @GetMapping("/{name}/{subprojectname}/{taskname}/done")
    public String setAsDone(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname){
        Task task = projectService.showTask(name, subprojectname, taskname);
        if (task.isDone()){
            task.setDone(false);
        }
        else {
            task.setDone(true);
        }
        projectService.finishATask(name, subprojectname, task);
        return "redirect:/project/{name}/{subprojectname}/tasks";
    }

    @GetMapping("/{name}/delete")
    public String deleteProject(@PathVariable String name){
        projectService.deleteProject(name);
        return "redirect:/project/overview";
    }


    @GetMapping("/{name}/{subprojectname}/delete")
    public String deleteSubproject(@PathVariable String name, @PathVariable String subprojectname){;
        projectService.deleteSubproject(name, subprojectname);
        return "redirect:/project/{name}/subprojects";
    }

    @GetMapping("profile/delete")
    public String deleteUser(HttpSession session){
        loggedInUser = (User)session.getAttribute("key");
        session.removeAttribute("key");
        projectService.deleteUser(loggedInUser);
        return "redirect:/project";

    }

    @GetMapping("/{name}/{subprojectname}/{taskname}/delete")
    public String deleteTask(@PathVariable String name, @PathVariable String subprojectname, @PathVariable String taskname){
        projectService.deleteTask(name, subprojectname, taskname);
        return "redirect:/project/{name}/{subprojectname}/tasks";
    }



}
