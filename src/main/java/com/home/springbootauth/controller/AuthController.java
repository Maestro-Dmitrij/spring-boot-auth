package com.home.springbootauth.controller;

import com.home.springbootauth.domain.User;
import com.home.springbootauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //Для request следует использовать DTO с аннотациями для проверок (отсутствие пустых строк/ null и т.п.).
    //но для ускорения процесса проверка через условие на сервисном слое
    @GetMapping("/add-user-info")
    public ResponseEntity<String> getUserInfo(@RequestBody User user) {
        return ResponseEntity.ok(authService.getUserInfo(user));
    }

    @GetMapping("/check-user")
    public ResponseEntity<Boolean> checkCachedUser(@RequestParam String searchString) {
        return ResponseEntity.ok(authService.checkUser(searchString));
    }
}