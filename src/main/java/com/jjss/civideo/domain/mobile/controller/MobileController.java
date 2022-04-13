package com.jjss.civideo.domain.mobile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/ui", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MobileController {

    @GetMapping("/tapbar")
    public ResponseEntity<List<String>> tapBar(String version) {
        return ResponseEntity.ok(List.of("알림", "My"));
    }

}
