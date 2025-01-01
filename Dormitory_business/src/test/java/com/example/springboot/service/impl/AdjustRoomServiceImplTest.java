package com.example.springboot.service.impl;

import com.example.springboot.entity.AdjustRoom;
import com.example.springboot.mapper.AdjustRoomMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdjustRoomServiceImplTest {

    @Mock
    private AdjustRoomMapper adjustRoomMapper;

    @InjectMocks
    private AdjustRoomServiceImpl adjustRoomService;

    @Test
    void shouldRejectDuplicatePendingApply() {
        AdjustRoom adjustRoom = new AdjustRoom();
        adjustRoom.setUsername("stu001");
        adjustRoom.setCurrentRoomId(101);
        adjustRoom.setTowardsRoomId(202);

        when(adjustRoomMapper.selectCount(any())).thenReturn(1L);

        int result = adjustRoomService.addApply(adjustRoom);
        Assertions.assertEquals(0, result);
        verify(adjustRoomMapper, never()).insert(any(AdjustRoom.class));
    }

    @Test
    void shouldFillDefaultStateAndApplyTime() {
        AdjustRoom adjustRoom = new AdjustRoom();
        adjustRoom.setUsername("stu001");
        adjustRoom.setCurrentRoomId(101);
        adjustRoom.setTowardsRoomId(202);

        when(adjustRoomMapper.selectCount(any())).thenReturn(0L);
        when(adjustRoomMapper.insert(any(AdjustRoom.class))).thenReturn(1);

        int result = adjustRoomService.addApply(adjustRoom);
        Assertions.assertEquals(1, result);
        Assertions.assertEquals("未处理", adjustRoom.getState());
        Assertions.assertNotNull(adjustRoom.getApplyTime());
        Assertions.assertFalse(adjustRoom.getApplyTime().isBlank());
    }
}
