package com.example.springboot.controller;

import com.example.springboot.common.JwtUtil;
import com.example.springboot.entity.Admin;
import com.example.springboot.mapper.AdminMapper;
import com.example.springboot.mapper.AdjustRoomMapper;
import com.example.springboot.mapper.DormBuildMapper;
import com.example.springboot.mapper.DormManagerMapper;
import com.example.springboot.mapper.DormRoomMapper;
import com.example.springboot.mapper.NoticeMapper;
import com.example.springboot.mapper.RepairMapper;
import com.example.springboot.mapper.StudentMapper;
import com.example.springboot.mapper.VisitorMapper;
import com.example.springboot.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

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
    void shouldReturnTokenWhenLoginSuccess() throws Exception {
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setName("系统管理员");
        admin.setAvatar("/files/avatar1.jpg");

        when(adminService.adminLogin(anyString(), anyString())).thenReturn(admin);
        when(jwtUtil.generateToken("admin", "admin", "admin", "系统管理员", "/files/avatar1.jpg"))
                .thenReturn("mock-jwt-token");

        String payload = objectMapper.writeValueAsString(new LoginRequest("admin", "123456"));

        mockMvc.perform(post("/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.data.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.data.role").value("admin"));
    }

    @Test
    void shouldReturnErrorWhenLoginFailed() throws Exception {
        when(adminService.adminLogin(anyString(), anyString())).thenReturn(null);

        String payload = objectMapper.writeValueAsString(new LoginRequest("admin", "wrong"));

        mockMvc.perform(post("/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("-1"));
    }

    private static class LoginRequest {
        public final String username;
        public final String password;

        private LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
