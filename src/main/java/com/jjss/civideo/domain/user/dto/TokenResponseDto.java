package com.jjss.civideo.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponseDto {

    private final String accessToken;

    private final String refreshToken;

    private final String code;

}
