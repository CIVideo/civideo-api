package com.jjss.civideo.domain.user.controller;

import com.jjss.civideo.config.BaseControllerTest;
import com.jjss.civideo.domain.user.entity.Provider;
import com.jjss.civideo.domain.user.entity.User;
import com.jjss.civideo.domain.user.repository.UserRepository;
import com.jjss.civideo.domain.user.service.UserService;
import com.jjss.civideo.global.config.auth.SecurityConfig;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class UserControllerTest extends BaseControllerTest {

	@MockBean
	UserService userService;

	@Mock
	UserRepository userRepository;

	@Test
	@DisplayName("[PATCH /user/{id}] validation을 통과하는 parameter로 호출 시 204 return")
	public void updateUser_whenSendRightValue_then204() throws Exception {
		JSONObject userRequestDto = new JSONObject()
			.put("mbti", "ENFJ")
			.put("birth_date", "2022-04-26");

		User mockUser = User.builder()
			.provider(Provider.GOOGLE)
			.providerId("1175699863314647947")
			.build();
		given(userRepository.findById(1L)).willReturn(Optional.of(mockUser));

		mockMvc.perform(patch("/user/{id}", 1L)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(userRequestDto.toString()))
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$").doesNotExist())
			.andDo(document("user/update-user",
					requestHeaders(
						headerWithName(HttpHeaders.ACCEPT).description("application/json을 포함하는 값"),
						headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json 고정")
					),
					pathParameters(
						parameterWithName("id").description("User 식별값 (ID)")
					),
					requestFields(
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용할 닉네임").optional(),
						fieldWithPath("birth_date").type(JsonFieldType.STRING).description("생일 (YYYY-MM-DD의 format으로 보낼 것)").optional(),
						fieldWithPath("mbti").type(JsonFieldType.STRING).description("MBTI (Upper Case 사용할 것)").optional(),
						fieldWithPath("blood_type").type(JsonFieldType.STRING).description("혈액형 (Upper Case 사용할 것)").optional()
					)
				)
			);
	}

}
