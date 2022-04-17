package com.jjss.civideo.domain.user.controller;

import com.jjss.civideo.domain.user.dto.RefreshRequestDto;
import com.jjss.civideo.domain.user.dto.TokenRequestDto;
import com.jjss.civideo.domain.user.dto.TokenResponseDto;
import com.jjss.civideo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/token")
    public ResponseEntity<?> sendToken(@Valid TokenRequestDto tokenRequestDto) {
        TokenResponseDto tokenResponseDto = userService.createToken(tokenRequestDto.getProvider(), tokenRequestDto.getToken());
        if (tokenResponseDto == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "invalid token"));
        }

        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendRefreshToken(@Valid @RequestBody RefreshRequestDto refreshRequestDto) {
        String refreshToken = refreshRequestDto.getRefreshToken();
        TokenResponseDto tokenResponseDto = userService.refresh(refreshToken);

        return ResponseEntity.ok(tokenResponseDto);
    }

}
