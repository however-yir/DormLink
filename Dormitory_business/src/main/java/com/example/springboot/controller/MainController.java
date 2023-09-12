package com.example.springboot.controller;

import com.example.springboot.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/main")
public class MainController {

    /**
     * 获取身份信息
     */
    @GetMapping("/loadIdentity")
    public Result<?> loadIdentity(HttpServletRequest request) {
        Object identity = request.getAttribute("role");

        if (identity != null) {
            return Result.success(identity);
        } else {
            return Result.error("-1", "加载失败");
        }
    }

    /**
     * 获取个人信息
     */
    @GetMapping("/loadUserInfo")
    public Result<?> loadUserInfo(HttpServletRequest request) {
        Object username = request.getAttribute("username");
        if (username != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", username);
            userInfo.put("userId", request.getAttribute("userId"));
            userInfo.put("role", request.getAttribute("role"));
            userInfo.put("displayName", request.getAttribute("displayName"));
            userInfo.put("avatar", request.getAttribute("avatar"));
            return Result.success(userInfo);
        } else {
            return Result.error("-1", "加载失败");
        }
    }

    /**
     * 退出登录
     */
    @GetMapping("/signOut")
    public Result<?> signOut() {
        return Result.success();
    }
}
