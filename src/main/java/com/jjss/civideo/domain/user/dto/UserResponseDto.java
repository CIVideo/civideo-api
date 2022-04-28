package com.jjss.civideo.domain.user.dto;

import com.jjss.civideo.domain.user.entity.BloodType;
import com.jjss.civideo.domain.user.entity.Gender;
import com.jjss.civideo.domain.user.entity.Mbti;
import com.jjss.civideo.domain.user.entity.Provider;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserResponseDto {

	private Long id;
	private String nickname;
	private Gender gender;
	private LocalDate birthDate;
	private BloodType bloodType;
	private Mbti mbti;
	private String code;
	private Provider provider;

}
