package com.example.springboot.service.impl;

import com.example.springboot.entity.Repair;
import com.example.springboot.mapper.RepairMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepairServiceImplTest {

    @Mock
    private RepairMapper repairMapper;

    @InjectMocks
    private RepairServiceImpl repairService;

    @Test
    void shouldFillDefaultStateAndBuildTimeWhenCreate() {
        Repair repair = new Repair();
        repair.setRepairer("stu001");
        repair.setDormBuildId(1);
        repair.setDormRoomId(101);
        repair.setTitle("水龙头漏水");
        repair.setContent("卫生间水龙头持续漏水");

        when(repairMapper.insert(any(Repair.class))).thenReturn(1);

        int result = repairService.addNewOrder(repair);
        Assertions.assertEquals(1, result);
        Assertions.assertEquals("未完成", repair.getState());
        Assertions.assertNotNull(repair.getOrderBuildTime());
        Assertions.assertNull(repair.getOrderFinishTime());
    }

    @Test
    void shouldFillFinishTimeWhenOrderMarkedFinished() {
        Repair persisted = buildPersistedRepair("未完成", "2025-01-03 10:00:00", null);
        Repair updatePayload = new Repair();
        updatePayload.setId(1);
        updatePayload.setState("完成");

        when(repairMapper.selectById(1)).thenReturn(persisted);
        when(repairMapper.updateById(any(Repair.class))).thenReturn(1);

        int result = repairService.updateNewOrder(updatePayload);
        Assertions.assertEquals(1, result);

        ArgumentCaptor<Repair> captor = ArgumentCaptor.forClass(Repair.class);
        verify(repairMapper).updateById(captor.capture());
        Repair updated = captor.getValue();
        Assertions.assertEquals("完成", updated.getState());
        Assertions.assertEquals("2025-01-03 10:00:00", updated.getOrderBuildTime());
        Assertions.assertNotNull(updated.getOrderFinishTime());
    }

    @Test
    void shouldClearFinishTimeWhenOrderMarkedUnfinished() {
        Repair persisted = buildPersistedRepair("完成", "2025-01-03 10:00:00", "2025-01-03 13:00:00");
        Repair updatePayload = new Repair();
        updatePayload.setId(1);
        updatePayload.setState("未完成");
        updatePayload.setOrderFinishTime("2025-01-03 15:00:00");

        when(repairMapper.selectById(1)).thenReturn(persisted);
        when(repairMapper.updateById(any(Repair.class))).thenReturn(1);

        int result = repairService.updateNewOrder(updatePayload);
        Assertions.assertEquals(1, result);

        ArgumentCaptor<Repair> captor = ArgumentCaptor.forClass(Repair.class);
        verify(repairMapper).updateById(captor.capture());
        Repair updated = captor.getValue();
        Assertions.assertEquals("未完成", updated.getState());
        Assertions.assertNull(updated.getOrderFinishTime());
    }

    @Test
    void shouldRejectWhenFinishTimeEarlierThanBuildTime() {
        Repair persisted = buildPersistedRepair("未完成", "2025-01-03 10:00:00", null);
        Repair updatePayload = new Repair();
        updatePayload.setId(1);
        updatePayload.setState("完成");
        updatePayload.setOrderFinishTime("2025-01-03 09:59:59");

        when(repairMapper.selectById(1)).thenReturn(persisted);

        int result = repairService.updateNewOrder(updatePayload);
        Assertions.assertEquals(0, result);
        verify(repairMapper, never()).updateById(any(Repair.class));
    }

    private Repair buildPersistedRepair(String state, String buildTime, String finishTime) {
        Repair repair = new Repair();
        repair.setId(1);
        repair.setRepairer("stu001");
        repair.setDormBuildId(1);
        repair.setDormRoomId(101);
        repair.setTitle("空调故障");
        repair.setContent("空调无法启动");
        repair.setState(state);
        repair.setOrderBuildTime(buildTime);
        repair.setOrderFinishTime(finishTime);
        return repair;
    }
}
