package com.jjss.civideo.domain.couple.controller;

import com.jjss.civideo.domain.couple.dto.CoupleMatchRequestDto;
import com.jjss.civideo.domain.couple.dto.CoupleMatchResponseDto;
import com.jjss.civideo.domain.couple.service.CoupleService;
import com.jjss.civideo.global.exception.ConflictDataException;
import com.jjss.civideo.global.exception.ForbiddenException;
import com.jjss.civideo.global.exception.NotFoundDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/couple", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CoupleController {

	private final CoupleService coupleService;

	@PostMapping(value = "/match", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> matchCouple(@Valid @RequestBody CoupleMatchRequestDto coupleMatchRequestDto) throws NotFoundDataException, ConflictDataException, ForbiddenException {
		String myCode = coupleMatchRequestDto.getMyCode();
		String yourCode = coupleMatchRequestDto.getYourCode();

		Long coupleId = coupleService.matchCouple(myCode, yourCode);

		CoupleMatchResponseDto coupleMatchResponseDto = CoupleMatchResponseDto.builder()
			.coupleId(coupleId)
			.build();

		return ResponseEntity.created(URI.create("/couple/" + coupleId)).body(coupleMatchResponseDto);
	}

}
