package com.crewspace.auth.dto.req;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReIssueRequestDTO {
    @NotNull(message = "accessToken은 필수입니다!")
    private String accessToken;
    @NotNull(message = "refreshToken은 필수입니다!")
    private String refreshToken;
}
