package com.jjss.civideo.domain.user;

import com.jjss.civideo.config.BaseControllerTest;
import com.jjss.civideo.domain.user.controller.UserController;
import com.jjss.civideo.domain.user.dto.TokenResponseDto;
import com.jjss.civideo.domain.user.service.UserService;
import com.jjss.civideo.global.config.auth.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
public class UserControllerTests extends BaseControllerTest {

    @MockBean
    UserService userService;

    @Test
    @WithMockUser
    @DisplayName("[GET /auth/token] validation을 통과하는 parameter로 호출 시 200 return")
    public void sendToken_whenSendRightValue_then200() throws Exception {
        String provider = "kakao";
        String token = "real-access-token";

        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        String code = "1234567890";

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .code(code)
                .build();

        when(userService.createToken(provider, token)).thenReturn(tokenResponseDto);

        mockMvc.perform(get("/auth/token")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("provider", provider)
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andDo(document("authentication/create-token",
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("Accept header")
                                ),
                                requestParameters(
                                        parameterWithName("provider").description("OAuth2 provider"),
                                        parameterWithName("token").description("provider가 발급해준 access token")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                                ),
                                responseFields(
                                        fieldWithPath("access_token").type(JsonFieldType.STRING).description("Server에서 발급한 access token"),
                                        fieldWithPath("refresh_token").type(JsonFieldType.STRING).description("Server에서 발급한 refresh token"),
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("user에게 발급하는 random generated token (user code)")
                                )
                        )
                );
    }


    @Test
    @WithMockUser
    @DisplayName("[GET /auth/token] validation을 통과하지 못하는 parameter로 호출 시 400 return")
    public void sendToken_whenSendNotValidValue_then400() throws Exception {
        String provider = "??";
        String token = "idontknow";

        mockMvc.perform(get("/auth/token")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("provider", provider)
                        .param("token", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors[*].field").exists())
                .andExpect(jsonPath("$.errors[*].message").exists())
                .andDo(document("error/bad-request",
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("Accept header")
                                ),
                                requestParameters(
                                        parameterWithName("provider").description("OAuth2 provider"),
                                        parameterWithName("token").description("provider가 발급해준 access token")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                                ),
                                responseFields(
                                        fieldWithPath("errors").type(JsonFieldType.ARRAY).description("An array of field errors"),
                                        fieldWithPath("errors[].field").type(JsonFieldType.STRING).description("error field"),
                                        fieldWithPath("errors[].message").type(JsonFieldType.STRING).description("error description")
                                )
                        )
                );
    }

//    @Test
//    @DisplayName("[GET /auth/token] 인증 처리가 되지 않은 상태에서 호출 시 401 return")
//    public void sendToken_whenUnauthorized_then401() throws Exception {
//        String provider = "??";
//        String token = "idontknow";
//
//        mockMvc.perform(get("/auth/token")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .param("provider", provider)
//                        .param("token", token))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.request_url").exists())
//                .andExpect(jsonPath("$.message").exists())
//                .andDo(document("error/unauthorized",
//                                requestHeaders(
//                                        headerWithName(HttpHeaders.ACCEPT).description("Accept header")
//                                ),
//                                requestParameters(
//                                        parameterWithName("provider").description("OAuth2 provider"),
//                                        parameterWithName("token").description("provider가 발급해준 access token")
//                                ),
//                                responseHeaders(
//                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
//                                ),
//                                responseFields(
//                                        fieldWithPath("request_url").type(JsonFieldType.STRING).description("요청 URI"),
//                                        fieldWithPath("message").type(JsonFieldType.STRING).description("error description")
//                                )
//                        )
//                );
//    }

}
