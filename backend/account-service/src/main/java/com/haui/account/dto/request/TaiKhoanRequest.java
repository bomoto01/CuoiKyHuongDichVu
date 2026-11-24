package com.haui.account.dto.request;

import com.haui.common.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiKhoanRequest {
    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String pass;

    @NotBlank(message = "Tên tài khoản không được để trống")
    private String tenTK;

    private String sdtTK;

    @NotNull(message = "Vai trò (Role) không được để trống")
    private Role role;
}
