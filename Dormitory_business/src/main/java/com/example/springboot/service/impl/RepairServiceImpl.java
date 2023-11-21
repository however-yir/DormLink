package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Repair;
import com.example.springboot.mapper.RepairMapper;
import com.example.springboot.service.RepairService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    private static final String STATE_PENDING = "未完成";
    private static final String STATE_FINISHED = "完成";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 注入DAO层对象
     */
    @Resource
    private RepairMapper repairMapper;

    /**
     * 添加订单
     */
    @Override
    public int addNewOrder(Repair repair) {
        if (repair == null) {
            return 0;
        }
        String state = normalizeState(repair.getState(), STATE_PENDING);
        if (state == null) {
            return 0;
        }
        String buildTime = firstNonBlank(repair.getOrderBuildTime(), now());
        repair.setState(state);
        repair.setOrderBuildTime(buildTime);
        if (STATE_FINISHED.equals(state)) {
            String finishTime = firstNonBlank(repair.getOrderFinishTime(), now());
            if (!isTimeOrderValid(buildTime, finishTime)) {
                return 0;
            }
            repair.setOrderFinishTime(finishTime);
        } else {
            repair.setOrderFinishTime(null);
        }
        return repairMapper.insert(repair);
    }

    /**
     * 查找订单
     */
    @Override
    public Page find(Integer pageNum, Integer pageSize, String search) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<Repair> qw = new QueryWrapper<>();
        qw.like("title", search);
        Page orderPage = repairMapper.selectPage(page, qw);
        return orderPage;
    }

    @Override
    public Page individualFind(Integer pageNum, Integer pageSize, String search, String name) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<Repair> qw = new QueryWrapper<>();
        qw.like("title", search);
        qw.eq("repairer", name);
        Page orderPage = repairMapper.selectPage(page, qw);
        return orderPage;
    }

    /**
     * 更新订单
     */
    @Override
    public int updateNewOrder(Repair repair) {
        if (repair == null || repair.getId() == null) {
            return 0;
        }
        Repair persistedRepair = repairMapper.selectById(repair.getId());
        if (persistedRepair == null) {
            return 0;
        }
        String nextState = normalizeState(repair.getState(), persistedRepair.getState());
        if (nextState == null) {
            return 0;
        }
        String buildTime = firstNonBlank(repair.getOrderBuildTime(), persistedRepair.getOrderBuildTime(), now());
        repair.setState(nextState);
        repair.setOrderBuildTime(buildTime);
        if (STATE_FINISHED.equals(nextState)) {
            String finishTime = firstNonBlank(repair.getOrderFinishTime(), persistedRepair.getOrderFinishTime(), now());
            if (!isTimeOrderValid(buildTime, finishTime)) {
                return 0;
            }
            repair.setOrderFinishTime(finishTime);
        } else {
            repair.setOrderFinishTime(null);
        }
        return repairMapper.updateById(repair);
    }

    /**
     * 删除订单
     */
    @Override
    public int deleteOrder(Integer id) {
        return repairMapper.deleteById(id);
    }

    /**
     * 首页顶部：报修统计
     */
    @Override
    public int showOrderNum() {
        QueryWrapper<Repair> qw = new QueryWrapper<>();
        int orderCount = Math.toIntExact(repairMapper.selectCount(qw));
        return orderCount;
    }

    private String normalizeState(String state, String fallbackState) {
        String normalized = firstNonBlank(state, fallbackState);
        if (STATE_PENDING.equals(normalized) || STATE_FINISHED.equals(normalized)) {
            return normalized;
        }
        return null;
    }

    private String firstNonBlank(String... candidates) {
        for (String value : candidates) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private String now() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    private boolean isTimeOrderValid(String buildTime, String finishTime) {
        try {
            LocalDateTime build = LocalDateTime.parse(buildTime, DATE_TIME_FORMATTER);
            LocalDateTime finish = LocalDateTime.parse(finishTime, DATE_TIME_FORMATTER);
            return !finish.isBefore(build);
        } catch (DateTimeParseException | NullPointerException ex) {
            return false;
        }
    }
}
