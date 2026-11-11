package com.haui.library.dto.response;

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
    private String sdtKH;
    private String maSach;
    private String tenSach;
    private String tacGia;
    private String theLoai;
    private LocalDateTime ngayMuon;
    private LocalDateTime ngayTra;
    private Integer soLuong;
    private Boolean daQuaHan;
}

