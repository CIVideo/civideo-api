package com.jjss.civideo.domain.auth;

import com.jjss.civideo.config.BaseControllerTest;
import com.jjss.civideo.domain.auth.dto.TokenRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class AuthControllerTests extends BaseControllerTest {

    @Test
    public void sendToken_SendRightValue_200() throws Exception {
        TokenRequestDto tokenRequestDto = new TokenRequestDto();
        tokenRequestDto.setProvider("kakao");
        tokenRequestDto.setToken("");

        mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("")))
                .andDo(document("create-token",
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("Accept header"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                                ),
                                requestFields(
                                        fieldWithPath("provider").description("OAuth2 provider"),
                                        fieldWithPath("token").description("provider가 발급해준 access token")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                                ),
                                responseFields(
                                        fieldWithPath("access_token").description("Server에서 발급한 access token")
                                )
                        )
                );
    }

    @Test
    public void sendToken_SendEmptyValue_400() {

    }

}
