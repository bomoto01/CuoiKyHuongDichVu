package com.haui.library.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuGiaoDichResponse {
    private String maGD;
    private LocalDateTime ngayGD;
    private Integer soLuong;
    private String trangThai;
    private String username;
    private String tenTK;
    private String maKH;
    private String tenKH;
    private String maSach;
    private String tenSach;
}
