package com.jjss.civideo.domain.auth.dto;

import io.swagger.annotations.ApiModelProperty;

public class TokenResponseDto {

    @ApiModelProperty(value = "access token", dataType = "string", required = true, example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.hjQ4oomXpzjuXTSj5Th7CsDbBvLbO-WlhgWDdVayRNU")
    private String accessToken;

}
