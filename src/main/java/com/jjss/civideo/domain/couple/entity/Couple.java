package com.jjss.civideo.domain.couple.entity;

import com.jjss.civideo.global.config.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.time.LocalDate;

@SequenceGenerator(name = "COUPLE_SEQ_GENERATOR", sequenceName = "COUPLE_SEQUENCES")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Couple extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COUPLE_SEQ_GENERATOR")
	@Column(name = "couple_id")
	private Long id;

	private LocalDate beginDate;

	@Builder
	private Couple(LocalDate beginDate) {
		this.beginDate = beginDate;
	}

}
