package com.jjss.civideo.domain.couple;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Couple {

    @Id
    @GeneratedValue
    @Column(name = "couple_id")
    private Long id;

    private LocalDate beginDate;

}
