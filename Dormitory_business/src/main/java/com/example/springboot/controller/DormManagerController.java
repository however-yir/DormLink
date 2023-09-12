package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.JwtUtil;
import com.example.springboot.common.Result;
import com.example.springboot.entity.DormManager;
import com.example.springboot.entity.User;
import com.example.springboot.service.DormManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dormManager")
public class DormManagerController {
    private static final Logger log = LoggerFactory.getLogger(DormManagerController.class);

    @Resource
    private DormManagerService dormManagerService;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 宿管添加
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody DormManager dormManager) {
        int i = dormManagerService.addNewDormManager(dormManager);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "添加失败");
        }
    }

    /**
     * 宿管信息更新
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody DormManager dormManager) {
        int i = dormManagerService.updateNewDormManager(dormManager);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    /**
     * 宿管删除
     */
    @DeleteMapping("/delete/{username}")
    public Result<?> delete(@PathVariable String username) {
        int i = dormManagerService.deleteDormManager(username);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    /**
     * 宿管查找
     */
    @GetMapping("/find")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        Page page = dormManagerService.find(pageNum, pageSize, search);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 宿管登录
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        DormManager dormManager = dormManagerService.dormManagerLogin(user.getUsername(), user.getPassword());
        if (dormManager != null) {
            log.info("Dorm manager login succeeded");
            String token = jwtUtil.generateToken(
                    dormManager.getUsername(),
                    "dormManager",
                    dormManager.getUsername(),
                    dormManager.getName(),
                    dormManager.getAvatar()
            );
            Map<String, Object> payload = new HashMap<>();
            payload.put("token", token);
            payload.put("role", "dormManager");
            payload.put("userId", dormManager.getUsername());
            payload.put("username", dormManager.getUsername());
            payload.put("displayName", dormManager.getName());
            payload.put("avatar", dormManager.getAvatar());
            payload.put("user", dormManager);
            return Result.success(payload);
        } else {
            return Result.error("-1", "用户名或密码错误");
        }
    }
}
