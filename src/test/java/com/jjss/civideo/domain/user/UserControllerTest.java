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
	@DisplayName("[GET /user/{id}] id ?????? ???????????? user ?????? ??? 200 return")
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
						parameterWithName("id").description("User ????????? (ID)")
					),
					responseHeaders(
						headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json ??????")
					),
					responseFields(
						fieldWithPath("id").type(JsonFieldType.NUMBER).description("User ?????? ???"),
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????????").optional(),
						fieldWithPath("gender").type(JsonFieldType.STRING).description("??????").optional(),
						fieldWithPath("birth_date").type(JsonFieldType.STRING).description("??????").optional(),
						fieldWithPath("blood_type").type(JsonFieldType.STRING).description("?????????").optional(),
						fieldWithPath("mbti").type(JsonFieldType.STRING).description("MBTI").optional(),
						fieldWithPath("code").type(JsonFieldType.STRING).description("?????? ??????"),
						fieldWithPath("provider").type(JsonFieldType.STRING).description("????????? ??????")
					)
				)
			);
	}

	@Test
	@DisplayName("[DELETE /user/{id}] id ?????? ???????????? user ?????? ??? ?????? user ?????? ??? 204 return")
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
						parameterWithName("id").description("User ????????? (ID)")
					)
				)
			);
	}

	@Test
	@DisplayName("[PATCH /user/{id}] validation??? ???????????? parameter??? ?????? ??? ???????????? ??? 204 return")
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
						headerWithName(HttpHeaders.ACCEPT).description("application/json??? ???????????? ???"),
						headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json ??????")
					),
					pathParameters(
						parameterWithName("id").description("User ????????? (ID)")
					),
					requestFields(
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("????????? ?????????").optional(),
						fieldWithPath("birth_date").type(JsonFieldType.STRING).description("?????? (YYYY-MM-DD??? format?????? ?????? ???)").optional(),
						fieldWithPath("mbti").type(JsonFieldType.STRING).description("MBTI (Upper Case ????????? ???)").optional(),
						fieldWithPath("blood_type").type(JsonFieldType.STRING).description("????????? (Upper Case ????????? ???)").optional()
					)
				)
			);
	}

}
