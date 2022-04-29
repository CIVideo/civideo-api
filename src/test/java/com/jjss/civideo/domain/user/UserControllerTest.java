package com.jjss.civideo.domain.user;

import com.jjss.civideo.config.BaseControllerTest;
import com.jjss.civideo.domain.user.dto.UserResponseDto;
import com.jjss.civideo.domain.user.entity.BloodType;
import com.jjss.civideo.domain.user.entity.Mbti;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.oneOf;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
	@DisplayName("[GET /user/{id}] id 값에 해당하는 user 존재 시 200 return")
	public void getUser_whenExistUser_then200() throws Exception {
		User mockUser = User.builder()
			.provider(Provider.GOOGLE)
			.providerId("1175699863314647947")
			.build();
		mockUser.setNickname("111");
		mockUser.setBloodType(BloodType.O);
		mockUser.setBirthDate(LocalDate.of(2022, 4, 27));
		mockUser.setMbti(Mbti.ENFJ);

		UserResponseDto userResponseDto = mockUser.toUserResponseDto();
		userResponseDto.setId(1L);
		given(userService.getUser(1L)).willReturn(userResponseDto);

		mockMvc.perform(get("/user/{id}", 1L)
				.header(HttpHeaders.AUTHORIZATION, "Bearer ${access_token}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").isNumber())
			.andExpect(jsonPath("$.code").isString())
			.andExpect(jsonPath("$.provider").value(oneOf("GOOGLE", "KAKAO", "APPLE")))
			.andDo(document("user/get-user",
					requestHeaders(
						headerWithName(HttpHeaders.AUTHORIZATION).description("access token")
					),
					pathParameters(
						parameterWithName("id").description("User 식별값 (ID)")
					),
					responseHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json 고정")
					),
					responseFields(
						fieldWithPath("id").type(JsonFieldType.NUMBER).description("User 식별 값"),
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임").optional(),
						fieldWithPath("gender").type(JsonFieldType.STRING).description("성별").optional(),
						fieldWithPath("birth_date").type(JsonFieldType.STRING).description("생일").optional(),
						fieldWithPath("blood_type").type(JsonFieldType.STRING).description("혈액형").optional(),
						fieldWithPath("mbti").type(JsonFieldType.STRING).description("MBTI").optional(),
						fieldWithPath("code").type(JsonFieldType.STRING).description("개인 코드"),
						fieldWithPath("provider").type(JsonFieldType.STRING).description("로그인 타입")
					)
				)
			);
	}

	@Test
	@DisplayName("[DELETE /user/{id}] id 값에 해당하는 user 존재 시 해당 user 삭제 후 204 return")
	public void deleteUser_whenSendRightValue_then204() throws Exception {
		mockMvc.perform(delete("/user/{id}", 1L)
				.header(HttpHeaders.AUTHORIZATION, "Bearer ${access_token}"))
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$").doesNotExist())
			.andDo(document("user/delete-user",
					requestHeaders(
						headerWithName(HttpHeaders.AUTHORIZATION).description("access token")
					),
					pathParameters(
						parameterWithName("id").description("User 식별값 (ID)")
					)
				)
			);
	}

	@Test
	@DisplayName("[PATCH /user/{id}] validation을 통과하는 parameter로 호출 시 업데이트 후 204 return")
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
				.header(HttpHeaders.AUTHORIZATION, "Bearer ${access_token}")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(userRequestDto.toString()))
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$").doesNotExist())
			.andDo(document("user/update-user",
					requestHeaders(
						headerWithName(HttpHeaders.AUTHORIZATION).description("access token"),
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
