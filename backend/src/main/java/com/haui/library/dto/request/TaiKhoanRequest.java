package com.haui.library.dto.request;

import com.haui.library.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiKhoanRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;

    private String pass;

    private String tenTK;

    private String sdtTK;

    private Role role;
}

