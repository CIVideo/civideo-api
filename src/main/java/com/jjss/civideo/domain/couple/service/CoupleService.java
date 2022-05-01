package com.jjss.civideo.domain.couple.service;

import com.jjss.civideo.domain.couple.entity.Couple;
import com.jjss.civideo.domain.couple.repository.CoupleRepository;
import com.jjss.civideo.domain.user.entity.User;
import com.jjss.civideo.domain.user.repository.UserRepository;
import com.jjss.civideo.global.exception.ConflictDataException;
import com.jjss.civideo.global.exception.ForbiddenException;
import com.jjss.civideo.global.exception.NotFoundDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {NotFoundDataException.class, ConflictDataException.class})
public class CoupleService {

	private final UserRepository userRepository;
	private final CoupleRepository coupleRepository;

	public Long matchCouple(String myCode, String yourCode) throws NotFoundDataException, ConflictDataException, ForbiddenException {
		User me = userRepository.findByCode(myCode).orElseThrow(() -> new NotFoundDataException("해당 code에 해당하는 유저를 찾을 수 없습니다."));
		Long authenticatedUserId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!me.getId().equals(authenticatedUserId)) {
			throw new ForbiddenException("해당 code를 가진 유저를 update할 권한이 없습니다.");
		}

		User you = userRepository.findByCode(yourCode).orElseThrow(() -> new NotFoundDataException("해당 code에 해당하는 유저를 찾을 수 없습니다."));

//		if (me.getCouple() != null) {
//			throw new ConflictDataException("해당 code에 해당하는 유저는 이미 커플 매치가 된 유저입니다.");
//		}
//		if (you.getCouple() != null) {
//			throw new ConflictDataException("해당 code에 해당하는 유저는 이미 커플 매치가 된 유저입니다.");
//		}

		Couple couple = Couple.builder()
			.build();

		me.match(couple);
		you.match(couple);

		return coupleRepository.save(couple).getId();
	}

}
