package com.agilesoftTest.controller;

import com.agilesoftTest.dao.UsersDAO;
import com.agilesoftTest.model.Task;
import com.agilesoftTest.dao.TasksDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.agilesoftTest.model.User;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RolesAllowed("USER")
@RestController
public class TasksController {
    
    @Autowired
    private TasksDAO tasksDAO;

    @Autowired
    private UsersDAO usersDAO;

    /**
     * Returns the list of tasks of the current user
     * @param httpSession
     * @return
     */
    @GetMapping("/tasks")
    public List<Task> readTasks(HttpSession httpSession) {
        User user = usersDAO.findById((Long) httpSession.getAttribute("userId")).orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user logged in"); } );
        List<Task> totalTasks = (List<Task>) tasksDAO.findAll();
        List<Task> tasksList = new ArrayList<Task>();
        for(Task task : totalTasks) {
            if(task.getUser().equals(user)) {
                tasksList.add(task);
            }
        }
        return tasksList;
    }

    /**
     * Returns the task of the given id if it belongs to the current user
     * @param httpSession
     * @param taskId
     * @return
     * @throws ResponseStatusException
     */
    @GetMapping("/tasks/{taskId}")
    public Task readTask(HttpSession httpSession, @PathVariable Long taskId) throws ResponseStatusException {
        User user = usersDAO.findById((Long) httpSession.getAttribute("userId")).orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user logged in"); } );
        Task task = tasksDAO.findById(taskId).orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"); } );
        if (!task.getUser().equals(user))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task doesn't belongs to user");
        }
        return task;
    }

    /**
     * Creates a new task belonging to the current user
     * @param httpSession
     * @param task
     * @throws ResponseStatusException
     */
    @PostMapping("/tasks")
    void createTask(HttpSession httpSession, @RequestBody Task task) throws ResponseStatusException {
        User user = usersDAO.findById((Long) httpSession.getAttribute("userId")).orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user logged in"); } );
        task.setUser(user);
        tasksDAO.save(task);
    }

    /**
     * Sets the specified task as current if it belongs to the current user
     * @param httpSession
     * @param taskId
     * @throws ResponseStatusException
     */
    @PutMapping("/tasks/{taskId}")
    void updateTask(HttpSession httpSession, @PathVariable Long taskId) throws ResponseStatusException {
        User user = usersDAO.findById((Long) httpSession.getAttribute("userId")).orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user logged in"); } );
        Optional<Task> dbTask = tasksDAO.findById(taskId);
        if (dbTask.isPresent() && user.equals(dbTask.get().getUser()))
        {
            dbTask.get().setCurrent(true);
            tasksDAO.save(dbTask.get());
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
    }

    /**
     * Deletes the specified task if it belongs to the current user
     * @param httpSession
     * @param taskId
     * @throws ResponseStatusException
     */
    @DeleteMapping("/tasks/{taskId}")
    void deleteTask(HttpSession httpSession, @PathVariable Long taskId) throws ResponseStatusException {
        User user = usersDAO.findById((Long) httpSession.getAttribute("userId")).orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user logged in"); } );
        Optional<Task> dbTask = tasksDAO.findById(taskId);
        if (dbTask.isPresent() && user.equals(dbTask.get().getUser()))
        {
            tasksDAO.deleteById(taskId);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
    }
}
