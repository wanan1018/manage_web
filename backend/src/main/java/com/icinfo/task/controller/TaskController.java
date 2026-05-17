/**
 * @project   task-management-system
 * @module    backend  
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
package com.icinfo.task.controller;

import com.icinfo.task.dto.Result;
import com.icinfo.task.entity.Task;
import com.icinfo.task.entity.User;
import com.icinfo.task.service.TaskService;
import com.icinfo.task.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public Result<List<Task>> list(@RequestParam(required = false) String status,
                                   @RequestParam(required = false) Long assigneeId,
                                   HttpServletRequest request) {
        String role = getRole(request);
        Long userId = getUserId(request);

        if ("TUTOR".equals(role)) {
            return Result.success(taskService.getTaskList(null, status, assigneeId));
        } else {
            return Result.success(taskService.getTaskList(userId, status, null));
        }
    }

    @PostMapping
    public Result<Task> create(@RequestBody Task task, HttpServletRequest request) {
        String role = getRole(request);
        Long userId = getUserId(request);

        if (!"TUTOR".equals(role)) {
            task.setAssigneeId(userId);
        }
        if (task.getAssigneeId() == null) {
            task.setAssigneeId(userId);
        }
        task.setCreatorId(userId);
        return Result.success(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public Result<Task> update(@PathVariable Long id, @RequestBody Task task) {
        return Result.success(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return Result.success();
    }

    private Long getUserId(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = userService.getByUsername(username);
        return user.getId();
    }

    private String getRole(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = userService.getByUsername(username);
        return user.getRole();
    }
}
