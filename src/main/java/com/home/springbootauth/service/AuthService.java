package com.home.springbootauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.springbootauth.domain.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;

    public String getUserInfo(User user) {

        checkUserFields(user);
        if (checkUserInSession(user.getPassword())) {
            throw new RuntimeException("Пароль не уникален. Смените пароль");
        }

        ObjectMapper objectMapper = new ObjectMapper();

        String hash = passwordEncoder.encode(user.getPassword());

        user.setPasswordHash(hash);
        String strValue = null;
        try {
            strValue = objectMapper.writeValueAsString(user);
            //т.к. АПИ для проверки может принимать как hash, так и пароль в явном виде в сессию прокидываю один и тот же объект с различными именами
            // При условии, что я верно понял задачу (и не надо создавать классический сервис аутентификации)
            session.setAttribute(user.getPassword(), strValue);
            session.setAttribute(user.getPasswordHash(), strValue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return strValue;
    }

    public boolean checkUser(String searchStr) {
        if (checkUserInSession(searchStr)) {
            return true;
        } else {
            return false;
        }
    }

    private void checkUserFields(User user) {
        if (null == user.getPassword() || null == user.getName() ||
                user.getPassword().isEmpty() || user.getName().isEmpty()
        ) {
            throw new RuntimeException("Поля имя и пароль обязательны для заполнения");
        }
    }

    private boolean checkUserInSession(String password) {
        return Optional.ofNullable(session.getAttribute(password))
                .isPresent();
    }
}