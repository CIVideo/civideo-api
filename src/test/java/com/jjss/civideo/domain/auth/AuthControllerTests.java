package com.jjss.civideo.domain.auth;

import com.jjss.civideo.config.BaseControllerTest;
import com.jjss.civideo.domain.auth.controller.AuthController;
import com.jjss.civideo.domain.auth.dto.RefreshRequestDto;
import com.jjss.civideo.domain.auth.dto.TokenRequestDto;
import com.jjss.civideo.domain.auth.dto.TokenResponseDto;
import com.jjss.civideo.domain.auth.service.AuthService;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
public class AuthControllerTests extends BaseControllerTest {

	@MockBean
	AuthService authService;

	@Test
	@DisplayName("[POST /auth/token] validation을 통과하는 parameter로 호출 시 200 return")
	public void sendToken_whenSendRightValue_then200() throws Exception {
		String provider = "kakao";
		String token = "real-access-token";

		String accessToken = "access-token";
		String refreshToken = "refresh-token";
		String code = "1234567890";

		TokenRequestDto tokenRequestDto = new TokenRequestDto();
		tokenRequestDto.setProvider(provider);
		tokenRequestDto.setToken(token);

		TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.code(code)
			.build();

		given(authService.createToken(provider, token)).willReturn(tokenResponseDto);

		mockMvc.perform(post("/auth/token")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(tokenRequestDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.access_token").exists())
			.andDo(document("authentication/create-token",
					requestHeaders(
						headerWithName(HttpHeaders.ACCEPT).description("application/json을 포함하는 값"),
						headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json 고정")
					),
					requestFields(
						fieldWithPath("provider").description("OAuth2 provider"),
						fieldWithPath("token").description("provider가 발급해준 access token")
					),
					responseHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json 고정")
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
	@DisplayName("[POST /auth/token] validation을 통과하지 못하는 parameter로 호출 시 400 return")
	public void sendToken_whenSendNotValidValue_then400() throws Exception {
		String provider = "??";
		String token = "idontknow";

		TokenRequestDto tokenRequestDto = new TokenRequestDto();
		tokenRequestDto.setProvider(provider);
		tokenRequestDto.setToken(token);

		mockMvc.perform(post("/auth/token")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(tokenRequestDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath("$.errors").isNotEmpty())
			.andExpect(jsonPath("$.errors[*].field").exists())
			.andExpect(jsonPath("$.errors[*].message").exists())
			.andDo(document("error/bad-request",
					requestHeaders(
						headerWithName(HttpHeaders.ACCEPT).description("application/json을 포함하는 값"),
						headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json 고정")
					),
					requestFields(
						fieldWithPath("provider").description("OAuth2 provider"),
						fieldWithPath("token").description("provider가 발급해준 access token")
					),
					responseHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json 고정")
					),
					responseFields(
						fieldWithPath("errors").type(JsonFieldType.ARRAY).description("An array of field errors"),
						fieldWithPath("errors[].field").type(JsonFieldType.STRING).description("error field"),
						fieldWithPath("errors[].message").type(JsonFieldType.STRING).description("error description")
					)
				)
			);
	}

	@Test
	@WithMockUser
	@DisplayName("[POST /auth/refresh] validation을 통과하는 parameter로 호출 시 200 return")
	public void refresh_whenSendRightValue_then200() throws Exception {
		String refreshToken = "valid-refresh-token";

		RefreshRequestDto refreshRequestDto = new RefreshRequestDto();
		refreshRequestDto.setRefreshToken(refreshToken);

		String accessToken = "access-token";
		String newRefreshToken = "new-refresh-token";

		TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
			.accessToken(accessToken)
			.refreshToken(newRefreshToken)
			.build();

		given(authService.refresh(refreshToken)).willReturn(tokenResponseDto);

		mockMvc.perform(post("/auth/refresh")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(refreshRequestDto)))
			.andExpect(status().isOk())
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.access_token").exists())
			.andExpect(jsonPath("$.refresh_token").exists())
			.andDo(document("authentication/refresh-token",
					requestHeaders(
						headerWithName(HttpHeaders.ACCEPT).description("application/json을 포함하는 값"),
						headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json 고정")
					),
					requestFields(
						fieldWithPath("refresh_token").description("만료되지 않은 refresh token")
					),
					responseHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json 고정")
					),
					responseFields(
						fieldWithPath("access_token").type(JsonFieldType.STRING).description("Server에서 발급한 access token"),
						fieldWithPath("refresh_token").type(JsonFieldType.STRING).description("Server에서 발급한 refresh token")
					)
				)
			);
	}

}
