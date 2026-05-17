package com.icinfo.task.controller;

import com.icinfo.task.common.JwtUtil;
import com.icinfo.task.dto.LoginRequest;
import com.icinfo.task.dto.Result;
import com.icinfo.task.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Result<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        var user = userService.login(request.getUsername(), request.getPassword());
        String token = jwtUtil.generateToken(user.getUsername());
        return Result.success(Map.of(
                "token", token,
                "username", user.getUsername(),
                "role", user.getRole()
        ));
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody LoginRequest request) {
        userService.register(request.getUsername(), request.getPassword());
        return Result.success();
    }
}
