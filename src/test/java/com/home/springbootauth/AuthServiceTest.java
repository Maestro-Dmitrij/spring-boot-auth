package com.home.springbootauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.springbootauth.domain.User;
import com.home.springbootauth.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private HttpSession httpSession;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Polina", "123");
    }

    @Test
    void getValidUserInfoTest() throws JsonProcessingException {

        User mappedValue = new User("Polina", "123");
        mappedValue.setPasswordHash("test-hash-code-for-polina");
        when(passwordEncoder.encode(user.getPassword())).thenReturn("test-hash-code-for-polina");
        when(objectMapper.readValue(anyString(), ArgumentMatchers.eq(User.class))).thenReturn(mappedValue);

        String userStr = authService.getUserInfo(user);
        User responseUser = objectMapper.readValue(userStr, User.class);

        assertNotNull(responseUser, "Пользователь null");
        assertNotNull(responseUser.getPasswordHash(), "Хэш пароля null");
        assertEquals(responseUser.getName(), user.getName(), "Имена пользователя не совпадают");
        assertEquals(responseUser.getPassword(), user.getPassword(), "Пароли пользователя не совпадают");
        verify(httpSession, times(1)).setAttribute(responseUser.getPassword(), userStr);
        verify(httpSession, times(1)).setAttribute(responseUser.getPasswordHash(), userStr);
    }

    @Test
    void getInvalidUserInfoTest() {
        assertThrows(RuntimeException.class, () -> authService.getUserInfo(new User(null, null)));
    }
}