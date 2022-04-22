package com.jjss.civideo.domain.couple;

import com.jjss.civideo.config.BaseControllerTest;
import com.jjss.civideo.domain.couple.controller.CoupleController;
import com.jjss.civideo.domain.couple.dto.CoupleMatchRequestDto;
import com.jjss.civideo.domain.couple.service.CoupleService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CoupleController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class CoupleControllerTests extends BaseControllerTest {

    @MockBean
    CoupleService coupleService;

    @Test
    @DisplayName("[POST /couple/match] 정상 호출 시 200 return")
    public void match_whenSendRightValue_then200() throws Exception {
        String code = "VavL3kO8vj";

        Principal principal = new UsernamePasswordAuthenticationToken(1L, "", Set.of(new SimpleGrantedAuthority("USER")));

        CoupleMatchRequestDto coupleMatchRequestDto = new CoupleMatchRequestDto();
        coupleMatchRequestDto.setTargetCode(code);

        given(coupleService.matchCouple(1L, code)).willReturn(1L);

        mockMvc.perform(post("/couple/match")
                        .principal(principal)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coupleMatchRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.couple_id").exists())
                .andDo(document("couple/match",
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("application/json을 포함하는 값"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json 고정")
                                ),
                                requestFields(
                                        fieldWithPath("target_code").description("상대의 개인 코드")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json 고정")
                                ),
                                responseFields(
                                        fieldWithPath("couple_id").type(JsonFieldType.NUMBER).description("매치된 couple 식별 값")
                                )
                        )
                );
    }

}