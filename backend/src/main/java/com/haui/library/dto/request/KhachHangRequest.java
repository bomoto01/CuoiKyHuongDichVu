package com.haui.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHangRequest {
    @NotBlank(message = "Mã khách hàng không được để trống")
    private String maKH;

    @NotBlank(message = "Tên khách hàng không được để trống")
    private String tenKH;

    private String sdtKH;
}

