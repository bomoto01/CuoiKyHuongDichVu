package com.haui.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheLoaiRequest {
    @NotBlank(message = "Mã thể loại không được để trống")
    private String maTheLoai;

    @NotBlank(message = "Tên thể loại không được để trống")
    private String tenTheLoai;
}
