package com.jjss.civideo.domain.mobile;

import com.jjss.civideo.config.BaseControllerTest;
import com.jjss.civideo.domain.mobile.controller.MobileController;
import com.jjss.civideo.global.config.auth.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MobileController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
public class MobileControllerTests extends BaseControllerTest {

    @Test
    @WithMockUser
    @DisplayName("[GET /ui/tapbar] 200 return")
    public void tapBar_whenSendRightValue_then200() throws Exception {
        String version = "1.0.0";

        mockMvc.perform(get("/ui/tapbar")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("version", version))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andDo(document("mobile/tap-bar",
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("Accept header")
                                ),
                                requestParameters(
                                        parameterWithName("version").description("Mobile App Version")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                                ),
                                responseFields(
                                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("Mobile 하단 tap text 정보")
                                )
                        )
                );
    }

}
