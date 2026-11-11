package com.haui.library.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SachRequest {
    @NotBlank(message = "Mã sách không được để trống")
    private String maSach;

    @NotBlank(message = "Tên sách không được để trống")
    private String tenSach;

    private String tacGia;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer soLuong;

    @NotBlank(message = "Mã thể loại không được để trống")
    private String maTheLoai;
}

