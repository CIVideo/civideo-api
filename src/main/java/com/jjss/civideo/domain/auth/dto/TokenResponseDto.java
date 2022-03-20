package com.jjss.civideo.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Token Response DTO")
@Getter
@Builder
public class TokenResponseDto {

    @Schema(description = "JWT Token",  example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.hjQ4oomXpzjuXTSj5Th7CsDbBvLbO-WlhgWDdVayRNU")
    private String accessToken;

}
