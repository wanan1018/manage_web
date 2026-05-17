/**
 * @project   task-management-system
 * @module    backend  
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
package com.icinfo.task.controller;

import com.icinfo.task.dto.Result;
import com.icinfo.task.entity.User;
import com.icinfo.task.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Result<List<Map<String, Object>>> list() {
        List<User> users = userService.getUserList();
        List<Map<String, Object>> result = users.stream().map(u -> Map.<String, Object>of(
                "id", u.getId(),
                "username", u.getUsername(),
                "role", u.getRole(),
                "createTime", u.getCreateTime() != null ? u.getCreateTime().toString() : ""
        )).collect(Collectors.toList());
        return Result.success(result);
    }
}
