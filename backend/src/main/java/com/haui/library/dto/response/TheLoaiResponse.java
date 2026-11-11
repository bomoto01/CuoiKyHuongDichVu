package com.haui.library.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheLoaiResponse {
    private String maTheLoai;
    private String tenTheLoai;
    private Integer soLuongDauSach;
}

