package com.example.alphasolutionseksamen.controller;

import com.example.alphasolutionseksamen.model.Project;
import com.example.alphasolutionseksamen.model.Subproject;
import com.example.alphasolutionseksamen.model.Task;
import com.example.alphasolutionseksamen.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path="project")
public class ProjectController {


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
        return "redirect:/project";
    }

    @GetMapping("/{name}/create")
    public String createSubproject(@PathVariable String name, Model model){
        model.addAttribute("projectname", name);
        model.addAttribute("subproject", new Subproject());
        return "createsubproject";
    }

    @PostMapping("/{name}/save")
    public String createSubproject(@PathVariable String name, @ModelAttribute Subproject subproject){
        System.out.println(name);
        Project project = projectService.showProject(name);
        projectService.createSubproject(project, subproject);
        return "redirect:/project";
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
        System.out.println(name);
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(project, subprojectname);
        projectService.createTask(project, subproject, task);
        return "redirect:/project";
    }

    @GetMapping("/overview")
    public String showProjects(Model model){
        List<Project> projects = projectService.showProjects();
        model.addAttribute("projects", projects);
        return "showprojects";
    }

    @GetMapping("/{name}/subprojects")
    public String showSubprojects(@PathVariable String name, Model model){
        Project project = projectService.showProject(name);
        List<Subproject>subprojects = project.getSubprojects();
        model.addAttribute("subprojects", subprojects);
        model.addAttribute("projectname", project.getName());
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
        System.out.println(project.getName());
        System.out.println(project.getDescription());
        projectService.updateProject(name, project);
        return "redirect:/project";
    }


    @GetMapping("/{name}/{subprojectname}/edit")
    public String updateSubproject(@PathVariable String name, @PathVariable String subprojectname, Model model){
        Project project = projectService.showProject(name);
        Subproject subproject = projectService.showSubproject(project, subprojectname);
        model.addAttribute("project", project);
        model.addAttribute("subproject", subproject);
        return "updatesubproject";
    }

   /* @PostMapping("/update")
    public String updateProject(Project project){
        projectService.updateProject(project);
        return "redirect:/project";
    }*/
}
