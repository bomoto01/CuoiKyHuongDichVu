package com.haui.library.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SachResponse {
    private String maSach;
    private String tenSach;
    private String tacGia;
    private Integer soLuong;
    private String maTheLoai;
    private String tenTheLoai;
}

