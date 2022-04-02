package com.jjss.civideo.domain.user;

import com.jjss.civideo.config.BaseControllerTest;
import com.jjss.civideo.domain.user.controller.UserController;
import com.jjss.civideo.domain.user.dto.TokenRequestDto;
import com.jjss.civideo.domain.user.service.UserService;
import com.jjss.civideo.global.config.auth.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@MockBean(UserService.class)
public class UserControllerTests extends BaseControllerTest {

//    @Test
//    public void sendToken_SendRightValue_200() throws Exception {
//        TokenRequestDto tokenRequestDto = new TokenRequestDto();
//        tokenRequestDto.setProvider("kakao");
//        tokenRequestDto.setToken("");
//
//        mockMvc.perform(get("/auth/token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString("")))
//                .andExpect(status().isBadRequest())
//                .andDo(document("create-token-200",
//                                requestHeaders(
//                                        headerWithName(HttpHeaders.ACCEPT).description("Accept header"),
//                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
//                                ),
//                                requestFields(
//                                        fieldWithPath("provider").type(JsonFieldType.STRING).description("OAuth2 provider"),
//                                        fieldWithPath("token").type(JsonFieldType.STRING).description("provider가 발급해준 access token")
//                                ),
//                                responseHeaders(
//                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
//                                ),
//                                responseFields(
//                                        fieldWithPath("access_token").type(JsonFieldType.STRING).description("Server에서 발급한 access token")
//                                )
//                        )
//                );
//    }

    @Test
    @WithMockUser
    public void sendToken_SendEmptyValue_400() throws Exception {
        TokenRequestDto tokenRequestDto = new TokenRequestDto();
        tokenRequestDto.setProvider("");
        tokenRequestDto.setToken("");

        mockMvc.perform(get("/auth/token")
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
                                        fieldWithPath("provider").type(JsonFieldType.STRING).description("OAuth2 provider"),
                                        fieldWithPath("token").type(JsonFieldType.STRING).description("provider가 발급해준 access token")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                                ),
                                responseFields(
                                        fieldWithPath("errors").type(JsonFieldType.OBJECT).description("errors object"),
                                        fieldWithPath("errors[]").type(JsonFieldType.ARRAY).description("An array of field errors"),
                                        fieldWithPath("errors[].field").type(JsonFieldType.STRING).description("error field"),
                                        fieldWithPath("errors[].message").type(JsonFieldType.STRING).description("error description")
                                )
                        )
                );
    }

}
