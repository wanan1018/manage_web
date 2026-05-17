/**
 * @project   task-management-system
 * @module    backend  
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
package com.icinfo.task.service;

import com.icinfo.task.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> getTaskList(Long userId, String status, Long assigneeId);
    Task createTask(Task task);
    Task updateTask(Long id, Task task);
    void deleteTask(Long id);
}
