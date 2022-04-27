package com.jjss.civideo.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jjss.civideo.domain.user.entity.BloodType;
import com.jjss.civideo.domain.user.entity.Mbti;
import com.jjss.civideo.global.validation.Enum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
public class UserRequestDto {

	private String nickname;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past(message = "생일은 현재 이전의 날짜여야 합니다.")
	private LocalDate birthDate;

	@Enum(enumClass = BloodType.class, message = "혈액형은 A, B, O, AB 중 하나여야 합니다.", nullable = true)
	private String bloodType;

	@Enum(enumClass = Mbti.class, message = "올바른 형태의 MBTI가 아닙니다.", nullable = true)
	private String mbti;

}
