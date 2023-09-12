package com.example.springboot.controller;

import com.example.springboot.common.JwtUtil;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.User;
import com.example.springboot.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Resource
    private AdminService adminService;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        Admin admin = adminService.adminLogin(user.getUsername(), user.getPassword());
        if (admin != null) {
            log.info("Admin login succeeded");
            String token = jwtUtil.generateToken(
                    admin.getUsername(),
                    "admin",
                    admin.getUsername(),
                    admin.getName(),
                    admin.getAvatar()
            );
            Map<String, Object> payload = new HashMap<>();
            payload.put("token", token);
            payload.put("role", "admin");
            payload.put("userId", admin.getUsername());
            payload.put("username", admin.getUsername());
            payload.put("displayName", admin.getName());
            payload.put("avatar", admin.getAvatar());
            payload.put("user", admin);
            return Result.success(payload);
        } else {
            return Result.error("-1", "用户名或密码错误");
        }
    }

    /**
     * 管理员信息更新
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody Admin admin) {
        int i = adminService.updateAdmin(admin);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }
}
