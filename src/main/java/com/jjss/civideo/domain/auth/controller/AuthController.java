package com.jjss.civideo.domain.auth.controller;

import com.jjss.civideo.domain.auth.dto.RefreshRequestDto;
import com.jjss.civideo.domain.auth.dto.TokenRequestDto;
import com.jjss.civideo.domain.auth.dto.TokenResponseDto;
import com.jjss.civideo.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping(value = "/token", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendToken(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
		TokenResponseDto tokenResponseDto = authService.createToken(tokenRequestDto.getProvider(), tokenRequestDto.getToken());
		if (tokenResponseDto == null) {
			return new ResponseEntity<>(Map.of("message", "invalid token"), HttpStatus.UNAUTHORIZED);
		}

		return ResponseEntity.ok(tokenResponseDto);
	}

	@PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendRefreshToken(@Valid @RequestBody RefreshRequestDto refreshRequestDto) {
		String refreshToken = refreshRequestDto.getRefreshToken();
		TokenResponseDto tokenResponseDto = authService.refresh(refreshToken);

		return ResponseEntity.ok(tokenResponseDto);
	}

}
