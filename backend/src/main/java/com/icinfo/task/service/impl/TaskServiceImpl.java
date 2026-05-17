package com.icinfo.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.icinfo.task.entity.Task;
import com.icinfo.task.mapper.TaskMapper;
import com.icinfo.task.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public List<Task> getTaskList(Long userId, String status, Long assigneeId) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<Task>()
                .eq(userId != null, Task::getAssigneeId, userId)
                .eq(status != null && !status.isEmpty(), Task::getStatus, status)
                .eq(assigneeId != null, Task::getAssigneeId, assigneeId)
                .orderByDesc(Task::getCreateTime);
        return taskMapper.selectList(wrapper);
    }

    @Override
    public Task createTask(Task task) {
        task.setStatus("TODO");
        taskMapper.insert(task);
        return task;
    }

    @Override
    public Task updateTask(Long id, Task task) {
        task.setId(id);
        taskMapper.updateById(task);
        return taskMapper.selectById(id);
    }

    @Override
    public void deleteTask(Long id) {
        taskMapper.deleteById(id);
    }
}
