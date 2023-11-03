package com.example.springboot.controller;

import com.example.springboot.common.JwtAuthenticationFilter;
import com.example.springboot.common.JwtUtil;
import com.example.springboot.common.SecurityConfig;
import com.example.springboot.entity.Repair;
import com.example.springboot.mapper.AdminMapper;
import com.example.springboot.mapper.AdjustRoomMapper;
import com.example.springboot.mapper.DormBuildMapper;
import com.example.springboot.mapper.DormManagerMapper;
import com.example.springboot.mapper.DormRoomMapper;
import com.example.springboot.mapper.NoticeMapper;
import com.example.springboot.mapper.RepairMapper;
import com.example.springboot.mapper.StudentMapper;
import com.example.springboot.mapper.VisitorMapper;
import com.example.springboot.service.RepairService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RepairController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class RepairControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RepairService repairService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private AdminMapper adminMapper;

    @MockBean
    private AdjustRoomMapper adjustRoomMapper;

    @MockBean
    private DormBuildMapper dormBuildMapper;

    @MockBean
    private DormManagerMapper dormManagerMapper;

    @MockBean
    private DormRoomMapper dormRoomMapper;

    @MockBean
    private NoticeMapper noticeMapper;

    @MockBean
    private RepairMapper repairMapper;

    @MockBean
    private StudentMapper studentMapper;

    @MockBean
    private VisitorMapper visitorMapper;

    @Test
    void shouldCreateRepairOrder() throws Exception {
        when(repairService.addNewOrder(any(Repair.class))).thenReturn(1);

        Repair payload = new Repair();
        payload.setRepairer("stu2025001");
        payload.setDormBuildId(1);
        payload.setDormRoomId(101);
        payload.setTitle("灯损坏");
        payload.setContent("宿舍灯泡损坏");

        mockMvc.perform(post("/repair/add")
                        .header("Authorization", "Bearer mock-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"));
    }
}
