package com.haui.book.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SachResponse {
    private String maSach;
    private String tenSach;
    private Integer soLuong;
    private String tacGia;
    private String maTheLoai;
    private String tenTheLoai;
}
