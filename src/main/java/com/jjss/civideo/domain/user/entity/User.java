package com.jjss.civideo.domain.user.entity;

import com.jjss.civideo.domain.couple.entity.Couple;
import com.jjss.civideo.domain.user.dto.UserResponseDto;
import com.jjss.civideo.global.config.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.time.LocalDate;
import java.util.Random;

@SequenceGenerator(name = "USER_SEQ_GENERATOR", sequenceName = "USER_SEQUENCES")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GENERATOR")
	@Column(name = "user_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "couple_id")
	private Couple couple;

	@Column(nullable = false, unique = true, updatable = false)
	private String providerId;

	@Setter
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Setter
	private Gender gender;

	@Setter
	private LocalDate birthDate;

	@Enumerated(EnumType.STRING)
	@Setter
	private BloodType bloodType;

	@Enumerated(EnumType.STRING)
	@Setter
	private Mbti mbti;

	@Column(columnDefinition = "CHAR(10)", nullable = false, updatable = false, unique = true)
	private String code;

	@Column(nullable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	private Provider provider;

	@Builder
	private User(String providerId, Provider provider) {
		this.providerId = providerId;
		this.provider = provider;
		this.code = createCode();
	}

	public UserResponseDto toUserResponseDto() {
		return UserResponseDto.builder()
			.id(id)
			.birthDate(birthDate)
			.bloodType(bloodType)
			.code(code)
			.nickname(nickname)
			.gender(gender)
			.mbti(mbti)
			.provider(provider)
			.build();
	}

	public void match(Couple couple) {
		this.couple = couple;
	}

	private String createCode() {
		int leftLimit = 48;
		int rightLimit = 122;
		int targetStringLength = 10;

		return new Random().ints(leftLimit, rightLimit + 1)
			.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
			.limit(targetStringLength)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
	}

}
