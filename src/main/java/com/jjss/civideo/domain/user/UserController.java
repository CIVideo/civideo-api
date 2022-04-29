package com.jjss.civideo.domain.user;

import com.jjss.civideo.domain.user.dto.UserRequestDto;
import com.jjss.civideo.domain.user.dto.UserResponseDto;
import com.jjss.civideo.domain.user.service.UserService;
import com.jjss.civideo.global.exception.ForbiddenException;
import com.jjss.civideo.global.exception.NotFoundDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id) throws NotFoundDataException {
		UserResponseDto userResponseDto = userService.getUser(id);
		return ResponseEntity.ok(userResponseDto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> updateUser(@PathVariable Long id) throws NotFoundDataException, ForbiddenException {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto userRequestDto) throws NotFoundDataException {
		userService.updateUser(id, userRequestDto);
		return ResponseEntity.noContent().build();
	}

}
