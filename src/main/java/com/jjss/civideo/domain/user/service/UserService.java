package com.jjss.civideo.domain.user.service;

import com.jjss.civideo.domain.user.dto.UserRequestDto;
import com.jjss.civideo.domain.user.dto.UserResponseDto;
import com.jjss.civideo.domain.user.entity.BloodType;
import com.jjss.civideo.domain.user.entity.Mbti;
import com.jjss.civideo.domain.user.entity.User;
import com.jjss.civideo.domain.user.repository.UserRepository;
import com.jjss.civideo.global.exception.NotFoundDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

	private final UserRepository userRepository;

	public UserResponseDto getUser(Long id) throws NotFoundDataException {
		User user = userRepository.findById(id).orElseThrow(() -> new NotFoundDataException("user_id", id, "해당 ID에 해당하는 유저를 찾을 수 없습니다."));
		return user.toUserResponseDto();
	}

	public void updateUser(Long id, UserRequestDto userRequestDto) throws NotFoundDataException {
		User user = userRepository.findById(id).orElseThrow(() -> new NotFoundDataException("user_id", id, "해당 ID에 해당하는 유저를 찾을 수 없습니다."));

		String mbti = userRequestDto.getMbti();
		if (mbti != null) {
			user.setMbti(Mbti.valueOf(mbti));
		}

		LocalDate birthDate = userRequestDto.getBirthDate();
		if (birthDate != null) {
			user.setBirthDate(birthDate);
		}

		String bloodType = userRequestDto.getBloodType();
		if (bloodType != null) {
			user.setBloodType(BloodType.valueOf(bloodType));
		}

		String nickname = userRequestDto.getNickname();
		if (nickname != null) {
			user.setNickname(nickname);
		}
	}

}
