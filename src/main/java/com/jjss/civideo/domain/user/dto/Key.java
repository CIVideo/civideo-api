package com.jjss.civideo.domain.user.dto;

import lombok.Getter;

@Getter
public class Key {

    private String kty;
    private String kid;
    private String use;
    private String alg;
    private String e;
    private String n;

}
