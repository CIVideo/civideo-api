package com.jjss.civideo.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RefreshRequestDto {

    @NotBlank(message = "refresh token은 비어 있을 수 없습니다.")
    private String refreshToken;

}
