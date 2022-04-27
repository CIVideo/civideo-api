package com.jjss.civideo.domain.couple.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CoupleMatchRequestDto {

	@NotBlank(message = "나의 code는 필수 값입니다.")
	private String myCode;

	@NotBlank(message = "상대의 code는 필수 값입니다.")
	private String yourCode;

}
