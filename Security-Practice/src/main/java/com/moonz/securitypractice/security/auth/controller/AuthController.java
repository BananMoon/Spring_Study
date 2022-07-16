package com.moonz.securitypractice.security.auth.controller;

import com.moonz.securitypractice.security.auth.dto.AuthRequestDto;
import com.moonz.securitypractice.security.auth.dto.AuthResponseDto;
import com.moonz.securitypractice.security.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDto.Login loginRequestDto) {
        AuthResponseDto.TokenInfo login = authService.login(loginRequestDto);

        // TODO: 2022-07-14 ApiResponseObject로 넘겨야함.
        return new ResponseEntity<>(login, HttpStatus.CREATED);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Valid @RequestBody AuthRequestDto.Reissue reissueRequestDto) {
        AuthResponseDto.Reissue reissue = authService.reissueAccessToken(reissueRequestDto);

        // TODO: 2022-07-14 ApiResponseObject로 넘겨야함.
        return new ResponseEntity<>(reissue, HttpStatus.CREATED);
    }
}
