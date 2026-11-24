package com.haui.borrow.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhieuMuonResponse {
    private String maMuon;
    private String maKH;
    private String tenKH;
    private String maSach;
    private String tenSach;
    private LocalDateTime ngayMuon;
    private LocalDateTime ngayTra;
    private Integer soLuong;
    private boolean daQuaHan;
}
