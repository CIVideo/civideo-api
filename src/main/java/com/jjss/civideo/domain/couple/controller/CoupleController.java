package com.jjss.civideo.domain.couple.controller;

import com.jjss.civideo.domain.couple.dto.CoupleMatchRequestDto;
import com.jjss.civideo.domain.couple.dto.CoupleMatchResponseDto;
import com.jjss.civideo.domain.couple.service.CoupleService;
import com.jjss.civideo.global.exception.ConflictDataException;
import com.jjss.civideo.global.exception.NotFoundDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<?> matchCouple(@Valid @RequestBody CoupleMatchRequestDto coupleMatchRequestDto, Authentication authentication) throws NotFoundDataException, ConflictDataException {
        Long coupleId = coupleService.matchCouple((Long) authentication.getPrincipal(), coupleMatchRequestDto.getTargetCode());

        CoupleMatchResponseDto coupleMatchResponseDto = CoupleMatchResponseDto.builder()
                .coupleId(coupleId)
                .build();

        return ResponseEntity.created(URI.create("/couple/" + coupleId)).body(coupleMatchResponseDto);
    }

}
