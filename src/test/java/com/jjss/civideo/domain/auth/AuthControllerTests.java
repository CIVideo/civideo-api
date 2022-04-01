package com.jjss.civideo.domain.auth;

import com.jjss.civideo.config.BaseControllerTest;
import com.jjss.civideo.domain.auth.controller.UserController;
import com.jjss.civideo.domain.auth.dto.TokenRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
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
                .andExpect(status().isBadRequest())
                .andDo(document("create-token-200",
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
    public void sendToken_SendEmptyValue_400() throws Exception {
        TokenRequestDto tokenRequestDto = new TokenRequestDto();
        tokenRequestDto.setProvider("");
        tokenRequestDto.setToken("");

        mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRequestDto)))
                .andExpect(status().isBadRequest())
                .andDo(document("create-token-400",
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
                                        fieldWithPath("[]").description("An array of field errors"),
                                        fieldWithPath("[].field").description("error field"),
                                        fieldWithPath("[].description").description("error description")
                                )
                        )
                );
    }

}
