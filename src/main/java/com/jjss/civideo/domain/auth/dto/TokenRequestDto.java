package com.jjss.civideo.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class TokenRequestDto {

    @ApiModelProperty(value = "access token", dataType = "string", example = "DdfARJxvRGjxsQIUcFmtOLuPEe_wZxr4xCt5HgorDNMAAAFs5-kqhg")
    @NotBlank(message = "access token은 필수 값입니다.")
    @NotEmpty(message = "access token값은 비어 있을 수 없습니다.")
    private String token;

    @ApiModelProperty(value = "provider", dataType = "string", example = "kakao")
    @NotBlank(message = "provider는 필수 값입니다.")
    @Pattern(regexp = "google|apple|kakao", message = "provider는 Google, Apple, Kakao 중 하나의 값이어야 합니다.")
    private String provider;

}
