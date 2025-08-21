package com.acc.chatdemo.auth.controller;

import com.acc.chatdemo.auth.JwtProvider;
import com.acc.chatdemo.auth.dto.AccessTokenDto;
import com.acc.chatdemo.user.dto.CreateUserDto;
import com.acc.chatdemo.user.entity.User;
import com.acc.chatdemo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenDto> login(@RequestBody CreateUserDto dto) {
        // 사용자 자격 증명 검증
        User user = userService.getUserByUsername(dto.getUsername());
        if (user == null || !user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtProvider.generateAccessToken(user);

        return ResponseEntity.ok(AccessTokenDto.builder()
                .accessToken(accessToken)
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<AccessTokenDto> register(@RequestBody CreateUserDto dto) {
        // This method should handle user registration.
        // The actual implementation would involve saving user details to the database.
        // For now, we return a placeholder string.
        User user = userService.createUser(dto);

        String accessToken = jwtProvider.generateAccessToken(user);

        return ResponseEntity.ok(AccessTokenDto.builder()
                .accessToken(accessToken)
                .build());
    }
}
