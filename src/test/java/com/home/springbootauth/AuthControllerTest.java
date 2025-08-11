package com.home.springbootauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.springbootauth.controller.AuthController;
import com.home.springbootauth.domain.User;
import com.home.springbootauth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    AuthController authController;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
        user = new User("Polina", "123");
    }

    @Test
    void checkCachedUserTest() throws Exception {
        when(authService.checkUser(user.getPassword())).thenReturn(false);
        mockMvc.perform(get("/check-user").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authService.checkUser(user.getPassword()))));
        verify(authService, times(1)).checkUser(user.getPassword());
    }
}