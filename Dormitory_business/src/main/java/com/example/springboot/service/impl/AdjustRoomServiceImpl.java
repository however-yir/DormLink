package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.AdjustRoom;
import com.example.springboot.mapper.AdjustRoomMapper;
import com.example.springboot.service.AdjustRoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AdjustRoomServiceImpl extends ServiceImpl<AdjustRoomMapper, AdjustRoom> implements AdjustRoomService {


    @Resource
    private AdjustRoomMapper adjustRoomMapper;

    /**
     * 添加调宿申请
     */
    @Override
    public int addApply(AdjustRoom adjustRoom) {
        QueryWrapper<AdjustRoom> pendingQuery = new QueryWrapper<>();
        pendingQuery.eq("username", adjustRoom.getUsername())
                .eq("currentroom_id", adjustRoom.getCurrentRoomId())
                .eq("towardsroom_id", adjustRoom.getTowardsRoomId())
                .in("state", "未处理", "待处理", "申请中");
        Long pendingCount = adjustRoomMapper.selectCount(pendingQuery);
        if (pendingCount != null && pendingCount > 0) {
            return 0;
        }
        if (adjustRoom.getState() == null || adjustRoom.getState().isBlank()) {
            adjustRoom.setState("未处理");
        }
        if (adjustRoom.getApplyTime() == null || adjustRoom.getApplyTime().isBlank()) {
            adjustRoom.setApplyTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        int insert = adjustRoomMapper.insert(adjustRoom);
        return insert;
    }

    /**
     * 查找调宿申请
     */
    @Override
    public Page find(Integer pageNum, Integer pageSize, String search) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<AdjustRoom> qw = new QueryWrapper<>();
        qw.like("username", search);
        Page orderPage = adjustRoomMapper.selectPage(page, qw);
        return orderPage;
    }

    /**
     * 删除调宿申请
     */
    @Override
    public int deleteAdjustment(Integer id) {
        int i = adjustRoomMapper.deleteById(id);
        return i;
    }


    /**
     * 更新调宿申请
     */
    @Override
    public int updateApply(AdjustRoom adjustRoom) {
        int i = adjustRoomMapper.updateById(adjustRoom);
        return i;
    }


}
