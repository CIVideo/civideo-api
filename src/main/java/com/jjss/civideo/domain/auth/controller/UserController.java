package com.jjss.civideo.domain.auth.controller;

import com.jjss.civideo.domain.auth.dto.TokenRequestDto;
import com.jjss.civideo.domain.auth.dto.TokenResponseDto;
import com.jjss.civideo.domain.auth.service.UserService;
import com.jjss.civideo.global.exception.BadRequestResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Access token 발급", description = "provider에서 제공하는 access token을 검증하여 인증 토큰(JWT)을 발급합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Errors.class)))
    })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = TokenResponseDto.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = Errors.class)
    })
    @GetMapping("/auth/token")
    public ResponseEntity<?> sendToken(@Valid TokenRequestDto tokenRequestDto, @ApiIgnore Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(BadRequestResponseDto.of(errors));
        }
        String accessToken = userService.createAccessToken(tokenRequestDto);
        if (accessToken == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "invalid token"));
        }

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken(accessToken)
                .build();

        return ResponseEntity.ok(tokenResponseDto);
    }

}
