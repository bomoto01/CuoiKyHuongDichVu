package com.haui.library.dto.response;

import com.haui.library.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiKhoanResponse {
    private String username;
    private String tenTK;
    private String sdtTK;
    private Role role;
}

