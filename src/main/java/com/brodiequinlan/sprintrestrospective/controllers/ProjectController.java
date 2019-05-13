package com.brodiequinlan.sprintrestrospective.controllers;

import com.brodiequinlan.sprintrestrospective.database.SqlConnection;
import com.brodiequinlan.sprintrestrospective.models.Login;
import com.brodiequinlan.sprintrestrospective.models.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping("/api/projects")
@RestController
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @RequestMapping(value = "/{token}/{username}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Project> all(@PathVariable String token, @PathVariable String username) {
        SqlConnection sql = new SqlConnection();
        List<Project> projects = new ArrayList<>();
        try {
            if (Login.validate_token(token, username)) {
                projects = sql.getProjects(username);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }


        return projects;
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Project add(@RequestBody Map<String, Object> payload) {
        SqlConnection sql = new SqlConnection();
        try {
            Project p = new Project(payload.get("name").toString(), payload.get("owner").toString(), null);
            p.setId(sql.addProject(p) + "");
            return p;
        } catch (NullPointerException ex) {
            StringBuilder b = new StringBuilder();
            payload.forEach((e, z) -> {
                if (!e.equals("name") && !e.equals("owner") && !e.equals("id")) {
                    b.append(e).append(" : ").append(z.toString());
                }
            });
            logger.error("Invalid JSON received: " + b.toString());
        }
        return new Project("null", "null", "-2");
    }

}
