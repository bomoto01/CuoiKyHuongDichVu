package com.haui.account.dto.response;

import com.haui.common.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
    private boolean authenticated;
    private String username;
    private String tenTK;
    private Role role;
}
